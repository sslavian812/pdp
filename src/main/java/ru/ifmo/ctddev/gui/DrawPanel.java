package ru.ifmo.ctddev.gui;

import ru.ifmo.ctddev.generate.DatasetGenerator;
import ru.ifmo.ctddev.generate.GausianDatasetGeneratorImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by viacheslav on 01.12.2015.
 */
class DrawPanel extends JPanel {

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.blue);
        g2d.scale(4, 4);

        g.setFont(new Font("TimesRoman", Font.PLAIN, 4));

        DatasetGenerator generator = new GausianDatasetGeneratorImpl();
        java.util.List<Point2D.Double> points = generator.generate(10, new Point2D.Double(150,150),
                new Point2D.Double(0,300), new Point2D.Double(300,0) );

        for (int i = 0; i < points.size(); ++i) {
            if (i == points.size() / 2)
                g2d.setColor(Color.red);
            int x = (int) points.get(i).getX();
            int y = (int) points.get(i).getY();
//            g2d.drawLine(x, y, x, y);
            g2d.fillOval(x, y, 2, 2);
            g2d.drawString("" + i % (points.size() / 2), x, y);
        }

//        g2d.setColor(Color.black);
//        g2d.drawLine(100, 0, 100, 100);
//        g2d.drawLine(0, 100, 100, 100);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }
}