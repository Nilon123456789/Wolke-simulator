package com.e24.wolke.backend.models.renderer;

import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.filesystem.properties.PropertiesManager;
import com.e24.wolke.filesystem.properties.SettingsPropertiesManager;
import javax.swing.JOptionPane;

/**
 * La classe {@code RendererConstants} regroupe les constantes reliees au {@code RendererModel}.
 *
 * @author MeriBouisri
 */
public final class RendererConstants {

  /** Le {@code PropertiesManager} de la classe */
  public static final PropertiesManager PROPERTIES_MANAGER = SettingsPropertiesManager.INSTANCE;

  // =================
  // DEFAULT CONSTANTS
  // =================

  /** Le {@code VisualizationType} par defaut */
  public static final VisualizationType DEFAULT_VISUALIZATION_TYPE =
      RendererProperties.readVisualizationType(RendererConstants.PROPERTIES_MANAGER);

  /** Les couleurs du dégradé visualisé, par défaut */
  public static final float[][] DEFAULT_GRADIENT_HSB_COLORS =
      RendererProperties.readGradientHSBColor(RendererConstants.PROPERTIES_MANAGER);

  /** L'écart entre les lignes du champ de vecteurs, par défaut */
  public static final int DEFAULT_VECTOR_FIELD_LINES_STEP =
      RendererProperties.readVectorFieldLinesStep(RendererConstants.PROPERTIES_MANAGER);

  /** L'écart minimum entre les lignes du champ de vecteurs */
  public static final int MIN_VECTOR_FIELD_LINES_STEP =
      RendererProperties.readVectorFieldLinesStepMin(RendererConstants.PROPERTIES_MANAGER);

  /** L'écart maximum entre les lignes du champ de vecteurs */
  public static final int MAX_VECTOR_FIELD_LINES_STEP =
      RendererProperties.readVectorFieldLinesStepMax(RendererConstants.PROPERTIES_MANAGER);

  /** Drapeau indiquant si l'OpenGL doit être utilisé pour le rendu */
  public static final boolean USE_OPENGL;

  static {
    boolean useOpenGL = RendererProperties.readUseOpenGL(RendererConstants.PROPERTIES_MANAGER);

    if (useOpenGL) {
      if (System.getProperty("os.name").toLowerCase().contains("mac")) {
        JOptionPane.showMessageDialog(
            null,
            LocaleManager.getLocaleResourceBundle().getString("ui.error.opengl_not_supported"),
            LocaleManager.getLocaleResourceBundle()
                .getString("ui.error.opengl_not_supported_title"),
            JOptionPane.ERROR_MESSAGE);

        System.exit(0);
      }
    }

    USE_OPENGL = useOpenGL;
  }

  /** Drapeau par défaut indiquant si la simulation devrait être visualisée en nuances de gris */
  public static final boolean DEFAULT_GRAYSCALE_VISUALISATION =
      RendererProperties.readGreyscale(RendererConstants.PROPERTIES_MANAGER);

  /** Drapeau par defaut indiquant l'affichage des vecteurs */
  public static final boolean DEFAULT_SHOW_VECTORS = false;

  /** Drapeau par defaut indiquant l'affichage de la grille */
  public static final boolean DEFAULT_SHOW_GRID = false;

  /** Drapeau par defaut indiquant l'affichage des lignes de flux */
  public static final boolean DEFAULT_SHOW_FLOWLINES = false;

  // ==============
  // ENUM CONSTANTS
  // ==============

  /**
   * Enum regorupant les differents type de visualisation de la simulation.
   *
   * @author MeriBouisri
   * @author Nilon123456789
   */
  public enum VisualizationType {
    /** Visualisation de la densite du fluide */
    DENSITY("ui.simulation.visualization.density"),
    /** Visualisation de la vitesse du fluide */
    VELOCITY("ui.simulation.visualization.velocity"),
    /** Visualisation de la vitesse en x du fluide */
    VELOCITY_X("ui.simulation.visualization.velocity_x"),
    /** Visualisation de la vitesse en y du fluide */
    VELOCITY_Y("ui.simulation.visualization.velocity_y"),
    /** Visualisation de la pression du fluide */
    PRESSURE("ui.simulation.visualization.pressure"),
    /** Visualisation de la pression en x du fluide */
    PRESSURE_X("ui.simulation.visualization.pressure_x"),
    /** Visualisation de la pression en y du fluide */
    PRESSURE_Y("ui.simulation.visualization.pressure_y");

    /** Le nom de la propriete de localisation */
    private final String localeProperty;

    /**
     * Construction d'un element de {@code VisualizationType} avec une propriete de localisation de
     * langue
     *
     * @param localeProperty Nom de la propriete de localisation
     */
    VisualizationType(String localeProperty) {
      this.localeProperty = localeProperty;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return LocaleManager.getLocaleResourceBundle().getString(localeProperty);
    }

    /**
     * Retourne le {@code VisualizationType} associe a l'index.
     *
     * @param index L'index de l'enum
     * @return Le {@code VisualizationType} associe a l'index ou {@code VisualisationType.DENSITY}
     *     si l'index est invalide
     */
    public static VisualizationType fromIndex(int index) {
      for (VisualizationType type : VisualizationType.values()) {
        if (type.ordinal() != index) continue;
        return type;
      }
      return VisualizationType.DENSITY;
    }
  }

  /** L'initialisation de la classe {@code RendererConstans} est interdite. */
  private RendererConstants() {
    throw new AssertionError();
  }
}
