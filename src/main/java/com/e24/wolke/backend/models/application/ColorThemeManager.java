package com.e24.wolke.backend.models.application;

import com.e24.wolke.utils.BiHashMap;
import com.e24.wolke.utils.themes.Srcery;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes;
import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes.FlatIJLookAndFeelInfo;

/**
 * La classe {@code ColorThemeManager} permet de gerer le theme de l'application
 *
 * @author MeriBouisri
 * @author adrienles
 * @author Nilon123456789
 */
public class ColorThemeManager {
  /** Les themes disponibles */
  protected static final BiHashMap<Class<?>, String> availableThemes =
      new BiHashMap<Class<?>, String>();

  /** Si les themes ont ete initialises */
  private static boolean isInitialized;

  /** Constructeur privé pour empêcher l'instanciation de la classe */
  private ColorThemeManager() {
    throw new IllegalStateException("Utility class");
  }

  /** Méthode intiialisant tous les thèmes (LAF) disponibles */
  protected static void setupAvailableLAF() {
    if (ColorThemeManager.isInitialized) return;

    availableThemes.put(Srcery.class, "Srcery");
    availableThemes.put(FlatDarkLaf.class, "Flat Dark");
    availableThemes.put(FlatLightLaf.class, "Flat Light");
    for (FlatIJLookAndFeelInfo theme : FlatAllIJThemes.INFOS) {
      if (theme.getName().contains("(Material)")) continue;
      try {
        availableThemes.put(Class.forName(theme.getClassName()), theme.getName());
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Methode permettant de recuperer la classe selon le nom du theme
   *
   * @param name Le nom du theme, sensible a la case
   * @return La classe du theme
   */
  public static Class<?> getThemeClass(String name) {
    return ColorThemeManager.availableThemes.get2(name);
  }

  /**
   * Methode permettant de recuperer le nom selon la classe du theme
   *
   * @param theme La classe du theme
   * @return Le nom du theme
   */
  public static String getThemeName(Class<?> theme) {
    return ColorThemeManager.availableThemes.get1(theme);
  }
}
