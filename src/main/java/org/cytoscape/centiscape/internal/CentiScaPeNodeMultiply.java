/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cytoscape.centiscape.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.centiscape.internal.visualizer.Centrality;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;

/**
 * Class that multiplies nodes in a network by an attribute value and calculates
 * the new centrality values
 * 
 * @author Andrea Pegoraro
 */
public class CentiScaPeNodeMultiply {
    
    private CyNetwork network,networkchild;
    private String attribute, direction;
    private CyNetworkManager networkmanager;
    private CyNetworkViewManager networkviewmanager;
    private CyNetworkViewFactory networkviewfactory;
    private CentiScaPeCore centiscapecore;
    private CyNetworkView networkchildview;
    private CySwingApplication cyDesktopService;
    private ArrayList<CyNode> nodesremove = new ArrayList<CyNode>();
    private HashMap <CyNode, CyNode> nodemap = new HashMap<CyNode, CyNode>();
    private HashMap <CyNode, ArrayList> childmap = new HashMap<CyNode, ArrayList>();
    private int warning = 0;
    
    public CentiScaPeNodeMultiply(CyNetwork network, String attribute, CentiScaPeCore centiscapecore, String direction){
        this.network = network;
        this.networkchild = centiscapecore.getNetworkFactory().createNetwork();
        this.attribute = attribute;
        this.direction = direction;
        this.centiscapecore = centiscapecore;
        this.cyDesktopService = centiscapecore.getCyDesktopService();
        this.networkmanager = centiscapecore.getCyNetworkManager();
        this.networkviewfactory = centiscapecore.getNetworkViewFactory();
        this.networkviewmanager = centiscapecore.getCyNetworkViewManager();
    }
    
