package com.e24.wolke.filesystem;

/**
 * L'interface {@code Loadable} permet de definir le comportement de chargement d'un objet qui
 * l'implemente
 *
 * @author MeriBouisri
 */
public interface Loadable {
  /**
   * Methode permettant de charger les donnees d'un objet
   *
   * @return {@code true} si le chargement a ete effectue avec succes
   */
  boolean load();
}
