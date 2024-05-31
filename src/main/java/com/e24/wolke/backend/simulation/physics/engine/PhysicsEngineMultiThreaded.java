package com.e24.wolke.backend.simulation.physics.engine;

import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.backend.models.obstacles.WObstacleCell;
import com.e24.wolke.backend.models.obstacles.WObstacleCellMatrix;
import com.e24.wolke.backend.models.simulation.SimulationConstants;
import com.e24.wolke.backend.models.simulation.SimulationConstants.OBSTACLE_TYPE;
import com.e24.wolke.backend.models.simulation.SimulationModel;
import com.e24.wolke.backend.simulation.physics.ParticleMatrix;
import com.e24.wolke.backend.simulation.physics.SimulationData;
import com.e24.wolke.utils.math.WDoubleMatrix;
import com.e24.wolke.utils.math.WMath;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * PhysicsEngineMultiThreaded.java
 *
 * <p>Cette classe est responsable d'effectuer tout les calculs physiques de la simulation de fluide
 * en utilisant plusieurs threads.
 *
 * <p>Les calculs mathématique sont basé sur l'implémentation de l'article Real-Time Fluid
 * Simulation on the GPU (<a
 * href="https://fileadmin.cs.lth.se/cs/Education/EDAN35/projects/13SoderlingStrandberg_Fluid.pdf">...</a>)
 *
 * @author Nilon123456789
 */
public class PhysicsEngineMultiThreaded extends PhysicsEngine {

  /** Logger de la classe */
  protected static final Logger LOGGER =
      LogManager.getLogger(PhysicsEngineMultiThreaded.class.getSimpleName());

  /**
   * Constructeur de la classe PhysicsEngine
   *
   * @param simulationData Les données de la simulation
   */
  public PhysicsEngineMultiThreaded(SimulationData simulationData) {
    super(simulationData);
  }

  /**
   * Constructeur de la classe PhysicsEngine
   *
   * @param simulationModel Le modèle de la simulation
   */
  public PhysicsEngineMultiThreaded(SimulationModel simulationModel) {
    super(simulationModel);
  }

  /**
   * {@inheritDoc}
   *
   * <p><i> Version multi-threaded </i>
   */
  @Override
  protected void advect(double timeStep) {
    ParticleMatrix particleMatrix = simulationData.getPreviousParticleMatrix();
    ParticleMatrix particleMatrixNew = simulationData.borrowParticleMatrixFromPool();

    AdvectPartSolver.setGlobalVariables(
        particleMatrix.getXLength(),
        particleMatrix.getYLength(),
        simulationData.xPixelByMeter(),
        simulationData.yPixelByMeter(),
        timeStep,
        particleMatrix.getXVelocity().getMatrix(),
        particleMatrix.getYVelocity().getMatrix(),
        particleMatrix.getTemperature().getMatrix(),
        particleMatrix.getAreaDensity().getMatrix(),
        particleMatrix.getPressure().getMatrix(),
        particleMatrixNew.getXVelocity().getMatrix(),
        particleMatrixNew.getYVelocity().getMatrix(),
        particleMatrixNew.getTemperature().getMatrix(),
        particleMatrixNew.getAreaDensity().getMatrix(),
        particleMatrixNew.getPressure().getMatrix());

    AdvectPartSolver advectPartSolverTask = new AdvectPartSolver(0, particleMatrix.getSize());

    ApplicationConstants.FORK_JOIN_POOL.invoke(advectPartSolverTask);

    AdvectPartSolver.resetGlobalVariables();

    simulationData.setCurrentParticleMatrix(particleMatrixNew);

    applyBoundaryConditions();
  }

  /**
   * Résout l'advection pour une portion de la matrice
   *
   * @author Nilon123456789
   */
  private class AdvectPartSolver extends RecursiveAction {

    /** Sérialisation de la classe */
    private static final long serialVersionUID = 1L;

