package com.e24.wolke.filesystem.properties;

import com.e24.wolke.backend.models.application.LocaleManager;

/**
 * Classe permettant de charger les paramètre de l'application
 *
 * @author Nilon123456789
 */
public class SettingsPropertiesManager extends PropertiesManager {

  /** Instance unique de la classe */
  public static final SettingsPropertiesManager INSTANCE = new SettingsPropertiesManager();

  /** version actuel du fichier */
  public static final String FILE_VERSION = "1.0.0";

  /** Nom du fichier de configuration */
  private static final String FILE_NAME = "settings.properties";

  /** Chemin du fichier de configuration par défaut */
  private static final String DEFAULT_FILE_PATH = "config/";

  /** Classe non instanciable hors de la classe */
  SettingsPropertiesManager() {
    super(
        DEFAULT_FILE_PATH,
        FILE_NAME,
        FILE_VERSION,
        true,
        LocaleManager.getLocaleResourceBundle()
            .getString("settingsPropertiesManager.save_comment"));
  }
}
