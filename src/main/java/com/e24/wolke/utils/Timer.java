package com.e24.wolke.utils;

import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.utils.interfaces.Reinitializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * Timer.java
 *
 * <p>Cette classe permet de mesurer le temps d'exécution d'une méthode.
 *
 * @author Nilon123456789
 */
public class Timer implements Reinitializable {

  /** Niveau de log par défaut */
  private static final Level DEFAULT_LOG_LEVEL = Level.INFO;

  /** Marqueur de log du timer */
  private static final Marker TIMER_MARKER = MarkerManager.getMarker("TIMER");

  /** Logger par défaut */
  private static final Logger DEFAULT_LOGGER = LogManager.getLogger(Timer.class.getSimpleName());

  /** Temps du début des timer */
  private final ConcurrentHashMap<String, Long> timers;

  /**
   * Classe interne pour stocker les résultats des timers
   *
   * <p>Permet de stocker le temps d'exécution et le nombre d'exécution
   *
   * @author Nilon123456789
   */
  class TimeEntry implements Map.Entry<Long, Integer> {
    /** Temps d'exécution */
    private long time = 0;

    /** Nombre d'exécution */
    private int count = 0;

    /** {@inheritDoc} */
    @Override
    public Long getKey() {
      return time;
    }

    /**
     * Retourne le nombre d'exécution
     *
     * @return Nombre d'exécution
     */
    @Override
    public Integer getValue() {
      return count;
    }

    /** {@inheritDoc} */
    @Override
    public Integer setValue(Integer value) {
      return count = value;
    }

    /**
     * Met à jour le temps d'exécution
     *
     * @param time Temps à ajouter
     */
    public void updateTime(long time) {
      this.time += time;
      this.count++;
    }

    /**
     * Retourne la moyenne des temps d'exécution
     *
     * @return Moyenne des temps d'exécution ({@code Long.MIN_VALUE} si le nombre d'exécution est 0)
     */
    public long getAverage() {
      if (count == 0) return Long.MIN_VALUE;
      return time / count;
    }
  }

  /** Résultat des timers */
  private final ConcurrentHashMap<String, TimeEntry> timersResults;

  /**
   * Liste des interval des timers qui affiche la moyenne des temps d'exécution à intervalle
   * régulier d'arrêt
   */
  private final ConcurrentHashMap<String, Integer> autoAveragePrintInterval;

  /**
   * Liste des niveaux de logs des timer qui affiche la moyenne des temps d'exécution à intervalle
   * régulier d'arrêt
   */
  private final ConcurrentHashMap<String, Level> autoAveragePrintLogLevel;

  /** Nom de la catégorie du timer */
  private final String categorieName;

  /** Logger de la classe */
  protected final Logger LOGGER;

  /**
   * Constructeur
   *
   * @param categorieName Nom de la catégorie du timer
   */
  public Timer(String categorieName) {
    this(categorieName, DEFAULT_LOGGER);
  }

  /**
   * Constructeur
   *
   * @param categorieName Nom de la catégorie du timer
   * @param logger Logger de la classe
   */
  public Timer(String categorieName, Logger logger) {
    LOGGER = LogManager.getLogger(categorieName);
    this.categorieName = categorieName;
    timers = new ConcurrentHashMap<String, Long>();
    timersResults = new ConcurrentHashMap<String, TimeEntry>();
    autoAveragePrintInterval = new ConcurrentHashMap<String, Integer>();
    autoAveragePrintLogLevel = new ConcurrentHashMap<String, Level>();
  }

  /**
   * Démarrer un timer
   *
   * @param timerName Nom du timer
   * @return Nom du timer si le timer a été démarré (vide sinon)
   */
  public String start(String timerName) {
    if (timerName.isEmpty()) {
      LOGGER.warn(
          TIMER_MARKER,
          LocaleManager.getLocaleResourceBundle().getString("timer.emptyName"),
          categorieName);
      return "";
    }

    if (timers.containsKey(timerName)) {
      LOGGER.warn(
          TIMER_MARKER,
          LocaleManager.getLocaleResourceBundle().getString("timer.alreadyStarted"),
          timerName,
          categorieName);
      return "";
    }

    timers.put(timerName, System.currentTimeMillis());

    if (!timersResults.containsKey(timerName)) {
      timersResults.put(timerName, new TimeEntry());
    }

    return timerName;
  }

