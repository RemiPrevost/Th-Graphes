package core;

public class Label implements Comparable<Label> {

	// boolean valant vrai si le sommet est definitivement fixe par l'algorithme
	private boolean marquage ; 

	// valeur courante du plus court chemin depuis l'origine vers le sommet
	private float cout ;
	
	// valeur courant de l'estimation vers la destination
	private double cout_dest ; 
	
	// sommet precedent
	private int pere ;
	
	// sommet courant
	private Node courant ;
	
	public Label() {
		this.marquage = false; 
		this.cout = 99999999 ;
		this.pere = -1 ;
		this.courant=null;
		this.cout_dest = 0 ;
	}
	
	public Label(Node courant) {
		this.marquage = false; 
		this.cout = 99999999 ;
		this.pere = -1 ;
		this.courant=courant;
		this.cout_dest = 0;
	}
	
	public Label (int pere, Node courant) {
		this.marquage = false; 
		this.cout = 0 ;
		this.pere = pere ; 
		this.courant=courant; 
		this.cout_dest = 0;
	}
	
	public boolean isMarquage() {
		return marquage;
	}

	public float getCout() {
		return cout;
	}

	public int getPere() {
		return pere;
	}

	public Node getCourant() {
		return courant ;
	}
	
	public double getCoutDest() {
		return cout_dest ;
	}
	
	// methode qui compare 2 labels au niveau des couts : cout depuis l'origine + cout estime a la destination
	public int compareTo(Label obj) {		
		double cout_diff = this.getCout()+this.getCoutDest()-obj.getCout()-obj.getCoutDest() ;
		return (int)(1000*cout_diff) ;
	}

	public void setCout (float cout) {
		this.cout = cout ;
	}

	public void setPere(int x) {
		this.pere = x ;
	}

	public void setMarquage() {
		this.marquage=true ;
	}
	
	public void setCoutDest(double cout) {
		this.cout_dest = cout ;
	}
}