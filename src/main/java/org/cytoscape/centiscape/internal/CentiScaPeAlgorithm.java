/*
 * CentiScaPeAlgorithm.java
 *
 * Created on 7 marzo 2007, 10.01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *
 * @author scardoni
 */
package org.cytoscape.centiscape.internal; 

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JPanel;
import org.cytoscape.centiscape.internal.Betweenness.BetweennessMethods;
import org.cytoscape.centiscape.internal.Betweenness.EdgeBetweenness;
import org.cytoscape.centiscape.internal.Betweenness.FinalResultBetweenness;
import org.cytoscape.centiscape.internal.Centroid.CentroidMethods;
import org.cytoscape.centiscape.internal.Centroid.FinalResultCentroid;
import org.cytoscape.centiscape.internal.Closeness.ClosenessMethods;
import org.cytoscape.centiscape.internal.Closeness.FinalResultCloseness;
import org.cytoscape.centiscape.internal.Degree.FinalResultDegree;
import org.cytoscape.centiscape.internal.Eccentricity.EccentricityMethods;
import org.cytoscape.centiscape.internal.Eccentricity.FinalResultEccentricity;
import org.cytoscape.centiscape.internal.EigenVector.CalculateEigenVector;
import org.cytoscape.centiscape.internal.Radiality.FinalResultRadiality;
import org.cytoscape.centiscape.internal.Stress.FinalResultStress;
import org.cytoscape.centiscape.internal.visualizer.ImplCentrality;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;

public class CentiScaPeAlgorithm {

    private boolean stop = false;
    // one Vector for each centrality
    private Vector ClosenessVectorResults;
    private Vector EccentricityVectorResults;
    private static Vector StressVectorResults;
    private Vector RadialityVectorResults;
    private static Vector BetweennessVectorResults;
    private static Vector CentroidVectorResults;
    private static Vector CentroidVectorofNodes;
    private Vector DegreeVectorResults;
    public double[][] adjacencyMatrixOfNetwork;
    public EdgeBetweenness edgeBetweenness;
    private double Diameter = 0;
    private double average = 0;
    private double totaldist;
    // variables to verify the selected centralities
    private boolean StressisOn = false;
    private boolean ClosenessisOn = false;
    private boolean EccentricityisOn = false;
    private boolean RadialityisOn = false;
    private boolean DiameterisOn = false;
    private boolean DiameterisSelected = false;
    private boolean BetweennessisOn = false;
    private boolean CentroidisOn = false;
    private boolean DegreeisOn = false;
    private boolean AverageisOn = false;
    private boolean EigenVectorisOn = false;
    private boolean BridgingisOn = false;
    private boolean EdgeBetweennessisOn = false;
    private boolean doNotDisplayBetweenness = false;
    public static TreeMap Stressmap = new TreeMap();
    Vector vectorOfNodeAttributes = new Vector();
    Vector vectorOfNetworkAttributes = new Vector();
    public Vector VectorResults = new Vector();
    public boolean openResultPanel;
    public String networkname;
    public CyNetwork network;
    public CentiScaPeCore centiscapecore;
    public List<CyNode> nodeList;
    public Boolean isWeighted;
    /**
     * Creates a new instance of CentiScaPeAlgorithm
     */
    public CentiScaPeAlgorithm(CentiScaPeCore centiscapecore) {
        this.centiscapecore = centiscapecore; 
    }

