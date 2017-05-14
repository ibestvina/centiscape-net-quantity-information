/*
 * CentMultiNetVisualizer.java
 *
 * Created on 18 dicembre 2007, 12.11
 */
package org.cytoscape.centiscape.internal.visualizer;

import java.awt.Component;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.centiscape.internal.CentiScaPeCore;




/**
 *
 * @author  Andrea Pegoraro
 */
public class CentMultiNetVisualizer extends javax.swing.JPanel implements  CytoPanelComponent {

    private BoxLayout CentralitiesSelectorLayout;
    private CentiScaPeCore centiscapecore;
    private CyApplicationManager cyApplicationManager;
    private Vector centralities;
    private CentMultiNetVisualOptions vo;

    /** Creates new form CentMultiNetVisualizer */
    public CentMultiNetVisualizer(CyApplicationManager cyApplicationManager, CentiScaPeCore centiscapecore) {
        initComponents();
        this.centiscapecore = centiscapecore;
        this.cyApplicationManager = cyApplicationManager;     
        CentralitiesSelectorLayout = null;
    }
    
    public void setEnabled(Vector<Centrality> vc) {

        super.setVisible(false);

        // set the layout for visualizing panels in box mode
        CentralitiesSelectorLayout = new BoxLayout(CentralitiesSelector, BoxLayout.Y_AXIS);
        CentralitiesSelector.setLayout(CentralitiesSelectorLayout);
        centralities = vc;
        vo = new CentMultiNetVisualOptions(cyApplicationManager,centiscapecore ,centralities);
        addOptionsPanel(vo);
        
        super.setVisible(true);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
        }
    }
    
    public void addOptionsPanel(JPanel op) {
        op.setAlignmentX(Component.LEFT_ALIGNMENT);
        CentralitiesSelector.add(op);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        CentralitiesSelector = new javax.swing.JPanel();
        ExitButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();

        setPreferredSize(new java.awt.Dimension(500, 698));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(480, 686));

        CentralitiesSelector.setMaximumSize(new java.awt.Dimension(568, 32767));

        ExitButton.setText("Close Result Panel");
        ExitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ExitButtonMouseClicked(evt);
            }
        });

        jSeparator3.setMaximumSize(new java.awt.Dimension(0, 2));

        org.jdesktop.layout.GroupLayout CentralitiesSelectorLayout = new org.jdesktop.layout.GroupLayout(CentralitiesSelector);
        CentralitiesSelector.setLayout(CentralitiesSelectorLayout);
        CentralitiesSelectorLayout.setHorizontalGroup(
            CentralitiesSelectorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(CentralitiesSelectorLayout.createSequentialGroup()
                .add(200, 200, 200)
                .add(ExitButton)
                .addContainerGap(202, Short.MAX_VALUE))
            .add(jSeparator3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        CentralitiesSelectorLayout.setVerticalGroup(
            CentralitiesSelectorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(CentralitiesSelectorLayout.createSequentialGroup()
                .add(ExitButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 11, Short.MAX_VALUE)
                .addContainerGap(641, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(CentralitiesSelector);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ExitButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExitButtonMouseClicked
        centiscapecore.closeCurrentResultPanel(this);
        // TODO add your handling code here:
    }//GEN-LAST:event_ExitButtonMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CentralitiesSelector;
    private javax.swing.JButton ExitButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    // End of variables declaration//GEN-END:variables

 public Component getComponent() {
		return this;
	}


	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.EAST;
	}


	public String getTitle() {
		return "Selected Netwroks Analysys";
	}


	public Icon getIcon() {
		return null;
	}



}