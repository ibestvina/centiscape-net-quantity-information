/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal.Betweenness;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.cytoscape.centiscape.internal.CentiScaPeMultiSPath;
import org.cytoscape.centiscape.internal.CentiScaPeShortestPathList;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;

/**
 *
 * @author faizi
 */

/** Edge Betweenness Definition: 
 * The edge betweenness centrality is defined as the number of the shortest 
 * paths that go through an edge in a graph or network (Girvan and Newman 2002)
 */
public class EdgeBetweenness {
    CyNetwork network ;
    Map<CyEdge,Double> edgeBetweennessValues ;
    
    // initialize 
    public EdgeBetweenness(CyNetwork network){
        this.network = network;
        edgeBetweennessValues = new HashMap<CyEdge,Double>(network.getNodeCount());
        
        //initialize map
        List<CyEdge> listOfEdgesInNetwork = network.getEdgeList();
        for(CyEdge edge: listOfEdgesInNetwork){
            edgeBetweennessValues.put(edge,0.0);
        }
    }
    
    public void updateEdgeBetweenness(Vector CentiScaPeMultiShortestPathVector){
        int numberOfPaths = CentiScaPeMultiShortestPathVector.size();
        
        for (int j = 0; j < numberOfPaths; j++) {
            CentiScaPeShortestPathList currentList;
            currentList = (CentiScaPeShortestPathList) CentiScaPeMultiShortestPathVector.get(j);
            
            Long lastNodeSUID = ((CentiScaPeMultiSPath) currentList.getLast()).getNode().getSUID();
            Double factor = 1.0;
            for(int k =0;k<numberOfPaths; k++) {
                CentiScaPeShortestPathList rolllist;
                rolllist = (CentiScaPeShortestPathList) CentiScaPeMultiShortestPathVector.get(j);
                if(rolllist != currentList){
                    if(((CentiScaPeMultiSPath) rolllist.getLast()).getNode().getSUID() == lastNodeSUID){
                        factor++;
                    }
                }
            }
            
            int lenghtOfCurrentList = currentList.size()-1;
            for(int i=0; i< lenghtOfCurrentList ; i++){
                //((CentiScaPeMultiSPath)currentList.get(i)).getNode().;
                List<CyEdge> edges = network.getConnectingEdgeList(((CentiScaPeMultiSPath)currentList.get(i)).getNode(), ((CentiScaPeMultiSPath)currentList.get(i+1)).getNode(), CyEdge.Type.ANY);
                //System.out.println(edges.size());
                edgeBetweennessValues.put(edges.get(0), edgeBetweennessValues.get(edges.get(0))+(1.0/factor));
            }
        }
        
    }
    
    public Map<CyEdge,Double> getEdgeBetweennessMap(){
        
        return edgeBetweennessValues;
    }
    
    public void displayEdgeBetweennessValues(){
        
        //edge and its betweenness
        System.out.println(edgeBetweennessValues);
        
    }
    
    
    
}
