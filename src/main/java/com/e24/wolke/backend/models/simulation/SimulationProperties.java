package com.e24.wolke.backend.models.simulation;

import com.e24.wolke.backend.models.WProperties;
import com.e24.wolke.backend.models.application.ApplicationProperties;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.simulation.SimulationConstants.BORDER_TYPE;
import com.e24.wolke.backend.models.simulation.SimulationConstants.Fluid;
import com.e24.wolke.filesystem.properties.PropertiesManager;
import com.e24.wolke.filesystem.properties.WPropertyKey;
import com.e24.wolke.filesystem.properties.WStandardPropertiesProcessor;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code SImulationProperties} permet de regrouper les proprietes de la simulation
 *
 * @author MeriBouisri
 */
public class SimulationProperties extends WProperties implements WStandardPropertiesProcessor {
  /** SerialVersionUID de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Logger} de la classe */
  private static final Logger LOGGER =
      LogManager.getLogger(SimulationProperties.class.getClass().getSimpleName());

  /** Masse volumique du fluide de la simulation (kg/m^3) */
  protected double volumeDensity = SimulationConstants.DEFAULT_VOLUME_DENSITY;

  /** Viscosite du fluide */
  protected double viscosity = SimulationConstants.DEFAULT_VISCOSITY;

  /** Vitesse du fluide si la bordure est en mode WIND_TUNNEL */
  protected double initialVelocity = SimulationConstants.DEFAULT_INITIAL_VELOCITY;

  /** Type de bordure de la matrice de particules */
  protected BORDER_TYPE borderType = SimulationConstants.DEFAULT_BORDER_TYPE;

  /** Le type de fluide */
  protected Fluid fluid = SimulationConstants.DEFAULT_FLUID;

  /** Temps de repos entre chaque itération (en ms) */
  protected double sleepTime = SimulationConstants.DEFAULT_SLEEP_TIME;

  /** Pas de la simulation (en ms) */
  protected double timeStep = SimulationConstants.DEFAULT_TIME_STEP;

  /** Valeur du confinement de vortex */
  protected double vortexConfinementFactor = SimulationConstants.DEFAULT_VORTEX_CONFINEMENT_FACTOR;

  /** Longueur de la simulation en X */
  protected int xLength = SimulationConstants.DEFAULT_MATRIX_SIZE_X;

  /** Largeur de la simulation en Y */
  protected int yLength = SimulationConstants.DEFAULT_MATRIX_SIZE_Y;

  /** Longueur physique de la simulation en X */
  protected double physicalXLength = SimulationConstants.DEFAULT_PHYSICAL_MATRIX_SIZE_X;

  /** Longueur physique de la simulation en Y */
  protected double physicalYLength = SimulationConstants.DEFAULT_PHYSICAL_MATRIX_SIZE_Y;

  /** Si la simulation est multithreaded */
  protected boolean multiThreaded = SimulationConstants.DEFAULT_MULTITHREADED;

  /**
   * COnstruction d'un {@code SimulationProperties}
   *
   * @param xLength La taille en x de la matrice de particules (px)
   * @param yLength La taille en y de la matrice de particules (px)
   * @param physicalXLength La taille physique en x de la matrice de particules (m)
   * @param physicalYLength La taille physique en y de la matrice de particules (m)
   * @param viscosity La viscosité du fluide (Pa*s)
   * @param volumeDensity La masse volumique du fluide (kg/m^3)
   * @param initialVelocity La vitesse du fluide si la bordure est en mode WIND_TUNNEL (m/s)
   * @param vortexConfinementFactor Valeur du confinement de vortex
   * @param timeStep Le pas de la simulation (en ms)
   * @param sleepTime Le temps de sleep entre chaque iteration (en ms)
   * @param borderType Le type de bordure de la matrice de particules
   * @param fluid Le type de fluide
   * @param multiThreaded Si la simulation est multithreaded
   */
  public SimulationProperties(
      int xLength,
      int yLength,
      double physicalXLength,
      double physicalYLength,
      double viscosity,
      double volumeDensity,
      double initialVelocity,
      double vortexConfinementFactor,
      double timeStep,
      double sleepTime,
      BORDER_TYPE borderType,
      Fluid fluid,
      boolean multiThreaded) {
    super();

    this.volumeDensity = volumeDensity;
    this.initialVelocity = initialVelocity;
    this.vortexConfinementFactor = vortexConfinementFactor;
    this.sleepTime = sleepTime;
    this.timeStep = timeStep;
    this.physicalXLength = physicalXLength;
    this.physicalYLength = physicalYLength;
    this.xLength = xLength;
    this.yLength = yLength;
    this.borderType = borderType;
    this.fluid = fluid;
    this.multiThreaded = multiThreaded;
  }

