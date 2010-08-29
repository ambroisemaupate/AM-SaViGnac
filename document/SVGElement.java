/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package amsavignac.document;

/**
 *
 * @author ambroisemaupate
 */
public interface SVGElement {
   /**
    * Return the DOM element corresponding to the current svg object
    * @return
    */
   public org.jdom.Element getElement();

   /**
    * Return the element unique id
    * @return
    */
   public String getElementID();
}
