package com.e24.wolke.backend.models.obstacles;

import com.e24.wolke.backend.models.simulation.SimulationConstants.OBSTACLE_TYPE;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester les fonctionnalites de {@code ObstacleModel}
 *
 * @author MeriBouisri
 */
public class ObstacleModelTest {

  /** Test pour {@code ObstacleModel#mergeObstacleData()} */
  @Test
  public void testMergeObstacleData00() {
    int w = 5;
    int h = 5;
    WObstacleCellMatrix obstacleMatrix = new WObstacleCellMatrix(w, h, true);

    obstacleMatrix.drawWindTunnelBorders();

    int[] obstaclePresenceData = {
      1, 1, 1, 1, 1,
      1, 0, 0, 0, 1,
      1, 0, 0, 0, 1,
      1, 0, 0, 0, 1,
      1, 1, 1, 1, 1,
    };

    OBSTACLE_TYPE[] expected = obstacleMatrix.toObstacleTypeArray();

    ObstacleModel.mergeObstacleData(obstacleMatrix, obstaclePresenceData);

    OBSTACLE_TYPE[] actual = obstacleMatrix.toObstacleTypeArray();

    Assert.assertArrayEquals(
        "expected=\n" + Arrays.toString(expected) + "\n actual=\n" + Arrays.toString(actual),
        expected,
        actual);
  }

  /** Test pour {@code ObstacleModel#mergeObstacleData()} */
  @Test
  public void testMergeObstacleData01() {
    int w = 5;
    int h = 5;
    int pos = 12;
    WObstacleCellMatrix obstacleMatrix = new WObstacleCellMatrix(w, h, true);

    obstacleMatrix.drawWindTunnelBorders();

    int[] obstaclePresenceData = {
      1, 1, 1, 1, 1,
      1, 0, 0, 0, 1,
      1, 0, 0, 0, 1,
      1, 0, 0, 0, 1,
      1, 1, 1, 1, 1,
    };

    obstaclePresenceData[pos] = 1;
    obstacleMatrix.setElementAt(new WObstacleCell(OBSTACLE_TYPE.STICK), pos);

    OBSTACLE_TYPE[] expected = obstacleMatrix.toObstacleTypeArray();

    ObstacleModel.mergeObstacleData(obstacleMatrix, obstaclePresenceData);

    OBSTACLE_TYPE[] actual = obstacleMatrix.toObstacleTypeArray();

    Assert.assertArrayEquals(
        "expected=\n" + Arrays.toString(expected) + "\n actual=\n" + Arrays.toString(actual),
        expected,
        actual);
  }
}
