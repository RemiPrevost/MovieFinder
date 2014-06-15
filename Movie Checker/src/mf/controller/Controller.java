package mf.controller;

import java.util.ArrayList;

import mf.database.pojo.*;
import mf.model.FicheFilm;
import mf.model.FicheFilmRed;
import mf.model.IntervalleDate;
import mf.model.Model;
import mf.view.Fenetre;
import mf.view.View;

public class Controller {
	public View view = null;
	
	private Model model = null;
	
	public Controller(Model model) {
		this.model = model;
		
		view = new Fenetre(this);
		
		model.addListeRealisateurListener(view);
		model.addListeActeurListener(view);
		model.addListeTypeListener(view);
		model.addListeNationaliteListener(view);
		model.addListeFicheFilmListener(view);
		
		model.RaiseFireListeRealisateurChanged();
		model.RaiseFireListeActeurChanged();
		model.RaiseFireListeGenreChanged();
		model.RaiseFireListeNationaliteChanged();
		model.RaiseFireListeFicheFilmChanged();
	}
	// Film
	
	public void updateF(String titre_disque, boolean seen, FicheFilm fiche_film) {
		model.updateF(titre_disque, seen, fiche_film);
	}
	
	public void deleteF(String titre_disque) {
		model.deleteF(titre_disque);
	}
	
	public FicheFilm getFicheFilm(String titre_disque) {
		return model.getFicheFilm(titre_disque);	
	}
	
	public boolean tryEditFilm(FicheFilmRed oldFFR, FicheFilmRed newFFR) {
		return model.editF(oldFFR, newFFR);
	}
	
	public void editFiltreIntervalleDate(IntervalleDate intervalle_date){
		model.editFiltreIntervalleDate(intervalle_date);
	}
	
	public IntervalleDate getDateExtrem() {
		return model.getDateExtrem();
	}
	
	public void editFiltreCurseurNote(int note_min) {
		model.editFiltreCurseurNote(note_min);
	}
	
	public void editFiltreSeen(int seen) {
		model.editFiltreSeen(seen);
	}
	
	public void editFiltreTitre(String titre) {
		model.editFiltreTitre(titre);
	}
	
	public ArrayList<Acteur> getListeActeurFiltre(ArrayList<Acteur> la) {
		return model.getListeActeurFiltre(la);
	}
	
	public void setActeurSelected(Acteur A) {
		model.setActeurSelected(A);
	}
	
	public ArrayList<Realisateur> getListeRealisateurFiltre(ArrayList<Realisateur> la) {
		return model.getListeRealisateurFiltre(la);
	}
	
	public void setRealisateurSelected(Realisateur A) {
		model.setRealisateurSelected(A);
	}
	
	public ArrayList<Type> getListeTypeFiltre(ArrayList<Type> la) {
		return model.getListeTypeFiltre(la);
	}
	
	public void setTypeSelected(Type A) {
		model.setTypeSelected(A);
	}
	
	public ArrayList<Nationalite> getListeNationaliteFiltre(ArrayList<Nationalite> ln) {
		return model.getListeNationaliteFiltre(ln);
	}
	
	public void setNationaliteSelected(Nationalite N) {
		model.setNationSelected(N);
	}
	
	// Pour Realisateur
	
	public boolean tryAddRealisateur(String nom, String prenom) {
		return model.addLR(nom, prenom);
	}
	
	public void tryRemoveRealisateur(String nom, String prenom) {
		model.removeR(nom, prenom);
	}
	
	public boolean tryEditRealisateur(Realisateur OldR, Realisateur NewR) {
		return model.editR(OldR,NewR);
	}
	
	public boolean isPresentRealisateur(Realisateur R) {
		return model.isPresentRealisateur(R);
	}
	
	// Pour Acteur
	
	public boolean tryAddActeur(String nom, String prenom) {
		return model.addLA(nom, prenom);
	}
	
	public void tryRemoveActeur(String nom, String prenom) {
		model.removeA(nom, prenom);
	}
	
	public boolean tryEditActeur(Acteur OldA, Acteur NewA) {
		return model.editA(OldA,NewA);
	}
	
	public boolean isPresentActeur(Acteur A) {
		return model.isPresentActeur(A);
	}
	
	// Pour Type
	
	public boolean tryAddType(String nom) {
		return model.addLG(nom);
	}
	
	public void tryRemoveType(String nom) {
		model.removeG(nom);
	}
	
	public boolean tryEditType(Type OldG, Type NewG) {
		return model.editG(OldG,NewG);
	}
	
	public boolean isPresentType(Type G) {
		return model.isPresentType(G);
	}
	
	// Pour Nationalite
	
	public boolean tryAddNationalite(String nom) {
		return model.addLN(nom);
	}
		
	public void tryRemoveNationalite(String nom) {
		model.removeN(nom);
	}
		
	public boolean tryEditNationalite(Nationalite OldN, Nationalite NewN) {
		return model.editN(OldN,NewN);
	}
		
	public boolean isPresentNationalite(Nationalite N) {
		return model.isPresentNationalite(N);
	}
}
