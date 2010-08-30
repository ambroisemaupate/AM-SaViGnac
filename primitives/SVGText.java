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
public class SVGText implements amsavignac.document.SVGElement {
   
   private java.awt.Point origin;
   private String text;
   private String textID;

   public SVGText ( int x, int y, String text ) {

      this.origin = new java.awt.Point(x, y);
      this.text = text;

      this.textID = "text"+(int)(Math.random()*100000);

      System.out.println("New SVGText : ID="+this.getElementID());
      
   }

   public Element getElement() {

      Element textElement = new Element("text", amsavignac.document.SVGDocument.namespace);
      textElement.setAttribute("x", ""+this.getX());
      textElement.setAttribute("y", ""+this.getY());
      textElement.setAttribute("id", this.getElementID());
      textElement.setText(this.text);

      return textElement;
   }

   public String getElementID() {

      return this.textID;
   }

   public int getX () {
      return this.origin.x;
   }
   public int getY () {
      return this.origin.y;
   }
   
}
