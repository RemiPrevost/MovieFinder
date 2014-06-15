package mf.model;

import java.util.ArrayList;

import mf.database.pojo.Acteur;
import mf.database.pojo.Film;
import mf.database.pojo.Realisateur;
import mf.database.pojo.Type;
import mf.database.pojo.Nationalite;

public class FicheFilm {
	private String titre_f = null;
	private String titre_vo = null; //en option pour plus tard
	private String titre_disque = null;
	private ArrayList<Realisateur> liste_realisateurs = new ArrayList<Realisateur>();
	private ArrayList<Acteur> liste_acteurs = new ArrayList<Acteur>();
	private ArrayList<Type> liste_genres = new ArrayList<Type>();
	private ArrayList<Nationalite> liste_nations = new ArrayList<Nationalite>();
	private int date = -1;
	private float note = -1;
	private boolean seen = false;
	private String affiche_url =null;
	
	public FicheFilm(Film f, ArrayList<Realisateur> lr, ArrayList<Acteur> la, ArrayList<Type> lg, ArrayList<Nationalite> ln) {
		this.titre_f = f.getTitre();
		this.titre_disque = f.getTitreDisque();
		this.date = f.getDate();
		this.note = f.getNote();
		this.seen = f.isSeen();
		this.affiche_url = f.getAffiche();
		this.liste_realisateurs = lr;
		this.liste_acteurs = la;
		this.liste_genres = lg;
		this.liste_nations = ln;
	}
	
	public FicheFilm() {}

	public String getTitre_vo() {
		return titre_vo;
	}

	public void setTitre_vo(String titre_vo) {
		this.titre_vo = titre_vo;
	}

	public float getNote() {
		return note;
	}

	public void setNote(float note) {
		this.note = note;
	}

	public String getTitre_f() {
		return titre_f;
	}

	public void setTitre_f(String titre_f) {
		this.titre_f = titre_f;
	}

	public String getTitre_disque() {
		return titre_disque;
	}

	public void setTitre_disque(String titre_disque) {
		this.titre_disque = titre_disque;
	}

	public ArrayList<Realisateur> getListe_realisateurs() {
		return liste_realisateurs;
	}

	public void addRealisateurs(Realisateur r) {
		this.liste_realisateurs.add(r);
	}

	public ArrayList<Acteur> getListe_acteurs() {
		return liste_acteurs;
	}

	public void addActeur(Acteur a) {
		this.liste_acteurs.add(a);
	}

	public ArrayList<Type> getListe_genres() {
		return liste_genres;
	}

	public void addGenre(Type g) {
		this.liste_genres.add(g);
	}

	public ArrayList<Nationalite> getListe_nations() {
		return liste_nations;
	}

	public void addNation(Nationalite n) {
		this.liste_nations.add(n);
	}
	
	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public String getAffiche_url() {
		return affiche_url;
	}

	public void setAffiche_url(String affiche_url) {
		this.affiche_url = affiche_url;
	}

	@Override
	public String toString() {
		return "FicheFilm ["
				+ (titre_f != null ? "titre_f=" + titre_f + ", " : "")
				+ (titre_vo != null ? "titre_vo=" + titre_vo + ", " : "")
				+ (liste_realisateurs != null ? "liste_realisateurs="
						+ liste_realisateurs + ", " : "")
				+ (liste_acteurs != null ? "liste_acteurs=" + liste_acteurs
						+ ", " : "")
				+ (liste_genres != null ? "liste_genres=" + liste_genres + ", "
						: "")
				+ (liste_nations != null ? "liste_nations=" + liste_nations
						+ ", " : "") + "date=" + date + ", note=" + note + ", "
				+ (affiche_url != null ? "affiche_url=" + affiche_url : "")
				+ "]";
	}

	
}
