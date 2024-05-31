package com.e24.wolke.backend.models.obstacles;

import com.e24.wolke.utils.math.WVector2D;

/**
 * La classe {@code WCell} permet de representer une cellule, avec une position.
 *
 * @author MeriBouisri
 */
public class WCell {

  /** Distance entre cette cellule et une cellule directement adjacente */
  public static final double ORTHOGONAL_CELL_DISTANCE = 1;

  /** Distance entre cette cellule et une cellule diagonale */
  public static final double DIAGONAL_CELL_DISTANCE = Math.sqrt(2);

  /** Position en x de la cellule. Valeur de -1 s'il n'y a aucune position */
  private int x;

  /** Position en y de la cellule d'obstacle. Valeur de -1 s'il n'y a aucune position */
  private int y;

  /**
   * Construction d'une {@code WCell} avec une position
   *
   * @param x La position en x de la cellule
   * @param y La position en y de la cellule
   */
  public WCell(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /** Construction d'un {@code WCell} sans position */
  public WCell() {
    this(-1, -1);
  }

  /**
   * Methode permettant de verifier si cette cellule est adjacente a cette cellule, que ce soit
   * orthogonalement ou diagonalement.
   *
   * @param cell La cellule a verifier
   * @return {@code true} si la cellule passee en parametre est une voisine a cette cellule
   */
  public boolean isNeighbor(WCell cell) {
    return this.isOrthogonalNeighbor(cell) || this.isDiagonalNeighbor(cell);
  }

  /**
   * Methode permettant de verifier si une cellule est directement a cote de cette cellule (gauche,
   * droite, haut, bas).
   *
   * @param cell La cellule a verifier
   * @return {@code true} si la cellule passee en parametre est une voisine orthoganle a cette
   *     cellule
   */
  public boolean isOrthogonalNeighbor(WCell cell) {
    return this.getPositionVector().distanceBetween(cell.getPositionVector())
        == WCell.ORTHOGONAL_CELL_DISTANCE;
  }

  /**
   * Methode permettant de verifier si une cellule est a cote de cette cellule, sur l'axe horizontal
   * (droite ou gauche)
   *
   * @param cell La cellule a verifier
   * @return {@code true} si la cellule passee en parametre est une voisine horizontalement a cette
   *     cellule
   */
  public boolean isHorizontalNeighbor(WCell cell) {
    return this.x == cell.getX();
  }

  /**
   * Methode permettant de verifier si une cellule est a cote de cette cellule, sur l'axe vertical
   * (haut ou bas)
   *
   * @param cell La cellule a verifier
   * @return {@code true} si la cellule passee en parametre est une voisine verticalement a cette
   *     cellule
   */
  public boolean isVerticalNeighbor(WCell cell) {
    return this.y == cell.getY();
  }

  /**
   * Methode permettant de verifier si cette cellule est situee diagonalement a cette cellule
   *
   * @param cell La cellule a verifier
   * @return {@code true} si la cellule passee en parametre est une voisine diagonale a cette
   *     cellule
   */
  public boolean isDiagonalNeighbor(WCell cell) {
    return this.getPositionVector().distanceBetween(cell.getPositionVector())
        == WCell.DIAGONAL_CELL_DISTANCE;
  }

  /**
   * Getter pour {@code this#x}, la position en x de cette cellule
   *
   * @return La position en x de cette cellule
   */
  public int getX() {
    return this.x;
  }

  /**
   * Getter pour {@code this#y}, la position en y de cette cellule
   *
   * @return La position en y de cette cellule dans
   */
  public int getY() {
    return this.y;
  }

  /**
   * Getter pour {@code this#x} et {@code this#y}, les coordonnees de la position de cette cellule
   *
   * @return La position en x et y de cette cellule
   */
  public int[] getPos() {
    return new int[] {this.x, this.y};
  }

  /**
   * Retourne la position de la cellule sous forme de vecteur {@code Vector2}
   *
   * @return {@code Vector2} contenant la position de la cellule
   */
  public WVector2D getPositionVector() {
    return new WVector2D(this.x, this.y);
  }

  /**
   * Setter pour {@code this#x}, la position en x de cette cellule
   *
   * @param x La position en x de cette cellule
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Setter pour {@code this#y}, la position en y de cette cellule
   *
   * @param y La position en y de cette cellule
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * Setter pour {@code this#x} et {@code this#y}, les coordonnees de la position de cette cellule
   *
   * @param x La position en x de cette cellule
   * @param y La position en y de cette cellule
   */
  public void setPos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Retourne si cette cellule a une position valide pour {@code this#x} et {@code this#y}
   *
   * @return Si cette cellule a une position valide pour {@code this#x} et {@code this#y}
   */
  public boolean hasPosition() {
    return !(this.x == -1 && this.y == -1);
  }

  // ==================
  // METHODES STATIQUES
  // ==================
  /**
   * Methode utilitaire permettant de verifier si une cellule aux coordonnees (x1, y1) est a cote de
   * la cellule aux coordonnees (x2, y2) (orthogonalement ou diagonalement)
   *
   * @param x1 La position en x de la premiere cellule
   * @param y1 La position en y de la premiere cellule
   * @param x2 La position en x de la deuxieme cellule
   * @param y2 La position en y de la deuxieme cellule
   * @return {@code true} si la cellule (x1, y1) est une voisine a la cellule (x2, y2)
   */
  public static boolean isNeighbor(int x1, int y1, int x2, int y2) {
    return Math.abs(x2 - x1) == 1 || Math.abs(y2 - y1) == 1;
  }

  /**
   * Methode utilitaire permettant de verifier si une cellule aux coordonnees (x1, y1) est a cote de
   * la cellule aux coordonnees (x2, y2) (haut, bas, droite, gauche)
   *
   * @param x1 La position en x de la premiere cellule
   * @param y1 La position en y de la premiere cellule
   * @param x2 La position en x de la deuxieme cellule
   * @param y2 La position en y de la deuxieme cellule
   * @return {@code true} si la cellule (x1, y1) est une voisine orthogonale a la cellule (x2, y2)
   */
  public static boolean isOrthogonalNeighbor(int x1, int y1, int x2, int y2) {
    return x1 == x2 || y1 == y2;
  }

  /**
   * Methode utilitaire permettant de verifier si une cellule aux coordonnees (x1, y1) est situee
   * diagonalement a la cellule aux coordonnees (x2, y2)
   *
   * @param x1 La position en x de la premiere cellule
   * @param y1 La position en y de la premiere cellule
   * @param x2 La position en x de la deuxieme cellule
   * @param y2 La position en y de la deuxieme cellule
   * @return {@code true} si la cellule (x1, y1) est une voisine diagonale a la cellule (x2, y2)
   */
  public static boolean isDiagonalNeighbor(int x1, int y1, int x2, int y2) {
    return Math.abs(x2 - x1) == 1 && Math.abs(y2 - y1) == 1;
  }

  /**
   * Methode utilitaire permettant de verifier si une cellule aux coordonnees (x1, y1) est a cote de
   * la cellule aux coordonnees (x2, y2), sur l'axe horizontal (droite ou gauche)
   *
   * @param x1 La position en x de la premiere cellule
   * @param y1 La position en y de la premiere cellule
   * @param x2 La position en x de la deuxieme cellule
   * @param y2 La position en y de la deuxieme cellule
   * @return {@code true} si la cellule (x1, y1) est une voisine horizontalement a la cellule (x2,
   *     y2)
   */
  public static boolean isHorizontalNeighbor(int x1, int y1, int x2, int y2) {
    return x1 == x2;
  }

  /**
   * Methode permettant de verifier si une cellule aux coordonnees (x1, y1) est a cote de la cellule
   * aux coordonnees (x2, y2), sur l'axe vertical (haut ou bas)
   *
   * @param x1 La position en x de la premiere cellule
   * @param y1 La position en y de la premiere cellule
   * @param x2 La position en x de la deuxieme cellule
   * @param y2 La position en y de la deuxieme cellule
   * @return {@code true} si la cellule (x1, y1) est une voisine verticalement a la cellule (x2, y2)
   */
  public static boolean isVerticalNeighbor(int x1, int y1, int x2, int y2) {
    return y1 == y2;
  }
}
