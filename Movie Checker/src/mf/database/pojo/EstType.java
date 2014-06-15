package mf.database.pojo;

import mf.exception.bdd.InvalidName;

public class EstType {
	private Film film = new Film();
	private Type genre = new Type();
	
	public EstType (Film film, Type genre) throws InvalidName {
		if (film.isEmpty() && genre.getGenre().isEmpty())
			throw new InvalidName("Cannot create new Est_Type with empty parameters. Use new Est_Type() instead");
		
		this.film = film;
		this.genre = genre;
	}
	
	public EstType(Film film) {
		this.film = film;
	}
	
	public EstType() {}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public Type getGenre() {
		return genre;
	}

	public void setGenre(Type genre) {
		this.genre = genre;
	}
	
	public boolean isEmpty(){
		return (this.film.isEmpty() && this.genre.getGenre().isEmpty());
	}

	public String toString(){
		return this.film.getTitreDisque() + " est du genre " + genre.toString();
	}
}
