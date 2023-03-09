package me.sshcrack.labyrinth.paint;

import me.sshcrack.labyrinth.Main;
import me.sshcrack.labyrinth.path.MazePoint;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class MazePanel extends JComponent {
    private final int xOffset;
    private final int yOffset;
    private final int PADDING = 5;


    public Dimension getPreferredSize( ) {
        int size = Main.DIM * Main.RES + PADDING;
        int w = size + xOffset;
        int h = size + yOffset;

        System.out.printf("Preferred size is %s %s \n", w, h);
        return new Dimension(w, h);
    }


    public MazePanel(int x, int y) {
        super();

        System.out.println(this.getLocation());
        this.xOffset = x + PADDING;
        this.yOffset = y + PADDING;
    }

    @Override
    public void paintComponent(Graphics g) {
        this.setBackground(Color.BLACK);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Main.DIM * Main.RES, Main.DIM * Main.RES);
        if(Main.thread == null || Main.thread.getMaze() == null) {
            String text = "Generating maze...";
            Font font = g.getFont();

            font = new Font(font.getFontName(), font.getStyle(), 40);
            g.setFont(font);

            FontRenderContext context = new FontRenderContext(font.getTransform(), false, false);
            Rectangle2D bounds = font.getStringBounds(text, context);

            g.setColor(Color.WHITE);
            int w = (int) bounds.getWidth() /2;
            int h = (int) bounds.getHeight() /2;
            int halfScale = Main.DIM * Main.RES / 2;

            g.drawString(text, halfScale - w, halfScale -h);

            if(Main.thread == null )
                Main.startGeneratorThread();
            return;
        }


        MazePoint[][] maze = Main.thread.getMaze();
        for (MazePoint[] mazePoints : maze) {
            for (MazePoint point : mazePoints) {
                point.draw(maze, g, xOffset, yOffset);
            }
        }


        List<MazePoint> solved = Main.thread.getSolved();
        if(solved == null)
            return;

        MazePoint prev = null;
        int s = Main.RES;
        int center = s / 2;
        for (MazePoint point : solved) {
            int startX = point.getX();
            int startY = point.getY();
            int endX = point.getX();
            int endY = point.getY();
            if(prev != null) {
                startX = prev.getX();
                startY = prev.getY();
            }

            prev = point;
            g.setColor(Color.YELLOW);
            g.drawLine(startX * s +xOffset + center, startY *s +yOffset + center, endX *s +xOffset + center, endY *s + yOffset + center);
        }
    }
}
