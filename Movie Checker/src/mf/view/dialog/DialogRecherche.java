package mf.view.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import mf.controller.Controller;
import mf.database.pojo.Nationalite;
import mf.database.pojo.Realisateur;
import mf.model.FicheFilm;
import mf.net.Stream;
import mf.view.component.ComponentAffiche;
import mf.view.modifier.ModifierDialog;

import com.sun.awt.AWTUtilities;

@SuppressWarnings("serial")
public class DialogRecherche extends JDialog implements MouseListener , ActionListener, KeyListener{
	private Controller controller;
	
	private JButton bouton_cancel = new JButton("Annuler");
	private JButton bouton_rechercher = new JButton("Rechercher");
	private JButton bouton_ok = new JButton("OK");
	
	private JPanel panel_bouton = new JPanel();
	private JPanel panel_recherche = new JPanel(new BorderLayout());
	
	private JTextField textfield_titre = new JTextField();
	
	private JLabel label_titre = new JLabel("Titre : ");
	private JLabel label_message = new JLabel("Echec de la connection !");
	
	private JPanel panel_titre = new JPanel();
	private JPanel panel_message = new JPanel(new BorderLayout());
	private JPanel panel_ff = new JPanel();
	
	private ArrayList<FicheFilm> LFF = new ArrayList<FicheFilm>();
	private FicheFilm fiche_film;
	
	private PanelFicheFilm[] liste_panel_ff;
	private JRadioButton[] liste_radio_bouton;
	private ButtonGroup bouton_group = new ButtonGroup();
	
	private Border border= BorderFactory.createRaisedBevelBorder();
	private Border border_empty= BorderFactory.createEmptyBorder();
	
	private JFrame parent;
	
	public DialogRecherche (JFrame parent, FicheFilm fiche_film, ArrayList<FicheFilm> LFF, Controller controller) {
		super(parent,true);
		this.setUndecorated(true);
		AWTUtilities.setWindowOpaque(this,false);
		
		this.setLocationRelativeTo(rootPane);
		this.setTitle("Rechercher des informations");
		panel_bouton.add(bouton_ok);
		panel_bouton.add(bouton_cancel);
		
		this.controller = controller;
		this.parent = parent;
		this.LFF = LFF;
		this.fiche_film = fiche_film;
		
		bouton_cancel.addActionListener(this);
		bouton_ok.addActionListener(this);
		bouton_rechercher.addActionListener(this);
		
		textfield_titre.setText(fiche_film.getTitre_f());
		textfield_titre.setPreferredSize(new Dimension(150,30));
		textfield_titre.addKeyListener(this);
		
		Font police = new Font("SquareFont", Font.BOLD, 35);
		label_message.setFont(police);
		label_message.setHorizontalAlignment(SwingConstants.CENTER);
		
		panel_titre.add(label_titre);
		panel_titre.add(textfield_titre);
		panel_titre.add(bouton_rechercher);
		
		panel_message.add(label_message);
		panel_recherche.add(panel_ff,BorderLayout.CENTER);
		
		this.getContentPane().add(panel_bouton,BorderLayout.SOUTH);
		this.getContentPane().add(panel_titre,BorderLayout.NORTH);
		
		this.setSize(1100,650);
		
		if (DialogRunning.getState() == DialogRunning.CANCELLED) {
			label_message.setText("Recherche abandonnée");
			this.getContentPane().add(panel_message,BorderLayout.CENTER);
			bouton_ok.setEnabled(false);
		}
		else if (DialogRunning.getState() == DialogRunning.FAIL || DialogRunning.getState() == DialogRunning.NC) {
			label_message.setText("Impossible de se connecter au serveur AlloCiné");
			this.getContentPane().add(panel_message,BorderLayout.CENTER);
			bouton_ok.setEnabled(false);
		}
		else if (LFF.size() == 0) {
			label_message.setText("Aucun résultat trouvé");
			this.getContentPane().add(panel_message,BorderLayout.CENTER);
			bouton_ok.setEnabled(false);
		}
		else {
			
			this.getContentPane().add(panel_recherche,BorderLayout.CENTER);

			liste_panel_ff = new PanelFicheFilm[LFF.size()];
			liste_radio_bouton = new JRadioButton[LFF.size()];

			panel_ff.setLayout(new GridLayout(1,LFF.size(),10,0));
			for (int i = 0; i < LFF.size(); i ++) {
				liste_panel_ff[i] = new PanelFicheFilm(LFF.get(i));
				liste_radio_bouton[i] = new JRadioButton();
				liste_radio_bouton[i].setHorizontalAlignment(SwingConstants.CENTER);
				liste_radio_bouton[i].addActionListener(this);
				liste_panel_ff[i].addMouseListener(this);
				bouton_group.add(liste_radio_bouton[i]);
				if (i == 0) {
					liste_radio_bouton[0].setSelected(true);
					liste_panel_ff[0].setBorder(border);
				}
				panel_ff.add(liste_panel_ff[i]);
				liste_panel_ff[i].add(liste_radio_bouton[i],BorderLayout.SOUTH);
			}
			
			if (LFF.size() == 0)
				bouton_ok.setEnabled(false);
		}

		ModifierDialog.createDialogBackPanel(this, parent.getContentPane());
		ModifierDialog.fadeIn(this);
	}
	
