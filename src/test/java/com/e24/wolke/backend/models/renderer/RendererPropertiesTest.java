package com.e24.wolke.backend.models.renderer;

import com.e24.wolke.backend.models.renderer.RendererConstants.VisualizationType;
import com.e24.wolke.filesystem.properties.WPropertyKey;
import java.util.Properties;
import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester les fonctionnalites de {@code RendererProperties}
 *
 * @author MeriBouisri
 */
public class RendererPropertiesTest {
  /** Methode permettant de tester {@code RendererProperties#readStandardProperties(Properties)} */
  @Test
  public void testReadStandardProperties() {
    RendererProperties properties = new RendererProperties();

    Properties stdProperties = new Properties();

    stdProperties = properties.writeStandardProperties();

    Assert.assertTrue(stdProperties.containsKey(WPropertyKey.RENDERER_GREYSCALE.getKey()));
    Assert.assertTrue(stdProperties.containsKey(WPropertyKey.RENDERER_SHOW_VECTORS.getKey()));
    Assert.assertTrue(stdProperties.containsKey(WPropertyKey.RENDERER_VISUALIZATION.getKey()));

    System.out.println(stdProperties.toString());

    boolean expectedGreyscale = true;
    VisualizationType expectedVis = VisualizationType.VELOCITY_X;

    WPropertyKey.RENDERER_GREYSCALE.write(expectedGreyscale, stdProperties);
    WPropertyKey.RENDERER_VISUALIZATION.write(expectedVis.ordinal(), stdProperties);

    properties.readStandardProperties(stdProperties);

    boolean actualGreyscale = properties.greyscale;
    VisualizationType actualVis = properties.visualizationType;

    Assert.assertEquals(expectedGreyscale, actualGreyscale);
    Assert.assertEquals(expectedVis, actualVis);
  }
}
