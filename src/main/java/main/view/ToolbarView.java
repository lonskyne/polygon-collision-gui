package main.view;

import main.SolutionDriver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ToolbarView extends JToolBar {
    private JTextField wait;
    private JButton start;

    public ToolbarView() {
        wait = new JTextField("500");
        start = new JButton("Start");

        wait.setVisible(true);
        start.setVisible(true);

        wait.setEnabled(true);
        start.setEnabled(false);

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

        add(wait);
        add(start);
    }

    public JButton getStart() {
        return start;
    }
}
