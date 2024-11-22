package com.e24.wolke.frontend.editor;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.eventsystem.Subject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 * Bouton de choix de couleur de l'éditeur avec pastille de la couleur actuelle.
 *
 * @author adrienles
 */
class EditorColorPickerButtonComponent extends EditorButtonComponent {

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Constructeur par defaut. */
  public EditorColorPickerButtonComponent() {
    super();
  }

  /**
   * Constructeur de la classe EditorColorPickerButtonComponent
   *
   * @param controller le {@code Controller} de l'application
   * @param subjects les {@code Subject} des événements que le bouton doit déclencher
   * @param iconPath le chemin de l'icône du bouton
   */
  public EditorColorPickerButtonComponent(
      Controller controller, Subject[] subjects, String iconPath) {
    super(controller, subjects, "ui.tooltips.color_picker", iconPath, false);
    setupSubscribers();
    setIcon(controller.getEditorModel().getToolbox().getColor());
  }

  /** Méthode qui s'occupe de l'abonnement aux événements */
  private void setupSubscribers() {
    super.controller
        .getEditorModel()
        .getSubscriber()
        .subscribe(
            Subject.EDITOR_COLOR_CHANGE,
            (color) -> {
              setIcon((Color) color);
            });
  }

  /**
   * Méthode qui s'occupe de la couleur de la pastille du bouton
   *
   * @param color la couleur de la pastille
   */
  protected void setIcon(Color color) {
    BufferedImage img =
        new BufferedImage(
            this.icon.getIconWidth(), this.icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = img.createGraphics();
    g2d.setRenderingHint(
        java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.drawImage(this.icon.getImage(), 0, 0, null);
    g2d.setColor(color);
    // Dessiner une petite pastille en bas à droite de l'icône
    g2d.fillOval(
        this.icon.getIconWidth() - this.icon.getIconWidth() / 4,
        this.icon.getIconHeight() - this.icon.getIconHeight() / 4,
        this.icon.getIconWidth() / 4,
        this.icon.getIconHeight() / 4);
    g2d.dispose();
    super.setIcon(new ImageIcon(img));
  }
}
