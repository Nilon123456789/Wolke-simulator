package com.e24.wolke.filesystem.properties;

import com.e24.wolke.utils.files.WPropertiesUtils;
import java.awt.Color;
import java.security.InvalidParameterException;
import java.util.Properties;

/**
 * La classe enum {@code PropertyKey} permet de regrouper le nom des cles de chaque propriete, ainsi
 * que le type attendu de la propriete
 *
 * @author MeriBouisri
 * @author Nilon123456789
 */
public enum WPropertyKey {
  // ========== APPLICATION PROPERTIES ========== //

  /** La cle pour {@code ApplicationProperties#colorMode} */
  APPLICATION_COLOR_MODE("application.darkMode", Boolean.class),

  /** La cle pour {@code ApplicationProperties#locale} */
  APPLICATION_LOCALE("application.defaultLanguage", String.class),

  /** La cle pour {@code ApplicationProperties#windowSize} */
  APPLICATION_WINDOW_SIZE("application.windowSize", int[].class),

  /** La cle pour {@code ApplicationProperties#zenModeVisibility} */
  APPLICATION_ZENMODE_VISIBILITY("application.zenModeVisibility", Boolean.class),

  /** La cle pour {@code ApplicationProperties#legendVisibility} */
  APPLICATION_LEGEND_VISIBILITY("simulation.legendVisibility", Boolean.class),

  /** La cle pour {@code ApplicationProperties#tab} */
  APPLICATION_TAB("application.startPage", Integer.class),

  /** La cle pour {@code ApplicationProperties#helpButtonVisibility} */
  APPLICATION_HELP_BUTTON_VISIBILITY("application.helpButtonVisibility", Boolean.class),

  /** La cle pour {@code ApplicationProperties#theme} */
  APPLICATION_THEME("application.theme", String.class),

  // ========== SIMULATION PROPERTIES ========== //

  /** La cle pour {@code ApplicationProperties#resolution} */
  SIMULATION_RESOLUTION("simulation.defaultResolution", Integer.class),

  /** La cle pour {@code Resolution#LOW} */
  SIMULATION_RESOLUTION_LOW("simulation.resolution.LOW", int[].class),

  /** La cle pour {@code Resolution#MEDIUM} */
  SIMULATION_RESOLUTION_MEDIUM("simulation.resolution.MEDIUM", int[].class),

  /** La cle pour {@code Resolution#HIGH} */
  SIMULATION_RESOLUTION_HIGH("simulation.resolution.HIGH", int[].class),

  /** La cle pour {@code Resolution#ULTRA} */
  SIMULATION_RESOLUTION_ULTRA("simulation.resolution.ULTRA", int[].class),

  /** La cle poour {@code SimulationProperties#subtaskSize} */
  SIMULATION_MULTITHREADED_SUBTASK_SIZE("simulation.multiThreaded.subtaskSize", Integer.class),

  /** La cle pour {@code SimulationProperties#physicalSize} */
  SIMULATION_PHYSICAL_SIZE("simulation.physicalSize", double[].class),

  /** La cle pour {@code SimulationProperties#fluidType} */
  SIMULATION_FLUID_TYPE("simulation.fluidType", Integer.class),

  /** La cle pour {@code SimulationProperties#initialVelocity} */
  SIMULATION_INITIAL_VELOCITY("simulation.initialVelocity", Double.class),

  /** La cle pour {@code SimulationProperties#volumeDensity} */
  SIMULATION_VOLUME_DENSITY("simulation.volumeDensity", Double.class),

  /** La cle pour {@code SimulationProperties#viscosity} */
  SIMULATION_VISCOSITY("simulation.viscosity", Double.class),

  /** La cle pour {@code SimulationProperties#timeStep} */
  SIMULATION_TIME_STEP("simulation.timeStep", Double.class),

  /** La cle pour {@code SimulationProperties#sleepTime} */
  SIMULATION_SLEEP_TIME("simulation.sleepTime", Double.class),

  /** La cle pour {@code SimulationProperties#borderType} */
  SIMULATION_BORDER_TYPE("simulation.borderType", Integer.class),

  /** La cle pour {@code SimulationProperties#vortexConfinement} */
  SIMULATION_VORTEX_CONFINEMENT("simulation.vortexConfinement", Double.class),

  /** La cle pour {@code SimulationProperties#jacobiIterations} */
  SIMULATION_JACOBISOLVER_MAX_ITERATIONS("simulation.jacobiSolver.maxIterations", Integer.class),

  /** La cle pour {@code SimulationProperties#JacobiDiff} */
  SIMULATION_JACOBISOLVER_TOLERANCE("simulation.jacobiSolver.tolerance", Double.class),

