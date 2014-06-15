package mf.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.sun.awt.AWTUtilities;

import mf.controller.Controller;
import mf.database.pojo.Acteur;
import mf.exception.bdd.InvalidName;
import mf.view.modifier.ModifierDialog;
import mf.view.modifier.ModifierPoliceCell;

@SuppressWarnings("serial")
public class PanActeur extends Panel {
	private Controller controller = null;
	
	private JPanel panel_boutons = new JPanel();
	
	private ModelTable model = new ModelTable();
	private JTable table;
	
	private JFrame parent = null;
	
	public PanActeur(String nom, Controller controller, JFrame parent) {
		super(nom);
		this.controller = controller;
		this.parent = parent;
		
		this.setBackground(Color.WHITE);
		
		panel_boutons.add(new JButton(new AddAction()),BorderLayout.WEST);
		panel_boutons.add(new JButton(new RemoveAction()),BorderLayout.CENTER);
		
		this.add(panel_boutons,BorderLayout.NORTH);

		table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		table.getColumnModel().getColumn(0).setCellRenderer(new ModifierPoliceCell());
		table.getColumnModel().getColumn(1).setCellRenderer(new ModifierPoliceCell());
		table.setRowHeight(20);
		DefaultTableCellRenderer custom = new DefaultTableCellRenderer(); 
		custom.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(custom);
		table.getColumnModel().getColumn(1).setCellRenderer(custom);
		
		this.add(new JScrollPane(table),BorderLayout.CENTER);
	}
	
	public void setListeActeurs(ArrayList<Acteur> liste_acteur) {
		this.model.update(liste_acteur);
	}
	
	private class AddAction extends AbstractAction {
		private AddAction() {
			super("Ajouter");
		}
		
		public void actionPerformed(ActionEvent e){
			new DialogAddA();
		}
	}
	
	private class RemoveAction extends AbstractAction {
		private RemoveAction() {
			super("Supprimer");
		}
		
		public void actionPerformed(ActionEvent e) {
			int[] selection = table.getSelectedRows();
			
			for (int i = selection.length - 1; i >= 0; i--)
				controller.tryRemoveActeur(model.getValueAt(table.getRowSorter().convertRowIndexToModel(selection[i]), 0),model.getValueAt(table.getRowSorter().convertRowIndexToModel(selection[i]), 1));
		}
	}
	
	private class ModelTable extends AbstractTableModel {
		private final ArrayList<Acteur> liste_acteur = new ArrayList<Acteur>();
		
		private final String[] entetes = {"Prenom", "Nom"};
		
		public ModelTable() {
			super();
		}
		
		public int getColumnCount() {
			return entetes.length;
		}

		public int getRowCount() {
			return liste_acteur.size();
		}
		
		public String getColumnName(int columnIndex) {
	        return entetes[columnIndex];
	    }
		

		public String getValueAt(int rowIndex, int columnIndex) {
			switch(columnIndex){
            case 0:
                return liste_acteur.get(rowIndex).getPrenom();
            case 1:
                return liste_acteur.get(rowIndex).getNom();
            default:
                return null; //Ne devrait jamais arriver
			}
		}
		
		public void setValueAt(Object edit, int rowIndex, int columnIndex) {
			if (edit != "") {
				Acteur oldR = liste_acteur.get(rowIndex);
				try {
					switch(columnIndex){
		            case 0:
		            	if (controller.tryEditActeur(oldR, new Acteur(oldR.getNom(),(String)edit))) {
		            		oldR.setPrenom((String)edit);
		            	}
		                break;
		            case 1:
		            	if (controller.tryEditActeur(oldR, new Acteur((String)edit,oldR.getPrenom()))) {
		            		oldR.setNom((String)edit);
		            	}
		                break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}
		
		public void update(ArrayList<Acteur> newla) {
			liste_acteur.clear();
			
			for (Acteur a : newla) {
				liste_acteur.add(a);
			}
			fireTableDataChanged();
		}		
	}
	
	private class DialogAddA extends JDialog implements ActionListener{
		private JPanel panel_boutons = new JPanel();
		private JPanel panel_saisie = new JPanel();
		private JPanel top = new JPanel(new BorderLayout());
		
		private JTextField nom_jtf = new JTextField("");
		private JTextField prenom_jtf = new JTextField("");
		
		private JLabel message_label = new JLabel("");
		private JLabel nom_label = new JLabel("Nom : ");
		private JLabel prenom_label = new JLabel("Prénom : ");
		
		private JButton cancel = new JButton("Annuler");
		private JButton add = new JButton("Ajouter");
		
		public DialogAddA () {
			super(parent,true);
			this.setUndecorated(true);
			AWTUtilities.setWindowOpaque(this,false);
			
			this.setSize(500, 110);
			this.setLocationRelativeTo(rootPane);
			this.setTitle("Ajouter un acteur");
			
			
			panel_boutons.add(add);
			panel_boutons.add(cancel);
			add.addActionListener(this);
			cancel.addActionListener(this);
			
			message_label.setHorizontalAlignment(JLabel.CENTER);
			Font police = new Font("SquareFont", Font.BOLD, 15);
			message_label.setFont(police);
			
			nom_jtf.setPreferredSize(new Dimension(150,30));
			prenom_jtf.setPreferredSize(new Dimension(150,30));
			
			panel_saisie.add(prenom_label);
			panel_saisie.add(prenom_jtf);
			panel_saisie.add(nom_label);
			panel_saisie.add(nom_jtf);
			
			top.add(panel_saisie,BorderLayout.CENTER);
			top.add(message_label,BorderLayout.SOUTH);
			
			this.getContentPane().add(top,BorderLayout.CENTER);
			this.getContentPane().add(panel_boutons,BorderLayout.SOUTH);
			top.setBackground(Color.WHITE);
			panel_boutons.setBackground(Color.WHITE);
			panel_saisie.setBackground(Color.WHITE);
			
			ModifierDialog.createDialogBackPanel(this, parent.getContentPane());
			
			ModifierDialog.fadeIn(this);
		}

		public void actionPerformed(ActionEvent e) {
			String nom, prenom;
			
			if (e.getSource() == cancel) {
				ModifierDialog.fadeOut(this);
			}
			else {
				nom = nom_jtf.getText();
				prenom = prenom_jtf.getText();
				nom_jtf.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				prenom_jtf.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				if (nom.isEmpty())
					if (prenom.isEmpty()) {
						message_label.setText("Veuillez entrer le nom et le prénom de l'acteur");
						nom_jtf.setBorder(BorderFactory.createLineBorder(Color.RED));
						prenom_jtf.setBorder(BorderFactory.createLineBorder(Color.RED));
					}
					else {
						message_label.setText("Veuillez entrer le nom de l'acteur");
						nom_jtf.setBorder(BorderFactory.createLineBorder(Color.RED));
					}
				else if (prenom.isEmpty()) {
					message_label.setText("Veuillez entrer le prénom de l'acteur");
					prenom_jtf.setBorder(BorderFactory.createLineBorder(Color.RED));
				} else
					try {
						if (controller.isPresentActeur(new Acteur(nom,prenom)))
							message_label.setText("Cet acteur est déjà présent dans la base.");
						else {
							controller.tryAddActeur(nom,prenom);
							ModifierDialog.fadeOut(this);
						}
					} catch (InvalidName e1) {
						e1.printStackTrace();
					}
			}
		}
	}
}
