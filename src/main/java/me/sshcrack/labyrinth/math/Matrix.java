package me.sshcrack.labyrinth.math;

import me.sshcrack.labyrinth.Main;
import me.sshcrack.labyrinth.path.Direction;
import me.sshcrack.labyrinth.path.MazePoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Matrix {
    public static boolean inBounds(Vec2 point) {
        int xL = Main.DIM;
        int yL = Main.DIM;

        int x = point.getX();
        int y = point.getY();

        return !(x < 0 || y < 0 || x >= xL || y >= yL);
    }

    @Nullable
    public static MazePoint getNeighbour(MazePoint[][] maze, @NotNull MazePoint point, @NotNull Direction dir) {
        Vec2 newPos = getNeighbourCoords(point, dir);
        if(newPos == null)
                return null;

        return maze[newPos.getX()][newPos.getY()];
    }

    @Nullable
    public static Vec2 getNeighbourCoords(MazePoint point, @NotNull Direction dir) {
        Vec2 pos = point.getPosition();
        Vec2 dirVec = Vec2.fromDirection(dir);

        Vec2 newPos = pos.add(dirVec);
        if(!Matrix.inBounds(newPos))
            return null;

        return newPos;
    }
}
