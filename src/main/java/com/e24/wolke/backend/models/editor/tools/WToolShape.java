package com.e24.wolke.backend.models.editor.tools;

import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * La classe {@code WToolShape} permet de
 *
 * @author MeriBouisri
 */
public abstract class WToolShape extends WTool {

  /** La longueur horizontale de la forme */
  private int width;

  /** La longueur verticale de la forme */
  private int height;

  /**
   * Constructeur permettant aux classes enfant de {@code WTool} de definir leur {@code ToolType}
   *
   * @param toolType Le type d'outil selon les elements de {@code ToolType}
   */
  protected WToolShape(ToolType toolType) {
    super(toolType);
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasDraft() {
    return true;
  }

  /**
   * Getter pour {@code this#width}, la longueur horizontale de la forme
   *
   * @return La longueur horizontale de la forme
   */
  public int getWidth() {
    return width;
  }

  /**
   * Getter pour {@code this#width}, la longueur verticale de la forme
   *
   * @return La longueur verticale de la forme
   */
  public int getHeight() {
    return height;
  }

  /**
   * Methode permettant de corriger les coordonnees de cette instance, de maniere a ce que le coin
   * dynamique de la forme suive le mouvement de la souris, et le point initial de la forme reste
   * statique.
   *
   * @param startX La coordonnee en x du debut du dessin
   * @param startY La coordonnee en y du debut du dessin
   * @param currentX La coordonnee en x actuelle du dessin
   * @param currentY La coordonnee en y actuelle du dessin
   * @return Nouvelles coordonnees dans un {@code int[]} tel que {@code [ newStartX, newStartY,
   *     newCurrentX, newCurrentY ]}
   */
  public static int[] correctCoordinates(int startX, int startY, int currentX, int currentY) {
    return new int[] {
      Math.min(startX, currentX),
      Math.min(startY, currentY),
      Math.max(startX, currentX),
      Math.max(startY, currentY)
    };
  }

  /** {@inheritDoc} */
  @Override
  public void use(Graphics2D g2d) {
    int[] coords =
        WToolShape.correctCoordinates(getStartX(), getStartY(), getCurrentX(), getCurrentY());

    g2d.setComposite(AlphaComposite.SrcOver);
    g2d.draw(createShape(coords[0], coords[1], coords[2] - coords[0], coords[3] - coords[1]));
  }

  /**
   * Methode permettant de definir la forme geometrique a dessiner
   *
   * @param x L'origine de la forme en x
   * @param y L'origine de la forme en y
   * @param width La longueur horizontale de la forme
   * @param height La longueur verticale de la forme
   * @return Le {@code Shape} a dessiner
   */
  public abstract Shape createShape(int x, int y, int width, int height);
}
