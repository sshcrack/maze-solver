package me.sshcrack.labyrinth.generator;

import me.sshcrack.labyrinth.Main;
import me.sshcrack.labyrinth.path.MazePoint;

import java.awt.*;
import java.util.List;

public class MazeGenerator {
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

    public static void generateMaze(MazePoint[][] maze) throws InterruptedException {
        List<MazePoint> mainPath = MainPath.get(maze, Main.start, Main.end);

        for (MazePoint mazePoint : mainPath) {
            mazePoint.setColor(Color.BLUE);
        }


        Main.start.setColor(Color.GREEN);
        Main.end.setColor(Color.RED);

        List<MazePoint> immutable = mainPath;
        while(immutable.size() < Main.DIM * Main.DIM && !Thread.currentThread().isInterrupted()) {
            immutable = Branches.generate(maze, immutable);
            MazeGeneratorThreaded.fireUpdate();
        }

    }


}
