package mf.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import mf.controller.Controller;
import mf.model.FicheFilm;
import mf.model.FicheFilmRed;
import mf.view.modifier.ModifierPoliceCell;

@SuppressWarnings("serial")
public class PanFilm extends Panel{
	private Controller controller = null;
	
	private JPanel panel_bottom = new JPanel(new BorderLayout());
	private JPanel panel_table = new JPanel(new BorderLayout());
	private PanFiltre panel_filtre = null;
	private PanFiche panel_fiche = null;
	
	private FicheFilm fiche_film = new FicheFilm();
	
	private ModelTable model = new ModelTable();
	private JTable table;
	
	public PanFilm(String nom, Controller controller, JFrame parent) {
		super(nom);
		this.controller = controller;
		this.panel_filtre = new PanFiltre(controller);
		this.panel_fiche = new PanFiche(controller,parent);
		
		this.setBackground(Color.WHITE);

		table = new JTable(model);
		DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer(); 
		centerRender.setHorizontalAlignment(JLabel.CENTER); 
		table.getColumnModel().getColumn(0).setCellRenderer(new ModifierPoliceCell());
		table.getColumnModel().getColumn(1).setCellRenderer(new ModifierPoliceCell());
		table.getColumnModel().getColumn(2).setCellRenderer(new ModifierPoliceCell());
		table.getColumnModel().getColumn(0).setCellRenderer(centerRender);
		table.getColumnModel().getColumn(2).setCellRenderer(new NoteCellRenderer());
		table.getColumnModel().getColumn(1).setCellRenderer(new DateCellRenderer());
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		table.setRowHeight(20);

	    table.setColumnSelectionAllowed(false);
	    table.setRowSelectionAllowed(true);
	    table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		table.getSelectionModel().addListSelectionListener(new JTableListeSelectionListener());
		
		panel_table.add(new JScrollPane(table));
		
		panel_filtre.setPreferredSize(new Dimension(this.getWidth(),200));
		panel_fiche.setPreferredSize(new Dimension(300,this.getHeight()));
		
		panel_bottom.add(panel_table,BorderLayout.CENTER);
		panel_bottom.add(panel_fiche,BorderLayout.EAST);
		
		this.add(panel_filtre,BorderLayout.NORTH);
		this.add(panel_bottom,BorderLayout.CENTER);
		
		panel_fiche.setVisible(false);
	}
	
	public void setListeFichesFilms(ArrayList<FicheFilm> liste_fiche_film) {
		this.model.update(liste_fiche_film);
	}
	
	public void updateFiltreActeur() {
		panel_filtre.updateFiltreActeur();
	}
	
	public void updateFiltreRealisateur() {
		panel_filtre.updateFiltreRealisateur();
	}
	
	public void updateFiltreType() {
		panel_filtre.updateFiltreType();
	}
	
	public void updateFiltreNation() {
		panel_filtre.updateFiltreNation();
	}
	
	private class ModelTable extends AbstractTableModel {
		private final ArrayList<FicheFilmRed> fiches_films = new ArrayList<FicheFilmRed>();
		private final String[] entetes = {"Titre", "Date de sortie", "Note","Visionné"};
		
		public ModelTable() {
			super();
		}
		
		public int getColumnCount() {
			return entetes.length;
		}

		public int getRowCount() {
			return fiches_films.size();
		}
		
		public String getColumnName(int columnIndex) {
	        return entetes[columnIndex];
	    }

		public Object getValueAt(int rowIndex, int columnIndex) {
			switch(columnIndex){
            case 0:
                return fiches_films.get(rowIndex).getTitre_f();
            case 1:
                return fiches_films.get(rowIndex).getDate();
            case 2:
                return fiches_films.get(rowIndex).getNote();
            case 3:
                return fiches_films.get(rowIndex).isSeen();
            default:
                return null; //Ne devrait jamais arriver
			}
		}
		
