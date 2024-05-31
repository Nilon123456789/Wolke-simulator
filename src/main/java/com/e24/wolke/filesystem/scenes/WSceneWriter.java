package com.e24.wolke.filesystem.scenes;

import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.utils.files.WFileUtils;
import com.e24.wolke.utils.files.WPropertiesUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code WSceneWriter} permet d'ecrire les donnees a un fichier .wlks
 *
 * @author MeriBouisri
 */
public class WSceneWriter {

  /** Le {@code Logger} de cette instance */
  private static Logger logger = LogManager.getLogger(WSceneWriter.class.getSimpleName());

  /**
   * Methode permettant d'ecrire les donnees de scene dans un fichier
   *
   * @param scene La {@code WScene} dont les donnees seront ecrites dans le fichier
   * @param parentFolder Le {@code File} representant le dossier dans lequel les donnees de scene
   *     sera ecrite
   * @return Le {@code File} du fichier wlks
   * @throws IOException En cas d'erreur quelconque
   */
  public static File write(WScene scene, File parentFolder) throws IOException {
    WSceneWriter.logger.info(
        LocaleManager.getLocaleResourceBundle().getString("scene.writer.during"), scene.getName());

    File sceneFolder = WSceneWriter.createSceneFolder(parentFolder, scene.getName());

    if (sceneFolder == null) {
      WSceneWriter.logger.info(
          LocaleManager.getLocaleResourceBundle().getString("scene.writer.fail"), scene.getName());
      throw new IOException();
    }

    File editorImageFile = WSceneWriter.exportEditorImage(sceneFolder, scene.getEditorImage());
    File simulationPropertiesFile =
        WSceneWriter.exportSimulationProperties(sceneFolder, scene.getSimulationProperties());
    File rendererPropertiesFile =
        WSceneWriter.exportRendererProperties(sceneFolder, scene.getRendererProperties());

    if (editorImageFile == null
        || simulationPropertiesFile == null
        || rendererPropertiesFile == null) {
      WSceneWriter.logger.error(
          LocaleManager.getLocaleResourceBundle().getString("scene.writer.fail"), scene.getName());
      throw new IOException();
    }

    File[] files = {editorImageFile, simulationPropertiesFile, rendererPropertiesFile};

    File zip = WSceneWriter.zipFiles(parentFolder, files, scene.getName());

    if (zip == null) {
      WSceneWriter.logger.error(
          LocaleManager.getLocaleResourceBundle().getString("scene.writer.fail"), "NULL");
      return null;
    }

    for (File file : files) file.delete();

    sceneFolder.delete();

    WSceneWriter.logger.info(
        LocaleManager.getLocaleResourceBundle().getString("scene.writer.success"), scene.getName());

    return zip;
  }

  /**
   * @param parentFolder Le dossier dans lequel creer le fichier zip
   * @param files Le tableau de {@code File[]} a mettre dans le fichier zip
   * @param basename Le nom de base du fichier zip
   * @return Le {@code File} du fichier compresse
   */
  private static File zipFiles(File parentFolder, File[] files, String basename) {
    String zipname = basename + "." + WSceneConstants.WOLKE_EXTENSION;

    File zipFile = new File(parentFolder, zipname);

    FileOutputStream fout = null;
    ZipOutputStream zout = null;
    try {
      fout = new FileOutputStream(zipFile);
      zout = new ZipOutputStream(fout);
      for (File file : files) {
        ZipEntry entry = new ZipEntry(file.getName());
        zout.putNextEntry(entry);
        FileInputStream fis = new FileInputStream(file);

        byte[] bytes = new byte[1024];
        int length;

        while ((length = fis.read(bytes)) >= 0) {
          zout.write(bytes, 0, length);
        }
        fis.close();
      }

      zout.close();
      fout.close();
    } catch (IOException e) {
      WSceneWriter.logger.error(e.getMessage());
      return null;
    }
    return zipFile;
  }

  /**
   * Methode permettant de creer le {@code File} qui represente le dossier de scene
   *
   * @param parentFolder Le {@code File} dans lequel le dossier sera cree
   * @param sceneName Le nom de la scene
   * @return Le {@code File} qui represente le dossier qui contient les fichiers de scene
   */
  private static File createSceneFolder(File parentFolder, String sceneName) {
    if (!parentFolder.isDirectory()) {
      WSceneWriter.logger.error("Not a directory : " + parentFolder.getPath());
      return null;
    }

    File sceneFolder = new File(parentFolder, sceneName);

    if (sceneFolder.mkdir()) return sceneFolder;

    WSceneWriter.logger.error("Failed to create folder : " + sceneFolder.getPath());
    return null;
  }

