package com.e24.wolke.backend.models.editor.tools;

import com.e24.wolke.filesystem.properties.SettingsPropertiesManager;
import java.awt.BasicStroke;
import java.awt.Color;

/**
 * Classe regroupant les constantes liees aux outils du mode editeur de l'application.
 *
 * @author MeriBouisri
 * @author Nilon123456789
 */
public class WToolConstants {

  /** Le {@code ToolType} par defaut */
  public static final ToolType DEFAULT_TOOL_TYPE =
      ToolType.fromIndex(
          SettingsPropertiesManager.INSTANCE.getIntProperty("editor.tools.defaultTool"));

  /** La couleur de dessin par defaut */
  public static final Color DEFAULT_COLOR =
      SettingsPropertiesManager.INSTANCE.getColorProperty("editor.tools.defaultColor");

  /** L'index de l'épaisseur de ligne par defaut */
  public static final int DEFAULT_LINE_THICKNESS_INDEX =
      SettingsPropertiesManager.INSTANCE.getIntProperty("editor.tools.lines.defaultWidthIndex");

  /** Les epaisseurs de lignes de dessin possibles */
  public static final int[] LINE_THICKNESS =
      SettingsPropertiesManager.INSTANCE.getIntArrayProperty("editor.tools.lines.width");

  /** L'épaisseur de ligne par defaut */
  public static final int DEFAULT_LINE_THICKNESS = LINE_THICKNESS[DEFAULT_LINE_THICKNESS_INDEX];

  /** La tete des lignes de dessin par defaut */
  public static final int DEFAULT_STROKE_CAP = BasicStroke.CAP_ROUND;

  /** Le biseau des lignes de dessin par defaut */
  public static final int DEFAULT_STROKE_BEVEL = BasicStroke.JOIN_BEVEL;

  /** Classe non instanciable */
  private WToolConstants() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * La classe enum {@code ToolTypes} regroupe les types d'outils de dessin dans le mode editeur de
   * l'application
   *
   * @author MeriBouisri
   * @author Nilon123456789
   */
  public enum ToolType {
    /** Aucun outil */
    NONE,
    /** Outil pour dessiner des pixels */
    PENCIL,
    /** Outil pour effacer des pixels */
    ERASER,
    /** Outil pour dessiner une ellipse */
    ELLIPSE,
    /** Outil pour dessiner un rectangle */
    RECTANGLE,
    /** Outil pour dessiner un polygone */
    POLYGON,
    /** Outil pour dessiner une image */
    IMAGE;

    /**
     * Retourne le type d'outil correspondant a l'index
     *
     * @param index L'index du type d'outil
     * @return Le type d'outil correspondant a l'index ou {@code ToolType.NONE} si l'index n'existe
     *     pas
     */
    public static ToolType fromIndex(int index) {
      for (ToolType toolType : ToolType.values()) {
        if (toolType.ordinal() != index) continue;
        return toolType;
      }
      return ToolType.NONE;
    }
  }
}
