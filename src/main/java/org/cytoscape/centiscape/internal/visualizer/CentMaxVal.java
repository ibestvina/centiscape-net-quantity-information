/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cytoscape.centiscape.internal.visualizer;

import java.util.List;
import java.util.ListIterator;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/**
 *  Class that gets the max and sum values of a centrality
 * 
 * @author Andrea Pegoraro
 */
public class CentMaxVal {
    
    private Centrality cent;
    
    private CyNetwork network;
    
    private String centralityname;
    
    private String direction;
    
    public CentMaxVal (CyNetwork network, Centrality cent){
        
        this.network = network;
        this.cent = cent; 
        
        
        if ((cent.getName().equals("OutDegree"))||(cent.getName().equals("InDegree" ))){
            centralityname = cent.getName();
            direction = "Dir";
        }
        else{
            centralityname = cent.getName().split(" ")[0];
            direction = cent.getName().split(" ")[1];
        }
    }
    
    public double getMaxCentVal (){
    
        double value = network.getDefaultNetworkTable().getRow(network.getSUID()).get(centralityname+" max value "+direction, Double.class);
    
        return value;
    }
    
    public double getSumCentVal (){
       
       double value = 0;
       List nodelist = network.getNodeList();
       
       for (ListIterator it = nodelist.listIterator(); it.hasNext();) {
           CyNode node = (CyNode)it.next();
           value = value + network.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class);       
       }
       
       return value;
    }
    
    public double getMinCentVal (){
    
        double value = network.getDefaultNetworkTable().getRow(network.getSUID()).get(centralityname+" min value "+direction, Double.class);
    
        return value;
    }
    
    public double getCentroidSumVal () {
       
        double value = 0;
        List nodelist = network.getNodeList();
       
        for (ListIterator it = nodelist.listIterator(); it.hasNext();) {
            CyNode node = (CyNode)it.next();
            value = value + (network.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class)-this.getMinCentVal());       
        }
       
        return value;
    }
    
}
