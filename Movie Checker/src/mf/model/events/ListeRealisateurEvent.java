package mf.model.events;

import java.util.ArrayList;
import java.util.EventObject;

import mf.database.pojo.Realisateur;

@SuppressWarnings("serial")
public class ListeRealisateurEvent extends EventObject {
	private ArrayList<Realisateur> newLR = new ArrayList<Realisateur>();
	
	public ListeRealisateurEvent(Object source, ArrayList<Realisateur> newLR){
		super(source);
		
		this.newLR = newLR;
	}
	
	public  ArrayList<Realisateur> getNewListeRealisateur(){
		return this.newLR;
	}
}
