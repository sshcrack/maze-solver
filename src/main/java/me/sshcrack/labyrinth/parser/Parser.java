package me.sshcrack.labyrinth.parser;

import me.sshcrack.labyrinth.Main;
import me.sshcrack.labyrinth.math.Matrix;
import me.sshcrack.labyrinth.path.Direction;
import me.sshcrack.labyrinth.path.MazePoint;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static String serialize(MazePoint[][] maze) {
        JSONArray out = new JSONArray();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(Main.DIM);

        for (MazePoint[] mazePoints : maze) {
            for (MazePoint curr : mazePoints) {
                JSONObject obj = new JSONObject();
                obj.put("x", curr.getX());
                obj.put("y", curr.getY());
                obj.put("c", curr.getColor().getRGB());

                JSONArray dirs = new JSONArray();
                for (Direction face : curr.getFaces()) {
                    dirs.put(face);
                }
                obj.put("f", dirs);

                out.put(obj);
            }
        }

        return out.toString();
    }

    public static MazePoint[][] deserialize(byte[] data) {
        String str = new String(data, StandardCharsets.UTF_8);
        JSONArray arr = new JSONArray(str);

        int size = (int) Math.sqrt(arr.length());

        MazePoint[][] maze = new MazePoint[size][size];
        for (Object o : arr) {
            JSONObject obj = (JSONObject) o;
            MazePoint e = new MazePoint(obj.getInt("x"), obj.getInt("y"));
            e.setColor(new Color(obj.getInt("c")));

            JSONArray dirs = obj.getJSONArray("f");
            List<Direction> f = new ArrayList<>();
            for (int i = 0; i < dirs.length(); i++) {
                    f.add(dirs.getEnum(Direction.class, i));
            }

            e.setFaces(f);
            maze[e.getX()][e.getY()] = e;
        }

        return maze;
    }
}