    //creates a new network with the same nodes and edge as the original but 
    //in an amount that depens on the specified attribute value
    public Integer multiplyNodes (){
        Integer answer = JOptionPane.YES_OPTION;
        Integer sumvalue = 0;
        for (ListIterator itNode=network.getNodeList().listIterator();itNode.hasNext();){
            CyNode node = (CyNode)itNode.next();
            sumvalue = sumvalue + network.getDefaultNodeTable().getRow(node.getSUID()).get(attribute, Integer.class);
        }
        if(sumvalue>5000){
            Object[] options = {"Continue", "Abort"};
            answer = JOptionPane.showOptionDialog(this.cyDesktopService.getJFrame(),
                     "The multiplied network is gonna contain more than 5000 nodes"
                     +"which can make the operation take a lot of time"
                     +"are you sure you want to continue?", "CentiScaPe",
                     JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                     null, options, options[0]);
        } 
        if (answer == JOptionPane.NO_OPTION){
            networkmanager.destroyNetwork(networkchild);
            return answer;            
        }
        String networkname = network.getRow(network).get(CyNetwork.NAME, String.class);
        networkchild.getRow(networkchild).set(CyNetwork.NAME, networkname + " child");
        networkchild.getDefaultNodeTable().createColumn(attribute, Integer.class, true);
        
        //copy the network nodes in the new network
        for (ListIterator itNodeF=network.getNodeList().listIterator();itNodeF.hasNext();){
            CyNode node = (CyNode)itNodeF.next();
            CyNode addedNode = networkchild.addNode();
            nodemap.put(node, addedNode);
            networkchild.getRow(addedNode).set(CyNetwork.NAME, network.getDefaultNodeTable().getRow(node.getSUID()).get("name", String.class));
            networkchild.getDefaultNodeTable().getRow(addedNode.getSUID()).set(attribute, network.getDefaultNodeTable().getRow(node.getSUID()).get(attribute, Integer.class));
        }
        //copy the network edges in the new network
        for(ListIterator itEdgeF = network.getEdgeList().listIterator(); itEdgeF.hasNext();){
            CyEdge edge = (CyEdge)itEdgeF.next();
            CyNode sourcenode = (CyNode)nodemap.get(edge.getSource());
            CyNode targetnode = (CyNode)nodemap.get(edge.getTarget());
            networkchild.addEdge(sourcenode, targetnode, edge.isDirected());
        }
        networkmanager.addNetwork(networkchild);
        
        //generate child nodes depending on the selected attribute value of each node
        //and connect them with the father node, the nodes connected to the father and eachother
        for (ListIterator itNode=networkchild.getNodeList().listIterator();itNode.hasNext();){
            CyNode node = (CyNode)itNode.next();
            Integer nodevalue = networkchild.getDefaultNodeTable().getRow(node.getSUID()).get(attribute, Integer.class);
            String curnodename = networkchild.getDefaultNodeTable().getRow(node.getSUID()).get("name", String.class);
            ArrayList<CyNode> childs = new ArrayList<CyNode>();
            //for directed networks child nodes are connected with 2 directed edges
            if (direction.equals("Directed")){
                if(nodevalue == 0){
                    nodesremove.add(node);
                    networkchild.removeEdges(networkchild.getAdjacentEdgeList(node, CyEdge.Type.ANY));
                    if(warning == 0){
                        JOptionPane.showMessageDialog(this.cyDesktopService.getJFrame(),
                        "Nodes with "+attribute+" value 0 will be removed", "CentiScaPe", JOptionPane.WARNING_MESSAGE);
                    }    
                    warning = 1;
                }
                for(int i = 1; i < nodevalue; i++){
                    CyNode curnode = networkchild.addNode();
                    childs.add(curnode);
                    networkchild.getDefaultNodeTable().getRow(curnode.getSUID()).set("name", curnodename + " child" + i);
                    for(ListIterator itEdge = networkchild.getAdjacentEdgeList(node, CyEdge.Type.OUTGOING).listIterator(); itEdge.hasNext();){
                        CyEdge curedge = (CyEdge)itEdge.next();
                        networkchild.addEdge(curnode, curedge.getTarget(), curedge.isDirected());
                    }
                    for(ListIterator itEdge = networkchild.getAdjacentEdgeList(node, CyEdge.Type.INCOMING).listIterator(); itEdge.hasNext();){
                        CyEdge curedge = (CyEdge)itEdge.next();
                        networkchild.addEdge(curedge.getSource(), curnode, curedge.isDirected());
                    }   
                    networkchild.addEdge(node, curnode, true);
                    networkchild.addEdge(curnode, node, true);
                    childmap.put(node, childs);
                }
            }
            //for undirected networks child nodes are connected with 1 undirected edge
            if (direction.equals("Undirected")){
                if(nodevalue == 0){
                    nodesremove.add(node);
                    networkchild.removeEdges(networkchild.getAdjacentEdgeList(node, CyEdge.Type.ANY));
                    if(warning == 0){
                        Object[] options = {"Continue", "Abort"};
                        answer = JOptionPane.showOptionDialog(this.cyDesktopService.getJFrame(),
                        "An "+attribute+" value of 0 corresponds to removing a node. "
                        +"In this case the computation of undirected centralities"
                        +"could not be correct. It's suggested to use directed centralities", "CentiScaPe",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                    } 
                    if (answer == JOptionPane.NO_OPTION){
                        networkmanager.destroyNetwork(networkchild);
                        return answer;            
                    }
                    warning = 1;
                }
                for(int i = 1; i < nodevalue; i++){
                    CyNode curnode = networkchild.addNode();
                    childs.add(curnode);
                    networkchild.getDefaultNodeTable().getRow(curnode.getSUID()).set("name", curnodename + " child" + i);
                    for(ListIterator itEdge = networkchild.getAdjacentEdgeList(node, CyEdge.Type.ANY).listIterator(); itEdge.hasNext();){
                        CyEdge curedge = (CyEdge)itEdge.next();
                        if (curedge.getSource().equals(node)){
                            networkchild.addEdge(curnode, curedge.getTarget(), curedge.isDirected());
                        }else{
                            networkchild.addEdge(curedge.getSource(), curnode, curedge.isDirected());
                        }    
                    }  
                    networkchild.addEdge(node, curnode, false);
                    childmap.put(node, childs);
                }
            }       
        }   
        networkchild.removeNodes(nodesremove);
        networkchildview = networkviewfactory.createNetworkView(networkchild);
        networkviewmanager.addNetworkView(networkchildview);
        return answer;    
    }
    
    //calculate the centralities for the original network as sum of the centralities
    //of the original nodes and their childs in the new network
    public void calculateCentralities(Vector centralities){
        double [] centralitieschild;
        for (Iterator itCent = centralities.iterator(); itCent.hasNext();) {
                Centrality cent = (Centrality) itCent.next();
                network.getDefaultNodeTable().createColumn(cent.getName(), Double.class, false);
        }        
        for (ListIterator itNode=network.getNodeList().listIterator();itNode.hasNext();){
            CyNode node = (CyNode)itNode.next();
            if(network.getDefaultNodeTable().getRow(node.getSUID()).get(attribute, Integer.class)==0){
                continue;
            }
            centralitieschild = new double[centralities.size()];
            if (childmap.containsKey(nodemap.get(node))){
                for (ListIterator itNodechild=((ArrayList)childmap.get(nodemap.get(node))).listIterator();itNodechild.hasNext();){
                    CyNode nodechild = (CyNode)itNodechild.next();
                    for(int i = 0; i < centralities.size(); i++){
                        Centrality centrality = (Centrality)centralities.get(i);
                        centralitieschild[i]=centralitieschild[i]+networkchild.getDefaultNodeTable().getRow(nodechild.getSUID()).get(centrality.getName(), Double.class);
                    }
                }
            }
            int p = 0;
            for (Iterator itCent = centralities.iterator(); itCent.hasNext();) {
                Centrality cent = (Centrality) itCent.next();
                network.getDefaultNodeTable().getRow(node.getSUID()).set(cent.getName(), (centralitieschild[p]+networkchild.getDefaultNodeTable().getRow(((CyNode)(nodemap.get(node))).getSUID()).get(cent.getName(), Double.class)));
                p++;
            }    
        }
    }
    
    public void attributeRounding(Double RoFac){
        network.getDefaultNodeTable().createColumn("rounded " + attribute, Integer.class, false);
        for (ListIterator itNode=network.getNodeList().listIterator();itNode.hasNext();){
            CyNode node = (CyNode)itNode.next();
            Double nodevalue = network.getDefaultNodeTable().getRow(node.getSUID()).get(attribute, Double.class);
            Integer RoundedValue = ((Long)(Math.round(nodevalue*RoFac))).intValue();
            network.getDefaultNodeTable().getRow(node.getSUID()).set("rounded " + attribute, RoundedValue);
        }           
    }
    
    public CyNetwork getNetworkChild(){
        return networkchild;
    }
    
    public CyNetworkView getNetworkChildView(){
        return networkchildview;
    }
    
    public void deleteNetwork(){
        networkmanager.destroyNetwork(networkchild);
    }
}
