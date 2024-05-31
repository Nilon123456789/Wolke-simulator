package com.e24.wolke.backend.models.simulation;

import com.e24.wolke.backend.models.simulation.SimulationConstants.BORDER_TYPE;
import com.e24.wolke.filesystem.properties.WPropertyKey;
import java.util.Properties;
import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester les fonctionnalites de {@code SimulationProperties}
 *
 * @author MeriBouisri
 */
public class SimulationPropertiesTest {

  /**
   * Methode permettant de tester {@code SimulationProperties#readStandardProperties(Properties)}
   */
  @Test
  public void testReadStandardProperties() {
    SimulationProperties properties = new SimulationProperties();

    Properties stdProperties = new Properties();

    stdProperties = properties.writeStandardProperties();

    Assert.assertTrue(stdProperties.containsKey(WPropertyKey.SIMULATION_BORDER_TYPE.getKey()));
    Assert.assertTrue(stdProperties.containsKey(WPropertyKey.SIMULATION_VISCOSITY.getKey()));
    Assert.assertTrue(stdProperties.containsKey(WPropertyKey.SIMULATION_VOLUME_DENSITY.getKey()));

    System.out.println(stdProperties.toString());

    BORDER_TYPE expectedBorder = BORDER_TYPE.BOX;
    double expectedViscosity = SimulationConstants.DEFAULT_VISCOSITY + 1;

    WPropertyKey.SIMULATION_BORDER_TYPE.write(expectedBorder.ordinal(), stdProperties);
    WPropertyKey.SIMULATION_VISCOSITY.write(expectedViscosity, stdProperties);

    properties.readStandardProperties(stdProperties);

    BORDER_TYPE actualBorder = properties.borderType;
    double actualViscosity = properties.viscosity;

    Assert.assertEquals(expectedBorder, actualBorder);
    Assert.assertEquals(expectedViscosity, actualViscosity, 0);
  }
}
