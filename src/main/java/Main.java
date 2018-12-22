/*
 * Copyright (c) 2018.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import gui.AppMainWindow;

public class Main {

    private static String appName = "Image Resizer";

    private static JFrame frame;

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> runGUI());
    }

    private static void runGUI(){

//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        frame = new AppMainWindow(appName);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                closeApp();
            }
        });

        frame.setBounds(100,100,800,600);

        frame.setVisible(true);
    }

    static void closeApp(){
        int selection = JOptionPane.showConfirmDialog(
                null,
                "Zamknąć program?",
                "Potwierdź wyjście",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if(selection == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }

}
