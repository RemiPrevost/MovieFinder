package mf.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.sun.awt.AWTUtilities;

import mf.controller.Controller;
import mf.database.pojo.Acteur;
import mf.database.pojo.Nationalite;
import mf.database.pojo.Realisateur;
import mf.database.pojo.Type;
import mf.exception.files.CannotDeleteMovie;
import mf.exception.files.CannotPlayMovie;
import mf.exception.files.IsNotFile;
import mf.exception.files.UnfoundFile;
import mf.files.Fichier;
import mf.model.FicheFilm;
import mf.net.Stream;
import mf.view.component.ComponentAffiche;
import mf.view.dialog.DialogRecherche;
import mf.view.dialog.DialogRunning;
import mf.view.modifier.ModifierDialog;

@SuppressWarnings("serial")
public class PanFiche extends JPanel implements ActionListener{
	private JFrame parent;
	
	private JButton bouton_edit = new JButton("Rechercher");
	private JButton bouton_play = new JButton("Lecture");
	private JButton bouton_delete = new JButton("Supprimer");

	private JLabel label_titre_date = new JLabel();
	private JLabel label_realisateur_titre = new JLabel();
	private JLabel label_acteur_titre = new JLabel();
	private JLabel label_genre_titre = new JLabel();
	private JLabel label_nation_titre = new JLabel();
	private JLabel label_realisateur = new JLabel();
	private JLabel label_acteur = new JLabel();
	private JLabel label_genre = new JLabel();
	private JLabel label_nation = new JLabel();

	private ComponentAffiche affiche;
	
	private JPanel panel_bouton = new JPanel();
	private JPanel panel_titre = new JPanel();
	private JPanel panel_affiche = new JPanel(new BorderLayout());
	private JPanel panel_infos = new JPanel(new GridLayout(8, 1, 0, 5));
	private JPanel panel_center = new JPanel();
	
	private Controller controller;

	private FicheFilm fiche_film = new FicheFilm();

	public PanFiche(Controller controller, JFrame parent) {
		super(new BorderLayout());
		this.parent = parent;
		this.controller = controller;
		panel_titre.add(label_titre_date);
		panel_infos.add(label_realisateur_titre);
		panel_infos.add(label_realisateur);
		panel_infos.add(label_acteur_titre);
		panel_infos.add(label_acteur);
		panel_infos.add(label_genre_titre);
		panel_infos.add(label_genre);
		panel_infos.add(label_nation_titre);
		panel_infos.add(label_nation);
		
		label_realisateur_titre.setHorizontalAlignment(SwingConstants.CENTER);
		label_realisateur.setHorizontalAlignment(SwingConstants.CENTER);
		label_acteur_titre.setHorizontalAlignment(SwingConstants.CENTER);
		label_acteur.setHorizontalAlignment(SwingConstants.CENTER);
		label_genre_titre.setHorizontalAlignment(SwingConstants.CENTER);
		label_genre.setHorizontalAlignment(SwingConstants.CENTER);
		label_nation_titre.setHorizontalAlignment(SwingConstants.CENTER);
		label_nation.setHorizontalAlignment(SwingConstants.CENTER);
		
		label_titre_date.setFont(new Font("Dialog",Font.BOLD,16));
		label_realisateur_titre.setFont(new Font("Dialog",Font.BOLD,16));
		label_acteur_titre.setFont(new Font("Dialog",Font.BOLD,16));
		label_genre_titre.setFont(new Font("Dialog",Font.BOLD,16));
		label_nation_titre.setFont(new Font("Dialog",Font.BOLD,16));
		label_realisateur.setFont(new Font("Dialog",Font.PLAIN,14));
		label_acteur.setFont(new Font("Dialog",Font.PLAIN,14));
		label_genre.setFont(new Font("Dialog",Font.PLAIN,14));
		label_nation.setFont(new Font("Dialog",Font.PLAIN,14));
		
		panel_center.setLayout(new BoxLayout(panel_center,BoxLayout.PAGE_AXIS));
		panel_center.add(panel_affiche);
		panel_center.add(panel_infos);
		panel_center.add(Box.createRigidArea(new Dimension(0,0)));
		
		panel_bouton.add(bouton_edit);
		panel_bouton.add(bouton_play);
		panel_bouton.add(bouton_delete);
		
		this.add(panel_center, BorderLayout.CENTER);
		this.add(panel_bouton, BorderLayout.SOUTH);
		this.add(panel_titre, BorderLayout.NORTH);
		
		bouton_play.addActionListener(this);
		bouton_delete.addActionListener(this);
		bouton_edit.addActionListener(this);

	}