	private void cancel() {
		this.removeAll();
		if (DialogRunning.getState() == DialogRunning.SUCCESS && LFF.size() != 0)
			for (PanelFicheFilm lpff : liste_panel_ff)
				lpff.free();
	}
	
	private void printResult() {
		if (DialogRunning.getState() == DialogRunning.CANCELLED) {
			panel_ff.removeAll();

			for (int i = 0; i<liste_panel_ff.length; i++) {
				if (liste_panel_ff[i] != null) {
					liste_panel_ff[i].free();
					liste_panel_ff[i] = null;
				}
				liste_radio_bouton[i] = null;
			}
			
			this.getContentPane().remove(panel_recherche);
			this.getContentPane().add(panel_message,BorderLayout.CENTER);
			this.getContentPane().repaint();
			label_message.setText("Recherche abandonnée");
		}
		else if (DialogRunning.getState() == DialogRunning.FAIL || DialogRunning.getState() == DialogRunning.NC) {
			panel_ff.removeAll();

			for (int i = 0; i<liste_panel_ff.length; i++) {
				if (liste_panel_ff[i] != null) {
					liste_panel_ff[i].free();
					liste_panel_ff[i] = null;
				}
				liste_radio_bouton[i] = null;
			}
			
			this.getContentPane().remove(panel_ff);
			this.getContentPane().add(panel_message,BorderLayout.CENTER);
			
			label_message.setText("Echec de la connection !");
		}
		else {
			panel_ff.removeAll();
			panel_ff.repaint();

			for (int i = 0; i<liste_panel_ff.length; i++) {
				if (liste_panel_ff[i] != null) {
					liste_panel_ff[i].free();
					liste_panel_ff[i] = null;
				}
				liste_radio_bouton[i] = null;
			}
			
			liste_panel_ff = new PanelFicheFilm[LFF.size()];
			liste_radio_bouton = new JRadioButton[LFF.size()];
			
			for (int i = 0; i < LFF.size(); i ++) {
				liste_panel_ff[i] = new PanelFicheFilm(LFF.get(i));
				liste_radio_bouton[i] = new JRadioButton();
				liste_radio_bouton[i].setHorizontalAlignment(SwingConstants.CENTER);
				liste_radio_bouton[i].addActionListener(this);
				liste_panel_ff[i].addMouseListener(this);
				bouton_group.add(liste_radio_bouton[i]);
				if (i == 0) {
					liste_radio_bouton[0].setSelected(true);
					liste_panel_ff[0].setBorder(border);
				}
				panel_ff.add(liste_panel_ff[i]);
				liste_panel_ff[i].add(liste_radio_bouton[i],BorderLayout.SOUTH);
			}
			
			if (LFF.size() != 0)
				bouton_ok.setEnabled(true);
			else 
				bouton_ok.setEnabled(false);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bouton_cancel) {
			cancel();
			ModifierDialog.fadeOut(this);
		}
		else if (e.getSource() == bouton_rechercher) {
			LFF.clear();
			new DialogRunning(parent,textfield_titre.getText());
			if (DialogRunning.getState() == DialogRunning.SUCCESS)
				while (Stream.hasNext()) {
					LFF.add(Stream.getNext());
				}
			printResult();
			
			if (DialogRunning.getState() == DialogRunning.SUCCESS)
				for (int i = 0; i < liste_radio_bouton.length; i++){
					liste_panel_ff[i].setBorder(border_empty);
					if (e.getSource() == liste_radio_bouton[i]) {
						liste_panel_ff[i].setBorder(border);
					}
				}
		}
		else if (e.getSource() == bouton_ok) {
			int num_selected = 0;
			for (int i = 0; i < liste_radio_bouton.length; i++) {
				if (liste_radio_bouton[i].isSelected()) {
					num_selected = i;
					break;
				}
				
			}
			controller.updateF(fiche_film.getTitre_disque(), fiche_film.isSeen(), LFF.get(num_selected));
			ModifierDialog.fadeOut(this);
		}
	}
	
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyChar() == KeyEvent.VK_ESCAPE) {
			cancel();
			ModifierDialog.fadeOut(this);
		}
		else if (arg0.getKeyChar() == KeyEvent.VK_ENTER) {
			if (textfield_titre.isFocusOwner())
				bouton_rechercher.doClick();
		}
	}

	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent event) {}
	
	public void mouseClicked(MouseEvent arg0) {}
	
	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}

	public void mousePressed(MouseEvent arg0) {}

	public void mouseReleased(MouseEvent e) {
		for (int i = 0; i < liste_panel_ff.length; i++){
			liste_panel_ff[i].setBorder(border_empty);
			if (e.getSource() == liste_panel_ff[i]) {
				liste_panel_ff[i].setBorder(border);
				liste_radio_bouton[i].setSelected(true);
			}
		}
	}
	
	private class PanelFicheFilm extends JPanel{
		private JLabel titre = new JLabel("");
		private JLabel date = new JLabel("");
		private JLabel titre_realisateur = new JLabel("");
		private JLabel[] realisateurs;
		private JLabel titre_nation = new JLabel("");
		private JLabel nations = new JLabel("");
		private ComponentAffiche affiche;
		private JPanel panel_realisateurs = new JPanel();
		private JPanel panel_titre = new JPanel(new GridLayout(2,1));
		private JPanel panel_infos = new JPanel();
		
		public PanelFicheFilm(FicheFilm ff) {
			boolean first = true;
			
			try {
				affiche = new ComponentAffiche(ff.getAffiche_url(),200,ComponentAffiche.WEB,parent);
			} catch (IOException e) {
				try {
					affiche = new ComponentAffiche("affiche_default.aff",200,parent);
				} catch (IOException e1) {}
			}
			
			realisateurs = new JLabel[ff.getListe_realisateurs().size()];
			
			panel_titre.setMaximumSize(new Dimension(1050/LFF.size(),50));
			titre_realisateur.setMaximumSize(new Dimension(1050/LFF.size(),50));
			
			
			if (ff.getDate() != -1) {
				date.setText("(" + ff.getDate() + ")");
				panel_titre.add(titre);
				panel_titre.add(date);
			}
			else {
				panel_titre.setLayout(new FlowLayout());
				panel_titre.add(titre);
			}
			
			titre.setText(ff.getTitre_f());
				
			if (ff.getListe_realisateurs().isEmpty()) {
				titre_realisateur.setText("Réalisateur :");
				realisateurs = new JLabel[1];
				realisateurs[0] = new JLabel("Inconnu");
			}
			else if (ff.getListe_realisateurs().size() == 1) 
				titre_realisateur.setText("Réalisateur :");
			else
				titre_realisateur.setText("Realisateurs : ");
			
			for (Realisateur r : ff.getListe_realisateurs()) {
				if (first) {
					realisateurs[ff.getListe_realisateurs().indexOf(r)] = new JLabel(r.toString());
					first = false;
				}
				else {
					realisateurs[ff.getListe_realisateurs().indexOf(r)] = new JLabel(", "+r.toString());
				}
			}
			first = true;
			
			if (ff.getListe_nations().isEmpty()) {
				titre_nation.setText("Nationalité :");
				nations.setText("Inconnue");
			}
			else if (ff.getListe_nations().size() == 1) 
				titre_nation.setText("Nationalité :");
			else
				titre_nation.setText("Nationalités : ");
			
			for (Nationalite n : ff.getListe_nations()) {
				if (first) {
					nations.setText(n.toString());
					first = false;
				}
				else {
					nations.setText(nations.getText() + ", " + n.toString());
				}
			}
			
			titre.setHorizontalAlignment(SwingConstants.CENTER);
			date.setHorizontalAlignment(SwingConstants.CENTER);
			titre_realisateur.setHorizontalAlignment(SwingConstants.CENTER);
			titre_nation.setHorizontalAlignment(SwingConstants.CENTER);
			nations.setHorizontalAlignment(SwingConstants.CENTER);
			
			titre.setFont(new Font("Dialog",Font.BOLD,16));
			date.setFont(new Font("Dialog",Font.BOLD,16));
			titre_realisateur.setFont(new Font("Dialog",Font.BOLD,16));
			for (JLabel realisateur : realisateurs)
				realisateur.setFont(new Font("Dialog",Font.PLAIN,14));
			titre_nation.setFont(new Font("Dialog",Font.BOLD,16));
			nations.setFont(new Font("Dialog",Font.PLAIN,14));

			for (JLabel realisateur : realisateurs)
				panel_realisateurs.add(realisateur);
			
			
			panel_infos.setLayout(new BoxLayout(panel_infos,BoxLayout.PAGE_AXIS));
			panel_infos.add(titre_realisateur);
			panel_infos.add(panel_realisateurs);
			panel_infos.add(titre_nation);
			panel_infos.add(nations);
			
			panel_titre.setAlignmentX(CENTER_ALIGNMENT);
			titre_realisateur.setAlignmentX(CENTER_ALIGNMENT);
			panel_realisateurs.setAlignmentX(CENTER_ALIGNMENT);
			titre_nation.setAlignmentX(CENTER_ALIGNMENT);
			nations.setAlignmentX(CENTER_ALIGNMENT);
			
			this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
			this.add(panel_titre);
			this.add(affiche);
			this.add(panel_infos);
			this.add(Box.createRigidArea(new Dimension(0,100)));
			if (affiche.getHeight() < 250)
				this.add(Box.createRigidArea(new Dimension(0,250- affiche.getHeight())));
		}
		
		protected void free() {
			panel_titre.removeAll();
			panel_realisateurs.removeAll();
			panel_infos.removeAll();
			affiche.free();
			titre = null;
			date = null;
			for (@SuppressWarnings("unused") JLabel r : realisateurs)
				r = null;
			titre_realisateur = null;
			titre_nation = null;
			nations = null;
			System.gc();
		}
	}
}