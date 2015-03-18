package core;

import java.awt.Color;
import java.io.PrintStream;
import java.util.ArrayList;

import core.Graphe.Mode;

import base.Readarg;

public class Covoiturage extends Algo {


	//Numero des sommets origine et destination
	private int origineV;
	private int origineP;
	private int destination;

	public Covoiturage(Graphe gr, PrintStream fichierSortie, Readarg readarg) {
		super(gr, fichierSortie, readarg);

		this.origineV = readarg.lireInt("Numero du sommet d'origine de la voiture ? ");

		this.origineP = readarg.lireInt("Numero du sommet d'origine du piéton ? ");

		this.destination = readarg.lireInt("Numero du sommet destination ? ");

		int mode = readarg.lireInt("Cout en temps(0) ou distance(1) ? (par défaut c'est le cout en temps) ") ;
		if (mode == 1) 
			Graphe.setMode(Mode.DISTANCE);
		else
			Graphe.setMode(Mode.TEMPS) ;
	}

	public void run() {
		System.out.println("Dessin") ;
		Chemin.dessineSommet(graphe.getDessin(), Color.ORANGE, graphe.getTabNode()[origineV]) ;
		Chemin.dessineSommet(graphe.getDessin(), Color.BLACK, graphe.getTabNode()[origineP]) ;
		Chemin.dessineSommet(graphe.getDessin(), Color.BLUE, graphe.getTabNode()[destination]) ;

		// PCC de voiture vers tous
		PccGenerique pccV = new PccGenerique(graphe, System.out, origineV, false);
		pccV.run();
		
		// PCC du pieton vers tous
		PccGenerique pccP = new PccGenerique(graphe, System.out, origineP, true);
		pccP.run();
		
		// Determination de l'intersection entre les deux parcours
		Label[] tab_labels =intersection(pccV.getTabLabels(), pccP.getTabLabels());
		
		// Dijkstra inverse depuis destination vers tous
		Graphe graphe_inv = new Graphe(graphe) ;
		graphe_inv.InverserGraphe();
		PccGenerique pccD = new PccGenerique(graphe_inv, System.out, destination, false);
		pccD.run();
		
		// Determination de l'intersection entre la premiere intersection et le dernier parcours
		tab_labels = intersection (tab_labels,pccD.getTabLabels());
		
		// Affichage du sommet trouve 
		System.out.println(getMin(tab_labels).toString());
		Chemin.dessineSommet(graphe.getDessin(), Color.RED, getMin(tab_labels)) ;
		
		// construction des chemins
		Chemin c1=pccV.buildChemin(getMin(tab_labels), false) ;
		Chemin c2=pccP.buildChemin(getMin(tab_labels), true) ;
		Chemin c3=pccD.buildChemin(getMin(tab_labels), false) ;
		
		// calcul des couts totaux
		double cout_total = (Math.max(c1.getCout(), c2.getCout())+c3.getCout()) ;
		if (Graphe.getMode()==Mode.DISTANCE)
			System.out.println("\nCout total du covoiturage : " + cout_total/1000 + " kilometres.") ;
		else 
			System.out.println("\nCout total du covoiturage : " + cout_total + " minutes.") ;
		
	}

	public Label[] intersection(Label[] tab1, Label[] tab2) {
		ArrayList<Label> liste_labels = new ArrayList<Label>(); 
		for (int i=0; i<tab1.length ; i++) {
			if (tab1[i].isMarquage() && tab2[i].isMarquage()) {
				liste_labels.add(tab1[i]);
				// mise a jour des couts : somme des deux couts
				liste_labels.get(liste_labels.size()-1).setCout(tab1[i].getCout()+tab2[i].getCout()) ;
			}
		}
		return ArrayListToTab(liste_labels);
	}

	public Node getMin(Label[] tab_labels) {
		double cout_min = Double.MAX_VALUE;
		Node elu = null;

		for (Label l : tab_labels) {
			if (cout_min > l.getCout()) {
				cout_min = l.getCout();
				elu = l.getCourant();
			}
		}
		if (elu == null) 
			System.out.print("ERREUR : Intersection nulle!");

		return elu;
	}
	
	private Label[] ArrayListToTab(ArrayList<Label> liste_labels) {
		Label[] tab_labels = new Label[liste_labels.size()];
		
		for (int i=0; i<liste_labels.size(); i++) {
			tab_labels[i] = liste_labels.get(i);
		}
		
		return tab_labels;
	}

}