/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com_nos_classes;

import java.util.List;

/**
 *
 * @author zonne de travaille
 */
public class Etat_Deterministe {

    public Etat_Deterministe(Etat_transition etat, List<Etat_transition> liste_Etat, boolean marquage) {
        this.etat = etat;
        this.liste_Etat = liste_Etat;
        this.marquage=marquage;
    }

    public Etat_transition getEtat() {
        return etat;
    }

    public List<Etat_transition> getListe_Etat() {
        return liste_Etat;
    }

    public void setEtat(Etat_transition etat) {
        this.etat = etat;
    }

    public void setListe_Etat(List<Etat_transition> liste_Etat) {
        this.liste_Etat = liste_Etat;
    }

    @Override
    public String toString() {
        return "etat caracteriser par "+etat.getEtiquette_etat()+" la lise des etats "+liste_Etat;
    }

    public void setMarquage(boolean marquage) {
        this.marquage = marquage;
    }
    
    public boolean getMarquage(){
        return marquage;
    }

    
    //modification de la visiblite de l'etiquete
    public boolean compare(List<Etat_transition> etat){
        if(etat.size()==this.getListe_Etat().size()){
            for(int i=0; i<etat.size(); i++){
                if(etat.get(i).getEtiquette_etat()!=this.getListe_Etat().get(i).getEtiquette_etat())
                    return false;
            }
            
            return true;
        }
        
        return false;
    }
    
    public void Finalization(){
        for(Etat_transition c: this.getListe_Etat())
            if(c.isIsfinal())
                this.getEtat().setIsfinal(true);
        
    }
    
    
    
    private boolean marquage;
    private Etat_transition etat;
    private List<Etat_transition> liste_Etat;
}
