package com.e24.wolke.filesystem.scenes;

import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.utils.files.WFileUtils;
import com.e24.wolke.utils.files.WPropertiesUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code WSceneReader} permet de lire les donnees d'un fichier .wlks
 *
 * @author MeriBouisri
 */
public class WSceneReader {

  /** Le {@code Logger} de cette instance */
  private static Logger logger = LogManager.getLogger(WSceneReader.class.getSimpleName());

  /**
   * Methode permettant de lire les donnees de scene a partir d'un fichier
   *
   * @param file Le {@code File} de format {@code Wolke} qui contient les donnees de la scene
   * @return Le {@code WScene} qui contient les donnees lues, ou null si la lecture a echoue
   */
  public static WScene read(File file) {
    if (!file.exists()) {
      WSceneReader.logger.error(
          LocaleManager.getLocaleResourceBundle().getString("userFileManager.fileNotFound"),
          file.getName());
      return null;
    }

    if (!WFileUtils.isWolkeFile(file)) {
      WSceneReader.logger.error(
          LocaleManager.getLocaleResourceBundle().getString("scene.incorrect_format")
              + " : "
              + WFileUtils.getExtension(file.getName()));
      return null;
    }

    WScene scene = new WScene(WFileUtils.getBasename(file.getName()));

    try {
      File dir = new File(file.getParentFile(), WFileUtils.getBasename(file.getName()));

      WFileUtils.unzipToDirectory(file.getPath(), dir.getPath());

      WSceneReader.readSceneFilesFromDirectory(dir, scene);

      for (File f : dir.listFiles()) f.delete();

      dir.delete();

    } catch (IOException e) {
      e.printStackTrace();
      WSceneReader.logger.error(
          LocaleManager.getLocaleResourceBundle().getString("scene.reader.fail"), file.getPath());
      return null;
    }

    WSceneReader.logger.info(
        LocaleManager.getLocaleResourceBundle().getString("scene.reader.success"), file.getPath());

    return scene;
  }

  /**
   * Methode permettant de lire les donnees de scene dans un dossier
   *
   * @param dir Dossier qui contient tous les fichier
   * @param scene Le {@code WScene} a modifier (avec side-effects)
   * @return {@code WScene} modifie, {@code null} en cas d'erreur
   * @throws IOException IOException
   */
  private static WScene readSceneFilesFromDirectory(File dir, WScene scene) throws IOException {
    if (!dir.isDirectory()) return null;

    if (scene == null) scene = new WScene();

    File imageFile = new File(dir, WSceneConstants.IMAGE_FILENAME);
    File simulationPropertiesFile = new File(dir, WSceneConstants.SIMULATION_PROPERTIES_FILENAME);
    File rendererPropertiesFile = new File(dir, WSceneConstants.RENDERER_PROPERTIES_FILENAME);

    File[] files = {imageFile, simulationPropertiesFile, rendererPropertiesFile};

    for (File file : files)
      if (!file.exists()) throw new IOException("File does not exist : " + file.getName());

    BufferedImage image = WSceneReader.readEditorImageFile(imageFile);
    Properties simulationProperties =
        WSceneReader.readSimulationPropertiesFile(simulationPropertiesFile);
    Properties rendererProperties = WSceneReader.readRendererPropertiesFile(rendererPropertiesFile);

    scene.setEditorImage(image);
    scene.setSimulationProperties(simulationProperties);
    scene.setRendererProperties(rendererProperties);

    return scene;
  }

  /**
   * Lecture de EditorImageFile
   *
   * @param file File
   * @return {@code BufferedImage}
   * @throws IOException IOException
   */
  private static BufferedImage readEditorImageFile(File file) throws IOException {
    if (!file.exists()) throw new FileNotFoundException(file.getPath());

    return ImageIO.read(file);
  }

  /**
   * Lecture de SimulationProperties
   *
   * @param file File
   * @return {@code Properties}
   * @throws IOException IOException
   */
  private static Properties readSimulationPropertiesFile(File file) throws IOException {
    if (!file.exists()) throw new FileNotFoundException(file.getPath());

    return WPropertiesUtils.importProperties(file);
  }

  /**
   * Lecture de RendererPropetties
   *
   * @param file File
   * @return {@code Properties}
   * @throws IOException IOException
   */
  private static Properties readRendererPropertiesFile(File file) throws IOException {
    if (!file.exists()) throw new FileNotFoundException(file.getPath());

    return WPropertiesUtils.importProperties(file);
  }
}
