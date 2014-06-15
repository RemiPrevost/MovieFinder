package mf.database.pojo;

public class Realisateur {
	private String nom = "";
	private String prenom = "";
	
	public Realisateur(String nom, String prenom) {
		this.nom = nom;
		this.prenom = prenom;
	}
	
	public Realisateur(){}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public boolean isEmpty() {
		return (this.nom.isEmpty() && this.prenom.isEmpty());
	}
	
	public String toString(){
		return (this.prenom + " " + this.nom);
	}
}