	public void updateFicheFilm(FicheFilm fiche_film) {
		this.fiche_film = fiche_film;
		printFicheFilm();
	}

	private void printFicheFilm() {
		String temp = new String("");
		int compteur = 0;

		if(fiche_film.getDate() != -1)
			label_titre_date.setText(fiche_film.getTitre_f() + " (" + fiche_film.getDate() + ")");
		else
			label_titre_date.setText(fiche_film.getTitre_f());
		
		panel_affiche.removeAll();
		if (fiche_film.getAffiche_url().isEmpty()) {
			try {
				affiche = new ComponentAffiche("affiche_default.aff",200,parent);
				panel_affiche.add(affiche);
				panel_center.remove(panel_center.getComponentCount() - 1);
				panel_center.add(Box.createRigidArea(new Dimension(0,330 - affiche.getHeight())));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				affiche = new ComponentAffiche(fiche_film.getAffiche_url(),200,parent);
				panel_affiche.add(affiche);
				if (affiche.getHeight() < 350) {
					panel_center.remove(panel_center.getComponentCount() - 1);
					panel_center.add(Box.createRigidArea(new Dimension(0,330 - affiche.getHeight())));
				}
			} catch (IOException e) {
				try {
					affiche = new ComponentAffiche("affiche_default.aff",200,parent);
					panel_affiche.add(affiche);
					panel_center.remove(panel_center.getComponentCount() - 1);
					panel_center.add(Box.createRigidArea(new Dimension(0,330 - affiche.getHeight())));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		panel_affiche.add(affiche);
		
		for (Realisateur r : fiche_film.getListe_realisateurs()) {
			if (compteur != 0)
				temp += ",  " + r.toString();
			else
				temp += r.toString();
			compteur++;
		}
		if (compteur == 0)
			label_realisateur.setText("Inconnu");
		else
			label_realisateur.setText(temp);
		
		if (compteur > 1)
			label_realisateur_titre.setText("Réalisateurs :");
		else
			label_realisateur_titre.setText("Réalisateur :");

		

		temp = "";
		compteur = 0;

		for (Acteur a : fiche_film.getListe_acteurs()) {
			if (compteur != 0)
				temp += ",  " + a.toString();
			else
				temp += a.toString();
			compteur++;
		}
		if (compteur == 0)
			label_acteur.setText("Inconnu");
		else
			label_acteur.setText(temp);
		
		if (compteur > 1)
			label_acteur_titre.setText("Acteurs : ");
		else
			label_acteur_titre.setText("Acteur : ");

		temp = "";
		compteur = 0;

		for (Type g : fiche_film.getListe_genres()) {
			if (compteur != 0)
				temp += ", " + g.toString();
			else
				temp += g.toString();
			compteur++;
		}
		if (compteur == 0)
			label_genre.setText("Inconnu");
		else
			label_genre.setText(temp);
		
		if (compteur > 1)
			label_genre_titre.setText("Genres : ");
		else
			label_genre_titre.setText("Genre : ");

		temp = "";
		compteur = 0;

		for (Nationalite n : fiche_film.getListe_nations()) {
			if (compteur != 0)
				temp += ", " + n.toString();
			else
				temp += n.toString();
			compteur++;
		}
		if (compteur == 0)
			label_nation.setText("Inconnue");
		else
			label_nation.setText(temp);
		
		if (compteur > 1)
			label_nation_titre.setText("Nationalités : ");
		else
			label_nation_titre.setText("Nationalité : ");
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bouton_play) {
			try {
				Fichier.playMovie(fiche_film.getTitre_disque());
			} catch (CannotPlayMovie e1) {
				new DialogFichierIntrouvable("Fichier " + fiche_film.getTitre_disque() + " introuvable !");
			}
		}
		else if (e.getSource() == bouton_delete) {
			new DialogDelete();
		}
		else if(e.getSource() == bouton_edit) {
			ArrayList<FicheFilm> LFF = new ArrayList<FicheFilm>();
			new DialogRunning(parent,fiche_film.getTitre_f());
			if (DialogRunning.getState() == DialogRunning.SUCCESS)
				while (Stream.hasNext()) {
					LFF.add(Stream.getNext());
				}
			new DialogRecherche(parent,fiche_film,LFF,controller);
		}
	}
	
	private class DialogFichierIntrouvable extends JDialog implements ActionListener {
		private JPanel panel_boutons = new JPanel();
		private JPanel panel_text = new JPanel();
		
		private JLabel label_message = new JLabel();
		
		private JButton ok = new JButton("Ok");
		
		public DialogFichierIntrouvable(String message) {
			super(parent,true);
			this.setUndecorated(true);
			AWTUtilities.setWindowOpaque(this,false);
			
			this.setSize(450,90);
			this.setLocationRelativeTo(rootPane);
			this.setTitle("Lecture impossible");
			
			label_message.setText(message);
			
			Font police = new Font("SquareFont", Font.PLAIN, 16);
			label_message.setFont(police);
			
			panel_text.add(label_message);

			panel_boutons.add(ok);
			
			ok.addActionListener(this);
			
			this.getContentPane().add(panel_text, BorderLayout.CENTER);
			panel_text.setBackground(Color.WHITE);
			this.getContentPane().add(panel_boutons,BorderLayout.SOUTH);
			panel_boutons.setBackground(Color.WHITE);
			
			ModifierDialog.createDialogBackPanel(this, parent.getContentPane());
			ModifierDialog.fadeIn(this);
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == ok) {
				ModifierDialog.fadeOut(this);
			}
		}
	}
	
	private class DialogDelete extends JDialog implements ActionListener {
		private JPanel panel_boutons = new JPanel();
		private JPanel panel_text = new JPanel();
		
		private JLabel label_message = new JLabel("Voulez-vous également supprimer le film de votre disque dur ?");
		
		private JButton cancel = new JButton("Annuler");
		private JButton yes = new JButton("Oui");
		private JButton no = new JButton("Non");
		
		public DialogDelete() {
			super(parent,true);
			this.setUndecorated(true);
			AWTUtilities.setWindowOpaque(this,false);
			
			this.setSize(450,90);
			this.setLocationRelativeTo(rootPane);
			this.setTitle("Supprimer " + fiche_film.getTitre_f());
			
			Font police = new Font("SquareFont", Font.PLAIN, 16);
			label_message.setFont(police);
			
			panel_text.add(label_message);

			panel_boutons.add(yes);
			panel_boutons.add(no);
			panel_boutons.add(cancel);
			
			cancel.addActionListener(this);
			no.addActionListener(this);
			yes.addActionListener(this);
			
			this.getContentPane().add(panel_text, BorderLayout.CENTER);
			panel_text.setBackground(Color.WHITE);
			this.getContentPane().add(panel_boutons,BorderLayout.SOUTH);
			panel_boutons.setBackground(Color.WHITE);
			
			ModifierDialog.createDialogBackPanel(this, parent.getContentPane());
			ModifierDialog.fadeIn(this);
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == cancel) {
				ModifierDialog.fadeOut(this);
			}
			else if (e.getSource() == no) {
				controller.deleteF(fiche_film.getTitre_disque());
				ModifierDialog.fadeOut(this);
			}
			else if (e.getSource() == yes) {
				try {
					if(Fichier.deleteMovie(fiche_film.getTitre_disque())) {
						controller.deleteF(fiche_film.getTitre_disque());
						ModifierDialog.fadeOut(this);
					}
				} catch (CannotDeleteMovie | UnfoundFile | IsNotFile e1) {
					
				} finally {
					label_message.setText("Impossible de supprimer le fichier. Réessayer ?");
					panel_boutons.remove(no);
				}
			}
		}
	}
}
