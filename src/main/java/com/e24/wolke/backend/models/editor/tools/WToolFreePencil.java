package com.e24.wolke.backend.models.editor.tools;

import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import java.awt.Graphics2D;

/**
 * La classe {@code WToolPencil} permet de dessiner un point sur un {@code BufferedImage}
 *
 * @author MeriBouisri
 */
public class WToolFreePencil extends WToolFree {

  /** Le {@code ToolType} de la classe */
  public static final ToolType TOOL_TYPE = ToolType.PENCIL;

  /** Construction d'un {@code WToolPencil} */
  public WToolFreePencil() {
    super(WToolFreePencil.TOOL_TYPE);
  }

  /** {@inheritDoc} */
  public void draw(Graphics2D g2d, int prevX, int prevY, int currentX, int currentY) {
    g2d.drawLine(prevX, prevY, currentX, currentY);
  }
}
