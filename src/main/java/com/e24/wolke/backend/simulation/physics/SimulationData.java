package com.e24.wolke.backend.simulation.physics;

import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.obstacles.WObstacleCellMatrix;
import com.e24.wolke.backend.models.simulation.SimulationConstants;
import com.e24.wolke.backend.models.simulation.SimulationConstants.BORDER_TYPE;
import com.e24.wolke.backend.models.simulation.SimulationConstants.Fluid;
import com.e24.wolke.utils.interfaces.Reinitializable;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SimulationData.java
 *
 * <p>Cette classe est responsable de contenir les données de la simulation
 *
 * @author Nilon123456789
 * @author MeriBouisri
 */
public class SimulationData implements Reinitializable {

  /** Viscosité du fluide de la simulation dynamique (Pa*s) */
  private double dynamicViscosity;

  /** Masse volumique du fluide de la simulation (kg/m^3) */
  private double volumeDensity;

  /** Viscosité cinématique du fluide de la simulation (m^2/s) */
  private double kinematicViscosity;

  /** Vitesse du fluide si la bordure est en mode WIND_TUNNEL */
  private double initialVelocity;

  /** Matrice de la simulation actuelle */
  private ParticleMatrix currentParticleMatrix;

  /** Matrice de particule précédente */
  private ParticleMatrix previousParticleMatrix;

  /** Matrice de la simulation prête pour le rendu */
  private final ConcurrentLinkedQueue<ParticleMatrix> renderParticleMatrixQueue;

  /**
   * Matrice contenant les obstacles et bordure
   *
   * @see com.e24.wolke.backend.models.simulation.SimulationConstants.OBSTACLE_TYPE
   */
  private WObstacleCellMatrix obstacles;

  /**
   * Type de bordure de la matrice de particules
   *
   * @see com.e24.wolke.backend.models.simulation.SimulationConstants.BORDER_TYPE
   */
  private BORDER_TYPE borderType;

  /** Temps de repos entre chaque itération (en ms) */
  private double sleepTime;

  /** Pas de la simulation (en ms) */
  private double timeStep;

  /** Valeur du confinement de vortex */
  private double vortexConfinementFactor = SimulationConstants.DEFAULT_VORTEX_CONFINEMENT_FACTOR;

  /** Longueur de la simulation en X */
  private int xLength;

  /** Largeur de la simulation en Y */
  private int yLength;

  /** Longueur physique de la simulation en X */
  private double physicalXLength;

  /** Longueur physique de la simulation en Y */
  private double physicalYLength;

  /** Piscine de matrice de particules */
  private final ParticleMatrixPool particleMatrixPool;

  /** Logger de la classe */
  protected static final Logger LOGGER = LogManager.getLogger(SimulationData.class.getSimpleName());

