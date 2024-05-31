package com.e24.wolke.filesystem;

import java.io.File;

/**
 * Constantes pour le système de fichiers.
 *
 * @author Nilon123456789
 */
public class WFileSystemConstant {
  /** chemin du fichier utilisateur */
  public static final String SAVE_PATH =
      System.getProperty("user.home") + File.separator + "wolke" + File.separator;

  /** Constructeur privé pour empêcher l'instanciation de la classe */
  private WFileSystemConstant() {
    throw new IllegalStateException("Utility class");
  }
}