  /** La cle pour {@code SimulationProperties#cflWarn} */
  SIMULATION_CFL_MINWARNING("simulation.cfl.minWarning", Double.class),

  /** La cle pour {@code SimulationProperties#cflError} */
  SIMULATION_CFL_MINERROR("simulation.cfl.minError", Double.class),

  /** La cle pour {@code SimulationProperties#cflCheckInterval} */
  SIMULATION_CFL_FREQUENCY("simulation.cfl.checkInterval", Integer.class),

  /** La cle pour {@code SimulationProperties#multiThreaded} */
  SIMULATION_MULTITHREADED("simulation.multiThreaded", Boolean.class),

  // ========== RENDERER PROPERTIES ========== //

  /** La cle pour {@code RendererProperties#visualization} */
  RENDERER_VISUALIZATION("renderer.defaultVisualisation", Integer.class),

  /** La cle pour {@code RendererProperties#gradientStartColor} */
  RENDERER_GRADIENT_START_COLOR("renderer.grandientStartColor", Color.class),

  /** La cle pour {@code RendererProperties#gradientStartColor} */
  RENDERER_GRADIENT_END_COLOR("renderer.grandientEndColor", Color.class),

  /** La cle pour {@code RendererProperties#vectorFieldStepSize} */
  RENDERER_VECTORFIELD_STEP_SIZE("renderer.vectorField.stepSize", Integer.class),

  /** La cle pour {@code RendererProperties#vectorFieldMinStepSize} */
  RENDERER_VECTORFIELD_STEP_SIZE_MIN("renderer.vectorField.minStepSize", Integer.class),

  /** La cle pour {@code RendererProperties#vectorFieldMaxStepSize} */
  RENDERER_VECTORFIELD_STEP_SIZE_MAX("renderer.vectorField.maxStepSize", Integer.class),

  /** La cle pour {@code RendererProperties#showVectors} */
  RENDERER_SHOW_VECTORS("renderer.showVectors", Boolean.class),

  /** La cle pour {@code RendererProperties#greyscale} */
  RENDERER_GREYSCALE("renderer.greyscale", Boolean.class),

  /** La cle pour {@code RendererProperties#useOpenGL} */
  RENDERER_OPENGL("renderer.openGL", Boolean.class),

  // ========== EDITOR PROPERTIES ========== //

  /** La cle pour {@code EditorProperties#maxLayers} */
  EDITOR_MAX_LAYERS("editor.maxLayers", Integer.class),

  // ========== CONSOLE PROPERTIES ========== //

  /** La cle pour {@code ConsoleProperties#consoleVisibility} */
  CONSOLE_VISIBILITY("console.showConsole", Boolean.class),

  /** La cle pour {@code ConsoleProperties#maxLines} */
  CONSOLE_MAX_LINES("console.maxLines", Integer.class),

  /** La cle pour {@code ConsoleProperties#logLevel} */
  CONSOLE_LOG_LEVEL("console.logLevel", String.class),

  // ========== TEST PROPERTIES ========== //

  /** Propriete test pour valeurs {@code Boolean} */
  TEST_BOOLEAN("test.boolean", Boolean.class, true),

  /** Propriete test pour valeurs {@code Integer} */
  TEST_INTEGER("test.integer", Integer.class, true),

  /** Propriete test pour valeurs {@code Double} */
  TEST_DOUBLE("test.double", Double.class, true),

  /** Propriete test pour valeurs {@code String} */
  TEST_STRING("test.string", String.class, true),

  /** Propriete test pour valeurs {@code int[]} */
  TEST_INTEGER_ARRAY("test.array.integer", int[].class, true),

  /** Propriete test pour valeurs {@code double[]} */
  TEST_DOUBLE_ARRAY("test.array.double", double[].class, true),

  /** Propriete test pour valeurs {@code Color} */
  TEST_COLOR("test.color", Color.class, true);

  /** Le nom de la cle */
  private String key;

  /** Le type attendu de la valeur de la cle */
  private Class<?> type;

  /** {@code true} si la propriete est utilisee pour les tests */
  private boolean isTest;

  /** Le {@code PropertiesManager} de tous les elements */
  private static PropertiesManager manager = SettingsPropertiesManager.INSTANCE;

  /**
   * Construction d'un element de {@code WPropertyKey} avec le nom de la cle et le type attendu
   *
   * @param key Le nom de la cle
   * @param type Le type attendu de la propriete
   */
  private WPropertyKey(String key, Class<?> type) {
    this(key, type, false);
  }

