package com.e24.wolke.backend.controller;

import com.e24.wolke.backend.keybinds.KeybindModel;
import com.e24.wolke.backend.models.WModel;
import com.e24.wolke.backend.models.application.ApplicationModel;
import com.e24.wolke.backend.models.console.ConsoleModel;
import com.e24.wolke.backend.models.editor.EditorModel;
import com.e24.wolke.backend.models.obstacles.ObstacleModel;
import com.e24.wolke.backend.models.renderer.RendererModel;
import com.e24.wolke.backend.models.simulation.SimulationModel;
import com.e24.wolke.eventsystem.EventBroker;
import com.e24.wolke.eventsystem.WEventMember;
import com.e24.wolke.filesystem.WModelLoader;
import com.e24.wolke.filesystem.WModelSaver;
import com.e24.wolke.filesystem.scenes.WSceneHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code Controller} permet de lier l'interface graphique aux modeles de l'application.
 *
 * @author MeriBouisri
 * @author adrienles
 */
public class Controller extends WEventMember {

  /** Le {@code Logger} de la classe */
  protected static final Logger LOGGER =
      LogManager.getLogger(Controller.class.getClass().getSimpleName());

  /** Le {@code SimulationModel} de l'application */
  private SimulationModel simulationModel;

  /** Le {@code ObstacleModel} de l'application */
  private ObstacleModel obstacleModel;

  /** Le {@code EditorModel} de l'application */
  private EditorModel editorModel;

  /** Le {@code ApplicationModel} de l'application */
  private ApplicationModel applicationModel;

  /** Le {@code RendererModel} de l'application */
  private RendererModel rendererModel;

  /** Le {@code ConsoleModel} de l'application */
  private ConsoleModel consoleModel;

  /** Le {@code KeybindModel} de l'application */
  private KeybindModel keybindModel;

  /** Liste des {@code WModel} de cette instance */
  private WModel[] models;

  /** Le {@code WModelSaver} de cette instance */
  private WModelSaver modelSaver;

  /** Le {@code WModelLoader} de cette instance */
  private WModelLoader modelLoader;

  /** Le {@code WSceneHandler} de cette instance */
  private WSceneHandler sceneHandler;

  /** Construction d'un {@code Controller}. Les instances de {@code WModel} sont initialisees. */
  public Controller() {
    super();

    modelSaver = new WModelSaver(this);
    modelLoader = new WModelLoader(this);

    applicationModel = new ApplicationModel(this);
    simulationModel = new SimulationModel(this);
    obstacleModel = new ObstacleModel(this);
    editorModel = new EditorModel(this);
    rendererModel = new RendererModel(this);
    consoleModel = new ConsoleModel(this);
    keybindModel = new KeybindModel(this);

    sceneHandler = new WSceneHandler(this);

    initializeArray();

    modelLoader.loadApplicationModel();
  }

  /**
   * Construction d'un {@code Controller} avec des {@code WModels} predefinis
   *
   * @param simulationModel Le {@code SimulationModel}
   * @param editorModel Le {@code EditorModel}
   * @param applicationModel {@code ApplicationModel}
   * @param rendererModel {@code RendererModel}
   * @param consoleModel {@code ConsoleModel}
   * @param obstacleModel {@code ObstacleModel}
   * @param keybindModel {@code KeybindModel}
   */
  public Controller(
      SimulationModel simulationModel,
      ObstacleModel obstacleModel,
      EditorModel editorModel,
      ApplicationModel applicationModel,
      RendererModel rendererModel,
      ConsoleModel consoleModel,
      KeybindModel keybindModel) {
    super();

    this.simulationModel = simulationModel;
    this.obstacleModel = obstacleModel;
    this.editorModel = editorModel;
    this.applicationModel = applicationModel;
    this.rendererModel = rendererModel;
    this.consoleModel = consoleModel;
    this.keybindModel = keybindModel;

    initializeArray();

    setControllerAll();
  }

  /** Methode utilitaire permettant d'initialiser le tableau de {@code WModel} de cette instance */
  private void initializeArray() {
    models =
        new WModel[] {
          simulationModel,
          obstacleModel,
          editorModel,
          applicationModel,
          rendererModel,
          consoleModel,
          keybindModel
        };
  }

  /** Methode permettant d'appeler {@code WModel#reinitialize()} sur tous les modeles. */
  private void reinitializeAll() {
    for (WModel model : models) model.reinitialize();
  }

