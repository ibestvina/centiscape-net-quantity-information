/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal.InformationQuantity;

import com.sun.org.apache.bcel.internal.generic.IFEQ;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/**
 *
 * @author Ivan
 */
public class NodeInformationQuantity {
    private static double epsilon = 0.00001;
    
    private CyNode node;
    private CyNetwork network;
    private boolean isDirected;
    private double nodeIQ;
    private double edgeIQ;
    private double totalIQ;
    
    private List<CyNode> inNeighbours;
    private List<CyNode> outNeighbours;
    private List<CyNode> neighbours;
    private int indeg;
    private int outdeg;
    private int deg;
    private double n;
    


    public NodeInformationQuantity(CyNode node, CyNetwork network, boolean isDirected) {
        this.node = node;
        this.network = network;
        this.isDirected = isDirected;
    }

    public void setNode(CyNode node) {
        this.node = node;
    }

    public CyNode getNode() {
        return node;
    }
    
    
    public double getNodeIQ() {
        return nodeIQ;
    }

    public double getEdgeIQ() {
        return edgeIQ;
    }

    public double getTotalIQ() {
        return (nodeIQ + edgeIQ) / 2;
    }
    
    
    public void execute(){
        if(isDirected){
            inNeighbours = network.getNeighborList(node,CyEdge.Type.INCOMING);
            indeg = inNeighbours.size();
            outNeighbours = network.getNeighborList(node,CyEdge.Type.OUTGOING);
            outdeg = outNeighbours.size();
        }
        else{
            neighbours = network.getNeighborList(node,CyEdge.Type.ANY);
            deg = neighbours.size();
        }

        n = network.getNodeCount();
        nodeIQ = calculateNodeIQ();
        edgeIQ = calculateEdgeIQ();
    }
    
    double log_10_2 = Math.log10(2.);
    
    private double log2(double x){
        return Math.log10(x) / log_10_2;
    }
    
    private double IQ(double p){
        if(p < epsilon || 1 - p < epsilon){
            return 0.0;
        }
        return p * log2(1/p) + (1-p) * log2(1/(1-p));
    }

            
    private double calculateNodeIQ(){
        if(isDirected){
            return (IQ(indeg / n) + IQ(outdeg / n)) / 2;
        }
        else{
            return IQ(deg / n);
        }
    }
    
    
    private double unionSize(List<CyNode> l1, List<CyNode> l2){
        Set<CyNode> set = new HashSet<CyNode>();

        set.addAll(l1);
        set.addAll(l2);
        
        return set.size();
    }
    
    
    private double calculateEdgeIQ(){
        if(isDirected){
            double outNbUnionSize;
            double outNbDiffSize;
            double inNbUnionSize;
            double inNbDiffSize;
            
            double totalEdgeIQ = 0;
            
            for(CyNode other : network.getNodeList()){
                List<CyNode> otherOutNeighbours = network.getNeighborList(other,CyEdge.Type.OUTGOING);
                outNbUnionSize = unionSize(outNeighbours, otherOutNeighbours);
                outNbDiffSize = 2 * outNbUnionSize - (outdeg + otherOutNeighbours.size());
                
                List<CyNode> otherInNeighbours = network.getNeighborList(other,CyEdge.Type.INCOMING);
                inNbUnionSize = unionSize(inNeighbours, otherInNeighbours);
                inNbDiffSize = 2 * inNbUnionSize - (indeg + otherInNeighbours.size());
                
                totalEdgeIQ += (IQ(outNbDiffSize / n) + IQ(inNbDiffSize / n)) / 2;
            }
            return totalEdgeIQ / n;
        }
        else{
            double nbUnionSize;
            double nbDiffSize;
            
            double totalEdgeIQ = 0;
            
            for(CyNode other : network.getNodeList()){
                List<CyNode> otherNeighbours = network.getNeighborList(node,CyEdge.Type.ANY);
                nbUnionSize = unionSize(neighbours, otherNeighbours);
                nbDiffSize = 2 * nbUnionSize - (deg + otherNeighbours.size());
                
                totalEdgeIQ += IQ(nbDiffSize / n);
            }
            return totalEdgeIQ / n;
        }
    }
    
}


