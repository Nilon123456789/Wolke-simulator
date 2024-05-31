package com.e24.wolke.backend.simulation.physics;

import com.e24.wolke.utils.pool.ObjectPool;

/**
 * ParticleMatrixPool.java
 *
 * <p>Cette classe est responsable de contenir les matrices de particules réutilisables
 *
 * @author Nilon123456789
 */
public class ParticleMatrixPool extends ObjectPool<ParticleMatrix> {

  /** Taille en x de la matrice de particules */
  private final int xLength;

  /** Taille en y de la matrice de particules */
  private final int yLength;

  /**
   * Constructeur de la classe ParticleMatrixPool
   *
   * @param xLength Taille en x de la matrice de particules
   * @param yLength Taille en y de la matrice de particules
   * @param minSize Nombre minimal d'objet dans la piscine
   * @param maxLength Nombre maximal d'objet dans la piscine
   */
  public ParticleMatrixPool(int xLength, int yLength, int minSize, int maxLength) {
    super(minSize, maxLength);

    this.xLength = xLength;
    this.yLength = yLength;

    initialize();
  }

  /**
   * Retourne la taille en x de la matrice de particules
   *
   * @return La taille en x de la matrice de particules
   */
  public int getXLength() {
    return xLength;
  }

  /**
   * Retourne la taille en y de la matrice de particules
   *
   * @return La taille en y de la matrice de particules
   */
  public int getYLength() {
    return yLength;
  }

  /**
   * Retourne la taille de la matrice de particules
   *
   * @return La taille de la matrice de particules [xLength, yLength]
   */
  public int[] getLength() {
    return new int[] {this.xLength, this.yLength};
  }

  /** Créer une nouvelle matrice de particules */
  @Override
  protected ParticleMatrix createObject() {
    return new ParticleMatrix(xLength, yLength);
  }

  /**
   * Retourne une matrice de particules dans la piscine
   *
   * @param object La matrice de particules à retourner
   * @throws IllegalArgumentException Si la matrice de particules retournée n'a pas la bonne taille
   */
  @Override
  public void returnObject(ParticleMatrix object) {
    if (object == null) return;
    if (object.getXLength() != xLength || object.getYLength() != yLength)
      throw new IllegalArgumentException(
          "La matrice de particules retournée n'a pas la bonne taille");
    super.returnObject(object);
  }
}
