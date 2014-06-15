package mf.database.pojo;

import mf.exception.bdd.InvalidName;

public class Acteur {
	private String nom = "";
	private String prenom = "";
	
	public Acteur(String nom, String prenom) throws InvalidName {
		
		if (nom.isEmpty() && prenom.isEmpty())
			throw new InvalidName("Cannot add actor with empty fields through this constructor, use new Acteur() instead.");
		this.nom = nom;
		this.prenom = prenom;
	}
	
	public Acteur(){}

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
