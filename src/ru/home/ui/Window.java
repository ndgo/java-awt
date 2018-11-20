package ru.home.ui;

import ru.home.manger.IUserManager;

import javax.swing.*;
import java.awt.*;

// окно программы
public class Window extends JFrame {

    // создание окна программы
    public Window(IUserManager userManager) throws HeadlessException {
        // при закрытии окна завершать программу
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // заполнение окна кнопками и элементами
        JComponent newContentPane = new WindowElements(userManager);

        // сохраняем содежимое окна в
        this.setContentPane(newContentPane);

        //окно должно занимать столько места, сколько требуют элементы окна
        this.pack();
        // окно должно быть видимым
        this.setVisible(true);
        // окно не должно уметь менять размер
        this.setResizable(false);
    }
}
