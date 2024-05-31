package com.e24.wolke.filesystem;

import com.e24.wolke.backend.models.application.LocaleManager;
import java.util.Locale;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * La classe {@code WUserFileManagerTest} permet de tester la classe {@code WUserFileManager}.
 *
 * @author Nilon123456789
 */
public class WUserFileManagerTest {
  /** Initialisation des propriétés utilisateur */
  @BeforeClass
  public static void setUp() {
    LocaleManager.update(Locale.FRENCH); // Initialisation de la locale pour les log
  }

  /**
   * Test de la méthode {@code copyResourcesToUserFolder()}. afin de vérifier que les fichiers
   * existent tous.
   */
  @Test
  public void testCopyResourcesToUserFolder() {
    Assert.assertTrue(WUserFileManager.copyResourcesToUserFolder());
  }
}
