package com.e24.wolke.backend.models.renderer;

import com.e24.wolke.backend.models.WProperties;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.renderer.RendererConstants.VisualizationType;
import com.e24.wolke.filesystem.properties.PropertiesManager;
import com.e24.wolke.filesystem.properties.WPropertyKey;
import com.e24.wolke.filesystem.properties.WStandardPropertiesProcessor;
import com.e24.wolke.utils.images.WColor;
import java.awt.Color;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code RendererProperties} permet de regrouper les proprietes reliees au {@code
 * RendererModel}
 *
 * @author MeriBouisri
 */
public class RendererProperties extends WProperties implements WStandardPropertiesProcessor {
  /** SerialVersionUID de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Logger} de la classe */
  private static final Logger LOGGER =
      LogManager.getLogger(RendererProperties.class.getClass().getSimpleName());

  /** Le {@code VisualizationType} du rendu */
  protected VisualizationType visualizationType = RendererConstants.DEFAULT_VISUALIZATION_TYPE;

  /** Activer ou non l'affichage de vecteurs */
  protected boolean showVectors = RendererConstants.DEFAULT_SHOW_VECTORS;

  /** Activer ou non l'affichage de la grille */
  protected boolean showGrid = RendererConstants.DEFAULT_SHOW_GRID;

  /** Activer ou non l'affichage des lignes de flux */
  protected boolean showFlowLines = RendererConstants.DEFAULT_SHOW_FLOWLINES;

  /** Les couleurs du dégradé visualisé */
  protected float[][] gradientHSBColors = RendererConstants.DEFAULT_GRADIENT_HSB_COLORS;

  /** Si la simulation devrait etre visualisee en nuances de gris */
  protected boolean greyscale = RendererConstants.DEFAULT_GRAYSCALE_VISUALISATION;

  /**
   * Construction d'un {@code RendererProperties}
   *
   * @param visualizationType Type de visualisation
   * @param showVectors Activer ou non l'affichage de vecteurs
   * @param showGrid Activer ou non l'affichage de la grille
   * @param showFlowLines Activer ou non l'affichage des lignes de flux
   * @param gradientHSBColors Les couleurs du degrade visualise
   * @param greyscale Si la simulation devrait etre visualisee en nuances de gris
   */
  public RendererProperties(
      VisualizationType visualizationType,
      boolean showVectors,
      boolean showGrid,
      boolean showFlowLines,
      float[][] gradientHSBColors,
      boolean greyscale) {
    super();
    this.visualizationType = visualizationType;
    this.showVectors = showVectors;
    this.showGrid = showGrid;
    this.showFlowLines = showFlowLines;
    this.gradientHSBColors = gradientHSBColors;
    this.greyscale = greyscale;
  }

  /** Construction d'un {@code RendererProperties} par defaut */
  public RendererProperties() {
    super();
  }

  // ========== VISUALIZATION TYPE ========== //

