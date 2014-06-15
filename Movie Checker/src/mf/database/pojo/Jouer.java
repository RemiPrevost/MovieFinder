package mf.database.pojo;

public class Jouer {
	private Acteur acteur = new Acteur();
	private Film film = new Film();
	
	public Jouer(Acteur acteur, Film film) {
		this.acteur = acteur;
		this.film = film;
	}
	
	public Jouer(Film film) {
		this.film = film;
	}
	
	public Jouer() {}

	/**
	 * @return the a
	 */
	public Acteur getActeur() {
		return acteur;
	}

	/**
	 * @param a the a to set
	 */
	public void setActeur(Acteur acteur) {
		this.acteur = acteur;
	}

	/**
	 * @return the f
	 */
	public Film getFilm() {
		return this.film;
	}

	/**
	 * @param f the f to set
	 */
	public void setFilm(Film film) {
		this.film = film;
	}
	
	public boolean isEmpty(){
		return (this.acteur.isEmpty() && this.film.isEmpty());
	}

	public String toString(){
		return this.acteur.toString() + " joue dans " + film.getTitreDisque();
	}
}
