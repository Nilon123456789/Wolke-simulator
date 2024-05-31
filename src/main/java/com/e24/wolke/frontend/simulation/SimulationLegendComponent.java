package com.e24.wolke.frontend.simulation;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.renderer.RendererConstants;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventComponent;
import com.e24.wolke.utils.images.WColor;
import com.e24.wolke.utils.interfaces.Reinitializable;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Composant du gradient de la légende de la simulation
 *
 * @author n-o-o-d-l-e
 */
public class SimulationLegendComponent extends JPanel implements WEventComponent, Reinitializable {
  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Contrôleur de l'application */
  private Controller controller;

  /** Couleurs HSB du dégradé */
  private float[][] gradientHSBColors = {
    RendererConstants.DEFAULT_GRADIENT_HSB_COLORS[0],
    RendererConstants.DEFAULT_GRADIENT_HSB_COLORS[1]
  };

  /** Nombre de pas de couleurs pour l'interpolation de la légende */
  private final int COLOR_COUNT = 256;

  /** Image tampon de la légende */
  BufferedImage legendImage = new BufferedImage(1, this.COLOR_COUNT, BufferedImage.TYPE_INT_RGB);

  /** Matrice de transformation du tampon de la légende afin qu'elle remplisse le panneau */
  private AffineTransform mat = new AffineTransform();

  /** Identifiant des abonnements */
  private int subscriptionID;

  /**
   * Créer le panneau de légende de la simulation
   *
   * @param controller Le contrôleur de l'application
   */
  public SimulationLegendComponent(Controller controller) {
    this.controller = controller;
    this.subscriptionID = hashCode();
    addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            SimulationLegendComponent.this.mat.setToIdentity();
            mat.scale(getWidth(), getHeight() / (double) COLOR_COUNT);
          }
        });
    setOpaque(false);
    setupGradientHSBColors();
    setupSubscribers();
  }

  /** {@inheritDoc} */
  @Override
  public void setupSubscribers() {
    this.controller
        .getRendererModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_SIMULATION_GRADIENT_COLORS_CHANGED,
            msg -> setGradientHSBColors((float[][]) msg),
            subscriptionID);
    this.controller
        .getRendererModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_GRAYSCALE_VISUALISATION_CHANGED,
            msg -> setupGradientHSBColors(),
            subscriptionID);
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    removeSubscribers();
  }

  /** {@inheritDoc} */
  @Override
  public void removeSubscribers() {
    this.controller.getRendererModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
  }

  /**
   * Redessiner le panneau de légende de la simulation
   *
   * @param g Le contexte graphique
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    g2d.drawImage(this.legendImage, this.mat, null);
  }

  /** Mettre à jour le dégradé de la légende */
  private void setupGradientHSBColors() {
    for (int i = 0; i < this.COLOR_COUNT; i++) {
      this.legendImage.setRGB(
          0,
          i,
          this.controller.getRendererModel().getGrayscaleVisualisation()
              ? WColor.interpolateGrayscaleFromMinMax(i, COLOR_COUNT, 0)
              : WColor.interpolateHSBColorFromMinMax(
                  gradientHSBColors[0], gradientHSBColors[1], i, 0, COLOR_COUNT));
    }
    repaint();
  }

  /**
   * Mettre à jour le dégradé de la légende
   *
   * @param gradientHSBColors Les nouvelles couleurs HSB du dégradé
   */
  public void setGradientHSBColors(float[][] gradientHSBColors) {
    if (gradientHSBColors != null && gradientHSBColors.length == 2) {
      this.gradientHSBColors = gradientHSBColors;
    }
    setupGradientHSBColors();
  }
}
