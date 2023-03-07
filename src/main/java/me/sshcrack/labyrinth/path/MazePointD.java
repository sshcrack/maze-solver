package me.sshcrack.labyrinth.path;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MazePointD extends MazePoint {
    private final int distance;
    private final MazePointD parent;

    public MazePointD(MazePoint point, @Nullable MazePointD parent) {
        super(point.getX(), point.getY());

        this.distance = parent == null ? 0 : parent.getDistance() +1;
        this.parent = parent;
    }

    public MazePointD(int x, int y, @Nullable MazePointD parent) {
        super(x, y);

        this.parent = parent;
        this.distance = parent == null ? 0 : parent.getDistance() +1;
    }

    @Nullable
    public MazePointD getParent() {
        return parent;
    }

    public int getDistance() {
        return distance;
    }

    public List<MazePoint> toPath(MazePoint[][] maze) {
        ArrayList<MazePoint> path = new ArrayList<>();
        path.add(this);

        MazePointD curr = this;
        while((curr = curr.getParent()) != null) {
            path.add(maze[curr.getX()][curr.getY()]);
        }

        Collections.reverse(path);
        return path;
    }
}
