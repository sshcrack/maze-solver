package me.sshcrack.labyrinth.math;

import me.sshcrack.labyrinth.Main;
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
    public static MazePointD getPath(MazePoint[][] maze, MazePoint start, MazePoint end, boolean debug) {
        ArrayList<MazePointD> pendingPoints = new ArrayList<>();
        ArrayList<MazePointD> connected = new ArrayList<>();
        boolean[][] visited = new boolean[Main.DIM][Main.DIM];


        pendingPoints.add(new MazePointD(start, null));
        while(!pendingPoints.isEmpty()) {
            MazePointD nextPoint = pendingPoints.remove(0);
            visited[nextPoint.getX()][nextPoint.getY()] = true;

            List<MazePointD> res = nextStep(maze, nextPoint, end, pendingPoints, visited, debug);
            connected.addAll(res);
        }

        Optional<MazePointD> lowestDistance = connected
                .stream()
                .reduce((a, b) -> a.getDistance() < b.getDistance() ? a : b);

        return lowestDistance.orElse(null);
    }

    private static ArrayList<MazePointD> nextStep(MazePoint[][] maze, MazePointD parent, MazePoint end, ArrayList<MazePointD> pendingPoints, boolean[][] visited, boolean debug) {
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

            if(debug)
                maze[point.getX()][point.getY()].setColor(Color.orange);
            pendingPoints.add(curr);
        }

        return connected;
    }

}
