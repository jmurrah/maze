package com.mazesolver;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Maze extends JFrame {
    private static final int CELL_SIZE  = 25;
    private static final int PADDING = 25;
    private int[][] grid;
    private boolean[][] horizontalWalls;
    private boolean[][] verticalWalls;
    private int rows;
    private int cols;

    public Maze(int width, int height) {
        this.rows = width;
        this.cols = height;

        horizontalWalls = new boolean[rows + 1][cols];
        verticalWalls = new boolean[rows][cols + 1];
        
        setTitle("Maze");
        setSize(cols * CELL_SIZE + (PADDING * 2), rows * CELL_SIZE + (PADDING * 3));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new MazePanel());
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
                    g2d.drawLine(j * CELL_SIZE + PADDING, PADDING, 
                                j * CELL_SIZE + PADDING, rows * CELL_SIZE + PADDING);
                    g2d.drawLine(PADDING, i * CELL_SIZE + PADDING, 
                                cols * CELL_SIZE + PADDING, i * CELL_SIZE + PADDING);
                }
            }
        }
    }
    
    public void addHorizontalWall(int row, int col) {
        horizontalWalls[row][col] = true;
        repaint();
    }
    
    public void addVerticalWall(int row, int col) {
        verticalWalls[row][col] = true;
        repaint();
    }
}
