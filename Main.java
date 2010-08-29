/**
 * Here is a test app for the SaViGnac API
 */
package amsavignac;

import amsavignac.document.SVGDocument;
import java.awt.Color;

/**
 * Main class for the testing app
 * @author ambroisemaupate
 */

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

       SVGDocument svgW = new SVGDocument("px");

       svgW.setSVGView(300, 300);
       svgW.addDescription("This is a svg file");

       svgW.addGroup();
       svgW.addDescription("This is a svg group");
       svgW.rotate(30, 50,50);

       svgW.setPaint(new java.awt.GradientPaint(0,0,Color.red, 100, 100, Color.blue));
       svgW.fillRect( 0, 0, 100, 100 );
       svgW.setPaint(java.awt.Color.MAGENTA);
       svgW.drawRect( 0, 0, 100, 100 );

       svgW.setPaint(java.awt.Color.MAGENTA);
       svgW.drawRect( 200, 100, 50, 150 );

       svgW.closeGroup();
       svgW.addGroup("This is an other group");


       svgW.writeFile("test.svg");
    }

}
