/*
 * EccentricityMethods.java
 *
 * Created on 7 dicembre 2007, 16.29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.cytoscape.centiscape.internal.Eccentricity;

import java.util.Vector;
import org.cytoscape.centiscape.internal.CentiScaPeMultiSPath;
import org.cytoscape.centiscape.internal.CentiScaPeShortestPathList;
import org.cytoscape.model.CyNode;


/**
 *
 * @author scardoni
 */
public class EccentricityMethods {
    
    /** Creates a new instance of EccentricityMethods */
    public EccentricityMethods() {
    }
    
    public static FinalResultEccentricity CalculateEccentricity(Vector SingleShortestPathVector,CyNode CurrentRoot ) {
           
        
             int currentmaxvalue=0;
             
                 CentiScaPeShortestPathList CurrentEccentricityList;
                 int distance =0;
                 FinalResultEccentricity CurrentEccentricity = new FinalResultEccentricity(CurrentRoot,0);
                 for (int j = 0; j < SingleShortestPathVector.size(); j++) {
                 CurrentEccentricityList = (CentiScaPeShortestPathList)SingleShortestPathVector.elementAt(j);
                 distance = ((CentiScaPeMultiSPath)CurrentEccentricityList.getLast()).getCost();
                 currentmaxvalue = Math.max(currentmaxvalue,distance);
               //  JOptionPane.showMessageDialog(view.getComponent(),
               //    "calcolato maxvalue eccentricity " );
              
          }
                 double currenteccentricityvalue =0;
                 //int numSingleShortestPaths = SingleShortestPathVector.size();
                 if (!SingleShortestPathVector.isEmpty()){
                        currenteccentricityvalue= (1/(double)currentmaxvalue);
                    }
          CurrentEccentricity.update(currenteccentricityvalue);
          return CurrentEccentricity;
           
       }
    
}
