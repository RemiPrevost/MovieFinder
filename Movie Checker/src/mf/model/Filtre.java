package mf.model;

import java.util.ArrayList;

import mf.database.pojo.Acteur;
import mf.database.pojo.Nationalite;
import mf.database.pojo.Realisateur;
import mf.database.pojo.Type;

public class Filtre {
	private String titre_partiel = new String();
	private ArrayList<Realisateur> liste_realisateurs = new ArrayList<Realisateur>();
	private ArrayList<Acteur> liste_acteurs = new ArrayList<Acteur>();
	private ArrayList<Type> liste_genres = new ArrayList<Type>();
	private ArrayList<Nationalite> liste_nations = new ArrayList<Nationalite>();
	private int date_min = -1;
	private int note_min = -1;
	private int seen = -1;
	
	public Filtre() {}

	public String getTitre_partiel() {
		return titre_partiel;
	}

	public void setTitre_partiel(String titre_f) {
		this.titre_partiel = titre_f;
	}

	public ArrayList<Realisateur> getListe_realisateurs() {
		return liste_realisateurs;
	}

	public void setListe_realisateurs(ArrayList<Realisateur> liste_realisateurs) {
		this.liste_realisateurs = liste_realisateurs;
	}

	public ArrayList<Acteur> getListe_acteurs() {
		return liste_acteurs;
	}

	public void setListe_acteurs(ArrayList<Acteur> liste_acteurs) {
		this.liste_acteurs = liste_acteurs;
	}

	public ArrayList<Type> getListe_genres() {
		return liste_genres;
	}

	public void setListe_genres(ArrayList<Type> liste_genres) {
		this.liste_genres = liste_genres;
	}

	public ArrayList<Nationalite> getListe_nations() {
		return liste_nations;
	}

	public void setListe_nations(ArrayList<Nationalite> liste_nations) {
		this.liste_nations = liste_nations;
	}

	public int getDate_min() {
		return date_min;
	}

	public void setDate_min(int date_min) {
		this.date_min = date_min;
	}

	public int getNote_min() {
		return note_min;
	}

	public void setNote_min(int note_min) {
		this.note_min = note_min;
	}

	public int getSeen() {
		return seen;
	}

	public void setSeen(int seen) {
		this.seen = seen;
	}

	
	
}
