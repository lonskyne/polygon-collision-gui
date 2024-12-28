package main.view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private static MainView instance;

    private ToolbarView toolbar;

    public static MainView getInstance() {
        if(instance == null)
            instance = new MainView();
        return instance;
    }

    public MainView() throws HeadlessException {
        this.setTitle("PolygonCollision - Viktor Davidović");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1280, 720);

        toolbar = new ToolbarView();

        add(toolbar, BorderLayout.NORTH);
        add(CanvasView.getInstance());

        this.setVisible(true);
    }

    public ToolbarView getToolbar() {
        return toolbar;
    }
}
