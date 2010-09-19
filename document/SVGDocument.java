
package amsavignac.document;

import amsavignac.primitives.*;

import java.io.*;
import org.jdom.*;
import org.jdom.output.*;

import java.awt.*;

/**
 * Describe a single SVGDocument, this is the main class for SVG Exporting
 * The base class is {@link #java.awt.Graphics2D java.awt.Graphics2D}
 * @author ambroisemaupate
 */
public class SVGDocument extends java.awt.Graphics2D {

   /* ----------- DOM ELEMENTS ------------*/
   /**
    * The namespace is added to all the DOM elements.
    * It defines the SVG namespace used by extern applications to recognize the SVG format.
    */
   public static Namespace namespace = Namespace.getNamespace("http://www.w3.org/2000/svg");
   
   private Element root;
   private Element currentElement;
   private org.jdom.Document document;

   /* ----------- GRAPHICS ----------------*/
   private String svgUnit;

   private Paint currentPaint;
   private Color currentColor;
   private Font currentFont;

   /**
    * Create a new SVGDocument
    * @param unit A String which describe the Unit system used ( "px", "mm", "cm", ...)
    */
   public SVGDocument ( String unit) {
      super();

      this.svgUnit = unit;
      
      this.root = new Element("svg", SVGDocument.namespace);
      this.currentElement = this.root;
      this.document = new Document(root);

      this.currentColor = Color.black;
      this.currentFont = Font.getFont(Font.MONOSPACED);

      this.currentPaint = this.currentColor;
      
   }

   /**
    * Set the global svg parameters, must be called after the SVGDocument creation
    * @param width the svg canvas width
    * @param height the svg canvas height
    */
   public void setSVGView( float width, float height ) {

      this.root.setAttribute("width", width+this.getSVGUnit());
      this.root.setAttribute("height", height+this.getSVGUnit());
      this.root.setAttribute("viewBox", "0"+this.getSVGUnit()+" 0"+this.getSVGUnit()+" "+width+this.getSVGUnit()+" "+height+this.getSVGUnit());

      this.root.setAttribute("version", "1.1");

      this.getDOM().setDocType(new DocType("svg","-//W3C//DTD SVG 1.1//EN", "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd"));
   }

   /**
    * Add a SVG group in the current element and makes it the new current element of the DOM
    * @return The group element
    */
   public Element addGroup ( ) {

      Element group = new Element("g", SVGDocument.namespace);
      this.getCurrentElement().addContent(group);
      this.setCurrentElement(group);
      return group;
   }

   /**
    * Add a SVG group in the given element and makes it the new current element of the DOM
    * @param parent The parent element
    * @return The group element
    */
   public Element addGroup ( Element parent ) {

      Element group = new Element("g", SVGDocument.namespace);
      parent.addContent(group);
      this.setCurrentElement(group);

      return group;
   }

   /**
    * Add a SVG group in the given element and makes it the new current element of the DOM with a description
    * @param description The description
    * @return the created group
    */
   public Element addGroup ( String description ) {

      Element group = new Element("g", SVGDocument.namespace);
      this.currentElement.addContent(group);
      this.setCurrentElement(group);

      this.addDescription(description);

      return group;
   }

   /**
    * Close the current element, if it is a group element <g></g>
    * @return the parent element of the closed group or NULL
    */
   public Element closeGroup ( ) {
      
      if (this.currentElement.getName().equals("g")) {
         this.currentElement = this.currentElement.getParentElement();
         return this.currentElement;
      }
      return null;
   }

   /**
    * Add a svg description element in the current DOM element
    * @param description A String description
    * @return the created element
    */
   public Element addDescription( String description ) {

      Element desc = new Element("desc", SVGDocument.namespace);
      desc.addContent(description);

      this.getCurrentElement().addContent(desc);

      return desc;
   }

   /**
    * Add a svg description element in the given element
    * @param parent The parent element
    * @param description A String description
    * @return The created element
    */
   public Element addDescription( Element parent, String description ) {

      Element desc = new Element("desc", SVGDocument.namespace);
      desc.addContent(description);

      parent.addContent(desc);

      return desc;
   }

   /**
    * Add an element into the DOM at the currentElement position
    * @param element
    * @return
    */
   public Element addElement( Element element ) {
      
      this.getCurrentElement().addContent(element);

      return element;
   }

