package com.e24.wolke.filesystem.properties;

/**
 * L'interface {@code WPropertyHandler} permet aux classes qui l'implementent de definir la lecture
 * et l'ecriture d'un fichier de proprietes
 *
 * @author MeriBouisri
 */
public interface WPropertyProcessor {
  /** Methode permettant de lire les donnees d'un fichier de proprietes */
  void readProperties();

  /** Methode permettant d'ecrire les donnees a un fichier de proprietes */
  void writeProperties();
}
