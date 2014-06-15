package mf.database.pojo;

public class Type {
	private String genre = "";
	
	public Type(String genre) {
		this.genre = genre;
	}
	
	public Type() {}

	/**
	 * @return the genre
	 */
	public String getGenre() {
		return genre;
	}

	/**
	 * @param genre the genre to set
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public String toString() {
		return this.genre;
	}

	public boolean isEmpty() {
		return genre.isEmpty();
	}
	
	
}
