package com.e24.wolke.frontend.simulation;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.obstacles.WObstacleCell;
import com.e24.wolke.backend.simulation.physics.ParticleMatrix;
import com.e24.wolke.eventsystem.Subject;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

/**
 * Panneau d'inspection d'une cellule de la simulation
 *
 * @author n-o-o-d-l-e
 */
public class SimulationInspectPane extends JPanel {

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** Bouton de fermeture du panneau de légende */
  private JButton btnClose;

  /** Position de la cellule inspectée */
  private int[] cellPosition = new int[2];

  /** Étiquette de la position de la cellule inspectée */
  private JLabel lblCellPosition =
      new JLabel(
          LocaleManager.getLocaleResourceBundle().getString("ui.simulation.inspector.cellPosition")
              + " : {x: XXXX, y: XXXX} px");

  /** Étiquette de la velocité de la cellule inspectée */
  private JLabel lblCellVelocity =
      new JLabel(
          LocaleManager.getLocaleResourceBundle().getString("ui.simulation.inspector.cellVelocity")
              + " : {x: XX.XX, y: XX.XX} m/s");

  /** Étiquette de la pression de la cellule inspectée */
  private JLabel lblCellPressure =
      new JLabel(
          LocaleManager.getLocaleResourceBundle().getString("ui.simulation.inspector.cellPressure")
              + " : X.XX Pa");

  /** Étiquette de la densité de la cellule inspectée */
  private JLabel lblCellDensity =
      new JLabel(
          LocaleManager.getLocaleResourceBundle().getString("ui.simulation.inspector.cellDensity")
              + " : X.XX kg/m\u00B3");

  /** Étiquette de la température de la cellule inspectée */
  private JLabel lblCellTemperature =
      new JLabel(
          LocaleManager.getLocaleResourceBundle()
                  .getString("ui.simulation.inspector.cellTemperature")
              + " : XX.XX\u00B0C");

  /** Étiquette du type d'obstacle de la cellule inspectée */
  private JLabel lblCellObstacle =
      new JLabel(
          LocaleManager.getLocaleResourceBundle().getString("ui.simulation.inspector.cellObstacle")
              + " : XXXXXXX");

  /** Étiquette de l'angle de la normale de la cellule inspectée */
  private JLabel lblCellNormalAngle =
      new JLabel(
          LocaleManager.getLocaleResourceBundle()
                  .getString("ui.simulation.inspector.cellNormalAngle")
              + " : XX.XX rad");

  /** Taille des boutons */
  private final Dimension BUTTON_SIZE = new Dimension(24, 24);

  /** Constructeur par defaut */
  public SimulationInspectPane() {}

  /**
   * Créer le panneau d'inspection
   *
   * @param controller Le {@code Controller} de l'application
   */
  public SimulationInspectPane(Controller controller) {
    this.controller = controller;

    createTitleBar();

    this.cellPosition[0] = 50;
    this.cellPosition[1] = 50;

    add(lblCellPosition, "newline,grow");
    add(this.lblCellVelocity, "newline,grow");
    add(this.lblCellPressure, "newline,grow");
    add(this.lblCellDensity, "newline,grow");
    add(this.lblCellTemperature, "newline,grow");
    add(this.lblCellObstacle, "newline,grow");
    add(this.lblCellNormalAngle, "newline,grow");

    setPreferredSize(null);
    setSize(getPreferredSize());

    setupSubscribers();
  }

  /**
   * Surcharge de la méthode paintComponent pour dessiner un petit rectangle au coin de la fenêtre
   * inspecté
   *
   * @param g Le contexte graphique du composant
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(UIManager.getColor("Button.foreground"));
    g.fillPolygon(new int[] {0, 0, 20}, new int[] {0, 20, 0}, 3); // triangle en haut à gauche du
  }

  /** Créer la barre de titre du panneau d'inspection */
  private void createTitleBar() {

    putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanel");
    setBounds(0, 0, 245, 394);
    setLayout(new MigLayout("ins 5, gap 5", "[grow]", "[25][][][][][][][]"));

    JPanel panelTitle = new JPanel();
    add(panelTitle, "grow");
    panelTitle.setLayout(new BoxLayout(panelTitle, BoxLayout.X_AXIS));
    panelTitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanelContents");

    JLabel lblInspect =
        new JLabel(LocaleManager.getLocaleResourceBundle().getString("ui.simulation.inspector"));
    lblInspect.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
    panelTitle.add(lblInspect);

    panelTitle.add(Box.createGlue());

    this.btnClose = new JButton();
    btnClose.setMinimumSize(BUTTON_SIZE);
    btnClose.setMaximumSize(BUTTON_SIZE);
    btnClose.setPreferredSize(BUTTON_SIZE);
    btnClose.setIcon(
        new FlatSVGIcon("icons/close.svg", 0.75f)
            .setColorFilter(new ColorFilter(color -> UIManager.getColor("Button.foreground"))));

    this.btnClose.addActionListener(
        e -> controller.getApplicationModel().setInspectorButtonState(false));
    panelTitle.add(this.btnClose);
  }

