package com.e24.wolke.backend.models.layers;

import com.e24.wolke.backend.models.editor.layers.WLayer;
import com.e24.wolke.backend.models.editor.layers.WLayerGenerator;
import java.awt.image.BufferedImage;
import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester les fonctionnalites de {@code WLayerGenerator}
 *
 * @author MeriBouisri
 */
public class WLayerGeneratorTest {

  /** Test pour {@code WLayerFactory#createLayer()} */
  @Test
  public void testCreateLayer() {
    int w = 1;
    int h = 1;
    int t = BufferedImage.TRANSLUCENT;
    WLayerGenerator factory = new WLayerGenerator(w, h, t);
    WLayer layer = factory.createLayer();

    Assert.assertEquals(w, layer.getImage().getWidth());
    Assert.assertEquals(h, layer.getImage().getHeight());
    Assert.assertEquals(t, layer.getImage().getType());
  }

  /** Test pour {@code WLayerFactory#createLayer()} */
  @Test
  public void testCreateBufferedImage() {
    int w = 1;
    int h = 1;
    int t = BufferedImage.TRANSLUCENT;
    WLayerGenerator factory = new WLayerGenerator(w, h, t);
    BufferedImage image = factory.createBufferedImage();

    Assert.assertEquals(w, image.getWidth());
    Assert.assertEquals(h, image.getHeight());
    Assert.assertEquals(t, image.getType());
  }

  /** Test pour {@code WLayerFactory#isCompatibleLayer()} */
  @Test
  public void testIsCompatibleLayer() {
    int w = 1;
    int h = 1;
    int t = BufferedImage.TRANSLUCENT;
    WLayerGenerator factory = new WLayerGenerator(w, h, t);
    WLayer layer = factory.createLayer();
    WLayer incompatibleLayer = new WLayer(new BufferedImage(w + 1, h + 1, t + 1));

    Assert.assertTrue(factory.isCompatibleLayer(layer));
    Assert.assertFalse(factory.isCompatibleLayer(incompatibleLayer));
  }

  /** Test pour {@code WLayerFactory#isCompatibleBufferedImage()} */
  @Test
  public void testIsCompatibleBufferedImage() {
    int w = 1;
    int h = 1;
    int t = BufferedImage.TRANSLUCENT;
    WLayerGenerator factory = new WLayerGenerator(w, h, t);
    BufferedImage image = factory.createBufferedImage();
    BufferedImage incompatibleImage = new BufferedImage(w + 1, h + 1, t + 1);

    Assert.assertTrue(factory.isCompatibleBufferedImage(image));
    Assert.assertFalse(factory.isCompatibleBufferedImage(incompatibleImage));
  }
}
