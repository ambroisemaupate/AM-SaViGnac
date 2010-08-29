/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package amsavignac.primitives;
import org.jdom.Element;

/**
 *
 * @author ambroisemaupate
 */
public class SVGPolygon extends java.awt.Polygon implements amsavignac.document.SVGElement {

   private String polyID;

   public SVGPolygon (int[] xpoints, int[] ypoints, int npoints) {
      super(xpoints, ypoints, npoints);
      this.polyID = "polygon"+(int)(Math.random()*100000);
      System.out.println("New SVGPolygon : ID="+this.getElementID());
   }
   public Element getElement() {

      Element poly = new Element("polygon", amsavignac.document.SVGDocument.namespace);

      String points = "";
      
      for (int i=0; i < this.npoints; i++) {
         points += this.xpoints[i]+","+this.ypoints[i]+ " ";
      }

      poly.setAttribute("points", points);

      poly.setAttribute("id", ""+this.getElementID());

      return poly;
   }

   /**
    * @return the polyID
    */
   public String getElementID ()
   {
      return polyID;
   }
}
