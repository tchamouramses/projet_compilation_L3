package com_interface;

import com_nos_classes.Automate;
import com_nos_classes.Etat_Deterministe;
import com_nos_classes.Etat_transition;
import com_nos_classes.Stockage;
import com_nos_classes.Transition;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author zonne de travaille
 */
public interface verification {
    public List<Stockage> listeValeur(String chaine);
    
    //operateurs ou symbole?
	public boolean isOperateur(char valeur);
    
        public boolean inCorectChar(String chaine);
    
        public List salhpabet(String alphabet);
        
        public boolean evaluation(Automate a, String mot);
        
        public Etat_transition transition(Etat_transition etat, char car);
        
        public boolean isInto(char mot);
        
        public List<Etat_transition> e_Fermeture(List<Etat_transition> etat);
        
        public Automate AFD(Automate afn, String val);
        
        public boolean isInto(List<Etat_transition> pile, Etat_transition etat);
        
        public List<Etat_transition> transiter(List<Etat_transition> etat, char symbole);
        
        public Etat_Deterministe isInto(List<Etat_Deterministe> etat, List<Etat_transition> trans);
        
        public Etat_Deterministe etatMarquer(List<Etat_Deterministe> etat);
        
        public List<Transition> transitionList(Etat_transition etat, char car);
        
        public String supChar(String mot);
        
}
