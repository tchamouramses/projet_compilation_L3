package com_implementation;

import java.util.ArrayList;
import java.util.List;

import com_interface.Nos_fonctions;
import com_interface.verification;
import com_nos_classes.Automate;
import com_nos_classes.Etat_Deterministe;
import com_nos_classes.Etat_transition;
import com_nos_classes.Stockage;
import com_nos_classes.Transition;
import java.util.Stack;

public class Traitement implements Nos_fonctions, verification{

    @Override
	public Automate evaluation(List<Stockage> ls){
		// TODO Auto-generated method stub
		//cette pile contiendra les symboles
		Stack<Automate> pile=new Stack<Automate>();
		Automate automate=new Automate();
		
		for(Stockage stock:ls) {
			//si on rencontre un symbole de alphabet
			if(stock.getArbitre()==0) {
				//on construit automate d'un symbole et on empile dans la pile
				pile.push(this.construction(stock.getValeur()));
			}
			//si on est en presence d'un operateur
			else {
				if(stock.getValeur()=='.') {
					Automate auto_symb1=pile.pop();
					Automate auto_symb2=pile.pop();
					//creation de automate par concatenation
					Automate auto_resultant=this.concatenation(auto_symb2, auto_symb1);
					pile.push(auto_resultant);
					
				}
				else if(stock.getValeur()=='|') {
					Automate auto_symb1=pile.pop();
					Automate auto_symb2=pile.pop();
					//creation de automate par concatenation
					Automate auto_resultant=this.union(auto_symb2, auto_symb1);
					pile.push(auto_resultant);
				}
				else {
					Automate auto_symb=pile.pop();
					//creation de automate par concatenation
					Automate auto_resultant=this.multiplicite(auto_symb);
					pile.push(auto_resultant);
				}
			}
		}
		return pile.peek();
	}

    @Override
	public int priorite(char op) {
		// TODO Auto-generated method stub
		if(op=='|')
			return 3;
		else if(op=='.')
			return 5;
		//dans le cas ou on a le *
		else
			return 6;
	}

    @Override
    public List<Stockage> inFixer(String value) {
        // TODO Auto-generated method stub
		List<Stockage> list_stockage=new ArrayList<Stockage>();
		Stockage stock;
		for(int i=0;i<value.length();i++) {
			if(isOperateur(value.charAt(i))) {
				//dans le cas ou on a un operateur
				stock=new Stockage(value.charAt(i), 1);
				list_stockage.add(stock);
				//ajout du point dans la chaine de caractere
				if(i!=value.length()-1&&(value.charAt(i)=='*'||value.charAt(i)==')')&&(!(isOperateur(value.charAt(i+1)))||value.charAt(i+1)=='(')) {
					stock=new Stockage('.', 1);
					list_stockage.add(stock);
				}
			}
			else {
				stock=new Stockage(value.charAt(i), 0);
				list_stockage.add(stock);
				if(i!=value.length()-1&&(!isOperateur(value.charAt(i+1))||value.charAt(i+1)=='(')) {
					stock=new Stockage('.', 1);
					list_stockage.add(stock);
				}	
			}
			
		}
		
		return list_stockage;
    }


	@Override
	public List<Stockage> postFixer(List<Stockage> ls) {
		// TODO Auto-generated method stub
		//pile pour sauvegarder la les operateurs
		Stack<Stockage> pile=new Stack<Stockage>();
		//liste de stockage
		List<Stockage> liste=new ArrayList<Stockage>();
		for(Stockage stock:ls) {
			if(stock.getArbitre()==0) {
				liste.add(stock);
			}
			else {
				//si la pile est vide
				if(pile.isEmpty()||stock.getValeur()=='('||(pile.peek().getValeur()=='(') &&stock.getValeur()!=')') {
					pile.push(stock);
				}
				
				else if(stock.getValeur()==')'){
//					do {
//						liste.add(pile.peek());
//					}while(pile.isEmpty()||pile.pop().getValeur()=='(');
					while(!pile.isEmpty()&&pile.peek().getValeur()!='(') {
						liste.add(pile.pop());
					}
					//on enleve la parentheses ouvrante
					pile.pop();
				}	
				else if(priorite(pile.peek().getValeur())>=priorite(stock.getValeur())) {
					do {
						liste.add(pile.peek());
					}while(pile.isEmpty()||(priorite(pile.pop().getValeur())<priorite(stock.getValeur())&&pile.peek().getValeur()=='('));
					pile.push(stock);
				}
				//
				else if(priorite(pile.peek().getValeur())<priorite(stock.getValeur())) {
					pile.push(stock);
				}
			}
		}
		//lorsquon na fini avec le parcour
		while(!(pile.empty())) {
			
			liste.add(pile.pop());
		}
		return liste;
	}

