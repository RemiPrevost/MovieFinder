package mf.model.events;

import java.util.ArrayList;
import java.util.EventObject;

import mf.database.pojo.Nationalite;

@SuppressWarnings("serial")
public class ListeNationaliteEvent extends EventObject {
	private ArrayList<Nationalite> newLN = new ArrayList<Nationalite>();
	
	public ListeNationaliteEvent(Object source, ArrayList<Nationalite> newLN){
		super(source);
		
		this.newLN = newLN;
	}
	
	public  ArrayList<Nationalite> getNewListeNationalite(){
		return this.newLN;
	}
}
