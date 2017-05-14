
package org.cytoscape.centiscape.internal;
/*
 * CentiScaPeThreadEngine.java
 *
 * Created on 20 marzo 2007, 14.47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author scardoni
 */

import java.util.Vector;
import javax.swing.*;
import org.cytoscape.model.CyNetwork;



public class CentiScaPeThreadEngine extends Thread {
    
    private CentiScaPeAlgorithm CentiScaPealg;
    public CyNetwork currentnetwork;
    private JPanel c;
    private Vector centralities;
    /**
     * Creates a new instance of CentiScaPeThreadEngine
     */
    public CentiScaPeThreadEngine(CentiScaPeAlgorithm centiscapealgorithm, CyNetwork currentnetwork) {
     this.currentnetwork = currentnetwork;
      this.CentiScaPealg= centiscapealgorithm;
    
    }
    
 
    
    public void run(){      
        centralities = CentiScaPealg.ExecuteCentiScaPeAlgorithm(currentnetwork, c);
        c.setEnabled(true);
    }
    
    public void setCaller(JPanel caller){
        c=caller;
    }

    public void endprogram() {
        CentiScaPealg.endalgorithm();
    }
    
    public Vector getCentralities(){       
        return centralities;
    }
    
}

    

