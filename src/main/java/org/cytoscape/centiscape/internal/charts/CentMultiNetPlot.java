/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cytoscape.centiscape.internal.charts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import java.util.Collections;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.centiscape.internal.CentiScaPeCore;
import org.cytoscape.centiscape.internal.visualizer.CentMaxVal;
import org.cytoscape.centiscape.internal.visualizer.Centrality;
import org.cytoscape.centiscape.internal.visualizer.NetCent;
import org.cytoscape.centiscape.internal.visualizer.NetCentCompare;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;


/**
 *
 * @author Andrea Pegoraro
 */
public class CentMultiNetPlot extends JFrame 
         {
    public static String nodeName;
    
    public static String chart;
    
    public static String norm;
    
    public static Vector centralities;
  
    private CentiScaPeCore centiscapecore;
    
    private CyApplicationManager cyApplicationManager;
    
    private CySwingApplication cyDesktopService;
    
    private List selectednetworkviews;
    
    private ArrayList<NetCent> networklist = new ArrayList<NetCent>();
    
    private CyNetwork currentnetwork;
    
    private CyNetworkView currentnetworkview;
    
    private CyNode node;
    
    private int centnumber;
    
    private boolean nodefound;
    
    private Centrality ordercent;
    
    //constructor for the not ordered chart
    public CentMultiNetPlot(String nodeName, Vector centralities, CentiScaPeCore centiscapecore, String chart, String norm){
    
        super("Centralities visualization for node "+nodeName+" over the selected networks");
        
        this.ordercent = null;
        this.centralities = (Vector)centralities.clone();
        this.nodeName =  nodeName;
        this.centiscapecore = centiscapecore;
        this.chart = chart;
        this.norm = norm;
        cyApplicationManager = centiscapecore.getCyApplicationManager();
        cyDesktopService = centiscapecore.getCyDesktopService();
        
        JPanel jpanel = createDemoPanel();
     
        setContentPane(jpanel);
    }
    
    //constructor for the ordered chart
    public CentMultiNetPlot(String nodeName, Vector centralities, CentiScaPeCore centiscapecore, Centrality ordercent, String chart, String norm){
    
        super("Centralities visualization for node "+nodeName+" over the selected networks");
        
        this.ordercent = ordercent;
        this.centralities = (Vector)centralities.clone();
        this.nodeName =  nodeName;
        this.centiscapecore = centiscapecore;
        this.chart = chart;
        this.norm = norm;
        
        cyApplicationManager = centiscapecore.getCyApplicationManager();
        cyDesktopService = centiscapecore.getCyDesktopService();
        
        JPanel jpanel = createDemoPanel();
     
        setContentPane(jpanel);
    }
    
    private CategoryDataset createDataset() {
     
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        selectednetworkviews = cyApplicationManager.getSelectedNetworkViews();
        //find the node to plot in the selected networks
        for (ListIterator itNet = selectednetworkviews.listIterator(); itNet.hasNext();) {
            nodefound=false;
            centnumber = 0;
            currentnetworkview = (CyNetworkView)itNet.next();
            currentnetwork = currentnetworkview.getModel();
            for (ListIterator itNode=currentnetwork.getNodeList().listIterator();itNode.hasNext();){
                node = (CyNode)itNode.next();
                String curNodeName = currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get("name", String.class);
                if (nodeName.equals(curNodeName)){
                    nodefound = true;
                    break;
                }
            }
            //if a network doesn't contain the node show a warning message
            if (!nodefound){
                JOptionPane.showMessageDialog(this.cyDesktopService.getJFrame(), "There is no "+nodeName+"node in the "+currentnetwork+"network",
                               "Missing node", JOptionPane.INFORMATION_MESSAGE);
                continue;
            }
            //if no ordering centrality is present just create the dataset
            if (ordercent==null){
                for (Iterator itCent = centralities.iterator(); itCent.hasNext();) {
                    Centrality cent = (Centrality) itCent.next();
                    if (norm.equals("Real Value")){
                        defaultcategorydataset.addValue(currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class), cent.getName(),currentnetwork.toString());
                    }
                    if (norm.equals("Norm Max")){
                        CentMaxVal maxval = new CentMaxVal(currentnetwork, cent);
                        defaultcategorydataset.addValue(((currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class))/maxval.getMaxCentVal())*100, cent.getName(),currentnetwork.toString());
                    }
                    if (norm.equals("Norm Sum")){
                        CentMaxVal sumval = new CentMaxVal(currentnetwork, cent);
                        defaultcategorydataset.addValue(((currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class))/sumval.getSumCentVal())*100, cent.getName(),currentnetwork.toString());
                    }
                    centnumber = centnumber+1;
                }
            }
            //if the ordering centrality is present sort the network based on decreasing values of that centrality before creating the dataset
            else{
                for (Iterator itCent = centralities.iterator(); itCent.hasNext();) {
                    Centrality cent = (Centrality) itCent.next();
                    if(cent.getName().equals(ordercent.getName())){
                        NetCent netcent = new NetCent(currentnetwork,currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class));
                        if (norm.equals("Norm Max")){
                            CentMaxVal maxval = new CentMaxVal(currentnetwork, cent);
                            netcent = new NetCent(currentnetwork,((currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class))/maxval.getMaxCentVal())*100);
                        }
                        if (norm.equals("Norm Sum")){
                            CentMaxVal sumval = new CentMaxVal(currentnetwork, cent);
                            netcent = new NetCent(currentnetwork,((currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class))/sumval.getSumCentVal())*100);
                        }
                        
                        networklist.add(netcent); 
                    }
                }
            }
        }
        Collections.sort(networklist, new NetCentCompare());
        if (!(ordercent==null)){
            for (ListIterator itOrd = networklist.listIterator(); itOrd.hasNext();) {
                NetCent netcent = (NetCent) itOrd.next();
                centnumber = 0;
                CyNetwork orderednetwork = netcent.getNet();
                for (ListIterator itNode=orderednetwork.getNodeList().listIterator();itNode.hasNext();){
                    node = (CyNode)itNode.next();
                    String curNodeName = orderednetwork.getDefaultNodeTable().getRow(node.getSUID()).get("name", String.class);
                    if (nodeName.equals(curNodeName)){
                        break;
                    }
                }
                for (Iterator itOrdCent = centralities.iterator(); itOrdCent.hasNext();) {
                    Centrality cent = (Centrality) itOrdCent.next();
                    if (norm.equals("Real Value")){
                        defaultcategorydataset.addValue(orderednetwork.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class), cent.getName(),orderednetwork.toString());
                    }
                    //values normalized to maximum centrality value, percentage
                    if (norm.equals("Norm Max")){
                        CentMaxVal maxval = new CentMaxVal(orderednetwork, cent);
                        defaultcategorydataset.addValue(((orderednetwork.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class))/maxval.getMaxCentVal())*100, cent.getName(),orderednetwork.toString());
                    }
                    //values normalized to sum of centrality values, percentage
                    if (norm.equals("Norm Sum")){
                        CentMaxVal sumval = new CentMaxVal(orderednetwork, cent);
                        defaultcategorydataset.addValue(((orderednetwork.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class))/sumval.getSumCentVal())*100, cent.getName(),orderednetwork.toString());
                    }
                    centnumber = centnumber+1;
                }
            } 
        }
        return defaultcategorydataset;
    }
    
    JFreeChart createBarChart(CategoryDataset categorydataset) {
        
        ValueAxis valueAxis = new NumberAxis("Values");;
        
        final CategoryAxis categoryAxis = new CategoryAxis("Networks");
        
        if (norm.equals("Norm Max")){
           valueAxis = new NumberAxis("Normalised to max values (%)");
        }
        if (norm.equals("Norm Sum")){
           valueAxis = new NumberAxis("Normalised to sum values (%)");
        }
        
        final LayeredBarRenderer renderer = new LayeredBarRenderer();
        
        final CategoryPlot plot = new CategoryPlot(categorydataset, categoryAxis, valueAxis,renderer);
        
        plot.setOrientation(PlotOrientation.VERTICAL);
        
        JFreeChart jfreechart = new JFreeChart(nodeName, plot);

        //dinamically assign the bar widths based on the number of bars(centralities)
        //this operation it's necessary for the chart to draw properly
        if(centnumber==2){
            renderer.setSeriesBarWidth(0, 1.0);
            renderer.setSeriesBarWidth(1, 0.5);
        }
        else{
            double scale = 1.0;
            for (int i=0; i<centnumber; i++){
                renderer.setSeriesBarWidth(i, scale);
                scale = scale - (0.3/((i/1.5)+1));
            }    
        }
        
        renderer.setItemMargin(0.01);
        renderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator());
        
        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.01);
        domainAxis.setUpperMargin(0.01);
        domainAxis.setLowerMargin(0.01);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        return jfreechart;
    }
    
    JFreeChart createLineChart(CategoryDataset categorydataset) {
        
        ValueAxis valueAxis = new NumberAxis("Values");;
        
        final CategoryAxis categoryAxis = new CategoryAxis("Networks");
        
        if (norm.equals("Norm Max")){
           valueAxis = new NumberAxis("Normalised to max values (%)");
        }
        if (norm.equals("Norm Sum")){
           valueAxis = new NumberAxis("Normalised to sum values (%)");
        }
        
        final LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        
        final CategoryPlot plot = new CategoryPlot(categorydataset, categoryAxis, valueAxis,renderer);
        
        plot.setOrientation(PlotOrientation.VERTICAL);
        
        JFreeChart jfreechart = new JFreeChart(nodeName, plot);
        
        renderer.setItemMargin(0.01);
        renderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator());
        
        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.01);
        domainAxis.setUpperMargin(0.01);
        domainAxis.setLowerMargin(0.01);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        return jfreechart;
    }
    
        public JPanel createDemoPanel() {
            JFreeChart jfreechart;
            if (chart.equals("Lines and shapes")){
               jfreechart = createLineChart(createDataset());
            }
            else{
                jfreechart = createBarChart(createDataset());
            }
            ChartPanel chartpanel = new ChartPanel(jfreechart);
            chartpanel.setMouseWheelEnabled(true);
            return chartpanel;
    }
    
}
