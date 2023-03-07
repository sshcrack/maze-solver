package me.sshcrack.labyrinth.path;

import me.sshcrack.labyrinth.Main;
import me.sshcrack.labyrinth.math.Matrix;
import me.sshcrack.labyrinth.math.Vec2;
import org.jgrapht.alg.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MazePoint {
    private final int x;
    private final int y;
    private Color color = Color.WHITE;

    private List<Direction> sides = getAvailableSides();

    private List<Direction> getAvailableSides() {
        Vec2 pos = this.getPosition();
        return Arrays.stream(Direction.values())
                .filter(e -> {
                    Vec2 dVec = Vec2.fromDirection(e);
                    return true;//Matrix.inBounds(pos.add(dVec));
                })
                .collect(Collectors.toList());
    }

    public MazePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vec2 getPosition() {
        return new Vec2(this.x, this.y);
    }

    public void setSides(Direction... sides) {
        this.sides = Arrays.stream(sides).toList();
    }


    public void setSides(List<Direction> sides) {
        this.sides = sides;
    }

    public List<Direction> getSides() {
        return sides;
    }

    public boolean hasSide(Direction dir) {
        return sides.stream().anyMatch(e -> e == dir);
    }

    public void draw(Graphics g, int xOffset, int yOffset) {
        draw(g, xOffset, yOffset, this.color);
    }

    public void draw(Graphics g, int xOffset, int yOffset, Color c) {
        int scale = Main.RES;
        int diameter = 5;
        int padding = 8;

        int halfScale = scale / 2;
        int quarterScale = scale / 4;

        int centered = (int) (((double) scale / 2) - ((double) diameter / 2));
        int xStart = xOffset + x * scale;
        int yStart = yOffset + y * scale;

        g.setColor(c);
        g.drawOval(xStart + centered, yStart + centered, diameter, diameter);
        g.drawRect(xStart, yStart, scale, scale);

        for (Direction side : sides) {
            Vec2 dirVec = Vec2.fromDirection(side);
            Vec2 multVec = dirVec.multiply(new Vec2(halfScale, halfScale));
            int xDir = multVec.getX();
            int yDir = multVec.getY();

            Vec2 start = multVec.clone();
            Vec2 end = multVec.clone();

            if (yDir == 0) {
                start = start.add(new Vec2(0, -quarterScale));
                end = end.add(new Vec2(0, quarterScale));
            }

            if (xDir == 0) {
                start = start.add(new Vec2(-quarterScale, 0));
                end = end.add(new Vec2(quarterScale, 0));
            }

            g.setColor(Color.cyan);

            int xLineStart = xStart + halfScale + start.getX() + dirVec.getX() * padding;
            int yLineStart = yStart + halfScale + start.getY() + dirVec.getY() * padding;

            int xLineEnd = xStart + halfScale + end.getX() + dirVec.getX() * padding;
            int yLineEnd = yStart + halfScale + end.getY() + dirVec.getY() * padding;

            g.drawLine(xLineStart, yLineStart, xLineEnd, yLineEnd);
        }
    }

    public void setColor(Color c) {
        this.color = c;
    }


    public boolean isEqual(MazePoint other) {
        return this.x == other.getX() && this.y == other.getY();
    }

    @Override
    public String toString() {
        return "MazePoint{" +
                "x=" + x +
                ", y=" + y +
                ", sides=" + sides +
                '}';
    }
}
