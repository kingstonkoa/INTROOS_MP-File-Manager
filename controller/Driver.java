/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;

import view.Directory;

/**
 * The driver class.
 */
public class Driver
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
    	Controller c = new Controller();
    	Directory dir = new Directory(c);
		dir.setVisible(true);
    }
    
}
