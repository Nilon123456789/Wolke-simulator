package com.e24.wolke.backend.models.simulation;

import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.filesystem.properties.PropertiesManager;
import com.e24.wolke.filesystem.properties.SettingsPropertiesManager;
import com.e24.wolke.utils.math.WMath;
import com.e24.wolke.utils.math.WVector2D;

/**
 * La classe {@code SimulationConstants} regroupe les constantes liees a la simulation.
 *
 * @author MeriBouisri
 * @author Nilon123456789
 */
public final class SimulationConstants {

  /** Le {@code PropertiesManager} de la classe */
  public static final PropertiesManager PROPERTIES_MANAGER = SettingsPropertiesManager.INSTANCE;

  /** La taille en x de la matrice de particules, par defaut */
  public static final int DEFAULT_MATRIX_SIZE_X =
      ApplicationConstants.DEFAULT_RESOLUTION.getWidth();

  /** La taille en y de la matrice de particules, par defaut */
  public static final int DEFAULT_MATRIX_SIZE_Y =
      ApplicationConstants.DEFAULT_RESOLUTION.getHeight();

  /** La taille physique en x par défaut de la matrice de particules (m) */
  public static final double DEFAULT_PHYSICAL_MATRIX_SIZE_X =
      SimulationProperties.readPhysicalXLength(SimulationConstants.PROPERTIES_MANAGER);

  /** La taille physique en y par défaut de la matrice de particules (m) */
  public static final double DEFAULT_PHYSICAL_MATRIX_SIZE_Y =
      SimulationProperties.readPhysicalYLength(SimulationConstants.PROPERTIES_MANAGER);

  /** Type de fluide par defaut */
  public static final Fluid DEFAULT_FLUID =
      SimulationProperties.readFluid(SimulationConstants.PROPERTIES_MANAGER);

  /** Longeur maximal pour les listes de matrices de particules */
  public static final int MAX_PARTICLE_MATRIX_QUEUE_SIZE =
      SettingsPropertiesManager.INSTANCE.getIntProperty("renderer.maxQueuedFrames");

  // ===== VISCOSITY ===== //

  /** La viscosite du fluide, par defaut (Pa*s) */
  public static final double DEFAULT_VISCOSITY = 1.8E-5;

  /** Valeur minimale de la viscosite (Pa*s) */
  public static final double MIN_VISCOSITY = WMath.EPSILON;

  /** Valeur maximale de la viscosite (Pa*s) */
  public static final double MAX_VISCOSITY = Double.MAX_VALUE;

  /** Facteur de grandissement de la valeur pour le spinner */
  public static final double VISCOSITY_SPINNER_FACTOR = 1_000;

  // ===== VOLUME DENSITY ===== //

  /** La masse volumique du fluide, par defaut (kg/m^3) */
  public static final double DEFAULT_VOLUME_DENSITY = 1.225;

  /** Valeur minimale de la densite volumique (kg/m^3) */
  public static final double MIN_VOLUME_DENSITY = 0.0;

  /** Valeur maximale de la densite volumique (kg/m^3) */
  public static final double MAX_VOLUME_DENSITY = 1.0;

  // ===== TEMPERATURE ===== //

  /** La temperature du fluide, par defaut */
  public static final double DEFAULT_TEMPERATURE_DEG = 20;

  /** Valeur minimale de la temperature */
  public static final double MIN_TEMPERATURE_DEG = Double.MIN_VALUE;

  /** Valeur maximale de la temperature */
  public static final double MAX_TEMPERATURE_DEG = Double.MAX_VALUE;

  // ===== INITIAL VELOCITY ===== //

  /** La vitesse initiale, par defaut (m/s) */
  public static final double DEFAULT_INITIAL_VELOCITY =
      SimulationProperties.readInitialVelocity(SimulationConstants.PROPERTIES_MANAGER);

  /** Valeur minimale de la vitesse initiale (m/s) */
  public static final double MIN_INITIAL_VELOCITY = 0.0;

  /** Valeur maximale de la vitesse initiale (m/s) */
  public static final double MAX_INITIAL_VELOCITY = 340; // Vitesse du son

  /** Le pas de la simulation, par defaut */
  public static final double DEFAULT_TIME_STEP =
      SimulationProperties.readTimeStep(SimulationConstants.PROPERTIES_MANAGER);

