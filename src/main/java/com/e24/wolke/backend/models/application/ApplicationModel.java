package com.e24.wolke.backend.models.application;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.WModelSaveable;
import com.e24.wolke.backend.models.application.ApplicationConstants.*;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.utils.BiHashMap;
import java.util.Locale;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code ApplicationModel} sert a gerer la logique des fonctionnalités generales de
 * l'application et de l'exposer au {@code Controller}.
 *
 * @author MeriBouisri
 * @author adrienles
 */
public class ApplicationModel extends WModelSaveable {

  /** Le {@code Logger} de la classe */
  private static final Logger LOGGER =
      LogManager.getLogger(ApplicationModel.class.getClass().getSimpleName());

  /** Les proprietes de cette instance */
  private ApplicationProperties properties;

  /**
   * Drapeau pour savoir si le panneau d'inspection est épinglé à une cellule du canevas du
   * simulation ou s'il doit encore suivre la souris.
   */
  private boolean inspectorPanePinned = false;

  /** Les coordonées de la cellule actuellement inspectée par l'inspecteur */
  private Integer[] currentInspectorPosition = {0, 0};

  /**
   * Les coordonées de la souris par rapport au panneau de rendu en fonction de sa résolution. -1,
   * -1 si la souris n'est pas sur le panneau de rendu
   */
  private int[] mousePositionRelativeToSimulation = {-1, -1};

  /**
   * Les coordonées de la souris par rapport au cadre de l'application. -1, -1 si la souris n'est
   * pas sur le cadre de l'application
   */
  private int[] mousePositionRelativeToFrame = {-1, -1};

  /**
   * Construction d'un {@code ApplicationModel} avec parametres par defaut, sans {@code Controller}.
   */
  public ApplicationModel() {
    this(null);
  }

  /**
   * Construction d'un {@code ApplicationModel} avec parametres par defaut.
   *
   * @param controller Le {@code Controller} qui gere cette instance
   */
  public ApplicationModel(Controller controller) {
    this(
        controller,
        ApplicationConstants.DEFAULT_COLOR_MODE,
        ApplicationConstants.DEFAULT_LOCALE,
        ApplicationConstants.DEFAULT_RESOLUTION,
        ApplicationConstants.DEFAULT_TAB,
        ApplicationConstants.DEFAULT_LEGEND_VISIBILITY,
        ApplicationConstants.DEFAULT_ZEN_MODE_VISIBILITY,
        ApplicationConstants.DEFAULT_INSPECTOR_BUTTON_STATE,
        ApplicationConstants.DEFAULT_THEME,
        ApplicationConstants.DEFAULT_HELP_BUTTON_VISIBILITY);
  }

  /**
   * Construction d'un {@code ApplicationModel} avec un {@code Controller} et les parametres de
   * {@code ColorMode} et de {@code Localization}. La methode {@code this#initialize()} est appelee.
   *
   * @param controller Le {@code Controller} qui gere cette instance
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
  public ApplicationModel(
      Controller controller,
      ColorMode colorMode,
      Locale locale,
      Resolution resolution,
      ApplicationTab tab,
      boolean legendVisibility,
      boolean zenModeVisibility,
      boolean inspectorButtonState,
      Class<?> theme,
      boolean helpButtonVisibility) {
    super(controller);

    properties = new ApplicationProperties();
    setProperties(properties);

    setupKeybindSubscriptions();

    properties.colorMode = colorMode;
    properties.locale = locale;
    properties.resolution = resolution;
    properties.tab = tab;
    properties.legendVisibility = legendVisibility;
    properties.zenModeVisibility = zenModeVisibility;
    properties.inspectorButtonState = inspectorButtonState;
    properties.helpButtonVisibility = helpButtonVisibility;

    initialize();
  }

  /** {@inheritDoc} */
  @Override
  protected boolean setup() {
    ColorThemeManager.setupAvailableLAF();
    setupUIStyle();
    ToolTipManager.sharedInstance().setInitialDelay(ApplicationConstants.TOOLTIP_DELAY);

    updateLanguageLocale();

    ApplicationModel.LOGGER.debug("Initialized");

    return true;
  }

  // ==================
  // UI STYLE METHODS
  // ==================

