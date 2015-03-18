package core ;

import java.awt.Color;
import java.io.* ;

import core.Graphe.Mode;
import base.Readarg ;

public class PccStar extends Pcc {

	public PccStar(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
		couleur=Color.BLACK;
	}

	
	protected void init() {

		System.out.println("Run PCC-Star de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

			// initialisation de destination
			tab_labels[this.destination] = new Label(this.graphe.getTabNode()[this.destination]);
		
			// initialisation du tableau de labels
			// estimation en temps (en minutes)
			if (Graphe.getMode()==Mode.TEMPS) {
				for (int i = 0; i < tab_labels.length; i++) {
					tab_labels[i] = new Label(super.graphe.getTabNode()[i]);
					tab_labels[i].setCoutDest(60/((double)this.graphe.getVitMax()*1000)*(double)Graphe.distance(tab_labels[i].getCourant().getX(), tab_labels[i].getCourant().getY(), tab_labels[this.destination].getCourant().getX(), tab_labels[this.destination].getCourant().getY())) ;
				}
			}
			// estimation en distance (en metres)
			else {
				for (int i = 0; i < tab_labels.length; i++) {
					tab_labels[i] = new Label(super.graphe.getTabNode()[i]);
					tab_labels[i].setCoutDest((double)Graphe.distance(tab_labels[i].getCourant().getX(), tab_labels[i].getCourant().getY(), tab_labels[this.destination].getCourant().getX(), tab_labels[this.destination].getCourant().getY())) ;
				}
			}			
	}
	
	
	//affichage des resultats de Dijkstra A-Star
	public void afficherResultats (int max_tas, int max_elements) {
		// afficher le cout du chemin
		if (Graphe.getMode()==Mode.DISTANCE)
			System.out.println("\nCout du chemin par methode Dijkstra A-Star : "	+ tab_labels[destination].getCout()/1000+" kilometres");
		else 
			System.out.println("\nCout du chemin par methode Dijkstra A-Star : "	+ tab_labels[destination].getCout()+" minutes");
		// afficher le nombre max d'element dans le tas et le nombre max d'elements explores
		System.out.println("Nb max d'element dans le tas : " + max_tas + "\n");
		System.out.println("Nb max d'element explores : " + max_elements + "\n");
	}

}