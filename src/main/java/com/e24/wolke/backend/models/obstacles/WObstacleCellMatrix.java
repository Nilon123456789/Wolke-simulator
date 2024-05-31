package com.e24.wolke.backend.models.obstacles;

import com.e24.wolke.backend.models.simulation.SimulationConstants.BORDER_TYPE;
import com.e24.wolke.backend.models.simulation.SimulationConstants.OBSTACLE_TYPE;
import com.e24.wolke.backend.models.simulation.SimulationConstants.UnitCircle;
import com.e24.wolke.utils.math.WMatrix1D;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * La classe {@code WObstacleCellMatrix}
 *
 * @author MeriBouisri
 * @author Nilon123456789
 */
public class WObstacleCellMatrix extends WMatrix1D<WObstacleCell> {

  /** Matrice des angles de la normale appliquees par les particules d'obstacles */
  private WNormalCellMatrix normalMatrix;

  /** Le tableau contenant les indices de la bordure */
  private int[] borderIndices;

  /** Si cette matrice d'obstacles a une bordure */
  private boolean hasBorder;

  /**
   * Construction d'un {@code WObstacleMatrix} avec une longueur en x et en y. La matrice a une
   * bordure par defaut.
   *
   * @param xLength la longueur en x de la matrice d'obstacles
   * @param yLength la longueur en y de la matrice d'obstacles
   */
  public WObstacleCellMatrix(int xLength, int yLength) {
    this(xLength, yLength, true);
    normalMatrix = new WNormalCellMatrix(this);
  }

  /**
   * Construction d'un {@code WObstacleMatrix} avec une longueur en x et en y
   *
   * @param xLength la longueur en x de la matrice d'obstacles
   * @param yLength la longueur en y de la matrice d'obstacles
   * @param hasBorder Si la matrice d'obstacles a une bordure
   */
  public WObstacleCellMatrix(int xLength, int yLength, boolean hasBorder) {
    super(xLength, yLength);
    normalMatrix = new WNormalCellMatrix(this);
    this.hasBorder = hasBorder;
    borderIndices = super.getBorderIndices();
  }

  /** {@inheritDoc} */
  @Override
  public int[] getBorderIndices() {
    return borderIndices;
  }

  /** Genere la matrice d'orientation des normales */
  public void generateNormalMatrix() {
    for (int i = 0; i < getSize(); i++) {

      if (this.getElementAt(i) == null) continue;

      applyNormalAround(getElementAt(i), i);
    }
  }

  /**
   * Methode permettant de generer la moyenne des normales du voisinage d'un {@code WObstacleCell}
   * et d'y attribuer une valeur d'angle de la normale a la position de l'obstacle. Cette methode
   * met a jour le champ {@code WObstacleCell#averageNormal} des {@code WObstacleCell}.
   *
   * @see WObstacleCellMatrix#toAverageNormalArray()
   */
  public void generateAverageNormalMatrix() {
    for (int i = 0; i < getSize(); i++) {
      if (this.getElementAt(i) == null) continue;

      calculateAverageNormalAround(getElementAt(i), i);
    }
  }

  /**
   * Methode permettant de calculer l'angle de la normale appliquee par l'obstacle passé en
   * paramètre sur les cellules voisines
   *
   * @param obstacle L'obstacle autour duquel les normales doivent etre calculees
   * @param pos position de l'obstacle
   */
  public void applyNormalAround(WObstacleCell obstacle, int pos) {
    int[] obstaclePos = getPos(pos);
    obstacle.resetAverageNormal();
    obstacle.setPos(obstaclePos[0], obstaclePos[1]);

    int[] neighborPositions = getNeighborPositions(pos);

    for (int n : neighborPositions) {

      if (n == -1) continue;

      int[] nPos = getPos(n);
      WNormalCell normal = normalMatrix.getElementAt(n);

      if (normal == null) {
        normal = new WNormalCell();
        normal.setPos(nPos[0], nPos[1]);
        normalMatrix.setElementAt(normal, n);
      }

      obstacle.applyNormalTo(normalMatrix.getElementAt(n));
    }
  }

