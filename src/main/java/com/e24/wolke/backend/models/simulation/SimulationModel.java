package com.e24.wolke.backend.models.simulation;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.WModelSaveable;
import com.e24.wolke.backend.models.simulation.SimulationConstants.BORDER_TYPE;
import com.e24.wolke.backend.models.simulation.SimulationConstants.Fluid;
import com.e24.wolke.backend.models.simulation.SimulationConstants.FluidState;
import com.e24.wolke.backend.simulation.physics.SimulationData;
import com.e24.wolke.backend.simulation.physics.engine.PhysicsEngine;
import com.e24.wolke.backend.simulation.physics.engine.PhysicsEngineMultiThreaded;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.filesystem.scenes.WSceneMember;
import java.util.Properties;

/**
 * La classe {@code SimulationModel} sert a gerer la logique de la simulation de fluides et de
 * l'exposer au {@code Controller}.
 *
 * @author MeriBouisri
 * @author Nilon123456789
 * @author adrienles
 */
public class SimulationModel extends WModelSaveable implements WSceneMember<Properties> {

  /** Le {@code PhysicsEngine} de la simulation */
  private PhysicsEngine physicsEngine;

  /** Le {@code SimulationData} de la simulation */
  private SimulationData simulationData;

  /** Le {@code FluidType} par defaut */
  private FluidState currentFluidState = SimulationConstants.DEFAULT_FLUID_STATE;

  /** Les {@code WProperties} de cette instance */
  private SimulationProperties properties;

  /**
   * Construction d'un {@code SimulationModel} avec un {@code Controller}. La methode {@code
   * this#initialize()} est appelee.
   *
   * @param controller Le {@code Controller} qui gere cette instance.
   */
  public SimulationModel(Controller controller) {
    super(controller);
    setupKeybindSubscriptions();

    properties = new SimulationProperties();
    setProperties(properties);

    initialize();
  }

  /** {@inheritDoc} */
  @Override
  protected boolean setup() {

    simulationData =
        new SimulationData(
            properties.xLength,
            properties.yLength,
            properties.physicalXLength,
            properties.physicalYLength,
            properties.viscosity,
            properties.volumeDensity,
            properties.initialVelocity,
            properties.timeStep,
            properties.sleepTime,
            properties.borderType);

    physicsEngine =
        properties.multiThreaded ? new PhysicsEngineMultiThreaded(this) : new PhysicsEngine(this);

    return true;
  }

  // ==================
  // SIMULATION GETTERS
  // ==================

  /**
   * Getter pour le {@code SimulationData} de cette instance
   *
   * @return Le {@code SimulationData} de cette instance
   */
  public SimulationData getSimulationData() {
    return simulationData;
  }

  /**
   * Getter pour la valeur de la viscosite du {@code SimulationData} de cette instance
   *
   * @return La valeur de la viscosite du {@code SimulationData}
   */
  public double getViscosity() {
    return simulationData.getDynamicViscosity();
  }

  /**
   * Getter pour la valeur de la densite volumique du {@code SimulationData} de cette instance
   *
   * @return La valeur de la densite volumique du {@code SimulationData}
   */
  public double getVolumeDensity() {
    return simulationData.getVolumeDensity();
  }

  /**
   * Getter pour la valeur de la temperature
   *
   * @return La temperature (en degres)
   */
  public double getTemperatureDegrees() {
    // TODO
    return 0;
  }

  /**
   * Getter pour la valeur de la vitesse de sortie
   *
   * @return La vitesse de sortie
   */
  public double getOutputSpeed() {
    return simulationData.getInitialVelocity();
  }

  /**
   * Getter pour la valeur de la pression initiale
   *
   * @return La pression initiale
   */
  public double getInitialPressure() {
    // TODO
    return 0;
  }

  /**
   * Retourne le {@code FluidState} actuel
   *
   * @return Le {@code FluidState} actuel
   */
  public FluidState getCurrentFluidState() {
    return currentFluidState;
  }

  /**
   * Retourne le temps actuel de la simulation
   *
   * @return Le temps actuel de la simulation
   */
  public double getCurrentTime() {
    return physicsEngine.getCurrentTime();
  }

  /**
   * Retourne le facteur de confinement des vortex actuel
   *
   * @return Le facteur de confinement des vortex actuel
   */
  public double getVortexConfinementFactor() {
    return simulationData.getVortexConfinementFactor();
  }

