package com.e24.wolke.backend.models.renderer;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.WModelSaveable;
import com.e24.wolke.backend.models.obstacles.WObstacleCellMatrix;
import com.e24.wolke.backend.models.renderer.RendererConstants.VisualizationType;
import com.e24.wolke.backend.simulation.physics.ParticleMatrix;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.filesystem.scenes.WSceneMember;
import com.e24.wolke.utils.Timer;
import com.e24.wolke.utils.images.WColor;
import com.e24.wolke.utils.math.WVector2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code RendererModel} regroupe la logique et les valeurs a memoriser reliee au rendu de
 * la simulation.
 *
 * @author MeriBouisri
 * @author adrienles
 * @author Nilon123456789
 */
public class RendererModel extends WModelSaveable implements Runnable, WSceneMember<Properties> {

  /** Le {@code Logger} de la classe */
  private static final Logger LOGGER =
      LogManager.getLogger(RendererModel.class.getClass().getSimpleName());

  /** Les {@code RendererProperties} de cette instance */
  private RendererProperties properties;

  /** Etat du rendu de la simulation */
  private boolean isRendering;

  /** Etat du thread */
  private boolean isRunning = false;

  /** Timer */
  private final Timer timer =
      new Timer("RendererModel", LogManager.getLogger(RendererModel.class.getSimpleName()));

  /** La resolution de la simulation */
  private int[] resolution;

  /** La résolution du canvas de la simulation */
  private int[] canvasResolution;

  /** Image de la simulation */
  private BufferedImage simulationImage;

  /** Image des obstacles */
  private BufferedImage obstacleImage;

  /** Image des vecteurs */
  private BufferedImage vectorImage;

  /** Taille des pixels */
  private double pixelSize = 1.0;

  /** Matrice des transformations pour le rendu des vecteurs */
  private AffineTransform matScale = new AffineTransform();

  /** Chemin des lignes du champ de vecteurs */
  private Path2D.Double vectorFieldLinesPath = new Path2D.Double();

  /** Compteur pour le rendu des vecteurs */
  private int vectorRedrawCounter = 0;

  /** Frequence de rafraichissement des vecteurs */
  private int vectorRedrawFrequency = 5;

  /**
   * La fréquence à laquelle les lignes du champ de vecteurs vont être affichés par rapport à la
   * grille de simulation (ex: 10 signifie que les lignes du champ de vecteurs seront affichés tous
   * les 10 pixels de la grille de simulation)
   */
  private int vectorFieldLinesStep = RendererConstants.DEFAULT_VECTOR_FIELD_LINES_STEP;

  /**
   * Construction d'un {@code RendererModel} avec un {@code Controller}.
   *
   * @param controller Le {@code Controller} qui gere cette instance
   */
  public RendererModel(Controller controller) {
    super(controller);
    timer.setupAutoAveragePrint("updateSimulationFrame", 500);
    setupKeybindSubscriptions();
    properties = new RendererProperties();
    setProperties(properties);
    initialize();
  }

  /** {@inheritDoc} */
  @Override
  protected boolean setup() {
    updateResolution();
    simulationImage = getSimulationImage();
    handleCurrentFrame();
    start();

    RendererModel.LOGGER.debug("Initialized");

    return true;
  }

  /** Methode pour demarrer le processus de rendu de la simulation */
  public void start() {
    if (isRunning) return;

    isRunning = true;

    Thread thread = new Thread(this);
    thread.setName(getClass().getSimpleName());
    thread.start();
  }

  /** Methode pour arreter le processus de rendu de la simulation */
  public void stop() {
    isRunning = false;
    newFrameReady(); // Pour debloquer le thread
  }

  /**
   * Getter pour {@code this#resolution}
   *
   * @return La resolution de la simulation
   */
  public int[] getResolution() {
    return resolution;
  }

  /**
   * Getter pour {@code this#canvasResolution}
   *
   * @return La resolution du canvas de la simulation
   */
  public int[] getCanvasResolution() {
    return canvasResolution;
  }

  /**
   * Setter pour {@code this#canvasResolution}
   *
   * @param canvasResolution La resolution du canvas de la simulation
   */
  public void setCanvasResolution(int[] canvasResolution) {
    this.canvasResolution = canvasResolution;
  }

