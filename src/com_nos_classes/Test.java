package com_nos_classes;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import com.jtattoo.plaf.smart.SmartLookAndFeel;
import com.jtattoo.plaf.texture.TextureLookAndFeel;
import com_implementation.Traitement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Test {
    
    //renomation des etats transtitions
    
    

	public static void main(String[] args) {
           // HiFiLookAndFeel hi=new HiFiLookAndFeel();
           TextureLookAndFeel hi=new TextureLookAndFeel();
            SmartLookAndFeel sm=new SmartLookAndFeel();
            try {
                UIManager.setLookAndFeel(hi);
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            new fenetre().setVisible(true);
	}
}
