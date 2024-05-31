package com.e24.wolke.filesystem;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.eventsystem.WEventMember;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code WModelSaver} permet de gerer la sauvegarde des {@code WModel}
 *
 * @author MeriBouisri
 */
public class WModelSaver extends WEventMember {

  /** Le {@code Logger} de la classe */
  private static final Logger LOGGER =
      LogManager.getLogger(WModelSaver.class.getClass().getSimpleName());

  /** Le {@code Controller} de cette instance */
  private Controller controller;

  /**
   * Construction d'un {@code WModelSaver} avec un {@code Controller}
   *
   * @param controller Le {@code Controller} qui gere les modeles
   */
  public WModelSaver(Controller controller) {
    this.controller = controller;
  }

  /** Sauvegarder les donnees actuelles de {@code ApplicationModel} */
  public void saveApplicationModel() {
    WModelSaver.logStart("ApplicationModel");

    boolean success = controller.getApplicationModel().save();

    WModelSaver.logAfter(success);
  }

  /** Sauvegarder les donnees actuelles de {@code EditorModel} */
  public void saveEditorModel() {
    WModelSaver.logStart("EditorModel");
  }

  /** Sauvegarder les donnees actuelles de {@code SimulationModel} */
  public void saveSimulationModel() {
    WModelSaver.logStart("SimulationModel");
    boolean success = controller.getSimulationModel().save();
    WModelSaver.logAfter(success);
  }

  /** Sauvegarder les donnees actuelles de {@code SimulationModel} */
  public void saveRendererModel() {
    WModelSaver.logStart("RendererModel");
    boolean success = controller.getRendererModel().save();
    WModelSaver.logAfter(success);
  }

  /**
   * Methode a invoquer pour log le message de debut de sauvegarde
   *
   * @param name Le nom de l'objet a en cours de sauvegarde
   */
  private static void logStart(String name) {
    WModelSaver.LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("modelSaver.start"), name);
  }

  /**
   * Methode a invoquer pour log le message apres la fin de la sauvegarde
   *
   * @param success {@code true} la sauvegarde a ete effectuee avec succes, {@code false} si non
   */
  private static void logAfter(boolean success) {
    if (success) {
      WModelSaver.LOGGER.info(
          LocaleManager.getLocaleResourceBundle().getString("modelSaver.success"));
      return;
    }

    WModelSaver.LOGGER.error(LocaleManager.getLocaleResourceBundle().getString("modelSaver.error"));
  }
}
