package com.e24.wolke.backend.models.editor.tools;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * La classe {@code WToolbox} permet de memoriser des parametres partages par des outils differents
 * et de gerer le dessin sur une image
 *
 * @author MeriBouisri
 * @author n-o-o-d-l-e
 */
public class WToolbox {

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** La couleur actuelle de dessin */
  private Color color = WToolConstants.DEFAULT_COLOR;

  /** L'epaisseur actuelle de dessin */
  private int strokeThickness = WToolConstants.DEFAULT_LINE_THICKNESS;

  /** L'index de l'epaisseur actuelle de dessin */
  private int currentThicknessIndex = WToolConstants.DEFAULT_LINE_THICKNESS_INDEX;

  /** La tete des lignes de dessin */
  private final int STROKE_CAP = WToolConstants.DEFAULT_STROKE_CAP;

  /** Le biseau des lignes de dessin */
  private final int STROKE_BEVEL = WToolConstants.DEFAULT_STROKE_BEVEL;

  /** Le {@code ToolType} a utiliser pour le dessin */
  private ToolType currentToolType = WToolConstants.DEFAULT_TOOL_TYPE;

  /** Si un outil du {@code WEditorToolbox} est en cours d'utilisation */
  private boolean isPainting;

  /** Si une image intermediaire est presente, comme lors de dessin de formes */
  private boolean hasIntermediateImage;

  /** La coordonnee initiale en x du dessin courant */
  private int startX;

  /** La coordonnee initiale en y du dessin courant */
  private int startY;

  /** Le {@code HashMap} contenant les outils */
  private ArrayList<WTool> tools;

  /**
   * Construction d'un {@code WToolbox} avec un {@code WBufferedCanvas}
   *
   * @param controller Le {@code Controller} de l'application
   */
  public WToolbox(Controller controller) {
    this(controller, WToolConstants.DEFAULT_COLOR, WToolConstants.DEFAULT_LINE_THICKNESS);
  }

  /**
   * Construction d'un {@code WToolbox} avec des valeurs initiales pour {@code this#color} et {@code
   * this#strokeThickness}
   *
   * @param controller Le {@code Controller} de l'application
   * @param color La couleur actuelle de dessin
   * @param strokeThickness L'epaisseur actuelle de dessin
   */
  public WToolbox(Controller controller, Color color, int strokeThickness) {
    this.controller = controller;
    initializeTools();

    this.color = color;
    this.strokeThickness = strokeThickness;
  }

  /** Methode permettant d'initialiser la liste d'outils */
  private void initializeTools() {
    tools = new ArrayList<WTool>();

    tools.add(new WToolImage(this.controller));
    tools.add(new WToolFreePencil());
    tools.add(new WToolFreeEraser());
    tools.add(new WToolShapeEllipse());
    tools.add(new WToolShapeRectangle());
    tools.add(new WToolShapePolygon());
  }

  /**
   * Si une image intermediaire est en cours de dessin
   *
   * @return {@code true} si une image intermediaire est en cours de dessin
   */
  public boolean hasIntermediateImage() {
    return hasIntermediateImage;
  }

  /**
   * L'image intermediaire en cours de dessin. Cette image est utilisee par les objets {@code
   * WToolShape} pour afficher la forme en cours de dessin ou en cours de transformation
   *
   * @return Le {@code BufferedImage} contenant l'image intermediaire des {@code WToolShape}
   */
  public BufferedImage peekIntermediateImage() {
    return getTool().getDraft();
  }

  /**
   * Methode permettant de recuperer l'image intermediaire a la fin du dessin. Apres l'appel a cette
   * methode, {@code this#hasIntermediateImage} devient {@code false}
   *
   * @return Le {@code BufferedImage} representant l'image intermediaire de cette instance, s'il y a
   *     lieu
   */
  public BufferedImage pullIntermediateImage() {
    hasIntermediateImage = false;
    return getTool().getDraft();
  }

  /**
   * Methode permettant d'obtenir le {@code WTool} actuel selon le {@code this#currentToolType}
   *
   * @return Le {@code WTool} actuel selon le {@code this#currentToolType}. Retourne {@code null} si
   *     {@code ToolType == NONE}
   */
  public WTool getTool() {
    if (currentToolType == ToolType.NONE) return null;

    return tools.stream().filter(t -> t.getToolType().equals(currentToolType)).findFirst().get();
  }

  /**
   * Getter pour {@code this#color}, la couleur actuelle du dessin.
   *
   * @return La couleur actuelle de dessin
   */
  public Color getColor() {
    return color;
  }

