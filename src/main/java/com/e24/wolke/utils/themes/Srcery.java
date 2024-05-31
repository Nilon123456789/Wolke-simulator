package com.e24.wolke.utils.themes;

import com.formdev.flatlaf.IntelliJTheme;
import java.io.IOException;
import javax.swing.UIManager;
import org.apache.logging.log4j.LogManager;

/**
 * Une classe représentant le thème <a href="https://srcery.sh/">Srcery</a>.
 *
 * <p>Code inspiré de {@link com.formdev.flatlaf.intellijthemes.FlatArcIJTheme}.
 *
 * @see UIManager
 * @see IntelliJTheme
 * @author Nilon123456789
 */
public class Srcery extends IntelliJTheme.ThemeLaf {
  /** Le nom du thème */
  public static final String NAME = "Srcery";

  /**
   * Installe le thème Srcery.
   *
   * @return {@code true} si le thème a été installé, {@code false} sinon
   */
  public static boolean setup() {
    try {
      return setup(new Srcery());
    } catch (RuntimeException ex) {
      return false;
    }
  }

  /** Installe les informations du thème. */
  public static void installLafInfo() {
    installLafInfo(NAME, Srcery.class);
  }

  /** Constructeur de la classe Srcery. */
  public Srcery() {
    super(loadTheme("/themes/srcery.theme.json"));
  }

  /**
   * Charge le thème.
   *
   * @param name Le nom du thème
   * @return Le thème chargé
   */
  private static IntelliJTheme loadTheme(String name) {
    try {
      return new IntelliJTheme(Srcery.class.getResourceAsStream(name));
    } catch (IOException ex) {
      LogManager.getLogger("Srcery").error("Failed to load theme {}", NAME);
      throw new RuntimeException("Failed to load theme Srcery", ex);
    }
  }

  /**
   * Retourne le nom du thème.
   *
   * @return Le nom du thème
   */
  @Override
  public String getName() {
    return NAME;
  }
}