    /** Longeure en x */
    private static int xLength;

    /** Longeure en y */
    private static int yLength;

    /** taille d'un mettre x en pixel */
    private static double xMeterByPixel;

    /** taille d'un mettre y en pixel */
    private static double yMeterByPixel;

    /** pas de temps */
    private static double timeStep;

    /** vitesses en x */
    private static double[] xVelocity;

    /** vitesses en y */
    private static double[] yVelocity;

    /** temperature */
    private static double[] temperature;

    /** densité de zone */
    private static double[] areaDensity;

    /** pression */
    private static double[] pressure;

    /** nouvelle vitesse en x */
    private static double[] xVelocityNew;

    /** nouvelle vitesse en y */
    private static double[] yVelocityNew;

    /** nouvelle temperature */
    private static double[] temperatureNew;

    /** nouvelle densité de zone */
    private static double[] areaDensityNew;

    /** nouvelle pression */
    private static double[] pressureNew;

    /** Si les variables global on été définie */
    private static boolean isSetuped = false;

    /** Point de départ */
    private final int start;

    /** Point de fin */
    private final int end;

    /**
     * Initialise les variables globales.
     *
     * @param xLength Longeure en x
     * @param yLength Longeure en y
     * @param xMeterByPixel taille d'un mettre x en pixel
     * @param yMeterByPixel taille d'un mettre y en pixel
     * @param timeStep pas de temps
     * @param xVelocity vitesses en x
     * @param yVelocity vitesses en y
     * @param temperature temperature
     * @param areaDensity densité de zone
     * @param pressure pression
     * @param xVelocityNew nouvelle vitesse en x
     * @param yVelocityNew nouvelle vitesse en y
     * @param temperatureNew nouvelle temperature
     * @param areaDensityNew nouvelle densité de zone
     * @param pressureNew nouvelle pression
     */
    public static void setGlobalVariables(
        int xLength,
        int yLength,
        double xMeterByPixel,
        double yMeterByPixel,
        double timeStep,
        double[] xVelocity,
        double[] yVelocity,
        double[] temperature,
        double[] areaDensity,
        double[] pressure,
        double[] xVelocityNew,
        double[] yVelocityNew,
        double[] temperatureNew,
        double[] areaDensityNew,
        double[] pressureNew) {
      AdvectPartSolver.xLength = xLength;
      AdvectPartSolver.yLength = yLength;
      AdvectPartSolver.xMeterByPixel = xMeterByPixel;
      AdvectPartSolver.yMeterByPixel = yMeterByPixel;
      AdvectPartSolver.timeStep = timeStep;

      AdvectPartSolver.xVelocity = xVelocity;
      AdvectPartSolver.yVelocity = yVelocity;
      AdvectPartSolver.temperature = temperature;
      AdvectPartSolver.areaDensity = areaDensity;
      AdvectPartSolver.pressure = pressure;

      AdvectPartSolver.xVelocityNew = xVelocityNew;
      AdvectPartSolver.yVelocityNew = yVelocityNew;
      AdvectPartSolver.temperatureNew = temperatureNew;
      AdvectPartSolver.areaDensityNew = areaDensityNew;
      AdvectPartSolver.pressureNew = pressureNew;

      AdvectPartSolver.isSetuped = true;
    }

    /** Remet les variables globales à leur valeur par défaut (null) */
    public static void resetGlobalVariables() {
      AdvectPartSolver.isSetuped = false;
    }

    /**
     * Constructeur de la classe AdvectPartSolver
     *
     * @param start La position de départ de la résolution
     * @param end La position de fin de la résolution
     */
    public AdvectPartSolver(int start, int end) {
      this.start = start;
      this.end = end;
    }

