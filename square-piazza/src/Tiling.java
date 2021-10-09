import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.Color;

public class Tiling {

  private static final int displayWidth = 800;
  private static final int displayHeight = displayWidth;

  // Dir.SW means     +---+---+
  //                  |   |   |
  //                  +---+---+
  //                   SW |   |
  //                      +---+
  //
  private enum Dir {SW, NW, NE, SE}

  ;

  private Color sw, nw, ne, se;
  private double fountainRow;
  private double fountainColumn;

  public Tiling(double fountainRow, double fountainColumn) {
    this.fountainRow = fountainRow;
    this.fountainColumn = fountainColumn;
  }


  private String formatDir(Dir dir) {
    switch (dir) {
      case SW:
        return "SW";
      case NW:
        return "NW";
      case NE:
        return "NE";
      default:
        return "SE";
    }
  }


  public void colorTile(Dir directionOfTranslucent) {
    // assigning color to tiles
    int red = StdRandom.uniform(256);
    int green = StdRandom.uniform(256);
    int blue = StdRandom.uniform(256);
    Color translucent = new Color(0, 0, 0, 0);
    Color color = new Color(red, green, blue);

    switch (directionOfTranslucent) {
      case SW:
        this.sw = translucent;
        this.se = color;
        this.ne = color;
        this.nw = color;
        break;
      case SE:
        this.sw = color;
        this.se = translucent;
        this.ne = color;
        this.nw = color;
        break;
      case NE:
        this.sw = color;
        this.se = color;
        this.ne = translucent;
        this.nw = color;
        break;
      case NW:
        this.sw = color;
        this.se = color;
        this.ne = color;
        this.nw = translucent;
    }
  }

  // row and column zero based, always referencing SW block
  public void makeTile(int row, int column) {
    double squareBlock = 1;
    double halfBlock = squareBlock / 2;

    double x = ((column * squareBlock) + halfBlock);
    double y = ((row * squareBlock) + halfBlock);
    StdDraw.setPenColor(this.sw);
    StdDraw.filledSquare(x, y, halfBlock);

    StdDraw.setPenColor(this.nw);
    StdDraw.filledSquare(x, y + squareBlock, halfBlock);

    StdDraw.setPenColor(this.ne);
    StdDraw.filledSquare(x + squareBlock, y + squareBlock, halfBlock);

    StdDraw.setPenColor(this.se);
    StdDraw.filledSquare(x + squareBlock, y, halfBlock);

  }


  public void placeTile(int n, int row, int column, double fountainRow, double fountainColumn) {

    // declaring axis length and directions
    double axisLength = Math.pow(2, n);
    Dir fountainDirection;

    // variables to add row/column to axisLength/2
    double yOffset = row + (axisLength / 2);
    double xOffset = column + (axisLength / 2);

    // base case
    if (n == 0) {
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.filledSquare(1, 1, 0.5);
      return;
    }


    // assigning fountain location to a direction
    if (fountainRow >= yOffset && fountainColumn >= xOffset) {
      fountainDirection = Dir.NE;
    }
    else if (fountainRow < yOffset && fountainColumn >= xOffset) {
      fountainDirection = Dir.SE;
    }
    else if (fountainRow < yOffset && fountainColumn < xOffset) {
      fountainDirection = Dir.SW;
    }
    else {
      fountainDirection = Dir.NW;
    }

    // one two by two tile, no need for new "fountains"
    if (n == 1) {
      colorTile(fountainDirection);
      makeTile(row, column);
    }

    // making rest of image based off of new fountain location; dividing 4 times for divide and conquer
    else {
      colorTile(fountainDirection);
      makeTile((int) (yOffset - 1), (int) (xOffset - 1));
      if (fountainDirection == Dir.NW) {
        placeTile(n - 1, (int) yOffset, column, fountainRow,
                  fountainColumn);
      }
      else {
        placeTile(n - 1, (int) yOffset, column, yOffset + 0.5,
                  (xOffset - 1) + 0.5); // nw
      }
      if (fountainDirection == Dir.NE) {
        placeTile(n - 1, (int) yOffset, (int) xOffset, fountainRow,
                  fountainColumn);
      }
      else {
        placeTile(n - 1, (int) yOffset, (int) xOffset, yOffset + 0.5,
                  xOffset + 0.5); // ne
      }
      if (fountainDirection == Dir.SW) {
        placeTile(n - 1, row, column, fountainRow,
                  fountainColumn);
      }
      else {
        placeTile(n - 1, row, column, (yOffset - 1) + 0.5,
                  xOffset + 0.5); // se
      }
      if (fountainDirection == Dir.SE) {
        placeTile(n - 1, row, (int) xOffset, fountainRow,
                  fountainColumn);
      }
      else {
        placeTile(n - 1, row, (int) xOffset, yOffset + 0.5,
                  (xOffset - 1) + 0.5); // sw
      }

    }


  }

  public static void main(String[] args) {
    StdDraw.setCanvasSize(displayWidth, displayHeight);
    int n = Integer.parseInt(args[0]);
    StdDraw.setScale(0, Math.pow(2, n));
    // dimensions of grid
    double axisLength = Math.pow(2, n);

    // make row and column

    // draws grid
    for (int i = 0; i < axisLength; i++) {
      StdDraw.setPenColor(StdDraw.DARK_GRAY);
      StdDraw.line(0, i, axisLength, i);
      StdDraw.line(i, 0, i, axisLength);
    }

    double gridSize = (axisLength * axisLength);
    double squareBlock = 1;
    double halfBlock = squareBlock / 2;

    // places fountain at random location in grid
    int randomRow = (int) StdRandom.uniform(0, axisLength);
    int randomColumn = (int) StdRandom.uniform(0, axisLength);
    double fountainColumn = ((randomColumn * squareBlock) + halfBlock);
    double fountainRow = ((randomRow * squareBlock) + halfBlock);
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.filledSquare(fountainColumn, fountainRow, halfBlock);


    Tiling t = new Tiling(fountainColumn, fountainRow);
    t.placeTile(n, 0, 0, fountainRow, fountainColumn);
  }
}
