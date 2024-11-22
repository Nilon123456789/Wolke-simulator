package com.e24.wolke.application;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.simulation.SimulationConstants;
import com.e24.wolke.backend.simulation.physics.ParticleMatrix;
import com.e24.wolke.eventsystem.Subject;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.glsl.ShaderCode;
import java.nio.FloatBuffer;

/**
 * Canvas OpenGL pour l'affichage de la simulation.
 *
 * @author adrienles
 */
public class OpenGLCanvas extends GLCanvas implements GLEventListener {
  /** Numéro de sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application. */
  private Controller controller;

  /** L'animateur de l'application. */
  private Animator animator;

  /** L'identifiant du programme de shader. */
  private int shaderProgram;

  /** L'identifiant du "vertex array object" (VAO). */
  private int vao;

  /** L'identifiant du "vertex buffer object" (VBO). */
  private int vbo;

  /** L'identifiant de la texture de la simulation */
  private int texture;

  /** La largeur de la fenêtre de rendu. */
  private int width = 640;

  /** La hauteur de la fenêtre de rendu. */
  private int height = 360;

  /** Indique si une nouvelle image est arrivée afin de la convertir en texture */
  private boolean newFrame = false;

  /** L'objet de tampon de doubles pour les couleurs. */
  private FloatBuffer simBuffer;

  /** L'emplacement de la résolution dans le shader. */
  private int resolutionLocation;

  /** Les coordonnées des sommets du quad plein-écran. */
  private static final float[] QUAD_VERTICES = {
    -1.0f, 1.0f, // Haut gauche
    -1.0f, -1.0f, // Bas gauche
    1.0f, 1.0f, // Haut droit
    1.0f, -1.0f // Bas droit
  };

  /** l'écart de nouvelle images arrivées avant de print le fps */
  private int frameCount = 0;

  /**
   * Charge et compile un shader à partir d'un fichier et retourne son identifiant.
   *
   * @param gl Le contexte OpenGL
   */
  private void generateTextureFromCurrentFrame(GL4 gl) {
    if (!this.newFrame) {
      return;
    }
    gl.glBindTexture(GL4.GL_TEXTURE_2D, this.texture);

    // Récupération de la matrice de particules
    ParticleMatrix particleMatrix =
        this.controller.getSimulationModel().getSimulationData().pollRenderParticleMatrix();
    if (particleMatrix == null) {
      particleMatrix =
          this.controller.getSimulationModel().getSimulationData().getCurrentParticleMatrix();
    }

    this.simBuffer.clear();
    // Remplissage du tampon de particules et conversion en float
    int length = particleMatrix.getXLength() * particleMatrix.getYLength();
    for (int i = 0; i < length; i++) {
      this.simBuffer.put(
          (float)
              particleMatrix.getParticleByVisualization(
                  i, this.controller.getRendererModel().getCurrentVisualizationType()));
    }
    this.simBuffer.rewind();

    // Mise à jour de la texture avec les nouvelles données
    gl.glTexSubImage2D(
        GL4.GL_TEXTURE_2D,
        0,
        0,
        0,
        particleMatrix.getXLength(),
        particleMatrix.getYLength(),
        GL4.GL_RED,
        GL4.GL_FLOAT,
        this.simBuffer);

    // Retour de la matrice de particules dans le pool
    this.controller
        .getSimulationModel()
        .getSimulationData()
        .returnParticleMatrixToPool(particleMatrix);

    this.newFrame = false;
  }

  /**
   * Initialisation de l'application OpenGL.
   *
   * @param drawable Le contexte OpenGL animé
   */
  @Override
  public void init(GLAutoDrawable drawable) {
    // Setup de l'animateur pour le rendu
    GL4 gl = drawable.getGL().getGL4();

    // Active la synchronisation verticale (VSync)
    gl.setSwapInterval(0);

    int[] tmp = new int[1];

    // Création du VAO
    gl.glGenVertexArrays(1, tmp, 0);
    this.vao = tmp[0];
    gl.glBindVertexArray(this.vao);

    // Création du VBO
    gl.glGenBuffers(1, tmp, 0);
    this.vbo = tmp[0];
    gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, this.vbo);

    // Chargement des données des sommets du quad plein-écran
    gl.glBufferData(
        GL4.GL_ARRAY_BUFFER,
        (long) (long) QUAD_VERTICES.length * Float.BYTES,
        Buffers.newDirectFloatBuffer(OpenGLCanvas.QUAD_VERTICES),
        GL4.GL_STATIC_DRAW);

    // Configuration de l'attribut de sommet
    gl.glVertexAttribPointer(0, 2, GL4.GL_FLOAT, false, 0, 0);
    gl.glEnableVertexAttribArray(0);

    // Création de l'emplacement de la texture et association
    int[] textures = new int[1];
    gl.glGenTextures(1, textures, 0);
    this.texture = textures[0];

