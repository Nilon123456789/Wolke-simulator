package com.e24.wolke.filesystem.properties;

import java.util.Properties;

/**
 * La classe {@code WStandardPropertiesProcessor} permet de lire et d'ecrire des proprietes
 * specifiques avec un objet {@code Properties}
 *
 * @author MeriBouisri
 */
public interface WStandardPropertiesProcessor {

  /**
   * Methode permettant de convertir des proprietes a un {@code Properties}
   *
   * @return Le {@code Properties} standard avec les proprietes de cette instance
   */
  Properties writeStandardProperties();

  /**
   * Methode permettant d'extraire des proprietes specifiques d'un {@code Properties}
   *
   * @param properties Le {@code Properties} standard avec les proprietes a extraire
   */
  void readStandardProperties(Properties properties);
}
