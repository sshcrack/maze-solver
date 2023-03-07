package me.sshcrack.labyrinth.math;

import me.sshcrack.labyrinth.path.Direction;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.alg.util.Pair;

public class Vec2Dir {
    protected final int x;
    protected final int y;
    public Vec2Dir(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Vec2 fromDirection(@NotNull Direction dir) {
        Vec2 res = null;
        switch (dir) {
            case LEFT -> res = new Vec2(-1, 0);
            case TOP -> res = new Vec2(0, -1);
            case RIGHT -> res = new Vec2(1, 0);
            case BOTTOM -> res = new Vec2(0, 1);
        }

        return res;
    }


    public static Pair<Vec2, Vec2> dirToLine(@NotNull Direction dir) {
        Vec2 res1 = null;
        Vec2 res2 = null;
        switch (dir) {
            case LEFT -> {
                res1 = new Vec2(0, 0);
                res2 = new Vec2(0, -1);
            }
            case TOP -> {
                res1 = new Vec2(0, -1);
                res2 = new Vec2(1, -1);
            }
            case RIGHT -> {
                res1 = new Vec2(1, -1);
                res2 = new Vec2(1, 0);
            }
            case BOTTOM -> {
                res1 = new Vec2(0, 0);
                res2 = new Vec2(1, 0);
            }
        }

        return new Pair<>(res1, res2);
    }

    public static Pair<Vec2, Vec2> dirToLineSized(Direction dir, int size) {
        int quarterSize = size / 4;
        int halfSize = size / 2;

        Pair<Vec2, Vec2> dirRes = dirToLine(dir);

        Vec2 first= dirRes.getFirst();
        Vec2 second = dirRes.getSecond();

        Vec2 resF  = null;
        Vec2 resS = null;

        if(first.getX() != second.getX()) {
            resF = new Vec2(first.x + quarterSize, first.y);
            resS = new Vec2(second.x * halfSize, second.y);
        }

        if(first.getY() != second.getY()) {
            resF = new Vec2(first.x, first.y + quarterSize);
            resS = new Vec2(second.x, second.y * halfSize);
        }

        return new Pair<>(resF, resS);
    }
}