  /**
   * Constructeur de la classe SimulationData
   *
   * @param xLength La taille en x de la matrice de particules (px)
   * @param yLength La taille en y de la matrice de particules (px)
   * @param physicalXLength La taille physique en x de la matrice de particules (m)
   * @param physicalYLength La taille physique en y de la matrice de particules (m)
   * @param viscosity La viscosité du fluide (Pa*s)
   * @param volumeDensity La masse volumique du fluide (kg/m^3)
   * @param initialVelocity La vitesse du fluide si la bordure est en mode WIND_TUNNEL (m/s)
   * @param timeStep Le pas de la simulation (en ms)
   * @param sleepTime Le temps de sleep entre chaque iteration (en ms)
   * @param borderType Le type de bordure de la matrice de particules
   */
  public SimulationData(
      int xLength,
      int yLength,
      double physicalXLength,
      double physicalYLength,
      double viscosity,
      double volumeDensity,
      double initialVelocity,
      double timeStep,
      double sleepTime,
      BORDER_TYPE borderType) {
    if (viscosity <= 0) throw new IllegalArgumentException("Viscosity must be greater than 0");
    if (volumeDensity <= 0)
      throw new IllegalArgumentException("Volume density must be greater than 0");

    this.xLength = xLength;
    this.yLength = yLength;

    particleMatrixPool = new ParticleMatrixPool(xLength, yLength, 2, 5);

    dynamicViscosity = viscosity;
    this.volumeDensity = volumeDensity;
    kinematicViscosity = dynamicViscosity / this.volumeDensity;
    this.initialVelocity = initialVelocity;
    currentParticleMatrix = new ParticleMatrix(xLength, yLength);
    previousParticleMatrix = new ParticleMatrix(xLength, yLength);
    renderParticleMatrixQueue = new ConcurrentLinkedQueue<ParticleMatrix>();
    this.timeStep = timeStep;
    this.sleepTime = sleepTime;
    this.physicalXLength = physicalXLength;
    this.physicalYLength = physicalYLength;
    this.borderType = borderType;

    obstacles = new WObstacleCellMatrix(xLength, yLength);
    obstacles.drawRectangleObstacle(xLength / 6, (yLength / 2) - 12, 4, 24);

    setFluidType(Fluid.AIR);

    obstacles.drawBorder(borderType);
  }

  /**
   * Constructeur de la classe SimulationData
   *
   * @param xLength La taille en x de la matrice de particules
   * @param yLength La taille en y de la matrice de particules
   * @param viscosity La viscosité du fluide
   * @param volumeDensity La masse volumique du fluide
   * @param timeStep Le pas de la simulation
   * @param sleepTime Le temps de sleep entre chaque iteration (en ms)
   */
  public SimulationData(
      int xLength,
      int yLength,
      double viscosity,
      double volumeDensity,
      double timeStep,
      double sleepTime) {
    this(
        xLength,
        yLength,
        SimulationConstants.DEFAULT_PHYSICAL_MATRIX_SIZE_X,
        SimulationConstants.DEFAULT_PHYSICAL_MATRIX_SIZE_Y,
        viscosity,
        volumeDensity,
        SimulationConstants.DEFAULT_INITIAL_VELOCITY,
        timeStep,
        sleepTime,
        SimulationConstants.DEFAULT_BORDER_TYPE);
  }

  /**
   * Constructeur de la classe SimulationData avec les valeurs par défaut
   *
   * @see SimulationConstants
   */
  public SimulationData() {
    this(
        SimulationConstants.DEFAULT_MATRIX_SIZE_X,
        SimulationConstants.DEFAULT_MATRIX_SIZE_Y,
        SimulationConstants.DEFAULT_PHYSICAL_MATRIX_SIZE_X,
        SimulationConstants.DEFAULT_PHYSICAL_MATRIX_SIZE_Y,
        SimulationConstants.DEFAULT_VISCOSITY,
        SimulationConstants.DEFAULT_VOLUME_DENSITY,
        SimulationConstants.DEFAULT_INITIAL_VELOCITY,
        SimulationConstants.DEFAULT_TIME_STEP,
        SimulationConstants.DEFAULT_SLEEP_TIME,
        SimulationConstants.DEFAULT_BORDER_TYPE);
  }

  // =================================================================================
  // ======              Fonctions publique pour tout le monde                  ======
  // =================================================================================

  /**
   * Retourne la longueur physique de la simulation en X
   *
   * @return La longueur physique de la simulation en X
   */
  public double getPhysicalXLength() {
    return this.physicalXLength;
  }

