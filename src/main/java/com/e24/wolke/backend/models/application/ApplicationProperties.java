package com.e24.wolke.backend.models.application;

import com.e24.wolke.backend.models.WProperties;
import com.e24.wolke.backend.models.application.ApplicationConstants.ApplicationTab;
import com.e24.wolke.backend.models.application.ApplicationConstants.ColorMode;
import com.e24.wolke.backend.models.application.ApplicationConstants.Resolution;
import com.e24.wolke.filesystem.properties.PropertiesManager;
import com.e24.wolke.filesystem.properties.WPropertyKey;
import com.formdev.flatlaf.FlatDarkLaf;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code ApplicationProperties} permet de regrouper les champs relies au {@code
 * ApplicationModel}. Cette classe sert a la lecture et l'ecriture des proprietes avec un fichier,
 * ainsi qu'a la serialisation des donnees.
 *
 * @author MeriBouisri
 * @author n-o-o-d-l-e
 */
public class ApplicationProperties extends WProperties {
  /** SerialVersionUID de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Logger} de la classe */
  private static final Logger LOGGER =
      LogManager.getLogger(ApplicationProperties.class.getClass().getSimpleName());

  /** Le mode de couleur actuel de l'application */
  protected ColorMode colorMode = ApplicationConstants.DEFAULT_COLOR_MODE;

  /** La localisation actuelle de l'application */
  protected Locale locale = ApplicationConstants.DEFAULT_LOCALE;

  /** La resolution actuelle de l'application */
  protected Resolution resolution = ApplicationConstants.DEFAULT_RESOLUTION;

  /** L'onglet actuel de l'application */
  protected ApplicationTab tab = ApplicationConstants.DEFAULT_TAB;

  /** La visibilite de la legende */
  protected boolean legendVisibility = ApplicationConstants.DEFAULT_LEGEND_VISIBILITY;

  /** La visibilite du mode zen */
  protected boolean zenModeVisibility = ApplicationConstants.DEFAULT_ZEN_MODE_VISIBILITY;

  /** L'état d'activation du bouton de la pipette/inspecteur */
  protected boolean inspectorButtonState = ApplicationConstants.DEFAULT_INSPECTOR_BUTTON_STATE;

  /** Le theme actuel de l'application */
  protected Class<?> theme = ApplicationConstants.DEFAULT_THEME;

  /** La visibilite du bouton d'aide */
  protected boolean helpButtonVisibility = ApplicationConstants.DEFAULT_HELP_BUTTON_VISIBILITY;

  /**
   * Construction d'un {@code ApplicationProperties}
   *
   * @param colorMode {@code ColorMode} actuel de l'application
   * @param locale {@code Locale} actuel de l'application
   * @param resolution {@code Resolution} actuel de l'application
   * @param tab {@code ApplicationTab} actuel de l'application
   * @param legendVisibility l'etat d'affichage de la légende
   * @param zenModeVisibility l'etat d'affichage du mode zen
   * @param inspectorButtonState l'etat d'activation du bouton de la pipette/inspecteur
   * @param theme le theme actuel de l'application
   * @param helpButtonVisibility l'etat d'affichage du bouton d'aide
   */
  protected ApplicationProperties(
      ColorMode colorMode,
      Locale locale,
      Resolution resolution,
      ApplicationTab tab,
      boolean legendVisibility,
      boolean zenModeVisibility,
      boolean inspectorButtonState,
      Class<?> theme,
      boolean helpButtonVisibility) {
    super();

    this.colorMode = colorMode;
    this.locale = locale;
    this.resolution = resolution;
    this.tab = tab;
    this.legendVisibility = legendVisibility;
    this.zenModeVisibility = zenModeVisibility;
    this.inspectorButtonState = inspectorButtonState;
    this.theme = theme;
    this.helpButtonVisibility = helpButtonVisibility;
  }

  /** Construction de {@code ApplicationProperties} par defaut. */
  public ApplicationProperties() {
    super();
  }

  /**
   * Methode permettant de copier les valeurs des champs d'une instance de {@code
   * ApplicationProperties} aux champs de cette instance
   *
   * @param properties Le {@code ApplicationProperties} dont les valeurs de champs seront copiees
   *     aux champs de cette instance
   */
  protected void copy(ApplicationProperties properties) {
    colorMode = properties.colorMode;
    locale = properties.locale;
    resolution = properties.resolution;
    tab = properties.tab;
    legendVisibility = properties.legendVisibility;
    inspectorButtonState = properties.inspectorButtonState;
    theme = properties.theme;
    helpButtonVisibility = properties.helpButtonVisibility;
  }