  /**
   * Construction d'un element de {@code WPropertyKey} avec le nom de la cle et le type attendu
   *
   * @param key Le nom de la cle
   * @param type Le type attendu de la propriete
   * @param test {@code true} si la propriete est utilisee pour les tests
   */
  private WPropertyKey(String key, Class<?> type, boolean test) {
    this.key = key;
    this.type = type;
    this.isTest = true;
  }

  /**
   * Getter pour le {@code type} attendu de la propriete, tel que retourne par {@code
   * SettingsPropertiesManager#getProperty(String)}
   *
   * @return Le type attendu de la propriete
   */
  public Class<?> getType() {
    return type;
  }

  /**
   * Getter pour {@code this#key}, le nom de la cle de cet element
   *
   * @return Le nom de la cle de cet element
   */
  public String getKey() {
    return key;
  }

  /**
   * Getter pour {@code this#isTest}, qui indique si la propriete est utilisee pour les tests
   *
   * @return {@code true} si la propriete est utilisee pour les tests
   */
  public boolean isTest() {
    return isTest;
  }

  /**
   * Methode permettant de lire la valeur de cet element avec le {@code PropertiesManager} par
   * defaut.
   *
   * @return un {@code Object} assignable au type defini pour cet element
   * @throws InvalidParameterException Si le type de la propriete n'est pas reconnu
   */
  public Object read() {
    return read(WPropertyKey.manager);
  }

  /**
   * Methode permettant d'ecrire une valeur a un fichier de propriete avec le {@code
   * PropertiesManager} par defaut
   *
   * @param value La valeur de la propriete
   * @param save {@code true} si le fichier doit etre sauvegardé apres l'ecriture
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   * @throws InvalidParameterException si la valeur passee en parametre n'est pas une instance du
   *     type defini pour l'element
   */
  public boolean write(Object value, boolean save) {
    return write(value, WPropertyKey.manager, save);
  }

  /**
   * Methode permettant d'ecrire une valeur a un fichier de propriete avec le {@code
   * PropertiesManager} par defaut, et sans sauvegarde
   *
   * @param value La valeur de la propriete
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   * @throws InvalidParameterException si la valeur passee en parametre n'est pas une instance du
   *     type defini pour l'element
   */
  public boolean write(Object value) {
    return write(value, false);
  }

  /**
   * Methode permettant de lire la valeur de cet element
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return un {@code Object} assignable au type defini pour cet element
   * @throws InvalidParameterException Si le type de la propriete n'est pas reconnu
   */
  public Object read(PropertiesManager manager) {
    if (this.type == Boolean.class) return manager.getBooleanProperty(key);

    if (this.type == Integer.class) return manager.getIntProperty(key);

    if (this.type == int[].class) return manager.getIntArrayProperty(key);

    if (this.type == Double.class) return manager.getDoubleProperty(key);

    if (this.type == double[].class) return manager.getDoubleArrayProperty(key);

    if (this.type == String.class) return manager.getStringProperty(key);

    if (type == String.class.arrayType()) return manager.getArrayProperty(key);

    if (this.type == Color.class) return manager.getColorProperty(key);

    throw new InvalidParameterException(
        "Le type de l'objet n'est pas reconnu : " + type.toString());
  }

  /**
   * Methode permettant d'ecrire une valeur a un fichier de propriete
   *
   * @param manager le {@code PropertiesManager} avec lequel ecrire
   * @param value La valeur de la propriete
   * @param save {@code true} si le fichier de proprietes doit etre sauvegardé apres la modification
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   * @throws InvalidParameterException si la valeur passee en parametre n'est pas une instance du
   *     type defini pour l'element
   */
  public boolean write(Object value, PropertiesManager manager, boolean save) {
    if (!type.isInstance(value))
      throw new InvalidParameterException(
          "La valeur de type " + value.getClass() + " n'est pas une instance de " + type);

    if (this.type == Integer.class) return manager.setIntProperty(key, (int) value, save);

    if (this.type == Double.class) return manager.setDoubleProperty(key, (double) value, save);

    if (this.type == String.class) return manager.setStringProperty(key, (String) value, save);

    if (this.type == Boolean.class) return manager.setBooleanProperty(key, (boolean) value, save);

    if (this.type == double[].class)
      return manager.setDoubleArrayProperty(key, (double[]) value, save);

    if (this.type == int[].class) return manager.setIntArrayProperty(key, (int[]) value, save);

    if (type == String.class.arrayType())
      return manager.setArrayProperty(key, (String[]) value, save);

    if (this.type == Color.class) return manager.setColorProperty(key, (Color) value, save);

    return false;
  }

  /**
   * Methode permettant d'ecrire une valeur a un fichier de propriete, sans sauvegarde
   *
   * @param manager le {@code PropertiesManager} avec lequel ecrire
   * @param value La valeur de la propriete
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   * @throws InvalidParameterException si la valeur passee en parametre n'est pas une instance du
   *     type defini pour l'element
   */
  public boolean write(Object value, PropertiesManager manager) {
    return write(value, manager, false);
  }

