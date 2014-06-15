package mf.database.pojo;

public class Realiser {
	private Realisateur realisateur= new Realisateur();
	private Film film = new Film();
	
	public Realiser(Realisateur realisateur, Film film) {
		this.realisateur= realisateur;
		this.film = film;
	}
	
	public Realiser(Film film) {
		this.film = film;
	}
	
	public Realiser() {}

	/**
	 * @return the a
	 */
	public Realisateur getRealisateur() {
		return this.realisateur;
	}

	/**
	 * @param Rthe Rto set
	 */
	public void setRealisateur(Realisateur realisateur) {
		this.realisateur= realisateur;
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
	public void setF(Film film) {
		this.film = film;
	}
	
	public boolean isEmpty(){
		return (this.realisateur.isEmpty() && this.film.isEmpty());
	}
	
	public String toString(){
		return this.realisateur.toString() + " a réalisé " + film.getTitreDisque();
	}	
}
