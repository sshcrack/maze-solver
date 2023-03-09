package me.sshcrack.labyrinth.path;

import me.sshcrack.labyrinth.Main;
import me.sshcrack.labyrinth.math.Matrix;
import me.sshcrack.labyrinth.math.Vec2;
import me.sshcrack.labyrinth.paint.DrawUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MazePoint implements Cloneable{
    private final int x;
    private final int y;

    private Color color;
    private List<Direction> faces;

    private List<Direction> getAvailableSides() {
        Vec2 pos = this.getPosition();
        return Arrays.stream(Direction.values())
                .filter(e -> {
                    Vec2 dVec = Vec2.fromDirection(e);
                    return true;//Matrix.inBounds(pos.add(dVec));
                })
                .collect(Collectors.toList());
    }


    public MazePoint(int x, int y, Color color, List<Direction> faces) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.faces = faces;
    }

    public MazePoint(int x, int y) {
        this.x = x;
        this.y = y;
        this.faces = getAvailableSides();
        this.color = Color.WHITE;
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

    public void setFaces(Direction... faces) {
        this.faces = Arrays.stream(faces).toList();
    }
    public void setFaces(List<Direction> sides) {
        this.faces = sides;
    }

    public boolean hasAllFaces() {
        return this.faces.size() == Direction.values().length;
    }

    public List<Direction> getFaces() {
        return faces;
    }

    public boolean hasFace(Direction dir) {
        return faces.stream().anyMatch(e -> e == dir);
    }

    public void addFace(Direction dir) {
        if(this.faces.stream().noneMatch(e -> e.compareTo(dir) == 0)) {
            ArrayList<Direction> e=  new ArrayList<>(this.faces);
            e.add(dir);

            this.faces = e;
        }
    }

    public void drawDebug(Graphics g, int xOffset, int yOffset) {
        drawDebug(g, xOffset, yOffset, this.color);
    }

    public void drawDebug(Graphics g, int xOffset, int yOffset, Color c) {
        int scale = Main.RES;
        int diameter = 5;
        int padding = 8;

        int centered = (int) (((double) scale / 2) - ((double) diameter / 2));
        int xStart = xOffset + x * scale;
        int yStart = yOffset + y * scale;

        g.setColor(c);
        g.drawOval(xStart + centered, yStart + centered, diameter, diameter);
        g.drawRect(xStart, yStart, scale, scale);

        for (Direction side : faces) {
            DrawUtil.drawDirection(Color.cyan, g, side, scale, padding, xStart, yStart);
        }
    }

    public void draw(MazePoint[][] maze, Graphics g, int xOffset, int yOffset) {
        draw(maze, g, xOffset, yOffset, this.color);
    }

    public void draw(MazePoint[][] maze, Graphics g, int xOffset, int yOffset, Color c) {
        int scale = Main.RES;
        int diameter = 5;

        int xStart = xOffset + x * scale;
        int yStart = yOffset + y * scale;

        int centered = (int) (((double) scale / 2) - ((double) diameter / 2));
        g.setColor(c);
        g.drawOval(xStart + centered, yStart + centered, diameter, diameter);

        List<Direction> toDraw = Arrays.stream(Direction.values()).filter(e -> {
            MazePoint n = Matrix.getNeighbour(maze, this, e);
            if(n == null)
                return true;

            if(n.getFaces().stream().anyMatch(x -> x.opposite().compareTo(e) == 0) && !n.hasAllFaces())
                return false;
            if(this.getFaces().stream().anyMatch(x -> x.compareTo(e) == 0))
                return false;

            return true;
        })
        .collect(Collectors.toList());
        for (Direction side : toDraw) {
            DrawUtil.drawDirection(Color.white, g, side, scale, 0, xStart, yStart);
        }
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public Color getColor() {
        return color;
    }

    public boolean isEqual(MazePoint other) {
        return this.x == other.getX() && this.y == other.getY();
    }

    @Override
    public String toString() {
        return "MazePoint{" +
                "x=" + x +
                ", y=" + y +
                ", faces=" + faces +
                '}';
    }

    @Override
    public MazePoint clone() {
        return new MazePoint(x, y, color, faces);
    }
}