  /**
   * Methode permettant d'ecrire les proprietes du {@code SimulationProperties} a un fichier dans
   * {@code directoryPath}
   *
   * @param folder Le {@code File} qui defini le dossier dans lequel le fichier de proprietes sera
   *     ecrit
   * @param properties Le {@code Properties} a ecrire dans le dossier
   * @return Le fichier qui contient les proprietes de simulation
   */
  private static File exportSimulationProperties(File folder, Properties properties) {
    return WSceneWriter.exportProperties(
        folder, WSceneConstants.SIMULATION_PROPERTIES_FILENAME, properties);
  }

  /**
   * Methode permettant d'ecrire les proprietes du {@code RendererProperties} a un fichier dans
   * {@code directoryPath}
   *
   * @param folder Le {@code File} qui defini le dossier dans lequel le fichier de proprietes sera
   *     ecrit
   * @param properties Le {@code Properties} a ecrire dans le dossier
   * @return Le fichier qui contient les proprietes de simulation
   */
  private static File exportRendererProperties(File folder, Properties properties) {
    return WSceneWriter.exportProperties(
        folder, WSceneConstants.RENDERER_PROPERTIES_FILENAME, properties);
  }

  /**
   * Methode
   *
   * @param folder Le {@code File} qui defini le dossier dans lequel le fichier d'image sera ecrit
   * @param image L'image a exporter
   * @return Le {@code File} qui contient l'image exportee
   */
  private static File exportEditorImage(File folder, BufferedImage image) {
    return WSceneWriter.exportImage(folder, WSceneConstants.IMAGE_FILENAME, image);
  }

  /**
   * Methode permettant d'ecrire les proprietes a un fichier
   *
   * @param folder Le {@code File} qui defini le dossier dans lequel le fichier de proprietes sera
   *     ecrit
   * @param filename Le nom du fichier
   * @param properties Le {@code Properties} a ecrire dans le dossier
   * @return Le fichier qui contient les proprietes
   */
  private static File exportProperties(File folder, String filename, Properties properties) {
    WSceneWriter.logger.debug(
        LocaleManager.getLocaleResourceBundle().getString("scene.writer.during"), filename);

    File file = WPropertiesUtils.exportProperties(folder, filename, properties);

    if (file == null) {
      WSceneWriter.logger.error(
          LocaleManager.getLocaleResourceBundle().getString("scene.writer.fail"), filename);
      return null;
    }

    if (!file.exists()) {
      WSceneWriter.logger.error(
          LocaleManager.getLocaleResourceBundle().getString("scene.writer.fail"), filename);
      return null;
    }

    WSceneWriter.logger.info(
        LocaleManager.getLocaleResourceBundle().getString("scene.writer.success"), filename);

    return file;
  }

  /**
   * Methode permettant d'ecrire les proprietes a un fichier
   *
   * @param folder Le {@code File} qui defini le dossier dans lequel le fichier de proprietes sera
   *     ecrit
   * @param filename Le nom du fichier
   * @param image L'image a exporter
   * @return Le fichier qui contient les proprietes
   */
  private static File exportImage(File folder, String filename, BufferedImage image) {
    WSceneWriter.logger.debug(
        LocaleManager.getLocaleResourceBundle().getString("scene.writer.during"), filename);

    File file = null;

    try {
      file = WFileUtils.exportImageFile(folder, filename, "png", image);
    } catch (IOException e) {
      WSceneWriter.logger.error(
          LocaleManager.getLocaleResourceBundle().getString("scene.writer.fail"), filename);
      WSceneWriter.logger.error(e.getMessage());
      return null;
    }

    if (file == null) {
      WSceneWriter.logger.error(
          LocaleManager.getLocaleResourceBundle().getString("scene.writer.fail"), filename);
      return null;
    }

    if (!file.exists()) {
      WSceneWriter.logger.error(
          LocaleManager.getLocaleResourceBundle().getString("scene.writer.fail"), filename);
      return null;
    }

    WSceneWriter.logger.info(
        LocaleManager.getLocaleResourceBundle().getString("scene.writer.success"), filename);

    return file;
  }
}
