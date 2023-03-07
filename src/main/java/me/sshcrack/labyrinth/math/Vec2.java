package me.sshcrack.labyrinth.math;

import me.sshcrack.labyrinth.path.Direction;
import org.jgrapht.alg.util.Pair;

public class Vec2 extends Vec2Dir implements Cloneable {
    public Vec2(int x, int y) {
        super(x, y);
    }

    public Vec2 add(Vec2 toAdd) {
        return new Vec2(this.x + toAdd.x, this.y + toAdd.y);
    }


    public Vec2 subtract(Vec2 toAdd) {
        return new Vec2(this.x - toAdd.x, this.y - toAdd.y);
    }

    public Vec2 multiply(Vec2 toAdd) {
        return new Vec2(this.x * toAdd.x, this.y * toAdd.y);
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isEqual(Vec2 other) {
        return this.x == other.getX() && this.y == other.getY();
    }

    @Override
    public Vec2 clone() {
        //TODO idk do it better somehow
        try {
            return (Vec2) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Vec2(x, y);
    }
}
