package com.e24.wolke.utils.pool;

import com.e24.wolke.utils.math.WDoubleMatrix;

/**
 * DoubleMatrixPool.java
 *
 * <p>Cette classe est responsable de contenir les matrices de double dans une piscine
 *
 * @author Nilon123456789
 */
public class DoubleMatrixPool extends ObjectPool<WDoubleMatrix> {

  /** Nombre minimal d'objet dans la piscine */
  private static final int DEFAULT_MIN_SIZE = 3;

  /** Nombre maximal d'objet dans la piscine */
  private static final int DEFAULT_MAX_SIZE = 10;

  /** La longeur de la matrice */
  private final int xLength;

  /** La largeur de la matrice */
  private final int yLength;

  /**
   * Constructeur de la piscine de matrice de double
   *
   * @param xLength La taille en X
   * @param yLength La taille en Y
   */
  // Nils Lahye
  public DoubleMatrixPool(int xLength, int yLength) {
    super(DEFAULT_MIN_SIZE, DEFAULT_MAX_SIZE);

    this.xLength = xLength;
    this.yLength = yLength;

    initialize();
  }

  /**
   * Constructeur de la piscine de matrice de double
   *
   * @param minSize Nombre minimal d'objet dans la piscine
   * @param maxLength Nombre maximal d'objet dans la piscine
   * @param xLength La taille en X
   * @param yLength La taille en Y
   */
  public DoubleMatrixPool(int minSize, int maxLength, int xLength, int yLength) {
    super(minSize, maxLength);

    this.xLength = xLength;
    this.yLength = yLength;

    initialize();
  }

  /**
   * Retourne la taille en X de la matrice
   *
   * @return La taille en X de la matrice
   */
  public int getXLength() {
    return this.xLength;
  }

  /**
   * Retourne la taille en Y de la matrice
   *
   * @return La taille en Y de la matrice
   */
  public int getYLength() {
    return this.yLength;
  }

  /**
   * Retourne la taille de la matrice
   *
   * @return La taille de la matrice
   */
  public int getSize() {
    return this.xLength * this.yLength;
  }

  /**
   * Créer une nouvelle matrice de double
   *
   * @return La matrice de double
   */
  @Override
  protected WDoubleMatrix createObject() {
    return new WDoubleMatrix(this.xLength, this.yLength);
  }

  /**
   * Retourne une matrice de double à la piscine
   *
   * @param object La matrice de double à retourner
   * @throws IllegalArgumentException Si l'array de double retourné n'a pas la bonne taille
   */
  @Override
  public void returnObject(WDoubleMatrix object) {
    if (object.getXLength() != this.xLength || object.getYLength() != this.yLength) {
      throw new IllegalArgumentException("La matrice de double n'a pas la bonne taille");
    }

    super.returnObject(object);
  }
}