  /** Le temps de sleep entre chaque iteration, par defaut */
  public static final double DEFAULT_SLEEP_TIME = 0.01;

  /** Type de bordure par defaut */
  public static final BORDER_TYPE DEFAULT_BORDER_TYPE =
      SimulationProperties.readBorderType(SimulationConstants.PROPERTIES_MANAGER);

  /** Facteur de confinement des vortex */
  public static final double DEFAULT_VORTEX_CONFINEMENT_FACTOR =
      SimulationProperties.readVortexConfinementFactor(SimulationConstants.PROPERTIES_MANAGER);

  /** Si la simulation est multithreaded ou non */
  public static final boolean DEFAULT_MULTITHREADED =
      SimulationProperties.readMultiThreaded(SimulationConstants.PROPERTIES_MANAGER);

  /** Facteur minimal de confinement des vortex */
  public static final double VORTEX_CONFINEMENT_MIN_FACTOR = 0.0;

  /** Facteur maximal de confinement des vortex */
  public static final double VORTEX_CONFINEMENT_MAX_FACTOR = 1.0;

  /** Nombre maximum d'itérations pour la méthode de Jacobi */
  // TODO : Voir pour mettre une valeur plus appropriée
  public static final int MAX_JACOBI_ITERATIONS =
      SimulationProperties.readMaxJacobiIterations(SimulationConstants.PROPERTIES_MANAGER);

  /**
   * Pourcentage maximal entre deux itérations pour que la matrice soit considérée comme résolue (1
   * == 100%)
   */
  // TODO : Voir pour mettre une valeur plus appropriée
  public static final double MAX_JACOBI_DIFF =
      SimulationProperties.readMaxJacobiDiff(SimulationConstants.PROPERTIES_MANAGER);

  /** Valeur minimale du nombre de CFL avant de donner un avertissement */
  public static final double MIN_CFL_WARN =
      SimulationProperties.readMinCFLWarn(SimulationConstants.PROPERTIES_MANAGER);

  /** Valeur minimale du nombre de CFL avant de donner une erreur */
  public static final double MIN_CFL_ERR =
      SimulationProperties.readMinCFLError(SimulationConstants.PROPERTIES_MANAGER);

  /** Frequence de verification du CFL */
  public static final int CFL_CHECK_FREQUENCY =
      SimulationProperties.readCFLCheckFrequency(SimulationConstants.PROPERTIES_MANAGER);

  /**
   * Les types d'obstacles
   *
   * <p>[NONE, SLIP, STICK, INFLOW, OUTFLOW, ZERO]
   *
   * @author Nilon123456789
   */
  public enum OBSTACLE_TYPE {
    /**
     * Slip Boundary
     *
     * <pre>
     * Vertical :
     *    (u, -v)  Outside
     *   --------  Border
     *    (u, v)   Inside
     *  </pre>
     *
     * <pre>
     *  Horizontal :
     *    (-u, v)  Outside
     *   --------  Border
     *    (u, v)   Inside
     * </pre>
     *
     * <pre>
     *  Pressure :
     *       p     Outside
     *   --------  Border
     *       p     Inside
     * </pre>
     */
    SLIP,

    /**
     * Stick Boundary
     *
     * <pre>
     *   (-u, -v)   Outside
     *  ----------  Border
     *    (u, v)    Inside
     * </pre>
     *
     * <pre>
     *  Pressure :
     *       p     Outside
     *   --------  Border
     *       p     Inside
     * </pre>
     */
    STICK,

    /**
     * Inflow
     *
     * <pre>
     *  Horizontal :
     *    (u, 0)   Outside
     *   --------  Border
     *    (u, v)   Inside
     * </pre>
     *
     * <pre>
     *  Vertical :
     *    (0, v)   Outside
     *   --------  Border
     *    (u, v)   Inside
     * </pre>
     *
     * <pre>
     *  Pressure :
     *       p    Outside
     *   -------- Border
     *       p    Inside
     * </pre>
     */
    INFLOW,

    /**
     * Outflow
     *
     * <pre>
     *  Horizontal :
     *    (u, 0)   Outside
     *   --------  Border
     *    (u, v)   Inside
     * </pre>
     *
     * <pre>
     *  Vertical :
     *    (0, v)   Outside
     *   --------  Border
     *    (u, v)   Inside
     * </pre>
     *
     * <pre>
     *  Pressure :
     *      -p     Outside
     *   --------  Border
     *      p      Inside
     * </pre>
     */
    OUTFLOW,

