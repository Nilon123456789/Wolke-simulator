package com.e24.wolke.frontend;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.backend.models.application.ApplicationConstants.ApplicationTab;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.renderer.RendererConstants;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventComponent;
import com.e24.wolke.frontend.editor.EditorPane;
import com.e24.wolke.frontend.simulation.SimulationPane;
import com.e24.wolke.frontend.zen.ZenModePane;
import com.formdev.flatlaf.IntelliJTheme.ThemeLaf;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Locale;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Classe du cadre de la fenêtre principale de l'application
 *
 * @author adrienles
 * @author MeriBouisri
 */
public class MainFrame extends JFrame implements WEventComponent {

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Indique si c'est la première execution */
  private static boolean firstExecution = true;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** Cadre de contenu */
  private JPanel contentPane;

  /** Représente la barre de menu principale de l'application. */
  private MenuBar menuBar;

  /** Panneau tabulé pour l'éditeur et la simulation */
  private JTabbedPane tabbedPane;

  /** Panneau d'édition */
  private EditorPane editorPane;

  /** Panneau de simulation */
  private JPanel simulationPane;

  /** Panneau du mode zen */
  private ZenModePane zenModePane;

  /** Taille minimale de la fenêtre */
  private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(960, 560);

  /** Espace pour faire en sorte que la fenêtre ne soit pas collée aux bords de l'écran */
  private final int DISPLAY_GAP = 50;

  /** Panneau de verre */
  private JComponent glassPane =
      new JComponent() {
        private static final long serialVersionUID = 1L;

        @Override
        protected void paintComponent(Graphics g) {
          g.setColor(new Color(0f, 0f, 0f, 0.5f));
          g.fillRect(0, 0, getWidth(), getHeight());
        }
      };

  /** L'identifiant des abonnements */
  private int subscriptionID = hashCode();

  /**
   * Créer le cadre
   *
   * @param controller le {@code Controller} de l'application
   */
  public MainFrame(Controller controller) {
    this.controller = controller;

    setIconImages(ApplicationConstants.ICONS);

    setMinimumSize(MINIMUM_WINDOW_SIZE);
    setMaximumSize(getToolkit().getScreenSize());
    if (ApplicationConstants.DEFAULT_WINDOW_SIZE.getWidth() < MINIMUM_WINDOW_SIZE.getWidth()
        || ApplicationConstants.DEFAULT_WINDOW_SIZE.getHeight() < MINIMUM_WINDOW_SIZE.getHeight()) {
      setSize(MINIMUM_WINDOW_SIZE);
    } else {
      Dimension screenSize = getToolkit().getScreenSize();
      if (screenSize.getWidth() < ApplicationConstants.DEFAULT_WINDOW_SIZE.getWidth()
          || screenSize.getHeight() < ApplicationConstants.DEFAULT_WINDOW_SIZE.getHeight()) {
        setSize(screenSize.width, screenSize.height - DISPLAY_GAP);
      } else {
        setSize(ApplicationConstants.DEFAULT_WINDOW_SIZE);
      }
    }

    setGlassPane(glassPane);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    if (firstExecution) setEnabled(false);
    else setEnabled(true);
    firstExecution = false;

    setLocationRelativeTo(null);
    initializeComponents();

    if (RendererConstants.USE_OPENGL) {
      JOptionPane.showMessageDialog(
          null,
          LocaleManager.getLocaleResourceBundle().getString("ui.opengl_warning.body"),
          LocaleManager.getLocaleResourceBundle().getString("ui.opengl_warning.title"),
          JOptionPane.INFORMATION_MESSAGE);
    }

    setupSubscribers();

    handleThemeChange(controller.getApplicationModel().getCurrentTheme().getName());
  }

