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

// JPanel
// ListSelectionListener
public class WindowElements extends JPanel implements ListSelectionListener {
    private JList<User> list;
    private DefaultListModel<User> listModel;

    private JTextField nameField;
    private JTextField birthDayField;

    private IUserManager userManager;

    public WindowElements(IUserManager userManager) {
        super(new BorderLayout());

        this.userManager = userManager;

        Component listPanel = createListPanel(userManager);
        Component upperPanel = createUpperPanel();

        add(upperPanel, BorderLayout.PAGE_START);
        add(listPanel, BorderLayout.CENTER);
    }

    private Component createListPanel(IUserManager userManager) {
        //Create and populate the list model.
        listModel = new DefaultListModel<>();


        List<User> users = userManager.getAll();

        for (User user : users) {
            listModel.addElement(user);
        }

        //Create the list and put it in a scroll pane.
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        return new JScrollPane(list);
    }

    private Component createUpperPanel() {
        //Create the list-modifying buttons.
        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(new AddButtonListener());

        JButton deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(new DeleteButtonListener());

        JButton searchByNameButton = new JButton("Найти по имени");
        searchByNameButton.addActionListener(new SearchByNameButtonListener());

        JButton searchByBirthDateButton = new JButton("Найти по дате");
        searchByBirthDateButton.addActionListener(new SearchByBirthDateButtonListener());

        JButton showAllButton = new JButton("Показать все");
        showAllButton.addActionListener(new ShowAllButtonListener());

        //Create the text field for entering new names.
        nameField = new JTextField(15);
        nameField.addActionListener(new AddButtonListener());

        if (list.getSelectedIndex() != -1) {
            String name = listModel.getElementAt(list.getSelectedIndex()).getName();
            nameField.setText(name);
        }

        birthDayField = new JFormattedTextField(DateUtil.dateFormat());
        birthDayField.setColumns(10);
        if (list.getSelectedIndex() != -1) {
            String date = DateUtil.toString(listModel.getElementAt(list.getSelectedIndex()).getBirthDay());
            birthDayField.setText(date);
        }

        //Create a control panel, using the default FlowLayout.
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

    class DeleteButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            /*
             * This method can be called only if
             * there's a valid selection,
             * so go ahead and remove whatever's selected.
             */

            int selectedIndex = list.getSelectedIndex();

            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(new Frame(), "Ни один элемент для удаления не выбран");
                return;
            }

            User element = listModel.getElementAt(selectedIndex);

//            nameField.setText(element.getName());
//            birthDayField.setText(DateUtil.toString(element.getBirthDay()));
            userManager.delete(element.getId());


            int indexToDelete = selectedIndex;

            listModel.remove(indexToDelete);

            //Adjust the selection.
            if (selectedIndex == listModel.getSize()) {
                //Removed item in last position.
                selectedIndex--;
            }

            list.setSelectedIndex(selectedIndex);

            if (selectedIndex != -1) {
                nameField.setText(list.getSelectedValue().getName());
                birthDayField.setText(DateUtil.toString(list.getSelectedValue().getBirthDay()));
            }
        }
    }

    public class ShowAllButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            listModel.clear();

            for (User user : userManager.getAll()) {
                listModel.addElement(user);
            }

            list.setSelectedIndex(0);
        }
    }


    public class SearchByNameButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            listModel.clear();

            for (User user : userManager.searchByName(nameField.getText())) {
                listModel.addElement(user);
            }

            list.setSelectedIndex(0);
        }
    }


    public class SearchByBirthDateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            Date moment;
            try {
                moment = DateUtil.toDate(birthDayField.getText());
            } catch (ParseException exc) {
                JOptionPane.showMessageDialog(new Frame(), MessageFormat.format("Ожидаемый формат даты: {0}. Полученный формат даты: {1}", DateUtil.dateFormatExample(), birthDayField.getText()));
                return;
            }

            listModel.clear();

            for (User user : userManager.searchByDate(moment)) {
                listModel.addElement(user);
            }

            list.setSelectedIndex(0);
        }
    }

    /**
     * A listener shared by the text field and add button.
     */
    class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (nameField.getText().trim().isEmpty() || birthDayField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(new Frame(), "Заполни поля");
                return;
            }

            int size = listModel.getSize();


            String name = nameField.getText();

            Date birthDate;
            try {
                birthDate = DateUtil.toDate(birthDayField.getText());
            } catch (ParseException exc) {
                JOptionPane.showMessageDialog(new Frame(), MessageFormat.format("Ожидаемый формат даты: {0}. Полученный формат даты: {1}", DateUtil.dateFormatExample(), birthDayField.getText()));
                return;
            }
            listModel.addElement(userManager.save(name, birthDate));
            list.setSelectedIndex(size);


            //Otherwise insert the new one after the current selection,
            //and select new one.
        }
    }

    //Listener method for list selection changes.
    public void valueChanged(ListSelectionEvent e) {
        if (list.getSelectedIndex() != -1) {
            //Single selection: permit all operations.
            nameField.setText(list.getSelectedValue().getName());
            birthDayField.setText(DateUtil.toString(list.getSelectedValue().getBirthDay()));
        }
    }
}