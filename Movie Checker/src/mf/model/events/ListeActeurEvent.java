package mf.model.events;

import java.util.ArrayList;
import java.util.EventObject;

import mf.database.pojo.Acteur;


@SuppressWarnings("serial")
public class ListeActeurEvent extends EventObject {
	private ArrayList<Acteur> newLA = new ArrayList<Acteur>();
	
	public ListeActeurEvent(Object source, ArrayList<Acteur> newLA){
		super(source);
		
		this.newLA = newLA;
	}
	
	public  ArrayList<Acteur> getNewListeActeur(){
		return this.newLA;
	}
}