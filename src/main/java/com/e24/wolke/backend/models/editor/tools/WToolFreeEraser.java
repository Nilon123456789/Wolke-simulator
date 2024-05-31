package com.e24.wolke.backend.models.editor.tools;

import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;

/**
 * La classe {@code WToolEraser} permet d'effacer un point dur un {@code BufferedImage}
 *
 * @author MeriBouisri
 */
public class WToolFreeEraser extends WToolFree {

  /** Le {@code ToolType} de la classe */
  public static final ToolType TOOL_TYPE = ToolType.ERASER;

  /** Construction d'un {@code WToolEraser} */
  public WToolFreeEraser() {
    super(WToolFreeEraser.TOOL_TYPE);
  }

  /** {@inheritDoc} */
  @Override
  public void draw(Graphics2D g2d, int prevX, int prevY, int currentX, int currentY) {
    g2d.setComposite(AlphaComposite.Clear);
    g2d.drawLine(prevX, prevY, currentX, currentY);
    g2d.setComposite(AlphaComposite.SrcOver);
  }
}
