package mf.database.pojo;

import java.util.Calendar;

import mf.exception.bdd.InvalidDate;
import mf.exception.bdd.InvalidName;

public class Film {
	private String titre_disque = "";
	private String titre = "";
	private int date = -1;
	private float note = -1;
	private int seen = -1;
	private String affiche = "";
	
	public Film(String titre_disque) throws InvalidName {
		
		if (titre_disque.isEmpty())
			throw new InvalidName("Cannot add movie with empty title, use new Film() instead");
		
		this.titre_disque = titre_disque;
	}
	
	public Film(String titre_disque, String titre, int date, int seen, String affiche, float note) throws InvalidDate {
		
		if ((date < 1800 || date > Calendar.getInstance().get(Calendar.YEAR)) && date != -1)
			throw new InvalidDate("Invalid choosen year");
		
		this.titre_disque = titre_disque;
		this.titre = titre;
		this.date = date;
		if (seen == 1 || seen == 0)
			this.seen = seen;
		this.affiche = affiche;
		this.note = note;
	}
	
	public Film(String titre_disque, String titre, int date, int seen) throws InvalidDate {
		
		if ((date < 1800 || date > Calendar.getInstance().get(Calendar.YEAR)) && date != -1)
			throw new InvalidDate("Invalid choosen year");
		
		this.titre_disque = titre_disque;
		this.titre = titre;
		this.date = date;
		if (seen == 1 || seen == 0)
			this.seen = seen;
	}
	
	public Film(){}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}
	
	public String getTitreDisque() {
		return titre_disque;
	}

	public void setTitreDisque(String titre_disque) {
		this.titre_disque = titre_disque;
	}

	public int getDate() {
		return this.date;
	}

	public void setDate(int date) throws InvalidDate {
		if ((date < 1800 || date > Calendar.getInstance().get(Calendar.YEAR)) && date != -1)
			throw new InvalidDate("Invalid choosen year");
		
		this.date = date;
	}

	public float getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}

	public boolean isSeen() {
		if (this.seen == 1)
			return true;
		else
			return false;
	}
	
	public int getSeen() {
		return this.seen;
	}

	public void setSeen(boolean seen) {
		if (seen)
			this.seen = 1;
		else
			this.seen = 0;
	}
	
	public String getAffiche() {
		return affiche;
	}

	public void setAffiche(String affiche) {
		this.affiche = affiche;
	}

	public boolean isEmpty(){
		return (this.titre_disque.isEmpty() && this.titre_disque.isEmpty() && (this.date == -1) && (this.note == -1) && (this.seen == -1));
	}
	
	public String toString() {
		String s = this.titre_disque + " | ";
		if (!this.titre.isEmpty())
			s += this.titre;
		else 
			s += "unknown title";
		if (this.date != -1)
			s += " | " + this.date;
		else 
			s += " | unknown year";
		if (this.note != -1)
			s += " | " + this.note;
		else
			s+= " | unknown note";
		if (this.seen == 1)
			s += " | Seen";
		else if(this.seen == 0)
			s += " | Unseen";
		else
			s += " | seen ?";
		if (!this.affiche.isEmpty())
			s += " | " + this.affiche;
		else
			s += " | unknown affiche";
			
		return s;
	}
	
}
