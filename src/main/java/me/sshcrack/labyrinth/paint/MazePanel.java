package me.sshcrack.labyrinth.paint;

import me.sshcrack.labyrinth.Main;
import me.sshcrack.labyrinth.path.MazePoint;

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

        MazePoint[][] maze = Main.maze;
        for (MazePoint[] mazePoints : maze) {
            for (MazePoint point : mazePoints) {
                point.draw(g, xOffset, yOffset);
            }
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
