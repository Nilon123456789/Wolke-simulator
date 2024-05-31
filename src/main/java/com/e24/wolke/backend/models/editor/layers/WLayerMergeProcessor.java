package com.e24.wolke.backend.models.editor.layers;

import com.e24.wolke.eventsystem.WNotifier;
import com.e24.wolke.utils.Timer;
import com.e24.wolke.utils.images.WImageUtils;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code WLayerMergeProcessor} permet de produire des donnees d'images dans un processus
 * different
 *
 * @author MeriBouisri
 */
public class WLayerMergeProcessor extends WNotifier implements Runnable {

  /** Le temps de sleep du processus */
  private static final int SLEEP_TIME_MS = 5;

  /** Le logger de la classe */
  private static final Logger LOGGER =
      LogManager.getLogger(WLayerMergeProcessor.class.getSimpleName());

  /** L'image de toutes les calques fusionnees */
  private BufferedImage mergedImage;

  /** Le tableau contenant les donnees de la fusion des calques */
  private int[] mergedImageData;

  /** L'image fusionnee des calques par-dessus le calque en cours de modification */
  private BufferedImage cachedOverLayer;

  /** L'image fusionnee des calques en-dessous du calque en cours de modification */
  private BufferedImage cachedUnderLayer;

  /** Un {@code Raster} vide servant a effacer les {@code BufferedImage} */
  private Raster emptyRaster;

  /** Valeur pour determiner si le calque en cours de modification possede des calques par-dessus */
  private boolean hasOverLayer;

  /** Valeur pour determiner si le calque en cours de modification possede des calques en-dessous */
  private boolean hasUnderLayer;

  /** Si le processus dobservation des calques est en cours */
  private boolean isRunning;

  /** Le {@code WLayerList} de cette instance */
  private WLayerList layerList;

  /** Le {@code Timer} de cette instance */
  private Timer timer = new Timer("WLayerMergeProcessor", LOGGER);

  /** Valeur pour determiner si le processus est en cours de mise a jour */
  private boolean isUpdating = false;

  /**
   * Construction d'un {@code WLayerMergeProcessor} avec un {@code WLayerList}
   *
   * @param layerList Le {@code WLayerList} de cette instance
   */
  public WLayerMergeProcessor(WLayerList layerList) {
    super();

    this.layerList = layerList;

    mergedImage = layerList.getLayerGenerator().createBufferedImage();
    this.mergedImageData = new int[this.layerList.getLayerGenerator().getSize()];

    this.cachedOverLayer = layerList.getLayerGenerator().createBufferedImage();
    this.cachedUnderLayer = layerList.getLayerGenerator().createBufferedImage();

    emptyRaster = WImageUtils.createCompatibleEmptyRaster(mergedImage);

    timer.setupAutoAveragePrint("mergeLayers", 500);
    timer.setupAutoAveragePrint("toBinaryArray", 500);
    timer.setupAutoAveragePrint("clearCachedLayers", 500);
    timer.setupAutoAveragePrint("updateCachedLayers", 500);

    initialize();
  }

  /** Initialiser une premiere fois. */
  private void initialize() {
    updateCachedLayers();
    update();
  }

  /** Methode permettant de mettre a jour la production des donnees de cette instance */
  public void update() {
    if (isUpdating) return;
    isUpdating = true;
    mergeLayers();
    toBinaryArray();
    notifyObservers();
    isUpdating = false;
  }

  /**
   * Getter pour {@code this#cachedOverLayer}, le calque par-dessus le calque en cours de
   * modification
   *
   * @return Le calque par-dessus le calque en cours de modification
   */
  public BufferedImage getCachedOverLayer() {
    return cachedOverLayer;
  }

  /**
   * Getter pour {@code this#cachedOverLayer}, le calque en-dessous de le calque en cours de
   * modification
   *
   * @return Le calque le calque en cours de modification
   */
  public BufferedImage getCachedUnderLayer() {
    return cachedUnderLayer;
  }

  /**
   * Methode permettant de supprimer le cache des image {@code this#cachedOverLayer} et {@code
   * this#cachedUnderLayer}
   */
  private void clearCachedLayers() {
    timer.start("clearCachedLayers");

    cachedOverLayer.setData(emptyRaster);
    cachedUnderLayer.setData(emptyRaster);

    timer.stop("clearCachedLayers");
  }

