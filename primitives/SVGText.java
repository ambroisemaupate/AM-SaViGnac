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

      return null;
   }

   public String getElementID() {

      return this.textID;
   }
}
