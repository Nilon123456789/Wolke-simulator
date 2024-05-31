package com.e24.wolke.backend.models.obstacles;

import com.e24.wolke.utils.math.WMatrix1D;

/**
 * La classe {@code WNormalCellMatrix} represente une matrice contenant des objets {@code
 * WNormalCell}
 *
 * @author MeriBouisri
 */
public class WNormalCellMatrix extends WMatrix1D<WNormalCell> {

  /**
   * Construction d'un {@code WNormalMatrix} avec une longueur en x et en y
   *
   * @param xLength La longueur en x de la matrice de normales
   * @param yLength La longueur en y de la matrice de normales
   */
  public WNormalCellMatrix(int xLength, int yLength) {
    super(xLength, yLength);
  }

  /**
   * Construction d'un {@code WNormalMatrix} a partir d'un {@code WObstacleCellMatrix}.
   *
   * @param obstacles La matrice d'obstacles {@code WObstacleCellMatrix}
   */
  public WNormalCellMatrix(WObstacleCellMatrix obstacles) {
    super(obstacles.getXLength(), obstacles.getYLength());
  }

  /**
   * Retourne un {@code double[]} contenant les valeurs des angles des normales de la matrice
   *
   * @return Un {@code double[]} contenant les valeurs des angles des normales de la matrice
   */
  public double[] toDoubleArray() {
    double[] normals = new double[getSize()];
    for (int i = 0; i < normals.length; i++)
      normals[i] =
          (this.getElementAt(i) == null
              ? WNormalCell.NULL_ANGLE
              : getElementAt(i).getNormalOrientation());

    return normals;
  }

  /** {@inheritDoc} */
  @Override
  protected WNormalCell[] createGenericArray() {
    return new WNormalCell[getSize()];
  }
}
