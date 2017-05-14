package org.cytoscape.centiscape.internal;

import java.util.Properties;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator { 
	private final static String VERSION = "0.1";
    private CyApplicationManager applicationManager;
    private CySwingApplication desktopService;
    private CyServiceRegistrar serviceRegistrar;
    private CyNetworkManager networkManager;
    private CyNetworkViewManager networkViewManager;
    private CyNetworkViewFactory networkViewFactory;
    private CyNetworkFactory networkFactory;
    private CentiScaPeMenuAction menuAction;

    @Override
    public void start(BundleContext context) throws Exception {
        this.applicationManager = getService(context, CyApplicationManager.class);
        this.desktopService = getService(context, CySwingApplication.class);
        this.serviceRegistrar = getService(context, CyServiceRegistrar.class);
        this.networkManager = getService(context, CyNetworkManager.class);
        this.networkViewManager = getService(context, CyNetworkViewManager.class);
        this.networkViewFactory = getService(context, CyNetworkViewFactory.class);
        this.networkFactory = getService(context, CyNetworkFactory.class);
        this.menuAction = new CentiScaPeMenuAction(applicationManager, "CentiscapeGSoC2017" + VERSION, this);

        registerAllServices(context, menuAction, new Properties());
    }
    
    public CyServiceRegistrar getServiceRegistrar() {
        return serviceRegistrar;
    }

    public CyApplicationManager getApplicationManager() {
        return applicationManager;
    }

    public CySwingApplication getDesktopService() {
    	return desktopService;
    }

    public CentiScaPeMenuAction getMenuAction() {
        return menuAction;
    }   
    
    public CyNetworkManager getNetworkManager() {
        return networkManager;
    }
    
    public CyNetworkFactory getNetworkFactory() {
        return networkFactory;
    }
    
    public CyNetworkViewFactory getNetworkViewFactory() {
        return networkViewFactory;
    }
    public CyNetworkViewManager getNetworkViewManager() {
        return networkViewManager;
    }
}