package com.e24.wolke.backend.models.editor;

import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.editor.tools.WToolImage;
import com.e24.wolke.utils.images.WImageUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * La classe {@code WEditorIO} permet de gerer l'import et l'export de fichiers de l'editeur
 *
 * @author MeriBouisri
 * @author adrienles
 */
public class WEditorIO {

  /** Constructeur de la classe {@code WEditorIO} */
  private WEditorIO() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Methode permettant d'importer dans l'outil de dessin d'image SVG ou PNG
   *
   * @param imagePath Le chemin de l'image a importer
   * @param tool Le {@code WToolImage} avec lequel dessiner
   */
  public static void importToolImage(File imagePath, WToolImage tool) {
    if (imagePath.getName().endsWith(".svg")
        || imagePath.getName().endsWith(".png")
        || imagePath.getName().endsWith(".SVG")
        || imagePath.getName().endsWith(".PNG")) {
      try {
        tool.importImage(imagePath.toURI().toURL());
      } catch (MalformedURLException e) {
        JOptionPane.showMessageDialog(
            null,
            LocaleManager.getLocaleResourceBundle().getString("ui.editor.image_does_not_exist"),
            LocaleManager.getLocaleResourceBundle().getString("ui.console.log_levels.error"),
            JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(
          null,
          LocaleManager.getLocaleResourceBundle().getString("ui.editor.wrong_image_format"),
          LocaleManager.getLocaleResourceBundle().getString("ui.console.log_levels.error"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Methode permettant d'importer une image
   *
   * @param imagePath Le chemin de l'image
   * @param width Longueur horizontale
   * @param height Longueur verticale
   * @return Le {@code BufferedImage} a importer
   */
  public static BufferedImage importFixedImage(File imagePath, int width, int height) {
    if (imagePath.getName().endsWith(".svg")
        || imagePath.getName().endsWith(".png")
        || imagePath.getName().endsWith(".SVG")
        || imagePath.getName().endsWith(".PNG")) {
      try {

        if (imagePath.getName().endsWith(".svg") || imagePath.getName().endsWith(".SVG"))
          return WImageUtils.loadSVGImage(imagePath.toURI().toURL(), width, height);
        else if (imagePath.getName().endsWith(".png") || imagePath.getName().endsWith(".PNG"))
          return ImageIO.read(imagePath);

      } catch (MalformedURLException e) {
        JOptionPane.showMessageDialog(
            null,
            LocaleManager.getLocaleResourceBundle().getString("ui.editor.image_does_not_exist"),
            LocaleManager.getLocaleResourceBundle().getString("ui.console.log_levels.error"),
            JOptionPane.ERROR_MESSAGE);
      } catch (IOException e) {
        JOptionPane.showMessageDialog(
            null,
            "IOException",
            LocaleManager.getLocaleResourceBundle().getString("ui.console.log_levels.error"),
            JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(
          null,
          LocaleManager.getLocaleResourceBundle().getString("ui.editor.wrong_image_format"),
          LocaleManager.getLocaleResourceBundle().getString("ui.console.log_levels.error"),
          JOptionPane.ERROR_MESSAGE);
    }

    return null;
  }

  /**
   * Methode permettant d'exporter une image de l'editeur
   *
   * @param imagePath Le {@code File} vers lequel importer
   * @param image L'image a exporter
   */
  public static void exportImage(File imagePath, BufferedImage image) {
    try {
      imagePath.createNewFile();

      ImageIO.write(image, "png", imagePath);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          LocaleManager.getLocaleResourceBundle().getString("ui.editor.io_exception")
              + imagePath.getPath(),
          LocaleManager.getLocaleResourceBundle().getString("ui.console.log_levels.error"),
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