  /** Initialise les composants de la fenêtre principale. */
  private void initializeComponents() {

    menuBar = new MenuBar(controller);

    simulationPane = new SimulationPane(controller);

    editorPane = new EditorPane(controller);

    zenModePane = new ZenModePane(controller);

    setTitle(LocaleManager.getLocaleResourceBundle().getString("ui.title"));

    setJMenuBar(this.menuBar);

    this.contentPane = new JPanel();

    setContentPane(this.contentPane);
    this.contentPane.setLayout(new GridLayout(1, 0, 0, 0));

    this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    this.contentPane.add(this.tabbedPane);

    this.tabbedPane.addTab(
        LocaleManager.getLocaleResourceBundle().getString("ui.panel.simulation.title"),
        null,
        this.simulationPane,
        null);
    this.tabbedPane.addTab(
        LocaleManager.getLocaleResourceBundle().getString("ui.panel.editor.title"),
        null,
        this.editorPane,
        null);
    if (RendererConstants.USE_OPENGL) {
      this.tabbedPane.setEnabledAt(1, false);
    } else {
      this.tabbedPane.setSelectedIndex(ApplicationConstants.DEFAULT_TAB.getIndex());
    }
    this.tabbedPane.addChangeListener(
        e -> {
          handleTabChange();
        });
  }

  /**
   * Methode permettant de gérer le changement de visibilité du mode zen
   *
   * @param isVisible {@code true} si le mode zen est visible, {@code false} sinon
   */
  private void handleZenModeVisibilityChange(boolean isVisible) {
    if (isVisible) {
      setExtendedState(JFrame.MAXIMIZED_BOTH);
      this.contentPane.remove(this.tabbedPane);
      this.contentPane.add(this.zenModePane);
      this.zenModePane.revalidate();
    } else {
      this.contentPane.remove(this.zenModePane);
      this.contentPane.add(this.tabbedPane);
      this.tabbedPane.updateUI();
    }
  }

  /** Methode permettant de gérer le changement d'onglet */
  private void handleTabChange() {
    ApplicationTab tab = ApplicationTab.SIMULATION;
    if (this.tabbedPane.getSelectedIndex() == 1) {
      tab = ApplicationTab.EDITOR;
    }

    controller.getApplicationModel().setCurrentTab(tab);
  }

  /**
   * Methode permettant de reinitialiser cette instance
   *
   * @return nouveau {@code MainFrame} reinitialise a partir de cette instance
   */
  public MainFrame reinitialize() {
    removeComponentSubscribers();
    dispose();
    return new MainFrame(controller);
  }

  /**
   * Change la langue de l'application.
   *
   * @param locale la nouvelle locale
   */
  protected void changeLanguage(Locale locale) {
    this.setLocale(locale);

    removeComponentSubscribers();

    this.initializeComponents();
    this.tabbedPane.setSelectedIndex(controller.getApplicationModel().getCurrentTab().getIndex());
  }

  /**
   * Méthode se chargeant de mettre à jour l'interface suite à un changement de thème
   *
   * @param newThemeClass La classe du nouveau thème à appliquer
   */
  private void handleThemeChange(String newThemeClass) {
    try {
      UIManager.setLookAndFeel(newThemeClass);
    } catch (ClassNotFoundException
        | InstantiationException
        | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
    ThemeLaf.updateUI();
    removeComponentSubscribers();
    initializeComponents();
    this.tabbedPane.setSelectedIndex(controller.getApplicationModel().getCurrentTab().getIndex());
  }

  /** {@inheritDoc} */
  @Override
  public void setupSubscribers() {
    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_INTRO_FRAME_CLOSED,
            msg -> {
              setEnabled(true);
              getGlassPane().setVisible(false);
              toFront();
            },
            subscriptionID);
    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_APP_LANGUAGE_LOCALE_CHANGED,
            msg -> changeLanguage((Locale) msg),
            subscriptionID);
    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_APP_TAB_CHANGED,
            msg -> tabbedPane.setSelectedIndex(((ApplicationTab) msg).getIndex()),
            subscriptionID);
    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_APP_ZEN_MODE_VISIBILITY_CHANGED,
            msg -> handleZenModeVisibilityChange((Boolean) msg),
            subscriptionID);
    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_APP_THEME_CHANGED, msg -> handleThemeChange((String) msg), subscriptionID);
  }

  /** Methode permettant de retirer les abonnements des composants de cette instance */
  public void removeComponentSubscribers() {
    editorPane.reinitialize();
    menuBar.reinitialize();
  }

  /** {@inheritDoc} */
  @Override
  public void removeSubscribers() {
    controller.getApplicationModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
  }
}
