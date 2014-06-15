package mf.model.events;

import java.util.ArrayList;
import java.util.EventObject;

import mf.model.FicheFilm;

@SuppressWarnings("serial")
public class ListeFicheFilmEvent extends EventObject {
	private ArrayList<FicheFilm> newLFF = new ArrayList<FicheFilm>();
	
	public ListeFicheFilmEvent(Object source, ArrayList<FicheFilm> newLFF){
		super(source);
		
		this.newLFF = newLFF;
	}
	
	public  ArrayList<FicheFilm> getNewListeFicheFilm(){
		return this.newLFF;
	}
}