  /**
   * Methode permettant de lire la valeur de {@code VisualizationType} avec le {@code
   * PropertiesManager} de cette instance
   *
   * @return La valeur de {@code visualizationType}, ou la valeur actuelle en cas d'erreur
   */
  private VisualizationType readVisualizationType() {
    try {
      return RendererProperties.readVisualizationType(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return visualizationType;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code visualizationType}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code visualizationType}
   */
  public static VisualizationType readVisualizationType(PropertiesManager manager) {
    return VisualizationType.fromIndex((int) WPropertyKey.RENDERER_VISUALIZATION.read(manager));
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code visualizationType}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeVisualizationType() {
    try {
      return WPropertyKey.RENDERER_VISUALIZATION.write(visualizationType.ordinal(), getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // ========== SHOW VECTORS ========== //

  /**
   * Methode permettant de lire la valeur de {@code showVectors} avec le {@code PropertiesManager}
   * de cette instance
   *
   * @return La valeur de {@code showVectors}, ou la valeur actuelle en cas d'erreur
   */
  private boolean readShowVectors() {
    try {
      return RendererProperties.readShowVectors(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return showVectors;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code visualizationType}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code visualizationType}
   */
  public static boolean readShowVectors(PropertiesManager manager) {
    return (boolean) WPropertyKey.RENDERER_SHOW_VECTORS.read(manager);
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code showVectors}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeShowVectors() {
    try {
      return WPropertyKey.RENDERER_SHOW_VECTORS.write(showVectors, getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // ========== GREYSCALE VISUALIZATION ========== //

  /**
   * Methode permettant de lire la valeur de {@code greyscale} avec le {@code PropertiesManager} de
   * cette instance
   *
   * @return La valeur de {@code greyscale}, ou la valeur actuelle en cas d'erreur
   */
  private boolean readGreyscale() {
    try {
      return RendererProperties.readGreyscale(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return greyscale;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code greyscale}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code greyscale}
   */
  public static boolean readGreyscale(PropertiesManager manager) {
    return (boolean) WPropertyKey.RENDERER_GREYSCALE.read(manager);
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code greyscale}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeGreyscale() {
    try {
      return WPropertyKey.RENDERER_GREYSCALE.write(greyscale, getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // ========== MISC ========== //

  /**
   * Methode permettant de lire la valeur de {@code gradientStartColor}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code gradientStartColor}
   */
  public static Color readGradientStartColor(PropertiesManager manager) {
    return (Color) WPropertyKey.RENDERER_GRADIENT_START_COLOR.read(manager);
  }

  /**
   * Methode permettant de lire la valeur de {@code gradientEndColor}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code gradientStartColor}
   */
  public static Color readGradientEndColor(PropertiesManager manager) {
    return (Color) WPropertyKey.RENDERER_GRADIENT_END_COLOR.read(manager);
  }

  /**
   * Methode permettant de lire la valeur de {@code gradientHSBColor}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code gradientHSBColor}
   */
  public static float[][] readGradientHSBColor(PropertiesManager manager) {
    return new float[][] {
      WColor.toHSB(RendererProperties.readGradientStartColor(manager)),
      WColor.toHSB(RendererProperties.readGradientEndColor(manager))
    };
  }

  /**
   * Methode permettant de lire la valeur de {@code vectorFieldLinesStep}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code vectorFieldLinesStep}
   */
  public static int readVectorFieldLinesStep(PropertiesManager manager) {
    return (int) WPropertyKey.RENDERER_VECTORFIELD_STEP_SIZE.read(manager);
  }

  /**
   * Methode permettant de lire la valeur de {@code vectorFieldLinesStepMax}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code vectorFieldLinesStepMax}
   */
  public static int readVectorFieldLinesStepMax(PropertiesManager manager) {
    return (int) WPropertyKey.RENDERER_VECTORFIELD_STEP_SIZE_MAX.read(manager);
  }

  /**
   * Methode permettant de lire la valeur de {@code vectorFieldLinesStepMin}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code vectorFieldLinesStepMin}
   */
  public static int readVectorFieldLinesStepMin(PropertiesManager manager) {
    return (int) WPropertyKey.RENDERER_VECTORFIELD_STEP_SIZE_MIN.read(manager);
  }

  /**
   * Methode permettant de lire la valeur de {@code useOpenGL}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code useOpenGL}
   */
  public static boolean readUseOpenGL(PropertiesManager manager) {
    return (boolean) WPropertyKey.RENDERER_OPENGL.read(manager);
  }

  /** {@inheritDoc} */
  @Override
  public void readProperties() {
    visualizationType = readVisualizationType();
    showVectors = readShowVectors();
    greyscale = readGreyscale();
  }

  /** {@inheritDoc} */
  @Override
  public void writeProperties() {
    if (!getPropertiesManager().isEditable()) {
      // TODO custom exception
      RendererProperties.LOGGER.info(
          LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.notEditable"), "");
      return;
    }

    writeGreyscale();
    writeShowVectors();
    writeVisualizationType();
  }

  /** {@inheritDoc} */
  @Override
  public Properties writeStandardProperties() {
    Properties properties = new Properties();

    WPropertyKey.RENDERER_GREYSCALE.write(this.greyscale, properties);
    WPropertyKey.RENDERER_SHOW_VECTORS.write(this.showVectors, properties);
    WPropertyKey.RENDERER_VISUALIZATION.write(this.visualizationType.ordinal(), properties);

    return properties;
  }

  /** {@inheritDoc} */
  @Override
  public void readStandardProperties(Properties properties) {
    this.greyscale = (boolean) WPropertyKey.RENDERER_GREYSCALE.read(properties, this.greyscale);
    this.showVectors =
        (boolean) WPropertyKey.RENDERER_SHOW_VECTORS.read(properties, this.showVectors);
    this.visualizationType =
        VisualizationType.fromIndex(
            (int)
                WPropertyKey.RENDERER_VISUALIZATION.read(
                    properties, this.visualizationType.ordinal()));
  }
}
