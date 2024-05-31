package com.e24.wolke.frontend.help;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Composant swing pour afficher une image qui s'adapte à la taille du panneau et qui reste centrée.
 *
 * @author n-o-o-d-l-e
 */
public class ImageComponent extends JPanel {

  /** Numéro de série de la classe */
  private static final long serialVersionUID = 1L;

  /** L'URL de l'image à afficher */
  private URL imageURL;

  /** Étiquette de l'image */
  private JLabel imageLabel;

  /** Taille de l'image */
  private int[] imageSize = new int[2];

  /**
   * Constructeur de la classe de composant d'image
   *
   * @param imageURL L'URL de l'image à afficher
   */
  public ImageComponent(URL imageURL) {
    this.imageLabel = new JLabel();
    this.imageLabel.setHorizontalAlignment(JLabel.CENTER);
    this.imageLabel.setVerticalAlignment(JLabel.CENTER);
    this.setImageURL(imageURL);
    updateImage();
    this.setLayout(new BorderLayout());
    add(this.imageLabel, BorderLayout.CENTER);

    addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            updateImage();
          }
        });
  }

  /**
   * Met à jour l'image à afficher et redessine le panneau.
   *
   * @param imageURL L'URL de la nouvelle image à afficher
   */
  public void setImageURL(URL imageURL) {
    if (imageURL == null) {
      return;
    }
    this.imageURL = imageURL;
    ImageIcon imageIcon = new ImageIcon(this.imageURL);
    imageSize[0] = imageIcon.getIconWidth();
    imageSize[1] = imageIcon.getIconHeight();
    updateImage();
  }

  /** Met à jour l'image affichée ainsi que sa taille */
  private void updateImage() {
    if (getWidth() == 0 || getHeight() == 0) {
      return;
    }
    double imageRatio = ((double) imageSize[0]) / imageSize[1];
    double panelRatio = ((double) getWidth()) / getHeight();
    double scale = .95f;
    int width = 0;
    int height = 0;
    if (imageRatio > panelRatio) {
      width = getWidth();
      height = (int) (width / imageRatio * scale);
      width = (int) (width * scale);
    } else {
      height = getHeight();
      width = (int) (height * imageRatio * scale);
      height = (int) (height * scale);
    }
    this.imageLabel.setText(
        "<html><img src="
            + imageURL.toString()
            + " height="
            + height
            + " width="
            + width
            + "></html>");
  }

  /**
   * Retourne l'URL de l'image actuellement affichée.
   *
   * @return L'URL de l'image actuellement affichée
   */
  public URL getImageURL() {
    return this.imageURL;
  }
}
