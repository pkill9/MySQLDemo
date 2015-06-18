package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;



/**
Name: Bruno Calabria
Course: CNT 4714 Summer 2015
Assignment title: Project Two – Two-Tier Client-Server Application Development With MySQL and JDBC
Date: June 18, 2015
Class: Enterprise Programming
*/

public class GUI {

    private JTable Table;
    private JButton clearResultsButton;
    private JButton connectToDatabaseButton;
    private JButton clearCommandButton;
    private JButton executeSQLCommandButton;
    private JComboBox JDBC;
    private JComboBox dbURL;
    private JTextField User;
    private JTextArea SQLcommand;
    public  JPanel root;
    private JPasswordField Password;
    private JLabel ConectionMessage;
    private JScrollPane scrollpane;

    String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    String DATABASE_URL = "jdbc:mysql://localhost:3310/project2";

    private String USERNAME;
    private String PASSWORD;
    private String DB;
    private String JDBC_VAL;
    private String SQL_VAL;
    private Boolean connected = false;

    private Connection dbConnection;
    private Statement statement;

    private MyResultSetTable resultSetTable;

    public GUI() {
        JDBC.addItem(JDBC_DRIVER);
        dbURL.addItem(DATABASE_URL);

        clearCommandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SQLcommand.setText("");
            }
        });

        clearResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Table = new JTable();
                scrollpane.setViewportView(Table);
            }
        });

        connectToDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                USERNAME = User.getText();
                PASSWORD = String.valueOf(Password.getPassword());
                JDBC_VAL = JDBC.getSelectedItem().toString();
                DB = dbURL.getSelectedItem().toString();

                try {
                    Class.forName(JDBC_VAL);
                    dbConnection = DriverManager.getConnection(DB, USERNAME, PASSWORD);
                    ConectionMessage.setText("Connected to " + DB);
                    ConectionMessage.setVisible(true);
                    statement = dbConnection.createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    connected = true;
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
                    connected = false;
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    ConectionMessage.setText("Error with JDBC Value");
                    ConectionMessage.setVisible(true);
                    connected = false;
                    e1.printStackTrace();
                }
            }
        });

        executeSQLCommandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (connected) {
                    SQL_VAL = SQLcommand.getText();
                    try {
                        Table = new JTable();
                        scrollpane.setViewportView(Table);
                        resultSetTable = new MyResultSetTable(dbConnection, statement, SQL_VAL);
                        Table = new JTable(resultSetTable);
                        scrollpane.setViewportView(Table);
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Not Connected to DB", "Database error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    public void disconnectFromDatabase(){
        if ( !connected ){ return; }

        // close Statement and Connection
        try {
            statement.close();
            dbConnection.close();
        } // end try
        catch ( SQLException sqlException ){
            sqlException.printStackTrace();
        }
        finally  // update database connection status
        {
            connected = false;
        } // end finally
    } // end method disconnectFromDatabase

}
