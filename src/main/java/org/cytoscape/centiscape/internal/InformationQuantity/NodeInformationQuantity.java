/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal.InformationQuantity;

import org.cytoscape.model.CyNode;

/**
 *
 * @author Ivan
 */
public class NodeInformationQuantity {
    private CyNode node;
    private Double value;

    public NodeInformationQuantity(CyNode node, Double value) {
        this.node = node;
        this.value = value;
    }

    public void setNode(CyNode node) {
        this.node = node;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public CyNode getNode() {
        return node;
    }

    public Double getValue() {
        return value;
    }
    
}


