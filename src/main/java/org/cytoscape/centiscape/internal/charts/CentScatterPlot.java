package org.cytoscape.centiscape.internal.charts;



// Referenced classes of package demo:
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.panel.selectionhandler.EntitySelectionManager;
import org.jfree.chart.panel.selectionhandler.MouseClickSelectionHandler;
import org.jfree.chart.panel.selectionhandler.RectangularRegionSelectionHandler;
import org.jfree.chart.panel.selectionhandler.RegionSelectionHandler;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.item.IRSUtilities;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.NumberCellRenderer;
import org.jfree.chart.ui.WindowUtils;
import org.jfree.data.extension.DatasetIterator;
import org.jfree.data.extension.DatasetSelectionExtension;
import org.jfree.data.extension.impl.DatasetExtensionManager;
import org.jfree.data.extension.impl.XYCursor;
import org.jfree.data.extension.impl.XYDatasetSelectionExtension;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.SelectionChangeEvent;
import org.jfree.data.general.SelectionChangeListener;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

//            SampleXYDataset2
public class CentScatterPlot extends JFrame 
        implements SelectionChangeListener<XYCursor> {

    private static String x;
    private static String y;
    public static CyNetwork currentnetwork;
    public static CyNetworkView currentview;
    private JTable table;
    private DefaultTableModel model;
    private XYSeriesCollection dataset;
    
     public CentScatterPlot(String s, CyNetwork currentnetwork, CyNetworkView currentview) {
        super(s);
        ChartPanel jpanel = (ChartPanel) createDemoPanel();
        this.currentnetwork = currentnetwork;
        this.currentview = currentview;
        //creates a split panel to visualize the chart and the table of selected nodes
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        //adds the chart to the split panel
        split.add(jpanel);
        jpanel.setPreferredSize(new java.awt.Dimension(500, 270));

        JFreeChart chart = jpanel.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        this.dataset =  (XYSeriesCollection) plot.getDataset();
        
        //creates the table to visualize selected nodes data
        this.model = new DefaultTableModel(new String[] {"Node", "X", "Y"}, 0);
        this.table = new JTable(model);
        TableColumnModel tcm = this.table.getColumnModel();
        tcm.getColumn(1).setCellRenderer(new NumberCellRenderer());
        tcm.getColumn(2).setCellRenderer(new NumberCellRenderer());
        JPanel p = new JPanel(new BorderLayout());
        JScrollPane scroller = new JScrollPane(table);
        p.add(scroller);
        p.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Selected Items: "),
                new EmptyBorder(4, 4, 4, 4)));
        p.setPreferredSize(new java.awt.Dimension(100, 100));
        //adds the table to the split panel
        split.add(p);
        setContentPane(split);
    }

      public CentScatterPlot(String x, String y, CyNetwork currentnetwork,CyNetworkView currentview) {
        super("scatter plot for " + x + "/" + y);
        this.currentnetwork = currentnetwork;
        this.currentview = currentview;
        this.x = x;
        this.y = y;
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        ChartPanel jpanel = (ChartPanel)createDemoPanel();
        //creates a split panel to visualize the chart and the table of selected nodes
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        //adds the chart to the split panel
        split.add(jpanel);
        jpanel.setPreferredSize(new java.awt.Dimension(500, 270));

        JFreeChart chart = jpanel.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        this.dataset =  (XYSeriesCollection) plot.getDataset();
        
        //creates the table to visualize selected nodes data
        this.model = new DefaultTableModel(new String[] {"Node", x, y}, 0);
        this.table = new JTable(model);
        TableColumnModel tcm = this.table.getColumnModel();
        tcm.getColumn(1).setCellRenderer(new NumberCellRenderer());
        tcm.getColumn(2).setCellRenderer(new NumberCellRenderer());
        JPanel p = new JPanel(new BorderLayout());
        JScrollPane scroller = new JScrollPane(table);
        p.add(scroller);
        p.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Selected Items: "),
                new EmptyBorder(4, 4, 4, 4)));
        p.setPreferredSize(new java.awt.Dimension(100, 270));
        //adds the table to the split panel
        split.add(p);
        setContentPane(split);
    }
      
    //creates the chart from the dataset  
    private static JFreeChart createChart(XYDataset xydataset, DatasetSelectionExtension<XYCursor> ext) {
         JFreeChart jfreechart = ChartFactory.createScatterPlot("Centiscape Scatter Plot view", x, y, xydataset);
        
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        xyplot.setNoDataMessage("NO DATA");
        xyplot.setDomainZeroBaselineVisible(true);
        xyplot.setRangeZeroBaselineVisible(true);
        XYDotRenderer r = new XYDotRenderer();
        r.setDotHeight(5);
        r.setDotWidth(5);
        int i = 0;
        for (Iterator it = currentnetwork.getNodeList().listIterator(); it.hasNext();) {
            it.next();
            r.setSeriesPaint(i, Color.red);
            i = i+1;
        }
        xyplot.setRenderer(r);
        NumberAxis numberaxis = (NumberAxis) xyplot.getDomainAxis();
        numberaxis.setAutoRangeIncludesZero(false);
        numberaxis.setTickMarkInsideLength(2.0F);
        numberaxis.setTickMarkOutsideLength(0.0F);
        NumberAxis numberaxis1 = (NumberAxis) xyplot.getRangeAxis();
        numberaxis1.setTickMarkInsideLength(2.0F);
        numberaxis1.setTickMarkOutsideLength(0.0F);
                
        //add selection specific rendering
        IRSUtilities.setSelectedItemPaint(r, ext, Color.blue);
        
        //register plot as selection change listener
        ext.addChangeListener(xyplot);
        
        return jfreechart;
    }

    /**
     * The selection changed, so we change the table model
     * 
     * @param event
     */
    public void selectionChanged(SelectionChangeEvent<XYCursor> event) {
        //clear the selected nodes table
        while (this.model.getRowCount() > 0) {
            this.model.removeRow(0);
        }

        XYDatasetSelectionExtension ext = (XYDatasetSelectionExtension)
                event.getSelectionExtension(); 
        DatasetIterator<XYCursor> iter = ext.getSelectionIterator(true);
        
        //reset the node selection state to false
        for (Iterator it = currentnetwork.getNodeList().listIterator(); it.hasNext();) {
            CyNode node = (CyNode) it.next();
            currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).set("selected", false);            
        }
         
        while (iter.hasNext()) {
            XYCursor dc = iter.next();
            
            //get the SUID and centrality values of the nodes selected on the chart
            String seriesKey = this.dataset.getSeriesKey(dc.series).toString();
            Number x = this.dataset.getX(dc.series, dc.item);
            Number y = this.dataset.getY(dc.series, dc.item);
            
            //set the state of the nodes selected on the chart to selected in the network
            currentnetwork.getDefaultNodeTable().getRow(Long.valueOf(seriesKey).longValue()).set("selected", true);
            //get the node name to visualize on the table
            String nodeName = currentnetwork.getDefaultNodeTable().getRow(Long.valueOf(seriesKey).longValue()).get("name", String.class);
            
            //add selected nodes to the table
            this.model.addRow(new Object[] { nodeName, x, y});        
        }
        //update the network view to show the node selection
        currentview.updateView();
    }
    
    // create an XYDataSet for scatter plot from x-y centralities information
    private static XYDataset createDataSet() {
        double vx, vy;
        XYSeriesCollection dxy = new XYSeriesCollection();
      

        for (Iterator it = currentnetwork.getNodeList().listIterator(); it.hasNext();) {
            double[][] val = new double[2][1];
            CyNode node = (CyNode) it.next();
            XYSeries series = new XYSeries (node.getSUID());

            if      (currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).getRaw(x) != null) {  
                vx = Double.valueOf(currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).getRaw(x).toString());
                
                val[0][0] = vx;
            }

            if      (currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).getRaw(y) != null) {  
                vy = Double.valueOf(currentnetwork.getDefaultNodeTable().getRow(node.getSUID()).getRaw(y).toString());
                val[1][0] = vy;
            }
            series.add(val[0][0], val[1][0]);
            dxy.addSeries(series);
        }
        return (dxy);
    }
    
    //create the chart panel
    public JPanel createDemoPanel() {
        XYDataset xydataset = createDataSet();
        //extend dataset and add selection change listener
        DatasetSelectionExtension<XYCursor> datasetExtension = new XYDatasetSelectionExtension(xydataset);
        datasetExtension.addChangeListener(this);
        jfreechart = createChart(xydataset, datasetExtension);
        jfreechart.removeLegend();
        chartpanel = new ChartPanel(jfreechart);
        chartpanel.setVerticalAxisTrace(true);
        chartpanel.setHorizontalAxisTrace(true);
        
        chartpanel.setPopupMenu(null);
        chartpanel.setDomainZoomable(true);
        chartpanel.setRangeZoomable(true);
        chartpanel.setMouseWheelEnabled(true);
        
        // add a selection handler
        RegionSelectionHandler selectionHandler = new RectangularRegionSelectionHandler();
        chartpanel.addMouseHandler(selectionHandler);
        chartpanel.addMouseHandler(new MouseClickSelectionHandler());
        chartpanel.removeMouseHandler(chartpanel.getZoomHandler());
          
        // add a selection manager
        DatasetExtensionManager dExManager = new DatasetExtensionManager();
        dExManager.registerDatasetExtension(datasetExtension);
        chartpanel.setSelectionManager(new EntitySelectionManager(chartpanel, new Dataset[] { xydataset }, dExManager));
        
        chartpanel.setLayout(new GridBagLayout());
        
        //make a button to save an image of the chart
        JButton menu = new JButton("export image");
        
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.weightx = 1.0;
        
        constraint.weighty = 1.0;
        constraint.anchor = GridBagConstraints.LAST_LINE_END;
        constraint.gridx = 0;
        constraint.gridy = 0;
        chartpanel.add(menu, constraint);
        menu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveIt();
            }
        });
        return chartpanel;
    }
    
    static File f;
    static JFreeChart jfreechart;
    ChartPanel chartpanel;
    
    //saves an image file of the chart
    private  void saveIt() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("export image");
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == 0) {
            f = fc.getSelectedFile();
            if (!f.getAbsolutePath().endsWith(".png") && !f.getAbsolutePath().endsWith(".jpg")) {
                f = new File((new StringBuilder()).append(f.getAbsolutePath()).append(".jpg").toString());
            }
            if (f.exists() && 1 == JOptionPane.showConfirmDialog(this, (new StringBuilder()).append(f.getName()).append(" exists. Overwrite?").toString(), "Confirm export current chart as an image...", 0, 2)) {
                return;
            }
                if (f.getAbsolutePath().endsWith(".png")) {
                    savePng();
                }
                if (f.getAbsolutePath().endsWith(".jpg")) {
                    savePng();
                }
        }
    }
    
    //save the chart as a png image
    private  void savePng(){
        
    try{
        ChartUtilities.saveChartAsPNG(f, jfreechart, chartpanel.getWidth(), chartpanel.getHeight());
    } catch (IOException exc) {
                exc.printStackTrace();
            }
    }

   public static void main(String args[]) {
        CentScatterPlot scatterplotdemo1 = new CentScatterPlot("Scatter Plot", currentnetwork, currentview);
        scatterplotdemo1.pack();
        WindowUtils.centerFrameOnScreen(scatterplotdemo1);
        scatterplotdemo1.setVisible(true);
    }
}
