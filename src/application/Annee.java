package application;


public class Annee {
	
	private int annee;
	
	public Annee(int annee) {
		this.annee = annee;
	}


	public int getAnnee() {
		return this.annee;
	}
	

	@Override
	public String toString() {
		String res="";
		res = res +this.annee;
		return res;
	}
}