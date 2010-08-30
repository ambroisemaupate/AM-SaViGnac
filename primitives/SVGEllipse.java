/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package amsavignac.primitives;

import org.jdom.Element;

/**
 * Describe a SVG Ellipse
 * @author ambroisemaupate
 */
public class SVGEllipse extends java.awt.Rectangle implements amsavignac.document.SVGElement {

   private String ellipseID;
   
   /**
    * Center coordinates and Radius lengths
    */
   private int cx, cy, rx, ry;

   /**
    * Create a new SVG Ellipse
    * @param cx The center X of the ellipse
    * @param cy The center Y of the ellipse
    * @param rx The X radius of the ellipse
    * @param ry The Y radius of the ellipse
    */
   public SVGEllipse ( int cx, int cy, int rx, int ry ) {
      super(cx-rx, cy-ry, rx, ry);
      this.cx = cx;
      this.cy = cy;
      this.rx = rx;
      this.ry = ry;

      this.ellipseID = "ellipse"+(int)(Math.random()*100000);

      System.out.println("New SVGRectangle : ID="+this.getElementID());
   }

   /**
    * Return an ellipse DOM Element with dimension and position attributes and an unique ID
    * @return A DOM Element
    */
   public Element getElement() {

      Element rectElement = new Element("ellipse", amsavignac.document.SVGDocument.namespace);
      rectElement.setAttribute("cx", ""+this.cx);
      rectElement.setAttribute("cy", ""+this.cy);
      rectElement.setAttribute("rx", ""+this.rx);
      rectElement.setAttribute("ry", ""+this.ry);
      rectElement.setAttribute("id", this.getElementID());

      return rectElement;
   }
   

   public void setRy ( int ry )
   {
      this.ry = ry;
      this.height = ry*2;
      this.y = this.cy - ry;
   }

   public void setRx ( int rx )
   {
      this.rx = rx;
      this.width = rx*2;
      this.x = this.cx - rx;
   }

   public void setCy ( int cy )
   {
      this.cy = cy;
      this.y = cy - this.ry;
   }

   public void setCx ( int cx )
   {
      this.cx = cx;
      this.x = cx - this.rx;
   }
   

   public void setHeight ( int height )
   {
      this.height = height;
      this.ry = height/2;
      this.cy = this.y+this.ry;
   }

   public void setWidth ( int width )
   {
      this.width = width;
      this.rx = width/2;
      this.cx = this.x+this.rx;
   }

   public void setY ( int y )
   {
      this.y = y;
      this.cy = this.y+this.ry;
   }

   public void setX ( int x )
   {
      this.x = x;
      this.cx = this.x+this.rx;
   }

   /**
    * Return the ellipse unique ID
    * @return the rectId
    */
   public String getElementID ()
   {
      return this.ellipseID;
   }

}
