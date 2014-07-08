package mf;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mf.controller.Controller;
import mf.exception.files.FileSystemCorrupted;
import mf.exception.files.FileSystemUnavailable;
import mf.exception.files.IsNotDirectory;
import mf.exception.files.UnfoundFile;
import mf.files.Fichier;
import mf.model.Model;
import mf.view.dialog.DialogSelectRepertory;

public class Main {

	public static void main(String[] args) {
		
		try {
			//On force à utiliser le « look and feel » du système
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (InstantiationException e) {}
		catch (ClassNotFoundException e) {}
		catch (UnsupportedLookAndFeelException e) {}
		catch (IllegalAccessException e) {}	                
		
		try {
			Model model = new Model();

			/* ouverture du fichier system et vérification de sa présence */
			if (Fichier.OpenFileSystem(Fichier.WRITE)) {
				ArrayList<String> liste_films = Fichier.getFilms();
				for (String film : liste_films) {
					if (!model.isPresentFilm(film)) {
						boolean trouve = false;
						for (String film_exclu : Fichier.getTabFileName()) {
							if (film_exclu.equals(film)) {
								trouve = true;
								break;
							}
						}
						if (!trouve) {
							model.addF(film, Fichier.ExtractNameMovie(film));
						}
					}
				}
			}
			else { /* le fichier system n'est pas présent */
				JOptionPane.showMessageDialog(null, "Ceci est votre première utilisation de MovieFinder\n"
						+ "ou votre répertoire de travail habituel n'a pu être trouvé.\n"
						+ "Vous allez être amenés à indiquer le répertoire où se trouvent vos films.", "MovieFinder : Sélection du répertoire de travail", JOptionPane.INFORMATION_MESSAGE);
			
				String repertory = null;
				new DialogSelectRepertory(repertory);
				while(repertory == null);
				
				
			}
			
			new Controller(model);
			
		} catch (SQLException SQL_E) { /* en cas d'échec de connection à la base de donnée */                                                                                                              
			JOptionPane.showMessageDialog(null, "Impossible d'accéder à la base de donnée", "MovieFinder : Erreur", JOptionPane.ERROR_MESSAGE);
			
		} catch (UnfoundFile FILE_E) {                                                                                                                            
			JOptionPane.showMessageDialog(null, "Impossible d'accéder aux films du répertoire " + Fichier.work_directory, "MovieFinder : Erreur", JOptionPane.ERROR_MESSAGE);
		
		} catch (IsNotDirectory e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileSystemUnavailable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileSystemCorrupted e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
