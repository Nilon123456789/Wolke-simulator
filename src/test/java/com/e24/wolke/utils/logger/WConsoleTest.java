package com.e24.wolke.utils.logger;

import com.e24.wolke.backend.models.application.ApplicationConstants;
import java.util.LinkedList;
import org.apache.logging.log4j.Level;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * WConsoleTest.java
 *
 * <p>Classe de test pour la classe WConsole.
 *
 * @see WConsole
 * @author Nilon123456789
 */
public class WConsoleTest {

  /** Réinitialisation de la console avant chaque test */
  @Before
  public void setUp() {
    WConsole.clear();
  }

  /** Test #1 de la méthode isLogQueueEmpty */
  @Test
  public void testIsLogQueueEmpty1() {
    Assert.assertTrue(WConsole.isLogQueueEmpty());
  }

  /** Test #2 de la méthode isLogQueueEmpty */
  @Test
  public void testIsLogQueueEmpty2() {
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement("Class", "Method", "File", 0)));
    Assert.assertFalse(WConsole.isLogQueueEmpty());
  }

  /** Test de la méthode clear */
  @Test
  public void testClear() {
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement("Class", "Method", "File", 0)));
    WConsole.clear();

    Assert.assertTrue(WConsole.isLogQueueEmpty());
  }

  /** Test #1 de la méthode getClassNames */
  @Test
  public void testGetClassNames1() {
    String className = "Class";
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className, "Method", "File", 0)));

    Assert.assertEquals(1, WConsole.getClassNames().size());
    Assert.assertTrue(WConsole.getClassNames().contains(className));
  }

  /** Test #2 de la méthode getClassNames */
  @Test
  public void testGetClassNames2() {
    String className = "Class";
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className, "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className, "Method", "File", 0)));

    Assert.assertEquals(1, WConsole.getClassNames().size());
    Assert.assertTrue(WConsole.getClassNames().contains(className));
  }

  /** Test #3 de la méthode getClassNames */
  @Test
  public void testGetClassNames3() {
    String className1 = "Class1";
    String className2 = "Class2";
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className1, "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className2, "Method", "File", 0)));

    Assert.assertEquals(2, WConsole.getClassNames().size());
    Assert.assertTrue(WConsole.getClassNames().contains(className1));
    Assert.assertTrue(WConsole.getClassNames().contains(className2));
  }

  /** Test #4 de la méthode getClassNames */
  @Test
  public void testGetClassNames4() {
    String class1 = WConsoleTest.class.getName();
    String expectedClassName1 = WConsoleTest.class.getSimpleName();
    String class2 = WConsole.class.getName();
    String expectedClassName2 = WConsole.class.getSimpleName();

    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(class1, "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(class2, "Method", "File", 0)));

    Assert.assertEquals(2, WConsole.getClassNames().size());
    Assert.assertTrue(WConsole.getClassNames().contains(expectedClassName1));
    Assert.assertTrue(WConsole.getClassNames().contains(expectedClassName2));
  }

  /** Test #1 de la méthode filterMessageByClass */
  @Test
  public void testFilterMessageByClass1() {
    String className = "Class";
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className, "Method", "File", 0)));

    Assert.assertEquals(1, WConsole.filterMessageByClass(className).size());
  }

  /** Test #2 de la méthode filterMessageByClass */
  @Test
  public void testFilterMessageByClass2() {
    String className = "Class";
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className, "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className, "Method", "File", 0)));

    Assert.assertEquals(2, WConsole.filterMessageByClass(className).size());
  }

  /** Test #3 de la méthode filterMessageByClass */
  @Test
  public void testFilterMessageByClass3() {
    String className1 = "Class1";
    String className2 = "Class2";
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className1, "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className2, "Method", "File", 0)));

    Assert.assertEquals(1, WConsole.filterMessageByClass(className1).size());
    Assert.assertEquals(1, WConsole.filterMessageByClass(className2).size());
  }

  /** Test #4 de la méthode filterMessageByClass */
  @Test
  public void testFilterMessageByClass4() {
    Assert.assertEquals(0, WConsole.filterMessageByClass("RandomName").size());
  }

  /** Test #5 de la méthode filterMessageByClass */
  @Test
  public void testFilterMessageByClass5() {
    String className1 = "Class1";
    String className2 = "Class2";

    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className1, "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className2, "Method", "File", 0)));

    String classes[] = {className1, className2};
    Assert.assertEquals(2, WConsole.filterMessageByClass(classes).size());
  }

  /** Test #6 de la méthode filterMessageByClass */
  @Test
  public void testFilterMessageByClass6() {
    String className1 = "Class1";
    String className2 = "Class2";

    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className1, "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className2, "Method", "File", 0)));

    String classes[] = {className1, "RandomName"};
    Assert.assertEquals(1, WConsole.filterMessageByClass(classes).size());
  }

  /** Test #7 de la méthode filterMessageByClass */
  @Test
  public void testFilterMessageByClass7() {
    String className1 = "Class1";
    String className2 = "Class2";

    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className1, "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className2, "Method", "File", 0)));

    LinkedList<String> classes = new LinkedList<String>();
    classes.add(className1);
    classes.add(className2);

    Assert.assertEquals(2, WConsole.filterMessageByClass(classes).size());
  }

  /** Test #8 de la méthode filterMessageByClass */
  @Test
  public void testFilterMessageByClass8() {
    String className1 = "Class1";
    String className2 = "Class2";

    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className1, "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className2, "Method", "File", 0)));

    LinkedList<String> classes = new LinkedList<String>();
    classes.add(className1);
    classes.add("RandomName");

    Assert.assertEquals(1, WConsole.filterMessageByClass(classes).size());
  }

  /** Test #1 du maximum de logs par type */
  @Test
  public void testMaxWConsoleLength1() {
    for (int i = 0; i < ApplicationConstants.MAX_WCONSOLE_LENGTH; i++) {
      WConsole.append(
          new WLogEvent("Test", Level.DEBUG, new StackTraceElement("Class", "Method", "File", 0)));
    }

    Assert.assertEquals(
        ApplicationConstants.MAX_WCONSOLE_LENGTH, WConsole.filterMessageByClass("Class").size());
  }

  /** Test #2 du maximum de logs par type */
  @Test
  public void testMaxWConsoleLength2() {
    for (int i = 0; i < ApplicationConstants.MAX_WCONSOLE_LENGTH + 1; i++) {
      WConsole.append(
          new WLogEvent("Test", Level.DEBUG, new StackTraceElement("Class", "Method", "File", 0)));
    }

    Assert.assertEquals(
        ApplicationConstants.MAX_WCONSOLE_LENGTH, WConsole.filterMessageByClass("Class").size());
  }

  /** Test #3 du maximum de logs par type */
  @Test
  public void testMaxWConsoleLength3() {
    for (int i = 0; i < ApplicationConstants.MAX_WCONSOLE_LENGTH - 1; i++) {
      WConsole.append(
          new WLogEvent("Test", Level.DEBUG, new StackTraceElement("Class", "Method", "File", 0)));
    }

    Assert.assertEquals(
        ApplicationConstants.MAX_WCONSOLE_LENGTH - 1,
        WConsole.filterMessageByClass("Class").size());
  }

  /** Test #4 du maximum de logs par type */
  @Test
  public void testMaxWConsoleLength4() {
    for (int i = 0; i < ApplicationConstants.MAX_WCONSOLE_LENGTH; i++) {
      WConsole.append(
          new WLogEvent("Test", Level.DEBUG, new StackTraceElement("Class", "Method", "File", 0)));
      WConsole.append(
          new WLogEvent("Test", Level.WARN, new StackTraceElement("Class", "Method", "File", 0)));
    }

    Assert.assertEquals(
        ApplicationConstants.MAX_WCONSOLE_LENGTH * 2,
        WConsole.filterMessageByClass("Class").size());
  }

  /** Test #5 du maximum de logs par type */
  @Test
  public void testMaxWConsoleLength5() {
    for (int i = 0; i < ApplicationConstants.MAX_WCONSOLE_LENGTH; i++) {
      WConsole.append(
          new WLogEvent("Test", Level.DEBUG, new StackTraceElement("Class", "Method", "File", 0)));
      WConsole.append(
          new WLogEvent("Test", Level.WARN, new StackTraceElement("Class", "Method", "File", 0)));
    }

    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement("Class", "Method", "File", 0)));

    Assert.assertEquals(
        ApplicationConstants.MAX_WCONSOLE_LENGTH * 2,
        WConsole.filterMessageByClass("Class").size());
  }

  /** Test #1 de la méthode filterMessageByLogLevel */
  @Test
  public void testFilterMessageByLogLevel1() {
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement("Class", "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement("Class", "Method", "File", 0)));

    Assert.assertEquals(2, WConsole.filterMessageByLogLevel(Level.DEBUG).size());
    Assert.assertEquals(0, WConsole.filterMessageByLogLevel(Level.WARN).size());
  }

  /** Test #2 de la méthode filterMessageByLogLevel */
  @Test
  public void testFilterMessageByLogLevel2() {
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement("Class", "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.WARN, new StackTraceElement("Class", "Method", "File", 0)));

    Assert.assertEquals(2, WConsole.filterMessageByLogLevel(Level.DEBUG).size());
    Assert.assertEquals(1, WConsole.filterMessageByLogLevel(Level.WARN).size());
  }

  /** Test #1 de la méthode filterMessageByClassAndLevel */
  @Test
  public void testFilterMessageByClassAndLevel1() {
    String className = "Class";
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className, "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.WARN, new StackTraceElement(className, "Method", "File", 0)));

    Assert.assertEquals(2, WConsole.filterMessageByClassAndLevel(className, Level.DEBUG).size());
    Assert.assertEquals(1, WConsole.filterMessageByClassAndLevel(className, Level.WARN).size());
  }

  /** Test #2 de la méthode filterMessageByClassAndLevel */
  @Test
  public void testFilterMessageByClassAndLevel2() {
    String className = "Class";
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className, "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.WARN, new StackTraceElement(className, "Method", "File", 0)));

    Assert.assertEquals(0, WConsole.filterMessageByClassAndLevel(className, Level.ERROR).size());
  }

  /** Test #3 de la méthode filterMessageByClassAndLevel */
  @Test
  public void testFilterMessageByClassAndLevel3() {
    String className = "Class";
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className, "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.WARN, new StackTraceElement(className, "Method", "File", 0)));

    Assert.assertEquals(2, WConsole.filterMessageByClassAndLevel(className, Level.DEBUG).size());
    Assert.assertEquals(1, WConsole.filterMessageByClassAndLevel(className, Level.WARN).size());
  }

  /** Test #4 de la méthode filterMessageByClassAndLevel */
  @Test
  public void testFilterMessageByClassAndLevel4() {
    String className = "Class";
    WConsole.append(
        new WLogEvent("Test", Level.DEBUG, new StackTraceElement(className, "Method", "File", 0)));
    WConsole.append(
        new WLogEvent("Test", Level.WARN, new StackTraceElement(className, "Method", "File", 0)));

    Assert.assertEquals(0, WConsole.filterMessageByClassAndLevel(className, Level.ERROR).size());
    Assert.assertEquals(0, WConsole.filterMessageByClassAndLevel(className, Level.FATAL).size());
  }
}
