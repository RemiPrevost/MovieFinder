package mf.view.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mf.controller.Controller;
import mf.database.pojo.Acteur;
import mf.database.pojo.Nationalite;
import mf.database.pojo.Realisateur;
import mf.database.pojo.Type;
import mf.model.IntervalleDate;

@SuppressWarnings("serial")
public class PanFiltre extends JPanel {
	private Controller controller = null;
	
	private JSlider slide_note = new JSlider();
	private Hashtable<Integer,JLabel> labelTable = new Hashtable<Integer,JLabel>();
	private String[] field_seen = {"","Oui","Non"};
	private JComboBox<String> combo_seen = new JComboBox<String>(field_seen);
	private JComboBox<IntervalleDate> combo_date = new JComboBox<IntervalleDate>();
	private JTextField textfield_titre = new JTextField();
	private JComboBox<Acteur> combo_acteur = new  JComboBox<Acteur>();
	private JComboBox<Realisateur> combo_realisateur = new  JComboBox<Realisateur>();
	private JComboBox<Type> combo_genre = new  JComboBox<Type>();
	private JComboBox<Nationalite> combo_nation = new  JComboBox<Nationalite>();
	
	private JPanel filtre_top = new JPanel();
	private JPanel filtre_bottom = new JPanel();
	private JPanel filtre_acteur = new JPanel(new GridLayout(2,1));
	private JPanel filtre_titre = new JPanel(new GridLayout(2,1));
	private JPanel filtre_realisateur = new JPanel(new GridLayout(2,1));
	private JPanel filtre_genre = new JPanel(new GridLayout(2,1));
	private JPanel filtre_nation = new JPanel(new GridLayout(2,1));
	
	private JLabel label_note = new JLabel("Note minimale : ");
	private JLabel label_seen =  new JLabel("Visionné : ");
	private JLabel label_date = new JLabel("Date : ");
	private JLabel label_titre = new JLabel("Titre : ");
	private JLabel label_acteur = new JLabel("Acteur : ");
	private JLabel label_realisateur = new JLabel("Realisateur : ");
	private JLabel label_genre = new JLabel("Genre : ");
	private JLabel label_nation = new JLabel("Nationalité : ");
	
	private ArrayList<Acteur> liste_acteur = new ArrayList<Acteur>();
	private ArrayList<Realisateur> liste_realisateur = new ArrayList<Realisateur>();
	private ArrayList<Type> liste_genre = new ArrayList<Type>();
	private ArrayList<Nationalite> liste_nation = new ArrayList<Nationalite>();
	private ArrayList<IntervalleDate> liste_date = new ArrayList<IntervalleDate>();
	