  /** Methode permettant d'appeler {@code WModel#softReinitialize()} sur tous les modeles. */
  private void softReinitializeAll() {
    for (WModel model : models) model.softReinitialize();
  }

  /**
   * Methode permettant d'appeler {@code WModel#setController(Controller)} sur tous les modeles,
   * avec cette instance de {@code Controller} passee en parametre
   */
  private void setControllerAll() {
    for (WModel model : models) model.setController(this);
  }

  /**
   * Getter pour les {@code WModel} de l'application.
   *
   * @return Les {@code WModel} de l'application
   */
  public WModel[] getModels() {
    return models;
  }

  /**
   * Getter pour le {@code SimulationModel} de l'application.
   *
   * @return Le {@code SimulationModel} de l'application
   */
  public SimulationModel getSimulationModel() {
    if (!simulationModel.isInitialized()) simulationModel.initialize();

    return simulationModel;
  }

  /**
   * Getter pour le {@code EditorModel} de l'application.
   *
   * @return Le {@code EditorModel} de l'application
   */
  public EditorModel getEditorModel() {
    if (!editorModel.isInitialized()) editorModel.initialize();

    return editorModel;
  }

  /**
   * Getter pour le {@code ApplicationModel} de l'application.
   *
   * @return Le {@code ApplicationModel} de l'application
   */
  public ApplicationModel getApplicationModel() {
    if (!applicationModel.isInitialized()) applicationModel.initialize();

    return applicationModel;
  }

  /**
   * Getter pour le {@code RendererModel} de l'application.
   *
   * @return Le {@code RendererModel} de l'application
   */
  public RendererModel getRendererModel() {
    if (!rendererModel.isInitialized()) rendererModel.initialize();

    return rendererModel;
  }

  /**
   * Getter pour le {@code ConsoleModel} de l'application.
   *
   * @return Le {@code ConsoleModel} de l'application
   */
  public ConsoleModel getConsoleModel() {
    if (!consoleModel.isInitialized()) consoleModel.initialize();

    return consoleModel;
  }

  /**
   * Getter pour le {@code ObstacleModel} de l'application
   *
   * @return Le {@code ObstacleModel} de l'application
   */
  public ObstacleModel getObstacleModel() {
    if (!obstacleModel.isInitialized()) obstacleModel.initialize();

    return obstacleModel;
  }

  /**
   * Getter pour {@code this#modelSaver}, le gestionnaire de sauvegarde de cette instance.
   *
   * @return Le gestionnaire de sauvegarde de cette instance
   */
  public WModelSaver getModelSaver() {
    return modelSaver;
  }

  /**
   * Getter pour {@code this#modelLoader}, le gestionnaire de chargement de cette instance.
   *
   * @return Le gestionnaire de chargement de cette instance
   */
  public WModelLoader getModelLoader() {
    return modelLoader;
  }

  /**
   * Getter pour le {@code KeybindModel} de l'application
   *
   * @return Le {@code KeybindModel} de l'application
   */
  public KeybindModel getKeybindModel() {
    return keybindModel;
  }

  /**
   * Methode permettant de recuperer la resolution (longueur horizontale et verticale) du rendu de
   * la simulation.
   *
   * @return Un {@code int[2]} contenant la longueur horizontale et verticale {@code (xLength,
   *     yLength)} de la simulation
   */
  public int[] getSimulationResolution() {
    return simulationModel.getSimulationData().getCurrentParticleMatrix().getXandYLength();
  }

  /**
   * Getter pour le {@code WSceneHandler} de cette instance
   *
   * @return Le {@code WSceneHandler} de cette instance
   */
  public WSceneHandler getSceneHandler() {
    return this.sceneHandler;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Tous les modeles sont reinitialises. Le {@code EventBroker} est reinitialise.
   *
   * @see EventBroker#reinitialize()
   * @see WModel#reinitialize()
   */
  @Override
  public void reinitialize() {
    super.reinitialize();
    reinitializeAll();
  }

  /**
   * Methode permettant de reinitialiser les modeles sans reinitialiser leurs {@code EventBroker}.
   * La methode {@code WModel#softReinitialize()} de chaque modele est appelée
   *
   * @see EventBroker#reinitialize()
   * @see WModel#reinitialize()
   */
  public void softReinitialize() {
    softReinitializeAll();
  }
}