	@Override
	public Automate construction(char symbole) {
		// TODO Auto-generated method stub
		List<Transition>list_t=new ArrayList<Transition>();
		//creation de etat initial
		Etat_transition etat_debut=new Etat_transition(list_t ,false );
		List<Etat_transition> list_etats=new ArrayList<Etat_transition>();
		Etat_transition etat_fin=new Etat_transition(new ArrayList<Transition>(),true);
                etat_fin.setIsfinal(true);
		Transition t=new Transition(symbole,etat_fin);
		list_t.add(t);
		
		list_etats.add(etat_debut);
		list_etats.add(etat_fin);
		Automate a=new Automate(etat_debut, list_etats);
		return a;
	}

	@Override
	public Automate concatenation(Automate a1, Automate a2) {
		Automate a;
		Transition t=new Transition('£', a2.getEtat_initial());
		
		//on ajoute une une epsilonne transition a ancien etat d'acceoption vers etat initial de autre automate
		etat_final_ele(a1).getListe_de_transition().add(t);
		//on supprime l'ancien etat acceptation 
				etat_final_ele(a1).setIsfinal(false);
		//liste des etats finaux apres concatenation
		for(Etat_transition etat1:a2.getListe_etat()) {
			a1.getListe_etat().add(etat1);
		}
                
		a=new Automate(a1.getEtat_initial(), a1.getListe_etat());
		
		
		return a1;
	}

	@Override
	public Automate union(Automate a1, Automate a2) {
		// TODO Auto-generated method stub
		
		Transition t=new Transition('£', a1.getEtat_initial());
		Transition t1=new Transition('£', a2.getEtat_initial());
		//creation de etat finale de automate resultant
		Etat_transition etat_fin=new Etat_transition(new ArrayList<Transition>(), true);
		//creation des transitions vers etats finals crée
		Transition t2=new Transition('£',etat_fin);
		Transition t3=new Transition('£',etat_fin);
		//liste de transition pour etat initial
		List<Transition> list_trans_etat_init=new ArrayList<Transition>();
		//ajout des differentes transitions a la liste
		list_trans_etat_init.add(t);
		list_trans_etat_init.add(t1);
		//on associe la liste de transition a etat
		Etat_transition etat_init=new Etat_transition(list_trans_etat_init, false);
		//ajout des transtions aux anciens etats finaux
		etat_final_ele(a1).getListe_de_transition().add(t2);
		etat_final_ele(a2).getListe_de_transition().add(t3);
		
	//	mettre les etats finaux de a1 et a2 à false
		etat_final_ele(a2).setIsfinal(false);
		etat_final_ele(a1).setIsfinal(false);
		//nouvelle liste des etats du nouveau automate
		List<Etat_transition> listes_etats=new ArrayList<Etat_transition>();
		listes_etats=a1.getListe_etat();
		listes_etats.add(etat_init);
		listes_etats.add(etat_fin);
		//on parcourt et on ajoute les etats de autres automates
		for(Etat_transition etat:a2.getListe_etat()) {
			listes_etats.add(etat);
			
		}
		//creation de notre automate
		Automate a=new Automate(etat_init, listes_etats);
		return a;
	}

	@Override
	public Automate multiplicite(Automate a) {
		// TODO Auto-generated method stub
		//expi
		Transition t1=new Transition('£', a.getEtat_initial());
		
		List<Transition> list_transition=new ArrayList<Transition>();
		list_transition.add(t1);
		//on cree un nouvel etat de debut qui sera maintenant etat initial
		Etat_transition etat_debut=new Etat_transition(list_transition,false);
		//on cree un etat de fin qui sera maintenant etat final
		Etat_transition etat_fin=new Etat_transition(new ArrayList<Transition>(),true);
		//espsilonne transition vers etat de fin
		Transition t3=new Transition('£',etat_fin);
		//cette epsilonne transition part de l'ancien etat final à ancien etat initial
		etat_final_ele(a).getListe_de_transition().add(t1);
		//epsilonne transition de ancien etat final vers le nouveau
		etat_final_ele(a).getListe_de_transition().add(t3);
		//ancien etat final n'est plus un etat acception
		etat_final_ele(a).setIsfinal(false);
		etat_debut.getListe_de_transition().add(new Transition('£', etat_fin));
//		a la fin on ajoute les deux etats debut et fin à la liste des etats.
		a.getListe_etat().add(etat_debut);
		a.getListe_etat().add(etat_fin);
		//puis on contruit automate final
		
		Automate automate=new Automate(etat_debut,a.getListe_etat()); 
		return automate;
	}

	@Override
	public Automate ajout_transtion_aut(Automate a, Transition t) {
		// TODO Auto-generated method stub
	
		return null;
	}

