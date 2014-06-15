package mf.model.events;

import java.util.ArrayList;
import java.util.EventObject;

import mf.database.pojo.Type;

@SuppressWarnings("serial")
public class ListeTypeEvent extends EventObject {
	private ArrayList<Type> newLG = new ArrayList<Type>();
	
	public ListeTypeEvent(Object source, ArrayList<Type> newLG){
		super(source);
		
		this.newLG = newLG;
	}
	
	public  ArrayList<Type> getNewListeType(){
		return this.newLG;
	}
}
