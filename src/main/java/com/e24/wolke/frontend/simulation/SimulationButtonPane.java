package com.e24.wolke.frontend.simulation;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventComponent;
import com.e24.wolke.utils.interfaces.Reinitializable;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.Dimension;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

/**
 * Classe SimulationButtonPane qui agence les boutons controllant les états de la simulation
 *
 * @author n-o-o-d-l-e
 * @author MeriBouisri
 */
public class SimulationButtonPane extends JPanel implements WEventComponent, Reinitializable {

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** Bouton de démarrage de la simulation */
  private JToggleButton startButton;

  /** Bouton prochaine image de la simulation */
  private JButton nextButton;

  /** Bouton de réinitialisation de la simulation */
  private JButton reinitButton;

  /** Taille d'un bouton */
  private static Dimension BUTTON_SIZE = new Dimension(60, 60);

  /** Identifiant des abonnements */
  private int subscriptionID;

  /** Constructeur par defaut. */
  public SimulationButtonPane() {}

  /**
   * Constructeur de la classe SimulationButtonPane Les boutons de cette classe s'occupent des états
   * de la simulation
   *
   * @param controller le {@code Controller} de l'application
   */
  public SimulationButtonPane(Controller controller) {
    this.controller = controller;
    this.subscriptionID = this.hashCode();

    setLayout(new MigLayout("ins 5, gap 5", "[][][]", "[]"));

    startButton = new JToggleButton();
    setButtonSize(startButton);
    nextButton = new JButton();
    setButtonSize(nextButton);
    reinitButton = new JButton();
    setButtonSize(reinitButton);

    try {
      FlatSVGIcon startIcon = new FlatSVGIcon("icons/play_pause.svg", 1.75f);
      FlatSVGIcon nextIcon = new FlatSVGIcon("icons/next_frame.svg", 1.75f);
      FlatSVGIcon reinitIcon = new FlatSVGIcon("icons/restart.svg", 1.75f);

      startIcon.setColorFilter(new ColorFilter(color -> UIManager.getColor("Button.foreground")));
      nextIcon.setColorFilter(new ColorFilter(color -> UIManager.getColor("Button.foreground")));
      reinitIcon.setColorFilter(new ColorFilter(color -> UIManager.getColor("Button.foreground")));

      startButton.setIcon(startIcon);
      nextButton.setIcon(nextIcon);
      reinitButton.setIcon(reinitIcon);

      startButton.setToolTipText(
          LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.start"));
      nextButton.setToolTipText(
          LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.next_step"));
      reinitButton.setToolTipText(
          LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.reset"));
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (controller.getSimulationModel().isRunning()) {
      startButton.setSelected(true);
    }

    add(startButton);
    add(nextButton, "cell 1 0");
    add(reinitButton, "cell 2 0");

    putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanel");

    setupActionListeners();
    setupSubscribers();
  }

  /** {@inheritDoc} */
  @Override
  public void setupSubscribers() {
    this.controller
        .getSimulationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_SIMULATION_STATE_CHANGED,
            msg -> {
              startButton.setSelected((Boolean) msg);
            },
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
    this.controller.getSimulationModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
  }

  /**
   * Methode permettant de definir la taille des boutons.
   *
   * @param button le bouton a redimensionner
   */
  private void setButtonSize(AbstractButton button) {
    button.setPreferredSize(BUTTON_SIZE);
    button.setMinimumSize(BUTTON_SIZE);
    button.setMaximumSize(BUTTON_SIZE);
  }

  /** Methode permettant de definir les actions des boutons de controle de la simulation. */
  private void setupActionListeners() {
    startButton.addActionListener(
        e -> {
          if (startButton.isSelected()) {
            controller.getSimulationModel().onStartSimulation();
          } else {
            controller.getSimulationModel().onStopSimulation();
          }
        });

    nextButton.addActionListener(
        e -> {
          controller.getSimulationModel().onNextStepSimulation();
          startButton.setSelected(false);
        });

    reinitButton.addActionListener(e -> controller.getSimulationModel().onRestartSimulation());
  }
}