    // Création du tampon de particules
    this.simBuffer =
        Buffers.newDirectFloatBuffer(
            SimulationConstants.DEFAULT_MATRIX_SIZE_X * SimulationConstants.DEFAULT_MATRIX_SIZE_Y);

    // Création de la texture
    gl.glBindTexture(GL4.GL_TEXTURE_2D, this.texture);

    gl.glTexImage2D(
        GL4.GL_TEXTURE_2D,
        0,
        GL4.GL_R32F,
        SimulationConstants.DEFAULT_MATRIX_SIZE_X,
        SimulationConstants.DEFAULT_MATRIX_SIZE_Y,
        0,
        GL4.GL_RED,
        GL4.GL_FLOAT,
        this.simBuffer);

    // Filtrage de la texture
    gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
    gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);

    // Chargement du shader
    ShaderCode fragmentShader =
        ShaderCode.create(
            gl,
            GL4.GL_FRAGMENT_SHADER,
            getClass(),
            "shaders",
            "shaders/bin",
            "simulation_grid_fragment",
            false);
    fragmentShader.compile(gl, System.err);

    // Création et liaison du programme de shader
    this.shaderProgram = gl.glCreateProgram();
    gl.glAttachShader(this.shaderProgram, fragmentShader.id());
    gl.glLinkProgram(this.shaderProgram);
    gl.glUseProgram(this.shaderProgram);

    // Liaison de la texture au shader par uniform
    int textureLocation = gl.glGetUniformLocation(this.shaderProgram, "u_texture");
    gl.glUniform1i(textureLocation, 0);

    this.resolutionLocation = gl.glGetUniformLocation(this.shaderProgram, "u_resolution");
    gl.glUniform2f(this.resolutionLocation, width, height);

    // Destruction du shader et du programme qui ne sont plus nécessaires
    gl.glDeleteShader(fragmentShader.id());
    gl.glDeleteProgram(this.shaderProgram);
  }

  /**
   * Affichage et mise à jour de l'affichage de l'application OpenGL.
   *
   * @param drawable Le contexte OpenGL animé
   */
  @Override
  public void display(GLAutoDrawable drawable) {
    GL4 gl = drawable.getGL().getGL4();

    if (frameCount % 1000 == 0) {
      if (!this.controller.getSimulationModel().isRunning()) return;
      System.out.println("FPS: " + drawable.getAnimator().getLastFPS());
    }

    // Mise à jour de la résolution
    gl.glUniform2f(this.resolutionLocation, width, height);

    // Génération de la texture à partir de la matrice de particules
    generateTextureFromCurrentFrame(gl);

    // Dessin du quad plein-écran
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glBindVertexArray(this.vao);
    gl.glDrawArrays(GL4.GL_TRIANGLE_STRIP, 0, 4);

    // Échange des buffers
    gl.glFlush();
  }

  /**
   * Méthode appelée lors du changement de la taille de la fenêtre.
   *
   * @param drawable Le contexte OpenGL animé
   * @param x La position en x de la fenêtre
   * @param y La position en y de la fenêtre
   * @param width La largeur de la fenêtre
   * @param height La hauteur de la fenêtre
   */
  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    this.width = width;
    this.height = height;
  }

  /** Méthode appelée lors de la destruction de l'application OpenGL. */
  @Override
  public void dispose(GLAutoDrawable drawable) {
    GL4 gl = drawable.getGL().getGL4();

    // Destruction de l'ensemble des ressources OpenGL allouées
    gl.glDeleteVertexArrays(1, new int[] {this.vao}, 0);
    gl.glDeleteBuffers(1, new int[] {this.vbo}, 0);
    gl.glDeleteTextures(1, new int[] {this.texture}, 0);
    gl.glDeleteProgram(this.shaderProgram);
  }

  /**
   * Constructeur de la classe OpenGLCanvas.
   *
   * @param controller Le {@code Controller} de l'application
   * @param glCapabilities Les capacités OpenGL de l'appareil
   */
  public OpenGLCanvas(Controller controller, GLCapabilities glCapabilities) {
    super(glCapabilities);
    addGLEventListener(this);
    this.controller = controller;

    // Initialisation et configuration de l'animateur
    this.animator = new Animator(this);
    this.animator.setRunAsFastAsPossible(true);
    this.animator.setUpdateFPSFrames(2, null);
    this.animator.start();

    setupSubscribers();
  }

  /** Initialisation des abonnements de la classe */
  private void setupSubscribers() {
    controller
        .getRendererModel()
        .getSubscriber()
        .subscribe(
            Subject.ON_BUFFER_IMAGE_DONE,
            msg -> {
              newFrame = true;
              this.frameCount++;
            });
    controller
        .getRendererModel()
        .getSubscriber()
        .subscribe(
            Subject.ON_SIMULATION_VISUALIZATION_TYPE_CHANGED,
            msg -> {
              newFrame = true;
              this.frameCount++;
            });
  }
}