  /**
   * Methode permettant de lire le {@code colorMode}
   *
   * @return Le {@code colorMode}
   */
  private ColorMode readColorMode() {
    try {
      return ApplicationProperties.readColorMode(getPropertiesManager());
    } catch (Exception e) {
      return ApplicationConstants.DEFAULT_COLOR_MODE;
    }
  }

  /**
   * Methode permettant de lire le {@code colorMode}
   *
   * @param manager le {@code PropertiesManager} avec lequel lire
   * @return Le {@code colorMode}
   */
  public static ColorMode readColorMode(PropertiesManager manager) {
    return ColorMode.fromIndex((boolean) WPropertyKey.APPLICATION_COLOR_MODE.read(manager) ? 1 : 0);
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code colorMode}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeColorMode() {
    try {
      return WPropertyKey.APPLICATION_COLOR_MODE.write(
          this.colorMode.ordinal() == 0, getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Methode permettant de lire le {@code locale}
   *
   * @return Le {@code locale}
   */
  private Locale readLocale() {
    try {
      return ApplicationProperties.readLocale(getPropertiesManager());
    } catch (Exception e) {
      return ApplicationConstants.DEFAULT_LOCALE;
    }
  }

  /**
   * Methode permettant de lire le {@code locale}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return Le {@code locale}
   */
  public static Locale readLocale(PropertiesManager manager) {
    return Locale.forLanguageTag((String) WPropertyKey.APPLICATION_LOCALE.read(manager));
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code locale}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeLocale() {
    try {
      return WPropertyKey.APPLICATION_LOCALE.write(locale.toString(), getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Methode permettant de lire la resolution avec le {@code PropertiesManager} de cette instance
   *
   * @return La resolution
   */
  private Resolution readResolution() {
    try {
      return ApplicationProperties.readResolution(getPropertiesManager());
    } catch (Exception e) {
      return ApplicationConstants.DEFAULT_RESOLUTION;
    }
  }

  /**
   * Methode permettant de lire la resolution
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La resolution
   */
  public static Resolution readResolution(PropertiesManager manager) {
    return Resolution.fromIndex((int) WPropertyKey.SIMULATION_RESOLUTION.read(manager));
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code resolution}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  protected boolean writeResolution() {
    try {
      return WPropertyKey.SIMULATION_RESOLUTION.write(resolution.ordinal(), getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Methode permettant de lire l'onglet
   *
   * @return L'onglet
   */
  private ApplicationTab readTab() {
    try {
      return ApplicationProperties.readTab(getPropertiesManager());
    } catch (Exception e) {
      return ApplicationConstants.DEFAULT_TAB;
    }
  }

  /**
   * Methode permettant de lire l'onglet
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return L'onglet
   */
  public static ApplicationTab readTab(PropertiesManager manager) {
    return ApplicationTab.fromIndex((int) WPropertyKey.APPLICATION_TAB.read(manager));
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code tab}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeTab() {
    try {
      return WPropertyKey.APPLICATION_TAB.write(tab.ordinal(), getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Methode permettant de lire la visibilite de la legende
   *
   * @return La visibilite de la legende
   */
  private boolean readLegendVisibility() {
    try {
      return ApplicationProperties.readLegendVisibility(getPropertiesManager());
    } catch (Exception e) {
      return ApplicationConstants.DEFAULT_LEGEND_VISIBILITY;
    }
  }

  /**
   * Methode permettant de lire la visibilite de la legende
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La visibilite de la legende
   */
  public static boolean readLegendVisibility(PropertiesManager manager) {
    try {
      return (boolean) WPropertyKey.APPLICATION_LEGEND_VISIBILITY.read(manager);
    } catch (Exception e) {
      return ApplicationConstants.DEFAULT_LEGEND_VISIBILITY;
    }
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code legendVisibility}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeLegendVisibility() {
    try {
      return WPropertyKey.APPLICATION_LEGEND_VISIBILITY.write(legendVisibility, getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Methode permettant de lire la visibilite du mode zen avec le {@code PropertiesManager} de cette
   * instance
   *
   * @return la visibilite du mode zen
   */
  private boolean readZenModeVisibility() {
    try {
      return ApplicationProperties.readZenModeVisibility(getPropertiesManager());
    } catch (Exception e) {
      return ApplicationConstants.DEFAULT_ZEN_MODE_VISIBILITY;
    }
  }

  /**
   * Methode permettant de lire la visibilite du mode zen avec un {@code PropertiesManager}
   * quelconque
   *
   * @param manager Le {@code PropertiesManager} avec lequel effectuer la lecture
   * @return la visibilite du mode zen
   */
  public static boolean readZenModeVisibility(PropertiesManager manager) {
    return (boolean) WPropertyKey.APPLICATION_ZENMODE_VISIBILITY.read(manager);
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code ZenModeVisibility}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeZenModeVisibility() {
    try {
      return WPropertyKey.APPLICATION_ZENMODE_VISIBILITY.write(zenModeVisibility, getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Méthode permettant de lire la visibilité des boutons aide
   *
   * @return la visibilité des boutons aide
   */
  private boolean readHelpButtonVisibility() {
    try {
      return ApplicationProperties.readHelpButtonVisibility(getPropertiesManager());
    } catch (Exception e) {
      return ApplicationConstants.DEFAULT_HELP_BUTTON_VISIBILITY;
    }
  }

  /**
   * Méthode permettant de lire la visibilité des boutons aide avec un {@code PropertiesManager}
   * quelconque
   *
   * @param manager Le {@code PropertiesManager} avec lequel effectuer la lecture
   * @return la visibilité des boutons aide
   */
  public static boolean readHelpButtonVisibility(PropertiesManager manager) {
    return (boolean) WPropertyKey.APPLICATION_HELP_BUTTON_VISIBILITY.read(manager);
  }

  /**
   * Méthode permettant d'écrire la valeur de {@code helpButtonVisibility}
   *
   * @return {@code true} si l'écriture a été effectuée avec succès
   */
  private boolean writeHelpButtonVisibility() {
    try {
      return WPropertyKey.APPLICATION_HELP_BUTTON_VISIBILITY.write(
          helpButtonVisibility, getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code theme}
   *
   * @return Le {@code theme}. En cas d'erreur (le theme n'est pas retrouve dans la liste), le theme
   *     FlatDarkLaf est retourne EN cas d'autre erreur, le theme actuel est retourne
   */
  private Class<?> readTheme() {
    try {
      return (Class<?>) ApplicationProperties.readTheme(this.getPropertiesManager());
    } catch (Exception e) {
      return this.theme;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code theme}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return Le {@code theme}. En cas d'erreur, le theme FlatDarkLaf est retourne
   */
  public static Class<?> readTheme(PropertiesManager manager) {
    Class<?> theme =
        ColorThemeManager.getThemeClass((String) WPropertyKey.APPLICATION_THEME.read(manager));

    if (theme == null) theme = FlatDarkLaf.class;

    return theme;
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code theme}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeTheme() {
    try {
      String themeName = ColorThemeManager.getThemeName(this.theme);

      if (themeName == null) return false;

      return WPropertyKey.APPLICATION_THEME.write(themeName, this.getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /** {@inheritDoc} */
  @Override
  public void readProperties() {
    colorMode = readColorMode();
    locale = readLocale();
    resolution = readResolution();
    tab = readTab();
    legendVisibility = readLegendVisibility();
    zenModeVisibility = readZenModeVisibility();
    helpButtonVisibility = readHelpButtonVisibility();
    theme = readTheme();
  }

  /** {@inheritDoc} */
  @Override
  public void writeProperties() {
    if (!getPropertiesManager().isEditable()) {
      // TODO custom exception
      ApplicationProperties.LOGGER.info(
          LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.notEditable"), "");
      return;
    }

    writeColorMode();
    writeResolution();
    writeLocale();
    writeTab();
    writeLegendVisibility();
    writeZenModeVisibility();
    writeHelpButtonVisibility();
    writeTheme();
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return WPropertyKey.APPLICATION_COLOR_MODE
        + "="
        + colorMode
        + "\n"
        + WPropertyKey.APPLICATION_TAB
        + "="
        + tab
        + "\n"
        + WPropertyKey.APPLICATION_LOCALE
        + "="
        + locale
        + "\n"
        + WPropertyKey.APPLICATION_LEGEND_VISIBILITY
        + "="
        + legendVisibility
        + "\n"
        + WPropertyKey.APPLICATION_HELP_BUTTON_VISIBILITY
        + "="
        + helpButtonVisibility;
  }
}
