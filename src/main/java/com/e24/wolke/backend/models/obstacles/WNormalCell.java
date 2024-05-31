package com.e24.wolke.backend.models.obstacles;

import com.e24.wolke.utils.math.WMath;
import com.e24.wolke.utils.math.WVector2D;

/**
 * La classe {@code WNormalCell} permet de memoriser les normales appliquees par les {@code
 * WObstacleCell} adjacentes a une cellule.
 *
 * @author MeriBouisri
 */
public class WNormalCell extends WCell {

  /** Valeur associee a un angle d'un vecteur nul */
  public static final double NULL_ANGLE = Double.NaN;

  /** Valeur de l'angle de la normale */
  private double normalOrientation = WNormalCell.NULL_ANGLE;

  /** Construction d'un {@code WNormalCell} par defaut */
  public WNormalCell() {
    super();
  }

  /**
   * Construction d'une {@code WNormalCell} avec une position
   *
   * @param x La position en x de la cellule
   * @param y La position en y de la cellule
   */
  public WNormalCell(int x, int y) {
    super(x, y);
  }

  /**
   * Construction d'une {@code WNormalCell} avec une position
   *
   * @param x La position en x de la cellule
   * @param y La position en y de la cellule
   * @param normalOrientation L'orientation initial de la normale, en radians
   */
  public WNormalCell(int x, int y, double normalOrientation) {
    super(x, y);
    setNormalOrientation(normalOrientation);
  }

  /**
   * Getter pour {@code this#normalOrientation}
   *
   * @return La valeur de l'angle de la normale appliquee autour de cette cellule
   */
  public double getNormalOrientation() {
    return normalOrientation;
  }

  /**
   * Setter pour {@code this#normalOrientation}. L'angle est normalisé, tel que defini par la
   * methode {@code WMath#normalizeAngle(double)}
   *
   * @param normalOrientation La nouvelle valeur de l'angle de la force normale appliquee par les
   *     cellules voisines de cette cellule
   * @see WMath#normalizeAngle(double)
   */
  public void setNormalOrientation(double normalOrientation) {
    this.normalOrientation = WMath.normalizeAngle(normalOrientation);
  }

  /**
   * Methode permettant d'obtenir un objet {@code Vector2} a partir de l'angle de la normale {@code
   * this#normalOrientation}.
   *
   * @return Le {@code Vector2} a partir de l'angle de la normale, {@code Vector2#ZERO_VECTOR} si
   *     {@code this#normalOrientation} est {@code NaN}
   */
  public WVector2D getVector() {

    if (isNullOrientation()) return WVector2D.ZERO_VECTOR;

    return new WVector2D(normalOrientation);
  }

  /**
   * Methode permettant d'ajouter une force normale a l'angle du vecteur force memorisé dans {@code
   * this#normalOrientation}. {@code this#normalOrientation} prend la valeur de l'angle du vecteur
   * resultant.
   *
   * @param angle Angle de la normale a ajouter a {@code this#normalOrientation} par addition de
   *     vecteur
   * @return L'angle du vecteur resultant de cette operation. Retourne {@code
   *     WNormalCell#NULL_ANGLE} si la resultante est un vecteur nul.
   */
  public double addNormalOrientation(double angle) {
    if (!this.isNullOrientation())
      angle = WNormalCell.addVectorOrientation(this.normalOrientation, angle);

    setNormalOrientation(angle);

    return this.getNormalOrientation();
  }

  /**
   * Methode permettant d'additionner deux vecteurs selon leur angle
   *
   * @param theta1 L'angle du premier vecteur a additionner
   * @param theta2 L'angle du deuxieme vecteur a additionner
   * @return Angle du vecteur resultant de l'addition. Retourne {@code WNormalCell#NULL_ANGLE} si le
   *     vecteur resultant est nul
   * @see WNormalCell#NULL_ANGLE Angle du vecteur nul est {@code Double#NaN}
   */
  public static double addVectorOrientation(double theta1, double theta2) {
    WVector2D vResult = WVector2D.add(new WVector2D(theta1), new WVector2D(theta2));

    if (vResult.isNearlyZeroVector()) return WNormalCell.NULL_ANGLE;

    return vResult.getAngle();
  }

  /**
   * Methode permettant de determine si l'angle passe en parametre est associé à un vecteur nul. Un
   * vecteur de force normale nul signifie qu'aucune force normale n'est appliquee par les cellules
   * voisines a cette cellule.
   *
   * @return {@code true} si l'angle passe en parametre est associé à un vecteur nul
   */
  public boolean isNullOrientation() {
    return WNormalCell.isNullOrientation(this.normalOrientation);
  }

  /**
   * Methode permettant de determine si l'angle passe en parametre est associé à un vecteur nul.
   *
   * @param angle L'angle par rapport a l'horizontale du vecteur a
   * @return {@code true} si l'angle passe en parametre est associé à un vecteur nul
   */
  public static boolean isNullOrientation(double angle) {
    // TODO : Risque de change l'implementation. pas sure si utiliser NaN c'est une bonne idee
    return Double.isNaN(angle);
  }

  /** Methode permettant de reinitialiser la valeur de la normale de cette instance */
  public void reset() {
    this.normalOrientation = WNormalCell.NULL_ANGLE;
  }

  /**
   * Methode permettant d'appliquer la normale de cette cellule a une cellule {@code WObstacleCell}
   * voisine
   *
   * @param obstacleCell La cellule a laquelle appliquer la normale
   */
  public void applyNormalTo(WObstacleCell obstacleCell) {
    if (!isNeighbor(obstacleCell)) return;

    if (isDiagonalNeighbor(obstacleCell) && !obstacleCell.getAcceptDiagonalNeighbors()) return;
  }
}