  /** Mise à jour des données du panneau d'inspection à chaque image */
  private void updateData() {
    if (controller.getSimulationModel().getSimulationData().getCurrentParticleMatrix() != null) {
      ParticleMatrix particleMatrix =
          this.controller.getSimulationModel().getSimulationData().getCurrentParticleMatrix();
      int cellPositionInt = particleMatrix.getPos(cellPosition[0], cellPosition[1]);
      WObstacleCell obstacleCell =
          this.controller.getObstacleModel().getObstacleMatrix().getElementAt(cellPositionInt);

      if (cellPosition[0] >= 0 && cellPosition[1] >= 0) {
        this.lblCellPosition.setText(
            LocaleManager.getLocaleResourceBundle()
                    .getString("ui.simulation.inspector.cellPosition")
                + " : {x: "
                + this.cellPosition[0]
                + ", y: "
                + this.cellPosition[1]
                + "} px");
        this.lblCellVelocity.setText(
            LocaleManager.getLocaleResourceBundle()
                    .getString("ui.simulation.inspector.cellVelocity")
                + " : {x: "
                + formatDouble(particleMatrix.getXVelocityAt(cellPositionInt))
                + ", y: "
                + formatDouble(particleMatrix.getYVelocityAt(cellPositionInt))
                + "} m/s");
        this.lblCellPressure.setText(
            LocaleManager.getLocaleResourceBundle()
                    .getString("ui.simulation.inspector.cellPressure")
                + " : "
                + formatDouble(particleMatrix.getPressureAt(cellPositionInt))
                + " Pa");
        this.lblCellDensity.setText(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.inspector.cellDensity")
                + " : "
                + formatDouble(particleMatrix.getAreaDensityAt(cellPositionInt))
                + " kg/m\u00B3");
        this.lblCellTemperature.setText(
            LocaleManager.getLocaleResourceBundle()
                    .getString("ui.simulation.inspector.cellTemperature")
                + " : "
                + formatDouble(particleMatrix.getTemperatureAt(cellPositionInt))
                + "\u00B0C");
        this.lblCellObstacle.setText(
            LocaleManager.getLocaleResourceBundle()
                    .getString("ui.simulation.inspector.cellObstacle")
                + " : "
                + ((obstacleCell != null)
                    ? controller
                        .getObstacleModel()
                        .getObstacleMatrix()
                        .getElementAt(cellPositionInt)
                        .toString()
                    : LocaleManager.getLocaleResourceBundle().getString("common.none")));

        this.lblCellNormalAngle.setText(
            LocaleManager.getLocaleResourceBundle()
                    .getString("ui.simulation.inspector.cellNormalAngle")
                + " : "
                + ((obstacleCell != null)
                    ? (!Double.isNaN(obstacleCell.getAverageNormal())
                        ? (formatDouble(obstacleCell.getAverageNormal()) + " rad")
                        : LocaleManager.getLocaleResourceBundle().getString("common.none"))
                    : LocaleManager.getLocaleResourceBundle().getString("common.none")));
      }
    }
  }

  /**
   * Retourne la valeur formatée à 2 décimales d'un double en entrée
   *
   * @param value La valeur à formater
   * @return La valeur formatée
   */
  private String formatDouble(double value) {
    return String.format("%.2f", value);
  }

  /**
   * Met à jour la position de la cellule inspectée
   *
   * @param newCellPosition La nouvelle position de la cellule
   */
  public void setCellPosition(Integer[] newCellPosition) {
    this.cellPosition[0] = newCellPosition[0];
    this.cellPosition[1] = newCellPosition[1];
    updateData();
  }

  /**
   * Retourne la position de la cellule inspectée
   *
   * @return La position de la cellule inspectée
   */
  public int[] getCellPosition() {
    return this.cellPosition;
  }

  /** Setup des abonnements aux événements */
  private void setupSubscribers() {
    this.controller
        .getRendererModel()
        .getSubscriber()
        .subscribe(Subject.ON_BUFFER_IMAGE_DONE, e -> updateData());
    this.controller
        .getApplicationModel()
        .getSubscriber()
        .subscribe(Subject.ON_SIMULATION_CANVAS_PANE_CLICKED, e -> setCellPosition((Integer[]) e));
  }
}
