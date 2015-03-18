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

			// initialisation de destination*
			tab_labels[this.destination] = new Label(this.graphe.getTabNode()[this.destination]);
		
			// initialisation du tableau de labels
			// estimation en temps
			if (Graphe.getMode()==Mode.TEMPS) {
				for (int i = 0; i < tab_labels.length; i++) {
					tab_labels[i] = new Label(super.graphe.getTabNode()[i]);
					tab_labels[i].setCoutDest(60/((double)this.graphe.getVitMax()*1000)*Graphe.distance(tab_labels[i].getCourant().getX(), tab_labels[i].getCourant().getY(), tab_labels[this.destination].getCourant().getX(), tab_labels[this.destination].getCourant().getY())) ;
				}
			}
			// estimation en distance
			else {
				for (int i = 0; i < tab_labels.length; i++) {
					tab_labels[i] = new Label(super.graphe.getTabNode()[i]);
					tab_labels[i].setCoutDest(Graphe.distance(tab_labels[i].getCourant().getX(), tab_labels[i].getCourant().getY(), tab_labels[this.destination].getCourant().getX(), tab_labels[this.destination].getCourant().getY())) ;
				}
			}			
	}
	
	public void afficherResultats (int compteur) {
		// afficher le cout du chemin
		System.out.println("Cout du chemin par methode Dijkstra A-Star : "	+ tab_labels[destination].getCout());
		// afficher le nombre max d'element dans le tas
		System.out.println("Nb max d'element : " + compteur);
	}

}
