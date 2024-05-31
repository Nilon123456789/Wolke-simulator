package com.e24.wolke.backend.models.editor.tools;

import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import com.e24.wolke.utils.images.WImageUtils;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * La classe abstraite {@code WTool} permet de definir un objet pouvant etre utilisé comme outil de
 * dessin dans le mode editeur de l'application.
 *
 * @author MeriBouisri
 * @author n-o-o-d-l-e
 */
public abstract class WTool {

  /** Le type d'outil selon les elements de {@code ToolType} */
  private ToolType toolType;

  /** Le {@code BufferedImage} brouillon sur lequel le dessin est affiche, s'il y a lieu */
  private BufferedImage draft;

  /** La coordonnee en x du debut du dessin avec ce {@code WTool} */
  private int startX;

  /** La coordonnee en y du debut du dessin avec ce {@code WTool} */
  private int startY;

  /** La coordonnée en x précédente du dessin avec ce {@code WTool} */
  private int previousX;

  /** La coordonnée en y précédente du dessin avec ce {@code WTool} */
  private int previousY;

  /** La coordonnee en x actuelle du dessin avec ce {@code WTool} */
  private int currentX;

  /** La coordonnee en y actuelle du dessin avec ce {@code WTool} */
  private int currentY;

  /**
   * Constructeur permettant aux classes enfant de {@code WTool} de definir leur {@code ToolType}
   *
   * @param toolType Le type d'outil selon les elements de {@code ToolType}
   */
  protected WTool(ToolType toolType) {
    this.toolType = toolType;
  }

  /**
   * Getter pour l'image intermediaire sur lequel le dessin est affiche, s'il y a lieu. L'image
   * intermediaire sert de brouillon pour les dessins qui necessitent d'etre modifies par
   * l'utilisateur au cours du dessin. Toujours verifier la presence de l'image brouillon avec
   * {@code this#hasIntermediateImage()} avant d'utiliser cette methode.
   *
   * @return Le {@code BufferedImage} representant l'image brouillon sur lequelle le dessin de
   *     l'outil est affiche au cours de la modification
   */
  public BufferedImage getDraft() {
    return draft;
  }

  /**
   * Si cette instance possede une image brouillon sur lequel le dessin est affiche au cours de la
   * modification, s'il y a lieu
   *
   * @return {@code true} si cette instance possede une image brouillon
   */
  public boolean hasDraft() {
    return this.draft == null;
  }

  /**
   * Setter pour {@code this#draft}, l'image brouillon de cette instance.
   *
   * @param draft L'image sur laquelle l'affichage du brouillon sera effectue
   */
  public void setDraft(BufferedImage draft) {
    this.draft = draft;
  }

  /**
   * Methode permettant de creer un {@code BufferedImage} pour l'assigner a {@code this#draft}. Le
   * {@code BufferedImage} cree est une image vide compatible avec l'image passee en parametre
   *
   * @param image L'image avec laquelle le {@code this#draft} doit etre compatible
   */
  public void setCompatibleDraft(BufferedImage image) {
    draft = WImageUtils.createCompatibleEmptyBufferedImage(image);
  }

  /**
   * Getter pour {@code this#startX}, la coordonnee en x du debut du dessin avec cette instance
   *
   * @return La coordonnee en x du debut du dessin avec cette instance
   */
  public int getStartX() {
    return startX;
  }

  /**
   * Setter pour {@code this#startX}, la coordonnee en x du debut du dessin avec cette instance
   *
   * @param startX La coordonnee en x du debut du dessin avec cette instance
   */
  public void setStartX(int startX) {
    this.startX = startX;
  }

  /**
   * Getter pour {@code this#startY}, la coordonnee en y du debut du dessin avec cette instance
   *
   * @return La coordonnee en y du debut du dessin avec cette instance
   */
  public int getStartY() {
    return startY;
  }

  /**
   * Setter pour {@code this#startY}, la coordonnee en y du debut du dessin avec cette instance
   *
   * @param startY La coordonnee en y du debut du dessin avec cette instance
   */
  public void setStartY(int startY) {
    this.startY = startY;
  }

  /**
   * Getter pour {@code this#previousX}, la coordonnee en x precedente du dessin avec cette instance
   *
   * @return la coordonnee en x precedente du dessin avec cette instance
   */
  public int getPreviousX() {
    return this.previousX;
  }

  /**
   * Setter pour {@code this#previousX}, la coordonnee en x precedente du dessin avec cette instance
   *
   * @param previousX la coordonnee en x precedente du dessin avec cette instance
   */
  public void setPreviousX(int previousX) {
    this.previousX = previousX;
  }

  /**
   * Getter pour {@code this#previousY}, la coordonnee en y precedente du dessin avec cette instance
   *
   * @return la coordonnee en y precedente du dessin avec cette instance
   */
  public int getPreviousY() {
    return this.previousY;
  }