  /**
   * Methode permettant de determiner la moyenne des angles des normales autour d'un obstacle.
   *
   * @param obstacle L'obstacle sur lequel calculer la moyenne des normales
   * @param pos La position de l'obstacle
   */
  public void calculateAverageNormalAround(WObstacleCell obstacle, int pos) {
    int[] obstaclePos = getPos(pos);
    obstacle.setPos(obstaclePos[0], obstaclePos[1]);

    int[] neighborPositions = getNeighborPositions(pos);

    int count = 0;
    double sum = 0;

    for (int n : neighborPositions) {
      if (n == -1) continue;

      // Ne pas compter les normales autour s'il y a un obstacle a la position
      if (this.getElementAt(n) != null) continue;

      WNormalCell normal = normalMatrix.getElementAt(n);

      if (normal == null) continue;

      if (normal.isNullOrientation()) continue;

      sum += normal.getNormalOrientation();
      count++;
    }

    obstacle.setAverageNormal(sum / count);
  }

  /**
   * Methode permettant de convertir la matrice d'obstacles en matrice de valeurs 0 et 1, ou une
   * valeur 1 indique la presence d'un obstacle, et la valeur 0 indique l'absence d'un obstacle.
   *
   * @return Un tableau de 0 et 1, indiquant l'absence ou la presence d'un obstacle a une position
   *     donnee
   */
  public int[] toBinaryArray() {
    int[] bin = new int[getSize()];

    for (int i = 0; i < bin.length; i++) bin[i] = this.getElementAt(i) == null ? 0 : 1;

    return bin;
  }

  /**
   * Methode permettant d'obtenir un {@code array} des valeurs de la normale moyenne de chaque
   * {@code WObstacle}.
   *
   * @return Un {@code array} des valeurs de la normale moyenne de chaque {@code WObstacle}.
   * @see WObstacleCellMatrix#generateAverageNormalMatrix() Methode a appeler pour mettre a jour les
   *     normales
   */
  public double[] toAverageNormalArray() {
    double[] averageNormals = new double[getSize()];

    for (int i = 0; i < averageNormals.length; i++) {
      if (this.getElementAt(i) == null) {
        averageNormals[i] = UnitCircle.NONE.getAngleRad();
        continue;
      }

      averageNormals[i] = getElementAt(i).getAverageNormal();
    }

    return averageNormals;
  }

  /**
   * Methode permettant de convertir la matrice d'obstacle en tableau de donnees des types de
   * bordure {@code OBSTACLE_TYPE} des cellules d'obstacles
   *
   * @return Le tableau {@code BORDER_TYPE[]} contenant les donnees des types de bordure des
   *     cellules d'obstacles
   */
  public OBSTACLE_TYPE[] toObstacleTypeArray() {
    OBSTACLE_TYPE[] obstacleType = new OBSTACLE_TYPE[getSize()];

    for (int i = 0; i < obstacleType.length; i++)
      obstacleType[i] =
          getElementAt(i) == null ? OBSTACLE_TYPE.NONE : getElementAt(i).getObstacleType();

    return obstacleType;
  }

  /**
   * Getter pour {@code this#normalMatrix}
   *
   * @return La matrice contenant les orienations des normales des cellules voisines a celles des
   *     obstacles
   */
  public WNormalCellMatrix getNormalMatrix() {
    return normalMatrix;
  }

  /**
   * Methode permettant de convertir cette instance en {@code BufferedImage}. Un pixel de couleur
   * {@code rgb} sera dessine aux positions dans lesquelles un {@code WObstacleCell} est present
   * dans cette instance.
   *
   * @param rgb La couleur avec laquelle les pixels doivent etre dessines
   * @return Le {@code BufferedImage} dessine a partir de cette instance
   */
  public BufferedImage toBufferedImage(int rgb) {
    BufferedImage image =
        new BufferedImage(getXLength(), getYLength(), BufferedImage.TYPE_INT_ARGB);
    toBufferedImage(image, rgb);
    return image;
  }

  /**
   * Methode permettant de dessiner les obstacles sur un {@code BufferedImage} donne. Un pixel de
   * couleur {@code rgb} sera dessine aux positions dans lesquelles un {@code WObstacleCell} est
   * present dans cette instance.
   *
   * @param image Le {@code BufferedImage} sur lequel les pixels seront dessine
   * @param rgb La couleur avec laquelle les pixels doivent etre dessines
   */
  public void toBufferedImage(BufferedImage image, int rgb) {
    int w = Math.min(image.getWidth(), getXLength());
    int h = Math.min(image.getHeight(), getYLength());

    for (int i = 0; i < w; i++)
      for (int j = 0; j < h; j++) if (getElementAt(i, j) != null) image.setRGB(i, j, rgb);
  }

