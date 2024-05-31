package com.e24.wolke.application;

import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.simulation.physics.SimulationData;
import com.e24.wolke.backend.simulation.physics.engine.PhysicsEngine;
import com.e24.wolke.backend.simulation.physics.engine.PhysicsEngineMultiThreaded;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * Classe d'exécution de test de simulation du simulateur Wolke
 *
 * @author Nilon123456789
 */
public class Simulation {

  /** Scanner pour la saisie utilisateur */
  static Scanner scanner = new Scanner(System.in);

  /** Taille en X de la grille */
  static int xSize = 640;

  /** Taille en Y de la grille */
  static int ySize = 360;

  /** Viscosite du fluide */
  static double viscosite = 0.1;

  /** Densite volumique du fluide */
  static double volumeDensity = 0.1;

  /** Pas de temps */
  static double timeStep = 1;

  /** Nombre d'itérations */
  static int iterations = 500;

  /** Frequance d'impression */
  static int printIterations = Math.max(iterations / 10, 10);

  /** Classe non instanciable */
  private Simulation() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Main de l'application
   *
   * @param args Arguments de la ligne de commande
   */
  public static void main(String[] args) {
    LocaleManager.update(Locale.FRENCH);

    disableInfoLog();

    System.out.println("###############################################");
    System.out.println("\nTest d'exécution de la simulation du simulateur Wolke\n");

    System.out.println(
        "Cet outil vise à comparer les performances du moteur physique pour des valeurs de"
            + " simulation différentes ainsi que pour comparer la version single-threaded et"
            + " multi-threaded");
    System.out.println(
        "\tNota : En ce moment, le moteur physique multi-threaded est toujours en cours de"
            + " développement et pas encore plus performant que la version"
            + " single-threaded\n");

    System.out.println("Fait par Nilon123456789\n");
    System.out.println("###############################################\n\n");

    // Récupération des paramètres de simulation
    getUserInput();

    // Affichage des paramètres de simulation
    System.out.println("\n\n###############################################");
    System.out.println("Paramètres de simulation :");
    System.out.println("\tTaille de la grille : " + xSize + "x" + ySize);
    System.out.println("\tViscosité : " + viscosite);
    System.out.println("\tDensité volumique : " + volumeDensity);
    System.out.println("\tPas de temps : " + timeStep);
    System.out.println("\tNombre d'itérations : " + iterations);

    // Test de la version single-threaded

    System.out.println("\n###############################################");
    System.out.println("Test de la version single-threaded");

    SimulationData simData =
        new SimulationData(xSize, ySize, viscosite, volumeDensity, timeStep, 0);
    PhysicsEngine physicsEngine = new PhysicsEngine(simData);

    test(physicsEngine, simData);

    // Test de la version multi-threaded

    System.out.println("\n###############################################");
    System.out.println("Test de la version multi-threaded");

    simData = new SimulationData(xSize, ySize, viscosite, volumeDensity, timeStep, 0);
    physicsEngine = new PhysicsEngineMultiThreaded(simData);

    test(physicsEngine, simData);

    // Fin de l'application
    System.out.println("\nFin de l'application");

    scanner.close();

    System.exit(1);
  }

  /** Désactive les logs d'information */
  private static void disableInfoLog() {
    LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
    Configuration config = ctx.getConfiguration();
    LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
    loggerConfig.setLevel(Level.WARN);
    ctx.updateLoggers(); // This causes all Loggers to refetch information from their LoggerConfig.
  }

  /** Récupère les paramètres de simulation de l'utilisateur */
  private static void getUserInput() {

    int tempXSize =
        askForIntInput("Entrez la taille en X de la grille, -1 pour défaut (640) ", xSize);
    while (tempXSize < 30) {
      System.out.println("La taille minimale en X est de 30");
      tempXSize =
          askForIntInput("Entrez la taille en X de la grille, -1 pour défaut (640) ", xSize);
    }
    xSize = tempXSize;

    int tempYSize =
        askForIntInput("Entrez la taille en Y de la grille, -1 pour défaut (360) ", ySize);
    while (tempYSize < 30) {
      System.out.println("La taille minimale en Y est de 30");
      tempYSize =
          askForIntInput("Entrez la taille en Y de la grille, -1 pour défaut (360) ", ySize);
    }
    ySize = tempYSize;

    viscosite =
        askForDoubleInput(
            String.format("Entrez la viscosité du fluide, -1 pour défaut (%f) ", viscosite),
            viscosite);
    volumeDensity =
        askForDoubleInput(
            String.format(
                "Entrez la densité volumique du fluide, -1 pour défaut (%f) ", volumeDensity),
            volumeDensity);

    timeStep =
        askForDoubleInput(
            String.format("Entrez le pas de temps, -1 pour défaut (%s) ", timeStep), timeStep);
    iterations =
        askForIntInput(
            String.format("Entrez le nombre d'itérations, -1 pour défaut (%s) ", iterations),
            iterations);

    printIterations = Math.max(iterations / 10, 10);
  }

  /**
   * Demande à l'utilisateur un entier
   *
   * @param message Le message à afficher
   * @param defaultValue La valeur par défaut
   * @return L'entier entré par l'utilisateur
   */
  private static int askForIntInput(String message, int defaultValue) {
    boolean valid = false;

    int input = defaultValue;

    System.out.print(message);

    while (!valid) {
      try {
        input = scanner.nextInt();

        if (input == -1) return defaultValue;

        if (input < 0) {
          System.out.println("Veuillez entrer un entier positif");
          continue;
        }

        valid = true;
      } catch (InputMismatchException e) {
        System.out.println("InputMismatchException");
      } catch (NoSuchElementException e) {
        System.out.println("NoSuchElementException");
      } catch (IllegalStateException e) {
        System.out.println("IllegalStateException");
      }
    }

    return input;
  }

  /**
   * Demande à l'utilisateur un double
   *
   * @param message Le message à afficher
   * @param defaultValue La valeur par défaut
   * @return Le double entré par l'utilisateur
   */
  private static double askForDoubleInput(String message, double defaultValue) {
    boolean valid = false;
    double input = 0;

    System.out.print(message);
    while (!valid) {
      try {
        input = scanner.nextDouble();

        if (input == -1.0d) return defaultValue;

        if (input < 0) {
          System.out.println("Veuillez entrer un nombre positif");
          continue;
        }

        valid = true;
      } catch (Exception e) {
        System.out.println("Veuillez entrer un nombre valide");
      }
    }

    return input;
  }

  /**
   * Test de la simulation
   *
   * @param physicsEngine Le moteur physique
   * @param simData Les données de simulation
   */
  private static void test(PhysicsEngine physicsEngine, SimulationData simData) {
    for (int i = 0; i < iterations; i++) {
      physicsEngine.update(simData.getTimeStep());

      if (i % printIterations == 0) System.out.println("\nIteration " + i);
      System.out.print("*");

      simData.returnParticleMatrixToPool(simData.pollRenderParticleMatrix());
    }

    System.out.println("\nFin de la simulation\n");

    physicsEngine.printAverageTime();
  }
}
