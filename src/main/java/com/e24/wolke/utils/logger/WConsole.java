package com.e24.wolke.utils.logger;

import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.console.ConsoleModel;
import com.e24.wolke.utils.images.WColor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;

/**
 * Le back-end de la console de l'application
 *
 * <p>Permet de gérer les logs de l'application
 *
 * @author Nilon123456789
 */
public class WConsole {
  /** Liste des logs */
  private static final ConcurrentLinkedDeque<WLogEvent> logQueue =
      new ConcurrentLinkedDeque<WLogEvent>();

  /** Compteur de logs par niveau et classe */
  private static final ConcurrentHashMap<String, HashMap<Level, Integer>> eventCount =
      new ConcurrentHashMap<String, HashMap<Level, Integer>>();

  /** Le modèle de la console */
  private static ConsoleModel consoleModel;

  /** Classe utilitaire, ne doit pas être instanciée */
  private WConsole() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Définit le modèle de la console
   *
   * @param consoleModel modèle de la console
   */
  public static void setConsoleModel(ConsoleModel consoleModel) {
    WConsole.consoleModel = consoleModel;
  }

  /**
   * Retourne si la liste des logs est vide
   *
   * @return true si la liste est vide
   */
  public static boolean isLogQueueEmpty() {
    return logQueue.isEmpty();
  }

  /**
   * Ajoute un log à la liste
   *
   * @param event log
   * @return true si le log a été ajouté
   */
  public static boolean append(WLogEvent event) {
    if (event == null) return false;

    HashMap<Level, Integer> levelCount = eventCount.get(event.getClassName());
    if (levelCount == null) {
      levelCount = new HashMap<Level, Integer>();
      eventCount.put(event.getClassName(), levelCount);
    }

    int count = levelCount.getOrDefault(event.getLevel(), -1);
    if (count == -1) {
      levelCount.put(event.getLevel(), 1);
    } else if (count >= ApplicationConstants.MAX_WCONSOLE_LENGTH) {
      for (WLogEvent log : logQueue) { // Retire le plus ancien log de la classe et du niveau donné
        if (!log.getClassName().equals(event.getClassName())
            || !log.getLevel().equals(event.getLevel())) continue;
        logQueue.remove(log);
        break;
      }
    } else {
      levelCount.put(event.getLevel(), count + 1);
    }

    if (consoleModel != null) consoleModel.onNewLog(event);

    return logQueue.add(event);
  }

  /** Vide la liste des logs */
  public static void clear() {
    logQueue.clear();
    eventCount.clear();
  }

  /**
   * Retourne la liste des logs ayant un niveau de log supérieur ou égal à level
   *
   * @param level niveau de log
   * @return liste des logs
   */
  public static LinkedList<String> filterMessageByLogLevel(Level level) {
    LinkedList<String> messages = new LinkedList<String>();
    for (WLogEvent log : logQueue) {
      if (!log.getLevel().isMoreSpecificThan(level)) continue;
      messages.add(log.getFormattedMessage());
    }
    return messages;
  }

  /**
   * Retourne une liste des noms des classes sources des logs
   *
   * @return liste des noms des classes sources des logs
   */
  public static LinkedList<String> getClassNames() {
    LinkedList<String> classNames = new LinkedList<String>();
    for (WLogEvent log : logQueue) {
      if (classNames.contains(log.getClassName())) continue;
      classNames.add(log.getClassName());
    }
    classNames.sort(String::compareTo);

    return classNames;
  }

  /**
   * Retourne la liste des logs ayant une source de log égale à class
   *
   * @param className classe source (nom court {@code Class.getSimpleName()})
   * @return liste des logs
   */
  public static LinkedList<String> filterMessageByClass(String className) {
    LinkedList<String> messages = new LinkedList<String>();
    for (WLogEvent log : logQueue) {
      if (!log.getClassName().equals(className)) continue;
      messages.add(log.getFormattedMessage());
    }
    return messages;
  }

  /**
   * Retourne la liste des logs ayant une source de log égale à l'une des classes
   *
   * @param className classes sources (noms courts {@code Class.getSimpleName()})
   * @return liste des logs
   */
  public static LinkedList<String> filterMessageByClass(String[] className) {
    LinkedList<String> messages = new LinkedList<String>();
    for (WLogEvent log : logQueue) {
      for (String name : className) {
        if (!log.getClassName().equals(name)) continue;
        messages.add(log.getFormattedMessage());
        break;
      }
    }
    return messages;
  }

  /**
   * Retourne la liste des logs ayant une source de log égale à l'une des classes
   *
   * @param classNames classes sources (noms courts {@code Class.getSimpleName()})
   * @return liste des logs
   */
  public static LinkedList<String> filterMessageByClass(Iterable<String> classNames) {
    LinkedList<String> messages = new LinkedList<String>();
    for (WLogEvent log : logQueue) {
      for (String name : classNames) {
        if (!log.getClassName().equals(name)) continue;
        messages.add(log.getFormattedMessage());
        break;
      }
    }
    return messages;
  }

