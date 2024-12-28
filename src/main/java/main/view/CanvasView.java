package main.view;

import main.model.Polygon;
import main.observer.MyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CanvasView extends JPanel implements MyListener {
    private Polygon lPoly, rPoly;
    private static CanvasView instance;

    public static CanvasView getInstance() {
        if(instance == null)
            instance = new CanvasView();
        return instance;
    }

    private CanvasView() {
        super();
        setBackground(Color.WHITE);
        lPoly = new Polygon();
        lPoly.addListener(this);
        rPoly = new Polygon();
        rPoly.addListener(this);
        this.setVisible(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(!lPoly.isFinished())
                    lPoly.addPoint(e.getX(), e.getY());
                else if(!rPoly.isFinished())
                    rPoly.addPoint(e.getX(), e.getY());

                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        lPoly.paint(g2);
        rPoly.paint(g2);
    }

    @Override
    public void update(String message) {
        MainView.getInstance().getToolbar().getStart().setEnabled(lPoly.isFinished() && rPoly.isFinished());
        repaint();
    }

    public Polygon getrPoly() {
        return rPoly;
    }

    public Polygon getlPoly() {
        return lPoly;
    }
}
