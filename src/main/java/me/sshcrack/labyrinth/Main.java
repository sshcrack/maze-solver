package me.sshcrack.labyrinth;

import me.sshcrack.labyrinth.paint.MazePanel;
import me.sshcrack.labyrinth.path.MazePoint;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main extends JFrame {
    public static MazePoint[][] maze;
    public static Main instance;
    public static Random random = new Random();

    public static final int DIM = 30;
    public static final int RES = 30;

    public void generateMaze() {
        System.out.println("Generate!");
        maze = MazeGenerator.generateMaze(DIM);
        refresh();
    }


    private void refresh() {
        System.out.println("Refreshing");
        MazePanel panel = new MazePanel(0, 0);
        add(panel);

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
    }



    public static void main(String[] args) {
        instance = new Main();
    }
}