   /**
    * Add an element into the DOM in the given parent
    * @param parent
    * @param element
    * @return
    */
   public Element addElement( Element parent, Element element ) {

      parent.addContent(element);

      return element;
   }

   /**
    * Return an element with its ID attribute
    * @param id
    * @param root This element perform the recurency of this method
    * @return
    */
   public Element getElementByID( String id, Element root ){

      //java.util.List children = root.getChildren();
      //java.util.Iterator it = children.iterator();
      java.util.Iterator it = root.getDescendants(new org.jdom.filter.ElementFilter());

      while (it.hasNext() == true){

         Element element = (Element)it.next();
         //System.out.print("\n"+element+element.getAttributes());

         if (element.getAttributeValue("id") != null){
            if (element.getAttributeValue("id").equals(id)){
               System.out.println("\n"+element+element.getAttributes());
               return element;
            }
         }
      }
      
      return null;
   }

   /**
    * Return an element with its ID attribute
    * @param id
    * @return
    */
   public Element getElementByID( String id ){

      System.out.println("Begin searching in the DOM Tree ----------- ID = "+id);
      return this.getElementByID(id, this.getDOM().getRootElement());
   }
   /**
    * Apply current painting parameters on the given element stroke (Color or Gradient)
    * @param element
    * @param strokeWidth
    */
   private void applyPaintOnStroke( Element element, int strokeWidth ){

      System.out.println("Apply Paint ------------");
      if (element != null){
         if (this.getPaint().getClass() == Color.class) {
            Color c = (Color)this.getPaint();
            element.setAttribute("stroke", "rgb("+c.getRed()+","+c.getGreen()+","+c.getBlue()+")");
            element.setAttribute("stroke-width", ""+strokeWidth);
         }
         else if (this.getPaint().getClass() == GradientPaint.class) {

            GradientPaint gp = (GradientPaint)this.getPaint();

            // Create the gradient element
            int randId = (int)Math.random()*10000;
            Element defines = new Element("defs", SVGDocument.namespace);
            Element gradient = new Element("linearGradient", SVGDocument.namespace);
            gradient.setAttribute("id", ""+randId);
            gradient.setAttribute("gradientUnits", "userSpaceOnUse");
            gradient.setAttribute("x1", ""+gp.getPoint1().getX());
            gradient.setAttribute("y1", ""+gp.getPoint1().getY());
            gradient.setAttribute("x2", ""+gp.getPoint2().getX());
            gradient.setAttribute("y2", ""+gp.getPoint2().getY());

               Element stop1 = new Element("stop", SVGDocument.namespace);
               Color c1 = gp.getColor1();
               stop1.setAttribute("offset", "0%");
               stop1.setAttribute("stop-color", "rgb("+c1.getRed()+","+c1.getGreen()+","+c1.getBlue()+")");

               Element stop2 = new Element("stop", SVGDocument.namespace);
               Color c2 = gp.getColor2();
               stop1.setAttribute("offset", "100%");
               stop1.setAttribute("stop-color", "rgb("+c2.getRed()+","+c2.getGreen()+","+c2.getBlue()+")");

               gradient.addContent(stop1);
               gradient.addContent(stop2);

            defines.addContent(gradient);
            element.getParentElement().addContent(defines);

            element.setAttribute("stroke", "url(#"+randId+")");
            element.setAttribute("stroke-width", ""+strokeWidth);
         }
      }
      else {
         System.out.println("WARNING : Element is null");
      }
   }

