/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package amsavignac.primitives;

import org.jdom.*;

/**
 * Describe a SVG Rectangle
 * @author ambroisemaupate
 */
public class SVGRectangle extends java.awt.Rectangle implements amsavignac.document.SVGElement {

   private String rectId;

   /**
    * Create a new SVG Rectangle with its position and dimensions
    * @param x The top-left point X coordinate
    * @param y the top-left point Y coordinate
    * @param width The rectangle width
    * @param height The rectangle height
    */
   public SVGRectangle( int x, int y, int width, int height ) {
      super(x, y, width, height );

      this.rectId = "rectangle"+(int)(Math.random()*100000);

      System.out.println("New SVGRectangle : ID="+this.getElementID());
   }

   /**
    * Return a rectangle DOM Element with dimension and position attributes and an unique ID
    * @return A DOM Element
    */
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
    * Return the rectangle unique ID
    * @return the rectId
    */
   public String getElementID ()
   {
      return rectId;
   }
}
