package com.e24.wolke.utils;

import com.e24.wolke.backend.models.application.LocaleManager;
import java.util.Locale;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * TimerTest.java
 *
 * <p>Classe de test pour la classe Timer.
 *
 * @see Timer
 * @author Nilon123456789
 */
public class TimerTest {

  /** Initialisation des tests */
  @BeforeClass
  public static void setUp() {
    // new ApplicationModel();
    LocaleManager.update(Locale.FRENCH);
  }

  /** Test si le timer retourne "" lorsqu'il est déjà démarré */
  @Test
  public void testTimerAlreadyStarted() {
    Timer timer = new Timer("Test");
    timer.start("Test");
    Assert.assertEquals("", timer.start("Test"));
  }

  /** Test si le timer retourne -1 lorsqu'il n'est pas démarré (avec stop) */
  @Test
  public void testTimerNotStarted() {
    Timer timer = new Timer("Test");
    Assert.assertEquals(Long.MIN_VALUE, timer.stop("Test"));
  }

  /** Test si le timer retourne -1 lorsqu'il n'est pas démarré (avec stopAndPrint) */
  @Test
  public void testTimerNotStartedAndPrint() {
    Timer timer = new Timer("Test");
    Assert.assertEquals(Long.MIN_VALUE, timer.stopAndPrint("Test"));
  }

  /** Test si deleteAll supprime bien tout les timers */
  @Test
  public void testTimerDeleteAll() {
    Timer timer = new Timer("Test");
    timer.start("Test1");
    timer.start("Test2");
    timer.start("Test3");

    timer.deleteAll();

    Assert.assertEquals(Long.MIN_VALUE, timer.stop("Test1"));
    Assert.assertEquals(Long.MIN_VALUE, timer.stop("Test2"));
    Assert.assertEquals(Long.MIN_VALUE, timer.stop("Test3"));
  }

  /** Test #1 si reinitialiser foncitonne bien */
  @Test
  public void testTimerReinitialize1() {
    Timer timer = new Timer("Test");
    timer.start("Test");
    timer.reinitialize();
    Assert.assertEquals(Long.MIN_VALUE, timer.stop("Test"));
  }

  /** Test #2 si reinitialiser foncitonne bien */
  @Test
  public void testTimerReinitialize2() {
    Timer timer = new Timer("Test");
    timer.start("Test");
    timer.reinitialize();
    Assert.assertEquals(Long.MIN_VALUE, timer.stopAndPrint("Test"));
  }

  /** Test #3 si reinitialiser foncitonne bien */
  @Test
  public void testTimerReinitialize3() {
    Timer timer = new Timer("Test");
    timer.start("Test");
    timer.reinitialize();
    Assert.assertEquals("Test", timer.start("Test"));
  }

  /** Test #4 si reinitialiser foncitonne bien */
  @Test
  public void testTimerReinitialize4() {
    Timer timer = new Timer("Test");
    timer.start("Test");
    timer.reinitialize();
    Assert.assertEquals(Long.MIN_VALUE, timer.getAverage("Test"));
  }

  /** Test #1 si le nom du timer est null */
  @Test
  public void testTimerEmptyName1() {
    Timer timer = new Timer("Test");
    Assert.assertEquals("", timer.start(""));
  }

  /** Test #2 si le nom du timer est null */
  @Test
  public void testTimerEmptyName2() {
    Timer timer = new Timer("Test");
    Assert.assertEquals(Long.MIN_VALUE, timer.stop(""));
  }

  /** Test #3 si le nom du timer est null */
  @Test
  public void testTimerEmptyName3() {
    Timer timer = new Timer("Test");
    Assert.assertEquals(Long.MIN_VALUE, timer.stopAndPrint(""));
  }
}
