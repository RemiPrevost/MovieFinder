package mf.database.pojo;

public class Nationalite {
	private String nation = "";
	
	public Nationalite(String nation) {
		this.nation = nation;
	}
	
	public Nationalite() {}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}
	
	public boolean isEmpty() {
		return this.nation.isEmpty();
	}
	
	public String toString(){
		return this.nation;
	}
}
