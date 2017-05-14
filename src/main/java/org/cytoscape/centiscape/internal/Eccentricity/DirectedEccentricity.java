/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal.Eccentricity;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.cytoscape.centiscape.internal.CentiScaPeMultiSPath;
import org.cytoscape.centiscape.internal.CentiScaPeShortestPathList;
import org.cytoscape.centiscape.internal.visualizer.ImplCentrality;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;

/**
 *
 * @author faizi
 */
public class DirectedEccentricity {
    public static double execute(Vector CentiScaPeSingleShortestPathVector){
        Double eccOfCurrentNode = new Double(0);
        int numSingleShortestPaths = CentiScaPeSingleShortestPathVector.size();
        CentiScaPeShortestPathList CurrentList;
        
        if (numSingleShortestPaths==0){
            return eccOfCurrentNode;
        }
        for (int j = 0; j < numSingleShortestPaths; j++) {
		CurrentList = (CentiScaPeShortestPathList) CentiScaPeSingleShortestPathVector.get(j);
                int length = ((CentiScaPeMultiSPath)CurrentList.getLast()).getCost();
		if(length > eccOfCurrentNode)
                {
                    eccOfCurrentNode=(double)(length);
                    
                }
	}
        
        return (1/(eccOfCurrentNode));
    }
    
    
}
