/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cytoscape.centiscape.internal.visualizer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.centiscape.internal.CentiScaPeCore;
import org.cytoscape.centiscape.internal.charts.CentMultiNetPlot;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;

/**
 *
 * @author Andrea Pegoraro
 */
public class CentMultiNetVisualOptions extends javax.swing.JPanel {

    public static String nodeName;
    
    public static Vector centralities;
    
    private Vector selectedCentralities = new Vector();
    
    private HashSet<String> nodeNameSet = new HashSet<String>();
  
    private CentiScaPeCore centiscapecore;
    
    private CyApplicationManager cyApplicationManager;
    
    private List selectednetworkviews;
    
    private CyNetwork currentnetwork;
    
    private CyNetworkView currentnetworkview;
    
    private CyNode node;
    
    private Centrality ordercent;
    
    /** Creates new form CentMultiNetVisualOptions */
    public CentMultiNetVisualOptions(CyApplicationManager cyApplicationManager, CentiScaPeCore centiscapecore, Vector centralities) {
        this.centiscapecore = centiscapecore;
        this.centralities = (Vector)centralities.clone();
        this.cyApplicationManager = cyApplicationManager;
        initComponents();
        initializeCheckBoxes();
        loadNodesList();
        loadOrderCentList();
        loadChartList();
        loadNormList();
    }

    //populates the node selection combobox
    public  void loadNodesList(){
        nodeCombo.removeAllItems();
        selectednetworkviews = cyApplicationManager.getSelectedNetworkViews();
        for (ListIterator itNet = selectednetworkviews.listIterator(); itNet.hasNext();) {
            currentnetworkview = (CyNetworkView)itNet.next();
            currentnetwork = currentnetworkview.getModel();
            for (ListIterator itNode=currentnetwork.getNodeList().listIterator();itNode.hasNext();){
                node=(CyNode)itNode.next();
                nodeName = currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get("name", String.class);
                nodeNameSet.add(nodeName);
            }    
        }
        for (Iterator itCurNode = nodeNameSet.iterator(); itCurNode.hasNext();) {
            String curNode = (String)itCurNode.next();
            nodeCombo.addItem(curNode);
        }        
    }
    
    //populates the ordering centrality combobox
    public  void loadOrderCentList(){
        centOrderCombo.removeAllItems();
        centOrderCombo.addItem("no ordering");
        if(DegreeCheckbox.isSelected()){
            centOrderCombo.addItem("Degree");
        }
        if(RadialityCheckbox.isSelected()){
            centOrderCombo.addItem("Radiality");
        }
        if(ClosenessCheckbox.isSelected()){
            centOrderCombo.addItem("Closeness");
        }
        if(StressCheckbox.isSelected()){
            centOrderCombo.addItem("Stress");
        }
        if(BetweennessCheckbox.isSelected()){
            centOrderCombo.addItem("Betweenness");
        }
        if(CentroidValueCheckbox.isSelected()){
            centOrderCombo.addItem("Centroid");
        }
        if(EccentricityCheckbox.isSelected()){
            centOrderCombo.addItem("Eccentricity");
        }
        if(EigenVectorCheckbox.isSelected()){
            centOrderCombo.addItem("EigenVector");
        }
        if(BridgingCheckbox.isSelected()){
            centOrderCombo.addItem("Bridging");
        }             
    }
    
    public  void loadChartList(){
        chartCombo.removeAllItems();
        chartCombo.addItem("Layered bars");
        chartCombo.addItem("Lines and shapes");
    }
    
    public  void loadNormList(){
        normCombo.removeAllItems();
        normCombo.addItem("Real Value");
        normCombo.addItem("Norm Max");
        normCombo.addItem("Norm Sum");
    }
     