    public Vector ExecuteCentiScaPeAlgorithm(CyNetwork network,JPanel c) {

        stop = false;
        this.network = network;

        openResultPanel = false;

        CentiScaPeStartMenu menustart = (CentiScaPeStartMenu) c;
        isWeighted = menustart.isWeighted;

        System.out.println("Entered Centiscape Algorithm "+network.toString());
        String networkname = network.getDefaultNetworkTable().getRow(network.getSUID()).get("name", String.class);
        
        int totalnodecount = network.getNodeCount();
        int totalEdgeCount = network.getEdgeCount();
        Long networksuid = network.getSUID();
        nodeList = network.getNodeList();

        int nodeworked = 0;
        Vector CentiScaPeMultiShortestPathVector = null;

        CyTable nodeTable = network.getDefaultNodeTable();
        CyTable networkTable = network.getDefaultNetworkTable();

        
        for (Iterator i = vectorOfNodeAttributes.iterator(); i.hasNext();) {
            
            String attributetoremove = (String) i.next();
            if (nodeTable.getColumn(attributetoremove) != null) {
                nodeTable.deleteColumn(attributetoremove);
            }
        }

        for (Iterator i = vectorOfNetworkAttributes.iterator(); i.hasNext();) {

            String attributetoremove = (String) i.next();
            if (networkTable.getColumn(attributetoremove) != null) {
                nodeTable.deleteColumn(attributetoremove);
            }

        }
        
        
        // Create the data structure for each selected centralities
        if (ClosenessisOn) {
            ClosenessVectorResults = new Vector();
            openResultPanel = true;
        }
        if (EccentricityisOn) {
            EccentricityVectorResults = new Vector();
            openResultPanel = true;
        }
        if (DegreeisOn) {
            DegreeVectorResults = new Vector();
            openResultPanel = true;
        }
        if (RadialityisOn) {
            RadialityVectorResults = new Vector();
            openResultPanel = true;
        }
        if (DiameterisOn) {
            //double currentDiametervalue = 0;
        }

        if (AverageisOn) {
            totaldist = 0;
        }
        
        if (EigenVectorisOn){
            adjacencyMatrixOfNetwork = new double[totalnodecount][totalnodecount];
            
        }
        if(EdgeBetweennessisOn){
            edgeBetweenness = new EdgeBetweenness(network);
        }
        if (BetweennessisOn || StressisOn || CentroidisOn) {
            openResultPanel = true;
            if (BetweennessisOn) {
                BetweennessVectorResults = new Vector();
            }
            if (StressisOn) {
                StressVectorResults = new Vector();
            }
            if (CentroidisOn) {

                CentroidVectorResults = new Vector();
                CentroidVectorofNodes = new Vector();
            }
            Stressmap.clear();
            
            for (Iterator i = network.getNodeList().iterator(); i.hasNext();) {
                if (stop) {
                    unselectallnodes(network);
                    return null;
                }
                CyNode root = (CyNode) i.next();
                if (BetweennessisOn) {
                    BetweennessVectorResults.add(new FinalResultBetweenness(root, 0));
                }
                if (StressisOn || BetweennessisOn) {

                    // StressVectorResults.add(new FinalResultStress(root.getSUID(), 0));
                    Stressmap.put(root.getSUID(), new Double(0));
                }
                if (CentroidisOn) {

                    CentroidVectorofNodes.addElement(root.getSUID());
                }

            }
        }
        TreeMap inizializedmap = new TreeMap(Stressmap);
        Diameter = 0;
        // Start iteration on each node
        int k = 0;
        for (CyNode root : nodeList) {

            if (stop) {
                unselectallnodes(network);
                return null;
            }
            nodeworked++;
            menustart.updatenodecounting(nodeworked, totalnodecount);
            //CyNode root = (CyNode) i.next();

            if (EigenVectorisOn) {                
                List<CyNode> neighbors = network.getNeighborList(root, CyEdge.Type.ANY);
                for(CyNode neighbor : neighbors)
                {
                    adjacencyMatrixOfNetwork[k][nodeList.indexOf(neighbor)] = 1.0 ;
                }
                k++;
            }
            
            // Execute the multi shortest path algorithm for node root and put the results on the
            // vector called ShortestPathVector
            CentiScaPeMultiShortestPathVector = CentiScaPeMultiShortestPathTreeAlgorithm.ExecuteMultiShortestPathTreeAlgorithm(network, root, StressisOn, BetweennessisOn, inizializedmap, false, isWeighted);

            // Create a Single Shortest Path Vector
            Vector CentiScaPeSingleShortestPathVector = new Vector();
            Vector NodesFound = new Vector();
            CentiScaPeShortestPathList CurrentList;
            for (int j = 0; j < CentiScaPeMultiShortestPathVector.size(); j++) {

                CurrentList = (CentiScaPeShortestPathList) CentiScaPeMultiShortestPathVector.get(j);
                //System.out.println(CurrentList.toString());
               
                String nodename = ((CentiScaPeMultiSPath) CurrentList.getLast()).getName();
                if (!NodesFound.contains(nodename)) {
                    NodesFound.add(nodename);
                    CentiScaPeSingleShortestPathVector.add(CurrentList);
                }
            }
            
            // Calculate each properties
            if (ClosenessisOn) {

                ClosenessVectorResults.add((FinalResultCloseness) ClosenessMethods.CalculateCloseness(CentiScaPeSingleShortestPathVector, root));
            }

            if (EccentricityisOn) {
                EccentricityVectorResults.add(EccentricityMethods.CalculateEccentricity(CentiScaPeSingleShortestPathVector, root));

            }
            if (DegreeisOn) {
                FinalResultDegree currentDegree = new FinalResultDegree(root, 0);

                double currentDegreevalue = network.getNeighborList(root, CyEdge.Type.ANY).size();
                currentDegree.update(currentDegreevalue);
                DegreeVectorResults.addElement(currentDegree);
            }

            if (RadialityisOn) {
                CentiScaPeShortestPathList currentlist;
                //RadialityVectorResults.add (CalculateRadiality(CentiScaPeSingleShortestPathVector,root));
                FinalResultRadiality currentRadiality = new FinalResultRadiality(root, 0);
                int distance;
                for (int j = 0; j < CentiScaPeSingleShortestPathVector.size(); j++) {
                    currentlist = (CentiScaPeShortestPathList) CentiScaPeSingleShortestPathVector.elementAt(j);
                    distance = ((CentiScaPeMultiSPath)currentlist.getLast()).getCost();
                    currentRadiality.updatesizevector(new Integer(distance));
                }
                RadialityVectorResults.add(currentRadiality);
            }
            if (DiameterisOn || DiameterisSelected) {

                double currentdiametervalue;
                currentdiametervalue = CalculateDiameter(CentiScaPeSingleShortestPathVector);
                if (Diameter < currentdiametervalue) {
                    Diameter = currentdiametervalue;
                }
            }
            if (AverageisOn) {
                CentiScaPeShortestPathList currentlist;
                for (int j = 0; j < CentiScaPeSingleShortestPathVector.size(); j++) {
                    currentlist = (CentiScaPeShortestPathList) CentiScaPeSingleShortestPathVector.elementAt(j);
                    //totaldist = totaldist + currentlist.size() - 1;
                    totaldist = totaldist+ ((CentiScaPeMultiSPath)currentlist.get(currentlist.size()-1)).getCost();
                }
            }


            if (BetweennessisOn) {
               // BetweennessVectorResults.add(new FinalResultBetweenness(root,0));
                BetweennessMethods.updateBetweenness(CentiScaPeMultiShortestPathVector, BetweennessVectorResults);
            }
            if (EdgeBetweennessisOn){
                edgeBetweenness.updateEdgeBetweenness(CentiScaPeMultiShortestPathVector);
                
            }
            if (StressisOn) {
                // StressMethods.updateStress(CentiScaPeMultiShortestPathVector, StressVectorResults);
            }

            if (CentroidisOn) {

                CentroidMethods.updateCentroid(CentiScaPeSingleShortestPathVector, root, totalnodecount, CentroidVectorofNodes, CentroidVectorResults);
            }
            
        }


        unselectallnodes(network);

        if (RadialityisOn) {
            for (Iterator i = RadialityVectorResults.iterator(); i.hasNext();) {
                if (stop) {
                    unselectallnodes(network);
                    return null;
                }
                FinalResultRadiality currentRadiality;
                currentRadiality = (FinalResultRadiality) i.next();
                double Diametervalue = Diameter;
                int currentDist = 0;
                double currentRadialityvalue, parziale1, parziale2;
                for (int j = 0; j < currentRadiality.getVectorSize(); j++) {
                    currentDist = currentRadiality.getlistsizeat(j);
                    if (currentDist != 0) {
                        parziale1 = Diametervalue + 1 - currentDist;
                        parziale2 = totalnodecount - 1;
                        currentRadialityvalue = ((Diametervalue + 1 - currentDist));
                        currentRadiality.update(currentRadialityvalue);
                    }
                }
                currentRadiality.finalcalculus(totalnodecount - 1);
            }
            //RadialityVectorResults.add (CalculateRadiality(CentiScaPeSingleShortestPathVector,root));

        }

        if (CentroidisOn) {

            CentroidMethods.calculateCentroid(CentroidVectorResults, totalnodecount, CentroidVectorofNodes);



        }


        menustart.endcalculus(totalnodecount);
        VectorResults.clear();


        if (DiameterisSelected) {
            networkTable.createColumn("Diameter unDir", Double.class, false);
            network.getRow(network).set("Diameter unDir", new Double(Diameter));

            vectorOfNetworkAttributes.addElement("Diameter unDir");
        }

        if (AverageisOn) {

            average = totaldist / (totalnodecount * (totalnodecount - 1));
            networkTable.createColumn("Average Distance unDir", Double.class, false);
            network.getRow(network).set("Average Distance unDir", new Double(average));

            vectorOfNetworkAttributes.addElement("Average Distance unDir");
        }
        if (EccentricityisOn) {
            nodeTable.createColumn("Eccentricity unDir", Double.class, false);
            vectorOfNodeAttributes.addElement("Eccentricity unDir");
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;
            for (Iterator i = EccentricityVectorResults.iterator(); i.hasNext();) {

                FinalResultEccentricity currentnodeeccentricity = (FinalResultEccentricity) i.next();
                double currenteccentricity = currentnodeeccentricity.geteccentricity();
                if (currenteccentricity < min) {
                    min = currenteccentricity;
                }
                if (currenteccentricity > max) {
                    max = currenteccentricity;
                }
                totalsum = totalsum + currenteccentricity;

                CyRow row = nodeTable.getRow(currentnodeeccentricity.getNode().getSUID());
                row.set("Eccentricity unDir", new Double(currenteccentricity));
            }
            networkTable.createColumn("Eccentricity Max value unDir", Double.class, false);
            networkTable.createColumn("Eccentricity min value unDir", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("Eccentricity mean value unDir", Double.class, false);
            network.getRow(network).set("Eccentricity Max value unDir", new Double(max));
            network.getRow(network).set("Eccentricity min value unDir", new Double(min));
            network.getRow(network).set("Eccentricity mean value unDir", new Double(mean));

            vectorOfNetworkAttributes.addElement("Eccentricity Max value");
            vectorOfNetworkAttributes.addElement("Eccentricity min value");
            vectorOfNetworkAttributes.addElement("Eccentricity mean value");

            ImplCentrality eccentricityCentrality = new ImplCentrality("Eccentricity unDir", true, mean, min, max);
            VectorResults.add(eccentricityCentrality);
        }

        if (ClosenessisOn) {
            nodeTable.createColumn("Closeness unDir", Double.class, false);
            vectorOfNodeAttributes.addElement("Closeness unDir");
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;
            for (Iterator i = ClosenessVectorResults.iterator(); i.hasNext();) {
                FinalResultCloseness currentnodecloseness = (FinalResultCloseness) i.next();
                double currentcloseness = currentnodecloseness.getCloseness();


                if (currentcloseness < min) {
                    min = currentcloseness;
                }
                if (currentcloseness > max) {
                    max = currentcloseness;
                }
                totalsum = totalsum + currentcloseness;

                CyRow row = nodeTable.getRow(currentnodecloseness.getNode().getSUID());
                row.set("Closeness unDir", new Double(currentcloseness));
            }
            networkTable.createColumn("Closeness Max value unDir", Double.class, false);
            networkTable.createColumn("Closeness min value unDir", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("Closeness mean value unDir", Double.class, false);
            network.getRow(network).set("Closeness Max value unDir", new Double(max));
            network.getRow(network).set("Closeness min value unDir", new Double(min));
            network.getRow(network).set("Closeness mean value unDir", new Double(mean));
            vectorOfNetworkAttributes.addElement("Closeness Max value");
            vectorOfNetworkAttributes.addElement("Closeness min value");
            vectorOfNetworkAttributes.addElement("Closeness mean value");
            ImplCentrality closenessCentrality = new ImplCentrality("Closeness unDir", true, mean, min, max);
            VectorResults.add(closenessCentrality);
        }


        if (RadialityisOn) {
            nodeTable.createColumn("Radiality unDir", Double.class, false);
            vectorOfNodeAttributes.addElement("Radiality unDir");
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;
            for (Iterator i = RadialityVectorResults.iterator(); i.hasNext();) {

                FinalResultRadiality currentnoderadiality = (FinalResultRadiality) i.next();

                double currentradiality = currentnoderadiality.getRadiality();

                if (currentradiality < min) {
                    min = currentradiality;
                }
                if (currentradiality > max) {
                    max = currentradiality;
                }
                totalsum = totalsum + currentradiality;
                CyRow row = nodeTable.getRow(currentnoderadiality.getNode().getSUID());
                row.set("Radiality unDir", new Double(currentradiality));
            }
            networkTable.createColumn("Radiality Max value unDir", Double.class, false);
            networkTable.createColumn("Radiality min value unDir", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("Radiality mean value unDir", Double.class, false);
            network.getRow(network).set("Radiality Max value unDir", new Double(max));
            network.getRow(network).set("Radiality min value unDir", new Double(min));
            network.getRow(network).set("Radiality mean value unDir", new Double(mean));
            vectorOfNetworkAttributes.addElement("Radiality Max value");
            vectorOfNetworkAttributes.addElement("Radiality min value");
            vectorOfNetworkAttributes.addElement("Radiality mean value");
            ImplCentrality radialityCentrality = new ImplCentrality("Radiality unDir", true, mean, min, max);
            VectorResults.add(radialityCentrality);

        }

        if (BetweennessisOn && doNotDisplayBetweenness==false ) {
            nodeTable.createColumn("Betweenness unDir", Double.class, false);
            vectorOfNodeAttributes.addElement("Betweenness unDir");
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;

            for (Iterator i = BetweennessVectorResults.iterator(); i.hasNext();) {

                FinalResultBetweenness currentnodebetweenness = (FinalResultBetweenness) i.next();

                double currentbetweenness = currentnodebetweenness.getBetweenness();

                if (currentbetweenness < min) {
                    min = currentbetweenness;
                }
                if (currentbetweenness > max) {
                    max = currentbetweenness;
                }
                totalsum = totalsum + currentbetweenness;
                
                
            CyRow row = nodeTable.getRow(currentnodebetweenness.getNode().getSUID());
             row.set("Betweenness unDir", new Double(currentbetweenness));
              }
            networkTable.createColumn("Betweenness Max value unDir", Double.class, false);
            networkTable.createColumn("Betweenness min value unDir", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("Betweenness mean value unDir", Double.class, false);
            network.getRow(network).set("Betweenness Max value unDir", new Double(max));
            network.getRow(network).set("Betweenness min value unDir", new Double(min));
            network.getRow(network).set("Betweenness mean value unDir", new Double(mean));
            vectorOfNetworkAttributes.addElement("Betweenness Max value");
            vectorOfNetworkAttributes.addElement("Betweenness min value");
            vectorOfNetworkAttributes.addElement("Betweenness mean value");
            ImplCentrality betweennessCentrality = new ImplCentrality("Betweenness unDir", true, mean, min, max);
            VectorResults.add(betweennessCentrality);

        }

        if (DegreeisOn) {
            nodeTable.createColumn("Degree unDir", Double.class, false);
            vectorOfNodeAttributes.addElement("Degree unDir");
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;
            for (Iterator i = DegreeVectorResults.iterator(); i.hasNext();) {

                FinalResultDegree currentnodeDegree = (FinalResultDegree) i.next();

                double currentdegree = currentnodeDegree.getDegree();

                if (currentdegree < min) {
                    min = currentdegree;
                }
                if (currentdegree > max) {
                    max = currentdegree;
                }
                totalsum = totalsum + currentdegree;
                CyRow row = nodeTable.getRow(currentnodeDegree.getNode().getSUID());
                row.set("Degree unDir", new Double(currentdegree));
            }
            networkTable.createColumn("Degree Max value unDir", Double.class, false);
            networkTable.createColumn("Degree min value unDir", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("Degree mean value unDir", Double.class, false);
            network.getRow(network).set("Degree Max value unDir", new Double(max));
            network.getRow(network).set("Degree min value unDir", new Double(min));
            network.getRow(network).set("Degree mean value unDir", new Double(mean));
            vectorOfNetworkAttributes.addElement("Degree Max value");
            vectorOfNetworkAttributes.addElement("Degree min value");
            vectorOfNetworkAttributes.addElement("Degree mean value");
            ImplCentrality degreeCentrality = new ImplCentrality("Degree unDir", true, mean, min, max);
            VectorResults.add(degreeCentrality);
        }


        if (StressisOn) {
            nodeTable.createColumn("Stress unDir", Double.class, false);
            vectorOfNodeAttributes.addElement("Stress unDir");



            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;
            Set stressSet = Stressmap.entrySet();
            for (Iterator i = stressSet.iterator(); i.hasNext();) {
                Map.Entry currentmapentry = (Map.Entry) i.next();
                long currentnodeSUID = (Long) currentmapentry.getKey();
                CyNode currentnode = network.getNode(currentnodeSUID);
                double currentstress = (double) (Double) (currentmapentry.getValue());
                StressVectorResults.add(new FinalResultStress(currentnode, currentstress));

                if (currentstress < min) {
                    min = currentstress;
                }
                if (currentstress > max) {
                    max = currentstress;
                }
                totalsum = totalsum + currentstress;
                CyRow row = nodeTable.getRow(currentnodeSUID);
                row.set("Stress unDir", new Double(currentstress));
            }
            networkTable.createColumn("Stress Max value unDir", Double.class, false);
            networkTable.createColumn("Stress min value unDir", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("Stress mean value unDir", Double.class, false);
            network.getRow(network).set("Stress Max value unDir", new Double(max));
            network.getRow(network).set("Stress min value unDir", new Double(min));
            network.getRow(network).set("Stress mean value unDir", new Double(mean));
            vectorOfNetworkAttributes.addElement("Stress Max value");
            vectorOfNetworkAttributes.addElement("Stress min value");
            vectorOfNetworkAttributes.addElement("Stress mean value");
            ImplCentrality stressCentrality = new ImplCentrality("Stress unDir", true, mean, min, max);
            VectorResults.add(stressCentrality);

        }
        if (CentroidisOn) {
            nodeTable.createColumn("Centroid unDir", Double.class, false);
            vectorOfNodeAttributes.addElement("Centroid unDir");
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;
            for (Iterator i = CentroidVectorResults.iterator(); i.hasNext();) {


                FinalResultCentroid currentnodeCentroid = (FinalResultCentroid) i.next();

                double currentcentroid = currentnodeCentroid.getCentroid();

                if (currentcentroid < min) {
                    min = currentcentroid;
                }
                if (currentcentroid > max) {
                    max = currentcentroid;
                }
                totalsum = totalsum + currentcentroid;
                CyRow row = nodeTable.getRow(currentnodeCentroid.getNode().getSUID());
                row.set("Centroid unDir", new Double(currentcentroid));
            }
            networkTable.createColumn("Centroid Max value unDir", Double.class, false);
            networkTable.createColumn("Centroid min value unDir", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("Centroid mean value unDir", Double.class, false);
            network.getRow(network).set("Centroid Max value unDir", new Double(max));
            network.getRow(network).set("Centroid min value unDir", new Double(min));
            network.getRow(network).set("Centroid mean value unDir", new Double(mean));
      
            vectorOfNetworkAttributes.addElement("Centroid Max value unDir");
            vectorOfNetworkAttributes.addElement("Centroid min value unDir");
            vectorOfNetworkAttributes.addElement("Centroid mean value unDir");
            ImplCentrality centroidCentrality = new ImplCentrality("Centroid unDir", true, mean, min, max);
            VectorResults.add(centroidCentrality);
        }
        if(EigenVectorisOn){
            vectorOfNodeAttributes.addElement("EigenVector unDir");   
            
            vectorOfNetworkAttributes.addElement("EigenVector Max value unDir");
            vectorOfNetworkAttributes.addElement("EigenVector min value unDir");
            vectorOfNetworkAttributes.addElement("EigenVector mean value unDir");
            
            CalculateEigenVector.executeAndWriteValues(adjacencyMatrixOfNetwork,network, nodeList,nodeTable,"EigenVector ", VectorResults, "unDir");
        }
        if (BridgingisOn) {
            nodeTable.createColumn("Bridging unDir", Double.class, false);
            vectorOfNodeAttributes.addElement("Bridging unDir");
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;

            for (Iterator i = BetweennessVectorResults.iterator(); i.hasNext();) {
                FinalResultBetweenness currentnodebetweenness = (FinalResultBetweenness) i.next();

                double currentbetweenness = currentnodebetweenness.getBetweenness();
                CyNode root = currentnodebetweenness.getNode();
                List<CyNode> bridgingNeighborList = network.getNeighborList(root, CyEdge.Type.ANY);
                double bridgingCoefficient = 0;if(bridgingNeighborList.size() != 0){
                double BCNumerator = (1/(double)(bridgingNeighborList.size()));
                double BCDenominator = 0;                
                for (CyNode bridgingNeighbor : bridgingNeighborList) {
                    BCDenominator = BCDenominator + 1/(double)(network.getNeighborList(bridgingNeighbor, CyEdge.Type.ANY).size());
                }
                bridgingCoefficient = BCNumerator/BCDenominator;
                }
                double bridgingCentrality = bridgingCoefficient*currentbetweenness;
                if ( bridgingCentrality < min) {
                    min = bridgingCentrality;
                }
                if (bridgingCentrality > max) {
                    max = bridgingCentrality;
                }
                totalsum = totalsum + bridgingCentrality;
            CyRow row = nodeTable.getRow(currentnodebetweenness.getNode().getSUID());
             row.set("Bridging unDir", new Double(bridgingCentrality));
              }
            networkTable.createColumn("Bridging Max value unDir", Double.class, false);
            networkTable.createColumn("Bridging min value unDir", Double.class, false);
            double mean = totalsum / totalnodecount;
            networkTable.createColumn("Bridging mean value unDir", Double.class, false);
            network.getRow(network).set("Bridging Max value unDir", new Double(max));
            network.getRow(network).set("Bridging min value unDir", new Double(min));
            network.getRow(network).set("Bridging mean value unDir", new Double(mean));
            vectorOfNetworkAttributes.addElement("Bridging Max value unDir");
            vectorOfNetworkAttributes.addElement("Bridging min value unDir");
            vectorOfNetworkAttributes.addElement("Bridging mean value unDir");
            ImplCentrality bridgingCentrality = new ImplCentrality("Bridging unDir", true, mean, min, max);
            VectorResults.add(bridgingCentrality);

        }
        if(EdgeBetweennessisOn){
            /*edgeBetweenness.displayEdgeBetweennessValues();
            
            List<CyNode> nodeList = network.getNodeList();
            for(CyNode r:nodeList){
                System.out.println(r.getSUID()+"-->"+network.getRow(r).get(CyNetwork.NAME, String.class));
            }
            */
            CyTable edgeTable = network.getDefaultEdgeTable();
            edgeTable.createColumn("Edge Betweenness unDir", Double.class, false);
            Map<CyEdge,Double> values = edgeBetweenness.getEdgeBetweennessMap();
            Set<CyEdge> edges = values.keySet();
        
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, totalsum = 0, currentvalue;
            Iterator it = edges.iterator();
            
            while (it.hasNext()) {
                        
                CyEdge root = (CyEdge) it.next();
                currentvalue = values.get(root);
                if (currentvalue < min) {
                    min = currentvalue;
                }
                if (currentvalue > max) {
                    max = currentvalue;
                }
                totalsum = totalsum + currentvalue;
                CyRow row = edgeTable.getRow(root.getSUID());
                row.set("Edge Betweenness unDir", new Double(currentvalue));
            }
            
            //CyTable networkTable = network.getDefaultNetworkTable();
                //int totalnodecount = network.getNodeCount();
            networkTable.createColumn("Edge Betweenness "+"Max value unDir", Double.class, false);
            networkTable.createColumn("Edge Betweenness "+"min value unDir", Double.class, false);
            double mean = totalsum / totalEdgeCount;
            networkTable.createColumn("Edge Betweenness "+"mean value unDir", Double.class, false);
            network.getRow(network).set("Edge Betweenness "+"Max value unDir", new Double(max));
            network.getRow(network).set("Edge Betweenness "+"min value unDir", new Double(min));
            network.getRow(network).set("Edge Betweenness "+"mean value unDir", new Double(mean));

            // for embending centrality in Results Panel -- These two lines are enough
            ImplCentrality edgeBetweennessCentrality = new ImplCentrality("Edge Betweenness unDir", true, mean, min, max);
            VectorResults.add(edgeBetweennessCentrality);
        }
        
        if (openResultPanel) {
            /// cytoPaneleast.setState(CytoPanelState.DOCK);
        } else {
            ///cytoPaneleast.setState(CytoPanelState.HIDE);
        }
    
    return VectorResults;

    }

    public void endalgorithm() {
        stop = true;
    }

    public void setChecked(boolean[] ison) {
        DiameterisOn = ison[4];
        DiameterisSelected = ison[0];
        AverageisOn = ison[1];
        DegreeisOn = ison[2];
        EccentricityisOn = ison[3];
        RadialityisOn = ison[4];
        ClosenessisOn = ison[5];
        StressisOn = ison[6];
        BetweennessisOn = ison[7];
        CentroidisOn = ison[8];
        EigenVectorisOn = ison[9];
        BridgingisOn = ison[10];
        EdgeBetweennessisOn = ison[11];
        if(BridgingisOn && BetweennessisOn==false){
            BetweennessisOn = true;
            doNotDisplayBetweenness = true;
        }
        if(ison[7]){
            doNotDisplayBetweenness = false;
        }

    }

    public void unselectallnodes(CyNetwork network) {
        for (Iterator i = network.getNodeList().iterator(); i.hasNext();) {
            CyNode tmpnode = (CyNode) i.next();
            CyRow row = network.getRow(tmpnode);
            row.set("selected", true);
        }
    }

    public double CalculateDiameter(Vector SingleShortestPathVector) {
        CentiScaPeShortestPathList currentdiameterlist;
        int currentmaxvalue = 0;
        double currentvalue = 0;
        int cost=0;
        for (int j = 0; j < SingleShortestPathVector.size(); j++) {
            currentdiameterlist = (CentiScaPeShortestPathList) SingleShortestPathVector.elementAt(j);
            cost = ((CentiScaPeMultiSPath)currentdiameterlist.getLast()).getCost();
            currentmaxvalue = Math.max(currentmaxvalue, cost);
        }
        if (currentmaxvalue > currentvalue) {
            currentvalue = ((double) currentmaxvalue);
        }
        return currentvalue;
    }

    public String getName(CyNode currentnode, CyNetwork currentnetwork) {
        return currentnetwork.getRow(currentnode).get("name", String.class);

    }
}