    /**
     * ZERO
     *
     * <pre>
     * Velocity :
     *    (0, 0)   Outside
     *   --------  Border
     *    (u, v)   Inside
     * </pre>
     *
     * <pre>
     *  Pressure :
     *       0     Outside
     *   --------  Border
     *       p     Inside
     * </pre>
     */
    ZERO,

    /** None */
    NONE;

    /** La valeur binaire representant l'element */
    final byte binaryValue;

    /**
     * Construction d'un element de {@code BORDER_TYPE} avec une valeur binaire
     *
     * @param binaryValue La valeur binaire representant l'element
     */
    OBSTACLE_TYPE(byte binaryValue) {
      this.binaryValue = binaryValue;
    }

    /**
     * Construction d'un element de {@code BORDER_TYPE}. La valeur binaire {@code this#binaryValue}
     * est representee par la position ordinale de l'element.
     */
    OBSTACLE_TYPE() {
      binaryValue = (byte) ordinal();
    }
  }

  /**
   * Les types de bordures de la simulation
   *
   * <p>[WIND_TUNNEL, BOX, OPEN]
   *
   * @author Nilon123456789
   */
  public enum BORDER_TYPE {
    /**
     * Wind Tunnel
     *
     * <pre>
     * Simulation :
     * &#9473; &#9473; &#9473; &#9473; &#9473; &#9473;
     * &#8696; &#8594; &#8594; &#8594; &#8594; &#8696;
     * &#8696; &#8594; &#8594; &#8594; &#8594; &#8696;
     * &#8696; &#8594; &#8594; &#8594; &#8594; &#8696;
     * &#9473; &#9473; &#9473; &#9473; &#9473; &#9473;
     * </pre>
     */
    WIND_TUNNEL("ui.simulation.border_type.wind_tunnel"),

    /**
     * Box
     *
     * <pre>
     * Simulation :
     * &#9487; &#9473; &#9473; &#9473; &#9473; &#9491;
     * &#9475; &#8226; &#8226; &#8226; &#8226; &#9475;
     * &#9475; &#8226; &#8226; &#8226; &#8226; &#9475;
     * &#9475; &#8226; &#8226; &#8226; &#8226; &#9475;
     * &#9495; &#9473; &#9473; &#9473; &#9473; &#9499;
     * </pre>
     */
    BOX("ui.simulation.border_type.box"),

    /**
     * Open
     *
     * <pre>
     * Simulation :
     *  &#10505; &#10505; &#10505; &#10505; &#10505; &#10505; &#10505;
     *  &#8695; &#8226; &#8226; &#8226; &#8226; &#8696;
     *  &#8695; &#8226; &#8226; &#8226; &#8226; &#8696;
     *  &#8695; &#8226; &#8226; &#8226; &#8226; &#8696;
     *  &#10504; &#10504; &#10504; &#10504; &#10504; &#10504; &#10504;
     * </pre>
     */
    OPEN("ui.simulation.border_type.open");

    /** L'identifiant pour le nom */
    private final String nameLocalizationString;

    /**
     * Construction d'un element de {@code BORDER_TYPE} avec une propriete de localisation de langue
     *
     * @param nameLocalizationString Nom de la propriete de localisation
     */
    BORDER_TYPE(String nameLocalizationString) {
      this.nameLocalizationString = nameLocalizationString;
    }

    /**
     * Getter pour {@code this#nameLocalizationString}, le nom de la propriete de localisation
     *
     * @return Le nom de la propriete de localisation
     */
    public String getNameLocalizationString() {
      return this.nameLocalizationString;
    }

    /** Retourne le nom de l'element */
    @Override
    public String toString() {
      return LocaleManager.getLocaleResourceBundle().getString(nameLocalizationString);
    }

    /**
     * Retourne l'element de {@code BORDER_TYPE} correspondant à l'index
     *
     * @param index L'index de l'element
     * @return L'element de {@code BORDER_TYPE} correspondant à l'index ou {@code
     *     BORDER_TYPE.WIND_TUNNEL} si l'index est invalide
     */
    public static BORDER_TYPE getBorderType(int index) {
      for (BORDER_TYPE borderType : BORDER_TYPE.values()) {
        if (borderType.ordinal() != index) continue;
        return borderType;
      }
      return BORDER_TYPE.WIND_TUNNEL;
    }
  }

