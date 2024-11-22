package com.e24.wolke.backend.models.application;

import com.e24.wolke.filesystem.properties.SettingsPropertiesManager;
import java.awt.Dimension;
import java.awt.Image;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ForkJoinPool;
import javax.swing.ImageIcon;

/**
 * La classe {@code ApplicationConstants} regroupe les constantes liees a l'application en general.
 *
 * @author MeriBouisri
 * @author adrienles
 * @author Nilon123456789
 */
public final class ApplicationConstants {

  /** Constructeur privé pour empêcher l'instanciation de la classe */
  private ApplicationConstants() {
    throw new IllegalStateException("Utility class");
  }

  // ====================
  // PROPERTIES CONSTANTS
  // ====================

  // =================
  // DEFAULT CONSTANTS
  // =================

  // TODO : Trouver pourquoi il charge toujours l'icone de la plus petite taille, ce qui fait en
  // sorte que l'icone est petite dans la barre de taches
  /** Le chemin vers les différentes tailles d'icônes de l'application */
  public static final List<Image> ICONS =
      Arrays.asList(
          new ImageIcon(ApplicationConstants.class.getResource("/icons/wolke-logo-256.png"))
              .getImage());

  /** Le mode de couleur {@code ColorMode} par defaut */
  public static final ColorMode DEFAULT_COLOR_MODE =
      ApplicationProperties.readColorMode(SettingsPropertiesManager.INSTANCE);

  /** La localisation {@code Locale} par defaut */
  public static final Locale DEFAULT_LOCALE =
      ApplicationProperties.readLocale(SettingsPropertiesManager.INSTANCE);

  /** Valeurs de {@code Locale} valides */
  public static final Locale[] VALID_LOCALES = {Locale.FRENCH, Locale.ENGLISH};

  /** La resolution {@code Resolution} par defaut */
  public static final Resolution DEFAULT_RESOLUTION =
      ApplicationProperties.readResolution(SettingsPropertiesManager.INSTANCE);

  /** La visibilité de la légende par defaut */
  public static final boolean DEFAULT_LEGEND_VISIBILITY =
      ApplicationProperties.readLegendVisibility(SettingsPropertiesManager.INSTANCE);

  /** La visibilité de la console par defaut */
  public static final boolean DEFAULT_CONSOLE_VISIBILITY =
      SettingsPropertiesManager.INSTANCE.getBooleanProperty("console.showConsole");

  /** La visibilité du mode zen par defaut */
  public static final boolean DEFAULT_ZEN_MODE_VISIBILITY = false;

  /** Le temps de délai d'affichage des tooltips */
  public static final int TOOLTIP_DELAY =
      SettingsPropertiesManager.INSTANCE.getIntProperty("application.tooltip.delay");

  /** La taille de la fenêtre par defaut */
  public static final Dimension DEFAULT_WINDOW_SIZE =
      new Dimension(
          SettingsPropertiesManager.INSTANCE.getIntArrayProperty("application.windowSize")[0],
          SettingsPropertiesManager.INSTANCE.getIntArrayProperty("application.windowSize")[1]);

  /** L'onglet sélectionné par défaut */
  public static final ApplicationTab DEFAULT_TAB =
      ApplicationProperties.readTab(SettingsPropertiesManager.INSTANCE);

  /** L'état du bouton d'inspecteur par défaut */
  public static final boolean DEFAULT_INSPECTOR_BUTTON_STATE = false;

  /** Le thème par défaut */
  public static final Class<?> DEFAULT_THEME =
      ApplicationProperties.readTheme(SettingsPropertiesManager.INSTANCE);

  /** La visibilité du bouton d'aide par défaut */
  public static final boolean DEFAULT_HELP_BUTTON_VISIBILITY =
      ApplicationProperties.readHelpButtonVisibility(SettingsPropertiesManager.INSTANCE);

  /** La longeure maximal de log a garder */
  public static final int MAX_WCONSOLE_LENGTH =
      SettingsPropertiesManager.INSTANCE.getIntProperty("console.maxLines");

  /** Le chemin relatif vers le dossier des images d'exemple */
  public static final String SAMPLE_IMAGES_FOLDER_SUBDIR = "sample-images";

  // ==============
  // MULTITHREADING
  // ==============

  /** Le pool de threads pour les tâches forkJoin */
  public static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();

  /** Seuil pour la division des tâches des forkJoin */
  // TODO : Voir pour une meilleur valeur?
  public static final int SUBTASK_THRESHOLD =
      SettingsPropertiesManager.INSTANCE.getIntProperty("simulation.multiThreaded.subtaskSize");

  // ==============
  // ENUM CONSTANTS
  // ==============