  /**
   * Arrêter un timer
   *
   * @param timerName Nom du timer
   * @return Temps d'exécution du timer ({@code Long.MIN_VALUE} si le timer n'existe pas)
   */
  public long stop(String timerName) {
    if (timerName.isEmpty()) return Long.MIN_VALUE;
    if (!timers.containsKey(timerName)) {
      LOGGER.warn(
          TIMER_MARKER,
          LocaleManager.getLocaleResourceBundle().getString("timer.noTimerNamed"),
          timerName,
          categorieName);
      return Long.MIN_VALUE;
    }

    Long startTime = timers.getOrDefault(timerName, null);

    if (startTime == null) {
      LOGGER.warn(
          TIMER_MARKER,
          LocaleManager.getLocaleResourceBundle().getString("timer.noTimerNamed"),
          timerName,
          categorieName);
      return Long.MIN_VALUE;
    }

    long time = System.currentTimeMillis() - timers.get(timerName).longValue();
    timers.remove(timerName);
    timersResults.get(timerName).updateTime(time);

    if (!autoAveragePrintInterval.containsKey(timerName)) return time;

    int frequency = autoAveragePrintInterval.get(timerName).intValue();
    if (this.timersResults.get(timerName).getValue() % frequency != 0) return time;

    Level level = autoAveragePrintLogLevel.getOrDefault(timerName, DEFAULT_LOG_LEVEL);

    LOGGER.log(
        level,
        TIMER_MARKER,
        LocaleManager.getLocaleResourceBundle().getString("timer.autoAveragePrint"),
        timerName,
        categorieName,
        getAverage(timerName));

    return time;
  }

  /**
   * Arrête un timer et affiche le temps d'exécution
   *
   * @param timerName Nom du timer
   * @return Temps d'exécution du timer ({@code Long.MIN_VALUE} si le timer n'existe pas)
   */
  public long stopAndPrint(String timerName) {
    long time = stop(timerName);
    LOGGER.trace(TIMER_MARKER, "{} - {} : {} ms", categorieName, timerName, time);
    return time;
  }

  /**
   * Supprime tout les timer
   *
   * <p>Ne suprime pas les fréquences d'affichage des moyennes
   */
  public void deleteAll() {
    timers.clear();
    timersResults.clear();
  }

  /**
   * Retourne la moyenne des temps d'exécution d'un timer
   *
   * @param timerName Nom du timer
   * @return Moyenne des temps d'exécution du timer ({@code Long.MIN_VALUE} si le timer n'existe
   *     pas)
   */
  public long getAverage(String timerName) {
    if (timerName.isEmpty()) {
      LOGGER.warn(
          TIMER_MARKER,
          LocaleManager.getLocaleResourceBundle().getString("timer.emptyName"),
          categorieName);
      return Long.MIN_VALUE;
    }

    if (!timersResults.containsKey(timerName) || timersResults.get(timerName).getValue() == 0)
      return Long.MIN_VALUE;

    return timersResults.get(timerName).getAverage();
  }

  /**
   * Crée un timer qui affiche la moyenne des temps d'exécution à intervalle régulier d'arrêt
   *
   * @param timerName Nom du timer
   * @param frequency Fréquence d'affichage de la moyenne
   * @param level Niveau de log de l'affichage
   * @return true si le timer a été créé, false sinon
   */
  public boolean setupAutoAveragePrint(String timerName, int frequency, Level level) {
    if (timerName.isEmpty()) {
      LOGGER.warn(
          TIMER_MARKER,
          LocaleManager.getLocaleResourceBundle().getString("timer.emptyName"),
          categorieName);
      return false;
    }

    if (frequency <= 0) {
      LOGGER.error(
          TIMER_MARKER,
          LocaleManager.getLocaleResourceBundle().getString("timer.invalidFrequency"),
          frequency,
          timerName,
          categorieName);
      return false;
    }

    Integer previousValue = autoAveragePrintInterval.put(timerName, frequency);
    autoAveragePrintLogLevel.put(timerName, level);

    if (previousValue != null) {
      LOGGER.warn(
          TIMER_MARKER,
          LocaleManager.getLocaleResourceBundle().getString("timer.alreadyAutoAveragePrint"),
          timerName,
          categorieName,
          previousValue,
          frequency);
    }

    return true;
  }

  /**
   * Crée un timer qui affiche la moyenne des temps d'exécution à intervalle régulier d'arrêt
   *
   * @param timerName Nom du timer
   * @param frequency Fréquence d'affichage de la moyenne
   * @return true si le timer a été créé, false sinon
   */
  public boolean setupAutoAveragePrint(String timerName, int frequency) {
    return setupAutoAveragePrint(timerName, frequency, DEFAULT_LOG_LEVEL);
  }

  /**
   * Supprime un timer qui affiche la moyenne des temps d'exécution à intervalle régulier d'arrêt
   *
   * @param timerName Nom du timer
   * @return true si le timer a été supprimé, false sinon
   */
  public boolean removeAutoAveragePrint(String timerName) {
    if (timerName.isEmpty()) {
      LOGGER.warn(
          TIMER_MARKER,
          LocaleManager.getLocaleResourceBundle().getString("timer.emptyName"),
          categorieName);
      return false;
    }

    Integer previousValue = autoAveragePrintInterval.remove(timerName);
    autoAveragePrintLogLevel.remove(timerName);

    if (previousValue == null) {
      LOGGER.warn(
          TIMER_MARKER,
          LocaleManager.getLocaleResourceBundle().getString("timer.noAutoAveragePrint"),
          timerName,
          categorieName);
      return false;
    }

    return true;
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    timers.clear();
    timersResults.clear();
    autoAveragePrintInterval.clear();
    autoAveragePrintLogLevel.clear();
  }
}