  /**
   * Retourne le pas de temps de la simulation
   *
   * @return Le pas de temps de la simulation
   */
  public double getTimeStep() {
    return simulationData.getTimeStep();
  }

  /**
   * Retourne la taille physique de la matrice en x
   *
   * @return La taille de la matrice en x
   */
  public double getPhysicalXSize() {
    return simulationData.getPhysicalXLength();
  }

  /**
   * Retourne la taille phyisique de la matrice en y
   *
   * @return La taille de la matrice en y
   */
  public double getPhysicalYSize() {
    return simulationData.getPhysicalYLength();
  }

  /**
   * Setter pour la resolution de la simulation
   *
   * @param xLength La longueur en x
   * @param yLength La longueur en y
   */
  public void setResolution(int xLength, int yLength) {
    properties.xLength = xLength;
    properties.yLength = yLength;
  }

  /**
   * Retourne le {@code BORDER_TYPE} actuel
   *
   * @return Le {@code BORDER_TYPE} actuel
   */
  public BORDER_TYPE getBorderType() {
    return simulationData.getBorderType();
  }

  // ==================
  // SIMULATION SETTERS
  // ==================

  /**
   * Setter pour la valeur de {@code viscosity} du {@code SimulationData} de cette instance.
   *
   * @param viscosity Nouvelle valeur de la viscosite
   */
  public void setViscosity(double viscosity) {
    properties.viscosity = viscosity;
    simulationData.setDynamicViscosity(properties.viscosity);
  }

  /**
   * Setter pour la temperature, en degres.
   *
   * @param tempDegrees nouvelle temperature, en degres
   */
  public void setTemperature(double tempDegrees) {
    // TODO
  }

  /**
   * Setter pour le {@code volumeDensity} du {@code SimulationData} de cette instance
   *
   * @param volumeDensity Nouvelle valeur du {@code volumeDensity}
   */
  public void setVolumeDensity(double volumeDensity) {
    properties.volumeDensity = volumeDensity;
    simulationData.setVolumeDensity(properties.volumeDensity);
  }

  /**
   * Setter pour le {@code outputSpeed}
   *
   * @param outputSpeed nouvelle valeur du {@code outputSpeed}
   */
  public void setOutputSpeed(double outputSpeed) {
    properties.initialVelocity = outputSpeed;
    simulationData.setInitialVelocity(properties.initialVelocity);
  }

  /**
   * Setter pour la pression initiale
   *
   * @param initialPressure La pression initiale
   */
  public void setInitialPressure(double initialPressure) {
    // TODO
  }

  /**
   * Setter pour le facteur de confinement des vortex
   *
   * @param vortexConfinementFactor Le facteur de confinement des vortex
   */
  public void setVortexConfinementFactor(double vortexConfinementFactor) {
    properties.vortexConfinementFactor = vortexConfinementFactor;
    simulationData.setVortexConfinementFactor(properties.vortexConfinementFactor);
  }

  /**
   * Setter pour le pas de temps de la simulation
   *
   * @param timeStep Le pas de temps de la simulation
   */
  public void setTimeStep(double timeStep) {
    properties.timeStep = timeStep;
    simulationData.setTimeStep(properties.timeStep);
  }

  /**
   * Setter pour le {@code BORDER_TYPE} actuel
   *
   * @param borderType Le nouveau {@code BORDER_TYPE} actuel
   */
  public void setBorderType(BORDER_TYPE borderType) {
    properties.borderType = borderType;
    simulationData.setBorderType(properties.borderType);
  }

  /**
   * Setter pour le {@code FLUID} actuel
   *
   * @param fluid Le {@code FLUID} actuel
   */
  public void setCurrentFluid(Fluid fluid) {
    properties.fluid = fluid;
    simulationData.setFluidType(properties.fluid);
  }

  /**
   * Setter pour le {@code FluidType} actuel
   *
   * @param fluidType Le nouveau {@code FluidType} actuel
   */
  public void setCurrentFluidState(FluidState fluidType) {
    currentFluidState = fluidType;
  }

  /**
   * Setter pour la taille de la matrice en x
   *
   * @param xSize La taille de la matrice en x
   */
  public void setPhysicalXSize(double xSize) {
    simulationData.setPhysicalXLength(xSize);
  }

  /**
   * Setter pour la taille de la matrice en y
   *
   * @param ySize La taille de la matrice en y
   */
  public void setPhysicalYSize(double ySize) {
    simulationData.setPhysicalYLength(ySize);
  }

