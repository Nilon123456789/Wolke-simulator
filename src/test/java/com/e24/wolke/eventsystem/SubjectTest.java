package com.e24.wolke.eventsystem;

import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester les methodes de la classe {@code Subject}
 *
 * @see Subject
 * @author MeriBouisri
 */
public class SubjectTest {

  /**
   * Test #1 permettant de tester la methode {@code Subject#isValidMessage(Object)}.
   *
   * <p>Ce test sert de verifier que les objets d'un type heritant d'un {@code messageType} sont
   * valides.
   */
  @Test
  public void testIsValidMessage01() {
    Subject subjObject = Subject.ON_TEST_PUBSUB_OBJECT;

    Object msgObject = new Object();
    String msgString = "";
    Integer msgInteger = 1;

    Assert.assertTrue(subjObject.isValidMessage(msgObject));
    Assert.assertTrue(subjObject.isValidMessage(msgString));
    Assert.assertTrue(subjObject.isValidMessage(msgInteger));
  }

  /**
   * Test #2 permettant de tester la methode {@code Subject#isValidMessage(Object)}.
   *
   * <p>Ce test sert a verifier que les objets d'un type qui n'herite pas d'un {@code messageType}
   * ne sont pas valides.
   */
  @Test
  public void testIsValidMessage02() {
    Subject subjInteger = Subject.ON_TEST_PUBSUB_INTEGER;

    Object msgObject = new Object();
    String msgString = "";
    Integer msgInteger = 1;

    Assert.assertTrue(subjInteger.isValidMessage(msgInteger));
    Assert.assertFalse(subjInteger.isValidMessage(msgObject));
    Assert.assertFalse(subjInteger.isValidMessage(msgString));
  }

  /**
   * Test #3 permettant de tester la methode {@code Subject#isValidMessage(Object)}.
   *
   * <p>Ce test sert a verifier que les messages non-null ne sont pas valides pour un {@code
   * messageType} null.
   */
  @Test
  public void testIsValidMessage03() {
    Subject subjNull = Subject.ON_TEST_PUBSUB_NULL;

    String msgString = "";

    Assert.assertTrue(subjNull.isValidMessage(null));
    Assert.assertFalse(subjNull.isValidMessage(msgString));
  }

  /**
   * Test #4 permettant de tester la methode {@code Subject#isValidMessage(Object)}.
   *
   * <p>Ce test sert a verifier que les messages null ne sont pas valides pour un {@code
   * messageType} non-null.
   */
  @Test
  public void testIsValidMessage04() {
    Subject subjObject = Subject.ON_TEST_PUBSUB_OBJECT;

    Assert.assertFalse(subjObject.isValidMessage(null));
  }
}
