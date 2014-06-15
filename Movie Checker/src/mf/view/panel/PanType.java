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

import com.sun.awt.AWTUtilities;

import mf.controller.Controller;
import mf.view.modifier.ModifierDialog;
import mf.database.pojo.Type;

@SuppressWarnings("serial")
public class PanType extends Panel {
	private Controller controller = null;
	
	private JPanel panel_boutons = new JPanel();
	
	private ModelTable model = new ModelTable();
	private JTable table;
	
	private JFrame parent = null;

	
	public PanType(String nom, Controller controller, JFrame parent) {
		super(nom);
		this.controller = controller;
		this.parent = parent;
		
		this.setBackground(Color.WHITE);
		
		panel_boutons.add(new JButton(new AddAction()),BorderLayout.WEST);
		panel_boutons.add(new JButton(new RemoveAction()),BorderLayout.CENTER);
		
		this.add(panel_boutons,BorderLayout.NORTH);

		table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		table.setRowHeight(20);

		
		this.add(new JScrollPane(table),BorderLayout.CENTER);

	}
	
	public void setListeTypes(ArrayList<Type> liste_genre) {
		this.model.update(liste_genre);
	}
	
	private class AddAction extends AbstractAction {
		private AddAction() {
			super("Ajouter");
		}
		
		public void actionPerformed(ActionEvent e){
			new DialogAddR();
		}
	}
	
	private class RemoveAction extends AbstractAction {
		private RemoveAction() {
			super("Supprimer");
		}
		
		public void actionPerformed(ActionEvent e) {
			int[] selection = table.getSelectedRows();
			
			for (int i = selection.length - 1; i >= 0; i--)
				controller.tryRemoveType(model.getValueAt(table.getRowSorter().convertRowIndexToModel(selection[i]), 0));
		}
	}
	
	private class ModelTable extends AbstractTableModel {
		private final ArrayList<Type> liste_genre = new ArrayList<Type>();
		
		private final String[] entetes = {"Nom"};
		
		public ModelTable() {
			super();
		}
		
		public int getColumnCount() {
			return entetes.length;
		}

		public int getRowCount() {
			return liste_genre.size();
		}
		
		public String getColumnName(int columnIndex) {
	        return entetes[columnIndex];
	    }
		

		public String getValueAt(int rowIndex, int columnIndex) {
			switch(columnIndex){
            case 0:
                return liste_genre.get(rowIndex).getGenre();
            default:
                return null; //Ne devrait jamais arriver
			}
		}
		
		public void setValueAt(Object edit, int rowIndex, int columnIndex) {
			if (edit != "") {
				Type oldG = liste_genre.get(rowIndex);
				
	            if (controller.tryEditType(oldG, new Type((String)edit))) 
	            	oldG.setGenre((String)edit);
			}
		}
		
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}
		
		public void update(ArrayList<Type> newlg) {
			liste_genre.clear();
			
			for (Type g : newlg) {
				liste_genre.add(g);
			}
			fireTableDataChanged();
		}		
	}
	
	private class DialogAddR extends JDialog implements ActionListener{
		
		private JPanel panel_boutons = new JPanel();
		private JPanel panel_saisie = new JPanel();
		private JPanel top = new JPanel(new BorderLayout());
		
		private JTextField nom_jtf = new JTextField("");
		
		private JLabel message_label = new JLabel("");
		private JLabel nom_label = new JLabel("Genre : ");
		
		private JButton cancel = new JButton("Annuler");
		private JButton add = new JButton("Ajouter");
		
		public DialogAddR () {
			super(parent,true);
			this.setUndecorated(true);
			AWTUtilities.setWindowOpaque(this,false);
			
			this.setSize(500, 110);
			this.setLocationRelativeTo(rootPane);
			this.setTitle("Ajouter un genre");
			
			
			panel_boutons.add(add);
			panel_boutons.add(cancel);
			add.addActionListener(this);
			cancel.addActionListener(this);
			
			message_label.setHorizontalAlignment(JLabel.CENTER);
			Font police = new Font("SquareFont", Font.BOLD, 15);
			message_label.setFont(police);
			
			nom_jtf.setPreferredSize(new Dimension(150,30));

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
			String nom;
			
			if (e.getSource() == cancel) {
				ModifierDialog.fadeOut(this);
			}
			else {
				nom = nom_jtf.getText();
				nom_jtf.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				if (nom.isEmpty()) {
					message_label.setText("Veuillez entrer le nom du nouveau genre");
					nom_jtf.setBorder(BorderFactory.createLineBorder(Color.RED));
				} else {
					if (controller.isPresentType(new mf.database.pojo.Type(nom))) {
						message_label.setText("Ce genre est déjà présent dans la base.");
						nom_jtf.setBorder(BorderFactory.createLineBorder(Color.RED));
					} else {
						controller.tryAddType(nom);
						ModifierDialog.fadeOut(this);
					}
				}	
			}
		}
	}
}
