package org.cytoscape.centiscape.internal.charts;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.cytoscape.centiscape.internal.visualizer.Centrality;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class CentPlotByNode extends JFrame {

    public static String nodeName;
    public static Vector currentcentralities;
   
    public CyNode node;
    
    public  DefaultCategoryDataset dds = new DefaultCategoryDataset();
  
    public CyNetwork currentnetwork;

    // polymorphic constructor
    public CentPlotByNode(CyNode node, CyNetwork currentnetwork, Vector currentcentralities) {
      

        super("Plot By Node visualization for " + currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get("name", String.class));
        this.currentnetwork = currentnetwork;
        this.nodeName = currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get("name", String.class);
        this.setDefaultCloseOperation(this.HIDE_ON_CLOSE);
        this.currentcentralities = (Vector)currentcentralities.clone();
        this.node = node;
        
        JPanel jpanel = createDemoPanel();

        setContentPane(jpanel);
    }

    //for the input centrality creates a dataset with mean, min, max value of that centrality in 
    //the selected networkand the value of the centrality for the selected node
    private CategoryDataset createDataset(Centrality cent) {
     
        String s1 = nodeName + cent.getName()+ " values";
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        String centralityname;
        String direction;
            System.out.println(nodeName + " centrality " + cent.getName());
            if ((cent.getName().equals("OutDegree"))||(cent.getName().equals("InDegree" ))){
                centralityname = cent.getName();
                direction = "Dir";
            }
            else{
                centralityname = cent.getName().split(" ")[0];
                direction = cent.getName().split(" ")[1];
            }
            if (centralityname.equals("Node")) {
                centralityname = cent.getName().split(" ")[2];
            }

            double v1 = currentnetwork.getDefaultNetworkTable().getRow(currentnetwork.getSUID()).get(centralityname+" mean value "+direction, Double.class);
            double v2 = currentnetwork.getDefaultNetworkTable().getRow(currentnetwork.getSUID()).get(centralityname+" min value "+direction, Double.class);
            double v3 = currentnetwork.getDefaultNetworkTable().getRow(currentnetwork.getSUID()).get(centralityname+" max value "+direction, Double.class);
            double v4 = currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class);
                
            System.out.println(" scrivo  il nodo " + centralityname + "v4 " + v4);
            if ((!Double.isInfinite(v1))
                && (!Double.isInfinite(v2))
                && (!Double.isInfinite(v3))
                && (!Double.isInfinite(v4))) {
                defaultcategorydataset.addValue(v1, "average value", centralityname);
                defaultcategorydataset.addValue(v2, "min value", centralityname);
                defaultcategorydataset.addValue(v3, "max value", centralityname);
                defaultcategorydataset.addValue(v4, s1, centralityname);
                    
                System.out.println(" scrivo  il nodo2 " + centralityname + " nodo vtmp = " + currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).get(cent.getName(), Double.class)
                + "v4 " + v4);
                System.out.println(" scrivo  il nodo3 " + centralityname + "v4 " + v4);
            }
        return defaultcategorydataset;
    }
    
  JFreeChart createChart(CategoryDataset categorydataset, Centrality cent) {
        JFreeChart jfreechart = ChartFactory.createBarChart(nodeName, cent.getName()+ " statistics for " + nodeName, "Value", categorydataset);
        jfreechart.setBackgroundPaint(Color.white);

        // plotting setup
        CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
        categoryplot.setBackgroundPaint(Color.lightGray);
        categoryplot.setDomainGridlinePaint(Color.white);
        categoryplot.setDomainGridlinesVisible(true);
        categoryplot.setRangeGridlinePaint(Color.white);

        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        //bar renderer setup
        BarRenderer renderer = (BarRenderer) categoryplot.getRenderer();
        // permit bar outline marking
        renderer.setDrawBarOutline(true);
        categoryplot.setRenderer(renderer);
        // gradients for plots
        GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
        GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, Color.green, 0.0F, 0.0F, new Color(0, 64, 0));
        GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, new Color(64, 0, 0));
        GradientPaint gradientpaint3 = new GradientPaint(0.0F, 0.0F, Color.WHITE, 0.0F, 0.0F, new Color(64, 64, 0));
        renderer.setSeriesPaint(0, gradientpaint);
        renderer.setSeriesPaint(1, gradientpaint1);
        renderer.setSeriesPaint(2, gradientpaint2);
        renderer.setSeriesPaint(2, gradientpaint3);
        
        renderer.setLegendItemToolTipGenerator(new StandardCategorySeriesLabelGenerator("Tooltip: {0}"));

        return jfreechart;
    }

    //for evry centrality in the input vector (except Edge Betweeneess) creates a
    //chart, all the charts are drawn in the same panel with a grid layout
    public JPanel createDemoPanel() {
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new GridLayout(2,5));
        for (Iterator it = this.currentcentralities.iterator(); it.hasNext();) {
            Centrality cent = (Centrality) it.next(); 
            if (!(cent.getName().equals("Edge Betweenness unDir")) && !(cent.getName().equals("Edge Betweenness Dir"))) {
                JFreeChart jfreechart = createChart(createDataset(cent), cent);
                ChartPanel panel = new ChartPanel(jfreechart);
                panel.setMouseWheelEnabled(true);
                jpanel.add(panel);
            }    
        }   
        return jpanel;
    }
}
