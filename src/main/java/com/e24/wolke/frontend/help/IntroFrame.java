package com.e24.wolke.frontend.help;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.eventsystem.Subject;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.geom.RoundRectangle2D;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 * La classe {@code IntroFrame} sert à afficher la fenêtre d'introduction de l'application.
 *
 * <p>La fenêtre d'introduction est affichée lors du lancement de l'application.
 *
 * @author Nilon123456789
 */
public class IntroFrame extends JFrame {

  /** Serial version UID */
  private static final long serialVersionUID = 1L;

  /** Taille initiale */
  private static final Dimension INITIAL_SIZE = new Dimension(300, 400);

  /** Espace entre les composants */
  private final int GAP = 10;

  /** Largeur de l'arc */
  private final int ARC_WIDTH = 50;

  /** Controlleur */
  private Controller controller;

  /** Panneau de contenue */
  private JPanel contentPane;

  /** Logo de l'application */
  private JLabel logoLabel;

  /** Paneau du texte */
  private JTextPane textPane;

  /** Bouton continuer */
  private JButton continueButton;

  /** Bouton instructions */
  private JButton instructionsButton;

  /**
   * Constructeur de la classe {@code IntroFrame}
   *
   * @param controller Le {@code Controller} de l'application
   */
  public IntroFrame(Controller controller) {
    super("Wolke");
    setIconImages(ApplicationConstants.ICONS);
    setResizable(false);
    setSize(IntroFrame.INITIAL_SIZE);
    setLocationRelativeTo(this.rootPane);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setAlwaysOnTop(true);
    setUndecorated(true);
    setShape(
        new RoundRectangle2D.Double(
            0,
            0,
            IntroFrame.INITIAL_SIZE.getWidth(),
            IntroFrame.INITIAL_SIZE.getHeight(),
            ARC_WIDTH,
            ARC_WIDTH));

    this.controller = controller;

    this.contentPane = new JPanel();
    this.contentPane.setLayout(new BorderLayout(0, 0));
    this.setContentPane(this.contentPane);

    ImageIcon logo =
        new ImageIcon(
            Thread.currentThread().getContextClassLoader().getResource("icons/wolke-logo-128.png"));
    this.logoLabel = new JLabel(logo);
    this.contentPane.add(this.logoLabel, BorderLayout.NORTH);

    this.textPane = new JTextPane();
    this.textPane.setEditable(false);
    this.textPane.setHighlighter(null);
    this.textPane.setFocusable(false);
    this.textPane.setAlignmentX(JTextField.CENTER);
    this.textPane.setMargin(new Insets(GAP, GAP * 2, GAP, GAP * 2));
    this.textPane.setContentType("text/html");

    ResourceBundle bundle = LocaleManager.getLocaleResourceBundle();
    this.textPane.setText(
        "<h1 style=\"text-align: center; margin-bottom: 0;\">"
            + bundle.getString("ui.intro.title")
            + "</h1>"
            + "<h4 style=\"text-align: center; font-weight: lighter; margin-bottom: 0;\">"
            + bundle.getString("ui.intro.version")
            + bundle.getString("version")
            + "</h4>"
            + "<h4 style=\"text-align: center; font-weight: lighter; margin-top: 0;\">"
            + bundle.getString("ui.intro.java.version")
            + System.getProperty("java.version")
            + "</h4><p style=\"text-align: center; margin: 4;\">"
            + bundle.getString("ui.intro.body.1")
            + "</p><p style=\"text-align: center; margin-top: 0;\">"
            + bundle.getString("ui.intro.body.2")
            + "</p><h3 style=\"text-align: center; margin-top: 15;\">"
            + bundle.getString("ui.intro.greating")
            + "</h3>");
    this.contentPane.add(this.textPane, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    this.contentPane.add(buttonPanel, BorderLayout.SOUTH);

    this.continueButton = new JButton(bundle.getString("ui.intro.start"));
    this.continueButton.setDefaultCapable(false);
    this.continueButton.addActionListener(e -> this.dispose());
    buttonPanel.add(this.continueButton);

    buttonPanel.add(new JLabel("  "));

    this.instructionsButton = new JButton(bundle.getString("ui.intro.instructions"));
    this.instructionsButton.setDefaultCapable(true);
    this.instructionsButton.addActionListener(
        e -> {
          controller
              .getApplicationModel()
              .getPublisher()
              .publish(Subject.KEYBIND_SHOW_INSTRUCTIONS_PRESSED, null);
          this.dispose();
        });
    buttonPanel.add(this.instructionsButton);

    this.rootPane.setDefaultButton(this.instructionsButton);
  }

  /** {@inheritDoc} */
  @Override
  public void dispose() {
    this.controller
        .getApplicationModel()
        .getPublisher()
        .publish(Subject.ON_INTRO_FRAME_CLOSED, null);

    super.dispose();
  }
}
