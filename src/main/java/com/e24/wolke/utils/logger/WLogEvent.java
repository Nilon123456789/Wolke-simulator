package com.e24.wolke.utils.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;

/**
 * WLogEvent
 *
 * <p>Classe pour représenter un log
 *
 * @author Nilon123456789
 */
public class WLogEvent {

  /** Message formatté */
  private final String formattedMessage;

  /** Niveau de log */
  private final Level level;

  /** Source du log */
  private final StackTraceElement source;

  /** Nom de la class */
  private final String className;

  /** Log original */
  private LogEvent originalEvent = null;

  /**
   * Constructeur
   *
   * @param formattedMessage message formatté
   * @param level niveau de log
   * @param source source du log
   */
  public WLogEvent(String formattedMessage, Level level, StackTraceElement source) {
    this.formattedMessage = formattedMessage;
    this.level = level;
    this.source = source;

    int lastDot = source.getClassName().lastIndexOf('.');
    className = source.getClassName().substring(lastDot + 1);
  }

  /**
   * Constructeur
   *
   * @param formattedMessage message formatté
   * @param event log original
   */
  public WLogEvent(String formattedMessage, LogEvent event) {
    this(formattedMessage, event.getLevel(), event.getSource());
    originalEvent = event;
  }

  /**
   * Retourne le message formatté
   *
   * @return message formatté
   */
  public String getFormattedMessage() {
    return this.formattedMessage;
  }

  /**
   * Retourne le niveau de log
   *
   * @return niveau de log
   */
  public Level getLevel() {
    return this.level;
  }

  /**
   * Retourne la source du log
   *
   * @return source du log
   */
  public StackTraceElement getSource() {
    return this.source;
  }

  /**
   * Retourne le nom de la class
   *
   * @return nom de la class
   */
  public String getClassName() {
    return this.className;
  }

  /**
   * Retourne le log original
   *
   * @return log original ou null si non disponible
   */
  public LogEvent getOriginalEvent() {
    return this.originalEvent;
  }

  /**
   * Retourne le moment de reception du log dans le fuseau horaire de l'application
   *
   * @return moment de reception du log
   */
  public String getLogTime() {
    if (originalEvent == null) {
      return "";
    }
    Date date = new Date(this.originalEvent.getTimeMillis());
    DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    formatter.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
    return formatter.format(date);
  }
}