	public PanFiltre(final Controller controller) {
		super(new GridLayout(2,1));
		this.controller = controller;
		
		liste_acteur = controller.getListeActeurFiltre(new ArrayList<Acteur>());
		for (Acteur a : liste_acteur) {
			combo_acteur.addItem(a);
		}
		
		liste_realisateur = controller.getListeRealisateurFiltre(new ArrayList<Realisateur>());
		for (Realisateur r : liste_realisateur) {
			combo_realisateur.addItem(r);
		}
		
		liste_genre = controller.getListeTypeFiltre(new ArrayList<Type>());
		for (Type g : liste_genre) {
			combo_genre.addItem(g);
		}
		
		liste_nation = controller.getListeNationaliteFiltre(new ArrayList<Nationalite>());
		for (Nationalite n : liste_nation) {
			combo_nation.addItem(n);
		}
		
		IntervalleDate intervalle_date = controller.getDateExtrem();
		liste_date = IntervalleDate.createListIntervalleDate(intervalle_date.getDate1(), intervalle_date.getDate2());
		for (IntervalleDate id : liste_date) {
			combo_date.addItem(id);
		}
		
		JLabel label0 = new JLabel("0");
		JLabel label1 = new JLabel("1");
		JLabel label2 = new JLabel("2");
		JLabel label3 = new JLabel("3");
		JLabel label4 = new JLabel("4");
		JLabel label5 = new JLabel("5");
		
		label1.setForeground(new Color(255,170,11));
		label0.setForeground(new Color(255,11,11));
		label2.setForeground(new Color(255,224,11));
		label3.setForeground(new Color(181,206,2));
		label4.setForeground(new Color(0,227,17));
		label5.setForeground(new Color(0,227,17));
		
		label0.setFont(new Font("Dialog",Font.BOLD,13));
		label1.setFont(new Font("Dialog",Font.BOLD,13));
		label2.setFont(new Font("Dialog",Font.BOLD,13));
		label3.setFont(new Font("Dialog",Font.BOLD,13));
		label4.setFont(new Font("Dialog",Font.BOLD,13));
		label5.setFont(new Font("Dialog",Font.BOLD,13));
		
		labelTable.put(new Integer (-1), new JLabel("-"));
		labelTable.put( new Integer( 0 ), label0);
		labelTable.put( new Integer( 1 ), label1);
		labelTable.put( new Integer( 2 ), label2);
		labelTable.put( new Integer( 3 ), label3);
		labelTable.put( new Integer( 4 ), label4);
		labelTable.put( new Integer( 5 ), label5);
		
		slide_note.setLabelTable( labelTable );
		
		slide_note.setMinimum(-1);
		slide_note.setMaximum(5);
		slide_note.setValue(-1);
		slide_note.setMajorTickSpacing(1);
		slide_note.setPaintTicks(true);
		slide_note.setPaintLabels(true);
		slide_note.addChangeListener(new NoteChanged()); 
		
		textfield_titre.setPreferredSize(new Dimension(200,30));
		
		combo_seen.addActionListener(new ComboSeenAction());
		textfield_titre.addKeyListener(new TitreKey());
		combo_acteur.addItemListener(new ComboActeurChanged());
		combo_realisateur.addItemListener(new ComboRealisateurChanged());
		combo_genre.addItemListener(new ComboGenreChanged());
		combo_nation.addItemListener(new ComboNationChanged());
		combo_date.addActionListener(new ComboDateAction());
		
		label_acteur.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		label_titre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		label_realisateur.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		label_genre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		label_nation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		
		this.add(filtre_top);
		this.add(filtre_bottom);
		filtre_top.add(filtre_titre);
		filtre_top.add(filtre_realisateur);
		filtre_top.add(filtre_acteur);
		filtre_top.add(filtre_genre);
		filtre_top.add(filtre_nation);
		filtre_bottom.add(label_seen);
		filtre_bottom.add(combo_seen);
		filtre_bottom.add(label_note);
		filtre_bottom.add(slide_note);
		filtre_bottom.add(label_date);
		filtre_bottom.add(combo_date);
		filtre_titre.add(label_titre);
		filtre_titre.add(textfield_titre);
		filtre_acteur.add(label_acteur);
		filtre_acteur.add(combo_acteur);
		filtre_realisateur.add(label_realisateur);
		filtre_realisateur.add(combo_realisateur);
		filtre_genre.add(label_genre);
		filtre_genre.add(combo_genre);
		filtre_nation.add(label_nation);
		filtre_nation.add(combo_nation);
		this.setBorder(BorderFactory.createTitledBorder("Filtres disponibles : "));
	}
	
	public void updateFiltreActeur() {
		Acteur a_selected = (Acteur)combo_acteur.getSelectedItem();
		
		combo_acteur.removeAllItems();
		liste_acteur.clear();
		liste_acteur = controller.getListeActeurFiltre(new ArrayList<Acteur>());
		for (Acteur a : liste_acteur) {
			combo_acteur.addItem(a);
			if (a.getNom().equals(a_selected.getNom()) && a.getPrenom().equals(a_selected.getPrenom()))
				combo_acteur.setSelectedItem(a);
		}
	}
	
