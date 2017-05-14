/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal.Radiality;

import java.util.Vector;
import org.cytoscape.centiscape.internal.CentiScaPeMultiSPath;
import org.cytoscape.centiscape.internal.CentiScaPeShortestPathList;

/**
 *
 * @author faizi
 */
public class DirectedRadiality {
    public static double execute(double Diameter,int totalNodeCount, Vector CentiScaPeSingleShortestPathVector){
        double radiality =0.0;
        radiality = Diameter*(totalNodeCount-1);
        CentiScaPeShortestPathList CurrentList;
        int numSingleShortestPaths = CentiScaPeSingleShortestPathVector.size();
        int distance;
        for (int j = 0; j < numSingleShortestPaths; j++) {
		CurrentList = (CentiScaPeShortestPathList) CentiScaPeSingleShortestPathVector.get(j);
                distance = ((CentiScaPeMultiSPath)CurrentList.getLast()).getCost();
                radiality = radiality - (1/(double)distance);
		
	}
        if(radiality==0.0){
            return radiality;
        }
        return (1/radiality);
    }
    
}

