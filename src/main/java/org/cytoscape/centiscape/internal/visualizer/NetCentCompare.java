/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cytoscape.centiscape.internal.visualizer;

import java.util.Comparator;

/**
 *
 * @author Andrea Pegoraro
 */
public class NetCentCompare implements Comparator{
    
    public int compare(Object a , Object b) {
         NetCent elementa = (NetCent)a;
         NetCent elementb = (NetCent)b;
         int result;
         result =elementb.getCent().compareTo(elementa.getCent());
         return result;
    } 
    
}