  /**
   * Setter pour {@code this#color}, la couleur actuelle du dessin.
   *
   * @param color La couleur actuelle de dessin
   */
  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Getter pour {@code this#currentThickness}, l'epaisseur actuelle du dessin
   *
   * @return L'epaisseur actuelle du dessin
   */
  public int getStrokeThickness() {
    return strokeThickness;
  }

  /**
   * Getter pour {@code this#currentThicknessIndex}, l'index de l'epaisseur actuelle du dessin
   *
   * @return L'index de l'epaisseur actuelle du dessin
   */
  public int getCurrentThicknessIndex() {
    return currentThicknessIndex;
  }

  /** Methode permettant de changer l'epaisseur du dessin actuel */
  public void cycleThroughThickness() {
    currentThicknessIndex = (currentThicknessIndex + 1) % WToolConstants.LINE_THICKNESS.length;
    strokeThickness = WToolConstants.LINE_THICKNESS[currentThicknessIndex];
  }

  /**
   * Setter pour {@code this#strokeThickness}, l'epaisseur actuelle du dessin
   *
   * @param strokeThickness L'epaisseur actuelle du dessin
   */
  public void setStrokeThickness(int strokeThickness) {
    this.strokeThickness = strokeThickness;
  }

  /**
   * Setter pour {@code this#currentTool}, le {@code ToolType} actuel en utilisation
   *
   * @param currentToolType Le {@code toolType} actuel en utilisation
   */
  public void setCurrentToolType(ToolType currentToolType) {
    this.currentToolType = currentToolType;
  }

  /**
   * Getter pour {@code this#currentTool}, le {@code ToolType} actuel en utilisation
   *
   * @return le {@code ToolType} actuel en utilisation
   */
  public ToolType getCurrentToolType() {
    return currentToolType;
  }

  /**
   * Setter pour {@code this#startX}, la coordonnee en x du debut du dessin
   *
   * @param startX La coordonnee en x du debut du dessin
   */
  public void setStartX(int startX) {
    this.startX = startX;
  }

  /**
   * Setter pour {@code this#startY}, la coordonnee en y du debut du dessin
   *
   * @param startY La coordonnee en y du debut du dessin
   */
  public void setStartY(int startY) {
    this.startY = startY;
  }

  /**
   * Setter pour {@code this#startX} et {@code this#startY}, les coordonnees en x et y du debut du
   * dessin
   *
   * @param startX La coordonnee en x du debut du dessin
   * @param startY La coordonnee en y du debut du dessin
   */
  public void setStartPosition(int startX, int startY) {
    if (this.getTool() == null) return;

    this.startX = startX;
    this.startY = startY;
    getTool().setStartPosition(startX, startY);
  }

  /**
   * Si un outil de cette instance est en cours d'utilisation
   *
   * @return {@code true} si un outil de cette instance est en cours d'utilisation
   */
  public boolean isPainting() {
    return isPainting;
  }

  /**
   * Methode permettant de creer et modifier un {@code Graphics2D} associe au {@code BufferedImage}
   * passe en parametre, et retourne le {@code Graphics2D} modifie
   *
   * @param image Le {@code BufferedImage} a partir duquel le {@code Graphics2D} est cree
   * @return Le {@code Graphics2D} cree a partir du {@code BufferedImage} passe en parametre, avec
   *     les parametres actuels de cette instance
   */
  private Graphics2D setupGraphicsParameters(BufferedImage image) {
    Graphics2D g2d = image.createGraphics();

    g2d.setColor(color);

    g2d.setStroke(new BasicStroke(this.strokeThickness, STROKE_CAP, STROKE_BEVEL));

    return g2d;
  }

  /**
   * Methode permettant d'utiliser l'outil associe au {@code this#currentTool}, avec les coordonnees
   * actuelles du dessin. Les coordonnees initiales utilisees sont celles memorisees dans les champs
   * {@code this#startX} et {@code this#startY}
   *
   * @param image le {@code BufferedImage} sur lequel dessiner
   * @param currentX La coordonnee en x actuelle du dessin
   * @param currentY La coordonnee en y actuelle du dessin
   */
  public void use(BufferedImage image, int currentX, int currentY) {
    use(image, startX, startY, currentX, currentY);
  }

  /**
   * Methode permettant de verifier que les coordonnees sont dans les limites de l'image
   *
   * @param image Le {@code BufferedImage} dont les limites doivent etre respectees
   * @param x La coordonnee en x a verifier
   * @param y La coordonnee en y a verifier
   * @return {@code true} si les coordonnees respectent les limites de l'image
   */
  public boolean isInsideBufferedImage(BufferedImage image, int x, int y) {
    return x > 0 && x < image.getWidth() && y > 0 && y < image.getHeight();
  }

  /**
   * Methode permettant d'utiliser l'outil associe au {@code this#currentTool}, avec les coordonnees
   * initiales du dessin et les coordonnees actuelles. Si l'outil actuel possede une image
   * intermediaire (comme dans le cas des {@code WToolShape}), le {@code BufferedImage} passe en
   * parametre est ignore et le dessin est effectue sur l'image intermediaire de l'outil.
   *
   * @param image Le {@code BufferedImage} sur lequel dessiner
   * @param startX La coordonnee en x du debut du dessin
   * @param startY La coordonnee en y du debut du dessin
   * @param currentX La coordonnee en x actuelle du dessin
   * @param currentY La coordonnee en y actuelle du dessin
   * @see WToolbox#hasIntermediateImage() Si une image intermediaire est presente
   * @see WToolbox#peekIntermediateImage() Acces a l'image intermediaire sur lequel le dessin a ete
   *     effectue, s'il y a lieu
   */
  public void use(BufferedImage image, int startX, int startY, int currentX, int currentY) {
    if (this.getTool() == null) return;

    if (isPainting) return;

    isPainting = true;

    getTool().setCurrentPosition(currentX, currentY);

    // Si outil ne possede pas d'image intermediaire, dessiner directement sur image
    // passee en
    // parametre
    Graphics2D g2d;

    if (getTool().hasDraft()) {

      hasIntermediateImage = true;

      getTool().setCompatibleDraft(image);
      g2d = setupGraphicsParameters(getTool().getDraft());

    } else {
      hasIntermediateImage = false;
      g2d = setupGraphicsParameters(image);
    }

    getTool().use(g2d);

    g2d.dispose();

    isPainting = false;
  }
}