  /** Methode permettant de configurer le style de l'interface utilisateur */
  public void setupUIStyle() {
    UIManager.put(
        "[style]Panel.layeredPanel",
        "[light]background: tint($Panel.background,50%);"
            + "[dark]background: shade($Panel.background,15%);"
            + "[light]border: 0,0,0,0,shade($Panel.background,20%),,12;"
            + "[dark]border: 0,0,0,0,tint($Panel.background,15%),,12");

    UIManager.put(
        "[style]Panel.layeredPanelContents",
        "[light]background: tint($Panel.background,50%);"
            + "[dark]background: shade($Panel.background,15%);");

    UIManager.put(
        "[style]Panel.subPanel",
        "[light]border: 0,0,0,0,shade($Panel.background,20%),,12;"
            + "[dark]border: 0,0,0,0,tint($Panel.background,20%),,12");
  }

  // ==================
  // COLOR MODE METHODS
  // ==================

  /**
   * Getter pour le {@code ColorMode} actuel de l'application
   *
   * @return le {@code ColorMode} actuel de l'application
   */
  public ColorMode getCurrentColorMode() {
    return properties.colorMode;
  }

  /**
   * Setter pour le {@code ColorMode} actuel de l'application
   *
   * @param colorMode le nouveau {@code ColorMode} actuel de l'application
   */
  public void setCurrentColorMode(ColorMode colorMode) {
    properties.colorMode = colorMode;
  }

  /**
   * Methode permettant d'actualiser le {@code ColorMode} de l'application. Le sujet {@code
   * Subject#ON_APP_COLORMODE_CHANGED} est diffusé sur le {@code EventBroker} local de cette
   * instance.
   *
   * @return {@code true} si la diffusion a ete effectuee avec succes
   */
  public boolean updateColorMode() {
    return false;
  }

  // =======================
  // MENU BAR METHODS
  // =======================

  /**
   * Setter pour l'état d'affichage de la légende
   *
   * @param visibility le nouvel état d'affichage de la légende
   */
  public void setLegendVisibility(boolean visibility) {
    properties.legendVisibility = visibility;
    updateLegendVisibility();
  }

  /**
   * Getter pour l'état d'affichage de la légende
   *
   * @return l'état d'affichage de la légende
   */
  public boolean getLegendVisibility() {
    return properties.legendVisibility;
  }

  /**
   * Setter pour l'état d'affichage du mode zen
   *
   * @param visibility le nouvel état d'affichage du mode zen
   */
  public void setZenModeVisibility(boolean visibility) {
    properties.zenModeVisibility = visibility;
    updateZenModeVisibility();
  }

  /**
   * Getter pour l'état d'affichage du mode zen
   *
   * @return l'état d'affichage du mode zen
   */
  public boolean getZenModeVisibility() {
    return properties.zenModeVisibility;
  }

  // =======================
  // INSPECTOR METHODS
  // =======================

  /**
   * Setter pour l'état d'activation du bouton de la pipette/inspecteur
   *
   * @param state le nouvel état d'activation du bouton de la pipette/inspecteur
   */
  public void setInspectorButtonState(boolean state) {
    properties.inspectorButtonState = state;
    updateInspectorButtonState();
  }

  /**
   * Getter pour l'état d'activation du bouton de la pipette/inspecteur
   *
   * @return l'état d'activation du bouton de la pipette/inspecteur
   */
  public boolean getInspectorButtonState() {
    return properties.inspectorButtonState;
  }

  /**
   * Setter pour l'état d'épinglage du panneau d'inspection
   *
   * @param state le nouvel état d'épinglage du panneau d'inspection
   */
  public void setInspectorPanePinned(boolean state) {
    inspectorPanePinned = state;
  }

  /**
   * Getter pour l'état d'épinglage du panneau d'inspection
   *
   * @return l'état d'épinglage du panneau d'inspection
   */
  public boolean getInspectorPanePinned() {
    return inspectorPanePinned;
  }

  /** Méthode permettant de basculer l'état d'épinglage du panneau d'inspection */
  public void toggleInspectorPanePinned() {
    inspectorPanePinned = !inspectorPanePinned;
  }

  /**
   * Setter pour les coordonnées de la cellule actuellement inspectée par l'inspecteur
   *
   * @param position les nouvelles coordonnées de la cellule actuellement inspectée par l'inspecteur
   */
  public void setCurrentInspectorPosition(Integer[] position) {
    currentInspectorPosition = position;
  }

  /**
   * Getter pour les coordonnées de la cellule actuellement inspectée par l'inspecteur
   *
   * @return les coordonnées de la cellule actuellement inspectée par l'inspecteur
   */
  public Integer[] getCurrentInspectorPosition() {
    return currentInspectorPosition;
  }

  /**
   * Setter pour les coordonnées de la souris par rapport au panneau de rendu en fonction de sa
   * résolution
   *
   * @param newCoord les nouvelles coordonnées de la souris
   */
  public void setMousePositionRelativeToSimulation(int[] newCoord) {
    mousePositionRelativeToSimulation = newCoord;
  }