  /**
   * Modifie la longueur physique de la simulation en X
   *
   * @param physicalXLength La nouvelle longueur physique de la simulation en X
   */
  public void setPhysicalXLength(double physicalXLength) {
    this.physicalXLength = physicalXLength;
    this.physicalYLength = physicalXLength * this.yLength / this.xLength;

    SimulationData.LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.simulationData.physicalLengthSet"),
        physicalXLength,
        physicalYLength);
  }

  /**
   * Retourne la longueur physique de la simulation en Y
   *
   * @return La longueur physique de la simulation en Y
   */
  public double getPhysicalYLength() {
    return this.physicalYLength;
  }

  /**
   * Modifie la longueur physique de la simulation en Y
   *
   * @param physicalYLength La nouvelle longueur physique de la simulation en Y
   */
  public void setPhysicalYLength(double physicalYLength) {
    this.physicalYLength = physicalYLength;
    this.physicalXLength = physicalYLength * this.xLength / this.yLength;

    SimulationData.LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.simulationData.physicalLengthSet"),
        physicalXLength,
        physicalYLength);
  }

  /**
   * Retourne le nombre de metre par pixel en X
   *
   * @return Le nombre de metre par pixel en X
   */
  public double xMeterByPixel() {
    return this.physicalXLength / this.xLength;
  }

  /**
   * Retourne le nombre de metre par pixel en Y
   *
   * @return Le nombre de metre par pixel en Y
   */
  public double yMeterByPixel() {
    return this.physicalYLength / this.yLength;
  }

  /**
   * Retourne le nombre de pixel par metre en X
   *
   * @return Le nombre de pixel par metre en X
   */
  public double xPixelByMeter() {
    return this.xLength / this.physicalXLength;
  }

  /**
   * Retourne le nombre de pixel par metre en Y
   *
   * @return Le nombre de pixel par metre en Y
   */
  public double yPixelByMeter() {
    return this.yLength / this.physicalYLength;
  }

  /**
   * Modifie la viscosité du fluide et la masse volumique du fluide en fonction du type de fluide
   * donné
   *
   * @param fluid Le type de fluid
   * @see Fluid
   */
  public void setFluidType(Fluid fluid) {
    this.dynamicViscosity = fluid.getDynamicViscosity();
    this.volumeDensity = fluid.getVolumeDensity();
    this.kinematicViscosity = this.dynamicViscosity / this.volumeDensity;
    this.vortexConfinementFactor = fluid.getVortexConfinementFactor();
    SimulationData.LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.simulationData.fluidTypeSet"),
        fluid);
  }

  /**
   * Retourne le facteur de confinement de vortex
   *
   * @return Le facteur de confinement de vortex
   */
  public double getVortexConfinementFactor() {
    return vortexConfinementFactor;
  }

  /**
   * Modifie le facteur de confinement de vortex
   *
   * @param vortexConfinementFactor Le nouveau facteur de confinement de vortex
   */
  public void setVortexConfinementFactor(double vortexConfinementFactor) {
    if (vortexConfinementFactor < SimulationConstants.VORTEX_CONFINEMENT_MIN_FACTOR)
      throw new IllegalArgumentException(
          "Vortex confinement factor must be greater than "
              + SimulationConstants.VORTEX_CONFINEMENT_MIN_FACTOR);
    else if (vortexConfinementFactor > SimulationConstants.VORTEX_CONFINEMENT_MAX_FACTOR)
      throw new IllegalArgumentException(
          "Vortex confinement factor must be lower than "
              + SimulationConstants.VORTEX_CONFINEMENT_MAX_FACTOR);

    this.vortexConfinementFactor = vortexConfinementFactor;
  }

  /**
   * Retourne la viscosité dynamique du fluide (Pa*s)
   *
   * @return La viscosité dynamique du fluide (Pa*s)
   */
  public double getDynamicViscosity() {
    return this.dynamicViscosity;
  }

  /**
   * Modifie la viscosité dynamique du fluide
   *
   * @param newViscosity La nouvelle viscosité dynamique du fluide (Pa*s)
   */
  public void setDynamicViscosity(double newViscosity) {
    if (newViscosity <= 0) throw new IllegalArgumentException("Viscosity must be greater than 0");
    this.dynamicViscosity = newViscosity;

    this.kinematicViscosity = this.dynamicViscosity / this.volumeDensity;

    SimulationData.LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.simulationData.viscositySet"),
        newViscosity);
  }

  /**
   * Retourne la masse volumique du fluide (kg/m^3)
   *
   * @return La masse volumique du fluide (kg/m^3)
   */
  public double getVolumeDensity() {
    return this.volumeDensity;
  }

  /**
   * Modifie la masse volumique du fluide
   *
   * @param newVolumeDensity La nouvelle masse volumique du fluide (kg/m^3)
   */
  public void setVolumeDensity(double newVolumeDensity) {
    if (newVolumeDensity <= 0)
      throw new IllegalArgumentException("Volume density must be greater than 0");

    this.volumeDensity = newVolumeDensity;

    this.kinematicViscosity = this.dynamicViscosity / this.volumeDensity;

    SimulationData.LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.simulationData.volumeDensitySet"),
        newVolumeDensity);
  }

  /**
   * Retourne la viscosité cinématique du fluide (m^2/s)
   *
   * @return La viscosité cinématique du fluide (m^2/s)
   */
  public double getKinematicViscosity() {
    return this.kinematicViscosity;
  }

  /**
   * Retourne la vitesse initiale du fluide
   *
   * @return La vitesse initiale du fluide
   */
  public double getInitialVelocity() {
    return this.initialVelocity;
  }

  /**
   * Modifie la vitesse initiale du fluide
   *
   * @param initialVelocity La nouvelle vitesse initiale du fluide
   */
  public void setInitialVelocity(double initialVelocity) {
    if (initialVelocity < SimulationConstants.MIN_INITIAL_VELOCITY)
      throw new IllegalArgumentException(
          "Initial velocity must be greater than " + SimulationConstants.MIN_INITIAL_VELOCITY);
    else if (initialVelocity > SimulationConstants.MAX_INITIAL_VELOCITY)
      throw new IllegalArgumentException(
          "Initial velocity must be lower than " + SimulationConstants.MAX_INITIAL_VELOCITY);

    this.initialVelocity = initialVelocity;

    SimulationData.LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.simulationData.initialVelocitySet"),
        initialVelocity);
  }

  /**
   * Retourne le temps de sleep entre chaque iteration
   *
   * @return Le temps de sleep entre chaque iteration (en ms)
   */
  public double getSleepTime() {
    return this.sleepTime;
  }

  /**
   * Modifie le temps de sleep entre chaque iteration
   *
   * @param sleepTime Le nouveau temps de sleep entre chaque iteration (en ms)
   */
  public void setSleepTime(double sleepTime) {
    this.sleepTime = sleepTime;
  }

  /**
   * Retourne le pas de la simulation
   *
   * @return Le pas de la simulation (en ms)
   */
  public double getTimeStep() {
    return this.timeStep;
  }

  /**
   * Modifie le pas de la simulation
   *
   * @param timeStep Le nouveau pas de la simulation (en ms)
   * @throws IllegalArgumentException Si le pas de la simulation est négatif ou nul
   */
  public void setTimeStep(double timeStep) {
    if (timeStep <= 0) {
      LOGGER.error(
          LocaleManager.getLocaleResourceBundle().getString("log.simulationData.negativeTimeStep"),
          timeStep);
      throw new IllegalArgumentException(
          LocaleManager.getLocaleResourceBundle().getString("log.simulationData.negativeTimeStep"));
    }

    if (timeStep * this.initialVelocity > this.xMeterByPixel()) {
      LOGGER.warn(
          LocaleManager.getLocaleResourceBundle().getString("log.simulaitonData.largeTimeStep"),
          timeStep,
          this.xMeterByPixel() / this.initialVelocity);
    }

    this.timeStep = timeStep;

    SimulationData.LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.simulationData.timeStepSet"),
        timeStep);
  }

  /**
   * Retourne le type de bordure de la matrice de particules
   *
   * @return Le type de bordure de la matrice de particules
   */
  public BORDER_TYPE getBorderType() {
    return this.borderType;
  }

  /**
   * Modifie le type de bordure de la matrice de particules
   *
   * @param borderType Le nouveau type de bordure
   */
  public void setBorderType(BORDER_TYPE borderType) {
    this.borderType = borderType;
    this.obstacles.drawBorder(borderType);

    SimulationData.LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.simulationData.borderTypeSet"),
        borderType);
  }

  /**
   * Retourne la matrice de particule prête à être dessinée et la retire de la queue
   *
   * <p>Rendre la matrice récupérée une fois que le rendu est terminé avec {@link
   * #returnParticleMatrixToPool(ParticleMatrix)}
   *
   * @return La matrice de particule prête à être dessinée (null si aucune)
   */
  public ParticleMatrix pollRenderParticleMatrix() {
    return this.renderParticleMatrixQueue.poll();
  }

  /**
   * Retourne la matrice de particule prête à être dessinée sans la retirer de la queue
   *
   * @return La matrice de particule prête à être dessinée (null si aucune)
   */
  public ParticleMatrix peekRenderParticleMatrix() {
    return this.renderParticleMatrixQueue.peek();
  }

  /**
   * Retourne la longueur de la queue de matrice de particules prête à être dessinée
   *
   * @return La longueur de la queue de matrice de particules prête à être dessinée
   */
  public int getRenderParticleMatrixQueueSize() {
    return this.renderParticleMatrixQueue.size();
  }

  /**
   * Emprunte une matrice de particules à la piscine
   *
   * @return La matrice de particules empruntée
   */
  public ParticleMatrix borrowParticleMatrixFromPool() {
    return this.particleMatrixPool.borrowObject();
  }

  /**
   * Retourne la matrice de particules à la piscine
   *
   * @param matrix La matrice de particules à retourner
   */
  public void returnParticleMatrixToPool(ParticleMatrix matrix) {
    this.particleMatrixPool.returnObject(matrix);
  }

  /**
   * Retourne la matrice des obstacles
   *
   * @return Matrice des obstacles
   */
  public WObstacleCellMatrix getObstacle() {
    return this.obstacles;
  }

  /**
   * Retourne si la position est hors de la matrice
   *
   * @param pos La position
   * @return vrais si la position est hors de la matrice, faux sinon
   */
  public boolean isPosOutBoundary(int pos) {
    return pos < 0 || pos >= this.getCurrentParticleMatrix().getSize();
  }

  /**
   * Determine si une cellule est hors de la matrice ou s'il y a un obstacle
   *
   * @param pos La position de la cellule
   * @return vrai si la cellule est hors de la matrice, faux sinon
   */
  public boolean isCellObstructed(int pos) {
    if (isPosOutBoundary(pos)) return true;

    return this.getObstacle().getElementAt(pos) != null;
  }

  /**
   * Modifie la matrices des obstacles
   *
   * @param obstacles Nouvelle matrice des obstacles
   * @throws IllegalArgumentException Si la matrice des obstacles n'est pas de la même taille que la
   */
  public void setObstacle(WObstacleCellMatrix obstacles) {
    if (obstacles.getSize() != this.xLength * this.yLength)
      throw new IllegalArgumentException(
          "Obstacle matrix must be the same size as the particle matrix");
    this.obstacles = obstacles;
  }

  /**
   * Retourne la matrice de particules de la simulation
   *
   * @return La matrice de particules de la simulation courante
   */
  public ParticleMatrix getCurrentParticleMatrix() {
    return this.currentParticleMatrix;
  }

  /**
   * Modifie la matrice de particules active de la simulation
   *
   * @param newMatrix La nouvelle matrice de particules
   * @throws IllegalArgumentException Si la nouvelle matrice n'est pas de la même taille que la
   *     matrice actuelle
   */
  public void setCurrentParticleMatrix(ParticleMatrix newMatrix) {
    if (newMatrix.getXLength() != this.xLength || newMatrix.getYLength() != this.yLength)
      throw new IllegalArgumentException("New matrix must be the same size as the current matrix");
    this.currentParticleMatrix = newMatrix;
  }

  /**
   * Retourne la matrice de particules précédente
   *
   * @return La matrice de particules précédente
   */
  public ParticleMatrix getPreviousParticleMatrix() {
    return this.previousParticleMatrix;
  }

  /**
   * Modifie la matrice de particules précédente
   *
   * @param newMatrix La nouvelle matrice de particules précédente
   * @throws IllegalArgumentException Si la nouvelle matrice n'est pas de la même taille que la
   *     matrice actuelle
   */
  public void setPreviousParticleMatrix(ParticleMatrix newMatrix) {
    if (newMatrix.getXLength() != this.xLength || newMatrix.getYLength() != this.yLength)
      throw new IllegalArgumentException("New matrix must be the same size as the current matrix");
    this.previousParticleMatrix = newMatrix;
  }

  /** Ajoute la matrice de particule a la queue pour être dessinée */
  public void setNewRenderMatrix() {
    this.renderParticleMatrixQueue.add(this.previousParticleMatrix);

    if (this.renderParticleMatrixQueue.size()
        > SimulationConstants.MAX_PARTICLE_MATRIX_QUEUE_SIZE) {
      LOGGER.trace(
          LocaleManager.getLocaleResourceBundle()
              .getString("log.simulationData.renderQueueTooLong"),
          this.renderParticleMatrixQueue.size());
      returnParticleMatrixToPool(this.renderParticleMatrixQueue.poll());
    }

    this.setPreviousParticleMatrix(this.currentParticleMatrix);
  }

  /** Reinisialise la matrice de particules courante */
  public void resetParticleMatrix() {
    this.currentParticleMatrix = new ParticleMatrix(this.xLength, this.yLength);
    this.previousParticleMatrix = new ParticleMatrix(this.xLength, this.yLength);
  }

  /** Reinitialise la matrice d'obstacles courante */
  public void resetObstacleMatrix() {
    this.obstacles = null;
    this.obstacles = new WObstacleCellMatrix(xLength, yLength);
    this.obstacles.drawRectangleObstacle(xLength / 6, (yLength / 2) - 12, 4, 24);
    this.obstacles.drawWindTunnelBorders();
    this.obstacles.generateNormalMatrix();
    this.obstacles.generateAverageNormalMatrix();
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    this.xLength = SimulationConstants.DEFAULT_MATRIX_SIZE_X;
    this.yLength = SimulationConstants.DEFAULT_MATRIX_SIZE_Y;
    this.physicalXLength = SimulationConstants.DEFAULT_PHYSICAL_MATRIX_SIZE_X;
    this.physicalYLength = SimulationConstants.DEFAULT_PHYSICAL_MATRIX_SIZE_Y;
    this.dynamicViscosity = SimulationConstants.DEFAULT_VISCOSITY;
    this.volumeDensity = SimulationConstants.DEFAULT_VOLUME_DENSITY;
    this.timeStep = SimulationConstants.DEFAULT_TIME_STEP;
    this.sleepTime = SimulationConstants.DEFAULT_SLEEP_TIME;
    this.borderType = SimulationConstants.DEFAULT_BORDER_TYPE;
    this.initialVelocity = SimulationConstants.DEFAULT_INITIAL_VELOCITY;
    this.vortexConfinementFactor = SimulationConstants.DEFAULT_VORTEX_CONFINEMENT_FACTOR;

    this.kinematicViscosity = this.dynamicViscosity / this.volumeDensity;

    this.resetParticleMatrix();
    this.resetObstacleMatrix();

    LOGGER.info(LocaleManager.getLocaleResourceBundle().getString("log.simulationData.reseted"));
  }
}
