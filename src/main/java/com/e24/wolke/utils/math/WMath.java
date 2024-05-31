package com.e24.wolke.utils.math;

/**
 * WMath.java
 *
 * <p>Cette classe contient des méthodes mathématiques utiles supplémentaires
 *
 * @author Nilon123456789
 * @author MeriBouisri
 * @author n-o-o-d-l-e
 */
public class WMath {

  /** Valeur de l'epsilon */
  public static final double EPSILON = 1.0e-10;

  /** Valeur de PI diviser par 2 */
  public static final double PI_OVER_2 = Math.PI / 2;

  /** Classe non instanciable */
  private WMath() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Interpolation linéaire
   *
   * @param a Première valeur
   * @param b Deuxième valeur
   * @param k Facteur d'interpolation (entre 0 et 1)
   * @return La valeur interpolée
   */
  public static double lerp(double a, double b, double k) {
    return a + k * (b - a);
  }

  /**
   * Interpolation bilinéaire
   *
   * @param a Première valeur
   * @param b Deuxième valeur
   * @param c Troisième valeur
   * @param d Quatrième valeur
   * @param k Facteur d'interpolation en x (entre 0 et 1)
   * @param l Facteur d'interpolation en y (entre 0 et 1)
   * @return La valeur interpolée
   */
  public static double bilerp(double a, double b, double c, double d, double k, double l) {
    return (1 - k) * (1 - l) * a + k * (1 - l) * b + (1 - k) * l * c + k * l * d;
  }

  /**
   * Calcule le module de deux composante x et y
   *
   * @param x composante x
   * @param y composante y
   * @return le module de x et y
   */
  public static double modulus(double x, double y) {
    return Math.sqrt(x * x + y * y);
  }

  /**
   * Effectue et retourne le modulo d'une valeur en s'assurant que le résultat est positif
   *
   * @param value Valeur à moduler
   * @param modulus Modulo
   * @return Le modulo de la valeur
   */
  public static int positiveModulus(int value, int modulus) {
    return (value % modulus + modulus) % modulus;
  }

  /**
   * Conversion d'une matrice en un string lisible
   *
   * @param x matrice à convertire
   * @param xLength longeur de la matrice en x
   * @param yLength longeur de la matrice en y
   * @return la matrice sous forme de texte
   */
  public static String arrayToString(double[] x, int xLength, int yLength) {
    int size = xLength * yLength;

    if (size != x.length)
      throw new ArrayIndexOutOfBoundsException("Calculated size not matching array size");

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < size; i++) {

      sb.append(x[i]);
      sb.append(" ");

      if (i % xLength == xLength - 1) sb.append("\n");
    }
    return sb.toString();
  }

  /**
   * Conversion d'une matrice en un string lisible
   *
   * @param x matrice à convertire
   * @param xLength longeur de la matrice en x
   * @param yLength longeur de la matrice en y
   * @return la matrice sous forme de texte
   */
  public static String arrayToString(int[] x, int xLength, int yLength) {
    int size = xLength * yLength;

    if (size != x.length)
      throw new ArrayIndexOutOfBoundsException("Calculated size not matching array size");

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < size; i++) {

      sb.append(x[i]);
      sb.append(" ");

      if (i % xLength == xLength - 1) sb.append("\n");
    }
    return sb.toString();
  }

  /**
   * Contraint une valeur entre deux bornes
   *
   * @param value Valeur à clamp
   * @param min Borne minimale
   * @param max Borne maximale
   * @return La valeur clampée
   */
  public static double clamp(double value, double min, double max) {
    return Math.max(min, Math.min(max, value));
  }

  /**
   * Contraint une valeur entre deux bornes
   *
   * @param value Valeur à clamp
   * @param min Borne minimale
   * @param max Borne maximale
   * @return La valeur clampée
   */
  public static int clamp(int value, int min, int max) {
    return Math.max(min, Math.min(max, value));
  }

  /**
   * Conversion de deux matrice en un string lisible [x, y]
   *
   * @param x matrice en x à convertire
   * @param y matrice en y à convertire
   * @param xLength longeur de la matrice en x
   * @param yLength longeur de la matrice en y
   * @return la matrice sous forme de texte
   */
  public static String arrayToString(double[] x, double[] y, int xLength, int yLength) {
    int size = xLength * yLength;

    if (size != x.length || size != y.length)
      throw new ArrayIndexOutOfBoundsException("Calculated size not matching array size");

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < size; i++) {

      sb.append("[");
      sb.append(x[i]);
      sb.append(", ");
      sb.append(y[i]);
      sb.append("]");

      if (i % xLength == xLength - 1) sb.append("\n");
    }
    return sb.toString();
  }

  /**
   * Methode permettant de normaliser une valeur d'angle en radians a une valeur entre [-pi, pi]
   *
   * @param angle L'angle en radians a normaliser
   * @return une valeur entre [-pi, pi] representant l'angle normalisé
   */
  public static double normalizeAngle(double angle) {
    return Math.atan2(Math.sin(angle), Math.cos(angle));
  }

  /**
   * Methode permettant de comparer un nombre avec zero, avec un seuil de difference {@code
   * WMath.EPSILON}
   *
   * @param a Nombre a comparer a zero
   * @return {@code true} a est approximativement egal a zero, selon le seuil de tolerance
   */
  public static boolean nearlyZero(double a) {
    return WMath.nearlyEquals(a, 0, WMath.EPSILON);
  }

  /**
   * Methode permettant de normaliser une valeur en utilisant un seuil de tolerance {@code
   * WMath.EPSILON} Exemple: si la valeur est approximativement egale a zero, la valeur est remise a
   * zero sinon, la valeur est retournée inchangée
   *
   * @param value Valeur a normaliser
   * @return la valeur normalisée
   */
  public static double normalize(double value) {
    if (WMath.nearlyZero(value)) return 0;
    return value;
  }

  /**
   * Methode permettant de comaprer deux nombres avec un seuil de difference {@code WMath.EPSILON}
   *
   * @param a Premier nombre a comparer
   * @param b Deuxieme nombre a comparer
   * @return {@code true} si a et b sont approximativement egaux, selon le seuil de tolerance {@code
   *     WMath.EPSILON}
   */
  public static boolean nearlyEquals(double a, double b) {
    return WMath.nearlyEquals(a, b, WMath.EPSILON);
  }

  /**
   * Methode permettant de comaprer deux nombres avec un seuil de difference epsilon, tel que a - b
   * &lt;= epsilon
   *
   * @param a Premier nombre a comparer
   * @param b Deuxieme nombre a comparer
   * @param epsilon Le seuil de tolerance pour la difference entre les deux nombres
   * @return {@code true} si les a et b sont approximativement egaux, selon le seuil de tolerance
   */
  public static boolean nearlyEquals(double a, double b, double epsilon) {
    return (Math.abs(a - b) <= epsilon);
  }

  /**
   * Methode permettant de calculer la moyenne mobile d'une valeur
   *
   * @param value Valeur a ajouter
   * @param average Moyenne actuelle
   * @param count Nombre de valeurs
   * @return La moyenne mobile
   */
  public static double movingAverage(double value, double average, int count) {
    return (average + value) / count;
  }
}
