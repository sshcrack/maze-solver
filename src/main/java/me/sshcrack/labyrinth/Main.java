package me.sshcrack.labyrinth;

import me.sshcrack.labyrinth.generator.MazeGeneratorThreaded;
import me.sshcrack.labyrinth.paint.MazePanel;
import me.sshcrack.labyrinth.parser.Parser;
import me.sshcrack.labyrinth.path.Direction;
import me.sshcrack.labyrinth.path.MazePoint;

import static javax.swing.JOptionPane.*;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Random;

public class Main extends JFrame {
    public static MazeGeneratorThreaded thread;
    public static Main instance;
    public static Random random = new Random();
    public static final double CONNECT_PROBABILITY = .25;
    public static MazePoint start;
    public static MazePoint end;

    public static int DIM = 60;
    public static final int TOTAL_PIXEL = 15 * 60;
    public static int RES = 15;

    public static double waitMultiplier = 1;

    private static MazePanel panel;
    private static boolean generating = false;
    private static JLabel seedLabel;
    public Main() {
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(DIM * RES, DIM * RES);
        setBounds(10,10,DIM * RES,DIM * RES);
        System.out.printf("Setting dim %s\n", DIM * RES);

        setBackground(Color.BLACK);
        setTitle("Maze Thingy");
        //setResizable(false);
        setVisible(true);

        JPanel outer = new JPanel();
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));

        panel = new MazePanel(0, 0);
        outer.add(panel);

        JPanel buttonGroup = new JPanel();

        JButton instant = new JButton("Instant");
        instant.addActionListener(e -> Main.waitMultiplier = 0);

        JButton halfX = new JButton("0.5x");
        halfX.addActionListener(e -> Main.waitMultiplier = 2);

        JButton oneX = new JButton("1x");
        oneX.addActionListener(e -> Main.waitMultiplier = 1);

        JButton twoX = new JButton("2x");
        twoX.addActionListener(e -> Main.waitMultiplier = 0.5);

        buttonGroup.add(instant);
        buttonGroup.add(halfX);
        buttonGroup.add(oneX);
        buttonGroup.add(twoX);

        seedLabel = new JLabel();
        seedLabel.setText("Not generated yet.");


        JPanel topRow = new JPanel();
        topRow.add(getExportRow());
        topRow.add(getSizeSettings());
        topRow.add(seedLabel);

        topRow.setLayout(new BoxLayout(topRow, BoxLayout.X_AXIS));

        outer.add(topRow);
        outer.add(buttonGroup);

        outer.setLayout(new BoxLayout(outer, BoxLayout.PAGE_AXIS));
        add(outer);

        validate();
        pack();

        this.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseDragged(MouseEvent e) {}
            @Override
            public void mouseMoved(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {

                if(Main.thread != null) {
                    if(Main.thread.isAlive())
                        Main.thread.interrupt();
                }

                startGeneratorThread();
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    private JPanel getExportRow() {
        JPanel p = new JPanel();

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Maze Files", "maze", "maze");
        chooser.setFileFilter(filter);

        JButton exportButton = new JButton("Export");
        JButton importButton = new JButton("Import");

        exportButton.addActionListener(e -> {
            if(thread == null || !thread.isDone()) {
                showMessageDialog(null, "Maze has to be generated first", "Error", ERROR_MESSAGE);
                return;
            }

            MazePoint[][] maze =  thread.getMaze();
            if(maze == null) {
                showMessageDialog(null, "Maze has to be generated first", "Error", ERROR_MESSAGE);
                return;
            }

            if(chooser.showSaveDialog(p) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if(!file.exists()) {
                    try {
                        boolean created = file.createNewFile();
                        if(!created) {
                            System.err.println("Not created");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        showMessageDialog(null, "Invalid file", "Error", ERROR_MESSAGE);
                        return;
                    }
                }

                String serialized = Parser.serialize(maze);
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    outputStream.write(serialized.getBytes(StandardCharsets.UTF_8));
                } catch (IOException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                    showMessageDialog(null, "Invalid file", "Error", ERROR_MESSAGE);
                }
            }
        });

        importButton.addActionListener(e -> {
            if(thread != null)
                thread.interrupt();


            if(chooser.showOpenDialog(p) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if(!file.exists()) {
                    showMessageDialog(null, "Invalid file", "Error", ERROR_MESSAGE);
                    return;
                }

                byte[] data;
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    data = inputStream.readAllBytes();
                } catch (IOException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                    showMessageDialog(null, "Invalid file", "Error", ERROR_MESSAGE);
                    return;
                }

                MazePoint[][] maze = Parser.deserialize(data);
                startGeneratorThread(maze);
            }
        });

        p.add(importButton);
        p.add(exportButton);

        return p;
    }

    private JPanel getSizeSettings() {
        JPanel p = new JPanel();

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        // If you want the value to be committed on each keystroke instead of focus lost
        formatter.setCommitsOnValidEdit(true);
        JFormattedTextField field = new JFormattedTextField(formatter);
        field.setPreferredSize(new Dimension(60, 30));
        field.setValue(Main.DIM);


        JButton set = new JButton("Set");
        set.addActionListener(e -> {
            boolean shouldInterrupt = Main.thread != null && Main.thread.isAlive();
            if(shouldInterrupt) {
                Main.thread.interrupt();
            }

            int val = (int) field.getValue();
            Main.DIM = val;
            Main.RES = Math.max(2, TOTAL_PIXEL / val);

            if(shouldInterrupt)
                startGeneratorThread();
        });

        p.add(field);
        p.add(set);

        return p;
    }

    public static void startGeneratorThread(MazePoint[][] presetMaze) {
        if(generating)
            return;
        generating = true;
        long seed = new Random().nextLong();
        MazeGeneratorThreaded mazeGenerator = new MazeGeneratorThreaded(seed);

        if(presetMaze != null) {
            mazeGenerator = new MazeGeneratorThreaded(presetMaze);
        } else {
            Main.seedLabel.setText(String.valueOf(seed));
        }

        Main.thread = mazeGenerator;
        Main.thread.addUpdateListener(Main.panel::repaint);
        Main.thread.start();

        generating = false;
    }

    public static void startGeneratorThread() {
        startGeneratorThread(null);
    }



    public static void main(String[] args) {
        instance = new Main();
    }
}
