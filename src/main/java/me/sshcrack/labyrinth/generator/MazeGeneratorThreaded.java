package me.sshcrack.labyrinth.generator;

import me.sshcrack.labyrinth.Main;
import me.sshcrack.labyrinth.math.ShortestPath;
import me.sshcrack.labyrinth.path.MazePoint;
import me.sshcrack.labyrinth.path.MazePointD;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MazeGeneratorThreaded extends Thread {
    private MazePoint[][] maze;

    private List<MazePoint> solved = null;
    private final ArrayList<Runnable> onUpdate = new ArrayList<>();
    private final long seed;
    private boolean isDone = false;

    public MazeGeneratorThreaded(long seed) {
        this.seed = seed;
        Main.random = new Random(seed);
    }

    public MazeGeneratorThreaded(MazePoint[][] presetMaze) {
        this.seed = 0;
        Main.random = new Random();
        maze = presetMaze;
    }


    public void run() {
        if(maze == null) {
            maze = MazeGenerator.fillMaze(Main.DIM);
            try {
                MazeGenerator.generateMaze(maze);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        MazePointD path = null;
        try {
            path = ShortestPath.getPath(maze, Main.start, Main.end, true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(path != null) {
            this.solved = path.toPath(maze);
        } else {
            System.err.println("Could not solve maze.");
        }

        isDone = true;
    }

    public boolean isDone() {
        return isDone;
    }

    public MazePoint[][] getMaze() {
        return maze;
    }

    public List<MazePoint> getSolved() {
        return solved;
    }

    public void addUpdateListener(Runnable toRun) {
        this.onUpdate.add(toRun);
    }

    public static void fireUpdate() throws InterruptedException {
        fireUpdate(100);
    }

    public static void fireUpdate(long wait) throws InterruptedException {
        if(Main.thread == null) {
            System.err.println("Could not fire update.");
            return;
        }

        for (Runnable r : Main.thread.onUpdate) {
            r.run();
        }

        if(Main.waitMultiplier == 0)
            return;

        long sleepFor = (long) (wait * Main.waitMultiplier);
        Thread.sleep(sleepFor);
    }
}