		public void setValueAt(Object edit, int rowIndex, int columnIndex) {
			FicheFilmRed oldFFR = fiches_films.get(rowIndex);
			switch(columnIndex) {
			case 0 :
				if (controller.tryEditFilm(oldFFR, new FicheFilmRed((String)edit,oldFFR.getDate(),oldFFR.getNote(),oldFFR.isSeen())) && !((String)edit).isEmpty())
					oldFFR.setTitre_f((String)edit);
				break;
			case 1 :
				if (controller.tryEditFilm(oldFFR, new FicheFilmRed(oldFFR.getTitre_f(),(Integer)edit,oldFFR.getNote(),oldFFR.isSeen())))
					oldFFR.setDate((Integer)edit);
				break;
			case 2 : 
				if (controller.tryEditFilm(oldFFR, new FicheFilmRed(oldFFR.getTitre_f(),oldFFR.getDate(),(Float)edit,oldFFR.isSeen())))
					oldFFR.setNote((Float)edit);
				break;
			case 3 :
				if (controller.tryEditFilm(oldFFR, new FicheFilmRed(oldFFR.getTitre_f(),oldFFR.getDate(),oldFFR.getNote(),(Boolean)edit)))
					oldFFR.setSeen((Boolean)edit);
				break;
			}
			
		}
		
		public void update(ArrayList<FicheFilm> lff) {
			fiches_films.clear();
			
			for (FicheFilm ff : lff) {
				fiches_films.add(new FicheFilmRed(ff));	
			}
			fireTableDataChanged();
		}
		
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int column) {
            switch (column) {
                case 0:
                    return String.class;
                case 1:
                    return Integer.class;
                case 2:
                    return Float.class;
                case 3:
                    return Boolean.class;
                default:
                    return String.class;
            }
        }
		
		public ArrayList<FicheFilmRed> getListeFFR() {
				return fiches_films;
		}
		
	}
	
	private class JTableListeSelectionListener implements ListSelectionListener {
		ArrayList<FicheFilmRed> LFFR = model.getListeFFR();
		public void valueChanged(ListSelectionEvent e) {
			boolean found = false;
			if (!e.getValueIsAdjusting()) {
				if (table.getSelectedRow() != -1) {
					for (FicheFilmRed ffr : LFFR) {
						if (ffr.getTitre_f() == table.getValueAt(table.getSelectedRow(), 0)) {
							fiche_film = controller.getFicheFilm(ffr.getTitre_disque());
							panel_fiche.updateFicheFilm(fiche_film);
							panel_fiche.setVisible(true);
						}
					}
				}
				else {
					if (fiche_film.getTitre_disque() != null && table.getRowCount() != 0) {
						for (int i = 0; i < table.getRowCount(); i++) {
							if (fiche_film.getTitre_f().equals(table.getValueAt(i,0)) && fiche_film.getDate() == (Integer)table.getValueAt(i, 1)) {
								table.getSelectionModel().setSelectionInterval(i, i);
								found = true;
							}
						}
						if (!found){
							panel_fiche.setVisible(false);
						}
					}
					else {
						panel_fiche.setVisible(false);
					}
				}
			}
		}
	}
	
	private class NoteCellRenderer extends DefaultTableCellRenderer {
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        
	        float note = (Float)value;
	        
	        this.setHorizontalAlignment(JLabel.CENTER);
	        
	        if (note == -1) {
	        	this.setText("-");
	        	this.setBackground(new Color(223,223,233));
	        }
	        else if (note < 1)
	        	this.setBackground(new Color(255,11,11));
	        else if (note < 2)
	        	this.setBackground(new Color(255,170,11));
	        else if (note < 3)
	        	this.setBackground(new Color(255,224,11));
	        else if (note < 4)
	        	this.setBackground(new Color(181,206,2));
	        else
	        	this.setBackground(new Color(0,227,17));
	        	
	        return this;
		}
	}
	
	private class DateCellRenderer extends DefaultTableCellRenderer {
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        
	        this.setHorizontalAlignment(JLabel.CENTER);
	        
	        int date = (Integer)value;
	        
	        if (date == -1)
	        	this.setText("-");
	        
	        return this;
		}
	}
}