package ru.home.ui;

import ru.home.manger.IUserManager;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    public Window(IUserManager userManager) throws HeadlessException {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new WindowElements(userManager);
        newContentPane.setOpaque(true); //content panes must be opaque
        this.setContentPane(newContentPane);

        //Don't let the content pane get too small.
        //(Works if the Java look and feel provides
        //the window decorations.)
        newContentPane.setMinimumSize(
                new Dimension(
                        newContentPane.getPreferredSize().width,
                        100));

        //Display the window.
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
    }
}
