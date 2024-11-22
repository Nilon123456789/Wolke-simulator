package com.e24.wolke.frontend.input;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.frontend.help.HelpButtonComponent;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

/**
 * Composant abstrait de saisie de données avec options pour le cacher.
 *
 * @author adrienles
 */
public class AbstractInputPane extends JPanel {

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application */
  protected Controller controller;

  /** La clé pour aller chercher le titre du panneau */
  protected String titleLocaleKey;

  /** Case à cocher pour activer ou non l'affichage du panneau de propriétés */
  protected JCheckBox chckbxShow;

  /** Contraintes de disposition du panneau */
  protected static final String LAYOUT_CONSTRAINTS = "ins 5, gap 5";

  /** Hauteur du panneau instancié */
  protected Dimension componentDimension;

  /** Hauteur minimale/minimisée du panneau */
  protected static final int MINIMIZED_HEIGHT = 35;

  /** Hauteur maximale que peut avoir le panneau */
  protected int maxComponentHeight;

  /** Largeur d'une structure de taille petite */
  protected static final int SMALL_STRUCT_WIDTH = 5;

  /** Largeur d'une structure de taille moyenne */
  protected static final int MEDIUM_STRUCT_WIDTH = 35;

  /** Dimension préférée du panneau */
  public static final Dimension PREFERRED_DIMENSION = new Dimension(300, 623);

  /** Le {@code MigLayout} du panneau */
  protected final MigLayout PANEL_LAYOUT = new MigLayout(LAYOUT_CONSTRAINTS, "[grow]", "[]");

  /** La clé pour aller chercher l'aide du panneau */
  protected String helpKey;

  /** Composant du bouton d'aide */
  protected HelpButtonComponent helpButtonComponent;

  /**
   * Constructeur de la classe AbstractInputPane initisalisant le panneau maître avec le bouton pour
   * l'afficher et le cacher
   *
   * @param controller Le {@code Controller} de l'application
   * @param titleLocaleKey La clé pour aller chercher le titre du panneau
   * @param helpKey La clé pour aller chercher l'aide du panneau
   */
  public AbstractInputPane(Controller controller, String titleLocaleKey, String helpKey) {
    this.controller = controller;
    this.titleLocaleKey = titleLocaleKey;
    this.helpKey = helpKey;

    addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            updateSize();
          }
        });

    putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanel");
    setPreferredSize(PREFERRED_DIMENSION);
    setSize(PREFERRED_DIMENSION.width, PREFERRED_DIMENSION.height);

    setLayout(PANEL_LAYOUT);

    add(createTitlePanel(), "grow");
  }

  /**
   * Crée le panneau de titre du panneau de propriétés avec la case à cocher pour afficher ou non
   * ses paramètres
   *
   * @return Le panneau de titre
   */
  private JPanel createTitlePanel() {
    JPanel panelTitle = new JPanel();
    panelTitle.setLayout(new BoxLayout(panelTitle, BoxLayout.X_AXIS));

    JLabel lblProperties =
        new JLabel(LocaleManager.getLocaleResourceBundle().getString(titleLocaleKey));
    lblProperties.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
    panelTitle.add(lblProperties);

    panelTitle.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));
    helpButtonComponent = new HelpButtonComponent(controller, helpKey);
    panelTitle.add(helpButtonComponent);
    panelTitle.add(Box.createVerticalStrut(HelpButtonComponent.BUTTON_SIZE.height));
    panelTitle.add(Box.createGlue());

    chckbxShow =
        new JCheckBox(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.show_properties.lbl"));
    chckbxShow.setHorizontalTextPosition(SwingConstants.LEFT);
    chckbxShow.setSelected(true);
    chckbxShow.addActionListener(e -> handleShowProperties());
    chckbxShow.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.show_hide"));
    panelTitle.add(chckbxShow);

    panelTitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanelContents");

    return panelTitle;
  }

  /**
   * Gère l'affichage ou non des propriétés. Doit être {@code @Override} dans la classe qui {@code
   * extends} celle-ci, et doit appeler la méthode de cette class ({@code
   * super.handleShowProperties()} APRÈS le reste de son implémentation)
   */
  public void handleShowProperties() {
    if (chckbxShow.isSelected()) {
      setSize(
          PREFERRED_DIMENSION.width,
          getPreferredSize().height < maxComponentHeight
              ? getPreferredSize().height
              : maxComponentHeight);
    } else {
      setSize(PREFERRED_DIMENSION.width, MINIMIZED_HEIGHT);
    }
    controller
        .getApplicationModel()
        .getPublisher()
        .publish(Subject.ON_UI_INPUT_PANE_MINIMIZED, chckbxShow.isSelected());
  }

  /** Met à jour la taille du panneau */
  public void updateSize() {
    if ((getSize().height > MINIMIZED_HEIGHT)) {
      componentDimension = getSize();
    }
  }

  /**
   * Retourne vrai si le panneau est minimisé
   *
   * @return true si le panneau est minimisé
   */
  public boolean isMinimized() {
    return !chckbxShow.isSelected();
  }

  /**
   * Retourne la hauteur que devrait avoir le panneau minimisé
   *
   * @return La hauteur minimisée du panneau
   */
  public int getMinimisedHeight() {
    return MINIMIZED_HEIGHT;
  }

  /**
   * Définit la hauteur maximale que peut avoir le panneau, puisque cela dépend de la hauteur de la
   * fenêtre
   *
   * @param maxComponentHeight La hauteur maximale que peut avoir le panneau
   */
  public void setMaxComponentHeight(int maxComponentHeight) {
    this.maxComponentHeight = maxComponentHeight;
  }
}
