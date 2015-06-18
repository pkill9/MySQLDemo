package com.company;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 Name: Bruno Calabria
 Course: CNT 4714 Summer 2015
 Assignment title: Project Two – Two-Tier Client-Server Application Development With MySQL and JDBC
 Date: June 18, 2015
 Class: Enterprise Programming
 */


public class Main {

    public static void main(String[] args) {

        JFrame app = new JFrame("GUI");
        GUI g = new GUI();
        app.setContentPane(g.root);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.pack();
        app.setVisible(true);

        app.addWindowListener(new WindowAdapter(){
            // disconnect from database and exit when window has closed
            public void windowClosed( WindowEvent event )
            {
                g.disconnectFromDatabase();
                System.exit( 0 );
            } // end method windowClosed
        } // end WindowAdapter inner class
        );
    }
}
