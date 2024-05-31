package com.e24.wolke.backend.models.obstacles;

import com.e24.wolke.backend.models.simulation.SimulationConstants.OBSTACLE_TYPE;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester les methodes de {@code }
 *
 * @author MeriBouisri
 */
public class WObstacleCellMatrixTest {

  /** Methode permettant de tester {@code WObstacleCellMatrix#fromBufferedImage(BufferedImage)} */
  @Test
  public void testFromBufferedImage() {
    int w = 3;
    int h = 3;

    int x = 1;
    int y = 1;

    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    img.setRGB(1, 1, Color.BLACK.getRGB());

    WObstacleCellMatrix matrix = WObstacleCellMatrix.fromBufferedImage(img);

    Assert.assertEquals("", matrix.getXLength(), img.getWidth());
    Assert.assertEquals("", matrix.getYLength(), img.getHeight());

    Assert.assertNotNull(matrix.getElementAt(x, y));
    Assert.assertNull(matrix.getElementAt(0, 0));
  }

  /**
   * Methode permettant de tester {@code WObstacleCellMatrix#toBufferedImage(BufferedImage, int)}
   */
  @Test
  public void testToBufferedImage() {
    int w = 3;
    int h = 3;

    int x = 1;
    int y = 1;

    int rgb = Color.BLACK.getRGB();

    WObstacleCellMatrix matrix = new WObstacleCellMatrix(w, h);
    matrix.setElementAt(new WObstacleCell(), x, y);

    BufferedImage img = matrix.toBufferedImage(rgb);

    Assert.assertEquals("", matrix.getXLength(), img.getWidth());
    Assert.assertEquals("", matrix.getYLength(), img.getHeight());

    Assert.assertEquals(img.getRGB(x, y), rgb);
    Assert.assertNotEquals(img.getRGB(0, 0), rgb);
  }

  /** Test pour {@code WObstacleCellMatrix#toBorderTypeArray()} */
  public void testToBorderTypeArray() {
    int w = 5;
    int h = 5;

    WObstacleCellMatrix obstacleMatrix = new WObstacleCellMatrix(w, h, true);
    obstacleMatrix.drawWindTunnelBorders();

    int size = (2 * (w + h)) - 4;
    int cnt = 0;
    OBSTACLE_TYPE[] expected = new OBSTACLE_TYPE[size];
    for (int i = 0; i < obstacleMatrix.getSize(); i++) {
      if (obstacleMatrix.getElementAt(i) == null) continue;

      expected[cnt] = obstacleMatrix.getElementAt(i).getObstacleType();
      cnt++;
    }

    OBSTACLE_TYPE[] actual = obstacleMatrix.toObstacleTypeArray();

    Assert.assertArrayEquals(Arrays.toString(expected), expected, actual);
  }

  /** Test pour {@code WObstacleCellMatrix#getBorderIndices()} */
  @Test
  public void testGetBorderIndices() {
    int w = 10;
    int h = 20;
    WObstacleCellMatrix obstacleMatrix = new WObstacleCellMatrix(w, h, true);

    obstacleMatrix.drawWindTunnelBorders();

    int[] actual = obstacleMatrix.getBorderIndices();
    int[] expected = new int[(2 * (w + h)) - 4];

    int cnt = 0;
    for (int i = 0; i < obstacleMatrix.getSize(); i++) {
      if (obstacleMatrix.getElementAt(i) == null) continue;
      expected[cnt] = i;
      cnt++;
    }

    Assert.assertArrayEquals(expected, actual);
  }
}
