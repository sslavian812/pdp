package ru.ifmo.ctddev;

import javax.swing.*;

/**
 * Created by viacheslav on 01.12.2015.
 */
public class ShowPoints extends JFrame {

    public ShowPoints() {
        initUI();
    }

    public final void initUI() {

        DrawPanel dpnl = new DrawPanel();
        add(dpnl);

        setSize(500, 500);
        setTitle("Points");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                ShowPoints ex = new ShowPoints();
                ex.setVisible(true);
            }
        });
    }
}