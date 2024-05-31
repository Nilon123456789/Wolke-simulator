package com.e24.wolke.backend.models.obstacles;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.WModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code ObstacleModel} regroupe la logique reliee a la gestion des obstacles de la
 * simulation.
 *
 * @author MeriBouisri
 */
public class ObstacleModel extends WModel {

  /** Le {@code Logger} de la classe */
  private static final Logger LOGGER =
      LogManager.getLogger(ObstacleModel.class.getClass().getSimpleName());

  /** Le {@code WObstacleCellMatrix} de cette instance */
  private WObstacleCellMatrix obstacleMatrix;

  /** Tableau contenant les donnees qui indiquent la presence d'un obstacle dans une cellule */
  private int[] obstaclePresenceData;

  /**
   * Construction d'un {@code ObstacleModel} avec un {@code Controller}. La methode {@code
   * this#initialize()} est appelee.
   *
   * @param controller Le {@code Controller} qui gere cette instance
   */
  public ObstacleModel(Controller controller) {
    super(controller);
    setupKeybindSubscriptions();

    initialize();
  }

  /** {@inheritDoc} */
  @Override
  protected boolean setup() {

    obstacleMatrix = getController().getSimulationModel().getSimulationData().getObstacle();
    obstaclePresenceData = obstacleMatrix.toBinaryArray();

    ObstacleModel.LOGGER.debug("Initialized");

    return true;
  }

  /**
   * Methode permettant d'avertir le {@code SimulationModel} d'un changement. La matrice d'obstacles
   * de {@code SimulationData} est mise a jour.
   */
  public void notifySimulationModel() {
    getController().getSimulationModel().getSimulationData().setObstacle(obstacleMatrix);
  }

  /**
   * Setter pour {@code this#obstaclePresenceData}, le tableau de donnees permettant de determiner
   * la presence d'un obstacle
   *
   * @param obstaclePresenceData Tableau de donnees binaires permettant de definir la presence d'un
   *     obstacle
   */
  public void setObstaclePresenceData(int[] obstaclePresenceData) {
    this.obstaclePresenceData = obstaclePresenceData;
  }

  /**
   * Getter pour {@code this#obstaclePresenceData}, le tableau de donnees permettant de determiner
   * la presence d'un obstacle
   *
   * @return Tableau de donnees binaires permettant de definir la presence d'un obstacle
   */
  public int[] getObstaclePresenceData() {
    return obstaclePresenceData;
  }

  /**
   * Methode permettant de mettre a jour les donnees du tableau indiquant la presence d'un obstacle.
   */
  public void retrieveObstaclePresenceData() {
    obstaclePresenceData = getController().getEditorModel().getMergedImageData();
  }

  /**
   * Getter pour {@code this#obstacleMatrix}, la matrice d'obstacles de cette instance
   *
   * @return La matrice d'obstacles de cette instance
   */
  public WObstacleCellMatrix getObstacleMatrix() {
    return obstacleMatrix;
  }

  /**
   * Methode permettant de fusionner la matrice d'obstacles actuelle avec la nouvelle matrice
   * d'obstacles.
   */
  protected void mergeObstacleData() {
    ObstacleModel.mergeObstacleData(obstacleMatrix, obstaclePresenceData);
  }

  /**
   * Methode permettant de fusionner une matrice d'obstacles avec les donnees de presence
   * d'obstacles. Cette methode opere par "side-effects", modifiant donc le {@code
   * WObstacleCellMatrix} passe en parametre.
   *
   * @param obstacleMatrix La matrice d'obstacles a modifier
   * @param obstaclePresenceData Le tableau contenant les donnees de presence d'obstacles a une
   *     position donnee
   */
  public static void mergeObstacleData(
      WObstacleCellMatrix obstacleMatrix, int[] obstaclePresenceData) {
    if (obstaclePresenceData.length != obstacleMatrix.getSize())
      throw new IndexOutOfBoundsException(
          "Le obstacleData n'a pas la meme taille que la matrice d'obstacles");

    for (int i = 0; i < obstacleMatrix.getSize(); i++) {

      // Ne pas overwrite les bordures
      if (obstacleMatrix.isBorderPosition(i)) continue;

      // Verifier s'il y a deja un obstacle dans la matrice
      if (obstacleMatrix.getElementAt(i) != null) {

        // Si est devenu non-present, overwrite avec null
        if (obstaclePresenceData[i] == 0) obstacleMatrix.setElementAt(null, i);

        continue;
      }

      // Si aucun obstacle dans la matrice, et qu'il devrait maintenant avoir un obstacle, creer un
      // nouvel obstacle
      if (obstaclePresenceData[i] == 1) obstacleMatrix.setElementAt(new WObstacleCell(), i);
    }

    obstacleMatrix.generateNormalMatrix();
    obstacleMatrix.generateAverageNormalMatrix();
  }

  /**
   * Methode a invoquer lorsque de nouveaux obstacles sont dessines. Les obstacles ne seront pas
   * calcules sur la simulation n'est pas partie
   */
  public void onNewObstacles() {
    if (!this.getController().getSimulationModel().isRunning()) return;
    this.recalculateObstacles();
  }

  /** Methode a invoquer lors du nouveau calcul d'obstacles */
  public void recalculateObstacles() {
    this.retrieveObstaclePresenceData();
    this.mergeObstacleData();
    this.notifySimulationModel();
  }

  /** {@inheritDoc} */
  public void setupKeybindSubscriptions() {}

  /** {@inheritDoc} */
  @Override
  public void softReinitialize() {
    obstacleMatrix = getController().getSimulationModel().getSimulationData().getObstacle();

    obstaclePresenceData = obstacleMatrix.toBinaryArray();
  }
}
