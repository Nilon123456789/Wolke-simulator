package com.e24.wolke.filesystem.scenes;

import java.io.FilenameFilter;

/**
 * La classe {@code WSceneConstants} regroupe les constantes reliees au scenes
 *
 * @author MeriBouisri
 */
public final class WSceneConstants {

  /** Constructeur privé pour empêcher l'instanciation de la classe */
  private WSceneConstants() {
    throw new IllegalStateException("Utility class");
  }

  /** Le nom de l'extension du fichier de format Wolke */
  public static final String WOLKE_EXTENSION = "wlks";

  /** Le nom du fichier qui contient les proprietes de la simulation */
  public static final String SIMULATION_PROPERTIES_FILENAME = "simulation.properties";

  /** Le nom du fichier qui contient les proprietes du rendu */
  public static final String RENDERER_PROPERTIES_FILENAME = "renderer.properties";

  /** Le nom du fichier qui contient l'image de l'editeur */
  public static final String IMAGE_FILENAME = "editor.png";

  /** FileFilter pour les fichiers de format Wolke */
  public static final FilenameFilter WOLKE_FILE_FILTER =
      (dir, name) -> name.endsWith("." + WOLKE_EXTENSION);
}