  /**
   * Setter pour {@code this#previousY}, la coordonnee en y precedente du dessin avec cette instance
   *
   * @param previousY la coordonnee en y precedente du dessin avec cette instance
   */
  public void setPreviousY(int previousY) {
    this.previousY = previousY;
  }

  /**
   * Getter pour {@code this#currentX}, la coordonnee en x actuelle du dessin avec cette instance
   *
   * @return la coordonnee en x actuelle du dessin avec cette instance
   */
  public int getCurrentX() {
    return this.currentX;
  }

  /**
   * Setter pour {@code this#currentXX}, la coordonnee en x actuelle du dessin avec cette instance
   *
   * @param currentX la coordonnee en x actuelle du dessin avec cette instance
   */
  public void setCurrentX(int currentX) {
    this.currentX = currentX;
  }

  /**
   * Getter pour {@code this#currentY}, la coordonnee en y actuelle du dessin avec cette instance
   *
   * @return la coordonnee en y actuelle du dessin avec cette instance
   */
  public int getCurrentY() {
    return this.currentY;
  }

  /**
   * Setter pour {@code this#currentY}, la coordonnee en y actuelle du dessin avec cette instance
   *
   * @param currentY la coordonnee en y actuelle du dessin avec cette instance
   */
  public void setCurrentY(int currentY) {
    this.currentY = currentY;
  }

  /**
   * Setter pour la position actuelle de la souris
   *
   * @param x La coordonnee en x initiale du dessin avec la souris
   * @param y La coordonnee en y initiale du dessin avec la souris
   */
  public void setStartPosition(int x, int y) {
    startX = x;
    startY = y;
  }

  /**
   * Getter pour la position initiale de la souris
   *
   * @return un tableau {@code int[2]} contenant la position (x, y) initiale de la souris
   */
  public int[] getStartPosition() {
    return new int[] {startX, startY};
  }

  /**
   * Setter pour la position actuelle de la souris et actualisation de la position precedente
   *
   * @param x La coordonnee en x actuelle du dessin avec la souris
   * @param y La coordonee en y actuelle du dessin avec la souris
   */
  public void setCurrentPosition(int x, int y) {
    previousX = currentX;
    previousY = currentY;
    currentX = x;
    currentY = y;
    if ((currentX == startX && currentY == startY)) {
      previousX = currentX;
      previousY = currentY;
    }
  }

  /**
   * Getter pour la position actuelle de la souris
   *
   * @return un tableau {@code int[2]} contenant la position (x, y) actuelle de la souris
   */
  public int[] getCurrentPosition() {
    return new int[] {currentX, currentY};
  }

  /**
   * Getter pour la position precedente de la souris
   *
   * @return un tableau {@code int[2]} contenant la position (x, y) precedente de la souris
   */
  public int[] getPreviousPosition() {
    return new int[] {previousX, previousY};
  }

  /**
   * Setter pour la position précédente de la souris
   *
   * @param x La coordonnee en x precedente du dessin avec la souris
   * @param y La coordonee en y precedente du dessin avec la souris
   */
  public void setPreviousPosition(int x, int y) {
    previousX = x;
    previousY = y;
  }

  /**
   * Setter pour la position initiale et la position actuelle de la souris
   *
   * @param startX La coordonnee en x initiale du dessin avec la souris
   * @param startY La coordonee en y initiale du dessin avec la souris
   * @param currentX La coordonnee en x actuelle du dessin avec la souris
   * @param currentY La coordonee en y actuelle du dessin avec la souris
   */
  public void setPosition(int startX, int startY, int currentX, int currentY) {
    this.startX = startX;
    this.startY = startY;
    this.currentX = currentX;
    this.currentY = currentY;
  }

  /**
   * Getter pour la position initiale et la position actuelle de la souris
   *
   * @return Un {@code int[4]} contenant {@code this#startX, this#startY, this#currentX,
   *     this#currentY}, aux indices respectifs.
   */
  public int[] getPosition() {
    return new int[] {startX, startY, currentX, currentY};
  }

  /**
   * Getter pour le {@code ToolType} de l'outil
   *
   * @return Le {@code ToolType} de l'outil
   */
  public ToolType getToolType() {
    return toolType;
  }

  /**
   * Methode permettant d'utiliser l'outil sur un {@code BufferedImage}
   *
   * @param image Le {@code BufferedImage} sur lequel le dessin sera effectue
   */
  public void use(BufferedImage image) {
    Graphics2D g2d = image.createGraphics();
    use(g2d);
    g2d.dispose();
  }

  /**
   * Methode permettant d'utiliser l'outil de dessin avec un contexte graphique {@code Graphics2D}
   *
   * @param g2d Le contexte graphique {@code Graphics2D} avec lequel dessiner
   */
  public abstract void use(Graphics2D g2d);
}