  /**
   * Getter pour les coordonnées de la souris par rapport au panneau de rendu en fonction de sa
   * résolution
   *
   * @return les coordonnées de la souris
   */
  public int[] getMousePositionRelativeToSimulation() {
    return mousePositionRelativeToSimulation;
  }

  /**
   * Setter pour les coordonnées de la souris par rapport au cadre de l'application
   *
   * @param newCoord les nouvelles coordonnées de la souris
   */
  public void setMousePositionRelativeToFrame(int[] newCoord) {
    mousePositionRelativeToFrame = newCoord;
  }

  /**
   * Getter pour les coordonnées de la souris par rapport au cadre de l'application
   *
   * @return les coordonnées de la souris
   */
  public int[] getMousePositionRelativeToFrame() {
    return mousePositionRelativeToFrame;
  }

  // =======================
  // LANGUAGE LOCALE METHODS
  // =======================

  /**
   * Getter pour le {@code Locale} actuel de l'application
   *
   * @return le {@code Locale} actuel de l'application
   */
  public Locale getCurrentLocale() {
    return properties.locale;
  }

  /**
   * Setter pour le {@code Locale} actuel de l'application
   *
   * @param locale le nouveau {@code Locale} actuel de l'application
   */
  public void setCurrentLocale(Locale locale) {
    properties.locale = locale;
  }

  /**
   * Methode permettant d'actualiser le {@code languageLocale} de l'application. Le {@code
   * Subject#ON_APPLICATION_LOCALE_CHANGED} est diffusé sur le {@code EventBroker} local de cette
   * instance.
   *
   * @return {@code true} si la diffusion a ete effectuee avec succes
   */
  public boolean updateLanguageLocale() {
    LocaleManager.update(properties.locale);

    return getPublisher().publish(Subject.ON_APP_LANGUAGE_LOCALE_CHANGED, properties.locale);
  }

  // =======================
  // APPLICATIONTAB METHODS
  // =======================

  /**
   * Getter pour l'onglet actuel de l'application
   *
   * @return l'onglet actuel de l'application
   */
  public ApplicationTab getCurrentTab() {
    return properties.tab;
  }

  /**
   * Setter pour l'onglet actuel de l'application
   *
   * @param tab le nouvel onglet actuel de l'application
   */
  public void setCurrentTab(ApplicationTab tab) {
    properties.tab = tab;
    updateTab();
  }

  // =======================
  // HELP BUTTON METHODS
  // =======================

  /**
   * Setter pour l'état d'affichage du bouton d'aide
   *
   * @param visibility le nouvel état d'affichage du bouton d'aide
   */
  public void setHelpButtonVisibility(boolean visibility) {
    properties.helpButtonVisibility = visibility;
    updateHelpButtonVisibility();
  }

  /**
   * Getter pour l'état d'affichage du bouton d'aide
   *
   * @return l'état d'affichage du bouton d'aide
   */
  public boolean getHelpButtonVisibility() {
    return properties.helpButtonVisibility;
  }

  // ===============
  // UPDATE METHODES
  // ===============

  /**
   * Methode a invoquer lors du changement d'onglet
   *
   * @return {@code true} si l'actualisation a ete effectuee avec succes
   */
  private boolean updateTab() {
    return getPublisher().publish(Subject.ON_APP_TAB_CHANGED, properties.tab);
  }

  /**
   * Methode a invoquer lors du changement du theme
   *
   * @return {@code true} si l'actualisation a ete effectuee avec succes
   */
  private boolean updateTheme() {
    return getPublisher().publish(Subject.ON_APP_THEME_CHANGED, properties.theme.getName());
  }

  /**
   * Methode a invoquer lors du changement de l'etat du mode zen
   *
   * @return {@code true} si l'actualisation a ete effectuee avec succes
   */
  private boolean updateZenModeVisibility() {
    if (properties.zenModeVisibility) {
      getController().getSimulationModel().onStartSimulation();
    }
    return getPublisher()
        .publish(Subject.ON_APP_ZEN_MODE_VISIBILITY_CHANGED, properties.zenModeVisibility);
  }

  /**
   * Methode a invoquer lors du changement de l'etat de visibilite de la legende
   *
   * @return {@code true} si l'actualisation a ete effectuee avec succes
   */
  private boolean updateLegendVisibility() {
    return getPublisher()
        .publish(Subject.ON_APP_LEGEND_VISIBILITY_CHANGED, properties.legendVisibility);
  }

