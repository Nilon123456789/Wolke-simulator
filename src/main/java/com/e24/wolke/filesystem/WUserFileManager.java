package com.e24.wolke.filesystem;

import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.backend.models.application.LocaleManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code WUserFileManager} permet copier des fichier de resources dans le dossier de
 * l'utilisateur pour qu'il y ai accès en utilisant la méthode {@code copyResourcesToUserFolder()}
 *
 * <p>Exemple, s'il y a dans le dossier {@code resources} un dossier {@code sample-images} qui
 * contient des images. {@code copyResourcesToUserFolder()} va copier le dossier {@code
 * sample-images} dans le dossier de l'utilisateur.
 *
 * @author Nilon123456789
 * @see com.e24.wolke.filesystem.WFileSystemConstant Chemin du dossier utilisateur
 * @see com.e24.wolke.filesystem.WUserFileManager Liste des dossiers à copier
 */
public class WUserFileManager {

  /** Liste des dossier à copier du fichier resources au dossier utilisateur */
  private static final ResourcesEntry[] RESOURCES = {
    new ResourcesEntry(
        ApplicationConstants.SAMPLE_IMAGES_FOLDER_SUBDIR,
        new String[] {
          "airfoil-1.svg",
          "airfoil-2.svg",
          "airfoil-3.svg",
          "airfoil-4.svg",
          "airfoil-5.svg",
          "airfoil-6.svg",
          "airfoil-7.svg"
        }),
  };

  /** Logger */
  private static final Logger LOGGER = LogManager.getLogger(WUserFileManager.class.getName());

  /**
   * Classe interne pour représenter un dossier de resources
   *
   * @author Nilon123456789
   */
  static class ResourcesEntry {
    /** Le chemin du dossier dans les resources */
    private String folder;

    /** Les fichiers du dossier */
    private String[] files;

    /**
     * Constructeur de la classe {@code ResourcesEntry}.
     *
     * @param folder le chemin du dossier dans les resources
     * @param files les fichiers du dossier
     */
    public ResourcesEntry(String folder, String[] files) {
      this.folder = folder + "/";
      this.files = files;
    }

    /**
     * Retourne le chemin du dossier dans les resources.
     *
     * @return Le chemin du dossier dans les resources
     */
    public String getFolder() {
      return folder;
    }

    /**
     * Retourne les fichiers du dossier.
     *
     * @return Les fichiers du dossier
     */
    public String[] getFiles() {
      return files;
    }
  }

  /** Constructeur privé pour empêcher l'instanciation de la classe */
  private WUserFileManager() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Copy les dossiers de resources dans le dossier de l'utilisateur.
   *
   * <p>Exemple, s'il y a dans le dossier {@code resources} un dossier {@code sample-images} qui
   * contient des images. {@code copyResourcesToUserFolder()} va copier le dossier {@code
   * sample-images} dans le dossier de l'utilisateur.
   *
   * @see com.e24.wolke.filesystem.WFileSystemConstant Chemin du dossier utilisateur
   * @see com.e24.wolke.filesystem.WUserFileManager Liste des dossiers à copier
   * @return {@code true} si tous les fichiers ont été copiés, {@code false} sinon
   */
  public static boolean copyResourcesToUserFolder() {

    boolean success = true;
    for (ResourcesEntry entry : RESOURCES) {
      for (String file : entry.getFiles()) {
        success &= copyResource(entry.getFolder(), file);
      }
    }

    return success;
  }

  /**
   * Copie un fichier de resources dans le dossier de l'utilisateur.
   *
   * @param folderName le nom du dossier (ex: "sample-images/")
   * @param file le nom du fichier
   * @return {@code true} si le fichier a été copié, {@code false} sinon
   */
  private static boolean copyResource(String folderName, String file) {
    InputStream in =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(folderName + file);

    if (in == null) {
      LOGGER.error(
          LocaleManager.getLocaleResourceBundle().getString("userFileManager.fileNotFound"),
          file,
          new FileNotFoundException());
      return false;
    }

    String destinationFolderPath = WFileSystemConstant.SAVE_PATH + folderName;
    try {
      File userFolder = new File(destinationFolderPath);
      if (!userFolder.exists()) {
        userFolder.mkdirs();
      }

      File userFile = new File(destinationFolderPath + file);

      if (userFile.exists()) return true;

      Files.copy(in, userFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

    } catch (IOException e) {
      LOGGER.error(
          LocaleManager.getLocaleResourceBundle().getString("userFileManager.copyError"),
          file,
          destinationFolderPath,
          e);
      return false;
    }

    return true;
  }
}