    private void initializeCheckBoxes() {                                        
        // TODO add your handling code here:
        DegreeCheckbox.setEnabled(false);
        RadialityCheckbox.setEnabled(false);
        ClosenessCheckbox.setEnabled(false);
        StressCheckbox.setEnabled(false);
        BetweennessCheckbox.setEnabled(false);
        CentroidValueCheckbox.setEnabled(false);
        EccentricityCheckbox.setEnabled(false);
        EigenVectorCheckbox.setEnabled(false);
        BridgingCheckbox.setEnabled(false);
        
        for (Iterator itCent = centralities.iterator(); itCent.hasNext();) {
            Centrality cent = (Centrality)itCent.next();
            if(cent.getName().contains("Degree")) {
                DegreeCheckbox.setEnabled(true);
            }
            if(cent.getName().contains("Radiality")) {
                RadialityCheckbox.setEnabled(true);
            }
            if(cent.getName().contains("Closeness")) {
                ClosenessCheckbox.setEnabled(true);
            }
            if(cent.getName().contains("Stress")) {
                StressCheckbox.setEnabled(true);
            }
            if(cent.getName().equals("Betweenness unDir")||cent.getName().equals("Betweenness Dir")) {
                BetweennessCheckbox.setEnabled(true);
            }
            if(cent.getName().contains("Centroid")) {
                CentroidValueCheckbox.setEnabled(true);
            }
            if(cent.getName().contains("Eccentricity")) {
                EccentricityCheckbox.setEnabled(true);
            }
            if(cent.getName().contains("EigenVector")) {
                EigenVectorCheckbox.setEnabled(true);
            }
            if(cent.getName().contains("Bridging")) {
                BridgingCheckbox.setEnabled(true);
            }         
        }
    } 
    
    public void verifyselection() {
        if (DegreeCheckbox.isSelected()
                || EccentricityCheckbox.isSelected()
                || RadialityCheckbox.isSelected()
                || ClosenessCheckbox.isSelected()
                || StressCheckbox.isSelected()
                || BetweennessCheckbox.isSelected()
                || CentroidValueCheckbox.isSelected()
                || EigenVectorCheckbox.isSelected()
                || BridgingCheckbox.isSelected()) {

            PlotButton.setEnabled(true);
        } else {
            PlotButton.setEnabled(false);
        }
        loadOrderCentList();
    }                                           

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nodeCombo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        DegreeCheckbox = new javax.swing.JCheckBox();
        RadialityCheckbox = new javax.swing.JCheckBox();
        ClosenessCheckbox = new javax.swing.JCheckBox();
        StressCheckbox = new javax.swing.JCheckBox();
        BetweennessCheckbox = new javax.swing.JCheckBox();
        CentroidValueCheckbox = new javax.swing.JCheckBox();
        EccentricityCheckbox = new javax.swing.JCheckBox();
        EigenVectorCheckbox = new javax.swing.JCheckBox();
        BridgingCheckbox = new javax.swing.JCheckBox();
        PlotButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        SelectAllButton = new javax.swing.JButton();
        UnselectAllButton = new javax.swing.JButton();
        centOrderCombo = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        chartCombo = new javax.swing.JComboBox();
        normCombo = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();

        jLabel1.setText("Multiple Network Centrality plot options");

        nodeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Node Name");

