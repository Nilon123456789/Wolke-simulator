package com.e24.wolke.frontend.canvas;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.simulation.SimulationConstants;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventComponent;
import com.e24.wolke.utils.interfaces.Reinitializable;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe abstraite pour les panneaux de rendu
 *
 * @author adrienles
 */
public abstract class AbstractCanvasPane extends JPanel
    implements WEventComponent, Reinitializable {

  /** Logger de la classe */
  protected static final Logger LOGGER =
      LogManager.getLogger(AbstractCanvasPane.class.getSimpleName());

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application */
  protected Controller controller;

  /** Identifiant des abonnements */
  protected int subscriptionID;

  /** Résolution du canvas de rendu */
  protected int[] simulationResolution = {
    SimulationConstants.DEFAULT_MATRIX_SIZE_X, SimulationConstants.DEFAULT_MATRIX_SIZE_Y
  };

  /** Matrice de transformation de l'image afin qu'elle reste en plein écran et centrée */
  protected AffineTransform matSimulation = new AffineTransform();

  /**
   * Matrice de transformation de l'image afin d'agrandir les vecteurs à la taille du composant par
   * rapport à celle de la résolution
   */
  protected AffineTransform matScale = new AffineTransform();

  /**
   * Matrice de transformation de l'image afin qu'elle reste centrée, puisque sa résolution devrait
   * déjà correspondre à la taille maximale permise par le panneau. Utile pour l'affichage de champs
   * de vecteurs.
   */
  protected AffineTransform matFullImageRes = new AffineTransform();

  /** Résolution du composant swing */
  private int[] panelResolution;

  /** Résolution de l'espace prise par l'image */
  protected int[] fullImageResolution;

  /** Etat d'initialisation du panneau */
  protected boolean isInitialized = false;

  /** Taille d'un pixel en fonction de la résolution */
  protected double pixelSize;

  /** Image du champ de vecteurs */
  private BufferedImage imgVectorField = null;

  /** Image du dernier champ de vecteurs */
  private BufferedImage imgLastVectorField = null;

  /** Contexte graphique du champ de vecteurs */
  private Graphics2D g2dVectorField = null;

  /** Marge gauche du panneau */
  protected double leftPadding = 0;

  /** Marge haute du panneau */
  protected double topPadding = 0;

  /**
   * Constructeur de la classe de canvas
   *
   * @param controller Le {@code Controller} de l'application
   */
  public AbstractCanvasPane(Controller controller) {
    this.controller = controller;
    subscriptionID = hashCode();
    setupSubscribers();
    addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            updatePanelResolution();
          }
        });
  }

  /**
   * Initialisation du panneau de rendu de la simulation afin de la garder centrée et de bonne
   * dimension.
   */
  protected void initialize() {
    // TODO : Repenser au nombre/usage de matrices de transformation
    if (!this.isInitialized) {
      if (getWidth() != 0) {
        this.panelResolution = new int[] {getWidth(), getHeight()};
        this.simulationResolution = controller.getSimulationResolution();

        double ratio = (double) this.panelResolution[0] / panelResolution[1];
        double simulationRatio = (double) this.simulationResolution[0] / simulationResolution[1];

        if (ratio > simulationRatio) { // Le composant est plus large que la simulation
          this.pixelSize = (double) this.panelResolution[1] / simulationResolution[1];
          controller.getRendererModel().setPixelSize(this.pixelSize);
          this.fullImageResolution =
              new int[] {(int) (this.pixelSize * simulationResolution[0]), this.panelResolution[1]};
          this.controller.getRendererModel().setCanvasResolution(this.fullImageResolution);
        } else { // Le composant est plus haut que la simulation
          this.pixelSize = (double) this.panelResolution[0] / simulationResolution[0];
          controller.getRendererModel().setPixelSize(this.pixelSize);
          this.fullImageResolution =
              new int[] {this.panelResolution[0], (int) (this.pixelSize * simulationResolution[1])};
          this.controller.getRendererModel().setCanvasResolution(this.fullImageResolution);
        }
        this.matSimulation = new AffineTransform();
        this.leftPadding = (panelResolution[0] - this.pixelSize * simulationResolution[0]) / 2;
        this.topPadding = (panelResolution[1] - this.pixelSize * simulationResolution[1]) / 2;
        this.matSimulation.translate(this.leftPadding, this.topPadding);
        this.matFullImageRes = new AffineTransform();
        this.matFullImageRes.translate(
            (panelResolution[0] - fullImageResolution[0]) / 2,
            (panelResolution[1] - fullImageResolution[1]) / 2);
        this.matSimulation.scale(this.pixelSize, this.pixelSize);
        this.matScale = new AffineTransform();
        this.matScale.scale(this.pixelSize, this.pixelSize);
        this.controller.getRendererModel().setMatScale(this.matScale);

        this.isInitialized = true;
      }
    }
  }

  /**
   * Dessin du champs de vecteurs
   *
   * @param g2d Contexte graphique du panneau
   */
  protected void drawVectorField(Graphics2D g2d) {
    this.imgLastVectorField = this.imgVectorField;
    if (this.controller.getRendererModel().getIsRendering()) return;
    if (imgVectorField == null
        || this.imgVectorField.getWidth()
            != this.fullImageResolution[0]) { // Si l'image n'a pas été créée (première fois qu'on
      // affiche les vecteurs)
      // ou que la résolution a changé
      this.imgVectorField =
          new BufferedImage(
              this.fullImageResolution[0],
              this.fullImageResolution[1],
              BufferedImage.TYPE_INT_ARGB);
      this.g2dVectorField = this.imgVectorField.createGraphics();
    } else {
      this.g2dVectorField.setBackground(new Color(0, 0, 0, 0));
      this.g2dVectorField.clearRect(0, 0, this.fullImageResolution[0], this.fullImageResolution[1]);
    }

    this.g2dVectorField.setColor(Color.black);
    double circleDiam = 2.0f * this.pixelSize;
    int step = this.controller.getRendererModel().getVectorFieldLinesStep();
    for (int i = step - 1; i < this.simulationResolution[0] - step; i += step) {
      for (int j = step - 1; j < this.simulationResolution[1] - step; j += step) {
        this.g2dVectorField.fill(
            new Ellipse2D.Double(
                i * this.pixelSize - circleDiam / 2,
                j * this.pixelSize - circleDiam / 2,
                circleDiam,
                circleDiam));
      }
    }
    // C'est beau mais si lent haha
    this.g2dVectorField.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    this.g2dVectorField.setStroke(
        new BasicStroke((int) (pixelSize), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    if (this.controller.getRendererModel().getIsRendering()) {
      this.imgVectorField = this.imgLastVectorField;
      return;
    }
    try {
      this.g2dVectorField.draw(
          this.matScale.createTransformedShape(
              this.controller.getRendererModel().getVectorFieldLinesPath()));
    } catch (Exception e) {
      AbstractCanvasPane.LOGGER.warn(
          LocaleManager.getLocaleResourceBundle()
              .getString("log.rendering.drawVectorField.skipped"),
          e);
    }
    g2d.drawImage(this.imgVectorField, this.matFullImageRes, null);
  }

  /**
   * Retourne les coordonnées de pixel à l'échelle de l'image de la simulation
   *
   * @param x Coordonnée x du canvas
   * @param y Coordonnée y du canvas
   * @return Coordonnées de pixel à l'échelle de l'image de la simulation
   */
  protected int[] getScaledPixel(int x, int y) {
    int[] scaledPixel = new int[2];
    scaledPixel[0] = (int) ((x - this.matSimulation.getTranslateX()) / this.pixelSize);
    scaledPixel[1] = (int) ((y - this.matSimulation.getTranslateY()) / this.pixelSize);
    return scaledPixel;
  }

  /**
   * Retourne si les coordonnées de pixel sont valides
   *
   * @param pixelCoord Coordonnées de pixel
   * @return Si les coordonnées de pixel sont valides
   */
  protected boolean validScaledPixel(int[] pixelCoord) {
    return pixelCoord[0] >= 0
        && pixelCoord[0] < this.simulationResolution[0]
        && pixelCoord[1] >= 0
        && pixelCoord[1] < this.simulationResolution[1];
  }

  /** {@inheritDoc} */
  @Override
  public abstract void setupSubscribers();

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    removeSubscribers();
  }

  /** {@inheritDoc} */
  @Override
  public abstract void removeSubscribers();

  /** Met à jour la résolution du panneau */
  protected void updatePanelResolution() {
    this.panelResolution = new int[] {getWidth(), getHeight()};
    this.isInitialized = false;
    initialize();
    this.controller
        .getApplicationModel()
        .getPublisher()
        .publish(Subject.INSPECTOR_REPOSITION_NEEDED, null);
    repaint();
  }

  /**
   * Retourne les coordonnées du canvas à partir des valeurs de pixel
   *
   * @param coordinates Coordonnées de pixel
   * @return Coordonnées du canvas
   */
  public int[] getCanvasPositionFromPixelValues(int[] coordinates) {
    return new int[] {
      (int) (coordinates[0] * this.pixelSize + this.leftPadding),
      (int) (coordinates[1] * this.pixelSize + this.topPadding)
    };
  }
}
