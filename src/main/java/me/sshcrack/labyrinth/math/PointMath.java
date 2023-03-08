package me.sshcrack.labyrinth.math;

import me.sshcrack.labyrinth.path.Direction;
import me.sshcrack.labyrinth.path.MazePoint;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PointMath {
    public static void addOutsideFaces(MazePoint[][] maze, MazePoint p) {

        List<Direction> e = Arrays.stream(Direction.values())
                .filter(x -> {
                    MazePoint neighbour = Matrix.getNeighbour(maze, p, x);
                    if(neighbour == null)
                        return false;

                    return neighbour.getFaces().size() == Direction.values().length;
                })
                .collect(Collectors.toList());

        p.setFaces(e);
    }
}
