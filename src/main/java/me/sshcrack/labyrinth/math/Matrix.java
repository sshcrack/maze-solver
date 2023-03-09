package me.sshcrack.labyrinth.math;

import me.sshcrack.labyrinth.Main;
import me.sshcrack.labyrinth.path.Direction;
import me.sshcrack.labyrinth.path.MazePoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class Matrix {
    public static boolean inBounds(Vec2 point) {
        int xL = Main.DIM;
        int yL = Main.DIM;

        int x = point.getX();
        int y = point.getY();

        return !(x < 0 || y < 0 || x >= xL || y >= yL);
    }

    @Nullable
    public static MazePoint getNeighbour(MazePoint[][] maze, @NotNull MazePoint point, @Nullable Direction dir) {
        if(dir == null)
            return null;

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

    @Nullable
    public static Direction getRandomSide(MazePoint point) {
        return getRandomSide(point, (d) -> true);
    }


    @Nullable
    public static Direction getRandomSide(MazePoint point, Function<Direction, Boolean> sideValid) {
        // Cloning
        List<Direction> sides = new ArrayList<>(point.getFaces());
        Direction side = null;
        while (sides.size() != 0) {

            int index = Main.random.nextInt(sides.size());
            Direction rand = sides.get(index);

            if (Matrix.getNeighbourCoords(point, rand) == null || !sideValid.apply(rand)) {
                // Invalid neighbour
                sides.remove(index);
                continue;
            }

            side = rand;
            break;
        }

        return side;
    }
}
