package com.e24.wolke.frontend.canvas;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.eventsystem.Subject;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;

/**
 * Panneau de rendu du mode zen
 *
 * @author n-o-o-d-l-e
 */
public class ZenModeCanvasPane extends AbstractCanvasPane {

  /** Numéro de version de la classe pour la sérialisation */
  private static final long serialVersionUID = 1L;

  /** Image compositée de la simulation */
  private BufferedImage imgComposite =
      new BufferedImage(
          this.simulationResolution[0], this.simulationResolution[1], BufferedImage.TYPE_INT_RGB);

  /** Contexte graphique de l'image compositée */
  private Graphics2D compositor = this.imgComposite.createGraphics();

  /** Pression à emettre lors d'un clic gauche */
  private final double PRESSURE_LEFT_CLICK = 5.0;

  /** Pression à emettre lors d'un drag gauche */
  private final double PRESSURE_LEFT_DRAG = 3.0;

  /** Densité de particules à emettre lors d'un clic droit */
  private final double DENSITY_RIGHT_CLICK = 1.0;

  /**
   * Créer le panneau de rendu du mode zen
   *
   * @param controller Le {@code Controller} de l'application
   */
  public ZenModeCanvasPane(Controller controller) {
    super(controller);

    addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e) {
            int[] mousePos = getScaledPixel(e.getX(), e.getY());
            if (!validScaledPixel(mousePos)) {
              return;
            }

            if (SwingUtilities.isLeftMouseButton(e)) {
              controller
                  .getSimulationModel()
                  .getSimulationData()
                  .getCurrentParticleMatrix()
                  .setPressureAt(
                      controller
                          .getSimulationModel()
                          .getSimulationData()
                          .getCurrentParticleMatrix()
                          .getPos(mousePos[0], mousePos[1]),
                      PRESSURE_LEFT_CLICK);
            } else if (SwingUtilities.isRightMouseButton(e)) {
              controller
                  .getSimulationModel()
                  .getSimulationData()
                  .getCurrentParticleMatrix()
                  .setAreaDensityAt(
                      controller
                          .getSimulationModel()
                          .getSimulationData()
                          .getCurrentParticleMatrix()
                          .getPos(mousePos[0], mousePos[1]),
                      DENSITY_RIGHT_CLICK);
            }
          }
        });
    addMouseMotionListener(
        new MouseMotionAdapter() {
          @Override
          public void mouseDragged(MouseEvent e) {
            int[] mousePos = getScaledPixel(e.getX(), e.getY());
            if (!validScaledPixel(mousePos)) {
              return;
            }
            if (SwingUtilities.isLeftMouseButton(e)) {
              controller
                  .getSimulationModel()
                  .getSimulationData()
                  .getCurrentParticleMatrix()
                  .setPressureAt(
                      controller
                          .getSimulationModel()
                          .getSimulationData()
                          .getCurrentParticleMatrix()
                          .getPos(mousePos[0], mousePos[1]),
                      PRESSURE_LEFT_DRAG);
            }
            if (SwingUtilities.isRightMouseButton(e)) {
              controller
                  .getSimulationModel()
                  .getSimulationData()
                  .getCurrentParticleMatrix()
                  .setAreaDensityAt(
                      controller
                          .getSimulationModel()
                          .getSimulationData()
                          .getCurrentParticleMatrix()
                          .getPos(mousePos[0], mousePos[1]),
                      DENSITY_RIGHT_CLICK);
            }
          }
        });
  }

  /**
   * Dessin du panneau de rendu du mode zen
   *
   * @param g Contexte graphique du panneau
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

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
  }

  /** Mettre en place les abonnements aux événements de l'application */
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
