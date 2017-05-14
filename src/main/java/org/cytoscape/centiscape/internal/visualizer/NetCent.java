/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cytoscape.centiscape.internal.visualizer;

import org.cytoscape.model.CyNetwork;

/**
 *
 * @author Andrea Pegoraro
 */
public class NetCent {
    
    CyNetwork network;
    Double cent;
    
    public NetCent(CyNetwork network, Double cent){
        this.network=network;
        this.cent=cent;   
    }
    
    public CyNetwork getNet(){
        return network;
    }
    
    public Double getCent(){
        return cent;
    }
    
}
