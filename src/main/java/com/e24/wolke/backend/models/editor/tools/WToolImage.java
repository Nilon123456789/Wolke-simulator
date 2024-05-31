package com.e24.wolke.backend.models.editor.tools;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import com.e24.wolke.utils.images.WImageUtils;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * La classe {@code WToolShapeImage} permet de dessiner une image vectorielle.
 *
 * @author n-o-o-d-l-e
 */
public class WToolImage extends WToolShape {

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** Le {@code ToolType} de cet outil */
  public static final ToolType TOOL_TYPE = ToolType.IMAGE;

  /** Le {@code BufferedImage} de l'image */
  private BufferedImage image;

  /**
   * Construction d'un {@code WToolShapePolygon}
   *
   * @param controller Le {@code Controller} de l'application
   */
  public WToolImage(Controller controller) {
    super(WToolImage.TOOL_TYPE);
    this.controller = controller;
  }

  /** {@inheritDoc} */
  @Override
  public void use(Graphics2D g2d) {
    int[] coords = {getStartX(), getStartY(), getCurrentX(), getCurrentY()};

    g2d.setComposite(AlphaComposite.SrcOver);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    double drawWidth = coords[2] - coords[0];
    double drawHeight = coords[3] - coords[1];

    if (image.getWidth() > image.getHeight()) {
      drawHeight = drawWidth * image.getHeight() / image.getWidth();
    } else {
      drawWidth = drawHeight * image.getWidth() / image.getHeight();
    }

    g2d.drawImage(image, coords[0], coords[1], (int) drawWidth, (int) drawHeight, null);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
  }

  /**
   * Méthode permettant d'importer une image SVG ou PNG à partir d'un fichier.
   *
   * @param imagePath Le chemin du fichier SVG ou PNG
   */
  public void importImage(URL imagePath) {
    if (imagePath.getFile().endsWith(".svg")) {
      this.image =
          WImageUtils.loadSVGImage(
              imagePath,
              this.controller.getEditorModel().getEditorImage().getWidth(),
              this.controller.getEditorModel().getEditorImage().getHeight());
    } else if (imagePath.getFile().endsWith(".png")) {
      loadPNGImage(imagePath);
    } else {
      JOptionPane.showMessageDialog(
          null,
          LocaleManager.getLocaleResourceBundle().getString("ui.editor.image_does_not_exist"),
          LocaleManager.getLocaleResourceBundle().getString("ui.console.log_levels.error"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Méthode permettant de charger une image PNG.
   *
   * @param imagePath Le chemin du fichier PNG
   */
  private void loadPNGImage(URL imagePath) {
    try {
      this.image = ImageIO.read(imagePath);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          LocaleManager.getLocaleResourceBundle().getString("ui.editor.image_does_not_exist"),
          LocaleManager.getLocaleResourceBundle().getString("ui.console.log_levels.error"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /** Cette méthode ne devrait jamais être appellée */
  @Override
  public Shape createShape(int x, int y, int width, int height) {
    // Méthode qui ne devrait pas être appelée
    return new Rectangle2D.Double(x, y, width, height);
  }
}
