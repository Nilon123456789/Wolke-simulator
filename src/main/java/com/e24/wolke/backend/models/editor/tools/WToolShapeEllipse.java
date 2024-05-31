package com.e24.wolke.backend.models.editor.tools;

import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * La classe {@code WToolShapeEllipse} permet de dessiner une ellipse.
 *
 * @author MeriBouisri
 */
public class WToolShapeEllipse extends WToolShape {

  /** Le {@code ToolType} de cet outil */
  public static final ToolType TOOL_TYPE = ToolType.ELLIPSE;

  /** Construction d'un {@code WToolShapeEllipse} */
  public WToolShapeEllipse() {
    super(WToolShapeEllipse.TOOL_TYPE);
  }

  /** {@inheritDoc} */
  @Override
  public Shape createShape(int x, int y, int width, int height) {
    return new Ellipse2D.Double(x, y, width, height);
  }
}