  /**
   * Methode a invoquer lors du changement de l'etat du bouton d'inspection
   *
   * @return {@code true} si l'actualisation a ete effectuee avec succes
   */
  private boolean updateInspectorButtonState() {
    return getPublisher()
        .publish(Subject.ON_APP_INSPECTOR_VISIBILITY_CHANGED, properties.inspectorButtonState);
  }

  /**
   * Methode a invoquer lors du changement de l'etat d'affichage du bouton d'aide
   *
   * @return {@code true} si l'actualisation a ete effectuee avec succes
   */
  private boolean updateHelpButtonVisibility() {
    return getPublisher()
        .publish(Subject.ON_APP_HELP_BUTTON_VISIBILITY_CHANGED, properties.helpButtonVisibility);
  }

  // ==================
  // RESOLUTION METHODS
  // ==================

  /**
   * Getter pour le {@code Resolution} actuel de l'application
   *
   * @return le {@code Resolution} actuel de l'application
   */
  public Resolution getCurrentResolution() {
    return properties.resolution;
  }

  /**
   * Setter pour le {@code Resolution} actuel de l'application
   *
   * @param resolution le nouveau {@code Resolution} actuel de l'application
   */
  public void setCurrentResolution(Resolution resolution) {
    properties.resolution = resolution;
  }

  /**
   * Methode permettant d'actualiser le {@code Resolution} de l'application. Le sujet {@code
   * Subject#ON_APP_RESOLUTION_CHANGED} est diffusé sur le {@code EventBroker} local de cette
   * instance
   *
   * @return {@code true} si la diffusion a ete effectuee avec succes
   */
  public boolean updateResolution() {
    return false;
  }

  /**
   * Méthode qui retourne la liste des thèmes disponibles
   *
   * @return la liste des thèmes disponibles
   */
  public BiHashMap<Class<?>, String> getAvailableThemes() {
    return ColorThemeManager.availableThemes;
  }

  /**
   * Retourne le thème actuel de l'application
   *
   * @return Le thème actuel de l'application
   */
  public Class<?> getCurrentTheme() {
    return properties.theme;
  }

  /**
   * Actualise le thème actuel de l'application
   *
   * @param newTheme Le nouveau thème de l'appliction
   */
  public void setCurrentTheme(Class<?> newTheme) {
    properties.theme = newTheme;
    updateTheme();
  }

  /**
   * Setter pour la resolution de la simulation qui sera sauvegardee pour les prochains lancements
   *
   * @param resolution La resolution a sauvegarder
   */
  public void saveResolutionOnRestart(Resolution resolution) {
    Resolution backupResolution = properties.resolution;
    properties.resolution = resolution;
    properties.writeResolution();
    properties.resolution = backupResolution;
  }

  /** Methode permettant de reinitialiser l'application */
  public void onReinitialize() {
    LOGGER.info(LocaleManager.getLocaleResourceBundle().getString("log.reinitialize"));
    getPublisher().publish(Subject.ON_APP_REINITIALIZE, null);
  }

  /** {@inheritDoc} */
  public void setupKeybindSubscriptions() {
    getSubscriber()
        .subscribe(
            Subject.KEYBIND_ZEN_MODE_TOGGLE_PRESSED,
            (e) -> this.setZenModeVisibility(!this.getZenModeVisibility()));
    getSubscriber()
        .subscribe(Subject.KEYBIND_ZEN_MODE_EXIT_PRESSED, (e) -> this.setZenModeVisibility(false));
    getSubscriber()
        .subscribe(
            Subject.KEYBIND_LEGEND_TOGGLE_PRESSED,
            (e) -> this.setLegendVisibility(!this.getLegendVisibility()));
    getSubscriber()
        .subscribe(
            Subject.KEYBIND_INSPECTOR_TOGGLE_PRESSED,
            (e) -> this.setInspectorButtonState(!this.getInspectorButtonState()));
  }

  /** {@inheritDoc} */
  @Override
  public void softReinitialize() {
    properties.legendVisibility = ApplicationConstants.DEFAULT_LEGEND_VISIBILITY;
    properties.zenModeVisibility = ApplicationConstants.DEFAULT_ZEN_MODE_VISIBILITY;
    properties.inspectorButtonState = ApplicationConstants.DEFAULT_INSPECTOR_BUTTON_STATE;
    properties.helpButtonVisibility = ApplicationConstants.DEFAULT_HELP_BUTTON_VISIBILITY;
  }

  /** {@inheritDoc} */
  @Override
  protected void onPropertiesChanged() {
    updateLanguageLocale();
    updateTheme();
    updateLegendVisibility();
    updateHelpButtonVisibility();

    updateTab();

    updateColorMode();
    updateResolution();
  }
}