  /**
   * Retourne la liste des logs ayant une source de log égale a la classe et un niveau de log
   *
   * @param className classe source (nom court {@code Class.getSimpleName()})
   * @param level niveau de log
   * @return liste des logs
   */
  public static LinkedList<String> filterMessageByClassAndLevel(String className, Level level) {
    LinkedList<String> messages = new LinkedList<String>();
    for (WLogEvent log : logQueue) {
      if (!log.getClassName().equals(className)) continue;
      if (!log.getLevel().isMoreSpecificThan(level)) continue;
      messages.add(log.getFormattedMessage());
    }
    return messages;
  }

  /**
   * Retourne la liste des logs ayant une source de log égale a la classe et un niveau de log
   *
   * @param className classe source (nom court {@code Class.getSimpleName()})
   * @param level niveau de log
   * @return liste des logs
   */
  public static LinkedList<String> filterMessageByClassAndLevel(String[] className, Level level) {
    LinkedList<String> messages = new LinkedList<String>();
    for (WLogEvent log : logQueue) {
      if (!log.getLevel().isMoreSpecificThan(level)) continue;
      for (String name : className) {
        if (!log.getClassName().equals(name)) continue;
        messages.add(log.getFormattedMessage());
        break;
      }
    }
    return messages;
  }

  /**
   * Retourne la liste des logs ayant une source de log égale a la classe et un niveau de log
   *
   * @param className classe source (nom court {@code Class.getSimpleName()})
   * @param level niveau de log
   * @return liste des logs
   */
  public static LinkedList<String> filterMessageByClassAndLevel(
      Iterable<String> className, Level level) {
    LinkedList<String> messages = new LinkedList<String>();
    for (WLogEvent log : logQueue) {
      if (!log.getLevel().isMoreSpecificThan(level)) continue;
      for (String name : className) {
        if (!log.getClassName().equals(name)) continue;
        messages.add(log.getFormattedMessage());
        break;
      }
    }
    return messages;
  }

  /**
   * Retourne toutes les logs sous forme de document HTML
   *
   * @return document HTML
   */
  public static String generateHtmlLogs() {
    StringBuilder logo = new StringBuilder();
    InputStreamReader logoInput =
        new InputStreamReader(
            Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("icons/wolke-logo.svg"));
    try (BufferedReader reader = new BufferedReader(logoInput)) {
      for (String line; (line = reader.readLine()) != null; ) logo.append(line);
    } catch (Exception e) {
      logo.append("[logo]");
    }
    String logoString = logo.toString();
    String date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm").format(LocalDateTime.now());

    ResourceBundle bundle = LocaleManager.getLocaleResourceBundle();
    StringBuilder html = new StringBuilder();
    html.append("<html><head>");
    html.append("<title>")
        .append(bundle.getString("ui.console.export.html.title"))
        .append(" ")
        .append(date)
        .append("</title>");
    html.append("<style>");
    html.append("body {font-family: Arial, sans-serif;}");
    html.append("table {border-collapse: collapse; width: 100%;}");
    html.append("th, td {border: 1px solid black; padding: 8px;}");
    html.append("th {background-color: #f2f2f2;}");
    html.append("tr:nth-child(even) {background-color: #f2f2f2;}");
    html.append("tr:hover {background-color: #D3D3D3;}");
    html.append("td {text-align: left;}");
    html.append("svg {width: 45px; height: 45px; vertical-align: middle; margin-right: 10px;}");
    html.append("footer {bottom: 0; width: 100%; text-align: center;}");
    html.append("footer p {margin: 0;}");
    html.append("</style></head><body>");
    html.append("<h1>");
    html.append(logoString);
    html.append(bundle.getString("ui.console.export.html.title"));
    html.append("</h1>");
    html.append("<p>")
        .append(bundle.getString("ui.console.export.html.description"))
        .append(date)
        .append("</p>");
    html.append("<table>");
    html.append("<tr><th>")
        .append(bundle.getString("ui.console.export.html.time"))
        .append("</th><th>")
        .append(bundle.getString("ui.console.export.html.level"))
        .append("</th><th>")
        .append(bundle.getString("ui.console.export.html.class"))
        .append("</th><th>")
        .append(bundle.getString("ui.console.export.html.message"))
        .append("</th></tr>");
    for (WLogEvent log : logQueue) {
      LogEvent event = log.getOriginalEvent();
      html.append("<tr>");
      html.append("<td>").append(log.getLogTime()).append("</td>");
      html.append("<td>").append(WColor.getLogHtmlColor(log.getLevel())).append("</td>");
      html.append("<td>").append(log.getClassName()).append("</td>");
      html.append("<td>").append(event.getMessage().getFormattedMessage()).append("</td>");
      html.append("</tr>");
    }
    html.append("</table>");
    html.append("<hr>");
    html.append("</body><footer>");
    html.append("<p>");
    html.append("Version: ");
    html.append(LocaleManager.getLocaleResourceBundle().getString("version"));
    html.append("</p>");
    html.append("<p>");
    html.append(
        LocaleManager.getLocaleResourceBundle().getString("ui.console.export.html.java_version"));
    html.append(System.getProperty("java.version"));
    html.append("</p>");
    html.append("<p>");
    html.append(
        LocaleManager.getLocaleResourceBundle()
            .getString("ui.console.export.html.operating_system"));
    html.append(System.getProperty("os.name"));
    html.append("</p>");
    html.append("</footer></html>");

    return html.toString();
  }
}