   /**
    * Apply current painting parameters on the given element filling (Color or Gradient)
    * @param element
    */
   private void applyPaintOnFill( Element element ){

      System.out.println("Apply Paint ------------");

      if (element != null){
         if (this.getPaint().getClass() == Color.class) {
            Color c = (Color)this.getPaint();
            element.setAttribute("fill", "rgb("+c.getRed()+","+c.getGreen()+","+c.getBlue()+")");
         }
         else if (this.getPaint().getClass() == GradientPaint.class) {

            GradientPaint gp = (GradientPaint)this.getPaint();

            // Create the gradient element
            String randId = "gradient"+(int)(Math.random()*100000);
            Element defines = new Element("defs", SVGDocument.namespace);
            Element gradient = new Element("linearGradient", SVGDocument.namespace);
            gradient.setAttribute("id", ""+randId);
            gradient.setAttribute("gradientUnits", "userSpaceOnUse");
            gradient.setAttribute("x1", ""+gp.getPoint1().getX()+this.getSVGUnit());
            gradient.setAttribute("y1", ""+gp.getPoint1().getY()+this.getSVGUnit());
            gradient.setAttribute("x2", ""+gp.getPoint2().getX()+this.getSVGUnit());
            gradient.setAttribute("y2", ""+gp.getPoint2().getY()+this.getSVGUnit());

               Element stop1 = new Element("stop", SVGDocument.namespace);
               Color c1 = gp.getColor1();
               stop1.setAttribute("offset", "0%");
               stop1.setAttribute("stop-color", "rgb("+c1.getRed()+","+c1.getGreen()+","+c1.getBlue()+")");

               Element stop2 = new Element("stop", SVGDocument.namespace);
               Color c2 = gp.getColor2();
               stop2.setAttribute("offset", "100%");
               stop2.setAttribute("stop-color", "rgb("+c2.getRed()+","+c2.getGreen()+","+c2.getBlue()+")");

               gradient.addContent(stop1);
               gradient.addContent(stop2);

            defines.addContent(gradient);

            if (element.getParentElement() != null){
               element.getParentElement().addContent(defines);
            }
            else{
               System.err.println(element.toString()+" : doesn't have any parent element !");
            }

            element.setAttribute("fill", "url(#"+randId+")");
         }
      }
      else {
         System.out.println("WARNING : Element is null");
      }
   }

   /**
    * Apply Font parameters to the given element - the Font parameters can be found with the setFont() or getFont() methods
    * @param element
    */
   private void applyFontOnText( Element element ) {

      System.out.println("Apply Font ------------");

      if (element != null){
         if ( this.getFont() != null ) {
           
            element.setAttribute("font-family", ""+this.getFont().getFamily());
            element.setAttribute("font-size", ""+this.getFont().getSize()+"pt");
            
            switch (this.getFont().getStyle()) {
               
               case Font.BOLD :

                  element.setAttribute("font-weight", "bold");
                  break;

               case Font.ITALIC :

                  element.setAttribute("font-style", "italic");
                  break;

               case Font.BOLD & Font.ITALIC :
                  
                  element.setAttribute("font-weight", "bold");
                  element.setAttribute("font-style", "italic");
                  break;

               default:
                  break;
                  
            }
           
         }
      }
      else {
         System.out.println("WARNING : Element is null");
      }
   }
   /**
    * Write the SVG file into the given path
    * @param file Path to the writen file
    */
   public void writeFile( String file ) {
      try
      {
         //On utilise ici un affichage classique avec getPrettyFormat()
         XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
         //Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
         //avec en argument le nom du fichier pour effectuer la sérialisation.
         sortie.output(this.getDOM(), new FileOutputStream(file));
      }
      catch (java.io.IOException e){
         System.out.println(e.getMessage());
      }
   }

   /**
    * Return the whole DOM tree
    * @return the document tree
    */
   public org.jdom.Document getDOM () {
      return document;
   }


   
   /* ---------------- IMPLEMENTATION OF GRAPHICS 2D ---------------------- */
   public void addRenderingHints(java.util.Map hints) {

   }
   public void clip (java.awt.Shape shape) {
      
   }
   public void draw (java.awt.Shape shape) {
      
   }

   public void drawGlyphVector(java.awt.font.GlyphVector g, float x, float y) {
      
   }
   public void drawImage(java.awt.image.BufferedImage img, java.awt.image.BufferedImageOp op, int x, int y) {

   }
   public boolean drawImage(Image img, java.awt.geom.AffineTransform xform, java.awt.image.ImageObserver obs) {
      return false;
   }

   public void drawRenderableImage(java.awt.image.renderable.RenderableImage img, java.awt.geom.AffineTransform xform) {

   }
   public void drawRenderedImage(java.awt.image.RenderedImage img, java.awt.geom.AffineTransform xform) {

   }
   public void drawString(java.text.AttributedCharacterIterator iterator, float x, float y) {

   }
   public void drawString(java.text.AttributedCharacterIterator iterator, int x, int y) {

   }

   public void drawString(String s, float x, float y) {

      SVGText svgtext = new SVGText((int)x, (int)y, s);
      this.getCurrentElement().addContent(svgtext.getElement());
      this.applyPaintOnFill( this.getElementByID(svgtext.getElementID()) );
      this.applyFontOnText( this.getElementByID(svgtext.getElementID()) );
   }

