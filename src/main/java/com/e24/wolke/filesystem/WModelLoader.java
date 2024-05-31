package com.e24.wolke.filesystem;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventMember;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code WModelLoader} permet de gerer le chargement d'un {@code WModel}
 *
 * @author MeriBouisri
 */
public class WModelLoader extends WEventMember {

  /** Le {@code Logger} de la classe */
  private static final Logger LOGGER =
      LogManager.getLogger(WModelLoader.class.getClass().getSimpleName());

  /** Le {@code Controller} de cette instance */
  private Controller controller;

  /**
   * Construction d'un {@code WModelLoader} avec un {@code Controller}
   *
   * @param controller Le {@code Controller} qui gere les modeles
   */
  public WModelLoader(Controller controller) {
    this.controller = controller;
  }

  /**
   * Charger les donnees actuelles de {@code ApplicationModel}. Le sujet {@code
   * Subject#ON_LOAD_APPLICATION_MODEL} est diffuse
   */
  public void loadApplicationModel() {
    WModelLoader.logStart("ApplicationModel");

    boolean success = controller.getApplicationModel().load();

    WModelLoader.logAfter(success);

    if (success) getPublisher().publish(Subject.ON_LOAD_APPLICATION_MODEL, null);
  }

  /**
   * Charger les donnees actuelles de {@code EditorModel}. Le sujet {@code
   * Subject#ON_LOAD_EDITOR_MODEL} est diffuse
   */
  public void loadEditorModel() {
    WModelLoader.logStart("EditorModel");
    getPublisher().publish(Subject.ON_LOAD_EDITOR_MODEL, null);
  }

  /**
   * Charger les donnees actuelles de {@code SimulationModel} Le sujet {@code
   * Subject#ON_LOAD_SIMULATION_MODEL} est diffuse
   */
  public void loadSimulationModel() {
    WModelLoader.logStart("SimulationModel");

    boolean success = controller.getSimulationModel().load();

    WModelLoader.logAfter(success);
    if (success) getPublisher().publish(Subject.ON_LOAD_SIMULATION_MODEL, null);
  }

  /**
   * Charger les donnees actuelles de {@code RendererModel} Le sujet {@code
   * Subject#ON_LOAD_RENDERER_MODEL} est diffuse
   */
  public void loadRendererModel() {
    WModelLoader.logStart("RendererModel");
    boolean success = controller.getRendererModel().load();
    WModelLoader.logAfter(success);
    if (success) getPublisher().publish(Subject.ON_LOAD_RENDERER_MODEL, null);
  }

  /**
   * Methode a invoquer pour log le message de debut de chargement
   *
   * @param name Le nom de l'objet a en cours de chargement
   */
  private static void logStart(String name) {
    WModelLoader.LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("modelLoader.start"), name);
  }

  /**
   * Methode a invoquer pour log le message apres la fin du chargement
   *
   * @param success {@code true} si le chargement a ete effectue avec succes, {@code false} si non
   */
  private static void logAfter(boolean success) {
    if (success) {
      WModelLoader.LOGGER.info(
          LocaleManager.getLocaleResourceBundle().getString("modelLoader.success"));
      return;
    }

    WModelLoader.LOGGER.error(
        LocaleManager.getLocaleResourceBundle().getString("modelLoader.error"));
  }
}