  /** Construction d'un {@code SimulationProperties} avec valeurs par defaut */
  public SimulationProperties() {
    super();
  }

  // ========== PHYSICAL LENGTH ========== //

  /**
   * Methode permettant de lire la valeur de {@code xLength} avec le {@code PropertiesManager} de
   * cette instance
   *
   * @return La valeur de {@code xLength}, ou la valeur actuelle en cas d'erreur
   */
  private int readXLength() {
    try {
      return SimulationProperties.readXLength(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return xLength;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code xLength}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code xLength}
   */
  public static int readXLength(PropertiesManager manager) {
    return ApplicationProperties.readResolution(manager).getWidth();
  }

  /**
   * Methode permettant de lire la valeur de {@code yLength} avec le {@code PropertiesManager} de
   * cette instance
   *
   * @return La valeur de {@code yLength}, ou la valeur actuelle en cas d'erreur
   */
  private int readYLength() {
    try {
      return SimulationProperties.readYLength(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return yLength;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code yLength}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code yLength}
   */
  public static int readYLength(PropertiesManager manager) {
    return ApplicationProperties.readResolution(manager).getHeight();
  }

  /**
   * Methode permettant de lire la valeur de {@code this#physicalXLength} et {@code
   * ths#physicalYLength}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur de la taille physique, dans un tableau {@code double[2]}
   */
  public static int[] readLength(PropertiesManager manager) {
    return (int[]) ApplicationProperties.readResolution(manager).getSize();
  }

  // ========== PHYSICAL LENGTH ========== //

  /**
   * Methode permettant de lire la valeur de {@code physicalXLength} avec le {@code
   * PropertiesManager} de cette instance
   *
   * @return La valeur de {@code physicalXLength}, ou la valeur actuelle en cas d'erreur
   */
  private double readPhysicalXLength() {
    try {
      return SimulationProperties.readPhysicalXLength(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return physicalXLength;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code physicalXLength}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code physicalXLength}
   */
  public static double readPhysicalXLength(PropertiesManager manager) {
    return SimulationProperties.readPhysicalLength(manager)[0];
  }

  /**
   * Methode permettant de lire la valeur de {@code physicalYLength} avec le {@code
   * PropertiesManager} de cette instance
   *
   * @return La valeur de {@code physicalYLength}, ou la valeur actuelle en cas d'erreur
   */
  private double readPhysicalYLength() {
    try {
      return SimulationProperties.readPhysicalYLength(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return physicalYLength;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code physicalYLength}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code physicalYLength}
   */
  public static double readPhysicalYLength(PropertiesManager manager) {
    return SimulationProperties.readPhysicalLength(manager)[1];
  }

  /**
   * Methode permettant de lire la valeur de {@code this#physicalXLength} et {@code
   * ths#physicalYLength}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur de la taille physique, dans un tableau {@code double[2]}
   */
  public static double[] readPhysicalLength(PropertiesManager manager) {
    return (double[]) WPropertyKey.SIMULATION_PHYSICAL_SIZE.read(manager);
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code physicalXLength} et {@code physicalYLength}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writePhysicalLength() {
    try {
      return WPropertyKey.SIMULATION_PHYSICAL_SIZE.write(
          new double[] {physicalXLength, physicalYLength}, getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // ========== VOLUME DENSITY ========== //

  /**
   * Methode permettant de lire la valeur de {@code volumeDensity} avec le {@code PropertiesManager}
   * de cette instance
   *
   * @return La valeur de {@code volumeDensity}, ou la valeur par defaut en cas d'erreur
   */
  private double readVolumeDensity() {
    try {
      return SimulationProperties.readVolumeDensity(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return volumeDensity;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code volumeDensity}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code volumeDensity}
   */
  public static double readVolumeDensity(PropertiesManager manager) {
    return (double) WPropertyKey.SIMULATION_VOLUME_DENSITY.read(manager);
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code volumeDensity}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeVolumeDensity() {
    try {
      return WPropertyKey.SIMULATION_VOLUME_DENSITY.write(volumeDensity, getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // ========== BORDER TYPE ========== //

  /**
   * Methode permettant de lire la valeur de {@code borderType} avec le {@code PropertiesManager} de
   * cette instance
   *
   * @return La valeur de {@code borderType}, ou la valeur actuelle en cas d'erreur
   */
  private BORDER_TYPE readBorderType() {
    try {
      return SimulationProperties.readBorderType(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return borderType;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code borderType}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code borderType}
   */
  public static BORDER_TYPE readBorderType(PropertiesManager manager) {
    return BORDER_TYPE.getBorderType((int) WPropertyKey.SIMULATION_BORDER_TYPE.read(manager));
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code BORDER_TYPE}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeBorderType() {
    try {
      return WPropertyKey.SIMULATION_BORDER_TYPE.write(borderType.ordinal(), getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // ========== FLUID TYPE ========== //

  /**
   * Methode permettant de lire la valeur de {@code fluid} avec le {@code PropertiesManager} de
   * cette instance
   *
   * @return La valeur de {@code fluid}, ou la valeur actuelle en cas d'erreur
   */
  private Fluid readFluid() {
    try {
      return SimulationProperties.readFluid(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return fluid;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code fluid}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code fluid}
   */
  public static Fluid readFluid(PropertiesManager manager) {
    return Fluid.getFluid((int) WPropertyKey.SIMULATION_FLUID_TYPE.read(manager));
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code fluid}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeFluid() {
    try {
      return WPropertyKey.SIMULATION_FLUID_TYPE.write(fluid.ordinal(), getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // ========== MULTITHREADED ========== //

  /**
   * Methode permettant de lire la valeur de {@code multiThreaded} avec le {@code PropertiesManager}
   * de cette instance
   *
   * @return La valeur de {@code multiThreaded}, ou la valeur actuelle en cas d'erreur
   */
  private boolean readMultiThreaded() {
    try {
      return SimulationProperties.readMultiThreaded(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return multiThreaded;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code multiThreaded}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code multiThreaded}
   */
  public static boolean readMultiThreaded(PropertiesManager manager) {
    return (boolean) WPropertyKey.SIMULATION_MULTITHREADED.read(manager);
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code multiThreaded}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeMultiThreaded() {
    try {
      return WPropertyKey.SIMULATION_MULTITHREADED.write(multiThreaded, getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // ========== VISCOSITY ========== //

  /**
   * Methode permettant de lire la valeur de {@code viscosity} avec le {@code PropertiesManager} de
   * cette instance
   *
   * @return La valeur de {@code viscosity}, ou la valeur actuelle en cas d'erreur
   */
  private double readViscosity() {
    try {
      return SimulationProperties.readViscosity(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return viscosity;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code multiThreaded}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code multiThreaded}
   */
  public static double readViscosity(PropertiesManager manager) {
    return (double) WPropertyKey.SIMULATION_VISCOSITY.read(manager);
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code viscosity}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeViscosity() {
    try {
      return WPropertyKey.SIMULATION_VISCOSITY.write(viscosity, getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // ========== INITIAL VELOCITY ========== //

  /**
   * Methode permettant de lire la valeur de {@code initialVelocity} avec le {@code
   * PropertiesManager} de cette instance
   *
   * @return La valeur de {@code initialVelocity}, ou la valeur actuelle en cas d'erreur
   */
  private double readInitialVelocity() {
    try {
      return SimulationProperties.readInitialVelocity(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return initialVelocity;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code initialVelocity}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code initialVelocity}
   */
  public static double readInitialVelocity(PropertiesManager manager) {
    return (double) WPropertyKey.SIMULATION_INITIAL_VELOCITY.read(manager);
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code initialVelocity}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeInitialVelocity() {
    try {
      return WPropertyKey.SIMULATION_INITIAL_VELOCITY.write(initialVelocity, getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // ========== TIMESTEP ========== //

  /**
   * Methode permettant de lire la valeur de {@code timeStep} avec le {@code PropertiesManager} de
   * cette instance
   *
   * @return La valeur de {@code timeStep}, ou la valeur actuelle en cas d'erreur
   */
  private double readTimeStep() {
    try {
      return SimulationProperties.readTimeStep(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return timeStep;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code timeStep}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code timeStep}
   */
  public static double readTimeStep(PropertiesManager manager) {
    return (double) WPropertyKey.SIMULATION_TIME_STEP.read(manager);
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code timeStep}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeTimeStep() {
    try {
      return WPropertyKey.SIMULATION_TIME_STEP.write(timeStep, getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // ========== SLEEPTIME ========== //

  /**
   * Methode permettant de lire la valeur de {@code sleepTime} avec le {@code PropertiesManager} de
   * cette instance
   *
   * @return La valeur de {@code sleepTime}, ou la valeur actuelle en cas d'erreur
   */
  private double readSleepTime() {
    try {
      return SimulationProperties.readSleepTime(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return sleepTime;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code sleepTime}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code sleepTime}
   */
  public static double readSleepTime(PropertiesManager manager) {
    return (double) WPropertyKey.SIMULATION_SLEEP_TIME.read(manager);
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code sleepTime}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeSleepTime() {
    try {
      return WPropertyKey.SIMULATION_TIME_STEP.write(sleepTime, getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // ========== VORTEX CONFINEMENT FACTOR ========== //

  /**
   * Methode permettant de lire la valeur de {@code vortexConfinementFactor} avec le {@code
   * PropertiesManager} de cette instance
   *
   * @return La valeur de {@code vortexConfinementFactor}, ou la valeur actuelle en cas d'erreur
   */
  private double readVortexConfinementFactor() {
    try {
      return SimulationProperties.readVortexConfinementFactor(getPropertiesManager());
    } catch (Exception e) {
      e.printStackTrace();
      return vortexConfinementFactor;
    }
  }

  /**
   * Methode permettant de lire la valeur de {@code vortexConfinementFactor}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur pour {@code vortexConfinementFactor}
   */
  public static double readVortexConfinementFactor(PropertiesManager manager) {
    return (double) WPropertyKey.SIMULATION_VORTEX_CONFINEMENT.read(manager);
  }

  /**
   * Methode permettant d'ecrire la valeur de {@code vortexConfinementFactor}
   *
   * @return {@code true} si l'ecriture a ete effectuee avec succes
   */
  private boolean writeVortexConfinementFactor() {
    try {
      return WPropertyKey.SIMULATION_VORTEX_CONFINEMENT.write(
          vortexConfinementFactor, getSaveState());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // ========== MISC ========== //

  /**
   * Methode permettant de lire la valeur de {@code maxJacobiIterations}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur de {@code maxJacobiIterations}
   */
  public static int readMaxJacobiIterations(PropertiesManager manager) {
    return (int) WPropertyKey.SIMULATION_JACOBISOLVER_MAX_ITERATIONS.read(manager);
  }

  /**
   * Methode permettant de lire la valeur de {@code maxJacobiDiff}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur de {@code maxJacobiDiff}
   */
  public static double readMaxJacobiDiff(PropertiesManager manager) {
    return (double) WPropertyKey.SIMULATION_JACOBISOLVER_TOLERANCE.read(manager);
  }

  /**
   * Methode permettant de lire la valeur de {@code minCFLWarn}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur de {@code minCFLWarn}
   */
  public static double readMinCFLWarn(PropertiesManager manager) {
    return (double) WPropertyKey.SIMULATION_CFL_MINWARNING.read(manager);
  }

  /**
   * Methode permettant de lire la valeur de {@code minCFLError}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur de {@code minCFLError}
   */
  public static double readMinCFLError(PropertiesManager manager) {
    return (double) WPropertyKey.SIMULATION_CFL_MINERROR.read(manager);
  }

  /**
   * Methode permettant de lire la valeur de {@code cflCheckFrequency}
   *
   * @param manager Le {@code PropertiesManager} avec lequel lire
   * @return La valeur de {@code cflCheckFrequency}
   */
  public static int readCFLCheckFrequency(PropertiesManager manager) {
    return (int) WPropertyKey.SIMULATION_CFL_FREQUENCY.read(manager);
  }

  /** {@inheritDoc} */
  @Override
  public void readProperties() {
    sleepTime = readSleepTime();
    timeStep = readTimeStep();

    viscosity = readViscosity();
    initialVelocity = readInitialVelocity();
    volumeDensity = readVolumeDensity();
    vortexConfinementFactor = readVortexConfinementFactor();

    fluid = readFluid();
    borderType = readBorderType();

    xLength = readXLength();
    yLength = readYLength();

    physicalXLength = readPhysicalXLength();
    physicalYLength = readPhysicalYLength();

    multiThreaded = readMultiThreaded();
  }

  /** {@inheritDoc} */
  @Override
  public void writeProperties() {
    if (!getPropertiesManager().isEditable()) {
      // TODO custom exception
      SimulationProperties.LOGGER.info(
          LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.notEditable"), "");
      return;
    }

    writeBorderType();
    writeSleepTime();
    writeTimeStep();
    writeViscosity();
    writePhysicalLength();
    writeInitialVelocity();
    writeMultiThreaded();
    writeFluid();
    writeVolumeDensity();
    writeVortexConfinementFactor();
  }

  /** {@inheritDoc} */
  @Override
  public Properties writeStandardProperties() {
    Properties properties = new Properties();

    WPropertyKey.SIMULATION_BORDER_TYPE.write(this.borderType.ordinal(), properties);
    WPropertyKey.SIMULATION_FLUID_TYPE.write(this.fluid.ordinal(), properties);

    WPropertyKey.SIMULATION_SLEEP_TIME.write(this.sleepTime, properties);
    WPropertyKey.SIMULATION_TIME_STEP.write(this.timeStep, properties);
    WPropertyKey.SIMULATION_VISCOSITY.write(this.viscosity, properties);

    WPropertyKey.SIMULATION_PHYSICAL_SIZE.write(
        new double[] {this.physicalXLength, this.physicalYLength}, properties);

    WPropertyKey.SIMULATION_INITIAL_VELOCITY.write(this.initialVelocity, properties);
    WPropertyKey.SIMULATION_MULTITHREADED.write(this.multiThreaded, properties);

    WPropertyKey.SIMULATION_VOLUME_DENSITY.write(this.volumeDensity, properties);
    WPropertyKey.SIMULATION_VORTEX_CONFINEMENT.write(this.vortexConfinementFactor, properties);

    return properties;
  }

  /** {@inheritDoc} */
  @Override
  public void readStandardProperties(Properties properties) {
    this.borderType =
        BORDER_TYPE.getBorderType(
            (int) WPropertyKey.SIMULATION_BORDER_TYPE.read(properties, this.borderType.ordinal()));

    this.fluid =
        Fluid.getFluid(
            (int) WPropertyKey.SIMULATION_FLUID_TYPE.read(properties, this.fluid.ordinal()));

    this.sleepTime = (double) WPropertyKey.SIMULATION_SLEEP_TIME.read(properties, this.sleepTime);
    this.timeStep = (double) WPropertyKey.SIMULATION_TIME_STEP.read(properties, this.timeStep);
    this.viscosity = (double) WPropertyKey.SIMULATION_VISCOSITY.read(properties, this.viscosity);
    this.initialVelocity =
        (double) WPropertyKey.SIMULATION_INITIAL_VELOCITY.read(properties, this.initialVelocity);
    this.multiThreaded =
        (boolean) WPropertyKey.SIMULATION_MULTITHREADED.read(properties, this.multiThreaded);
    this.volumeDensity =
        (double) WPropertyKey.SIMULATION_VOLUME_DENSITY.read(properties, this.volumeDensity);
    this.vortexConfinementFactor =
        (double)
            WPropertyKey.SIMULATION_VORTEX_CONFINEMENT.read(
                properties, this.vortexConfinementFactor);
  }
}
