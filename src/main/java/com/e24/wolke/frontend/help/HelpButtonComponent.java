package com.e24.wolke.frontend.help;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventComponent;
import com.e24.wolke.utils.interfaces.Reinitializable;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.UIManager;

/**
 * Classe composant d'un bouton réutilisable permettant d'ouvrir le menu instructions à une certaine
 * page d'aide, associée au composant sur lequel il se retrouve.
 *
 * @author n-o-o-d-l-e
 */
public class HelpButtonComponent extends JButton implements WEventComponent, Reinitializable {

  /** Numéro de série de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** La taille du bouton */
  public static final Dimension BUTTON_SIZE = new Dimension(24, 24);

  /** Le chemin de l'icône du bouton */
  private final String ICON_PATH = "icons/help.svg";

  /** L'icône du bouton */
  private final FlatSVGIcon ICON;

  /** Identifiant des abonnements */
  private int subscriptionID;

  /**
   * Créer un bouton d'aide associé à une page d'aide
   *
   * @param controller Le {@code Controller} de l'application
   * @param key La clé de la page à laquelle naviguer dans le menu instructions après un clic
   */
  public HelpButtonComponent(Controller controller, String key) {
    this.controller = controller;
    this.subscriptionID = this.hashCode();

    this.setMinimumSize(BUTTON_SIZE);
    this.setMaximumSize(BUTTON_SIZE);
    this.setPreferredSize(BUTTON_SIZE);

    ICON = new FlatSVGIcon(ICON_PATH, 0.75f);
    ICON.setColorFilter(new ColorFilter(color -> UIManager.getColor("Button.foreground")));
    this.setIcon(ICON);

    setButtonVisibility(controller.getApplicationModel().getHelpButtonVisibility());

    setToolTipText(LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.help"));

    this.addActionListener(
        e ->
            controller
                .getApplicationModel()
                .getPublisher()
                .publish(Subject.ON_COMPONENT_HELP_BUTTON_PRESSED, key));

    setupSubscribers();
  }

  /**
   * Mettre à jour la visibilité du bouton
   *
   * @param visible {@code true} pour rendre le bouton visible, {@code false} pour le rendre
   *     invisible
   */
  private void setButtonVisibility(boolean visible) {
    this.setVisible(visible);
  }

  /** {@inheritDoc} */
  @Override
  public void setupSubscribers() {
    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_APP_HELP_BUTTON_VISIBILITY_CHANGED,
            e -> setButtonVisibility((boolean) e),
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
    controller.getApplicationModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
  }
}
