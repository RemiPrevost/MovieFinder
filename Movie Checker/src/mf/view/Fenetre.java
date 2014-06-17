package mf.view;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mf.controller.Controller;
import mf.model.events.*;
import mf.view.panel.*;

public class Fenetre extends View{
	public static final int WINDOW_WIDTH = 1150;
	public static final int WINDOW_HEIGHT = 850;
	
	private JFrame frame = null;
	private JTabbedPane onglets;
	private PanFilm panel_film = null;
	private PanActeur panel_acteur = null;
	private PanRealisateur panel_realisateur = null;
	private PanType panel_genre = null;
	private PanNationalite panel_nationalite = null;
	private JSplitPane split;

	
	public Fenetre(Controller controller) {
		super(controller);
		
		frame = new JFrame();
		frame.setTitle("MovieFinder");
		frame.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*{// Chargement du modifier avant le début du programme pour qu'il ne le ralentisse pas pendant l'exécution.
			frame.setLocation(-6000, -6000);
			frame.setVisible(true);
			ModifierDialog.createDialogBackPanel(new JDialog(frame,true),frame.getContentPane());
		}*/
		
		frame.setLocationRelativeTo(null);
		
		panel_realisateur = new PanRealisateur("Realisateurs", controller,frame);
		panel_acteur = new PanActeur("Acteurs", controller,frame);
		panel_genre = new PanType("Genres", controller,frame);
		panel_nationalite = new PanNationalite("Nationalités", controller,frame);
		panel_film = new PanFilm("Films",controller,frame);
		
		Panel[] liste_panels = {panel_realisateur,panel_acteur,panel_genre,panel_nationalite};
		
		onglets = new JTabbedPane();
		
		for (Panel panel : liste_panels) {
			onglets.add(panel.getNomPan(),panel);
		}
	
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panel_film,onglets);
		split.setDividerLocation(frame.getWidth()* 3/4);
		split.setOneTouchExpandable(true);
		split.setDividerSize(10); 
		
		frame.getContentPane().add(split);
		
		try {
			//On force à utiliser le « look and feel » du système
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//Ici on force tous les composants de notre fenêtre (this) à se redessiner avec le « look and feel » du système
			SwingUtilities.updateComponentTreeUI(frame);
		}
		catch (InstantiationException e) {}
		catch (ClassNotFoundException e) {}
		catch (UnsupportedLookAndFeelException e) {}
		catch (IllegalAccessException e) {}
		
		frame.setVisible(true);
	}

	public void listeRealisateurChanged(ListeRealisateurEvent event) {
		panel_realisateur.setListeRealisateurs(event.getNewListeRealisateur());
		panel_film.updateFiltreRealisateur();
	}
	
	public void listeActeurChanged(ListeActeurEvent event) {
		panel_acteur.setListeActeurs(event.getNewListeActeur());
		panel_film.updateFiltreActeur();
	}
	
	public void listeTypeChanged(ListeTypeEvent event) {
		panel_genre.setListeTypes(event.getNewListeType());
		panel_film.updateFiltreType();
	}

	public void listeNationaliteChanged(ListeNationaliteEvent event) {
		panel_nationalite.setListeNationalites(event.getNewListeNationalite());
		panel_film.updateFiltreNation();
	}

	public void listeFicheFilmChanged(ListeFicheFilmEvent event) {
		panel_film.setListeFichesFilms(event.getNewListeFicheFilm());
	}


}
