package com.e24.wolke.backend.models.application;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code LocaleManager} permet d'abstraire la gestion des packets de langue
 *
 * @author MeriBouisri
 */
public final class LocaleManager {

  /** L'emplacement de la ressources de localisation */
  public static final String LOCALE_BUNDLE_PATH = "localization.messages";

  /** Gestionnaire de ressources qui charge les packets de langues */
  private static ResourceBundle localeResourceBundle = ResourceBundle.getBundle(LOCALE_BUNDLE_PATH);

  /** Logger de la classe */
  protected static final Logger LOGGER = LogManager.getLogger(LocaleManager.class.getSimpleName());

  /** Classe non instanciable */
  private LocaleManager() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Methode permettant d'actualiser la localisation de l'application
   *
   * @param locale La localisation a appliquer
   */
  public static void update(Locale locale) {
    LocaleManager.localeResourceBundle =
        ResourceBundle.getBundle(LocaleManager.LOCALE_BUNDLE_PATH, locale);
    LocaleManager.LOGGER.info(
        localeResourceBundle.getString("localeManager.languageChanged"), locale.getLanguage());
    JOptionPane.setDefaultLocale(locale);
  }

  /**
   * Getter pour la ressource statique {@code localeResourceBundle}.
   *
   * @return Le {@code ResourceBundle} permettant de gerer la localisation de l'application
   */
  public static ResourceBundle getLocaleResourceBundle() {
    return LocaleManager.localeResourceBundle;
  }
}
