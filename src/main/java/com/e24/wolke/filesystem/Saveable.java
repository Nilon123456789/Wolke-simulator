package com.e24.wolke.filesystem;

/**
 * L'interface {@code Saveable} permet de definir le comportement de sauvegarde d'un objet
 *
 * @author MeriBouisri
 */
public interface Saveable {
  /**
   * Methode permettant de sauvegarder les donnees d'un objet {@code Saveable}
   *
   * @return {@code true} si la sauvegarde a ete effectuee avec succes
   */
  boolean save();
}
