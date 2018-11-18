package ru.home.manger;

import ru.home.data.User;

import java.util.Date;
import java.util.List;

public interface IUserManager {

    User save(String name, Date birthDay);

    List<User> getAll();

    List<User> searchByName(String name);

    List<User> searchByDate(Date moment);

    void delete(int id);
}
