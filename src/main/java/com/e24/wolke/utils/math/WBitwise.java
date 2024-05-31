package com.e24.wolke.utils.math;

/**
 * La classe {@code WBitwise} permet de regrouper les methodes utilitaires d'operations logiques sur
 * nombres binaires
 *
 * @author MeriBouisri
 */
public class WBitwise {

  /** Classe non instanciable */
  private WBitwise() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Methode permettant de fixer le bit a la position {@code p} a 1
   *
   * @param n Nombre a modifier
   * @param p Position du bit a fixer a 1
   * @return Le nouveau nombre apres l'operation
   */
  public static int set(int n, int p) {
    return n | (1 << p);
  }

  /**
   * Methode permettant de fixer le bit a la position {@code p} a une valeur binaire donnee
   *
   * @param n Nombre a modifier
   * @param p Position du bit a fixer
   * @param b Valeur du bit a fixer
   * @return Le nouveau nombre apres l'operation
   */
  public static int set(int n, int p, int b) {
    return (n & ~(1 << p)) | (b << p);
  }

  /**
   * Methode permettant de fixer le bit a la position {@code p} a 0
   *
   * @param n Nombre a modifier
   * @param p Position du bit a fixer
   * @return Le nouveau nombre apres l'operation
   */
  public static int unset(int n, int p) {
    return n & ~(1 << p);
  }

  /**
   * Methode permettant de toggle le bit a la position {@code p}
   *
   * @param n Nombre a modifier
   * @param p Position du bit a toggle
   * @return Le nouveau nombre apres l'operation
   */
  public static int flip(int n, int p) {
    return n ^ (1 << p);
  }

  /**
   * Methode permettant d'extraire la valeur d'un bit a la position donnee
   *
   * @param n Nombre qui contient le bit a extraire
   * @param p Position du bit a extraire
   * @return Valeur binaire (0 ou 1) representant l'etat du bit a la position donnee
   */
  public static int extract(int n, int p) {
    return (n & (1 << p)) >> p;
  }

  /**
   * Methode permettant de generer un bitmask de 1 avec un nombre donne de bits
   *
   * @param n Nombre de bits dans le bitmask
   * @return Un bitmask de 1 avec un nombre donne de bits
   */
  public static int mask(int n) {
    return (1 << n) - 1;
  }

  /**
   * Methode permettant de calculer le nombre de bits fixe a 1 dans un nombre
   *
   * @param n Le nombre dans lequel les bits 1 doivent etre comptes
   * @return Le nombre de bits fixes a 1 dans le nombre passe en parametre
   */
  public static int countSetBits(int n) {
    int set = 0;

    while (n != 0) {
      set += (n & 1);
      n >>= 1;
    }

    return set;
  }

  /**
   * Methode permettant d'effectuer un shift circulaire sur la valeur passee en parametre.
   *
   * @param n La valeur sur laquelle le shift circulaire sera effectue
   * @param p Le nombre de positions a shift
   * @param size Le nombre de bits a travers lesquels le shift est effectue
   * @return La valeur du nombre passe en parametre apres l'operation
   */
  public static int rotate(int n, int p, int size) {
    return ((n >> p) | (n << (size - p))) & WBitwise.mask(size);
  }
}
