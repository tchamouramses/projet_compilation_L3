package com_nos_classes;

public class Stockage {
	
	//on stockera une valeur dans la liste qui sera soit un symbole soit un etiquette de etat. on pourra se munit 
	char valeur;
	int arbitre;
	public Stockage(char valeur, int arbitre) {
		super();
		this.valeur = valeur;
		this.arbitre = arbitre;
	}
	public char getValeur() {
		return valeur;
	}
	public void setValeur(char valeur) {
		this.valeur = valeur;
	}
	public int getArbitre() {
		return arbitre;
	}
	public void setArbitre(int arbitre) {
		this.arbitre = arbitre;
	}
	
	
}