  /**
   * Methode permettant de convertir un objet {@code BufferedImage} en objet {@code
   * WObstacleCellMatrix}. Les pixels non-transparents deviennent des cellules d'obstacle.
   *
   * @param image Le {@code BufferedImage} a convertir
   * @return un {@code WObstacleCellMatrix} généré a partir du {@code BufferedImage}
   */
  public static WObstacleCellMatrix fromBufferedImage(BufferedImage image) {

    WObstacleCellMatrix obstacleMatrix =
        new WObstacleCellMatrix(image.getWidth(), image.getHeight());

    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        int rgb = image.getRGB(i, j);

        if (new Color(rgb, true).getAlpha() != 0)
          obstacleMatrix.setElementAt(new WObstacleCell(), i, j);
      }
    }

    return obstacleMatrix;
  }

  /**
   * Methode utilitaire pour construire un obstacle rectangulaire
   *
   * @param x Position x du coin en haut a gauche
   * @param y Position y du coin en haut a gauche
   * @param width longueur horizontale du rectangle
   * @param height longueur verticale du rectangle
   */
  public void drawRectangleObstacle(int x, int y, int width, int height) {
    for (int i = x; i < x + width; i++) {
      for (int j = y; j < y + height; j++) {
        setElementAt(new WObstacleCell(), i, j);
      }
    }
  }

  /**
   * Methode utilitaire pour construire la bordure de la matrice d'obstacles
   *
   * @param borderType le type de bordure a construire
   */
  public void drawBorder(BORDER_TYPE borderType) {
    switch (borderType) {
      case WIND_TUNNEL:
        drawWindTunnelBorders();
        break;
      case BOX:
        drawBoxBorders();
        break;
      case OPEN:
        drawOpenBorders();
        break;
    }

    generateNormalMatrix();
    generateAverageNormalMatrix();
  }

  /** Methode utilitaire pour construire les bordure pour simuler un tunnel de vent */
  public void drawWindTunnelBorders() {
    for (int pos = 0; pos < getSize(); pos++) {
      if (pos < getXLength() || pos > getSize() - getXLength())
        setElementAt(new WObstacleCell(-1, -1, OBSTACLE_TYPE.SLIP), pos);
      else if (pos % getXLength() == 0)
        setElementAt(new WObstacleCell(-1, -1, OBSTACLE_TYPE.INFLOW), pos);
      else if (pos % getXLength() == getXLength() - 1)
        setElementAt(new WObstacleCell(-1, -1, OBSTACLE_TYPE.OUTFLOW), pos);
    }
  }

  /** Methode utilitaire pour construire les bordure pour simuler une boite */
  public void drawBoxBorders() {
    for (int pos = 0; pos < getSize(); pos++) {
      if (pos < getXLength()
          || pos > getSize() - getXLength()
          || pos % getXLength() == 0
          || pos % getXLength() == getXLength() - 1)
        setElementAt(new WObstacleCell(-1, -1, OBSTACLE_TYPE.SLIP), pos);
    }
  }

  /** Methode utilitaire pour construire les bordure pour simuler un monde infinie */
  public void drawOpenBorders() {
    for (int pos = 0; pos < getSize(); pos++) {
      if (pos < getXLength()
          || pos > getSize() - getXLength()
          || pos % getXLength() == 0
          || pos % getXLength() == getXLength() - 1)
        setElementAt(new WObstacleCell(-1, -1, OBSTACLE_TYPE.OUTFLOW), pos);
    }
  }

  /**
   * Getter pour {@code this#hasBorder}, qui permet de definir si cette matrice d'obstacles a une
   * bordure
   *
   * @return {@code true} si cette matrice d'obstacles a une bordure
   */
  public boolean hasBorder() {
    return hasBorder;
  }

  /** {@inheritDoc} */
  @Override
  protected WObstacleCell[] createGenericArray() {
    return new WObstacleCell[getSize()];
  }
}
