package com_nos_classes;

import java.util.List;

public class Etat_transition {
		
	private  int etiquette_etat;
	private List<Transition> liste_de_transition;
	private boolean isfinal;
	public Etat_transition(List<Transition> liste_de_transition, boolean isfinal) {
		super();
		this.etiquette_etat = et;
		this.liste_de_transition = liste_de_transition;
		this.isfinal = isfinal;
		et=et+1;
	}
	public int getEtiquette_etat() {
		return etiquette_etat;
	}
	public void setEtiquette_etat(int etiquette_etat) {
		this.etiquette_etat = etiquette_etat;
	}
	public List<Transition> getListe_de_transition() {
		return liste_de_transition;
	}
	public void setListe_de_transition(List<Transition> liste_de_transition) {
		this.liste_de_transition = liste_de_transition;
	}
	public boolean isIsfinal() {
		return isfinal;
	}
	public void setIsfinal(boolean isfinal) {
		this.isfinal = isfinal;
	}
	
	
	public static int et=0;
}