  /**
   * Enum regroupant les options de mode de couleur de l'application.
   *
   * @author MeriBouisri
   * @author Nilon123456789
   */
  public enum ColorMode {
    /** Mode de couleur sombre */
    DARK,
    /** Mode de couleur clair */
    LIGHT;

    /**
     * Retourne le mode de couleur correspondant à l'index
     *
     * @param index L'index du mode de couleur
     * @return Le mode de couleur correspondant à l'index ou {@code ColorMode.DARK} si l'index
     *     n'existe pas
     */
    public static ColorMode fromIndex(int index) {
      for (ColorMode colorMode : ColorMode.values()) {
        if (colorMode.ordinal() != index) continue;
        return colorMode;
      }
      return ColorMode.DARK;
    }
  }

  /**
   * Enum regroupant les options de la resolution du rendu de l'application
   *
   * @author MeriBouisri
   * @author Nilon123456789
   */
  public enum Resolution {
    /** Resolution basse */
    LOW(
        SettingsPropertiesManager.INSTANCE.getIntArrayProperty("simulation.resolution.LOW")[0],
        SettingsPropertiesManager.INSTANCE.getIntArrayProperty("simulation.resolution.LOW")[1]),
    /** Resolution moyenne */
    MEDIUM(
        SettingsPropertiesManager.INSTANCE.getIntArrayProperty("simulation.resolution.MEDIUM")[0],
        SettingsPropertiesManager.INSTANCE.getIntArrayProperty("simulation.resolution.MEDIUM")[1]),
    /** Resolution haute */
    HIGH(
        SettingsPropertiesManager.INSTANCE.getIntArrayProperty("simulation.resolution.HIGH")[0],
        SettingsPropertiesManager.INSTANCE.getIntArrayProperty("simulation.resolution.HIGH")[1]),
    /** Resolution ultra haute */
    ULTRA(
        SettingsPropertiesManager.INSTANCE.getIntArrayProperty("simulation.resolution.ULTRA")[0],
        SettingsPropertiesManager.INSTANCE.getIntArrayProperty("simulation.resolution.ULTRA")[1]);

    /** Largeur de la resolution */
    private final int width;

    /** Hauteur de la resolution */
    private final int height;

    /**
     * Créer une resolution avec une largeur et une hauteur
     *
     * @param width La largeur de la resolution
     * @param height La hauteur de la resolution
     */
    Resolution(int width, int height) {
      this.width = width;
      this.height = height;
    }

    /**
     * Retourne la largeur de la resolution
     *
     * @return La largeur de la resolution
     */
    public int getWidth() {
      return width;
    }

    /**
     * Retourne la hauteur de la resolution
     *
     * @return La hauteur de la resolution
     */
    public int getHeight() {
      return height;
    }

    /**
     * Retourne les dimensions de la resolution dans un tableau {@code int[2]}
     *
     * @return la longueur horizontale et verticale de la resolution tel que [{@code this#width},
     *     {@code this#height}]
     */
    public int[] getSize() {
      return new int[] {width, height};
    }

    /**
     * Retourne la resolution correspondant à l'index
     *
     * @param index L'index de la resolution
     * @return La resolution correspondant à l'index ou {@code Resolution.LOW} si l'index n'existe
     *     pas
     */
    public static Resolution fromIndex(int index) {
      for (Resolution resolution : Resolution.values()) {
        if (resolution.ordinal() != index) continue;
        return resolution;
      }
      return Resolution.LOW;
    }
  }

  /**
   * Enum regroupant les onglets de l'application
   *
   * @author adrienles
   * @author Nilon123456789
   */
  public enum ApplicationTab {
    /** Onglet de la simulation */
    SIMULATION(0),
    /** Onglet de l'éditeur */
    EDITOR(1);

    /** L'index de l'onglet */
    private final int index;

    /**
     * Créer un onglet avec un index
     *
     * @param index L'index de l'onglet
     */
    ApplicationTab(int index) {
      this.index = index;
    }

    /**
     * Récupérer l'index de l'onglet
     *
     * @return L'index de l'onglet
     */
    public int getIndex() {
      return index;
    }

    /**
     * Récupérer l'onglet à partir de l'index
     *
     * @param index L'index de l'onglet
     * @return L'onglet correspondant à l'index ou {@code ApplicationTab.SIMULATION} si l'index
     *     n'existe pas
     */
    public static ApplicationTab fromIndex(int index) {
      for (ApplicationTab tab : ApplicationTab.values()) {
        if (tab.getIndex() != index) continue;
        return tab;
      }
      return ApplicationTab.SIMULATION;
    }
  }
}
