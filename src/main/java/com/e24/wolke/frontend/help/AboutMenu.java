package com.e24.wolke.frontend.help;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.eventsystem.Subject;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;

/**
 * La classe {@code AboutMenu} sert à afficher le menu "À propos" de l'application.
 *
 * @author n-o-o-d-l-e
 */
public class AboutMenu extends JFrame {

  /** Numéro de série de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** Le panneau d'onglets de la fenêtre */
  private JTabbedPane tabbedPane;

  /** Le panneau d'informations de la fenêtre */
  private JPanel panelInfo;

  /** Le panneau de crédits de la fenêtre */
  private JPanel panelCredits;

  /** Le panneau de texte d'informations de la fenêtre */
  private JTextPane textPaneInfo;

  /** Le panneau de texte de crédits de la fenêtre */
  private JTextPane textPaneCredits;

  /** Le panneau de défilement pour le panneau d'informations */
  private JScrollPane scrollPaneInfo;

  /** Le panneau de défilement pour le panneau de crédits */
  private JScrollPane scrollPaneCredits;

  /** La taille minimale de la fenêtre */
  private static final Dimension MINIMUM_SIZE = new Dimension(400, 300);

  /** La taille initiale de la fenêtre */
  private static final Dimension INITIAL_SIZE = new Dimension(600, 500);

  /** L'écart entre les composants de la fenêtre */
  private static final int GAP = 10;

  /** Constructeur par défaut. */
  public AboutMenu() {}

  /**
   * Constructeur de la classe {@code AboutMenu}.
   *
   * @param controller Le {@code Controller} de l'application
   */
  public AboutMenu(Controller controller) {
    super(LocaleManager.getLocaleResourceBundle().getString("ui.help.about.title"));
    this.controller = controller;
    setIconImages(ApplicationConstants.ICONS);
    setMinimumSize(AboutMenu.MINIMUM_SIZE);
    setSize(AboutMenu.INITIAL_SIZE);
    setLocationRelativeTo(this.rootPane);
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    setAlwaysOnTop(true);

    this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    getContentPane().add(this.tabbedPane, BorderLayout.CENTER);

    this.panelInfo = new JPanel();
    this.panelInfo.setBorder(null);
    this.tabbedPane.addTab(
        LocaleManager.getLocaleResourceBundle().getString("ui.help.about.information"),
        null,
        this.panelInfo,
        null);
    this.panelInfo.setLayout(new BorderLayout(0, 0));

    this.scrollPaneCredits = new JScrollPane();
    this.scrollPaneCredits.setBorder(null);
    this.panelInfo.add(this.scrollPaneCredits);

    this.textPaneInfo = new JTextPane();
    this.textPaneInfo.setContentType("text/html");
    this.textPaneInfo.setBorder(new EmptyBorder(0, GAP, 0, GAP));
    this.textPaneInfo.setEditable(false);
    this.textPaneInfo.setFocusable(false);
    this.textPaneInfo.addHyperlinkListener(
        e -> {
          if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED) return;
          if (!Desktop.isDesktopSupported()) return;

          try {
            Desktop.getDesktop().browse(e.getURL().toURI());
          } catch (Exception error) {
            error.printStackTrace();
          }
        });

    this.textPaneInfo.setText(
        "<h1 style=\"text-align: center\"><a"
            + " href=\"https://github.com/Nilon123456789/Wolke-simulator/releases/tag/v"
            + LocaleManager.getLocaleResourceBundle().getString("version")
            + "\">"
            + " Wolke Simulator v"
            + LocaleManager.getLocaleResourceBundle().getString("version")
            + "</a></h1><h2>Projet made in the context of the 420-SCD class during the H2024"
            + " semester</h2><h2>Made by:</h2><ul><li><h3><a"
            + " href=\"https://github.com/Nilon123456789/\">Nilon123456789</a></h3></li><li><h3><a"
            + " href=\"https://github.com/MeriBouisri\">MeriBouisri</a></h3></li><li><h3><a"
            + " href=\"https://github.com/n-o-o-d-l-e\">n-o-o-d-l-e </a></h3></li></ul><h2>Made"
            + " over the course of three months</h2>");
    this.scrollPaneCredits.setViewportView(this.textPaneInfo);

    this.panelCredits = new JPanel();
    this.panelCredits.setBorder(null);
    this.tabbedPane.addTab(
        LocaleManager.getLocaleResourceBundle().getString("ui.help.about.credits"),
        null,
        this.panelCredits,
        null);
    this.panelCredits.setLayout(new BorderLayout(0, 0));

    this.scrollPaneInfo = new JScrollPane();
    this.scrollPaneInfo.setBorder(null);
    this.panelCredits.add(this.scrollPaneInfo);

    this.textPaneCredits = new JTextPane();
    this.textPaneCredits.setContentType("text/html");
    this.textPaneCredits.setBorder(new EmptyBorder(0, GAP, 0, GAP));
    this.textPaneCredits.setEditable(false);
    this.textPaneCredits.setFocusable(false);
    this.textPaneCredits.addHyperlinkListener(
        e -> {
          if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (Desktop.isDesktopSupported()) {
              try {
                Desktop.getDesktop().browse(e.getURL().toURI());
              } catch (Exception error) {
                error.printStackTrace();
              }
            }
          }
        });

    this.textPaneCredits.setText(
        "<h1 style=\"text-align: center\">Credits</h1><h2>Articles</h2><ul><li><h3><a"
            + " href=\"https://fileadmin.cs.lth.se/cs/Education/EDAN35/projects/13SoderlingStrandberg_Fluid.pdf\">Real-Time"
            + " Fluid Simulation on the GPU</a></h3></li><li><h3><a"
            + " href=\"https://developer.nvidia.com/gpugems/gpugems/part-vi-beyond-triangles/chapter-38-fast-fluid-dynamics-simulation-gpu\">Chapter"
            + " 38. Fast Fluid Dynamics Simulation on the GPU</a></h3></li></ul><li><h3><a"
            + " href=\"https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage\">How"
            + " do you clone a BufferedImage</a></h3></li><li><h3><a"
            + " href=\"http://www.java2s.com/example/java-utility-method/color-interpolate/interpolate-color-start-color-end-float-p-11dfb.html\">Interpolate"
            + " color start color end float p</a></h3></li></ul><h2>Icons and"
            + " images</h2><ul><li><h3><a href=\"https://fonts.google.com/icons\">Google"
            + " Icons</a></h3></li><li><h3><a"
            + " href=\"https://w.wiki/9vqM\">Airfoils</a></h3></li></ul><h2>Library"
            + " used</h2><ul><li><h3><a href=\"https://github.com/dheid/colorpicker\">Java Swing"
            + " Color Picker Dialog</a></h3></li></ul>");
    this.scrollPaneInfo.setViewportView(this.textPaneCredits);

    setupSubscribers();
  }

  /** Met en place les abonnements aux événements de l'application. */
  private void setupSubscribers() {
    this.controller
        .getApplicationModel()
        .getSubscriber()
        .subscribe(
            Subject.ON_APP_LANGUAGE_LOCALE_CHANGED,
            (locale) -> {
              setVisible(false);
            });
  }

  /**
   * Affiche ou cache le menu "À propos" de l'application en s'assurant qu'il soit centré par
   * rapport à l'écran de l'utilisateur.
   *
   * @param b {@code true} pour afficher le menu, {@code false} pour le cacher
   */
  @Override
  public void setVisible(boolean b) {
    super.setVisible(b);
    setLocationRelativeTo(this.rootPane);
  }
}
