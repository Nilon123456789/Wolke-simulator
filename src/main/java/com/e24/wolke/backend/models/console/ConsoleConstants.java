package com.e24.wolke.backend.models.console;

import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.filesystem.properties.SettingsPropertiesManager;
import org.apache.logging.log4j.Level;

/**
 * La classe {@code ConsoleConstants} regroupe les constantes liees au fonctionnement de la console
 * et des filtrages de logs.
 *
 * @author n-o-o-d-l-e
 * @author Nilon123456789
 */
public class ConsoleConstants {

  /** Le niveau de log par defaut */
  public static final WLogLevel DEFAULT_LOG_FILTER =
      WLogLevel.getWLogLevel(
          Level.toLevel(SettingsPropertiesManager.INSTANCE.getStringProperty("console.logLevel")));

  /** La visibilite de la console par defaut */
  public static final boolean DEFAULT_CONSOLE_VISIBILITY =
      SettingsPropertiesManager.INSTANCE.getBooleanProperty("console.showConsole");

  /** Si la fenêtre de la console doit floter */
  public static final boolean DEFAULT_CONSOLE_FLOATING =
      SettingsPropertiesManager.INSTANCE.getBooleanProperty("console.alwaysOnTop");

  /** Classe non instanciable */
  private ConsoleConstants() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Enum regroupant les differents niveaux de filtrage des logs.
   *
   * @author n-o-o-d-l-e
   * @author Nilon123456789
   */
  public enum WLogLevel {
    /** Tous les logs */
    ALL("ui.console.log_levels.all", Level.ALL),

    /** Les logs de trace */
    TRACE("ui.console.log_levels.trace", Level.TRACE),

    /** Les logs de debug */
    DEBUG("ui.console.log_levels.debug", Level.DEBUG),

    /** Les logs d'info */
    INFO("ui.console.log_levels.info", Level.INFO),

    /** Les logs de warning */
    WARN("ui.console.log_levels.warn", Level.WARN),

    /** Les logs d'erreur */
    ERROR("ui.console.log_levels.error", Level.ERROR),

    /** Les logs de trace */
    FATAL("ui.console.log_levels.fatal", Level.FATAL);

    /** Le nom de la propriété de localisation */
    private final String localeProperty;

    /** Le niveau de log */
    private final Level level;

    /**
     * Construction d'un élément de {@code WLogLevel} avec une propriété de localisation de langue
     * et un niveau de log.
     *
     * @param localeProperty Nom de la propriété de localisation
     * @param level Niveau de log
     */
    WLogLevel(String localeProperty, Level level) {
      this.localeProperty = localeProperty;
      this.level = level;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return LocaleManager.getLocaleResourceBundle().getString(localeProperty);
    }

    /**
     * Retourne le niveau de log associe a l'enum.
     *
     * @return Le niveau de log
     */
    public Level getLevel() {
      return level;
    }

    /**
     * Retourne la valeur de l'enum associe a un niveau de log ou {@code INFO} si le niveau de log
     * n'est pas reconnu.
     *
     * @param level Niveau de log
     * @return L'enum associe
     */
    public static WLogLevel getWLogLevel(Level level) {
      for (WLogLevel wLogLevel : WLogLevel.values()) {
        if (wLogLevel.getLevel().equals(level)) {
          return wLogLevel;
        }
      }
      return WLogLevel.INFO;
    }
  }
}
