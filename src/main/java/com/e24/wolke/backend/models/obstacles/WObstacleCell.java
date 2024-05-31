package com.e24.wolke.backend.models.obstacles;

import com.e24.wolke.backend.models.simulation.SimulationConstants.OBSTACLE_TYPE;
import com.e24.wolke.backend.models.simulation.SimulationConstants.UnitCircle;

/**
 * La classe {@code WObstacleCell} represente la plus petite unité d'un obstacle.
 *
 * @author MeriBouisri
 * @author n-o-o-d-l-e
 */
public class WObstacleCell extends WCell {

  /** La normale moyenne des normales autour de cet obstacle */
  private double averageNormal = UnitCircle.NONE.getAngleRad();

  /** Type de bordure de la cellule d'obstacle */
  private OBSTACLE_TYPE obstacleType = OBSTACLE_TYPE.STICK;

  /** Si le calcul des normales dans les cellules voisines doit inclure les voisins diagonaux */
  private boolean acceptDiagonalNeighbors = false;

  /** Construction d'un {@code WObstacleCell} par defaut. */
  public WObstacleCell() {
    this(-1, -1);
  }

  /**
   * Construction d'un {@code WObstacleCell} avec une position
   *
   * @param x La position en x de la cellule
   * @param y La position en y de la cellule
   */
  public WObstacleCell(int x, int y) {
    super(x, y);
  }

  /**
   * Construction d'un {@code WObstacleCell} avec une position et un type de bordure
   *
   * @param x La position en x de la cellule
   * @param y La position en y de la cellule
   * @param borderType Le type de bordure {@code OBSTACLE_TYPE}
   */
  public WObstacleCell(int x, int y, OBSTACLE_TYPE borderType) {
    this(x, y);
    obstacleType = borderType;
  }

  /**
   * Construction d'un {@code WObstacleCell} avec une position.
   *
   * @param x La position en x de la cellule
   * @param y La position en y de la cellule
   * @param acceptDiagonalNeighbors Si les voisins diagonaux sont pris en compte dans le calcul de
   *     la normale ou non
   */
  public WObstacleCell(int x, int y, boolean acceptDiagonalNeighbors) {
    this(x, y);
    this.acceptDiagonalNeighbors = acceptDiagonalNeighbors;
  }

  /**
   * Construction d'un {@code WObstacleCell} avec une position et un type de bordure.
   *
   * @param x La position en x de la cellule
   * @param y La position en y de la cellule
   * @param borderType Le type de bordure {@code OBSTACLE_TYPE}
   * @param acceptDiagonalNeighbors Si les voisins diagonaux sont pris en compte dans le calcul de
   *     la normale ou non
   */
  public WObstacleCell(int x, int y, OBSTACLE_TYPE borderType, boolean acceptDiagonalNeighbors) {
    this(x, y, borderType);
    this.acceptDiagonalNeighbors = acceptDiagonalNeighbors;
  }

  /**
   * Construction d'un {@code WObstacleCell} avec un {@code OBSTACLE_TYPE}
   *
   * @param borderType le {@code OBSTACLE_TYPE} de cette instance
   */
  public WObstacleCell(OBSTACLE_TYPE borderType) {
    this();
    obstacleType = borderType;
  }

  /**
   * Construction d'un {@code WObstacleCell} avec un {@code OBSTACLE_TYPE} et {@code
   * acceptDiagonalNeighbors}
   *
   * @param acceptDiagonalNeighbors Si les voisins diagonaux sont pris en compte dans le calcul de
   *     la normale ou non
   */
  public WObstacleCell(boolean acceptDiagonalNeighbors) {
    this();
    this.acceptDiagonalNeighbors = acceptDiagonalNeighbors;
  }

  /**
   * Construction d'un {@code WObstacleCell} avec un {@code OBSTACLE_TYPE} et {@code
   * acceptDiagonalNeighbors}
   *
   * @param obstacleType le {@code OBSTACLE_TYPE} de cette instance
   * @param acceptDiagonalNeighbors Si les voisins diagonaux sont pris en compte dans le calcul de
   *     la normale ou non
   */
  public WObstacleCell(OBSTACLE_TYPE obstacleType, boolean acceptDiagonalNeighbors) {
    this();
    this.obstacleType = obstacleType;
    this.acceptDiagonalNeighbors = acceptDiagonalNeighbors;
  }

  /**
   * Getter pour {@code this#borderType}, le type de bordure {@code OBSTACLE_TYPE} de cette cellule
   *
   * @return Le type de bordure {@code OBSTACLE_TYPE} de cette cellule
   */
  public OBSTACLE_TYPE getObstacleType() {
    return obstacleType;
  }

  /**
   * Setter pour {@code this#borderType}, le type de bordure {@code OBSTACLE_TYPE} de cette cellule
   *
   * @param borderType Le type de bordure {@code OBSTACLE_TYPE} de cette cellule
   */
  public void setObstacleType(OBSTACLE_TYPE borderType) {
    obstacleType = borderType;
  }

  /**
   * Getter pour {@code this#acceptDiagonalNeighbors}
   *
   * @return Si les voisins diagonaux sont pris en compte dans le calcul des normales dans les cases
   *     voisines, ou non
   */
  public boolean getAcceptDiagonalNeighbors() {
    return acceptDiagonalNeighbors;
  }

  /**
   * Setter pour {@code this#acceptDiagonalNeighbors}
   *
   * @param accept Si les voisins diagonaux sont pris en compte dans le calcul des normales dans les
   *     cases voisines, ou non
   */
  public void setAcceptDiagonalNeighbors(boolean accept) {
    acceptDiagonalNeighbors = accept;
  }

  /**
   * Setter pour {@code this#averageNormal}, la moyenne des normales appliquees autour de la
   * position de cet obstacle
   *
   * @param angleRad L'angle de l'orientation des normales moyennes appliquees autour de la position
   *     de cet obstacle
   */
  public void setAverageNormal(double angleRad) {
    averageNormal = angleRad;
  }

  /**
   * Getter pour {@code this#averageNormal}, la moyenne des normales appliquees autour de cet
   * obstacle
   *
   * @return La moyenne des normales appliquees autour de cette obstacle
   */
  public double getAverageNormal() {
    return averageNormal;
  }

  /**
   * Methode permettant de reintiialiser la normale a la position de cet obstacle a {@code
   * UnitCircle#NONE} (NaN)
   */
  public void resetAverageNormal() {
    averageNormal = UnitCircle.NONE.getAngleRad();
  }

  /**
   * Methode permettant a cette instance de {@code WObstacleCell} d'appliquer une force normale par
   * rapport a la cellule voisine {@code WNormalCell}
   *
   * @param normalCell Cellule voisine a ce {@code WObstacleCell} dans laquelle la valeur de la
   *     normale appliquee par les obstacles est memorisee
   */
  public void applyNormalTo(WNormalCell normalCell) {
    if (!isNeighbor(normalCell)) return;
    if (isDiagonalNeighbor(normalCell) && !acceptDiagonalNeighbors) return;

    double orientation = normalCell.getPositionVector().substract(getPositionVector()).getAngle();

    if (isHorizontalNeighbor(normalCell)) orientation = -orientation;

    if (isDiagonalNeighbor(normalCell)) orientation = -orientation;

    normalCell.addNormalOrientation(orientation);
  }

  /**
   * Méthode retournant une chaîne de caractères représentant le nom localisé du type d'obstacle
   *
   * @return Le nom localisé du type d'obstacle
   */
  @Override
  public String toString() {
    return obstacleType.toString();
  }
}
