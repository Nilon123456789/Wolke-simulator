package com.e24.wolke.frontend.canvas;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.eventsystem.Subject;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

/**
 * Panneau de la visualisation de la simulation
 *
 * @author adrienles
 * @author MeriBouisri
 */
public class SimulationCanvasPane extends AbstractCanvasPane {

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Image compositée de la simulation */
  private BufferedImage imgComposite =
      new BufferedImage(
          this.simulationResolution[0], this.simulationResolution[1], BufferedImage.TYPE_INT_RGB);

  /** Contexte graphique de l'image compositée */
  private Graphics2D compositor = this.imgComposite.createGraphics();

  /**
   * La fraction de la longueur physique de la simulation pour laquelle on affiche la légende de la
   * taille physique
   */
  private final int PHYSICAL_LENGTH_FRACTION = 20;

  /** La hauteur d'un taquet de la légende de la taille physique */
  private final int TICK_HEIGHT = 10;

  /** Le padding en haut du panneau de la légende de la taille physique */
  private final int SIZE_LEGEND_OFFSET = 20;

  /** Le padding entre l'étiquette de la légende de la taille physique et les taquets */
  private final int SIZE_LEGEND_PADDING = 5;

  /** Constructeur par defaut. */
  public SimulationCanvasPane() {
    this(null);
  }

  /**
   * Créer le panneau de rendu de la simulation
   *
   * @param controller Le {@code Controller} de l'application
   */
  public SimulationCanvasPane(Controller controller) {
    super(controller);

    addMouseMotionListener(
        new MouseMotionAdapter() {
          @Override
          public void mouseMoved(MouseEvent e) {
            int[] mousePos = getScaledPixel(e.getX(), e.getY());
            if (!validScaledPixel(mousePos)) {
              mousePos[0] = -1;
              mousePos[1] = -1;
            }
            controller.getApplicationModel().setMousePositionRelativeToSimulation(mousePos);
          }
        });
  }

  /**
   * Dessin du panneau de rendu de la simulation
   *
   * @param g Contexte graphique du panneau4
   */
  @Override
  public void paintComponent(Graphics g) {

    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    if (!this.isInitialized) {
      initialize();
    }

    this.compositor.drawImage(this.controller.getRendererModel().getSimulationImage(), null, null);
    this.compositor.drawImage(this.controller.getEditorModel().getEditorImage(), null, null);
    g2d.drawImage(this.imgComposite, this.matSimulation, null);

    if (this.controller.getRendererModel().getShowVectors()) { // Si on doit afficher les vecteurs
      g2d.drawImage(
          this.controller.getRendererModel().getVectorImage(), this.matFullImageRes, null);
    }

    g2d.setColor(Color.WHITE);

    String sizeLegendSize =
        String.format(
                "%.2f",
                controller.getSimulationModel().getSimulationData().getPhysicalXLength()
                    / PHYSICAL_LENGTH_FRACTION)
            + " m";
    int textWidth = g2d.getFontMetrics().stringWidth(sizeLegendSize);
    double physicalLengthPortion = (getWidth() - 2 * leftPadding) / PHYSICAL_LENGTH_FRACTION;

    g2d.drawString(
        sizeLegendSize, getWidth() / 2 - textWidth / 2, (int) topPadding + SIZE_LEGEND_OFFSET);
    g2d.drawLine(
        (int) (getWidth() / 2 - physicalLengthPortion / 2),
        (int) topPadding + SIZE_LEGEND_OFFSET + SIZE_LEGEND_PADDING - TICK_HEIGHT / 2,
        (int) (getWidth() / 2 - physicalLengthPortion / 2),
        (int) topPadding + SIZE_LEGEND_OFFSET + SIZE_LEGEND_PADDING + TICK_HEIGHT / 2);
    g2d.drawLine(
        (int) (getWidth() / 2 - physicalLengthPortion / 2),
        (int) topPadding + SIZE_LEGEND_OFFSET + SIZE_LEGEND_PADDING,
        (int) (getWidth() / 2 + physicalLengthPortion / 2),
        (int) topPadding + SIZE_LEGEND_OFFSET + SIZE_LEGEND_PADDING);
    g2d.drawLine(
        (int) (getWidth() / 2 + physicalLengthPortion / 2),
        (int) topPadding + SIZE_LEGEND_OFFSET + SIZE_LEGEND_PADDING - TICK_HEIGHT / 2,
        (int) (getWidth() / 2 + physicalLengthPortion / 2),
        (int) topPadding + SIZE_LEGEND_OFFSET + SIZE_LEGEND_PADDING + TICK_HEIGHT / 2);
  }

  /** {@inheritDoc} */
  @Override
  public void setupSubscribers() {
    this.controller
        .getRendererModel()
        .getSubscriber()
        .subscribeWithID(Subject.ON_BUFFER_IMAGE_DONE, msg -> repaint(), this.subscriptionID);
  }

  /** {@inheritDoc} */
  @Override
  public void removeSubscribers() {
    this.controller.getRendererModel().getSubscriber().unsubscribeAllWithID(this.subscriptionID);
  }
}
