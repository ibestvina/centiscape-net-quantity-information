/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal;

import java.util.Vector;
import javax.swing.JPanel;
import org.cytoscape.model.CyNetwork;

/**
 *
 * @author faizi
 */
public class CentiScaPeDirectedThreadEngine extends Thread {
    public CyNetwork network;
    public boolean[] checkedCentralities;
    public String[] directedCentralities;
    public JPanel menu;
    public CentiScaPeDirectedAlgorithm algo;
    public CentiScaPeCore centiscapecore;
    private Vector centralities;
    
    public CentiScaPeDirectedThreadEngine(CyNetwork network, boolean[] checkedCentralities, String[] directedCentralities, JPanel c, CentiScaPeCore core){
        this.network=network;
        this.checkedCentralities=checkedCentralities;
        this.directedCentralities=directedCentralities;
        this.menu = c;
        this.centiscapecore = core;
    }
    
    public void run(){
        algo = new CentiScaPeDirectedAlgorithm();
        centralities = algo.executeCentralities(network, checkedCentralities, directedCentralities, menu,centiscapecore);
        
    }
    
    public void stopAlgo(){
        algo.stopAlgo();
    }
    
    public Vector getCentralities(){       
        return centralities;
    }
}