  /**
   * Classe enum regroupant les differents types de fluides de la simulation.
   *
   * <p>[AIR, WATER, OIL, HONEY]
   *
   * @author Nilon123456789
   */
  public enum Fluid {

    /**
     * Air
     *
     * <p>Viscosité dynamique : {@code 1.825E-5 Pa*s}
     *
     * <p>Masse volumique : {@code 1.204 kg/m^3}
     */
    AIR("ui.simulation.fluid.air", 1.825E-5, 1.204, 0.5),

    /**
     * Eau
     *
     * <p>Viscosité dynamique : {@code 1.002E-3 Pa*s}
     *
     * <p>Masse volumique : {@code 998.2 kg/m^3}
     */
    WATER("ui.simulation.fluid.water", 1.002E-3, 998.2, 0.35),

    /**
     * Huile
     *
     * <p>Viscosité dynamique : {@code 0.23939 Pa*s}
     *
     * <p>Masse volumique : {@code 881.5 kg/m^3}
     */
    OIL("ui.simulation.fluid.oil", 0.23939, 881.5, 0.15),

    /**
     * Miel
     *
     * <p>Viscosité dynamique : {@code 3.93 Pa*s}
     *
     * <p>Masse volumique : {@code 1420.0 kg/m^3}
     */
    HONEY("ui.simulation.fluid.honey", 3.93, 1420.0, 0);

    /** L'identifant pour le nom */
    private final String nameLocalizationString;

    /** La viscosité dynamique (Pa*s) */
    private final double dynamicViscosity;

    /** La masse volumique (kg/m^3) */
    private final double volumeDensity;

    /** Le facteur de confinement des vortex */
    private final double vortexConfinementFactor;

    /**
     * Construction d'un element de {@code Fluid} avec une propriete de localisation de langue, une
     * viscosite dynamique et une masse volumique
     *
     * @param nameLocalizationString Nom de la propriete de localisation
     * @param dynamicViscosity La viscosite dynamique
     * @param volumeDensity La masse volumique
     * @param vortexConfinementFactor Le facteur de confinement des vortex
     */
    Fluid(
        String nameLocalizationString,
        double dynamicViscosity,
        double volumeDensity,
        double vortexConfinementFactor) {
      this.nameLocalizationString = nameLocalizationString;
      this.dynamicViscosity = dynamicViscosity;
      this.volumeDensity = volumeDensity;
      this.vortexConfinementFactor = vortexConfinementFactor;
    }

    /**
     * Getter pour {@code this#nameLocalizationString}, le nom de la propriete de localisation
     *
     * @return Le nom de la propriete de localisation
     */
    public String getNameLocalizationString() {
      return this.nameLocalizationString;
    }

    /**
     * Getter pour {@code this#dynamicViscosity}, la viscosite dynamique
     *
     * @return La viscosite dynamique
     */
    public double getDynamicViscosity() {
      return this.dynamicViscosity;
    }

    /**
     * Getter pour {@code this#volumeDensity}, la masse volumique
     *
     * @return La masse volumique
     */
    public double getVolumeDensity() {
      return this.volumeDensity;
    }

    /**
     * Getter pour {@code this#vortexConfinementFactor}, le facteur de confinement des vortex
     *
     * @return Le facteur de confinement des vortex
     */
    public double getVortexConfinementFactor() {
      return this.vortexConfinementFactor;
    }

    /** Retourne le nom de la propriete de localisation de l'element */
    @Override
    public String toString() {
      return LocaleManager.getLocaleResourceBundle().getString(nameLocalizationString);
    }

    /**
     * Retourne l'element de {@code FLUID} correspondant à l'index
     *
     * @param index L'index de l'element
     * @return L'element de {@code FLUID} correspondant à l'index ou {@code FLUID.AIR} si l'index
     *     est invalide
     */
    public static Fluid getFluid(int index) {
      for (Fluid fluid : Fluid.values()) {
        if (fluid.ordinal() != index) continue;
        return fluid;
      }
      return Fluid.AIR;
    }
  }

