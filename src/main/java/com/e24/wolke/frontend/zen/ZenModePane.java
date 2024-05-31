package com.e24.wolke.frontend.zen;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.frontend.canvas.ZenModeCanvasPane;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Panneau du mode zen
 *
 * @author n-o-o-d-l-e
 */
public class ZenModePane extends JPanel {

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code ZenModeCanvasPane} du mode zen */
  private ZenModeCanvasPane zenModeCanvasPane;

  /** Bouton de sortie du mode zen */
  private JButton btnExitZenMode;

  /** Taille d'un bouton */
  private static final Dimension EXIT_BUTTON_SIZE = new Dimension(40, 40);

  /** Espace entre les composants */
  private static final int GAP = 10;

  /** Constructeur par defaut */
  public ZenModePane() {}

  /**
   * Créer le panneau du mode zen
   *
   * @param controller Le {@code Controller} de l'application
   */
  public ZenModePane(Controller controller) {
    zenModeCanvasPane = new ZenModeCanvasPane(controller);

    setLayout(new BorderLayout(0, 0));

    JLayeredPane layeredPane = new JLayeredPane();
    layeredPane.setLayout(null);
    add(layeredPane, BorderLayout.CENTER);

    layeredPane.add(zenModeCanvasPane, JLayeredPane.DEFAULT_LAYER);

    FlatSVGIcon iconExit = new FlatSVGIcon("icons/fullscreen_exit.svg");
    iconExit.setColorFilter(new ColorFilter(color -> UIManager.getColor("Button.foreground")));

    btnExitZenMode = new JButton(iconExit);
    btnExitZenMode.setMinimumSize(EXIT_BUTTON_SIZE);
    btnExitZenMode.setMaximumSize(EXIT_BUTTON_SIZE);
    btnExitZenMode.setPreferredSize(EXIT_BUTTON_SIZE);
    btnExitZenMode.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.zen_mode"));
    btnExitZenMode.addActionListener(
        e ->
            controller
                .getApplicationModel()
                .getPublisher()
                .publish(Subject.ON_APP_ZEN_MODE_VISIBILITY_CHANGED, false));

    layeredPane.add(btnExitZenMode, JLayeredPane.MODAL_LAYER);

    addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            handleResize();
          }
        });
  }

  /** Gestion du redimensionnement du panneau */
  public void handleResize() {
    zenModeCanvasPane.setBounds(0, 0, getWidth(), getHeight());
    btnExitZenMode.setBounds(
        getWidth() - EXIT_BUTTON_SIZE.width - GAP,
        getHeight() - EXIT_BUTTON_SIZE.height - GAP,
        EXIT_BUTTON_SIZE.width,
        EXIT_BUTTON_SIZE.height);
  }
}
