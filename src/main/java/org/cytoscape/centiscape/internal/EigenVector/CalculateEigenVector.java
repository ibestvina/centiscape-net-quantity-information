/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal.EigenVector;


import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import java.util.List;
import java.util.Vector;
import org.cytoscape.centiscape.internal.visualizer.ImplCentrality;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;

/**
 *
 * @author faizi
 */
public class CalculateEigenVector {
    
    public static void executeAndWriteValues(double[][] adjacencyMatrixOfNetwork,CyNetwork network, List<CyNode> nodeList, CyTable nodeTable, String centralityName, Vector VectorResults, String directionType){
        
        int numberOfNodes = nodeList.size();
        //double[][] adjacencyMatrixOfNetwork = new double[numberOfNodes][numberOfNodes];
        
        
        Matrix A = new Matrix(adjacencyMatrixOfNetwork);

        EigenvalueDecomposition e = A.eig();
	Matrix V = e.getV();
	
        double[][] EigenVectors = V.getArray();
        
        
        // = network.getDefaultNodeTable();
        nodeTable.createColumn(centralityName + directionType, Double.class, false);

            
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;
            for (int j=0 ; j<numberOfNodes ; j++) {
                currentvalue = EigenVectors[j][numberOfNodes-1];
                

                if (currentvalue < min) {
                    min = currentvalue;
                }
                if (currentvalue > max) {
                    max = currentvalue;
                }
                totalsum = totalsum + currentvalue;
                CyRow row = nodeTable.getRow(nodeList.get(j).getSUID());
                row.set(centralityName + directionType , new Double(currentvalue));
            }
            
            CyTable networkTable = network.getDefaultNetworkTable();
                int totalnodecount = network.getNodeCount();
            networkTable.createColumn(centralityName+"Max value " + directionType, Double.class, false);
            networkTable.createColumn(centralityName+"min value " + directionType, Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn(centralityName+"mean value " + directionType, Double.class, false);
            network.getRow(network).set(centralityName+"Max value " + directionType, new Double(max));
            network.getRow(network).set(centralityName+"min value " + directionType, new Double(min));
            network.getRow(network).set(centralityName+"mean value " + directionType, new Double(mean));

            // for embending EigenVector Centrality in Results Panel -- These two lines are enough
            ImplCentrality EigenVectorCentrality = new ImplCentrality(centralityName + directionType, true, mean, min, max);
            VectorResults.add(EigenVectorCentrality);
    }
}
