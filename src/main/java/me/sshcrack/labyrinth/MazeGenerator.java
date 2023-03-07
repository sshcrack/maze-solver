package me.sshcrack.labyrinth;

import me.sshcrack.labyrinth.math.Matrix;
import me.sshcrack.labyrinth.math.ShortestPath;
import me.sshcrack.labyrinth.path.Direction;
import me.sshcrack.labyrinth.path.MazePoint;
import me.sshcrack.labyrinth.path.MazePointD;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MazeGenerator {
    private static final Random r = Main.random;

    public static MazePoint[][] generateMaze(int dim) {
        MazePoint[][] maze = new MazePoint[dim][dim];
        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                maze[x][y] = new MazePoint(x, y);
                maze[x][y].setColor(Color.GRAY);
            }
        }

        MazePoint start = maze[Main.DIM -1][0];
        MazePoint end = maze[0][Main.DIM -1];
        List<MazePoint> startPath = getStartPath(maze, start, end);



        System.out.println("Getting shortest path...");
        MazePointD shortest = ShortestPath.getPath(maze, start, end);
        if(shortest != null) {
            System.err.println("Start path could be found.");

            List<MazePoint> path = shortest.toPath(maze);
            System.out.println(path);
            for (MazePoint mazePoint : path) {
                mazePoint.setColor(Color.BLUE);
            }
        }




        start.setColor(Color.GREEN);
        end.setColor(Color.RED);

        return maze;
    }




    @Nullable
    private static Direction getRandomSide(MazePoint point) {
        System.out.println("Getting random side...");
        // Cloning
        List<Direction> sides = new ArrayList<>(point.getSides());
        Direction side = null;
        while(sides.size() != 0) {

            int index = r.nextInt(sides.size());
            System.out.printf("size %s index %s %s\n", sides.size(), index, sides.size() == 0);
            Direction rand = sides.get(index);
            System.out.println(sides);

            if(Matrix.getNeighbourCoords(point, rand) == null) {
                // Invalid neighbour
                sides.remove(index);
                continue;
            }

            side = rand;
            break;
        }

        if(side == null)
            return null;

        return side;
    }

    private static List<MazePoint> getStartPath(MazePoint[][] maze, MazePoint start, MazePoint end) {
        System.out.println("Getting start path...");
        ArrayList<MazePoint> path = new ArrayList<>();

        Direction startSide = getRandomSide(start);
        if(startSide == null) {
            System.err.println("Could not find start side");
            return new ArrayList<>();
        }
        start.setSides(startSide);

        path.add(start);

        while(!path.get(path.size() -1).isEqual(end)) {
            MazePoint lastItem = path.get(path.size() -1);
            Direction side = getRandomSide(lastItem);
            break;
        }

        return path;
    }
}
