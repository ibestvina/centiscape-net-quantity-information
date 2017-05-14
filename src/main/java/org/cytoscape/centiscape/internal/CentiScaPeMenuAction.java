package org.cytoscape.centiscape.internal;

import java.awt.event.ActionEvent;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;

/**
 * Creates a new menu item under Apps menu section.
 *
 */
public class CentiScaPeMenuAction extends AbstractCyAction {
    
     public CyApplicationManager cyApplicationManager;
     public CySwingApplication cyDesktopService;
     public CyActivator cyactivator;

	public CentiScaPeMenuAction(CyApplicationManager cyApplicationManager, final String menuTitle, CyActivator cyactivator) {
		
		super(menuTitle, cyApplicationManager, null, null);
		setPreferredMenu("Apps");
                this.cyactivator = cyactivator;
		this.cyApplicationManager = cyApplicationManager;
                this.cyDesktopService = cyactivator.getDesktopService();
               
	}

	public void actionPerformed(ActionEvent e) {

		// Write your own function here.
		//JOptionPane.showMessageDialog(null, "Centiscapeeee");
              //  System.out.println("centiscapeee");
               // CentiScaPeStartMenu centiscapestartmenu = new CentiScaPeStartMenu(cyApplicationManager, cytoscapeDesktopService);
               // registerService(context,centiscapestartmenu,CytoPanelComponent.class, new Properties());
		CentiScaPeCore centiscapecore = new CentiScaPeCore(cyactivator);   
	}
}
