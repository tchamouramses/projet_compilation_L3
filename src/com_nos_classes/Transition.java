package com_nos_classes;

public class Transition {

		private char symbole;
		private Etat_transition destination;
		public char getSymbole() {
			return symbole;
		}
		public void setSymbole(char symbole) {
			this.symbole = symbole;
		}
                
                
		public Etat_transition getDestination() {
			return destination;
		}
                
                
		public void setDestination(Etat_transition destination) {
			this.destination = destination;
		}
                
                
		public Transition(char symbole, Etat_transition destination) {
			super();
			this.symbole = symbole;
			this.destination = destination;
		};
		
		
		
}
