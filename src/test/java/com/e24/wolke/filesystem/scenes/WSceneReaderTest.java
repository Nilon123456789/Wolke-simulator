package com.e24.wolke.filesystem.scenes;

import java.io.File;
import org.junit.Test;

/**
 * Classe pour tester {@code WSceneReader}
 *
 * @author MeriBouisri
 */
public class WSceneReaderTest {

  /** Test pour {@code WSceneReader#read(File)} */
  @Test
  public void testRead() {
    WScene scene = WSceneReader.read(new File("./src/test/resources/TestScene.wlks"));

    System.out.println(scene.getRendererProperties());
    System.out.println(scene.getSimulationProperties());
  }
}
