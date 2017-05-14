/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal.Closeness;

import java.util.Vector;
import org.cytoscape.centiscape.internal.CentiScaPeMultiSPath;
import org.cytoscape.centiscape.internal.CentiScaPeShortestPathList;

/**
 *
 * @author faizi
 */
public class DirectedCloseness {
    
    public static double execute(Vector CentiScaPeSingleShortestPathVector){
        double closenessOfCurrentNode = 0;
        int numSingleShortestPaths = CentiScaPeSingleShortestPathVector.size();
        CentiScaPeShortestPathList CurrentList;
        int distance =0;
        for (int j = 0; j < numSingleShortestPaths; j++) {
		CurrentList = (CentiScaPeShortestPathList) CentiScaPeSingleShortestPathVector.get(j);
                distance = ((CentiScaPeMultiSPath)CurrentList.getLast()).getCost();
		closenessOfCurrentNode = closenessOfCurrentNode + (double) (1 / ((double) distance)) ;
	}
        
        return closenessOfCurrentNode;
    }
    
    
}
