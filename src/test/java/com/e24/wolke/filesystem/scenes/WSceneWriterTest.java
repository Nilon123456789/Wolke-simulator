package com.e24.wolke.filesystem.scenes;

import com.e24.wolke.backend.models.renderer.RendererProperties;
import com.e24.wolke.backend.models.simulation.SimulationProperties;
import com.e24.wolke.utils.files.WFileUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester {@code WSceneWriter}
 *
 * @author MeriBouisri
 */
public class WSceneWriterTest {

  /** Width de l'image */
  public static final int w = 10;

  /** Height de l'image */
  public static final int h = 10;

  /** Test pour {@code WSceneWriter#write(Scene, File)} */
  @Test
  public void testWrite() {
    WScene scene = WSceneWriterTest.createScene();

    File folder = new File("./");

    File zip = null;
    try {
      zip = WSceneWriter.write(scene, folder);
      zip.deleteOnExit();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      Assert.fail();
    }

    Assert.assertNotNull(zip);
    Assert.assertTrue(WFileUtils.isWolkeFile(zip));
  }

  /**
   * Creer scene
   *
   * @return {@code WScene}
   */
  public static WScene createScene() {
    return new WScene(
        "TestScene",
        new BufferedImage(w, h, BufferedImage.TRANSLUCENT),
        (new SimulationProperties()).writeStandardProperties(),
        (new RendererProperties()).writeStandardProperties());
  }
}