    /** Applique l'advection pour une portion de la matrice */
    @Override
    public void compute() {
      if (!isSetuped)
        throw new IllegalStateException("Les variables globales n'ont pas été définies");

      if (end - start < ApplicationConstants.SUBTASK_THRESHOLD) {
        solve();
        return;
      }

      int mid = (int) ((this.start + this.end) * 0.5);

      invokeAll(new AdvectPartSolver(start, mid), new AdvectPartSolver(mid, end));
    }

    /** Applique l'advection pour une portion de la matrice */
    private void solve() {
      int x, y, previousXWhole, previousYWhole, pos00, pos01, pos10, pos11;
      double prevX, prevY, previousXFraction, previousYFraction, p00, p10, p01, p11;

      for (int pos = start; pos < end; pos++) {

        // Calcul de la position précédente de la particule
        x = pos % xLength;
        y = pos / xLength;

        // Calcul de la position précédente de la particule en fonction de la vitesse
        prevX = x - timeStep * xVelocity[pos] * xMeterByPixel;
        prevY = y - timeStep * yVelocity[pos] * yMeterByPixel;

        // Décomposition de la position précédente en partie entière et fractionnaire
        previousXWhole = (int) Math.floor(prevX);
        previousXFraction = prevX - previousXWhole;
        previousYWhole = (int) Math.floor(prevY);
        previousYFraction = prevY - previousYWhole;

        pos00 = getPosAtOffset(previousXWhole + previousYWhole * xLength, xLength, yLength, 0, 0);
        pos01 = getPosAtOffset(pos00, xLength, yLength, 0, 1);
        pos10 = getPosAtOffset(pos00, xLength, yLength, 1, 0);
        pos11 = getPosAtOffset(pos00, xLength, yLength, 1, 1);

        p00 = xVelocity[pos00];
        p10 = xVelocity[pos10];
        p01 = xVelocity[pos01];
        p11 = xVelocity[pos11];

        // Mise à jour de la vélocité en X
        xVelocityNew[pos] =
            WMath.normalize(WMath.bilerp(p00, p10, p01, p11, previousXFraction, previousYFraction));

        // Récupération des vélocité en Y
        p00 = yVelocity[pos00];
        p10 = yVelocity[pos10];
        p01 = yVelocity[pos01];
        p11 = yVelocity[pos11];

        // Mise à jour de la vélocité en Y

        yVelocityNew[pos] =
            WMath.normalize(WMath.bilerp(p00, p10, p01, p11, previousXFraction, previousYFraction));

        // Récupération de la température précédente
        p00 = temperature[pos00];
        p10 = temperature[pos10];
        p01 = temperature[pos01];
        p11 = temperature[pos11];

        // Mise à jour de la température
        temperatureNew[pos] =
            WMath.normalize(WMath.bilerp(p00, p10, p01, p11, previousXFraction, previousYFraction));

        // Récupération de densité de zone
        p00 = areaDensity[pos00];
        p10 = areaDensity[pos10];
        p01 = areaDensity[pos01];
        p11 = areaDensity[pos11];

        // Mise à jour de la densité de zone
        areaDensityNew[pos] =
            WMath.normalize(WMath.bilerp(p00, p10, p01, p11, previousXFraction, previousYFraction));

        // Recuperation de la pression
        // TODO : Check si System.arraycopy est plus rapide
        pressureNew[pos] = pressure[pos];
      }
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p><i> Version multi-threaded </i>
   */
  @Override
  protected void jacobiSolver(
      WDoubleMatrix x, int xLength, int yLength, double alpha, double rBeta, WDoubleMatrix b) {

    int size = x.getSize();

    if (b.getSize() != size)
      throw new IllegalArgumentException("La taille de la matrice x et b doit être égale à size");

    JacobiPartSolver jacobiSolverTask;

    WDoubleMatrix xNew = matriceArrayPool.borrowObject();

    double[] temp;

    JacobiPartSolver.setGlobalVariables(
        x.getMatrix(), xLength, yLength, alpha, rBeta, b.getMatrix(), xNew.getMatrix());

    double curentDiff;

    for (int iter = 0; iter < SimulationConstants.MAX_JACOBI_ITERATIONS; iter++) {

      applyBoundaryConditions();

      // On crée une nouvelle tâche pour résoudre la matrice
      jacobiSolverTask = new JacobiPartSolver(0, size);

      // On résout la matrice et on récupère la différence entre les deux itérations
      curentDiff =
          ApplicationConstants.FORK_JOIN_POOL.<Double>invoke(jacobiSolverTask).doubleValue();

      // On échange les matrices
      temp = x.getMatrix();
      x.setMatrix(xNew.getMatrix());
      xNew.setMatrix(temp);

      JacobiPartSolver.setGlobalVariables(
          x.getMatrix(), xLength, yLength, alpha, rBeta, b.getMatrix(), xNew.getMatrix());

      if (curentDiff < SimulationConstants.MAX_JACOBI_DIFF) break;
    }

    JacobiPartSolver.resetGlobalVariables();

    matriceArrayPool.returnObject(xNew);
  }

  /**
   * Utilise la méthode de Jacobi itérative pour résoudre des equations de poisson Equation de
   * poisson : x_{i,j}^{(k+1)}=\frac{x_{i-1,j}^{k}+x_{i+1,j}^{k}+x_{i,j-1}^{k}+x_{i,j+1}^{k}+\alpha
   * b_{i,j}}{\beta}
   *
   * @author Nilon123456789
   */
  private class JacobiPartSolver extends RecursiveTask<Double> {
    /** Sérialisation de la classe */
    private static final long serialVersionUID = 1L;

    /** La matrice a résoudre */
    private static double[] x;

    /** La taille en x de la matrice */
    private static int xLength;

    /** La taille en y de la matrice */
    private static int yLength;

    /** le coefficient alpha */
    private static double alpha;

    /** la reciproque du coefficient beta (1/beta ) */
    private static double rBeta;

    /** La matrice b (matrice doit être de la même taille que x) */
    private static double[] b;

    /** La matrice de destination */
    private static double[] xNew;

    /** Indique si les variables globales ont été définies */
    private static boolean isSetuped = false;

    /** Point de départ */
    private final int start;

    /** Point de fin */
    private final int end;

    /**
     * Constructeur de la classe JacobiPartSolver qui résoud une portion de la matrice
     *
     * @param start La position de départ de la résolution
     * @param end La position de fin de la résolution
     */
    public JacobiPartSolver(int start, int end) {
      this.start = start;
      this.end = end;
    }

    /**
     * Définit les variables globales
     *
     * @param x La matrice a résoudre
     * @param xLength La taille en x de la matrice
     * @param yLength La taille en y de la matrice
     * @param alpha le coefficient alpha
     * @param rBeta la reciproque du coefficient beta (1/beta )
     * @param b la matrice b (matrice doit être de la même taille que x)
     * @param xNew La matrice de destination
     */
    public static void setGlobalVariables(
        double[] x,
        int xLength,
        int yLength,
        double alpha,
        double rBeta,
        double[] b,
        double[] xNew) {
      JacobiPartSolver.x = x;
      JacobiPartSolver.xLength = xLength;
      JacobiPartSolver.yLength = yLength;
      JacobiPartSolver.alpha = alpha;
      JacobiPartSolver.rBeta = rBeta;
      JacobiPartSolver.b = b;
      JacobiPartSolver.xNew = xNew;

      JacobiPartSolver.isSetuped = true;
    }

    /** Remet les variables globales à leur valeur par défaut (null) */
    public static void resetGlobalVariables() {
      JacobiPartSolver.isSetuped = false;
    }

    /**
     * Résout une portion de la matrice
     *
     * @return La différence entre les deux itérations
     */
    @Override
    public Double compute() {
      if (!isSetuped)
        throw new IllegalStateException("Les variables globales n'ont pas été définies");

      if (end - start < ApplicationConstants.SUBTASK_THRESHOLD) {
        return solve();
      }

      int mid = (int) ((this.start + this.end) * 0.5);
      JacobiPartSolver left = new JacobiPartSolver(start, mid);
      JacobiPartSolver right = new JacobiPartSolver(mid, end);

      left.fork();
      return Math.min(right.compute().doubleValue(), left.join().doubleValue());
    }

    /**
     * Résout une portion de la matrice
     *
     * @return La différence entre les deux itérations
     */
    private double solve() {
      double xL,
          xR,
          xB,
          xT,
          valXNew,
          valX; // Les valeurs de x_{i-1,j}, x_{i+1,j}, x_{i,j-1}, x_{i,j+1}
      int xLPos, xRPos, xBPos, xTPos; // Les positions de x_{i-1,j}, x_{i+1,j}, x_{i,j-1}, x_{i,j+1}

      double cellDiff =
          1; // Le pourcentage de différence entre deux itérations pour un élément de la
      // matrice
      double minDiff = cellDiff;

      for (int pos = start; pos < end; pos++) {
        // On récupère les valeurs de x_{i-1,j}, x_{i+1,j}, x_{i,j-1}, x_{i,j+1}

        xLPos = getPosAtOffset(pos, xLength, yLength, -1, 0); // x_{i-1,j}
        xRPos = getPosAtOffset(pos, xLength, yLength, 1, 0); // x_{i+1,j}
        xBPos = getPosAtOffset(pos, xLength, yLength, 0, -1); // x_{i,j-1}
        xTPos = getPosAtOffset(pos, xLength, yLength, 0, 1); // x_{i,j+1}

        xL = x[xLPos]; // x_{i-1,j}
        xR = x[xRPos]; // x_{i+1,j}
        xB = x[xBPos]; // x_{i,j-1}
        xT = x[xTPos]; // x_{i,j+1}

        // On calcule la nouvelle valeur de x_{i,j}
        valXNew = xNew[pos] = WMath.normalize((xL + xR + xB + xT + alpha * b[pos]) * rBeta);

        valX = x[pos];

        if (valX == valXNew) continue;
        cellDiff = (valXNew - valX) / valX;

        if (cellDiff < 0) {
          cellDiff = -cellDiff;
        }

        minDiff = Math.min(minDiff, cellDiff);
      }

      return minDiff;
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p><i> Version multi-threaded </i>
   */
  @Override
  protected void vorticityConfinement() {
    double vorticityFactor = simulationData.getVortexConfinementFactor();

    if (vorticityFactor == 0) return;

    ParticleMatrix particleMatrix = simulationData.getCurrentParticleMatrix();

    int xLength = particleMatrix.getXLength();
    int yLength = particleMatrix.getYLength();
    int size = xLength * yLength;

    double xMeterByPixel = simulationData.xMeterByPixel();
    double yMeterByPixel = simulationData.yMeterByPixel();

    VortexConfinementPart.setGlobalVariables(
        xLength,
        yLength,
        xMeterByPixel,
        yMeterByPixel,
        vorticityFactor,
        particleMatrix.getXVelocity().getMatrix(),
        particleMatrix.getYVelocity().getMatrix(),
        particleMatrix.getVelocityCurl().getMatrix());

    VortexConfinementPart vortexConfinementTask = new VortexConfinementPart(0, size);

    ApplicationConstants.FORK_JOIN_POOL.invoke(vortexConfinementTask);

    VortexConfinementPart.resetGlobalVariables();

    applyBoundaryConditions();
  }

  /**
   * Calcule le confinement de vorticité pour une portion de la matrice
   *
   * @author Nilon123456789
   */
  private class VortexConfinementPart extends RecursiveAction {

    /** Sérialisation de la classe */
    private static final long serialVersionUID = 1L;

    /** La longueur en X de la matrice */
    private static int xLength;

    /** La longueur en Y de la matrice */
    private static int yLength;

    /** Nombre de mètres par pixel en X */
    private static double xMeterByPixel;

    /** Nombre de mètres par pixel en Y */
    private static double yMeterByPixel;

    /** Facteur de vorticité */
    private static double vorticityFactor;

    /** La matrice de vitesse x */
    private static double[] xVelocity;

    /** La matrice de vitesse y */
    private static double[] yVelocity;

    /** La matrice de curl de la vitesse */
    private static double[] velocityCurl;

    /** Si les valeurs on été définies */
    private static boolean isSetuped = false;

    /** Point de départ */
    private final int start;

    /** Point de fin */
    private final int end;

    /**
     * Définit les variables globales
     *
     * @param xLength La longueur en X de la matrice
     * @param yLength La longueur en Y de la matrice
     * @param xMeterByPixel Nombre de mètres par pixel en X
     * @param yMeterByPixel Nombre de mètres par pixel en Y
     * @param vorticityFactor Facteur de vorticité
     * @param xVelocity La matrice de vitesse x
     * @param yVelocity La matrice de vitesse y
     * @param velocityCurl La matrice de curl de la vitesse
     */
    public static void setGlobalVariables(
        int xLength,
        int yLength,
        double xMeterByPixel,
        double yMeterByPixel,
        double vorticityFactor,
        double[] xVelocity,
        double[] yVelocity,
        double[] velocityCurl) {
      VortexConfinementPart.xLength = xLength;
      VortexConfinementPart.yLength = yLength;
      VortexConfinementPart.xMeterByPixel = xMeterByPixel;
      VortexConfinementPart.yMeterByPixel = yMeterByPixel;
      VortexConfinementPart.vorticityFactor = vorticityFactor;
      VortexConfinementPart.xVelocity = xVelocity;
      VortexConfinementPart.yVelocity = yVelocity;
      VortexConfinementPart.velocityCurl = velocityCurl;

      VortexConfinementPart.isSetuped = true;
    }

    /** Remet les variables globales à leur valeur par défaut (null) */
    public static void resetGlobalVariables() {
      VortexConfinementPart.isSetuped = false;
    }

    /**
     * Constructeur de la classe VortexConfinementPart
     *
     * @param start La position de départ de la résolution
     * @param end La position de fin de la résolution
     */
    public VortexConfinementPart(int start, int end) {
      this.start = start;
      this.end = end;
    }

    /** Calcule le confinement de vorticité pour une portion de la matrice */
    @Override
    public void compute() {
      if (!isSetuped)
        throw new IllegalStateException("Les variables globales n'ont pas été définies");

      if (end - start < ApplicationConstants.SUBTASK_THRESHOLD) {
        solve();
        return;
      }

      int mid = (int) ((this.start + this.end) * 0.5);

      invokeAll(new VortexConfinementPart(start, mid), new VortexConfinementPart(mid, end));
    }

    /** Calcule le confinement de vorticité pour une portion de la matrice */
    private void solve() {
      int xTPos, xBPos, yLPos, yRPos;
      double gradX, gradY, grad, fx, fy, fac, velCurl;

      for (int pos = start; pos < end; pos++) {
        if (isCellObstructed(pos)) continue;

        xTPos = getPosAtOffset(pos, xLength, yLength, 0, 1); // x_{i,j+1}
        xBPos = getPosAtOffset(pos, xLength, yLength, 0, -1); // x_{i,j-1}
        yLPos = getPosAtOffset(pos, xLength, yLength, -1, 0); // y_{i-1,j}
        yRPos = getPosAtOffset(pos, xLength, yLength, 1, 0); // y_{i+1, j}

        velCurl =
            velocityCurl[pos] =
                0.5
                    * ((yVelocity[yRPos] - yVelocity[yLPos])
                        - (xVelocity[xTPos] - xVelocity[xBPos]));

        gradX = 0.5 * (Math.abs(velocityCurl[yRPos]) - Math.abs(velocityCurl[yLPos]));
        gradY = 0.5 * (Math.abs(velocityCurl[xTPos]) - Math.abs(velocityCurl[xBPos]));

        grad = Math.sqrt(Math.pow(gradX, 2) + Math.pow(gradY, 2));

        if (WMath.nearlyZero(grad)) continue;

        fac = 1 / grad;
        fx = fac * gradY;
        fy = -fac * gradX;

        xVelocity[pos] += vorticityFactor * fx * velCurl * xMeterByPixel;
        yVelocity[pos] += vorticityFactor * fy * velCurl * yMeterByPixel;
      }
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p><i> Version multi-threaded </i>
   */
  @Override
  protected void applyBoundaryConditions() {
    timer.start("ApplyBoundaryConditions");
    ParticleMatrix particleMatrix = simulationData.getCurrentParticleMatrix();

    ApplyBoundaryConditionsPart.setGlobalVariables(
        particleMatrix.getXLength(),
        particleMatrix.getYLength(),
        simulationData.getObstacle(),
        particleMatrix.getXVelocity().getMatrix(),
        particleMatrix.getYVelocity().getMatrix(),
        particleMatrix.getPressure().getMatrix(),
        particleMatrix.getAreaDensity().getMatrix());

    ApplyBoundaryConditionsPart applyBoundaryConditionsTask =
        new ApplyBoundaryConditionsPart(0, particleMatrix.getSize());

    ApplicationConstants.FORK_JOIN_POOL.invoke(applyBoundaryConditionsTask);

    ApplyBoundaryConditionsPart.resetGlobalVariables();

    timer.stop("ApplyBoundaryConditions");
  }

  /**
   * Applique les conditions aux bords de la simulation pour une portion de la matrice
   *
   * <p>TODO : L'implémentation n'est pas encore forcément 100% thread-safe
   *
   * <p>L'utilisation d'une matrice de particules temporaire pourrait être une solution
   *
   * @author Nilon123456789
   */
  private class ApplyBoundaryConditionsPart extends RecursiveAction {

    /** Sérialisation de la classe */
    private static final long serialVersionUID = 1L;

    /** La longueur en X de la matrice */
    private static int xLength;

    /** La longueur en Y de la matrice */
    private static int yLength;

    /** La matrice d'obstacle */
    private static WObstacleCellMatrix obstacleMatrix;

    /** La matrice de vitesse x */
    private static double[] xVelocity;

    /** La matrice de vitesse y */
    private static double[] yVelocity;

    /** La matrice de pression */
    private static double[] pressure;

    /** La matrice de densité de zone */
    private static double[] areaDensity;

    /** Si les valeurs on été définies */
    private static boolean isSetuped = false;

    /** Point de départ */
    private final int start;

    /** Point de fin */
    private final int end;

    /**
     * Définit les variables globales
     *
     * @param xLength La longueur en X de la matrice
     * @param yLength La longueur en Y de la matrice
     * @param obstacleMatrix La matrice d'obstacle
     * @param xVelocity La matrice de vitesse x
     * @param yVelocity La matrice de vitesse y
     * @param pressure La matrice de pression
     * @param areaDensity La matrice de densité de zone
     */
    public static void setGlobalVariables(
        int xLength,
        int yLength,
        WObstacleCellMatrix obstacleMatrix,
        double[] xVelocity,
        double[] yVelocity,
        double[] pressure,
        double[] areaDensity) {
      ApplyBoundaryConditionsPart.xLength = xLength;
      ApplyBoundaryConditionsPart.yLength = yLength;
      ApplyBoundaryConditionsPart.obstacleMatrix = obstacleMatrix;
      ApplyBoundaryConditionsPart.xVelocity = xVelocity;
      ApplyBoundaryConditionsPart.yVelocity = yVelocity;
      ApplyBoundaryConditionsPart.pressure = pressure;
      ApplyBoundaryConditionsPart.areaDensity = areaDensity;

      ApplyBoundaryConditionsPart.isSetuped = true;
    }

    /** Remet les variables globales à leur valeur par défaut (null) */
    public static void resetGlobalVariables() {
      ApplyBoundaryConditionsPart.isSetuped = false;
    }

    /**
     * Constructeur de la classe ApplyBoundaryConditionsPart
     *
     * @param start La position de départ de la résolution
     * @param end La position de fin de la résolution
     */
    public ApplyBoundaryConditionsPart(int start, int end) {
      this.start = start;
      this.end = end;
    }

    /** Applique les conditions aux bords de la simulation pour une portion de la matrice */
    @Override
    public void compute() {
      if (!isSetuped)
        throw new IllegalStateException("Les variables globales n'ont pas été définies");

      if (end - start < ApplicationConstants.SUBTASK_THRESHOLD) {
        solve();
        return;
      }

      int mid = (int) ((this.start + this.end) * 0.5);

      invokeAll(
          new ApplyBoundaryConditionsPart(start, mid), new ApplyBoundaryConditionsPart(mid, end));
    }

    /** Applique les conditions aux bords de la simulation pour une portion de la matrice */
    private void solve() {
      WObstacleCell obstacleCell;
      OBSTACLE_TYPE obstacle;

      double normalOrientation, nCos, nSin;

      int nPos;

      double angle, cos, sin, xV, yV, xRot, yRot;

      for (int pos = start; pos < end; pos++) {
        obstacleCell = obstacleMatrix.getElementAt(pos);

        if (obstacleCell == null) continue;

        obstacle = obstacleMatrix.getElementAt(pos).getObstacleType();

        if (obstacle == OBSTACLE_TYPE.NONE) continue;

        xVelocity[pos] = 0.0d;
        yVelocity[pos] = 0.0d;
        pressure[pos] = 0.0d;

        if (obstacle == OBSTACLE_TYPE.ZERO) continue;

        normalOrientation = obstacleCell.getAverageNormal();

        if (Double.isNaN(normalOrientation)) continue;

        nCos = Math.cos(normalOrientation);
        nSin = Math.sin(normalOrientation);

        nPos =
            getPosAtOffset(pos, xLength, yLength, (int) Math.round(nCos), (int) Math.round(nSin));
        if (isCellObstructed(nPos)) continue;

        pressure[pos] = pressure[nPos];

        switch (obstacle) {
          case INFLOW:
            xVelocity[pos] = WMath.normalize(xVelocity[nPos] * Math.abs(nCos));
            yVelocity[pos] = WMath.normalize(yVelocity[nPos] * Math.abs(nSin));
            pressure[pos] *= -1;
            break;

          case OUTFLOW:
            xVelocity[pos] = WMath.normalize(xVelocity[nPos] * Math.abs(nCos));
            yVelocity[pos] = WMath.normalize(yVelocity[nPos] * Math.abs(nSin));
            areaDensity[pos] = areaDensity[nPos];
            break;

          case SLIP:
            angle = WMath.PI_OVER_2 - normalOrientation;
            cos = Math.cos(angle);
            sin = Math.sin(angle);
            xV = xVelocity[nPos];
            yV = yVelocity[nPos];

            // Rotation de la vélocité sur la normale
            // Inversion du sens de la vélocité parallele à la normale
            xRot = xV * cos - yV * sin;
            yRot = -1 * (xV * sin + yV * cos);

            // Dérotation de la vélocité
            xVelocity[pos] = WMath.normalize(xRot * cos + yRot * sin);
            yVelocity[pos] = WMath.normalize(-1 * (xRot * sin - yRot * cos));

            break;

          case STICK:
            xVelocity[pos] = -xVelocity[nPos];
            yVelocity[pos] = -yVelocity[nPos];
            break;

          default:
        }
      }
    }
  }
}