        DegreeCheckbox.setText("Degree");
        DegreeCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DegreeCheckboxActionPerformed(evt);
            }
        });

        RadialityCheckbox.setText("Radiality");
        RadialityCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadialityCheckboxActionPerformed(evt);
            }
        });

        ClosenessCheckbox.setText("Closeness");
        ClosenessCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClosenessCheckboxActionPerformed(evt);
            }
        });

        StressCheckbox.setText("Stress");
        StressCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StressCheckboxActionPerformed(evt);
            }
        });

        BetweennessCheckbox.setText("Betweenness");
        BetweennessCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BetweennessCheckboxActionPerformed(evt);
            }
        });

        CentroidValueCheckbox.setText("Centroid Value");
        CentroidValueCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CentroidValueCheckboxActionPerformed(evt);
            }
        });

        EccentricityCheckbox.setText("Eccentricity");
        EccentricityCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EccentricityCheckboxActionPerformed(evt);
            }
        });

        EigenVectorCheckbox.setText("Eigen Vector");
        EigenVectorCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EigenVectorCheckboxActionPerformed(evt);
            }
        });

        BridgingCheckbox.setText("Bridging");
        BridgingCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BridgingCheckboxActionPerformed(evt);
            }
        });

        PlotButton.setText("Plot");
        PlotButton.setEnabled(false);
        PlotButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PlotButtonMouseClicked(evt);
            }
        });

        jLabel3.setText("Warning: For the plot to work properly make sure the node you want to plot has the ");

        jLabel4.setText("same name in evry network");

        jLabel5.setText("Centralities:");

        SelectAllButton.setText("Select All");
        SelectAllButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SelectAllButtonMouseClicked(evt);
            }
        });
        SelectAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectAllButtonActionPerformed(evt);
            }
        });

        UnselectAllButton.setText("Unselect All");
        UnselectAllButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UnselectAllButtonMouseClicked(evt);
            }
        });

        centOrderCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setText("Order by:");

        jLabel7.setText("Chart type:");

        chartCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        normCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setText("Normalization:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(EccentricityCheckbox)
                                .addGap(51, 51, 51)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(BetweennessCheckbox)
                                    .addComponent(EigenVectorCheckbox))
                                .addGap(73, 73, 73)
                                .addComponent(CentroidValueCheckbox)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(nodeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(centOrderCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(PlotButton, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(SelectAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(UnselectAllButton)
                        .addGap(42, 42, 42))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(DegreeCheckbox)
                                    .addComponent(StressCheckbox))
                                .addGap(71, 71, 71)
                                .addComponent(RadialityCheckbox)
                                .addGap(95, 95, 95)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ClosenessCheckbox)
                                    .addComponent(BridgingCheckbox))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chartCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(normCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nodeCombo)
                    .addComponent(centOrderCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DegreeCheckbox)
                    .addComponent(RadialityCheckbox)
                    .addComponent(ClosenessCheckbox))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(StressCheckbox)
                    .addComponent(BetweennessCheckbox)
                    .addComponent(CentroidValueCheckbox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EigenVectorCheckbox)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(EccentricityCheckbox)
                        .addComponent(BridgingCheckbox)))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(chartCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(normCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PlotButton)
                    .addComponent(SelectAllButton)
                    .addComponent(UnselectAllButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 427, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 337, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void UnselectAllButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UnselectAllButtonMouseClicked
        // TODO add your handling code here:
        DegreeCheckbox.setSelected(false);
        EccentricityCheckbox.setSelected(false);
        RadialityCheckbox.setSelected(false);
        ClosenessCheckbox.setSelected(false);
        StressCheckbox.setSelected(false);
        BetweennessCheckbox.setSelected(false);
        CentroidValueCheckbox.setSelected(false);
        EigenVectorCheckbox.setSelected(false);
        BridgingCheckbox.setSelected(false);
        verifyselection();
    }//GEN-LAST:event_UnselectAllButtonMouseClicked

    private void SelectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectAllButtonActionPerformed
        // TODO add your handling code here:
        verifyselection();
    }//GEN-LAST:event_SelectAllButtonActionPerformed

    private void SelectAllButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SelectAllButtonMouseClicked
        // TODO add your handling code here:
        if (DegreeCheckbox.isEnabled()){
            DegreeCheckbox.setSelected(true);
        }
        if (EccentricityCheckbox.isEnabled()){
            EccentricityCheckbox.setSelected(true);
        }
        if (RadialityCheckbox.isEnabled()){
            RadialityCheckbox.setSelected(true);
        }
        if (ClosenessCheckbox.isEnabled()){
            ClosenessCheckbox.setSelected(true);
        }
        if (StressCheckbox.isEnabled()){
            StressCheckbox.setSelected(true);
        }
        if (BetweennessCheckbox.isEnabled()){
            BetweennessCheckbox.setSelected(true);
        }
        if (CentroidValueCheckbox.isEnabled()){
            CentroidValueCheckbox.setSelected(true);
        }
        if (EigenVectorCheckbox.isEnabled()){
            EigenVectorCheckbox.setSelected(true);
        }
        if (BridgingCheckbox.isEnabled()){
            BridgingCheckbox.setSelected(true);
        }
        verifyselection();
    }//GEN-LAST:event_SelectAllButtonMouseClicked

    //Gets the centrality vector and, if selected, the ordering centrality needed by CentMultiNetPlot
    //and calls the correct constructor
    private void PlotButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlotButtonMouseClicked
        // TODO add your handling code here:
        selectedCentralities.clear();
        ordercent = null;
        if(DegreeCheckbox.isSelected()){
            for (Iterator itCent = centralities.iterator(); itCent.hasNext();) {
                Centrality cent = (Centrality)itCent.next();
                if(cent.getName().contains("Degree")) {
                    selectedCentralities.add(cent);
                    if (((String)centOrderCombo.getSelectedItem()).equals("Degree")){
                        ordercent=cent;               
                    }
                }
            }
            
        }
        if(RadialityCheckbox.isSelected()){
            for (Iterator itCent = centralities.iterator(); itCent.hasNext();) {
                Centrality cent = (Centrality)itCent.next();
                if(cent.getName().contains("Radiality")) {
                    selectedCentralities.add(cent);
                    if (((String)centOrderCombo.getSelectedItem()).equals("Radiality")){
                        ordercent=cent;               
                    }
                }
            }
        }
        if(ClosenessCheckbox.isSelected()){
            for (Iterator itCent = centralities.iterator(); itCent.hasNext();) {
                Centrality cent = (Centrality)itCent.next();
                if(cent.getName().contains("Closeness")) {
                    selectedCentralities.add(cent);
                    if (((String)centOrderCombo.getSelectedItem()).equals("Closeness")){
                        ordercent=cent;               
                    }
                }
            }
        }
        if(StressCheckbox.isSelected()){
            for (Iterator itCent = centralities.iterator(); itCent.hasNext();) {
                Centrality cent = (Centrality)itCent.next();
                if(cent.getName().contains("Stress")) {
                    selectedCentralities.add(cent);
                    if (((String)centOrderCombo.getSelectedItem()).equals("Stress")){
                        ordercent=cent;               
                    }
                }
            }
        }
        if(BetweennessCheckbox.isSelected()){
            for (Iterator itCent = centralities.iterator(); itCent.hasNext();) {
                Centrality cent = (Centrality)itCent.next();
                if(cent.getName().equals("Betweenness unDir")||cent.getName().equals("Betweenness Dir")) {
                    selectedCentralities.add(cent);
                    if (((String)centOrderCombo.getSelectedItem()).equals("Betweenness")){
                        ordercent=cent;               
                    }
                }
            }
        }
        if(CentroidValueCheckbox.isSelected()){
            for (Iterator itCent = centralities.iterator(); itCent.hasNext();) {
                Centrality cent = (Centrality)itCent.next();
                if(cent.getName().contains("Centroid")) {
                    selectedCentralities.add(cent);
                    if (((String)centOrderCombo.getSelectedItem()).equals("Centroid")){
                        ordercent=cent;               
                    }
                }
            }
        }
        if(EccentricityCheckbox.isSelected()){
            for (Iterator itCent = centralities.iterator(); itCent.hasNext();) {
                Centrality cent = (Centrality)itCent.next();
                if(cent.getName().contains("Eccentricity")) {
                    selectedCentralities.add(cent);
                    if (((String)centOrderCombo.getSelectedItem()).equals("Eccentricity")){
                        ordercent=cent;               
                    }
                }
            }
        }
        if(EigenVectorCheckbox.isSelected()){
            for (Iterator itCent = centralities.iterator(); itCent.hasNext();) {
                Centrality cent = (Centrality)itCent.next();
                if(cent.getName().contains("EigenVector")) {
                    selectedCentralities.add(cent);
                    if (((String)centOrderCombo.getSelectedItem()).equals("EigenVector")){
                        ordercent=cent;               
                    }
                }
            }
        }
        if(BridgingCheckbox.isSelected()){
            for (Iterator itCent = centralities.iterator(); itCent.hasNext();) {
                Centrality cent = (Centrality)itCent.next();
                if(cent.getName().contains("Bridging")) {
                    selectedCentralities.add(cent);
                    if (((String)centOrderCombo.getSelectedItem()).equals("Bridging")){
                        ordercent=cent;               
                    }
                }
            }
        }
        String nodename =(String)nodeCombo.getSelectedItem();
        String chart =(String)chartCombo.getSelectedItem();
        String norm = (String)normCombo.getSelectedItem();
        CentMultiNetPlot cmnp;
        if (ordercent == null){
            cmnp=new CentMultiNetPlot(nodename,selectedCentralities, centiscapecore, chart, norm);
        }
        else{
            cmnp=new CentMultiNetPlot(nodename,selectedCentralities, centiscapecore, ordercent, chart, norm);
        }
        cmnp.setSize(700,400);
        cmnp.setVisible(true);
    }//GEN-LAST:event_PlotButtonMouseClicked

    private void BridgingCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BridgingCheckboxActionPerformed
        // TODO add your handling code here:
        verifyselection();
    }//GEN-LAST:event_BridgingCheckboxActionPerformed

    private void EigenVectorCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EigenVectorCheckboxActionPerformed
        // TODO add your handling code here:
        verifyselection();
    }//GEN-LAST:event_EigenVectorCheckboxActionPerformed

    private void EccentricityCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EccentricityCheckboxActionPerformed
        // TODO add your handling code here:
        verifyselection();
    }//GEN-LAST:event_EccentricityCheckboxActionPerformed

    private void CentroidValueCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CentroidValueCheckboxActionPerformed
        // TODO add your handling code here:
        verifyselection();
    }//GEN-LAST:event_CentroidValueCheckboxActionPerformed

    private void BetweennessCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BetweennessCheckboxActionPerformed
        // TODO add your handling code here:
        verifyselection();
    }//GEN-LAST:event_BetweennessCheckboxActionPerformed

    private void StressCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StressCheckboxActionPerformed
        // TODO add your handling code here:
        verifyselection();
    }//GEN-LAST:event_StressCheckboxActionPerformed

    private void ClosenessCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClosenessCheckboxActionPerformed
        // TODO add your handling code here:
        verifyselection();
    }//GEN-LAST:event_ClosenessCheckboxActionPerformed

    private void RadialityCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadialityCheckboxActionPerformed
        // TODO add your handling code here:
        verifyselection();
    }//GEN-LAST:event_RadialityCheckboxActionPerformed

    private void DegreeCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DegreeCheckboxActionPerformed
        // TODO add your handling code here:
        verifyselection();
    }//GEN-LAST:event_DegreeCheckboxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox BetweennessCheckbox;
    private javax.swing.JCheckBox BridgingCheckbox;
    private javax.swing.JCheckBox CentroidValueCheckbox;
    private javax.swing.JCheckBox ClosenessCheckbox;
    private javax.swing.JCheckBox DegreeCheckbox;
    private javax.swing.JCheckBox EccentricityCheckbox;
    private javax.swing.JCheckBox EigenVectorCheckbox;
    private javax.swing.JButton PlotButton;
    private javax.swing.JCheckBox RadialityCheckbox;
    private javax.swing.JButton SelectAllButton;
    private javax.swing.JCheckBox StressCheckbox;
    private javax.swing.JButton UnselectAllButton;
    private javax.swing.JComboBox centOrderCombo;
    private javax.swing.JComboBox chartCombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox nodeCombo;
    private javax.swing.JComboBox normCombo;
    // End of variables declaration//GEN-END:variables
}
