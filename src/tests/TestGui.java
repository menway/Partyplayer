package tests;

import java.io.File;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JFrame;

import player.Player;

import library.Library;
import gui.LibraryFrame;

public class TestGui {
	public static void main(String[] args) {
		Library lib;
		try {
			lib = new Library(new File(System.getProperty("user.dir") + "/music"));
			Player player = new Player();
			Properties settings = new Properties();
			ResourceBundle bundle = ResourceBundle.getBundle("resources.lang");
			LibraryFrame frame = new LibraryFrame(lib, player, settings, bundle);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