  // =========================
  // SIMULATION RENDER METHODS
  // =========================

  /** Methode a invoquer lors du lancement de la simulation. */
  public void onStartSimulation() {
    getPublisher().publish(Subject.ON_SIMULATION_STATE_CHANGED, true);
    physicsEngine.start();

    this.notifyObstacleModel();
  }

  /** Methode a invoquer lors de l'arret de la simulation. */
  public void onStopSimulation() {
    getPublisher().publish(Subject.ON_SIMULATION_STATE_CHANGED, false);
    physicsEngine.stop();
  }

  /**
   * Methode a invoquer pour lancer la simulation si elle n'est pas deja lancee, ou arreter la
   * simulation si elle n'est pas arretee.
   */
  public void onToggleSimulation() {
    if (physicsEngine.isRunning()) {
      onStopSimulation();
      return;
    }

    onStartSimulation();
  }

  /** Methode a invoquer lors de l'incrementation du pas de la simulation. */
  public void onNextStepSimulation() {
    if (physicsEngine.isRunning()) {
      onStopSimulation();
    } else {
      physicsEngine.update(simulationData.getTimeStep());
    }
  }

  /** Methode a invoquer lors du recommencement de la simulation, avec les parametres actuels. */
  public void onRestartSimulation() {
    getPublisher().publish(Subject.ON_SIMULATION_RESTARTED, null);
    physicsEngine.reinitialize();
    getController().getRendererModel().handleCurrentFrame();
  }

  /** Methode a invoquer lorsque le {@code ObstacleModel} doit recalculer les obstacles */
  private void notifyObstacleModel() {
    this.getController().getObstacleModel().onNewObstacles();
  }

  /**
   * Retourne si la simulation est en cours d'execution.
   *
   * @return true si la simulation est en cours d'execution, false sinon
   */
  public boolean isRunning() {
    return physicsEngine.isRunning();
  }

  /**
   * Retourne le nombre de courant de la simulation
   *
   * @return Le nombre de courant de la simulation
   * @see <a href="https://www.simscale.com/blog/cfl-condition/">CFL condition</a>
   */
  public double getCFLNumber() {
    return physicsEngine.calculateCFL();
  }

  /**
   * Methode utilitaire permettant de set toutes les proprietes d'un {@code SimulationData} a partir
   * de {@code SimulationProperties}
   *
   * @param data Le {@code SimulationData} a modifier
   * @param properties Les proprietes associees a la simulation
   */
  private static void setSimulationDataProperties(
      SimulationData data, SimulationProperties properties) {
    data.setDynamicViscosity(properties.viscosity);
    data.setBorderType(properties.borderType);
    data.setVolumeDensity(properties.volumeDensity);
    data.setInitialVelocity(properties.initialVelocity);
    data.setFluidType(properties.fluid);
    data.setSleepTime(properties.sleepTime);
    data.setTimeStep(properties.timeStep);
  }

  /** {@inheritDoc} */
  public void setupKeybindSubscriptions() {
    getSubscriber()
        .subscribe(
            Subject.KEYBIND_SIMULATION_TOGGLE_PRESSED,
            e -> {
              onToggleSimulation();
            });
    getSubscriber()
        .subscribe(
            Subject.KEYBIND_SIMULATION_NEXT_FRAME_PRESSED,
            e -> {
              onNextStepSimulation();
            });
    getSubscriber()
        .subscribe(
            Subject.KEYBIND_SIMULATION_RESET_PRESSED,
            e -> {
              onRestartSimulation();
            });
  }

  /** {@inheritDoc} */
  @Override
  public void softReinitialize() {
    onStopSimulation();
    simulationData.reinitialize();
    setupKeybindSubscriptions();
    onRestartSimulation();
  }

  /** {@inheritDoc} */
  @Override
  protected void onPropertiesChanged() {
    boolean wasRunning = isRunning();

    onStopSimulation();
    SimulationModel.setSimulationDataProperties(simulationData, properties);
    onRestartSimulation();

    if (wasRunning) onStartSimulation();
  }

  /** {@inheritDoc} */
  @Override
  public Properties getSceneComponent() {
    return this.properties.writeStandardProperties();
  }

  /** {@inheritDoc} */
  @Override
  public boolean setSceneComponent(Properties sceneComponent) {
    this.onStopSimulation();
    this.properties.readStandardProperties(sceneComponent);
    this.onPropertiesChanged();
    return true;
  }
}
