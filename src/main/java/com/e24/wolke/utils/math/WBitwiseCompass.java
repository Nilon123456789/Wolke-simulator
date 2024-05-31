package com.e24.wolke.utils.math;

/**
 * La classe {@code WBitwiseCompass} permet de definir un systeme de directions ordinales en
 * utilisant des representations binaires et des operations bitwise.
 *
 * @author MeriBouisri
 */
public class WBitwiseCompass {

  /** Nombre de bits dans les donnees de direction */
  public static final int NUM_BITS = 4;

  /** Aucune direction */
  public static final byte NONE = 0b0;

  /** Nord */
  public static final byte MASK_N = 0b0001; // Offset : 0

  /** Est */
  public static final byte MASK_E = MASK_N << 1; // Offset : 1

  /** Sud */
  public static final byte MASK_S = MASK_E << 1; // Offset : 2

  /** Est */
  public static final byte MASK_W = MASK_S << 1; // Offset : 3

  /** Direction composee nord-est */
  public static final byte MASK_NE = MASK_N | MASK_E;

  /** Direction composee sud-est */
  public static final byte MASK_SE = MASK_S | MASK_E;

  /** Direction composee sud-ouest */
  public static final byte MASK_SW = MASK_S | MASK_W;

  /** Direction composee nord-ouest */
  public static final byte MASK_NW = MASK_N | MASK_W;

  /** Direction opposees nord-sud */
  public static final byte MASK_NS = MASK_N | MASK_S;

  /** Direction opposees est-ouest */
  public static final byte MASK_EW = MASK_E | MASK_W;

  /** Classe non instanciable */
  private WBitwiseCompass() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Methode permettant d'obtenir les composants du vecteur unitaire associe au nombre binaire n
   *
   * @param n Le nombre binaire a decoder pour obtenir les composantes du vecteur unitaire
   * @return un {@code int[2]} contenant les composants (x, y) du vecteur unitaire
   */
  public static int[] getVectorComponents(byte n) {
    return new int[] {
      // Extract bits for horizontal directions
      ((n & MASK_E) == 0 ? 0 : 1) - ((n & MASK_W) == 0 ? 0 : 1),

      // Extract bits for vertical directions
      ((n & MASK_N) == 0 ? 0 : 1) - ((n & MASK_S) == 0 ? 0 : 1)
    };
  }

  /**
   * Methode permettant de determiner si deux vecteurs sont opposes
   *
   * @param n1 Le premier nombre a comparer
   * @param n2 Le deuxieme nombre a comparer
   * @return 0 si les vecteurs sont opposes, 1 si les vecteurs ne sont pas opposes
   */
  public static int isOpposite(byte n1, byte n2) {
    return WBitwiseCompass.getOpposite(n1) == n2 ? 0 : 1;
  }

  /**
   * Methode permettant de calculer la direction opposee a celle representee par le nombre passe en
   * parametre.
   *
   * @param n Le nombre qui represente la direction dont l'opposee sera calculee
   * @return La valeur binaire de la direction representee par le nombre passe en parametre
   */
  public static byte getOpposite(byte n) {
    return (byte) WBitwise.rotate(n, 2, WBitwiseCompass.NUM_BITS);
  }
}
