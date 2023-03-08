package me.sshcrack.labyrinth.generator;

import me.sshcrack.labyrinth.math.Matrix;
import me.sshcrack.labyrinth.math.ShortestPath;
import me.sshcrack.labyrinth.path.Direction;
import me.sshcrack.labyrinth.path.MazePoint;
import me.sshcrack.labyrinth.path.MazePointD;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MainPath {
    public static List<MazePoint> get(MazePoint[][] maze, MazePoint start, MazePoint end) {
        System.out.println("Getting main path...");
        ArrayList<MazePoint> path = new ArrayList<>();

        Direction startSide = Matrix.getRandomSide(start);
        if (startSide == null) {
            System.err.println("Could not find main side");
            return new ArrayList<>();
        }

        path.add(start);

        // Sides mean the point and where a way should be connected to. Neighbour should not have the opposite face.

        Supplier<Boolean> inEnd = () -> path.get(path.size() -1).isEqual(end);
        while (!inEnd.get()) {
            MazePoint lastItem = path.get(path.size() - 1);
            MazePointD d = null;
            MazePoint next = null;

            while (d == null) {
                Direction side = Matrix.getRandomSide(lastItem);
                if (side == null) {
                    System.err.println("Could not find any random side");
                    break;
                }
                next = Matrix.getNeighbour(maze, lastItem, side);
                if(next == null)
                    continue;

                List<Direction> temp = lastItem.getFaces();
                lastItem.setFaces(side);

                d = ShortestPath.getPath(maze, start, end, false);
                if(d == null) {
                    lastItem.setFaces(temp);
                    continue;
                }

                next.setFaces(next.getFaces().stream().filter(e -> e != side.opposite()).collect(Collectors.toList()));
            }

            if (d == null) {
                System.out.println("D is null");
                break;
            }
            path.add(next);
        }

        return path;
    }
}