  /**
   * Methode permettant de memoriser les calques par-dessus et en-dessous du calque actuel en cours
   * de modification
   */
  private void updateCachedLayers() {
    timer.start("updateCachedLayers");
    clearCachedLayers();

    WLayer[] overLayers = this.layerList.getLayersAboveSelection();
    WLayer[] underLayers = this.layerList.getLayersBelowSelection();

    this.hasOverLayer = overLayers.length > 0;
    this.hasUnderLayer = underLayers.length > 0;

    if (this.hasOverLayer) this.cachedOverLayer = WLayerList.mergeLayers(overLayers);

    if (this.hasUnderLayer) this.cachedUnderLayer = WLayerList.mergeLayers(underLayers);

    timer.stop("updateCachedLayers");
  }

  /** Methode permettant de fusionner les calques du {@code this#manager} */
  private void mergeLayers() {
    timer.start("mergeLayers");

    BufferedImage tempCanvas = this.layerList.getLayerGenerator().createBufferedImage();
    Graphics2D g2d = tempCanvas.createGraphics();

    if (hasUnderLayer) g2d.drawImage(cachedUnderLayer, null, null);

    g2d.drawImage(this.layerList.getSelectedLayer().getImage(), null, null);

    if (hasOverLayer) g2d.drawImage(cachedOverLayer, null, null);

    g2d.dispose();

    mergedImage = tempCanvas;
    tempCanvas = null;

    timer.stop("mergeLayers");
  }

  /**
   * Methode permettant de convertir le {@code this#mergedImage} en tableau de donnees binaires (1
   * si le pixel est non-transparent, 0 si le pixel est transparent)
   */
  private void toBinaryArray() {
    timer.start("toBinaryArray");

    Raster alphaRaster = mergedImage.getAlphaRaster();

    if (alphaRaster == null) return;

    for (int i = 0; i < alphaRaster.getDataBuffer().getSize(); i++)
      this.mergedImageData[i] = alphaRaster.getDataBuffer().getElem(i) == 0 ? 0 : 1;

    timer.stop("toBinaryArray");
  }

  /**
   * Getter pour {@code this#mergedImage}, le {@code BufferedImage} qui represente la fusion de
   * toutes les calques
   *
   * @return Le {@code BufferedImage} qui represente la fusion de toutes les calques
   */
  public BufferedImage getMergedImage() {
    return mergedImage;
  }

  /**
   * Getter pour {@code this#mergedImageData}, le tableau contenant les donnees en nombres binaires,
   * ou 0 indique l'absence de pixel et 1 indique la presence de pixel
   *
   * @return le tableau contenant les donnees en nombres binaires, ou 0 indique l'absence de pixel
   *     et 1 indique la presence de pixel
   */
  public int[] getMergedImageData() {
    return mergedImageData;
  }

  /** Methode permettant de commencer le processus d'observation des calques */
  public void start() {
    if (isRunning) {
      LOGGER.debug("WLayerMergeProcessor already running");
      return;
    }

    updateCachedLayers();

    Thread thread = new Thread(this);

    thread.setName(getClass().getSimpleName() + " Thread");
    thread.start();
    isRunning = true;

    LOGGER.debug("WLayerMergeProcessor Thread started");
  }

  /** Arreter le processus d'observation des calques */
  public void stop() {
    if (!isRunning) {
      LOGGER.debug("WLayerMergeProcessor already stopped");
      return;
    }

    isRunning = false;

    // Mettre a jour une derniere fois
    update();

    LOGGER.debug("WLayerMergeProcessor Thread stopped");
  }

  /** Execute la boucle de fusion des calques */
  @Override
  public void run() {
    while (isRunning) {
      update();

      try {
        Thread.sleep(WLayerMergeProcessor.SLEEP_TIME_MS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Getter pour {@code this#isRunning}, pour determiner si le processus est en cours
   *
   * @return {@code true} si le processus est en cours
   */
  public boolean isRunning() {
    return isRunning;
  }
}
