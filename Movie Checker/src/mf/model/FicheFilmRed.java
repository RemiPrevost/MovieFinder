package mf.model;

public class FicheFilmRed {
	private String titre_f = null;
	private String titre_disque = null;
	private int date = -1;
	private float note = -1;
	private boolean seen = false;
		
	public FicheFilmRed(FicheFilm ff){
		this.setTitre_f(ff.getTitre_f());
		this.setDate(ff.getDate());
		this.setNote(ff.getNote());
		this.setSeen(ff.isSeen());
		this.setTitre_disque(ff.getTitre_disque());
	}
	
	public FicheFilmRed(String titre_f, int date, float note, boolean seen) {
		this.titre_f = titre_f;
		this.date = date;
		this.note = note;
		this.seen = seen;
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

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public float getNote() {
		return note;
	}

	public void setNote(float note) {
		this.note = note;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}
}