   public void drawString(String str, int x, int y) {

      SVGText svgtext = new SVGText(x, y, str);
      this.getCurrentElement().addContent(svgtext.getElement());
      this.applyPaintOnFill( this.getElementByID(svgtext.getElementID()) );
      this.applyFontOnText( this.getElementByID(svgtext.getElementID()) );
      
   }
   public void fill(Shape s) {
      
   }

   public Color	getBackground() {
      return null;
   }
   public Composite getComposite() {
      return null;
   }
   public GraphicsConfiguration	getDeviceConfiguration() {
      return null;
   }

   public java.awt.font.FontRenderContext getFontRenderContext() {
      return new java.awt.font.FontRenderContext(new java.awt.geom.AffineTransform(), true, true);
   }
   
   public Paint	getPaint() {
      return this.currentPaint;
   }
   public Object getRenderingHint(RenderingHints.Key hintKey) {
      return null;
   }
   public RenderingHints getRenderingHints() {
      return null;
   }
   public Stroke getStroke() {
      return null;
   }
   public java.awt.geom.AffineTransform getTransform() {
      return null;
   }
   public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
      return false;
   }

   /**
    * Rotate a group of SVG Elements
    * @param theta Angle in DEGREES
    */
   public void	rotate(double theta) {
      if (this.getCurrentElement().getName().equals("g")){

         this.getCurrentElement().setAttribute("transform", "rotate("+theta+")");
      }
      else {
         this.addGroup();
         this.rotate(theta);
      }
   }

   /**
    * Combine a rotation with a translation
    * @param theta
    * @param x
    * @param y
    * @see SVGDocument.rotate
    * @see SVGDocument.translate
    */
   public void	rotate(double theta, double x, double y) {
      if (this.getCurrentElement().getName().equals("g")){
         
         this.getCurrentElement().setAttribute("transform", "rotate("+theta+") translate("+x+this.getSVGUnit()+","+y+this.getSVGUnit()+")");
      }
      else {
         this.addGroup();
         this.rotate(theta, x, y);
      }
   }

   /**
    * Scale a group of SVG Elements - sx and sy must be equal for a homothetic scale
    * @param sx Xsscale factor
    * @param sy Y scale factor
    */
   public void	scale(double sx, double sy) {
      if (this.getCurrentElement().getName().equals("g")){
         
         this.getCurrentElement().setAttribute("transform", "scale("+sx+","+sy+")");
      }
      else {
         this.addGroup();
         this.scale(sx, sy);
      }
   }
   public void	setBackground(Color color) {

   }
   public void	setComposite(Composite comp) {

   }

   public void	setPaint(Paint paint) {
      this.currentPaint = paint;
   }
   public void	setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {

   }
   public void	setRenderingHints(java.util.Map hints) {

   }
   public void	setStroke(Stroke s) {

   }
   public void	setTransform(java.awt.geom.AffineTransform Tx) {

   }
   public void	shear(double shx, double shy) {

   }
   public void	transform(java.awt.geom.AffineTransform Tx) {

   }
   public void	translate(double tx, double ty) {

   }

   /**
    * Translate a group of SVG Elements
    * @param x
    * @param y
    */
   public void	translate(int x, int y) {
      if (this.getCurrentElement().getName().equals("g")){
         //transform="translate(-10,-20)"
         this.getCurrentElement().setAttribute("transform", "translate("+x+this.getSVGUnit()+","+y+this.getSVGUnit()+")");
      }
      else {
         this.addGroup();
         this.translate(x, y);
      }
   }

   /* --------------------- IMPLEMENTATION OF GRAPHICS ------------------------*/
   public void clearRect(int x, int y, int width, int height) {

   }
   public void clipRect(int x, int y, int width, int height) {

   }
   public void copyArea(int x, int y, int width, int height, int dx, int dy) {

   }
   public Graphics create() {
      return this;
   }
   public void dispose() {

   }

   /* ---------------------- DRAWING METHODS ---------------------------------- */
   public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
      
   }
   public boolean drawImage(Image img, int x, int y, Color bgcolor, java.awt.image.ImageObserver observer) {
      return false;
   }
   public boolean drawImage(Image img, int x, int y, java.awt.image.ImageObserver observer) {
      return false;
   }
   public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, java.awt.image.ImageObserver observer) {
      return false;
   }
   public boolean drawImage(Image img, int x, int y, int width, int height, java.awt.image.ImageObserver observer) {
      return false;
   }
   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, java.awt.image.ImageObserver observer) {
      return false;
   }
   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, java.awt.image.ImageObserver observer) {
      return false;
   }
   public void drawLine(int x1, int y1, int x2, int y2) {

   }

   /**
    * Draw a SVG Oval via SVGEllipse
    * @param x
    * @param y
    * @param width
    * @param height
    */
   public void drawOval(int x, int y, int width, int height) {
      SVGEllipse oval = new SVGEllipse(x + (width/2), y + (height/2), width/2, height/2);
      this.getCurrentElement().addContent(oval.getElement());
      this.applyPaintOnStroke( this.getElementByID(oval.getElementID()), 1 );
   }

   /**
    * Draw a SVG Polygon
    * @param xPoints
    * @param yPoints
    * @param nPoints
    */
   public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
      SVGPolygon poly = new SVGPolygon(xPoints, yPoints, nPoints);

      Element el = this.getCurrentElement().addContent( poly.getElement() );
      el.setAttribute("fill", "none");

      this.applyPaintOnStroke(this.getElementByID(poly.getElementID()), 1);
   }

   public void	drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {

   }
   public void	drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

   }

   /**
    * Draw a SVG Rectangle
    * @param x
    * @param y
    * @param width
    * @param height
    */
   @Override
   public void	drawRect(int x, int y, int width, int height) {
      
      SVGRectangle rect = new SVGRectangle(x, y, width, height);
      
      Element el = this.getCurrentElement().addContent( rect.getElement() );
      el.setAttribute("fill", "none");

      this.applyPaintOnStroke(this.getElementByID(rect.getElementID()), 1);
   }

   public void	fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

   }

   /**
    * Fill a SVG Oval via SVGEllipse
    * @param x
    * @param y
    * @param width
    * @param height
    */
   public void fillOval(int x, int y, int width, int height) {
      SVGEllipse oval = new SVGEllipse(x + (width/2), y + (height/2), width/2, height/2);
      this.getCurrentElement().addContent(oval.getElement());
      this.applyPaintOnFill( this.getElementByID(oval.getElementID()) );
   }

   /**
    * Fill a SVG Polygon
    * @param xPoints
    * @param yPoints
    * @param nPoints
    */
   public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {

      SVGPolygon poly = new SVGPolygon(xPoints, yPoints, nPoints);
      this.getCurrentElement().addContent(poly.getElement());
      this.applyPaintOnFill( this.getElementByID(poly.getElementID()) );
   }

   /**
    * Fill a rectangle with given parameters<br />
    * Filling parameters have to be setted up by the {@link SVGDocument.setPaint setPaint} method
    * @param x
    * @param y
    * @param width
    * @param height
    * @see SVGDocument.setPaint
    */
   public void	fillRect(int x, int y, int width, int height) {
      
      SVGRectangle rect = new SVGRectangle(x, y, width, height);
      this.getCurrentElement().addContent( rect.getElement() );

      //System.out.println(this.getElementByID(rect.getElementID()).toString());
      this.applyPaintOnFill( this.getElementByID(rect.getElementID()) );
   }
   public void	fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

   }
   public Shape	getClip() {
      return null;
   }
   public Rectangle getClipBounds() {
      return null;
   }
   public Color	getColor() {
      return this.currentColor;
   }
   public Font	getFont() {
      return this.currentFont;
   }
   public FontMetrics getFontMetrics(Font f) {
      return null;
   }
   public void	setClip(int x, int y, int width, int height) {

   }
   public void	setClip(Shape clip) {

   }
   public void	setColor(Color c) {
      this.currentColor = c;
      this.setPaint(this.currentColor);
   }
   public void	setFont(Font font) {
      this.currentFont = font;
   }
   public void	setPaintMode(){

   }
   public void	setXORMode(Color c1){

   }

   /**
    * @return the currentElement
    */
   public Element getCurrentElement ()
   {
      return currentElement;
   }

   /**
    * @param currentElement the currentElement to set
    */
   public void setCurrentElement ( Element currentElement )
   {
      this.currentElement = currentElement;
   }

   /**
    * @return the svgUnit
    */
   public String getSVGUnit ()
   {
      return svgUnit;
   }
}


