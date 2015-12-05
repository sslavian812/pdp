package ru.ifmo.ctddev.gui;

import javax.swing.*;

/**
 * Created by viacheslav on 01.12.2015.
 */
public class ShowPointsFrame extends JFrame {

    public ShowPointsFrame() {
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
                ShowPointsFrame ex = new ShowPointsFrame();
                ex.setVisible(true);
            }
        });
    }
}