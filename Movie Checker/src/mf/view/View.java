package mf.view;

import mf.controller.Controller;
import mf.model.listeners.*;

public abstract class View implements ListeRealisateurListener, ListeActeurListener, ListeTypeListener, ListeNationaliteListener, ListeFicheFilmListener {
	public static final int affiche_height = 250;
	public static final int affiche_width = 200;
	
	private Controller controller = null;
	 
	public View(Controller controller){
		super();
 
		this.controller = controller;
	}
 
	public final Controller getController(){
		return controller;
	}
 
}
