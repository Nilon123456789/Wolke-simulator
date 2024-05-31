package com.e24.wolke.backend.models.editor.tools;

import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * La classe {@code WToolShapeRectangle} permet de dessiner un rectangle.
 *
 * @author MeriBouisri
 */
public class WToolShapeRectangle extends WToolShape {

  /** Le {@code ToolType} de cet outil */
  public static final ToolType TOOL_TYPE = ToolType.RECTANGLE;

  /** Construction d'un {@code WToolShapePolygon} */
  public WToolShapeRectangle() {
    super(WToolShapeRectangle.TOOL_TYPE);
  }

  /** {@inheritDoc} */
  @Override
  public Shape createShape(int x, int y, int width, int height) {
    return new Rectangle2D.Double(x, y, width, height);
  }
}
