package com.e24.wolke.filesystem.scenes;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.renderer.RendererProperties;
import com.e24.wolke.backend.models.simulation.SimulationProperties;
import com.e24.wolke.eventsystem.Subject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code WSceneProcessor} permet de gerer les donnees d'une scene
 *
 * @author MeriBouisri
 */
public class WSceneHandler {

  /** Le {@code Logger} de cette instance */
  private static Logger logger = LogManager.getLogger(WSceneHandler.class.getSimpleName());

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /**
   * Construction d'un {@code WSceneProcessor} avec un {@code Controller}
   *
   * @param controller Le {@code Controller} de cette instance
   */
  public WSceneHandler(Controller controller) {
    this.controller = controller;
  }

  /**
   * @param file Le fichier {@code Wolke} qui contient les donnees de scene
   * @return {@code true} si l'import de la scene a ete effectue avec succes
   */
  public boolean importScene(File file) {
    WScene scene = WSceneReader.read(file);

    if (scene == null) return false;

    controller.getEditorModel().getLayerModel().getLayerList().clearAllLayers();
    controller.getEditorModel().setSceneComponent(scene.getEditorImage());
    controller.getSimulationModel().setSceneComponent(scene.getSimulationProperties());
    controller.getRendererModel().setSceneComponent(scene.getRendererProperties());
    controller.getApplicationModel().getPublisher().publish(Subject.ON_APP_SCENE_LOADED, null);

    return true;
  }

  /**
   * Methode permettant d'exporter la scene actuelle vers un fichier {@code Wolke}
   *
   * @param parentFolder Le {@code File} qui represente le dossier ou le fichier de scene sera cree
   * @param sceneName Le nom de la scene
   * @return {@code true} si l'export du fichier a ete effectue avec succes
   */
  public boolean exportScene(File parentFolder, String sceneName) {
    WScene scene =
        new WScene(
            sceneName,
            this.controller.getEditorModel().getSceneComponent(),
            this.controller.getSimulationModel().getSceneComponent(),
            this.controller.getRendererModel().getSceneComponent());

    boolean success = false;
    try {
      success = (WSceneWriter.write(scene, parentFolder) != null);

    } catch (IOException e) {
      WSceneHandler.logger.error(e.getMessage());
      return false;
    }

    return success;
  }

  /**
   * Methode permettant de creer une scene par defaut
   *
   * @return Un {@code WScene} par defaut
   */
  public WScene createDefaultScene() {
    Properties simProps = (new SimulationProperties()).writeStandardProperties();
    Properties renderProps = (new RendererProperties()).writeStandardProperties();
    BufferedImage image =
        controller.getEditorModel().getLayerModel().getLayerFactory().createBufferedImage();

    return new WScene("DefaultScene", image, simProps, renderProps);
  }
}