  /**
   * La classe enum {@code UnitCircle} permet de definir les angles, en radians, normalisés entre
   * [-pi, pi], du cercle trigonométrique avec les directions cardinales.
   *
   * @author MeriBouisri
   */
  public enum UnitCircle {
    /** Nord */
    N(Math.PI / 2),
    /** Sud */
    S(-Math.PI / 2),
    /** Est */
    E(0),
    /** Ouest */
    W(Math.PI),
    /** Nord est */
    NE(Math.PI / 4),
    /** Nord ouest */
    NW(3 * Math.PI / 4),
    /** Sud est */
    SE(7 * Math.PI / 4),
    /** Sud ouest */
    SW(5 * Math.PI / 4),
    /** Aucune direction */
    NONE(Double.NaN);

    /** Angle de la direction sur le cercle trigonometrique, en radians */
    private final double angleRad;

    /** Vecteur associé à l'angle de cet element */
    private final WVector2D vector;

    /**
     * Construction d'une direction sur le cercle trigonometrique avec un angle, en radians
     *
     * @param angleRad L'angle, en radians, associé a la direction de cet element
     */
    UnitCircle(double angleRad) {
      if (Double.isNaN(angleRad)) {
        this.angleRad = angleRad;
        vector = WVector2D.ZERO_VECTOR;
        return;
      }

      this.angleRad = WMath.normalizeAngle(angleRad);
      vector = new WVector2D(this.angleRad);
    }

    /**
     * Getter pour {@code this#angleRad}, l'angle (en radians) qui correspond a l'angle normalisé
     * par rapport à l'axe horizontal, du vecteur pointant vers la direction de cet element, tel que
     * sur le cercle trigonométrique. Retourne {@code Double#NaN} si l'element est {@code
     * UnitCircle#NONE}.
     *
     * @return {@code this#angleRad}, normalisé entre [-pi, pi]. Peut retourner {@code Double#NaN},
     *     si l'element est {@code UnitCircle#NONE}.
     */
    public double getAngleRad() {
      return angleRad;
    }

    /**
     * Getter pour {@code this#vector}, le vecteur associé à l'angle défini par cet element.
     * Retourne {@code Vector2#ZERO_VECTOR} si l'element est {@code UnitCircle#NONE}
     *
     * @return Un {@code Vector2} associé à l'angle de cet element.
     */
    public WVector2D getVector() {
      return vector;
    }

    /**
     * Methode permettant de determiner si l'angle passé en paramètre est approximativement égal à
     * l'angle de cet element.
     *
     * @param angleRad L'angle (en radians) a comparer. La valeur de cet angle est normalisee.
     * @return {@code true} si l'angle passé en paramètre, une fois normalisé, est approximativement
     *     egal à l'angle de cet element.
     * @see WMath#nearlyEquals(double, double)
     * @see WMath#normalizeAngle(double)
     */
    public boolean nearlyEquals(double angleRad) {
      return WMath.nearlyEquals(this.angleRad, WMath.normalizeAngle(angleRad));
    }

    /**
     * Methode permettant d'obtenir un element de {@code UnitCircle} a partir d'un angle donné.
     *
     * @param angleRad Angle, en radians, associe a une direction de {@code UnitCircle}
     * @return L'element de {@code UnitCircle} associe a l'angle donne. Retourne {@code
     *     UnitCircle#NONE} si {@code Double#isNaN(angleRad)}
     */
    public static UnitCircle getDirection(double angleRad) {
      if (!Double.isNaN(angleRad))
        for (UnitCircle unit : UnitCircle.values()) if (unit.nearlyEquals(angleRad)) return unit;

      return UnitCircle.NONE;
    }
  }

  /** Le {@code FluidType} par defaut */
  public static final FluidState DEFAULT_FLUID_STATE = FluidState.GASEOUS;

  /**
   * Enum regorupant les differents types de fluides de la simulation.
   *
   * @author MeriBouisri
   */
  public enum FluidState {
    /** Fluide gazeux */
    GASEOUS("ui.simulation.fluid_type.gaseous"),
    /** Fluide liquide */
    LIQUID("ui.simulation.fluid_type.liquid");

    /** Le nom de la propriete de localisation */
    private final String localeProperty;

    /**
     * Construction d'un element de {@code FluidType} avec une propriete de localisation de langue
     *
     * @param localeProperty Nom de la propriete de localisation
     */
    FluidState(String localeProperty) {
      this.localeProperty = localeProperty;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return LocaleManager.getLocaleResourceBundle().getString(localeProperty);
    }
  }

  /** L'initialisation de la classe {@code SimulationConstants} est interdite. */
  private SimulationConstants() {
    throw new AssertionError();
  }
}
