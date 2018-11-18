package ru.home.ui;

import ru.home.manger.UserManagerImpl;

public class Starter {

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(() -> new Window(new UserManagerImpl()));
    }
}
