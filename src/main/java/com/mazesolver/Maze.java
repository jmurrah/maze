package com.mazesolver;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Maze extends JFrame {
  private static final int CELL_SIZE = 15;
  private static final int PADDING = 25;
  private boolean[][] horizontalWalls;
  private boolean[][] verticalWalls;
  int rows;
  int cols;

  public Maze(int width, int height) {
    this.rows = width;
    this.cols = height;

    setTitle("Maze");
    setSize(cols * CELL_SIZE + (PADDING * 2), rows * CELL_SIZE + (PADDING * 3));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(new MazePanel());

    initializeWalls();
    generateMaze();
  }

  private class MazePanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;

      g2d.setColor(Color.WHITE);
      g2d.fillRect(0, 0, getWidth(), getHeight());

      // Draw cells with padding offset
      g2d.setColor(Color.LIGHT_GRAY);
      for (int i = 0; i <= rows; i++) {
        for (int j = 0; j <= cols; j++) {
          g2d.drawLine(
              j * CELL_SIZE + PADDING,
              PADDING,
              j * CELL_SIZE + PADDING,
              rows * CELL_SIZE + PADDING);
          g2d.drawLine(
              PADDING,
              i * CELL_SIZE + PADDING,
              cols * CELL_SIZE + PADDING,
              i * CELL_SIZE + PADDING);
        }
      }

      // Draw walls with padding offset
      g2d.setColor(Color.BLACK);
      g2d.setStroke(new BasicStroke(3));

      // Horizontal walls
      for (int i = 0; i <= rows; i++) {
        for (int j = 0; j < cols; j++) {
          if (horizontalWalls[i][j]) {
            g2d.drawLine(
                j * CELL_SIZE + PADDING,
                i * CELL_SIZE + PADDING,
                (j + 1) * CELL_SIZE + PADDING,
                i * CELL_SIZE + PADDING);
          }
        }
      }

      // Vertical walls
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j <= cols; j++) {
          if (verticalWalls[i][j]) {
            g2d.drawLine(
                j * CELL_SIZE + PADDING,
                i * CELL_SIZE + PADDING,
                j * CELL_SIZE + PADDING,
                (i + 1) * CELL_SIZE + PADDING);
          }
        }
      }
    }
  }

  private void initializeWalls() {
    horizontalWalls = new boolean[rows + 1][cols];
    verticalWalls = new boolean[rows][cols + 1];

    // Initialize all walls to true
    for (int i = 0; i <= rows; i++) {
      for (int j = 0; j < cols; j++) {
        horizontalWalls[i][j] = true;
      }
    }

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j <= cols; j++) {
        verticalWalls[i][j] = true;
      }
    }
  }

  private void generateMaze() {
    System.out.println("Generating Maze");
    new Thread(
            () -> {
              dfs(0, 0, new HashSet<>());
            })
        .start();
  }

  private void dfs(int x, int y, Set<Point> visited) {
    int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    Collections.shuffle(Arrays.asList(directions));
    visited.add(new Point(x, y));

    SwingUtilities.invokeLater(this::repaint);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      System.out.println("Error");
    }

    for (int[] direction : directions) {
      int rowOffset = direction[0], colOffset = direction[1];
      int newRow = rowOffset + x, newCol = colOffset + y;

      if (newRow >= 0
          && newRow < rows
          && newCol >= 0
          && newCol < cols
          && !visited.contains(new Point(newRow, newCol))) {
        if (rowOffset == 1) verticalWalls[y][x + 1] = false;
        if (rowOffset == -1) verticalWalls[y][x] = false;
        if (colOffset == 1) horizontalWalls[y + 1][x] = false;
        if (colOffset == -1) horizontalWalls[y][x] = false;

        dfs(x + rowOffset, y + colOffset, visited);
      }
    }
  }
}
