/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package amsavignac.primitives;

import org.jdom.*;

/**
 *
 * @author ambroisemaupate
 */
public class SVGRectangle extends java.awt.Rectangle implements amsavignac.document.SVGElement {

   private String rectId;

   public SVGRectangle( int x, int y, int width, int height ) {
      super(x, y, width, height );

      this.rectId = "rectangle"+(int)(Math.random()*100000);

      System.out.println("New SVGRectangle : ID="+this.getElementID());
   }

   public Element getElement() {

      Element rectElement = new Element("rect", amsavignac.document.SVGDocument.namespace);
      rectElement.setAttribute("x", ""+this.getX());
      rectElement.setAttribute("y", ""+this.getY());
      rectElement.setAttribute("width", ""+this.getWidth());
      rectElement.setAttribute("height", ""+this.getHeight());
      rectElement.setAttribute("id", this.getElementID());

      return rectElement;
   }

   /**
    * @return the rectId
    */
   public String getElementID ()
   {
      return rectId;
   }
}
