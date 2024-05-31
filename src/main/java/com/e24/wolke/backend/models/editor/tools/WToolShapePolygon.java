package com.e24.wolke.backend.models.editor.tools;

import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import java.awt.Polygon;
import java.awt.Shape;

/**
 * La classe {@code WToolShapePolygon} permet de dessiner un polygone regulier avec un nombre
 * quelconque de cotes.
 *
 * @author MeriBouisri
 */
public class WToolShapePolygon extends WToolShape {

  /** Le {@code ToolType} de cet outil */
  public static final ToolType TOOL_TYPE = ToolType.POLYGON;

  /** Le nombre de cotes du polygone */
  private int sides = 5;

  /** Construction d'un {@code WToolShapePolygon} */
  public WToolShapePolygon() {
    super(WToolShapePolygon.TOOL_TYPE);
  }

  /**
   * Setter pour {@code this#sides}, le nombre de cotes du polygone regulier a dessiner
   *
   * @param sides Le nombre de cotes du polygone regulier a dessiner
   */
  public void setSides(int sides) {
    this.sides = sides;
  }

  /**
   * Getter pour {@code this#sides}, le nombre de cotes du polygone regulier a dessiner
   *
   * @return Le nombre de cotes du polygone regulier a dessiner
   */
  public int getSides() {
    return sides;
  }

  /**
   * Genere un polygone regulier avec un nombre quelconque de cotes
   *
   * @param x Coordonnee en x du cote en haut a gauche du polygone
   * @param y Coordonnee en y du cote en haut a gauche du polygone
   * @param width Longueur horizontale du polygone
   * @param height Longueur verticale du polygone
   * @return Un nouveau {@code Polygon}
   */
  private Shape generatePolygon(int x, int y, int width, int height) {

    double radius = (double) (width > height ? width : height) / 2;
    double theta = 2.0 * Math.PI / this.sides;

    int[] xpoints = new int[sides];
    int[] ypoints = new int[sides];

    // Translate, then scale
    for (int i = 0; i < sides; i++) {
      xpoints[i] = (int) ((radius * Math.cos(i * theta)) + (x + width / 2.0));
      ypoints[i] = (int) ((radius * Math.sin(i * theta)) + (y + height / 2.0));
    }

    return new Polygon(xpoints, ypoints, sides);
  }

  /** {@inheritDoc} */
  @Override
  public Shape createShape(int x, int y, int width, int height) {
    return this.generatePolygon(x, y, width, height);
  }
}
