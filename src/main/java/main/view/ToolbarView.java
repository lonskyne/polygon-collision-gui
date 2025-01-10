package main.view;

import main.SolutionDriver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;


public class ToolbarView extends JToolBar {
    private JTextField wait;
    private JButton start;
    private JButton move;
    private JButton imprt;
    private JButton exprt;

    public ToolbarView() {
        wait = new JTextField("500");

        imprt = new JButton("Import");
        exprt = new JButton("Export");

        start = new JButton("Start");
        move = new JButton("Move");

        wait.setVisible(true);
        imprt.setVisible(true);
        exprt.setVisible(true);
        start.setVisible(true);
        move.setVisible(true);

        wait.setEnabled(true);
        imprt.setEnabled(true);
        exprt.setEnabled(false);
        start.setEnabled(false);
        move.setEnabled(false);

        imprt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser jFileChooser = new JFileChooser();

                int res = jFileChooser.showOpenDialog(null);

                if(res == JFileChooser.APPROVE_OPTION) {
                    try {
                        Scanner scanner = new Scanner(jFileChooser.getSelectedFile());

                        while(scanner.hasNextLine()) {
                            String[] s = scanner.nextLine().split(", ");

                            if(!CanvasView.getInstance().getlPoly().isFinished())
                                CanvasView.getInstance().getlPoly().addPoint(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
                            else if(!CanvasView.getInstance().getrPoly().isFinished())
                                CanvasView.getInstance().getrPoly().addPoint(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        exprt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser jFileChooser = new JFileChooser();

                int res = jFileChooser.showOpenDialog(null);

                if(res == JFileChooser.APPROVE_OPTION) {
                    try {
                        Writer writer = new FileWriter(jFileChooser.getSelectedFile());

                        writer.write(CanvasView.getInstance().getlPoly().getPointString());
                        writer.write("\n");
                        writer.write(CanvasView.getInstance().getrPoly().getPointString());
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SolutionDriver.setSpeed(Integer.parseInt(wait.getText()));
                        SolutionDriver.getInstance().solve();
                    }
                });
                t.start();
            }
        });

        move.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SolutionDriver.getInstance().move();
                    }
                });
                t.start();
            }
        });

        add(wait);
        add(imprt);
        add(exprt);
        add(start);
        add(move);
    }

    public JButton getStart() {
        return start;
    }

    public JButton getMove() {
        return move;
    }

    public JButton getImprt() {
        return imprt;
    }

    public JButton getExprt() {
        return exprt;
    }
}
