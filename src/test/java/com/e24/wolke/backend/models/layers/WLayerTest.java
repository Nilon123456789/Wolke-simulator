package com.e24.wolke.backend.models.layers;

import com.e24.wolke.backend.models.editor.layers.WLayer;
import com.e24.wolke.backend.models.editor.layers.WLayerGenerator;
import com.e24.wolke.utils.math.WMatrix1D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester les fonctionnalites de {@code WLayer}
 *
 * @author MeriBouisri
 */
public class WLayerTest {

  /** Test pour {@code WLayer#mergeFromAbove(BufferedImage)} */
  @Test
  public void testMergeFromAbove() {
    int w = 3;
    int h = 1;
    int t = BufferedImage.TRANSLUCENT;

    WLayerGenerator generator = new WLayerGenerator(w, h, t);
    BufferedImage img1 = generator.createBufferedImage();
    BufferedImage img2 = generator.createBufferedImage();
    WLayer layer = generator.createLayer();

    int c1 = Color.RED.getRGB();
    int c2 = Color.BLACK.getRGB();
    int cl = Color.BLUE.getRGB();

    for (int i = 0; i < w; i++) {
      // Draw only pixels 0-1
      if (i < 1) img1.setRGB(i, 0, c1);
      // Draw only pixels 0
      if (i < 2) img2.setRGB(i, 0, c2);

      // Draw all pixels
      layer.getImage().setRGB(i, 0, cl);
    }

    int[] expected = {cl, cl, cl};
    int[] actual = new int[w];

    //// Merge image 1

    layer.mergeFromAbove(img2);

    expected = new int[] {c2, c2, cl};
    actual = this.getRGBPixels(layer.getImage(), 0, 0, w, h);

    Assert.assertArrayEquals(expected, actual);

    // Merge image 2

    layer.mergeFromAbove(img1);
    expected = new int[] {c1, c2, cl};
    actual = this.getRGBPixels(layer.getImage(), 0, 0, w, h);
    Assert.assertArrayEquals(expected, actual);
  }

  /** Test pour {@code WLayer#mergeFromBelow(BufferedImage)} */
  @Test
  public void testMergeFromBelow() {
    int w = 3;
    int h = 1;
    int t = BufferedImage.TRANSLUCENT;

    WLayerGenerator generator = new WLayerGenerator(w, h, t);
    BufferedImage img1 = generator.createBufferedImage();
    BufferedImage img2 = generator.createBufferedImage();
    WLayer layer = generator.createLayer();

    int c1 = Color.RED.getRGB();
    int c2 = Color.BLACK.getRGB();
    int cl = Color.BLUE.getRGB();

    for (int i = 0; i < w; i++) {
      // Draw only pixels 0-1
      if (i < 1) layer.getImage().setRGB(i, 0, cl);
      // Draw only pixels 0
      if (i < 2) img2.setRGB(i, 0, c2);

      img1.setRGB(i, 0, c1);
    }

    int[] expected = new int[w];
    int[] actual = new int[w];

    //// Merge image 1

    layer.mergeFromBelow(img2);

    expected = new int[] {cl, c2, 0};
    actual = this.getRGBPixels(layer.getImage(), 0, 0, w, h);

    Assert.assertArrayEquals(expected, actual);

    // Merge image 2

    layer.mergeFromBelow(img1);
    expected = new int[] {cl, c2, c1};
    actual = this.getRGBPixels(layer.getImage(), 0, 0, w, h);
    Assert.assertArrayEquals(expected, actual);
  }

  /** test pour getRGBPixels */
  @Test
  public void testGetRGBPixels() {
    int w = 3;
    int h = 1;

    BufferedImage img = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
    int c1 = Color.RED.getRGB();
    int c2 = Color.BLACK.getRGB();
    int c3 = Color.BLUE.getRGB();

    img.setRGB(0, 0, c1);
    img.setRGB(1, 0, c2);
    img.setRGB(2, 0, c3);

    int[] expected = {c1, c2, c3};

    int[] actual = this.getRGBPixels(img, 0, 0, w, h);

    Assert.assertArrayEquals(
        "expected=" + Arrays.toString(expected) + ", actual=" + Arrays.toString(actual),
        expected,
        actual);
  }

  /**
   * Methode utilitaire
   *
   * @param image Image
   * @param x x
   * @param y y
   * @param w w
   * @param h h
   * @return int
   */
  public int[] getRGBPixels(BufferedImage image, int x, int y, int w, int h) {

    int[] rgb = new int[WMatrix1D.calculateMatrixSize(w, h)];

    for (int i = x; i < x + w; i++) {
      for (int j = y; j < y + h; j++) {
        rgb[WMatrix1D.getPosition1D(i, j, w)] = image.getRGB(i, j);
      }
    }

    return rgb;
  }
}
