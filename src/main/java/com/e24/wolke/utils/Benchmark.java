package com.e24.wolke.utils;

import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.simulation.physics.SimulationData;
import com.e24.wolke.backend.simulation.physics.engine.PhysicsEngine;
import com.e24.wolke.backend.simulation.physics.engine.PhysicsEngineMultiThreaded;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Benmark.java
 *
 * <p>Cette classe consite à offrir différentes méthodes pour mesurer le temps d'exécution de
 * l'application.
 *
 * @author Nilon123456789
 */
public class Benchmark {

  /** Nombre d'itérations pour les tests */
  private static final int TEST_ITERATIONS = 100;

  /** Tolerance d'erreur pour les tests */
  private static final double TOLERANCE = 0.05;

  /** Timer de la classe */
  private static final Timer timer = new Timer("Benchmark");

  /** Logger de la classe */
  protected static final Logger LOGGER = LogManager.getLogger(Benchmark.class.getSimpleName());

  /** Classe non instanciable */
  private Benchmark() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Détermine si la simulation s'exécute plus rapidement en utilisant plusieurs threads
   *
   * @return Vrai si la simulation s'exécute plus rapidement en utilisant plusieurs threads
   */
  public static boolean shouldMultiThread() {
    timer.start("shouldMultiThread");

    LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.benchmark.singleVsMulti.started"));

    SimulationData dataSingle = new SimulationData();
    SimulationData dataMulti = new SimulationData();

    PhysicsEngine singleThread = new PhysicsEngine(dataSingle);
    PhysicsEngine multiThread = new PhysicsEngineMultiThreaded(dataMulti);

    for (int i = 0; i < TEST_ITERATIONS; i++) {
      singleThread.update(dataSingle.getTimeStep());
      multiThread.update(dataMulti.getTimeStep());

      // On retourne les matrices de particules dans le pool puisqu'on ne les utilise pas
      dataSingle.returnParticleMatrixToPool(dataSingle.pollRenderParticleMatrix());
      dataMulti.returnParticleMatrixToPool(dataMulti.pollRenderParticleMatrix());

      if (i % 10 == 0) System.out.print("\nIteration " + i + " / " + TEST_ITERATIONS + "\n");
      System.out.print("*");
    }
    System.out.println("\n");

    long singleThreadTime = singleThread.getAverageStepExecutionTime();
    long multiThreadTime = multiThread.getAverageStepExecutionTime();

    double time = timer.stop("shouldMultiThread");
    LOGGER.info(
        LocaleManager.getLocaleResourceBundle()
                .getString("log.benchmark.singleVsMulti.singleResult")
            + singleThreadTime
            + "ms");
    LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.benchmark.singleVsMulti.multiResult")
            + multiThreadTime
            + "ms");
    LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.benchmark.singleVsMulti.finished")
            + time
            + "ms");

    // On calcule le pourcentage de différence entre les deux temps voir si ce n'est pas
    // significatif
    double diffPercentage =
        Math.abs((double) (multiThreadTime - singleThreadTime) / singleThreadTime);
    if (diffPercentage < TOLERANCE) return false;

    return multiThreadTime < singleThreadTime;
  }
}
