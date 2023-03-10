package me.sshcrack.labyrinth.generator;

import me.sshcrack.labyrinth.Main;
import me.sshcrack.labyrinth.math.Matrix;
import me.sshcrack.labyrinth.math.PointMath;
import me.sshcrack.labyrinth.path.Direction;
import me.sshcrack.labyrinth.path.MazePoint;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Branches {
    public static List<MazePoint> generate(MazePoint[][] maze, List<MazePoint> mainPath) throws InterruptedException {
        ArrayList<MazePoint> modified = new ArrayList<>(mainPath);
        MazePoint[][] outsides = new MazePoint[Main.DIM][Main.DIM];
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[x].length; y++) {
                outsides[x][y] = maze[x][y].clone();
            }
        }

        boolean[][] connected = new boolean[Main.DIM][Main.DIM];

        int i = 0;
        for (MazePoint point : mainPath) {
            int x = point.getX();
            int y = point.getY();

            MazePoint outsidePoint = outsides[x][y];
            PointMath.addOutsideFaces(outsides, outsidePoint);

            for (Direction face : outsidePoint.getFaces()) {
                MazePoint neighbour = Matrix.getNeighbour(maze, outsidePoint, face);
                if(neighbour == null)
                    continue;

                if(neighbour.getFaces().size() != Direction.values().length)
                    continue;

                if(!point.hasAllFaces())
                    point.addFace(face);
                else
                    point.setFaces(face);
                neighbour.setColor(Color.WHITE);
                generateBranch(maze, neighbour, connected, mainPath, modified);
                i++;
            }
        }


        return modified;
    }

    private static boolean isValidStep(MazePoint neighbour, boolean[][] connected) {
        if (neighbour == null)
            return false;

        int x = neighbour.getX();
        int y = neighbour.getY();

        return !connected[x][y];
    }

    private static void generateBranch(MazePoint[][] maze, @NotNull MazePoint branchStart, boolean[][] connected, List<MazePoint> immutablePath, List<MazePoint> modified) throws InterruptedException {
        Direction currFace = Matrix.getRandomSide(branchStart);
        boolean[][] visited = new boolean[Main.DIM][Main.DIM];

        if(currFace == null)
            return;

        modified.add(branchStart);
        MazePoint lastPoint = branchStart;
        while(!Thread.currentThread().isInterrupted()) {
            boolean isValid = isValidStep(lastPoint, connected);
            //System.out.printf("%s %s\n", lastPoint, currFace);
            if(!isValid)
                break;

            MazePoint finalLastPoint = lastPoint;
            currFace = Matrix.getRandomSide(lastPoint, (d) -> {
                MazePoint n = Matrix.getNeighbour(maze, finalLastPoint, d);
                if(n == null)
                    return true;
                return !visited[n.getX()][n.getY()];
            });
            if(currFace == null)
                break;

            MazePoint next = Matrix.getNeighbour(maze, lastPoint, currFace);
            if(next == null)
                break;


            int x = next.getX();
            int y = next.getY();

            if(visited[x][y])
                break;

            if(!next.hasAllFaces())
                break;

            next.setColor(Color.PINK);
            lastPoint.setFaces(currFace);
            lastPoint = next;
            visited[x][y] = true;
            MazeGeneratorThreaded.fireUpdate(10);
            modified.add(next);
        }

        lastPoint.setColor(Color.MAGENTA);
        MazePoint connectTo = Matrix.getNeighbour(maze, lastPoint, currFace);
        if(connectTo != null) {
            int x = lastPoint.getX();
            int y = lastPoint.getY();
            boolean shouldConnect = Main.random.nextDouble() < Main.CONNECT_PROBABILITY;
            if(shouldConnect) {
                if(connected[x][y])
                    lastPoint.addFace(currFace);
                else
                    lastPoint.setFaces(currFace);
            } else {
                if(immutablePath.stream().noneMatch(e -> e.getX() == x && e.getY() == y))
                    lastPoint.setFaces();
            }

            connected[x][y] = true;
        }
    }
}
