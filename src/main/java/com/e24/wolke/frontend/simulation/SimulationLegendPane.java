package com.e24.wolke.frontend.simulation;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.renderer.RendererConstants.VisualizationType;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventComponent;
import com.e24.wolke.utils.interfaces.Reinitializable;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

/**
 * Composant graphique qui affiche la légende de la simulation
 *
 * @author n-o-o-d-l-e
 */
public class SimulationLegendPane extends JPanel implements WEventComponent, Reinitializable {
  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Contrôleur de l'application */
  Controller controller;

  /** Composant graphique de la légende de la simulation */
  private SimulationLegendComponent simulationLegendComponent;

  /** Bouton de fermeture du panneau de légende */
  private JButton btnClose;

  /** Label du type de visualisation actuel */
  private JLabel lblLegendType;

  /** Nombre de graduations de la légende */
  private static final int TICK_COUNT = 6;

  /** Liste des graduations de la légende */
  private ArrayList<JLabel> labels = new ArrayList<JLabel>();

  /** Type de visualisation actuel avec les unités */
  private String currentVisualizedTypeWithUnits = "  ";

  /** Taille des boutons */
  private final Dimension BUTTON_SIZE = new Dimension(24, 24);

  /** Identifiant des abonnements */
  private int subscriptionID;

  /**
   * Créer le panneau de légende contenant les graduations de la simulation
   *
   * @param controller Le contrôleur de l'application
   */
  public SimulationLegendPane(Controller controller) {
    this.controller = controller;
    subscriptionID = hashCode();

    setupSubscribers();

    this.setVisible(controller.getApplicationModel().getLegendVisibility());

    putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanel");
    setBounds(0, 0, 227, 394);
    setLayout(new MigLayout("ins 5, gap 5", "[grow]", "[25][grow][]"));

    JPanel panelTitle = new JPanel();
    add(panelTitle, "grow");
    panelTitle.setLayout(new BoxLayout(panelTitle, BoxLayout.X_AXIS));
    panelTitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanelContents");

    JLabel lblLegend =
        new JLabel(LocaleManager.getLocaleResourceBundle().getString("ui.simulation.legend.lbl"));
    lblLegend.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
    panelTitle.add(lblLegend);

    panelTitle.add(Box.createGlue());

    btnClose = new JButton();
    btnClose.setMinimumSize(BUTTON_SIZE);
    btnClose.setMaximumSize(BUTTON_SIZE);
    btnClose.setPreferredSize(BUTTON_SIZE);
    btnClose.setToolTipText(LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.close"));
    btnClose.setIcon(
        new FlatSVGIcon("icons/close.svg", 0.75f)
            .setColorFilter(new ColorFilter(color -> UIManager.getColor("Button.foreground"))));
    panelTitle.add(btnClose);

    JPanel panelLegend = new JPanel();
    add(panelLegend, "newline,grow");
    panelLegend.setLayout(new MigLayout("ins 5, gap 5", "[grow][center]", "[grow]"));
    panelLegend.putClientProperty(FlatClientProperties.STYLE_CLASS, "subPanel");

    JPanel panelGradient = new JPanel();
    panelLegend.add(panelGradient, "grow");
    panelGradient.setLayout(new BorderLayout(0, 0));

    simulationLegendComponent = new SimulationLegendComponent(controller);
    panelGradient.add(simulationLegendComponent, BorderLayout.CENTER);

    panelLegend.add(createTickPanel(), "cell 1 0,grow");

    lblLegendType = new JLabel(currentVisualizedTypeWithUnits);
    add(lblLegendType, "newline,grow");
    updateLegendType();

    btnClose.addActionListener(
        e -> {
          controller.getApplicationModel().setLegendVisibility(false);
        });
  }

  /** {@inheritDoc} */
  @Override
  public void setupSubscribers() {
    controller
        .getRendererModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_SIMULATION_MINMAX_CHANGED,
            msg -> updateTicks((double[]) msg),
            subscriptionID);

    controller
        .getRendererModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_SIMULATION_VISUALIZATION_TYPE_CHANGED,
            msg -> updateLegendType(),
            subscriptionID);

    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_APP_LEGEND_VISIBILITY_CHANGED,
            msg -> {
              setVisible((Boolean) msg);
            },
            subscriptionID);
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    removeSubscribers();
    this.simulationLegendComponent.reinitialize();
  }

  /** {@inheritDoc} */
  @Override
  public void removeSubscribers() {
    controller.getRendererModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
    controller.getApplicationModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
  }

  /** Met à jour le type de visualisation de la légende */
  private void updateLegendType() {
    String currentVisualizedType =
        controller.getRendererModel().getCurrentVisualizationType().toString();
    if (controller.getRendererModel().getCurrentVisualizationType()
        == VisualizationType.PRESSURE_X) {
      currentVisualizedType =
          LocaleManager.getLocaleResourceBundle()
              .getString("ui.simulation.visualization.pressure_x_short");
    } else if (controller.getRendererModel().getCurrentVisualizationType()
        == VisualizationType.PRESSURE_Y) {
      currentVisualizedType =
          LocaleManager.getLocaleResourceBundle()
              .getString("ui.simulation.visualization.pressure_y_short");
    }
    switch (controller.getRendererModel().getCurrentVisualizationType()) {
      case PRESSURE:
      case PRESSURE_X:
      case PRESSURE_Y:
        currentVisualizedTypeWithUnits =
            currentVisualizedType
                + " "
                + LocaleManager.getLocaleResourceBundle()
                    .getString("ui.simulation.visualization.pressure.units");
        break;
      case DENSITY:
        currentVisualizedTypeWithUnits =
            currentVisualizedType
                + " "
                + LocaleManager.getLocaleResourceBundle()
                    .getString("ui.simulation.visualization.density.units");
        break;
      case VELOCITY:
      case VELOCITY_X:
      case VELOCITY_Y:
        currentVisualizedTypeWithUnits =
            currentVisualizedType
                + " "
                + LocaleManager.getLocaleResourceBundle()
                    .getString("ui.simulation.visualization.velocity.units");
        break;
    }
    lblLegendType.setText(currentVisualizedTypeWithUnits);
    revalidate();
  }

  /**
   * Créer le panneau des graduations de la légende et initie les graduations
   *
   * @return Le panneau des graduations
   */
  private JPanel createTickPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 1, 0, 0));
    for (int i = 0; i < TICK_COUNT; i++) {
      labels.add(new JLabel(String.format("%.2f", (1 - i * 1.0f / (TICK_COUNT - 1)))));
      panel.add(labels.get(i));
    }
    return panel;
  }

  /**
   * Met à jour les graduations de la légende en fonction de nouvelles valeurs minimums et maximums.
   *
   * @param minMax Les nouvelles valeurs minimums et maximums
   */
  public void updateTicks(double[] minMax) {
    int i = 0;
    double value = 1;
    for (JLabel tick : labels) {
      value = minMax[1] + (minMax[0] - minMax[1]) * (i / (double) (TICK_COUNT - 1));
      tick.setText(String.format("%.2f", value));
      i++;
    }
  }
}
