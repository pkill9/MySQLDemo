package com.company;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.*;

/**
 Name: Bruno Calabria
 Course: CNT 4714 Summer 2015
 Assignment title: Project Two – Two-Tier Client-Server Application Development With MySQL and JDBC
 Date: June 18, 2015
 Class: Enterprise Programming
 */

//Create Special Set table to be used with mysql return statements
public class MyResultSetTable extends AbstractTableModel{
    private Connection connection;
    private Statement statement;
    private String query;
    private int numrows;
    private int res;

    private ResultSet results;
    private ResultSetMetaData metaData;

    //Constructs table by running query on a given connection statement and query.
    public MyResultSetTable(Connection c, Statement s,String q) throws SQLException{
        connection = c;
        statement = s;
        query = q;

        //If its a select statement use setQuery if not use setUpdate
        if (query.charAt(0) == 'S' || query.charAt(0) == 's'){
            setQuery(query);
        }
        else{
            setUpdate(query);
        }

    }

    // Gets results from an executeQuery
    private void setQuery(String query) throws SQLException{

        results = statement.executeQuery(query);
        metaData = results.getMetaData();

        results.last();
        numrows = results.getRow();

        fireTableStructureChanged();

    }

    private void setUpdate(String query) throws SQLException {

        res = statement.executeUpdate(query);
        numrows = 0;
        JOptionPane.showMessageDialog(null, "The update has been executed", "Updated", JOptionPane.PLAIN_MESSAGE);

    }

    @Override
    public int getRowCount() {
        return numrows;
    }

    @Override
    public int getColumnCount() {
        try {
            return metaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try{
           results.next();
            results.absolute(rowIndex +1);
            return  results.getObject(columnIndex +1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Class getColumnClass (int columnIndex){
        try {
            String className = metaData.getColumnClassName(columnIndex + 1);
            return Class.forName(className);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Object.class;
    }

    public String getColumnName (int columnIndex){
        try {
            return metaData.getColumnName(columnIndex+1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