	public void updateFiltreRealisateur() {
		Realisateur r_selected = (Realisateur)combo_realisateur.getSelectedItem();
		
		combo_realisateur.removeAllItems();
		liste_realisateur.clear();
		liste_realisateur = controller.getListeRealisateurFiltre(new ArrayList<Realisateur>());
		for (Realisateur r : liste_realisateur) {
			combo_realisateur.addItem(r);
			if (r.getNom().equals(r_selected.getNom()) && r.getPrenom().equals(r_selected.getPrenom()))
				combo_realisateur.setSelectedItem(r);
		}
	}
	
	public void updateFiltreType() {
		Type g_selected = (Type)combo_genre.getSelectedItem();
		
		combo_genre.removeAllItems();
		liste_genre.clear();
		liste_genre = controller.getListeTypeFiltre(new ArrayList<Type>());
		for (Type g : liste_genre) {
			combo_genre.addItem(g);
			if (g.getGenre().equals(g_selected.getGenre()))
				combo_genre.setSelectedItem(g);
		}
	}
	
	public void updateFiltreNation() {
		Nationalite n_selected = (Nationalite)combo_nation.getSelectedItem();
		
		combo_nation.removeAllItems();
		liste_nation.clear();
		liste_nation = controller.getListeNationaliteFiltre(new ArrayList<Nationalite>());
		for (Nationalite n : liste_nation) {
			combo_nation.addItem(n);
			if (n.getNation().equals(n_selected.getNation()))
				combo_nation.setSelectedItem(n);
		}
	}
	
	private class NoteChanged implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			controller.editFiltreCurseurNote(((JSlider)event.getSource()).getValue());
		}
	}
	
	class ComboSeenAction implements ActionListener{
	    public void actionPerformed(ActionEvent e) {
	    	String selected  = (String)combo_seen.getSelectedItem();
	    	
	    	switch (selected) {
	    	case "" :
	    		controller.editFiltreSeen(-1);
	    		break;
	    	case "Oui" :
	    		controller.editFiltreSeen(1);
	    		break;
	    	case "Non" :
	    		controller.editFiltreSeen(0);
	    		break;
	    	}
	    }               
	}
	
	class ComboDateAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			IntervalleDate selected = (IntervalleDate)combo_date.getSelectedItem();
			
			controller.editFiltreIntervalleDate(selected);
		}
	}
	
	class TitreKey implements KeyListener {

		public void keyPressed(KeyEvent arg0) {
		}
                                                                                                                                         
		public void keyReleased(KeyEvent arg0) {
		}

		public void keyTyped(KeyEvent event) {
			char key_pressed = event.getKeyChar();
			
			if (KeyEvent.getExtendedKeyCodeForChar(key_pressed) != 8)
				controller.editFiltreTitre(((JTextField) event.getSource()).getText()+event.getKeyChar());
			else
				controller.editFiltreTitre(((JTextField) event.getSource()).getText());
		}
	}
	
	class ComboActeurChanged implements ItemListener {
		
		public void itemStateChanged(ItemEvent event) {
		
			if (event.getStateChange() == ItemEvent.SELECTED) {
				controller.setActeurSelected((Acteur)event.getItem());
			}
		}
	}
	
	class ComboRealisateurChanged implements ItemListener {
		
		public void itemStateChanged(ItemEvent event) {
		
			if (event.getStateChange() == ItemEvent.SELECTED) {
				controller.setRealisateurSelected((Realisateur)event.getItem());
			}
		}
	}
	
	class ComboGenreChanged implements ItemListener {
		
		public void itemStateChanged(ItemEvent event) {
		
			if (event.getStateChange() == ItemEvent.SELECTED) {
				controller.setTypeSelected((Type)event.getItem());
			}
		}
	}
	
	class ComboNationChanged implements ItemListener {
		
		public void itemStateChanged(ItemEvent event) {
		
			if (event.getStateChange() == ItemEvent.SELECTED) {
				controller.setNationaliteSelected((Nationalite)event.getItem());
			}
		}
	}
}
