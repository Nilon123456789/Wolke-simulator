package com.e24.wolke.utils.pool;

import com.e24.wolke.backend.models.application.LocaleManager;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ObjectPool.java
 *
 * <p>Cette classe est responsable de contenir les objets réutilisables
 *
 * <p>Source : <a href="https://www.javatpoint.com/object-pool-pattern">...</a>
 *
 * @param <T> Le type d'objet à contenir
 * @author Nilon123456789
 */
public abstract class ObjectPool<T> {

  /** Temps d'intervalle de validation */
  private static final int VALIDATION_INTERVAL = 1;

  /** Piscine des objets */
  private final ConcurrentLinkedQueue<T> pool;

  /** Executeur pour s'assurer que nous avons toujours des objets entre le min et le max */
  private final ScheduledExecutorService executorService;

  /** Nombre maximal d'objet dans la piscine */
  private final int maxLength;

  /**
   * Constructeur de la classe ObjectPool
   *
   * @param minSize Nombre minimal d'objet dans la piscine
   * @param maxLength Nombre maximal d'objet dans la piscine
   */
  public ObjectPool(int minSize, int maxLength) {
    this.maxLength = maxLength;
    pool = new ConcurrentLinkedQueue<T>();

    Logger LOGGER = LogManager.getLogger(getClass().getSimpleName());

    executorService = Executors.newSingleThreadScheduledExecutor();
    executorService.scheduleWithFixedDelay(
        new Runnable() {
          @Override
          public void run() {
            int size = ObjectPool.this.pool.size();

            if (size < minSize) {
              int sizeToBeAdded = minSize + size;
              for (int i = 0; i < sizeToBeAdded; i++) {
                pool.add(createObject());
              }
              LOGGER.info(
                  LocaleManager.getLocaleResourceBundle()
                      .getString("log.objectPool.newObjectCreated"),
                  createObject().getClass().getSimpleName(),
                  Integer.valueOf(ObjectPool.this.pool.size()));
            } else if (size > maxLength) {
              int sizeToBeRemoved = size - maxLength;
              for (int i = 0; i < sizeToBeRemoved; i++) {
                ObjectPool.this.pool.poll();
              }
              LOGGER.info(
                  LocaleManager.getLocaleResourceBundle()
                      .getString("log.objectPool.objectDestroyed"),
                  createObject().getClass().getSimpleName(),
                  Integer.valueOf(ObjectPool.this.pool.size()));
            }
          }
        },
        VALIDATION_INTERVAL,
        VALIDATION_INTERVAL,
        TimeUnit.SECONDS);
  }

  /** Initialise la piscine */
  public void initialize() {
    for (int i = 0; i < this.maxLength; i++) {
      this.pool.add(createObject());
    }
  }

  /**
   * Ajoute un objet à la piscine
   *
   * @param object L'objet à ajouter
   */
  public void returnObject(T object) {
    if (object == null) return;

    if (this.pool.size() > this.maxLength) return;

    this.pool.offer(object);
  }

  /**
   * Retire un objet de la piscine
   *
   * @return L'objet retiré
   */
  public T borrowObject() {
    T object = this.pool.poll();

    if (object == null) object = createObject();

    return object;
  }

  /**
   * Crée un objet
   *
   * @return L'objet créé
   */
  protected abstract T createObject();
}
