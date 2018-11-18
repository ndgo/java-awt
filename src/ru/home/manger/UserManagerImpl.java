package ru.home.manger;

import ru.home.data.User;
import ru.home.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class UserManagerImpl implements IUserManager {

    private final AtomicInteger idGenerator = new AtomicInteger();
    private final List<User> users = new ArrayList<>();

    public UserManagerImpl() {

        Date current = DateUtil.currentDate();

        save("Whistler", DateUtil.rollDays(current, -1));
//        save("Jackson Hole", DateUtil.rollDays(current, -2));
//        save("Squaw Valley", DateUtil.rollDays(current, -3));
//        save("Telluride", DateUtil.rollDays(current, -4));
//        save("Taos", DateUtil.rollDays(current, -5));
//        save("Snowbird", DateUtil.rollDays(current, -6));
//        save("Chamonix", DateUtil.rollDays(current, -7));
//        save("Banff", DateUtil.rollDays(current, -8));
//        save("Arapahoe Basin", DateUtil.rollDays(current, -9));
//        save("Kirkwood", DateUtil.rollDays(current, -10));
//        save("Sun Valley", DateUtil.rollDays(current, -11));
    }

    @Override
    public User save(String name, Date birthDay) {
        User user = new User(idGenerator.getAndIncrement(), name, birthDay);
        users.add(user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public List<User> searchByName(String name) {
        List<User> result = new ArrayList<>();

        for (User user : users) {
            if (user.getName().equals(name)) {
                result.add(user);
            }
        }

        return result;
    }

    @Override
    public List<User> searchByDate(Date moment) {
        List<User> result = new ArrayList<>();

        for (User user : users) {
            if (Objects.equals(user.getBirthDay(), moment)) {
                result.add(user);
            }
        }

        return result;
    }

    @Override
    public void delete(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                users.remove(user);
                return;
            }
        }
    }
}
