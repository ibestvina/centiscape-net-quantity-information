/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cytoscape.centiscape.internal;

import java.util.ListIterator;
import java.util.Vector;
import org.cytoscape.centiscape.internal.visualizer.CentMaxVal;
import org.cytoscape.centiscape.internal.visualizer.Centrality;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/**
 * Class that calculates normalized centralities
 * 
 * @author Andrea Pegoraro
 */



public class CentiScaPeNormCent {
    
    private Vector centralities;
    private CyNetwork network;
    private double SumNorm;
    private double MaxNorm;
            
    public CentiScaPeNormCent(Vector centralities, CyNetwork network){
        this.centralities = centralities; 
        this.network = network;
    }
    
    public void calculateNormalizedCentralities(){
        for (ListIterator it=centralities.listIterator();it.hasNext();){
            Centrality cent = (Centrality) it.next();
            network.getDefaultNodeTable().createColumn(cent.getName()+ " Max Norm", Double.class, false);
            network.getDefaultNodeTable().createColumn(cent.getName()+ " %", Double.class, false);
            for (ListIterator itNode=network.getNodeList().listIterator();itNode.hasNext();){
                CyNode node = (CyNode) itNode.next();
                CentMaxVal maxval = new CentMaxVal(network, cent);
                //for the Centroid value the min value is added to calculation to compensate for the negative value of the centrality
                if (cent.getName().equals("Centroid unDir")||cent.getName().equals("Centroid Dir")){
                    //normalization to max centrality value
                    MaxNorm = (((network.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class)- maxval.getMinCentVal())/(maxval.getMaxCentVal()- maxval.getMinCentVal())));
                    //normalization to sum of all centrality values
                    SumNorm = (((network.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class)- maxval.getMinCentVal())/(maxval.getCentroidSumVal()))*100); 
                }
                else{
                    //normalization to max centrality value
                    MaxNorm = (((network.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class))/maxval.getMaxCentVal()));
                    //normalization to sum of all centrality values
                    SumNorm = (((network.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class))/maxval.getSumCentVal())*100);
                }
                MaxNorm = Math.round(MaxNorm*10000000);
                MaxNorm = MaxNorm/10000000;
                SumNorm = Math.round(SumNorm*10000000);
                SumNorm = SumNorm/10000000;
                network.getDefaultNodeTable().getRow(node.getSUID()).set(cent.getName()+ " Max Norm", MaxNorm);
                network.getDefaultNodeTable().getRow(node.getSUID()).set(cent.getName()+ " %", SumNorm);            
            }
        }
    }
    
}