  /**
   * Getter pour {@code this#simulationImage}. Un nouveau {@code BufferedImage} est construit si
   * {@code this#simulationImage} est {@code null}.
   *
   * @return Le {@code BufferedImage} dans lequel le rendu de la simulation est dessine
   */
  public BufferedImage getSimulationImage() {
    if (this.simulationImage == null)
      simulationImage = new BufferedImage(resolution[0], resolution[1], BufferedImage.TYPE_INT_RGB);

    return simulationImage;
  }

  /**
   * Getter pour {@code this#obstacleMatrix}
   *
   * @return Le {@code BufferedImage} dans lequel les obstacles sont dessines
   */
  public BufferedImage getObstacleImage() {
    if (this.obstacleImage == null)
      obstacleImage = new BufferedImage(resolution[0], resolution[1], BufferedImage.TYPE_INT_ARGB);

    return obstacleImage;
  }

  /**
   * Getter pour {@code this#vectorImage}
   *
   * @return Le {@code BufferedImage} dans lequel les vecteurs sont dessines
   */
  public BufferedImage getVectorImage() {
    if (this.vectorImage == null)
      vectorImage = new BufferedImage(resolution[0], resolution[1], BufferedImage.TYPE_INT_ARGB);

    return vectorImage;
  }

  /**
   * Getter pour {@code this#pixelSize}
   *
   * @return La taille des pixels
   */
  public double getPixelSize() {
    return pixelSize;
  }

  /**
   * Setter pour {@code this#pixelSize}
   *
   * @param pixelSize La taille des pixels
   */
  public void setPixelSize(double pixelSize) {
    this.pixelSize = pixelSize;
  }

  /**
   * Getter pour {@code this#vectorFieldLinesPath}
   *
   * @return Le chemin des lignes formant le champ de vecteurs
   */
  public Path2D.Double getVectorFieldLinesPath() {
    return vectorFieldLinesPath;
  }

  /**
   * Setter pour {@code this#vectorFieldLinesPath}
   *
   * @param vectorFieldLinesPath Le chemin des lignes formant le champ de vecteurs
   */
  public void setVectorFieldLinesPath(Path2D.Double vectorFieldLinesPath) {
    this.vectorFieldLinesPath = vectorFieldLinesPath;
  }

  /**
   * Setter pour la fréquence à laquelle les lignes du champ de vecteurs vont être affichés par
   * rapport à la grille de simulation
   *
   * @param vectorFieldLinesStep La fréquence à laquelle les lignes du champ de vecteurs vont être
   *     affichés par rapport à la grille de simulation
   */
  public void setVectorFieldLinesStep(int vectorFieldLinesStep) {
    this.vectorFieldLinesStep = vectorFieldLinesStep;
  }

  /**
   * Getter pour la fréquence à laquelle les lignes du champ de vecteurs vont être affichés par
   * rapport à la grille de simulation
   *
   * @return La fréquence à laquelle les lignes du champ de vecteurs vont être affichés par rapport
   *     à la grille de simulation
   */
  public int getVectorFieldLinesStep() {
    return vectorFieldLinesStep;
  }

  /**
   * Getter pour l'etat du rendu de la simulation
   *
   * @return {@code true} si cette instance est en train de generer le rendu de la simulation
   */
  public boolean getIsRendering() {
    return isRendering;
  }

  /** Actualiser la resolution de l'image de la simulation. */
  public void updateResolution() {
    ParticleMatrix particleMatrix =
        getController().getSimulationModel().getSimulationData().getCurrentParticleMatrix();

    if (particleMatrix == null) return;

    resolution = particleMatrix.getXandYLength();
  }

  /** Methode a invoquer lorsque le rendu d'une nouvelle frame est pret. */
  // Nil Lahaye
  public synchronized void newFrameReady() {
    this.notifyAll();
  }

  /** Gestion de l'arrivée d'une nouvelle frame */
  public void handleNewFrame() {
    if (this.isRendering) {
      return;
    }

    if (RendererConstants.USE_OPENGL) {
      this.isRendering = true;
      if (this.getController()
              .getSimulationModel()
              .getSimulationData()
              .getRenderParticleMatrixQueueSize()
          > 1) {
        for (int i = 0;
            i
                < getController()
                        .getSimulationModel()
                        .getSimulationData()
                        .getRenderParticleMatrixQueueSize()
                    - 1;
            i++) {
          // On poll la matrice de particule pis on la remet dans le queue pour s'en débarasser. A
          // revoir.
          getController()
              .getSimulationModel()
              .getSimulationData()
              .returnParticleMatrixToPool(
                  getController()
                      .getSimulationModel()
                      .getSimulationData()
                      .pollRenderParticleMatrix());
        }
      }
      onBufferImageDone();
      this.isRendering = false;
      return;
    }

    ParticleMatrix particleMatrix =
        getController().getSimulationModel().getSimulationData().pollRenderParticleMatrix();

    this.isRendering = true;

    drawFluidSimulation(particleMatrix);

    vectorRedrawCounter++;
    if (getShowVectors() && vectorRedrawCounter % vectorRedrawFrequency == 0) {
      createVectorField(particleMatrix);
      vectorRedrawCounter = 0;
    }

    onBufferImageDone();

    // On poll la matrice de particule pis on le remet dans le queue. A revoir.
    getController()
        .getSimulationModel()
        .getSimulationData()
        .returnParticleMatrixToPool(particleMatrix);

    this.isRendering = false;
  }

