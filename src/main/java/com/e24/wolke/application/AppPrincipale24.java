package com.e24.wolke.application;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.filesystem.WUserFileManager;
import com.e24.wolke.filesystem.scenes.WSceneConstants;
import com.e24.wolke.frontend.MainFrame;
import com.e24.wolke.frontend.help.IntroFrame;
import com.formdev.flatlaf.FlatLaf;
import java.awt.EventQueue;
import java.awt.Taskbar;
import java.io.File;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Classe d'exécution du simulateur Wolke
 *
 * @author MeriBouisri
 * @author adrienles
 * @author Nilon123456789
 */
public class AppPrincipale24 {

  /** Le {@code Controller} de l'application */
  public static Controller controller;

  /** Le {@code MainFrame} de l'application */
  public static MainFrame frame;

  /** Classe non instanciable */
  private AppPrincipale24() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Main
   *
   * @param args Arguments de la ligne de commande
   */
  public static void main(String[] args) {
    AppPrincipale24.setupLocalization();
    FlatLaf.registerCustomDefaultsSource("themes");
    AppPrincipale24.setupInitialTheme();
    WUserFileManager.copyResourcesToUserFolder();
    setupMacOSIntricacies();
    EventQueue.invokeLater(
        new Runnable() {
          public void run() {
            try {
              controller = new Controller();
              frame = new MainFrame(controller);
              frame.setVisible(true);
              if (args.length > 0 && args[0].endsWith(WSceneConstants.WOLKE_EXTENSION)) {
                controller.getSceneHandler().importScene(new File(args[0]));
              }
              frame.getGlassPane().setVisible(true);
              new IntroFrame(controller).setVisible(true);

              controller
                  .getApplicationModel()
                  .getSubscriber()
                  .subscribe(Subject.ON_APP_REINITIALIZE, msg -> AppPrincipale24.reinitialize());
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  /**
   * Méthode initialisant les particularités de l'application de l'icone et du style de la barre de
   * menu de l'application sur Mac
   */
  public static void setupMacOSIntricacies() {
    try {
      final Taskbar taskbar = Taskbar.getTaskbar();
      taskbar.setIconImage(ApplicationConstants.ICONS.get(ApplicationConstants.ICONS.size() - 1));
      System.setProperty("apple.laf.useScreenMenuBar", "true");
    } catch (final UnsupportedOperationException e) {
      // Inutile de gérer cette exception, puisqu'elle fait juste indiquer que l'application n'a pas
      // été ouverte sur un Mac
    } catch (final SecurityException e) {
      System.out.println(
          "Il y a eu une exception de sécurité pour 'taskbar.setIconImage' " + e.getMessage());
    }
  }

  /** Méthode intiialisant le thème (LAF) de l'application par défaut */
  public static void setupInitialTheme() {
    try {
      UIManager.setLookAndFeel(ApplicationConstants.DEFAULT_THEME.getName());
    } catch (ClassNotFoundException
        | InstantiationException
        | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
  }

  /** Methode permettant d'initialiser le {@code LocaleManager} avec la langue par defaut */
  public static void setupLocalization() {
    LocaleManager.update(ApplicationConstants.DEFAULT_LOCALE);
  }

  /** Methode permettant de reinitialiser le {@code Controller} et le {@code MainFrame}. */
  public static void reinitialize() {
    controller.reinitialize();
    frame = frame.reinitialize();

    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribe(Subject.ON_APP_REINITIALIZE, msg -> AppPrincipale24.reinitialize());

    frame.setVisible(true);
  }
}
