package com.e24.wolke.backend.models.console;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.WModel;
import com.e24.wolke.backend.models.console.ConsoleConstants.WLogLevel;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.frontend.console.ConsoleFrame;
import com.e24.wolke.utils.logger.WConsole;
import com.e24.wolke.utils.logger.WLogEvent;
import java.io.FileOutputStream;
import java.util.LinkedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code ConsoleModel} sert a gerer la logique de la console ainsi que ses fonctions de
 * filtration et de l'exposer au {@code Controller}.
 *
 * @author n-o-o-d-l-e
 * @author Nilon123456789
 * @author MeriBouisri
 */
public class ConsoleModel extends WModel {

  /** Le {@code Logger} de la classe */
  private static final Logger LOGGER =
      LogManager.getLogger(ConsoleModel.class.getClass().getSimpleName());

  /** Instance de la fenetre de la console */
  private ConsoleFrame consoleFrame;

  /** Le texte actuel de la console, contenant tous les logs des niveaux souhaités */
  private String currentLogs;

  /** Le niveau de log actuel */
  private WLogLevel currentLogLevel = ConsoleConstants.DEFAULT_LOG_FILTER;

  /** Liste des classes dont les logs devraient être affichées */
  private LinkedList<String> currentLogClassNames = new LinkedList<>();

  /** La visibilité de la console */
  private boolean consoleVisibility;

  /**
   * Construction d'un {@code ConsoleModel} avec paramètres par défaut. La methode {@code
   * this#initialize()} est appelee.
   *
   * @param controller Le {@code Controller} qui gere cette instance.
   */
  public ConsoleModel(Controller controller) {
    this(controller, ConsoleConstants.DEFAULT_CONSOLE_VISIBILITY);
    WConsole.setConsoleModel(this);
  }

  /**
   * Construction d'un {@code ConsoleModel} avec un {@code Controller} et une visibilité de la
   * console. La methode {@code this#initialize()} est appelee.
   *
   * @param controller Le {@code Controller} qui gere cette instance.
   * @param consoleVisibility La visibilité de la console.
   */
  public ConsoleModel(Controller controller, boolean consoleVisibility) {
    super(controller);
    setupKeybindSubscriptions();
    this.consoleVisibility = consoleVisibility;

    initialize();
  }

  /** {@inheritDoc} */
  @Override
  protected boolean setup() {

    ConsoleModel.LOGGER.debug("Initialized");

    return true;
  }

  /**
   * Setter pour la visibilité de la console
   *
   * @param visibility la nouvelle visibilité de la console
   */
  public void setConsoleVisibility(boolean visibility) {
    this.consoleVisibility = visibility;
    if (consoleFrame == null) {
      this.consoleFrame = new ConsoleFrame(getController());
    }

    if (visibility) this.consoleFrame.start();
    else this.consoleFrame.stop();

    this.consoleFrame.setVisible(visibility);
    getPublisher().publish(Subject.ON_APP_CONSOLE_VISIBILITY_CHANGED, visibility);
  }

  /**
   * Getter pour la visibilité de la console
   *
   * @return la visibilité de la console
   */
  public boolean isConsoleVisible() {
    return this.consoleVisibility;
  }

  /**
   * Retourne le niveau de log actuel.
   *
   * @return Le niveau de log actuel.
   */
  public WLogLevel getCurrentLogLevel() {
    return this.currentLogLevel;
  }

  /**
   * Change le niveau de log actuel.
   *
   * @param currentLogLevel Le nouveau niveau de log.
   */
  public void setCurrentLogLevel(WLogLevel currentLogLevel) {
    this.currentLogLevel = currentLogLevel;
  }

  /**
   * Retourne la liste des classes dont les logs devraient être affichées.
   *
   * @return La liste des classes dont les logs devraient être affichées.
   */
  public LinkedList<String> getCurrentLogClassNames() {
    return currentLogClassNames;
  }

  /**
   * Change la liste des classes dont les logs devraient être affichées.
   *
   * @param currentLogClassNames La nouvelle liste des classes dont les logs devraient être
   *     affichées.
   */
  public void setCurrentLogClassNames(LinkedList<String> currentLogClassNames) {
    this.currentLogClassNames = currentLogClassNames;
  }

  /**
   * Retourne le texte actuel de la console.
   *
   * @return Le texte actuel de la console.
   */
  public String getCurrentLogs() {
    LinkedList<String> logs =
        WConsole.filterMessageByClassAndLevel(currentLogClassNames, currentLogLevel.getLevel());
    StringBuilder sb = new StringBuilder();
    for (String log : logs) {
      if (log == null) continue;
      sb.append(log);
    }
    this.currentLogs = sb.toString();
    return this.currentLogs;
  }

  /**
   * Methode a invoquer lorsqu'un nouveau log est recu.
   *
   * @param event Le log recu.
   */
  public void onNewLog(WLogEvent event) {
    getPublisher().publish(Subject.ON_CONSOLE_LOGS_CHANGED, event);
  }

  /** Efface les logs de la console. */
  public static void clearLogs() {
    WConsole.clear();
  }

  /**
   * Export les logs de la console au format html.
   *
   * @param directory Le dossier de destination.
   * @param file Le nom du fichier.
   */
  public static void exportLogs(String directory, String file) {

    if (file == null || directory == null) {
      return;
    }

    String htmlLog = WConsole.generateHtmlLogs();

    try {
      FileOutputStream fos = new FileOutputStream(directory + file);
      fos.write(htmlLog.getBytes());
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** {@inheritDoc} */
  public void setupKeybindSubscriptions() {}

  /** {@inheritDoc} */
  @Override
  public void softReinitialize() {
    currentLogClassNames.clear();
    currentLogLevel = ConsoleConstants.DEFAULT_LOG_FILTER;
    currentLogs = "";

    if (consoleFrame != null) {
      consoleFrame.dispose();
      consoleFrame = null;
    }

    consoleFrame = new ConsoleFrame(getController());
    setConsoleVisibility(ConsoleConstants.DEFAULT_CONSOLE_VISIBILITY);
  }
}
