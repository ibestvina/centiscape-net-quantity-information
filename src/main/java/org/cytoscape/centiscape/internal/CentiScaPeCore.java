/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.centiscape.internal.visualizer.CentMultiNetVisualizer;
import org.cytoscape.centiscape.internal.visualizer.CentVisualizer;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;

/**
 *
 * @author scardoni
 */
public class CentiScaPeCore {

    public CyNetwork network;
    public CyNetworkView view;
    public CyApplicationManager cyApplicationManager;
    public CyNetworkViewManager cyNetworkViewManager;
    public CyNetworkViewFactory cyNetworkViewFactory;
    public CyNetworkFactory cyNetworkFactory;
    public CyNetworkManager cyNetworkManager;
    public CySwingApplication cyDesktopService;
    public CyServiceRegistrar cyServiceRegistrar;
    public CyActivator cyactivator;
    public CentiScaPeStartMenu centiscapestartmenu;
    public CentVisualizer centvisualizer;
    public CentMultiNetVisualizer centmultinetvisualizer;
    public ArrayList visualizerlist;
    

    public CentiScaPeCore(CyActivator cyactivator) {
        this.cyactivator = cyactivator;
        this.cyApplicationManager = cyactivator.getApplicationManager();
        this.cyNetworkManager = cyactivator.getNetworkManager();
        this.cyDesktopService = cyactivator.getDesktopService(); 
        this.cyServiceRegistrar = cyactivator.getServiceRegistrar();
        this.cyNetworkViewFactory = cyactivator.getNetworkViewFactory();
        this.cyNetworkFactory = cyactivator.getNetworkFactory();
        this.cyNetworkViewManager = cyactivator.getNetworkViewManager();      
        centiscapestartmenu = createCentiScaPeStartMenu();
        visualizerlist = new ArrayList();
        updatecurrentnetwork();
    }

    public void updatecurrentnetwork() {

        //get the network view object
        if (view == null) {
            view = null;
            network = null;
        } else {
            view = cyApplicationManager.getCurrentNetworkView();
            //get the network object; this contains the graph  
            network = view.getModel();
        }

    }
    

    public void closecore() {
        network = null;
        view = null;

    }

 

    public CentiScaPeStartMenu createCentiScaPeStartMenu() {
        CentiScaPeStartMenu startmenu = new CentiScaPeStartMenu(cyactivator, this);
        cyServiceRegistrar.registerService(startmenu, CytoPanelComponent.class, new Properties());
        CytoPanel cytopanelwest = cyDesktopService.getCytoPanel(CytoPanelName.WEST);
        int index = cytopanelwest.indexOfComponent(startmenu);
        cytopanelwest.setSelectedIndex(index);
        return startmenu;
    }

    public void closeCentiscapeStartMenu() {

        cyServiceRegistrar.unregisterService(centiscapestartmenu, CytoPanelComponent.class);
    }

    
    public CentVisualizer createCentiScaPeVisualizer() {
       // System.out.println("create1");
        centvisualizer = new CentVisualizer(cyApplicationManager,this);//(cyactivator,this);
      //  System.out.println("create2");
        cyServiceRegistrar.registerService(centvisualizer, CytoPanelComponent.class, new Properties());
      //  System.out.println("create3");
        CytoPanel cytopaneleast = cyDesktopService.getCytoPanel(CytoPanelName.EAST);
        cytopaneleast.setState(CytoPanelState.DOCK);
        int index = cytopaneleast.indexOfComponent(centvisualizer);
        cytopaneleast.setSelectedIndex(index);
        visualizerlist.add(centvisualizer);
      //  System.out.println("create4");
        return centvisualizer;
    }

    public void closeCentiscapevisualizer() {
        for (Iterator i = visualizerlist.listIterator(); i.hasNext();) {
            CentVisualizer currentvisualizer = (CentVisualizer) i.next();    
 
        cyServiceRegistrar.unregisterService(currentvisualizer, CytoPanelComponent.class);
        }
    }

    public CentVisualizer getvisualizer() {
        return centvisualizer;
    }
    
    public CentMultiNetVisualizer createCentiScaPeMultiNetVisualizer() {
       // System.out.println("create1");
        centmultinetvisualizer = new CentMultiNetVisualizer(cyApplicationManager,this);//(cyactivator,this);
      //  System.out.println("create2");
        cyServiceRegistrar.registerService(centmultinetvisualizer, CytoPanelComponent.class, new Properties());
      //  System.out.println("create3");
        CytoPanel cytopaneleast = cyDesktopService.getCytoPanel(CytoPanelName.EAST);
        cytopaneleast.setState(CytoPanelState.DOCK);
        int index = cytopaneleast.indexOfComponent(centmultinetvisualizer);
        cytopaneleast.setSelectedIndex(index);
        //visualizerlist.add(centmultinetvisualizer);
      //  System.out.println("create4");
        return centmultinetvisualizer;
    }

    public CentMultiNetVisualizer getMultiNetVisualizer() {
        return centmultinetvisualizer;
    }
    
    public void closeMultiNetVisualizer() {

        cyServiceRegistrar.unregisterService(centmultinetvisualizer, CytoPanelComponent.class);
    }
    
    public CyApplicationManager getCyApplicationManager(){
        return this.cyApplicationManager;
    }
    public CyNetworkManager getCyNetworkManager(){
        return this.cyNetworkManager;
    }
    public CyNetworkViewManager getCyNetworkViewManager(){
        return this.cyNetworkViewManager;
    }
    public CySwingApplication getCyDesktopService(){
        return this.cyDesktopService;
    } 
    public CyNetworkViewFactory getNetworkViewFactory(){
        return this.cyNetworkViewFactory;
    }
    public CyNetworkFactory getNetworkFactory(){
        return this.cyNetworkFactory;
    }
     
     public void closeCurrentResultPanel(CentVisualizer currentresultpanel) {
        cyServiceRegistrar.unregisterService(currentresultpanel, CytoPanelComponent.class);
    }
     public void closeCurrentResultPanel(CentMultiNetVisualizer currentresultpanel) {
        cyServiceRegistrar.unregisterService(currentresultpanel, CytoPanelComponent.class);
    }
}
