package me.sshcrack.labyrinth.generator;

import me.sshcrack.labyrinth.Main;
import me.sshcrack.labyrinth.math.ShortestPath;
import me.sshcrack.labyrinth.path.MazePoint;
import me.sshcrack.labyrinth.path.MazePointD;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class MazeGenerator {
    private static final Random r = Main.random;

    public static MazePoint[][] fillMaze(int dim) {
        MazePoint[][] maze = new MazePoint[dim][dim];
        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                maze[x][y] = new MazePoint(x, y);
                maze[x][y].setColor(Color.GRAY);
            }
        }

        Main.start = maze[Main.DIM - 1][0];
        Main.end = maze[0][Main.DIM - 1];
        return maze;
    }

    public static void generateMaze(MazePoint[][] maze) {
        List<MazePoint> mainPath = MainPath.get(maze, Main.start, Main.end);

        for (MazePoint mazePoint : mainPath) {
            mazePoint.setColor(Color.BLUE);
        }


        Main.start.setColor(Color.GREEN);
        Main.end.setColor(Color.RED);

        Branches.generate(maze, Branches.generate(maze, mainPath));
    }


}
