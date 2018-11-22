package ru.home.ui;

import ru.home.data.User;
import ru.home.manger.IUserManager;
import ru.home.util.DateUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


public class WindowElements // Создать панель с элементами, которые будут менятся в зависимости от изменения списка list
        extends JPanel { // Стандартная панель, на который мы добавляем элементы управления программой

    // Список отвечает за отображение списка пользователей в окне программы
    private JList<User> list;

    // Поле класса ответчает за управление содержимым списка
    private DefaultListModel<User> listModel;

    // Текстовое поле, в которое вводится имя
    private JTextField nameField;
    // Текстовое поле, в которое вводится дата
    private JTextField birthDayField;
    // Менеджер по управлению пользователями
    private IUserManager userManager;

    // Конструктор, который принимает менеджер по работе с пользователями
    public WindowElements(IUserManager userManager) {
        // сделать так чтобы элементы не располагались в одну линию
        super(new BorderLayout());

        this.userManager = userManager;

        // Создаем список и заполняем его
        Component listPanel = createListPanel(userManager);
        // Создаем панель с кнопками и текстовыми полями ввода
        Component upperPanel = createUpperPanel();

        // Добавляем панель с кнопками и текстовыми полями на нашу основную панель
        add(upperPanel, BorderLayout.PAGE_START);
        // Добавляем список на нашу основную панель
        add(listPanel, BorderLayout.CENTER);
    }

    private Component createListPanel(IUserManager userManager) {
        // Создаем структуру для управления содержимом списка в пользовательском интерфейсе
        listModel = new DefaultListModel<>();

        // Получаем список предопределенных пользователей
        List<User> users = userManager.getAll();

        // Сохраняем пользователя в список
        for (User user : users) {
            // Добавляем пользователя в структуру управления списком для отображения на UI
            listModel.addElement(user);
        }

        // Создаем список, передавая в него структуру управления списком
        list = new JList<>(listModel);
        // Можно выделять только одну строчку
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // По умолчанию выбираем первый элемент в списке
        list.setSelectedIndex(0);
        // Добавлен слушатель события изменения списка для того чтобы заполнять текстовые поля, когда
        // мы кликаем левой кнопкой мыши на строчке списка
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // устанавливаем поля, когда мы действительно выбираем элемент списка.
                // Бывают события, когда список меняется, но выбранное значение в списке равно -1, то есть ни один элемент списка не выбран
                if (list.getSelectedIndex() != -1) {
                    nameField.setText(list.getSelectedValue().getName());
                    birthDayField.setText(DateUtil.toString(list.getSelectedValue().getBirthDay()));
                }
            }
        });

        // Список должен быть со скролом тк элементов в нем будет много
        return new JScrollPane(list);
    }

    private Component createUpperPanel() {
        JButton addButton = new JButton("Добавить");
        // Создаем обработчик события нажатия на кнопку добавления пользователя в список
        addButton.addActionListener(new AddButtonListener());

        JButton deleteButton = new JButton("Удалить");
        // Создаем обработчик события нажатия на кнопку удаления пользователя в список
        deleteButton.addActionListener(new DeleteButtonListener());

        JButton searchByNameButton = new JButton("Найти по имени");
        // Создаем обработчик события нажатия на кнопку поиск пользователя по имени
        searchByNameButton.addActionListener(new SearchByNameButtonListener());

        JButton searchByBirthDateButton = new JButton("Найти по дате");
        // Создаем обработчик события нажатия на кнопку поиск пользователя по дате
        searchByBirthDateButton.addActionListener(new SearchByBirthDateButtonListener());

        JButton showAllButton = new JButton("Показать все");
        // Создаем обработчик события нажатия на кнопку отображения всех пользователей
        showAllButton.addActionListener(new ShowAllButtonListener());

        // создаем текстовое поле Имя, дли поля 15 символов
        nameField = new JTextField(15);

        // Если список с пользователями не пустой, устанавливаем дефолтное значение текстового поля имя именем первого пользователя
        if (!listModel.isEmpty()) {
            String name = listModel.getElementAt(0).getName();
            nameField.setText(name);
        }

        // создаем текстовое поле дата. Дата может корректно обрабатываться,
        // только если в текстовое поле будет введена строка в формате ДД.ММ.ГГГГ
        birthDayField = new JFormattedTextField(DateUtil.dateFormat());
        // длина тестовой даты 10 символов
        birthDayField.setColumns(10);
        // Если список с пользователями не пустой, устанавливаем дефолтное значение текстового поля дата датой раждения первого отображенного пользователя в списке
        if (!listModel.isEmpty()) {
            String date = DateUtil.toString(listModel.getElementAt(0).getBirthDay());
            birthDayField.setText(date);
        }

        // Создаем панель-контейнер для хранения кнопок на UI
        JPanel buttonPane = new JPanel();
        buttonPane.add(nameField);
        buttonPane.add(birthDayField);
        buttonPane.add(addButton);
        buttonPane.add(deleteButton);
        buttonPane.add(searchByNameButton);
        buttonPane.add(searchByBirthDateButton);
        buttonPane.add(showAllButton);
        return buttonPane;
    }

    // обработчик нажатия на кнопку удалить
    // вызываем (вызываем метод actionPerformed(), когда нажимаем кнопку удаления пользователя
    class DeleteButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // получить индекс удаляемого пользователя в массиве пользователей, отображенных на данный момент на UI
            int selectedIndex = list.getSelectedIndex();

            // если мы не выбрали ни одного пользователя, но нажали кнопку удаления пользователя, то показывается сообщение
            // "Ни один элемент для удаления не выбран"
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(new Frame(), "Ни один элемент для удаления не выбран");
                return;
            }

            // Получаем удаляемого пользователя
            User element = listModel.getElementAt(selectedIndex);

            // Удаляем пользователя из хранилища
            userManager.delete(element.getId());

            // Сохраняем индекс пользователя в отобраемом списке на UI, который был удален из хранилища
            int indexToDelete = selectedIndex;

            // удаление пользователя в из списка на UI
            listModel.remove(indexToDelete);

            // Если был выбран последний пользователь, то после удаления 1 последнего пользователя надо выбрать в списке предпоследнего пользователя
            if (selectedIndex == listModel.getSize()) {
                selectedIndex--;
            }

            // после удаления выделим элемент списка.
            // если selectedIndex равен -1, то выделения не происходит
            // если selectedIndex больше размера списка, то выделения не происходит
            list.setSelectedIndex(selectedIndex);

            // если выделен элемент списка, устанавливаем имя пользователя и дату рождения пользователя в соответствующих текстовых полях
            if (selectedIndex != -1) {
                nameField.setText(list.getSelectedValue().getName());
                birthDayField.setText(DateUtil.toString(list.getSelectedValue().getBirthDay()));
            }
        }
    }

    // обработчик нажатия на кнопку отображения всего списка пользователей
    // вызываем (вызываем метод actionPerformed(), когда нажимаем кнопку отображения всех пользователей
    public class ShowAllButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // очищаем список элементов на UI
            listModel.clear();

            // Получаем список всех пользователей
            List<User> userList = userManager.getAll();

            // Перебираем каждого пользователя и сохраняем его в cтруктуру, отвечающую за отображение списка на UI
            for (User user : userList) {
                listModel.addElement(user);
            }

            // если selectedIndex больше размера списка, то выделения не происходит
            list.setSelectedIndex(0);
        }
    }


    // Обработчик нажатия на кнопку поиска по имени
    // вызываем (вызываем метод actionPerformed(), когда хотим найти всех пользователей, чье имя совпадает со значением
    // текстового поля имя пользователя
    public class SearchByNameButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            // очищаем список элементов на UI
            listModel.clear();

            // получаем значение текстового поля ИМЯ
            String name = nameField.getText();

            // Получаем из хранилища список пользователей, чье имя совпадает с искомым
            List<User> users = userManager.searchByName(name);

            // Добавляем список пользователей в структуру, отвечающую за отображение списка на UI
            for (User user : users) {
                // Добавляем по 1 пользователю в структуру, отвечающую за отображение списка на UI
                listModel.addElement(user);
            }

            // по умолчанию выделяем первый элмент списка, из тех пользователей что были найдены
            list.setSelectedIndex(0);
        }
    }

    // Обработчик нажатия на кнопку поиска по дате
    public class SearchByBirthDateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            Date date;
            try {
                // Получаем текстовое значения поля - дата рождения
                String dateString = birthDayField.getText();
                //  Преобразуем текстовое значение из формата ДД.ММ.ГГГГ в объект с типом Date
                date = DateUtil.toDate(dateString);
            } catch (ParseException exc) {
                // если не получилось преобразовать поле дата к реальной дате то показывается подсказка:
                // "Ожидаемый формат даты: 01.01.2018. Полученный формат даты 001.01.2018
                JOptionPane.showMessageDialog(new Frame(), MessageFormat.format("Ожидаемый формат даты: {0}. Полученный формат даты: {1}", DateUtil.dateFormatExample(), birthDayField.getText()));
                // Если не смогли распознать дату, то поиск не производится. Список пользователей не очищается
                return;
            }

            listModel.clear();

            for (User user : userManager.searchByDate(date)) {
                listModel.addElement(user);
            }

            list.setSelectedIndex(0);
        }
    }

    // обработчик доабвления пользователя в список
    class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Показывать уведомления с требованием заполнить поля, если текстовое поле имя или дата пустое или содержит пробелы
            if (nameField.getText().trim().isEmpty() || birthDayField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(new Frame(), "Заполни поля");
                return;
            }

            String name = nameField.getText();

            Date birthDate;
            try {
                // преобразуем текстовое поле дата рождение к реальной дате
                birthDate = DateUtil.toDate(birthDayField.getText());
            } catch (ParseException exc) {
                // если не получилось преобразовать поле дата к реальной дате то показывается подсказка:
                // "Ожидаемый формат даты: 01.01.2018. Полученный формат даты 001.01.2018
                JOptionPane.showMessageDialog(new Frame(), MessageFormat.format("Ожидаемый формат даты: {0}. Полученный формат даты: {1}", DateUtil.dateFormatExample(), birthDayField.getText()));
                // Если не смогли распознать дату, то добавление пользователя не производится
                return;
            }

            // Сохранение пользователя в хранилище
            User user = userManager.save(name, birthDate);

            // Добалвение пользователя с модель упределения списком чтобы он начал отображаться на панели
            listModel.addElement(user);
        }
    }
}