package me.sshcrack.labyrinth;

import me.sshcrack.labyrinth.generator.MazeGenerator;
import me.sshcrack.labyrinth.paint.MazePanel;
import me.sshcrack.labyrinth.path.MazePoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class Main extends JFrame {
    public static MazePoint[][] maze;
    public static Main instance;
    public static Random random = new Random();
    public static final double CONNECT_PROBABILITY = .25;
    public static MazePoint start;
    public static MazePoint end;
    public static Graphics graphics;
    private MazePanel panel = new MazePanel(0, 0);

    public static final int DIM = 30;
    public static final int RES = 30;

    public void generateMaze() {
        System.out.println("Generate!");
        long seed = new Random().nextLong();
        random = new Random(seed);
        System.out.printf("Generating with seed %s\n",seed);
        maze = MazeGenerator.fillMaze(DIM);
        MazeGenerator.generateMaze(maze);
        refresh();
    }

//5247088568711619088
    private void refresh() {
        System.out.println("Refreshing");



        validate();
        repaint();
        pack();
    }

    public Main() {
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(DIM * RES, DIM * RES);
        setBounds(10,10,DIM * RES,DIM * RES);
        System.out.printf("Setting dim %s\n", DIM * RES);

        setBackground(Color.BLACK);
        setTitle("Maze Thingy");
        setResizable(false);
        setVisible(true);

        generateMaze();
        add(panel);
        refresh();

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                generateMaze();
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }



    public static void main(String[] args) {
        instance = new Main();
    }
}
