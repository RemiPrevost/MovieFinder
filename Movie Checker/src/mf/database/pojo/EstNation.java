package mf.database.pojo;

import mf.exception.bdd.InvalidData;

public class EstNation {
	private Film film = new Film();
	private Nationalite nation = new Nationalite();
	
	public EstNation(Film film, Nationalite nation) throws InvalidData {
		if (film.isEmpty() && nation.isEmpty())
			throw new InvalidData("Cannot empty film and/or nation in table est_nation");
		
		this.film = film;
		this.nation = nation;
	}
	
	public EstNation(Film film) {
		this.film = film;
	}
	
	public EstNation() {}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public Nationalite getNation() {
		return nation;
	}

	public void setNation(Nationalite nation) {
		this.nation = nation;
	}

	@Override
	public String toString() {
		return this.film.getTitreDisque() + " est un film " + this.nation.getNation();
	}
	
	public boolean isEmpty() {
		return (this.film.isEmpty() && this.nation.isEmpty());
	}
}
