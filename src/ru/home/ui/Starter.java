package ru.home.ui;

import ru.home.manger.UserManagerImpl;

public class Starter {

    public static void main(String[] args) {
        // создаем менеджер пользователей
        UserManagerImpl userManager = new UserManagerImpl();
        // создаем окно программы
        new Window(userManager);
    }
}
