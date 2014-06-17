package mf;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mf.controller.Controller;
import mf.model.Model;

public class Main {

	public static void main(String[] args) {
		
		try {
			Model model = new Model();
			new Controller(model);
		} catch (SQLException SQL_E) { /* en cas d'�chec de connection � la base de donn�e */
			try {
				//On force � utiliser le � look and feel � du syst�me
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (InstantiationException e) {}
			catch (ClassNotFoundException e) {}
			catch (UnsupportedLookAndFeelException e) {}
			catch (IllegalAccessException e) {}	
			
			JOptionPane.showMessageDialog(null, "Impossible d'acc�der � la base de donn�e", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

}