	@Override
	public void afficher_automate(Automate a) {
		// TODO Auto-generated method stub
		for (Etat_transition etat : a.getListe_etat()) {
			if(!(etat.getListe_de_transition().isEmpty())) {
					for (Transition t : etat.getListe_de_transition()) {
						System.out.println(etat.getEtiquette_etat()+ " "+t.getSymbole()+" "+t.getDestination().getEtiquette_etat());
					}
			}
		}
		
	}

	@Override
	public Etat_transition etat_final_ele(Automate a) {
		// TODO Auto-generated method stub
		for(Etat_transition etats:a.getListe_etat()) {
			if(etats.isIsfinal()==true)
				return etats;
		}
		return null;
	}

	
        
        
        /*
                
        ***** ensemble de methodes implementé par ramses
        
        */
        
        
        	
   //retourne une liste de stockage     
    @Override
    public List<Stockage> listeValeur(String chaine) {
            List<Stockage> liste=new ArrayList<Stockage>();
            for(int i=0; i<chaine.length(); i++){
                if(isOperateur(chaine.charAt(i)))
                    liste.add(new Stockage(chaine.charAt(i), 0));
                else
                    liste.add(new Stockage(chaine.charAt(i), 1));
            }
            return liste;
        
    }
    
    
    
    /**** veification de la bonne forme de la chaine definissant l'expression reguliere  ***/
        @Override
	public boolean verification(String chaine) {
		
            int ouvert=0;
            if(!inCorectChar(chaine))
                return false;
                        
            for(int i=0; i<chaine.length(); i++){
                if(i==0 && (chaine.charAt(i)=='|' || chaine.charAt(i)=='*'))
                    return false;
            
                if(i==chaine.length()-1 && chaine.charAt(i)=='|')
                    return false;
            
                if(chaine.charAt(i)=='('){
                    ouvert++;
                }
                if(chaine.charAt(i)==')'){
                    if(ouvert<=0)
                        return false;
                    else
                        ouvert--;
                }
            
                if(chaine.charAt(i)=='|' && (chaine.charAt(i+1)=='|' || chaine.charAt(i+1)=='*')){
                    return false;
                }
            
                if(chaine.charAt(i)=='*' && i!=(chaine.length()-1) && chaine.charAt(i+1)=='*'){
                    return false;
                }
             
            }
            if(ouvert==0)
                return true;
            
            return false;
        }
        
        //verification si tous caractere appartient a l'alphabet
        @Override
        public boolean isInto(char mot){
            for(int i=0; i<this.getAlphabet().size(); i++)
                if(this.getAlphabet().get(i).equals(mot))
                    return true;
            return false;
        }
        
        
        
        //verification si un caractere est un operateur ou pas
        @Override
	public boolean isOperateur(char valeur) {
		// TODO Auto-generated method stub
                if(valeur=='*'||valeur=='|'||valeur=='.'|| valeur=='('||valeur==')')
			return true;
		return false;
	}
        
        
        //verifocation si lespression est definit sur l'aplphabet
        @Override
        public boolean inCorectChar(String chaine) {
            //To change body of generated methods, choose Tools | Templates.
            int etat=0;
            for(int i=0; i<chaine.length(); i++){
                if(!isInto(chaine.charAt(i)) && !isOperateur(chaine.charAt(i)))
                   etat=1;
            }
            if(etat==0)
                return true;
            
            return false;
            
        }
        
        
        public List getAlphabet() {
            return alphabet;
        }

    
        public void setAlphabet(List alphabet) {
        this.alphabet = alphabet;
        }
        
        
        
        // transforme une liste en une liste definissant l'aplhabet de notre langage
        @Override
        public List salhpabet(String alphabet){
            for(int i=0; i<alphabet.length(); i++){
                if(!isInto(alphabet.charAt(i)))
                    this.getAlphabet().add(alphabet.charAt(i));
            }
            return this.getAlphabet();
        }
        


    /******** definiton de la finction d'evamuation su un automate  ********/    
        // ajout de la notification l'etat final dans la fonction de construction
    @Override
    public boolean evaluation(Automate a, String mot1) {
        String mot=mot1+".";
        Etat_transition etat=a.getEtat_initial();
        List<Etat_transition> l=new ArrayList<Etat_transition>();
        for(Etat_transition e: a.getListe_etat())
            if(e.isIsfinal())
                l.add(e);
        
        char c=mot.charAt(0);
        int i=1;
        while(c!='.' && i<mot.length()){
            etat=transition(etat, c);
            if(etat!=null){
               c=mot.charAt(i);
               i++;
            }
            else
                return false;
            
        }       
        if(c=='.' && isInto(l, etat))
           return true; 
        
        return false;
    }

    
    /****** definition de la fontion de transition pour un etat et une valeur ******/
    @Override
    public Etat_transition transition(Etat_transition etat, char car) {
        List<Transition> liste=etat.getListe_de_transition();
        for(Transition t: liste){
            if(t.getSymbole()==car){
                return t.getDestination();
            }
        }
       
        return null;
    }
    
    
    @Override
    public List<Transition> transitionList(Etat_transition etat, char car){
        List<Transition> liste=etat.getListe_de_transition();
         List<Transition> l=new ArrayList<Transition>();
        
        for(Transition t: etat.getListe_de_transition()){
            if(t.getSymbole()==car)
                l.add(t);
        }
       
        return l;
    }
    
