package me.sshcrack.labyrinth.paint;

import me.sshcrack.labyrinth.math.Vec2;
import me.sshcrack.labyrinth.path.Direction;

import java.awt.*;

public class DrawUtil {
    public static void drawDirection(Color c, Graphics g, Direction side, int scale, int padding, int xStart, int yStart) {
        int halfScale = scale / 2;
        int quarterScale = scale / 4;

        Vec2 dirVec = Vec2.fromDirection(side).multiply(new Vec2(-1, -1));
        Vec2 multVec = Vec2.fromDirection(side).multiply(new Vec2(halfScale, halfScale));
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

        g.setColor(c);

        int xPadding = dirVec.getX() * padding;
        int yPadding = dirVec.getY() * padding;

        int xLineStart = xStart + halfScale + start.getX() + xPadding;
        int yLineStart = yStart + halfScale + start.getY() + yPadding;

        int xLineEnd = xStart + halfScale + end.getX() + xPadding;
        int yLineEnd = yStart + halfScale + end.getY() + yPadding;

        g.drawLine(xLineStart, yLineStart, xLineEnd, yLineEnd);
    }
}
