package me.sshcrack.labyrinth.paint;

import me.sshcrack.labyrinth.Main;
import me.sshcrack.labyrinth.generator.MazeGenerator;
import me.sshcrack.labyrinth.math.ShortestPath;
import me.sshcrack.labyrinth.path.MazePoint;
import me.sshcrack.labyrinth.path.MazePointD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MazePanel extends JPanel implements KeyListener {
    private final int xOffset;
    private final int yOffset;
    private final int PADDING = 5;

    public MazePanel(int x, int y) {
        super();

        this.xOffset = x + PADDING;
        this.yOffset = y + PADDING;
        int size = Main.DIM * Main.RES + PADDING;

        setPreferredSize(new Dimension(size + xOffset, size + yOffset));
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        this.setBackground(Color.BLACK);
/*
        boolean generated = Main.graphics != null;
        Main.graphics = g;

        if(!generated) {
            MazeGenerator.generateMaze(Main.maze);
        }
*/
        MazePoint[][] maze = Main.maze;
        for (MazePoint[] mazePoints : maze) {
            for (MazePoint point : mazePoints) {
                point.draw(maze, g, xOffset, yOffset);
                //point.drawDebug(g, xOffset, yOffset);
            }
        }


        MazePointD solved = ShortestPath.getPath(maze, Main.start, Main.end, false);
        if(solved == null) {
            System.err.println("Could not solve maze.");
            return;
        }

        MazePoint prev = null;
        int s = Main.RES;
        int center = s / 2;
        for (MazePoint point : solved.toPath(maze)) {
            int startX = point.getX();
            int startY = point.getY();
            int endX = point.getX();
            int endY = point.getY();
            if(prev != null) {
                startX = prev.getX();
                startY = prev.getY();
            }

            prev = point;
            g.setColor(Color.YELLOW);
            g.drawLine(startX * s +xOffset + center, startY *s +yOffset + center, endX *s +xOffset + center, endY *s + yOffset + center);
        }

        for (MazePoint point : solved.toPath(maze)) {
            point.setColor(Color.yellow);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        Main.instance.generateMaze();
    }

}
