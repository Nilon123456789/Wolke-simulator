package com.e24.wolke.backend.models.editor.tools;

import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import java.awt.Graphics2D;

/**
 * La classe {@code WToolFree} permet de definir le comportement d'un outil de dessin libre. La
 * particularite de ce type d'outil est que le dessin ne contient pas d'image intermediaire. Ce type
 * d'outil est egalement caracterise par le fait que la position actuelle devient la position
 * initiale du prochain appel a la methode {@code WTool#use(java.awt.Graphics2D)}
 *
 * @author MeriBouisri
 */
public abstract class WToolFree extends WTool {

  /**
   * Constructeur permettant aux classes enfants de {@code WTool} de definir leur {@code ToolType}
   *
   * @param toolType Le type d'outil selon les elements de {@code ToolType}
   */
  protected WToolFree(ToolType toolType) {
    super(toolType);
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasDraft() {
    return false;
  }

  /**
   * Methode permettant de dessiner a partir d'un contexte graphique, de la position precedente et
   * de la position actuelle
   *
   * @param g2d Le contexte graphique {@code Graphics2D}
   * @param prevX La position precedente en x
   * @param prevY La position precedente en y
   * @param currentX La position actuelle en x
   * @param currentY La position actuelle en y
   */
  public abstract void draw(Graphics2D g2d, int prevX, int prevY, int currentX, int currentY);

  /**
   * Methode permettant d'utiliser l'outil de dessin avec un contexte graphique {@code Graphics2D}.
   * La position actuelle est memorisee en tant que la position precedente dans les champs {@code
   * this#startX} et {@code this#startY} pour le prochain appel a cette methode.
   *
   * @param g2d Le contexte graphique {@code Graphics2D} avec lequel dessiner
   */
  @Override
  public void use(Graphics2D g2d) {
    draw(g2d, getStartX(), getStartY(), getCurrentX(), getCurrentY());
    setStartPosition(getCurrentX(), getCurrentY());
  }
}
