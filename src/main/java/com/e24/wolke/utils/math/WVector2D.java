package com.e24.wolke.utils.math;

/**
 * Vector2.java
 *
 * <p>Cette classe représente un vecteur 2D
 *
 * @author Nilon123456789
 * @author MeriBouisri
 * @author n-o-o-d-l-e
 */
public class WVector2D {

  /** Vecteur nul */
  public static final WVector2D ZERO_VECTOR = new WVector2D(0, 0);

  /** Composante x du vecteur */
  private double x;

  /** Composante y du vecteur */
  private double y;

  /**
   * Constructeur de la classe Vector2
   *
   * @param x La composante x du vecteur
   * @param y La composante y du vecteur
   */
  public WVector2D(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Construction d'un vecteur a partir d'un angle.
   *
   * @param angleRad L'angle du vecteur par rapport a l'axe horizontal, en radians
   */
  public WVector2D(double angleRad) {
    x = Math.cos(angleRad);
    y = Math.sin(angleRad);
  }

  /** Constructeur de la classe Vector2 */
  public WVector2D() {
    this(0, 0);
  }

  /**
   * Retourne la composante x du vecteur
   *
   * @return La composante x du vecteur
   */
  public double getX() {
    return x;
  }

  /**
   * Modifie la composante x du vecteur
   *
   * @param newX La nouvelle composante x du vecteur
   */
  public void setX(double newX) {
    x = newX;
  }

  /**
   * Retourne la composante y du vecteur
   *
   * @return La composante y du vecteur
   */
  public double getY() {
    return y;
  }

  /**
   * Modifie la composante y du vecteur
   *
   * @param newY La nouvelle composante y du vecteur
   */
  public void setY(double newY) {
    y = newY;
  }

  /**
   * Retourne les deux composantes du vecteur
   *
   * @return Les deux composantes du vecteur {@code [x, y]}
   */
  public double[] getComponents() {
    return new double[] {this.x, this.y};
  }

  /**
   * Modifie les deux composantes du vecteur
   *
   * @param newComponents Les deux nouvelles composantes du vecteur
   */
  public void setComponents(double[] newComponents) {
    x = newComponents[0];
    y = newComponents[1];
  }

  /**
   * Modifie les deux composantes du vecteur
   *
   * @param newX La nouvelle composante x du vecteur
   * @param newY La nouvelle composante y du vecteur
   */
  public void setComponents(double newX, double newY) {
    x = newX;
    y = newY;
  }

  /**
   * Retourne la norme du vecteur
   *
   * @return La norme du vecteur
   */
  public double getNorm() {
    return Math.sqrt(this.x * this.x + this.y * this.y);
  }

  /**
   * Retourne le vecteur normalisé
   *
   * @return Le vecteur normalisé
   */
  public WVector2D normalize() {
    double norm = getNorm();
    if (norm == 0) return new WVector2D(0, 0);
    return new WVector2D(getX() / norm, getY() / norm);
  }

  /**
   * Retourne le module du vecteur
   *
   * @return Le module du vecteur
   */
  public double modulus() {
    return Math.sqrt(this.x * this.x + this.y * this.y);
  }

  /**
   * Additionne un vecteur à ce vecteur
   *
   * @param vector Le vecteur à ajouter
   * @return Le vecteur résultant de l'addition
   */
  public WVector2D add(WVector2D vector) {
    return new WVector2D(x + vector.getX(), y + vector.getY());
  }

  /**
   * Methode statique permettant d'additionner deux vecteurs ensemble
   *
   * @param v1 Le premier vecteur a additioner
   * @param v2 Le deuxieme vecteur a additionner
   * @return Un nouveau vecteur resultant de l'addition des deux vecteurs
   */
  public static WVector2D add(WVector2D v1, WVector2D v2) {
    return new WVector2D(v1.getX() + v2.getX(), v1.getY() + v2.getY());
  }

  /**
   * Soustrait un vecteur à ce vecteur
   *
   * @param vector Le vecteur à soustraire
   * @return Le vecteur résultant de la soustraction
   */
  public WVector2D substract(WVector2D vector) {
    return new WVector2D(x - vector.getX(), y - vector.getY());
  }

  /**
   * Soustraction de deux vecteurs
   *
   * @param v1 Le premier vecteur a soustraire
   * @param v2 Le deuxieme vecteur a soustraire du premier
   * @return Unouveau vecteur resultant de la soustraction des deux vecteurs
   */
  public static WVector2D substract(WVector2D v1, WVector2D v2) {
    return new WVector2D(v1.getX() - v2.getX(), v1.getY() - v2.getY());
  }

  /**
   * Multiplie ce vecteur par un scalaire
   *
   * @param scalar Le scalaire par lequel multiplier
   * @return Le vecteur résultant de la multiplication
   */
  public WVector2D multiply(double scalar) {
    return new WVector2D(x * scalar, y * scalar);
  }

  /**
   * Retourne le module d'un vecteur aux composants (x, y)
   *
   * @param x Le composant x d'un vecteur
   * @param y Le composant y d'un vecteur
   * @return Le module du vecteur
   */
  public static double modulus(int x, int y) {
    return Math.sqrt(x * x + y * y);
  }

  /**
   * Methode utilitaire qui retourne la distance entre deux vecteurs aux composants (x1, y1) et (x2,
   * y2)
   *
   * @param x1 Le composant x du premier vecteur
   * @param y1 Le composant y du premier vecteur
   * @param x2 Le composant x du deuxieme vecteur
   * @param y2 Le composant y du deuxieme vecteur
   * @return La distance entre les vecteurs
   */
  public static double distanceBetween(int x1, int y1, int x2, int y2) {
    int dx = x2 - x1;
    int dy = y2 - y1;

    return Math.sqrt(dx * dx + dy * dy);
  }

  /**
   * Divise ce vecteur par un scalaire
   *
   * @param scalar Le scalaire par lequel diviser
   * @throws IllegalArgumentException Si le scalaire est égal à 0
   * @return Le vecteur résultant de la division
   */
  public WVector2D div(double scalar) {
    if (scalar == 0) throw new IllegalArgumentException("Division par zéro");

    return new WVector2D(x / scalar, y / scalar);
  }

  /**
   * Fait le produit scalaire de ce vecteur avec un autre vecteur
   *
   * @param vector Le vecteur avec lequel faire le produit scalaire
   * @return Le produit scalaire des deux vecteurs
   */
  public double dot(WVector2D vector) {
    return x * vector.getX() + y * vector.getY();
  }

  /**
   * Retroune l'angle entre les deux vecteurs
   *
   * @param vector Le vecteur avec lequel trouver l'angle
   * @return L'angle entre les deux vecteurs en radians
   */
  public double getAngle(WVector2D vector) {
    return Math.acos(dot(vector) / (getNorm() * vector.getNorm()));
  }

  /**
   * Retourne l'angle entre ce vecteur et l'axe horizontal, positif vers la droite
   *
   * @return Angle normalisé a [-pi, pi] entre ce vecteur et l'horizontale, en radians
   * @see Math#atan2(double, double)
   */
  public double getAngle() {
    return Math.atan2(y, x);
  }

  /**
   * Methode permettant de calculer la distance entre deux vecteurs
   *
   * @param v Le premier vecteur
   * @return La distance entre les deux vecteurs
   */
  public double distanceBetween(WVector2D v) {
    return WVector2D.distanceBetween(this, v);
  }

  /**
   * Methode permettant de calculer la distance entre deux vecteurs
   *
   * @param v1 Le premier vecteur
   * @param v2 Le deuxieme vecteur
   * @return La distance entre les deux vecteurs
   */
  public static double distanceBetween(WVector2D v1, WVector2D v2) {
    return v2.substract(v1).getNorm();
  }

  /**
   * Retourne si les vecteurs sont perpendiculaires par rapport a l'autre
   *
   * @param v Le vecteur a comparer
   * @return {@code true} si les vecteurs sont perpendiculaires, tel que v1.dot(v2) == 0
   */
  public boolean isPerpendicular(WVector2D v) {
    return WMath.nearlyZero(dot(v));
  }

  /**
   * Methode permettant de determiner si ce vecteur est nul
   *
   * @return Si le vecteur est nul
   */
  public boolean isZeroVector() {
    return equals(WVector2D.ZERO_VECTOR);
  }

  /**
   * Methode permettant de determiner si ce vecteur est nul, avec {@code this#nearlyEquals(Vector2)}
   *
   * @return {@code true} si ce vecteur est approximativement un vecteur nul
   * @see WMath#nearlyEquals(double, double)
   */
  public boolean isNearlyZeroVector() {
    return nearlyEquals(WVector2D.ZERO_VECTOR);
  }

  /**
   * Methode permettant de comparer deux vecteurs avec un seuil de comparaison
   *
   * @param v Le vecteur a comparer
   * @return {@code true} si les vecteurs sont approximativement egaux
   * @see WMath#nearlyEquals(double, double)
   */
  public boolean nearlyEquals(WVector2D v) {
    return WMath.nearlyEquals(x, v.getX()) && WMath.nearlyEquals(y, v.getY());
  }

  /**
   * Projette ce vecteur sur un autre vecteur
   *
   * @param vector Le vecteur sur lequel projeter
   * @return Le vecteur projeté
   */
  public WVector2D project(WVector2D vector) {
    double dotProduct = dot(vector);
    double normSquared = vector.getNorm() * vector.getNorm();

    return new WVector2D(
        (dotProduct / normSquared) * vector.getX(), (dotProduct / normSquared) * vector.getY());
  }

  /**
   * Retourne vrai si les deux vecteurs sont égaux
   *
   * @param other L'autre objet à comparer
   * @return Vrai si les deux vecteurs sont égaux
   */
  @Override
  public boolean equals(Object other) {
    if (other.getClass() != getClass()) return false;

    WVector2D otherVector = (WVector2D) other;

    return x == otherVector.getX() && y == otherVector.getY();
  }

  /**
   * Retourne une représentation textuelle du vecteur
   *
   * @return Une représentation textuelle du vecteur
   */
  @Override
  public String toString() {
    return "\u001B[32m{" + this.x + ", " + this.y + "}\u001B[0m";

    // return "{" + x + ", " + y + "}";
  }

  /**
   * Retourne une copie du vecteur
   *
   * @return Une copie du vecteur
   */
  @Override
  public WVector2D clone() {
    return new WVector2D(x, y);
  }

  /**
   * Interpole deux vecteurs
   *
   * @param a Le premier vecteur
   * @param b Le deuxième vecteur
   * @param t Le coefficient d'interpolation
   * @return Le vecteur interpolé
   */
  public static WVector2D lerp(WVector2D a, WVector2D b, double t) {
    return new WVector2D(WMath.lerp(a.getX(), b.getX(), t), WMath.lerp(a.getY(), b.getY(), t));
  }
}
