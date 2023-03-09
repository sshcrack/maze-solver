package me.sshcrack.labyrinth.math;

import me.sshcrack.labyrinth.Main;
import me.sshcrack.labyrinth.generator.MazeGeneratorThreaded;
import me.sshcrack.labyrinth.path.Direction;
import me.sshcrack.labyrinth.path.MazePoint;
import me.sshcrack.labyrinth.path.MazePointD;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShortestPath {
    @Nullable
    public static MazePointD getPath(MazePoint[][] maze, MazePoint start, MazePoint end, boolean waitVisuals) throws InterruptedException {
        return getPath(maze, start, end, waitVisuals, false);
    }

    @Nullable
    public static MazePointD getPath(MazePoint[][] maze, MazePoint start, MazePoint end, boolean waitVisuals, boolean anyPath) throws InterruptedException {
        ArrayList<MazePointD> pendingPoints = new ArrayList<>();
        ArrayList<MazePointD> connected = new ArrayList<>();
        boolean[][] visited = new boolean[Main.DIM][Main.DIM];
        Color[][] originalColors = new Color[Main.DIM][Main.DIM];


        pendingPoints.add(new MazePointD(start, null));
        while(!pendingPoints.isEmpty() && !Thread.currentThread().isInterrupted()) {
            MazePointD nextPoint = pendingPoints.remove(0);
            visited[nextPoint.getX()][nextPoint.getY()] = true;

            List<MazePointD> res = nextStep(maze, nextPoint, end, pendingPoints, visited, waitVisuals, originalColors);
            connected.addAll(res);
            if(res.size() > 0 && anyPath)
                break;
        }

        Optional<MazePointD> lowestDistance = connected
                .stream()
                .reduce((a, b) -> a.getDistance() < b.getDistance() ? a : b);

        for (int x = 0; x < Main.DIM; x++) {
            for (int y = 0; y < Main.DIM; y++) {
                Color original = originalColors[x][y];
                if(original != null)
                    maze[x][y].setColor(original);
            }
        }

        MazeGeneratorThreaded.fireUpdate(0);
        return lowestDistance.orElse(null);
    }

    private static ArrayList<MazePointD> nextStep(MazePoint[][] maze,
                                                  MazePointD parent,
                                                  MazePoint end,
                                                  ArrayList<MazePointD> pendingPoints,
                                                  boolean[][] visited,
                                                  boolean waitVisuals,
                                                  Color[][] originalColors
    ) throws InterruptedException {
        MazePoint mazeParent = maze[parent.getX()][parent.getY()];

        ArrayList<MazePointD> connected = new ArrayList<>();
        for(Direction dir :  mazeParent.getFaces()) {
            MazePoint point = Matrix.getNeighbour(maze, parent, dir);

            if(point == null)
                continue;
            if(point.getFaces().size() == 0){
                //System.out.printf("Point %s has no side %s parent %s\n",point,dir,parent);
                continue;
            }

            MazePointD curr = new MazePointD(point, parent);
            boolean alreadyVisited = visited[curr.getX()][curr.getY()];
            if(alreadyVisited)
                continue;

            visited[curr.getX()][curr.getY()] = true;
            if(point.isEqual(end)) {
                connected.add(curr);
                continue;
            }

            if(waitVisuals) {
                int x = point.getX();
                int y = point.getY();
                if(originalColors[x][y] == null)
                    originalColors[x][y] = maze[x][y].getColor();

                maze[x][y].setColor(Color.orange);
                MazeGeneratorThreaded.fireUpdate(5);
            }
            pendingPoints.add(curr);
        }

        return connected;
    }

}
