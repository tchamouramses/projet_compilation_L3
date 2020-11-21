package com.Graphviz.Dessin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com_implementation.Traitement;
import com_nos_classes.Automate;
import com_nos_classes.Etat_transition;
import com_nos_classes.Transition;


public class Dessin_automate {
        public static int taille=0;
	private Automate automate;
	Traitement traitement=new Traitement();
	public String mot;
	//le constructeur devra prendre l'automate à dessiner en parametre
	private Graphviz graph;
	public Dessin_automate(Automate automate) {
		super();
		this.automate = automate;
	}
	public Dessin_automate(Automate automate,String mot) {
		super();
		this.automate = automate;
		this.mot=mot;
	}

	public Automate getAutomate() {
		return automate;
	}

	
	
	
	public void setAutomate(Automate automate) {
		this.automate = automate;
	}
	
	
	public Graphviz getGraph() {
		return graph;
	}

	public void setGraph(Graphviz graph) {
		this.graph = graph;
	}

		//construction du graphisme a partir de automate
		public void const_Graphviz() {
			//Graphviz gv=new Graphviz();
			this.graph=new Graphviz();
			this.graph.addln(this.graph.start_graph());
			this.graph.add(automate.getEtat_initial().getEtiquette_etat()+"[color="+"\"red\""+"]");
			for(Etat_transition etat:this.automate.getListe_etat()) {
				//si c'est un etat acceptation
				if(etat.isIsfinal())
					this.graph.add(etat.getEtiquette_etat()+"[color="+"\"green\""+"style="+"\"dashed\""+"]");

				for(Transition t:etat.getListe_de_transition()) {
					String passage=etat.getEtiquette_etat()+" -> "+t.getDestination().getEtiquette_etat()+" [fillcolor="+"\"gray\""+" label="+"\""+" "+t.getSymbole()+"\""+"]"+";";
					this.graph.addln(passage);
				}
				
			}
			
			this.graph.add(this.graph.end_graph());
			
			
		}
		
		//surcharge de la methode const_graph
		//ca pourra dervir pour evaluation du mot
		public boolean const_Graphviz(int eva) {
			//initialisation du graph
			
			this.graph=new Graphviz();
			this.graph.addln(this.graph.start_graph());
			this.graph.add(automate.getEtat_initial().getEtiquette_etat()+"[color="+"\"red\""+"]");
			for(Etat_transition etat:this.automate.getListe_etat()) {
				//si c'est un etat acceptation
				if(etat.isIsfinal())
					this.graph.add(etat.getEtiquette_etat()+"[color="+"\"green\""+"style="+"\"dashed\""+"]");

				for(Transition t:etat.getListe_de_transition()) {
					String passage=etat.getEtiquette_etat()+" -> "+t.getDestination().getEtiquette_etat()+" [fillcolor="+"\"gray\""+" label="+"\""+" "+t.getSymbole()+"\""+"]"+";";
					this.graph.addln(passage);
				}
				
			}
			
			//marquage du passage
			Etat_transition etat1;
			String mot=this.mot+".";
		        Etat_transition etat=this.automate.getEtat_initial();
		        List<Etat_transition> l=new ArrayList<Etat_transition>();
		        //remplissage de la liste des etats finaux
		        for(Etat_transition e: this.automate.getListe_etat())
		            if(e.isIsfinal())
		                l.add(e);
		        
		        char c=mot.charAt(0);
		        int i=1;
		        while(c!='.' && i<mot.length()){
		        	etat1=etat;
		            etat=traitement.transition(etat, c);
		            //marquage du parcours
		            this.graph.add(etat1.getEtiquette_etat()+"->"+etat.getEtiquette_etat()+"[color="+"\"sienna\""+"style="+"\"dashed\""+"taillabel="+"\"  "+i+"\""+"labelfontcolor="+"\"blue\""+"];");
		            if(etat!=null){
		               c=mot.charAt(i);
		               i++;
		            }
		            else
		                return false;
		            
		        } 
		        this.graph.add(this.graph.end_graph());
		        if(c=='.' && traitement.isInto(l, etat))
		           return true; 
		        
		        return false;
			
		}
	/**
	 * Construct a DOT graph in memory, convert it
	 * to image and store the image in the file system.
	 */
	public void start(int i)
	{
		
//		Graphviz gv = new Graphviz();
//		gv.addln(gv.start_graph());
//		gv.addln("A -> B;");
//		gv.addln("A -> C;");
//		gv.addln(gv.end_graph());
		const_Graphviz();
		
		System.out.println(this.graph.getDotSource());

		this.graph.increaseDpi();   // 106 dpi

		String type = "png";
		//      String type = "dot";
		//      String type = "fig";    // open with xfig
		//      String type = "pdf";
		//      String type = "ps";
		//      String type = "svg";    // open with inkscape
		//      String type = "png";
		//      String type = "plain";
		
		String repesentationType= "dot";
		//		String repesentationType= "neato";
		//		String repesentationType= "fdp";
		//		String repesentationType= "sfdp";
		// 		String repesentationType= "twopi";
		// 		String repesentationType= "circo";
		
//		File out = new File("/tmp/out"+this.graph.getImageDpi()+"."+ type);   // Linux
                File out;
                if(i==0)
		    out = new File("imageafn/out"+taille+"." + type);    // Windows
                else if(i==1)
                    out = new File("imageafd/out1"+taille+"." + type);
                else {
                	const_Graphviz(0);
                	out=new File("imagerecon/outeva"+taille+"."+type);
                }
                
		this.graph.writeGraphToFile( this.graph.getGraph(this.graph.getDotSource(), type, repesentationType), out );
	}
	
	/**
	 * Read the DOT source from a file,
	 * convert to image and store the image in the file system.
	 */
	public  void start2()
	{
//		String dir = "/home/tchatseu/Téléchargements/graphviz-java-api-master";     // Linux
//		String input = dir+"/sample/simple.dot";
			   String input = "c:/netbeans.ws/graphviz-java-api/sample/simple.dot";    // Windows

		const_Graphviz();
		this.graph.readSource(input);
		System.out.println(this.graph.getDotSource());

		String type = "pdf";
		//    String type = "dot";
		//    String type = "fig";    // open with xfig
		//    String type = "pdf";
		//    String type = "ps";
		//    String type = "svg";    // open with inkscape
		//    String type = "png";
		//      String type = "plain";
		
		
		String repesentationType= "dot";
		//		String repesentationType= "neato";
		//		String repesentationType= "fdp";
		//		String repesentationType= "sfdp";
		// 		String repesentationType= "twopi";
		//		String repesentationType= "circo";
		
//		File out = new File("/tmp/simple." + type);   // Linux
			   File out = new File("c:/netbeans.ws/graphviz-java-api/tmp/simple." + type);   // Windows
		this.graph.writeGraphToFile(this.graph.getGraph(this.graph.getDotSource(), type, repesentationType), out );
	}
}
