package ru.ifmo.ctddev;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Created by viacheslav on 01.12.2015.
 */
class DrawPanel extends JPanel {

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.blue);
        g2d.scale(4,4);

        DatasetGenerator generator = new DatasetGeneratorImpl();
        java.util.List<Point2D> points = generator.generate(100, new Point2D.Double(50,50));

        for(int i=0; i< points.size(); ++i) {
            if(i == points.size()/2)
                g2d.setColor(Color.green);
            int x = (int)points.get(i).getX();
            int y = (int)points.get(i).getY();
//            g2d.drawLine(x, y, x, y);
            g2d.fillOval(x,y,2,2);
        }
//        for (int i = 0; i <= 1000; i++) {
//
//            Dimension size = getSize();
//            Insets insets = getInsets();
//
//            int w = size.width - insets.left - insets.right;
//            int h = size.height - insets.top - insets.bottom;
//
//            Random r = new Random();
//            int x = Math.abs(r.nextInt()) % w;
//            int y = Math.abs(r.nextInt()) % h;
//            g2d.drawLine(x, y, x, y);
//
//        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }
}