    // definition de la fonction e-fermeture
     @Override
    public List<Etat_transition> e_Fermeture(List<Etat_transition> etat) {
        List<Etat_transition> liste=new ArrayList<Etat_transition>();
        Stack<Etat_transition> pile=new Stack<Etat_transition>();
        
        for(Etat_transition et: etat){
            liste.add(et);
            pile.add(et);
        }
        
        while(!pile.isEmpty()){
            Etat_transition e=pile.pop();
            if(transition(e, '£')!=null){
                if(!isInto(liste, transition(e, '£'))){
                    for(Transition t1: transitionList(e, '£') ){
                    pile.add(t1.getDestination());
                    liste.add(t1.getDestination());
                    }
                }
            }
        }
        
        return liste;
    }

    
    //fonction de determinisation
    @Override
    public Automate AFD(Automate afn, String val) {
        etiquete=0;
        salhpabet(val);
        Etat_transition ini=afn.getEtat_initial();
        List<Etat_Deterministe> detat=new ArrayList<Etat_Deterministe>();
        
        List<Etat_transition> dtrans=new ArrayList<Etat_transition>();
        
        List<Etat_transition> d=new ArrayList<Etat_transition>();
        d.add(ini);
        Etat_transition etat=new Etat_transition(null, false);
        etat.setEtiquette_etat(etiquete);
                
        etiquete++;
        
        detat.add(new Etat_Deterministe(etat,e_Fermeture(d),false));
        
       while(etatMarquer(detat)!=null){
            Etat_Deterministe d1=etatMarquer(detat);
            d1.setMarquage(true);
            
            List<Transition> liste=new ArrayList<Transition>();
            for(int i=0; i<this.getAlphabet().size(); i++){
            List<Etat_transition> u=e_Fermeture(transiter(d1.getListe_Etat(),(char) this.getAlphabet().get(i)));
            if(u.size()!=0){
                Etat_transition a=new Etat_transition(null, false);
                a.setEtiquette_etat(etiquete);
                if(isInto(detat, u)!=null) {
                    a=isInto(detat, u).getEtat();
                    //ajout d'une transition epsiline dans le cas ou on bouvle
//                    Transition t=new Transition('£', a);
//                    liste.add(t);
                }
                if(isInto(detat, u)==null){
                    detat.add(new Etat_Deterministe(a, u, false));
                    etiquete++;
                }
                Transition t=new Transition((char) this.getAlphabet().get(i), a);
                liste.add(t);
            }
            }
            d1.getEtat().setListe_de_transition(liste);
            d1.Finalization();
            dtrans.add(d1.getEtat());
        }
        return (new Automate(etat, dtrans));
    }

    
    
    @Override
    public boolean isInto(List<Etat_transition> pile, Etat_transition etat) {
        
        for(Etat_transition afn : pile)
            if(afn.getEtiquette_etat()==etat.getEtiquette_etat())
                return true;
        
        return false;
    }
    
    
    
    @Override
    public List<Etat_transition> transiter(List<Etat_transition> et, char symbole){
        
        List<Etat_transition> liste=new ArrayList<Etat_transition>();
        
        for(Etat_transition etat : et){
            char sy=symbole;
            while(transition(etat,sy)!= null){
                liste.add(transition(etat,sy));
                if(!isInto(et, etat)){
                    et.add(transition(etat,sy));
                }else{
                    sy='§';
                }
            }
        }
        return liste;
    }

   
    @Override
    public Etat_Deterministe isInto(List<Etat_Deterministe> etat, List<Etat_transition> trans) {
        boolean val=true;
        for(Etat_Deterministe e: etat)
            if(e.compare(trans))
               return e; 
        
        return null;
    }
    
    
    
    
    @Override
    public Etat_Deterministe etatMarquer(List<Etat_Deterministe> etat) {
       for(Etat_Deterministe d: etat)
           if(!d.getMarquage()){
               return d;
           }
               
       return null;
    }
    
    
    
    @Override
    public String supChar(String m) {
        String mot1="";
        List mot=this.salhpabet(m);
        for(int i=0; i<mot.size(); i++){
            mot1=mot1+mot.get(i).toString();
        }
        
        
        return mot1.trim();
    }
            
            
    
    
    private static int etiquete=0;
    private List alphabet=new ArrayList();

    

    

    
}