  /**
   * Methode permettant de definir le {@code PropertiesManager} de tous les elements
   *
   * @param manager Le {@code PropertiesManager} de elements
   */
  protected static void setPropertiesManager(PropertiesManager manager) {
    WPropertyKey.manager = manager;
  }

  /**
   * Methode permettant d'ecrire la propriete au {@code Properties} passe en parametre
   *
   * @param value La valeur de la propriete
   * @param properties Le {@code Properties} auquel ecrire
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  public boolean write(Object value, Properties properties) {
    if (!type.isInstance(value))
      throw new InvalidParameterException(
          "La valeur de type " + value.getClass() + " n'est pas une instance de " + type);

    if (this.type == Integer.class)
      return WPropertiesUtils.setIntProperty(key, (int) value, properties);

    if (this.type == Double.class)
      return WPropertiesUtils.setDoubleProperty(key, (double) value, properties);

    if (this.type == String.class)
      return WPropertiesUtils.setStringProperty(key, (String) value, properties);

    if (this.type == Boolean.class)
      return WPropertiesUtils.setBooleanProperty(key, (boolean) value, properties);

    if (this.type == double[].class)
      return WPropertiesUtils.setDoubleArrayProperty(key, (double[]) value, properties);

    if (this.type == int[].class)
      return WPropertiesUtils.setIntArrayProperty(key, (int[]) value, properties);

    if (type == String.class.arrayType())
      return WPropertiesUtils.setArrayProperty(key, (String[]) value, properties);

    if (this.type == Color.class)
      return WPropertiesUtils.setColorProperty(key, (Color) value, properties);

    return false;
  }

  /**
   * Methode permettant de lire la valeur a partir d'un objet {@code Properties}
   *
   * @param properties Le {@code Properties} a partir duquel lire la valeur
   * @return un {@code Object} assignable au type defini pour cet element
   * @throws InvalidParameterException Si le type de la propriete n'est pas reconnu
   */
  public Object read(Properties properties) {
    if (this.type == Boolean.class) return WPropertiesUtils.getBooleanProperty(key, properties);

    if (this.type == Integer.class) return WPropertiesUtils.getIntProperty(key, properties);

    if (this.type == int[].class) return WPropertiesUtils.getIntArrayProperty(key, properties);

    if (this.type == Double.class) return WPropertiesUtils.getDoubleProperty(key, properties);

    if (this.type == double[].class)
      return WPropertiesUtils.getDoubleArrayProperty(key, properties);

    if (this.type == String.class) return WPropertiesUtils.getStringProperty(key, properties);

    if (type == String.class.arrayType()) return WPropertiesUtils.getArrayProperty(key, properties);

    if (this.type == Color.class) return WPropertiesUtils.getColorProperty(key, properties);

    throw new InvalidParameterException(
        "Le type de l'objet n'est pas reconnu : " + type.toString());
  }

  /**
   * Methode permettant de lire la valeur a partir d'un objet {@code Properties}, et retourner la
   * valeur par defaut passee en parametre en cas d'erreur
   *
   * @param properties Le {@code Properties} a partir duquel lire la valeur
   * @param defaultValue La valeur par defaut a retourner en cas d'erreur
   * @return un {@code Object} assignable au type defini pour cet element
   * @throws InvalidParameterException Si le type de la propriete n'est pas reconnu
   */
  public Object read(Properties properties, Object defaultValue) {
    if (this.type == Boolean.class)
      return WPropertiesUtils.getBooleanProperty(key, properties, (boolean) defaultValue);

    if (this.type == Integer.class)
      return WPropertiesUtils.getIntProperty(key, properties, (int) defaultValue);

    if (this.type == int[].class)
      return WPropertiesUtils.getIntArrayProperty(key, properties, (int[]) defaultValue);

    if (this.type == Double.class)
      return WPropertiesUtils.getDoubleProperty(key, properties, (double) defaultValue);

    if (this.type == double[].class)
      return WPropertiesUtils.getDoubleArrayProperty(key, properties, (double[]) defaultValue);

    if (this.type == String.class)
      return WPropertiesUtils.getStringProperty(key, properties, (String) defaultValue);

    if (type == String.class.arrayType())
      return WPropertiesUtils.getArrayProperty(key, properties, (String[]) defaultValue);

    if (this.type == Color.class)
      return WPropertiesUtils.getColorProperty(key, properties, (Color) defaultValue);

    throw new InvalidParameterException(
        "Le type de l'objet n'est pas reconnu : " + type.toString());
  }
}