  /**
   * Gestion de la frame actuelle dans le cas où l'utilisateur change le type de visualisation
   * pendant que la simulation est en pause
   */
  public void handleCurrentFrame() {
    if (this.isRendering) {
      return;
    }

    if (RendererConstants.USE_OPENGL) {
      this.isRendering = true;
      onBufferImageDone();
      this.isRendering = false;
      return;
    }

    ParticleMatrix particleMatrix =
        getController().getSimulationModel().getSimulationData().getCurrentParticleMatrix();

    this.isRendering = true;

    if (getShowVectors()) {
      createVectorField(particleMatrix);
    }

    drawFluidSimulation(particleMatrix);

    onBufferImageDone();

    this.isRendering = false;
  }

  /** Methode a invoquer lorsque la simulation est réinitialisée */
  public void setBlankFrame() {
    simulationImage = new BufferedImage(resolution[0], resolution[1], BufferedImage.TYPE_INT_RGB);
    onBufferImageDone();
  }

  /** Methode a invoquer lorsque le {@code this#simulationImage} est pret. */
  public void onBufferImageDone() {
    getPublisher().publish(Subject.ON_BUFFER_IMAGE_DONE, null);
  }

  /**
   * Dessiner la simulation de fluide
   *
   * @param particleMatrix La matrice de particules
   */
  private void drawFluidSimulation(ParticleMatrix particleMatrix) {
    if (particleMatrix == null) return;

    updateSimulationFrame(
        particleMatrix.getParticlesByVisualization(properties.visualizationType),
        particleMatrix.getXandYLength(),
        particleMatrix.getMinMaxByVisualization(properties.visualizationType),
        getController().getSimulationModel().getSimulationData().getObstacle());

    getPublisher()
        .publish(
            Subject.ON_SIMULATION_MINMAX_CHANGED,
            particleMatrix.getMinMaxByVisualization(properties.visualizationType));
  }

  /**
   * Mettre à jour l'image de la simulation
   *
   * @param mat Matrice de valeurs
   * @param size Tableau contenant 2 elements, la taille en x et y de la matrice de particules
   * @param minMax Tableau contenant 2 elements, le min et le max de la matrice de particule s
   * @param obstacles Tableau contenant les obstacles
   */
  public void updateSimulationFrame(
      double[] mat, int[] size, double[] minMax, WObstacleCellMatrix obstacles) {
    if (size.length < 2 || minMax.length < 2) return;

    updateSimulationFrame(mat, size[0], size[1], minMax[0], minMax[1], obstacles);
  }

  /**
   * Mettre à jour l'image de la simulation
   *
   * @param mat Matrice de valeurs
   * @param sizeX Taille en x de la matrice
   * @param sizeY Taille en y de la matrice
   * @param min Valeur minimale
   * @param max Valeur maximale
   * @param obstacles Tableau contenant les obstacles
   */
  public void updateSimulationFrame(
      double[] mat, int sizeX, int sizeY, double min, double max, WObstacleCellMatrix obstacles) {

    if (this.simulationImage == null) return;

    this.timer.start("updateSimulationFrame");
    int index;
    for (int i = 0; i < sizeX; i++) {
      for (int j = 0; j < sizeY; j++) {
        index = j * sizeX + i;
        getSimulationImage()
            .setRGB(
                i,
                j,
                properties.greyscale
                    ? WColor.interpolateGrayscaleFromMinMax(mat[index], min, max)
                    : WColor.interpolateHSBColorFromMinMax(
                        properties.gradientHSBColors[1],
                        properties.gradientHSBColors[0],
                        mat[index],
                        min,
                        max));
      }
    }
    timer.stop("updateSimulationFrame");
  }

