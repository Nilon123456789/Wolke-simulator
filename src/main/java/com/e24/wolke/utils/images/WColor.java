package com.e24.wolke.utils.images;

import java.awt.Color;
import org.apache.logging.log4j.Level;

/**
 * Classe regroupant les utilitaires de couleur.
 *
 * @author adrienles
 * @author Nilon123456789
 * @author MeriBouisri
 */
public class WColor {

  /** Classe non instanciable */
  private WColor() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Interpoler une couleur HSB en fonction de deux couleurs, d'une valeur minimale et maximale,
   * ainsi que la valeur à interpoler entre ces deux valeurs min et max. L'algorithme
   * d'interpolation est basé sur la méthode de l'interpolation de la teinte HSB
   *
   * @see <a href=
   *     "http://www.java2s.com/example/java-utility-method/color-interpolate/interpolate-color-start-color-end-float-p-11dfb.html">Java2S
   *     : Interpolate color start color end float p</a>
   * @param startHSB La couleur de départ
   * @param endHSB La couleur de fin
   * @param value La valeur à interpoler
   * @param min La valeur minimale
   * @param max La valeur maximale
   * @return La couleur interpolée
   */
  // HuC DI (KNAW)

  public static int interpolateHSBColorFromMinMax(
      float[] startHSB, float[] endHSB, double value, double min, double max) {
    // float ratio = calculateRatio(value, min, max);
    // float hue = ((endHSB[0] - startHSB[0]) * ratio) + startHSB[0];
    // float saturation = (startHSB[1] + endHSB[1]) / 2;
    // float brightness = (startHSB[2] + endHSB[2]) / 2;

    // if (value < min || value > max) {
    //   System.out.println("ERROR: Value out of bounds: " + value + " " + min + " " + max);
    // }
    // if (value < min) {
    //   value = min;
    // } else if (value > max) {
    //   value = max;
    // }

    return Color.HSBtoRGB(
        ((endHSB[0] - startHSB[0]) * (float) ((value - min) / (max - min))) + startHSB[0],
        (startHSB[1] + endHSB[1]) / 2,
        (startHSB[2] + endHSB[2]) / 2);
  }

  /**
   * Methode utilitaire permettant de convertir un {@code Color} en ses composants HSB
   *
   * @param color Le {@code Color} a convertir
   * @return Les composants HSB de la couleur
   */
  public static float[] toHSB(Color color) {
    return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
  }

  /**
   * Interpoler une nuance de gris en en fonction d'une valeur et de deux valeurs min et max.
   *
   * @param value La valeur à interpoler
   * @param min La valeur minimale
   * @param max La valeur maximale
   * @return La couleur interpolée en nuance de gris
   */
  public static int interpolateGrayscaleFromMinMax(double value, double min, double max) {
    int gray = (int) ((value - min) / (max - min) * 255);
    if (gray > 255) {
      gray = 255;
    } else if (gray < 0) {
      gray = 0;
    }
    return new Color(gray, gray, gray).getRGB();
  }

  /**
   * Listes de couleurs avec leur code ansi et html
   *
   * @author Nilon123456789
   */
  public enum WColorType {
    /** Noire */
    BLACK("\u001b[30m", "black"),
    /** Rouge */
    RED("\u001b[31m", "DarkRed"),
    /** Vert */
    GREEN("\u001b[32m", "Lime"),
    /** Jaune */
    YELLOW("\u001b[33m", "#FFD700"),
    /** Bleu */
    BLUE("\u001b[34m", "SkyBlue"),
    /** Violet */
    PURPLE("\u001b[35m", "Purple"),
    /** Cyan */
    CYAN("\u001b[36m", "MediumTurquoise"),
    /** Blanc */
    WHITE("\u001b[37m", "GhostWhite"),
    /** Gris */
    BRIGHT_BLACK("\u001b[1;30m", "gray"),
    /** Rouge clair */
    BRIGHT_RED("\u001b[1;31m", "Red"),
    /** Vert clair */
    BRIGHT_GREEN("\u001b[1;32m", "Green"),
    /** Jaune clair */
    BRIGHT_YELLOW("\u001b[1;33m", "Yellow"),
    /** Bleu clair */
    BRIGHT_BLUE("\u001b[1;34m", "Blue"),
    /** Violet clair */
    BRIGHT_PURPLE("\u001b[1;35m", "RebeccaPurple"),
    /** Cyan clair */
    BRIGHT_CYAN("\u001b[1;36m", "cyan"),
    /** Blanc clair */
    BRIGHT_WHITE("\u001b[1;37m", "White"),
    /** Réinitialisation de la couleur */
    DEFAULT("\u001b[m", null);

    /** Code de la couleur ansi */
    private final String ansiCode;

    /** code html de la couleur */
    private final String colorName;

    /**
     * Constructeur de l'énumération
     *
     * @param ansiCode code ansi
     * @param colorName code html
     */
    WColorType(String ansiCode, String colorName) {
      this.ansiCode = ansiCode;
      this.colorName = (colorName == null) ? "</font>" : "<font color=\"" + colorName + "\">";
    }

    /**
     * Récupérer le code ansi
     *
     * @return code ansi
     */
    public String getAnsiCode() {
      return this.ansiCode;
    }

    /**
     * Récupérer le code html
     *
     * @return code html
     */
    public String getColorName() {
      return this.colorName;
    }

    /**
     * Retourne la couleur en fonction du paramètre (ansi ou html)
     *
     * @param color string de la couleur
     * @param isAnsi true si c'est un code ansi, false si c'est un code html
     * @return code couleur
     */
    public static String getColor(String color, boolean isAnsi) {
      for (WColorType c : WColorType.values()) {
        if (isAnsi) {
          if (c.getAnsiCode().equals(color)) return c.getColorName();
        } else {
          if (c.getColorName().equals(color)) return c.getAnsiCode();
        }
      }
      return null;
    }
  }

  /**
   * Retourne le niveau de log formaté en html avec la couleur associée
   *
   * @param level niveau de log
   * @return le niveau de log formaté en html
   */
  public static String getLogHtmlColor(Level level) {
    StringBuilder sb = new StringBuilder();

    if (Level.DEBUG.equals(level)) {
      sb.append(WColorType.CYAN.getColorName());
    } else if (Level.INFO.equals(level)) {
      sb.append(WColorType.GREEN.getColorName());
    } else if (Level.WARN.equals(level)) {
      sb.append(WColorType.YELLOW.getColorName());
    } else if (Level.ERROR.equals(level)) {
      sb.append(WColorType.RED.getColorName());
    } else if (Level.FATAL.equals(level)) {
      sb.append(WColorType.PURPLE.getColorName());
    } else {
      sb.append(WColorType.DEFAULT.getColorName());
    }

    sb.append(level.toString());
    sb.append(WColorType.DEFAULT.getColorName());

    return sb.toString();
  }
}
