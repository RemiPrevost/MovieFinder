package mf.view.panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Panel extends JPanel {
	private static int i = 1;
	private String nom_pan = "default " + i;
	
	public Panel(){
		super(new BorderLayout());
		i++;
	}
	
	public Panel(String nom){
		super(new BorderLayout());
		this.nom_pan = nom;
		i++;
	}

	
	public String getNomPan(){
		return this.nom_pan;
	}
		
}
