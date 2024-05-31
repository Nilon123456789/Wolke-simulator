package com.e24.wolke.utils.math;

/**
 * WDoubleArray.java
 *
 * <p>Cette classe contient tout simplement une matrice de double.
 *
 * @author Nilon123456789
 * @author MeriBouisri
 */
public class WDoubleMatrix {

  /** La matrice d'objets */
  private double[] matrix;

  /** La longueur en x de la matrice, immuable */
  private final int xLength;

  /** La longueur en y de la matrice, immuable */
  private final int yLength;

  /** La taille de la matrice, immuable */
  private final int size;

  /**
   * Crée une matrice de double
   *
   * @param xLength Longueur de la matrice
   * @param yLength Largeur de la matrice
   */
  public WDoubleMatrix(int xLength, int yLength) {
    this.xLength = xLength;
    this.yLength = yLength;
    size = WMatrix1D.calculateMatrixSize(xLength, yLength);
    matrix = new double[size];
  }

  /**
   * Getter pour {@code this#matrix}
   *
   * @return La matrice d'elements de cette instance
   */
  public double[] getMatrix() {
    return matrix;
  }

  /**
   * Setter pour {@code this#matrix}
   *
   * @param matrix La nouvelle matrice d'elements
   * @throws IllegalArgumentException si la taille de la matrice passee en parametre n'est pas egale
   */
  public void setMatrix(double[] matrix) {
    if (matrix.length != size) throw new IllegalArgumentException("Matrix size mismatch");

    this.matrix = matrix;
  }

  /**
   * Getter pour {@code this#xLength}, la longueur en x de la matrice
   *
   * @return La longueur en x de la matrice
   */
  public int getXLength() {
    return xLength;
  }

  /**
   * Getter pour {@code this#yLength}, la longueur en y de la matrice
   *
   * @return La longueur en y de la matrice
   */
  public int getYLength() {
    return yLength;
  }

  /**
   * Getter pour {@code this#xLength} et {@code this#yLength}, la longueur en x et en y de la
   * matrice, dans la forme d'un tableau
   *
   * @return La longueur en x et en y de la matrice, dans la forme d'un tableau
   */
  public int[] getXYLength() {
    return new int[] {xLength, yLength};
  }

  /**
   * Getter pour {@code this#size}, la taille de la matrice
   *
   * @return La taille de la matrice
   */
  public int getSize() {
    return size;
  }
}