  /**
   * Methode a invoquer pour créer les lignes formant le champ de vecteurs
   *
   * @param matrix La matrice de particules
   */
  private void createVectorField(ParticleMatrix matrix) {
    if (matrix == null) return;

    vectorFieldLinesPath = new Path2D.Double();

    double[] xMinMax = new double[2];
    double[] yMinMax = new double[2];
    double[][] xyValues = new double[2][];
    xyValues[0] = new double[matrix.getXLength() * matrix.getYLength()];
    xyValues[1] = new double[matrix.getXLength() * matrix.getYLength()];

    // Attributions des valeurs en fonction du type de visualisation
    switch (properties.visualizationType) {
      case DENSITY:
        // La densité n'a pas de vecteurs
        return;
      case VELOCITY:
        xMinMax = matrix.getXVelocityMinMax();
        yMinMax = matrix.getYVelocityMinMax();
        xyValues[0] = matrix.getXVelocity().getMatrix();
        xyValues[1] = matrix.getYVelocity().getMatrix();
        break;
      case VELOCITY_X:
        xMinMax = matrix.getXVelocityMinMax();
        yMinMax[0] = 0;
        yMinMax[1] = 0;
        xyValues[0] = matrix.getXVelocity().getMatrix();
        break;
      case VELOCITY_Y:
        xMinMax[0] = 0;
        xMinMax[1] = 0;
        yMinMax = matrix.getYVelocityMinMax();
        xyValues[1] = matrix.getYVelocity().getMatrix();
        break;
      case PRESSURE:
        xMinMax = matrix.getXPressureGradientMinMax();
        yMinMax = matrix.getYPressureGradientMinMax();
        xyValues[0] = matrix.getXPressureGradient().getMatrix();
        xyValues[1] = matrix.getYPressureGradient().getMatrix();
        break;
      case PRESSURE_X:
        xMinMax = matrix.getXPressureGradientMinMax();
        yMinMax[0] = 0;
        yMinMax[1] = 0;
        xyValues[0] = matrix.getXPressureGradient().getMatrix();
        break;
      case PRESSURE_Y:
        xMinMax[0] = 0;
        xMinMax[1] = 0;
        yMinMax = matrix.getYPressureGradientMinMax();
        xyValues[1] = matrix.getYPressureGradient().getMatrix();
        break;
      default:
        return;
    }

    double xMax = Math.max(Math.abs(xMinMax[0]), Math.abs(xMinMax[1]));
    double yMax = Math.max(Math.abs(yMinMax[0]), Math.abs(yMinMax[1]));
    double max = Math.max(xMax, yMax);

    for (int i = this.vectorFieldLinesStep - 1;
        i < matrix.getXLength();
        i += this.vectorFieldLinesStep) {
      for (int j = this.vectorFieldLinesStep - 1;
          j < matrix.getYLength();
          j += this.vectorFieldLinesStep) {
        int index = matrix.getPos(i, j);
        if (getController().getSimulationModel().getSimulationData().isCellObstructed(index))
          continue;
        WVector2D direction = new WVector2D(xyValues[0][index], xyValues[1][index]);
        double ratio = direction.modulus() / max;
        direction = direction.normalize();
        direction = direction.multiply(ratio);
        this.vectorFieldLinesPath.moveTo(i, j);
        this.vectorFieldLinesPath.lineTo(
            i + (direction.getX() * (vectorFieldLinesStep / 2)),
            j + (direction.getY() * (vectorFieldLinesStep / 2)));
      }
    }

    vectorImage =
        new BufferedImage(canvasResolution[0], canvasResolution[1], BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = vectorImage.createGraphics();

    g2d.setColor(Color.BLACK);

    g2d.setColor(Color.black);
    double circleDiam = 2.0f * this.pixelSize;
    int step = vectorFieldLinesStep;
    for (int i = step - 1; i < this.resolution[0] - step; i += step) {
      for (int j = step - 1; j < this.resolution[1] - step; j += step) {
        g2d.fill(
            new Ellipse2D.Double(
                i * this.pixelSize - circleDiam / 2,
                j * this.pixelSize - circleDiam / 2,
                circleDiam,
                circleDiam));
      }
    }
    // C'est beau mais si lent haha
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setStroke(
        new BasicStroke((int) (pixelSize), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

    g2d.draw(this.matScale.createTransformedShape(vectorFieldLinesPath));
  }

  // =======
  // GETTERS
  // =======

  /**
   * Retourne le {@code VisualizationType} actuel
   *
   * @return Le {@code VisualizationType} actuel
   */
  public VisualizationType getCurrentVisualizationType() {
    return properties.visualizationType;
  }

  /**
   * Retourne l'état de la visualisation en nuances de gris
   *
   * @return L'état de la visualisation en nuances de gris
   */
  public boolean getGrayscaleVisualisation() {
    return properties.greyscale;
  }

  /**
   * Getter pour {@code showGrid}
   *
   * @return Si la grille doit etre affichee ou non
   */
  public boolean getShowGrid() {
    return properties.showGrid;
  }

  /**
   * Getter pour {@code showFlowLines}
   *
   * @return si les lignes de flux doivent etre affichees ou non
   */
  public boolean getShowFlowLines() {
    return properties.showFlowLines;
  }

  /**
   * Getter pour {@code showVectors}
   *
   * @return Si les vecteurs doivent etre affiches ou non
   */
  public boolean getShowVectors() {
    if (!(this.properties.visualizationType == VisualizationType.VELOCITY)) return false;
    return properties.showVectors;
  }

  /**
   * Getter pour les couleurs du dégradé visualisé
   *
   * @return Les couleurs du dégradé visualisé
   */
  public float[][] getGradientHSBColors() {
    return properties.gradientHSBColors;
  }

  // =======
  // SETTERS
  // =======

  /**
   * Setter pour le {@code VisualizationType} actuel
   *
   * @param visualizationType Le {@code VisualizationType} actuel
   */
  public void setCurrentVisualizationType(VisualizationType visualizationType) {
    properties.visualizationType = visualizationType;
  }

  /**
   * Setter pour la visualisation en nuances de gris
   *
   * @param newState Le nouvel état de la visualisation en nuances de gris
   */
  public void setGrayscaleVisualisation(boolean newState) {
    properties.greyscale = newState;
  }

  /**
   * Setter pour {@code showGrid}
   *
   * @param show Si la grille doit etre affichee ou non
   */
  public void setShowGrid(boolean show) {
    properties.showGrid = show;
  }

  /**
   * Setter pour {@code showFlowLines}
   *
   * @param show Si les lignes de flux doivent etre affichees ou non
   */
  public void setShowFlowLines(boolean show) {
    properties.showFlowLines = show;
  }

  /**
   * Setter pour {@code showVectors}
   *
   * @param show Si les vecteurs doivent etre affiches ou non
   */
  public void setShowVectors(boolean show) {
    properties.showVectors = show;
  }

  /**
   * Setter pour la matrice de transformation pour le rendu des vecteurs
   *
   * @return La matrice de transformation pour le rendu des vecteurs
   */
  public AffineTransform getMatScale() {
    return this.matScale;
  }

  /**
   * Setter pour la matrice de transformation pour le rendu des vecteurs
   *
   * @param matScale La matrice de transformation pour le rendu des vecteurs
   */
  public void setMatScale(AffineTransform matScale) {
    this.matScale = matScale;
  }

  /**
   * Setter pour les couleurs du dégradé visualisé
   *
   * @param gradientHSBColors Les couleurs du dégradé visualisé
   */
  public void setGradientHSBColors(float[][] gradientHSBColors) {
    properties.gradientHSBColors = gradientHSBColors;
  }

  /** {@inheritDoc} */
  public void setupKeybindSubscriptions() {}

  /** {@inheritDoc} */
  @Override
  public void softReinitialize() {
    stop();

    simulationImage = null;
    obstacleImage = null;
    resolution = null;
    updateResolution();

    properties.visualizationType = RendererConstants.DEFAULT_VISUALIZATION_TYPE;

    properties.showGrid = false;
    properties.greyscale = false;
    properties.showVectors = false;
    properties.showFlowLines = false;

    start();
  }

  /** Thread de rendue de la simulation */
  @Override
  public void run() {
    while (isRunning) {
      int length =
          getController()
              .getSimulationModel()
              .getSimulationData()
              .getRenderParticleMatrixQueueSize();

      if (length > 0) {
        handleNewFrame();
        continue;
      }

      // Block le thread jusqu'à ce qu'une nouvelle frame soit prête
      synchronized (this) {
        try {
          wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  protected void onPropertiesChanged() {}

  /** {@inheritDoc} */
  @Override
  public Properties getSceneComponent() {
    return this.properties.writeStandardProperties();
  }

  /** {@inheritDoc} */
  @Override
  public boolean setSceneComponent(Properties sceneComponent) {
    this.properties.readStandardProperties(sceneComponent);
    return true;
  }
}
