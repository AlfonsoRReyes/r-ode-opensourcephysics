/*
 * The org.opensourcephysics.demoapps package contains longer programs that demonstrate
 * how to use various Open Source Physics tools.
 * These examples are proof-of-concept only.
 *
 * Copyright (c) 2007  W. Christian.
 */
package org.opensourcephysics.davidson.ode.flow;
import org.opensourcephysics.controls.*;
import org.opensourcephysics.display.*;
import org.opensourcephysics.display2d.*;
import org.opensourcephysics.numerics.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * A vector field demonstration.
 * @author Wolfgang Christian
 * @version 1.0
 */
public class FlowLineApp extends AbstractSimulation implements InteractiveMouseHandler, PropertyChangeListener {

   PlottingPanel drawingPanel = new PlottingPanel("x", "y", null);

   /** Field drawingFrame */
   DrawingFrame drawingFrame = new DrawingFrame(drawingPanel);
   VectorPlot vectorfield;
   GridPointData vecdataset;
   DrawableBuffer drawableBuffer;
   String fxStr = "10*y*y", fyStr = "10*x";
   SuryonoParser fxParser;
   SuryonoParser fyParser;
   DecimalFormat scientificFormat = new DecimalFormat("0.##E0");
   DecimalFormat decimalFormat = new DecimalFormat("0.00");
   FlowLine fl;
   boolean showPaths = false;
   String solverName = "multistep";
   double dt = 0.1;
   double xmin=-10, xmax=10, ymin=-10, ymax=10;
   boolean periodicx=false, periodicy=false;
   CatControl cat;
   Color flowColor=Color.RED;

   /**
    * Constructor FlowLineApp
    *
    */
   public FlowLineApp() {
      try {
         fxParser = new SuryonoParser(fxStr, "x", "y");
      } catch(ParserException ex) {
         control.println(ex.getMessage());
      }
      try {
         fyParser = new SuryonoParser(fyStr, "x", "y");
      } catch(ParserException ex) {
         control.println(ex.getMessage());
      }
      drawingPanel.setInteractiveMouseHandler(this);
      drawingPanel.setAutoscaleX(true);
      drawingPanel.setAutoscaleY(true);
      drawingPanel.setSquareAspect(false);
      drawingPanel.setPreferredSize(new Dimension(300, 300));
      drawableBuffer = new DrawableBuffer();
      drawingPanel.addDrawable(drawableBuffer);
      drawingFrame.setAnimated(true);
   }

   /**
    * Starts the calculation.
    *
    * @param size
    */
   public void initField(int gridsize) {
      drawableBuffer.clear();
      vecdataset = new GridPointData(gridsize, gridsize, 3);
      vecdataset.setScale(xmin, xmax, ymin, ymax);
      vectorfield = new VectorPlot(vecdataset);
      drawableBuffer.addDrawable(vectorfield);
      drawingPanel.getAxes().setInteriorBackground(null);
      drawingPanel.getAxes().setShowMajorXGrid(false);
      drawingPanel.getAxes().setShowMajorYGrid(false);
      drawableBuffer.addDrawable(drawingPanel.getAxes());
      //vectorfield.setAutoscaleZ(true,0,0);  // the default
   }

   /**
    * Responds to property changes such as loading xml files by reinitializing the program.
    *
    * @param evt PropertyChangeEvent
    */
   public void propertyChange(PropertyChangeEvent evt) {
      boolean running = isRunning();
      if(running) {
         stopSimulation();
      }
      initialize();
      if(running) {
         startSimulation();
      }
   }

   /**
    * Method sampleField
    *
    */
   public void sampleField() {
      double[][][] data = vecdataset.getData();
      fxStr = control.getString("fx");
      fyStr = control.getString("fy");
      try {
         fxParser.setFunction(fxStr);
      } catch(ParserException e) {
         control.println(e.getMessage());
      }
      try {
         fyParser.setFunction(fyStr);
      } catch(ParserException e) {
         control.println(e.getMessage());
      }
      int rnum = data.length;
      int cnum = data[0].length;
      for(int i = 0; i<rnum; i++) {
         for(int j = 0; j<cnum; j++) {
            double a = fxParser.evaluate(data[i][j][0], data[i][j][1]);
            double b = fyParser.evaluate(data[i][j][0], data[i][j][1]);
            double mag = Math.sqrt(a*a+b*b);
            data[i][j][2] = mag;
            data[i][j][3] = a/mag;
            data[i][j][4] = b/mag;
         }
      }
      vectorfield.update();
   }

   /**
    * Method reset
    *
    */
   public void reset() {
      stopSimulation();
      drawingPanel.removeObjectsOfClass(FlowLine.class);
      control.setValue("ODE Solver", "multistep");
      control.setValue("dt", 0.1);
      control.setValue("adaptive solver tolerance", 1e-3);
      control.setValue("fx", "y");
      control.setValue("fy", "-sin(x)");
      control.setValue("x min", "-pi");
      control.setValue("x max", "pi");
      control.setValue("periodic x", true);
      control.setValue("y min", -3);
      control.setValue("y max", 3);
      control.setValue("periodic y", false);
      control.setValue("size", 24);
      control.setValue("show paths", false);
      control.setValue("show field", true);
      control.setValue("flow color", Color.RED);
      enableStepsPerDisplay(true);
      initialize();
      makeCat(xmin + 0.6*(xmax-xmin), (ymax+ymin)/2.0, 0.1*(xmax-xmin),32);
      drawingPanel.setMessage("Click-drag to create new states.", 0);
   }

   /**
    * Removes flow lines.
    */
   public void clearFlow(){
      drawingPanel.removeObjectsOfClass(FlowLine.class);
      drawingPanel.repaint();
   }

   void makeCat(double x0, double y0, double r, int n){
      solverName = control.getString("ODE Solver");
      flowColor = (Color) control.getObject("flow color");
      for(double theta=0, dtheta= 2*Math.PI/n; theta<2*Math.PI; theta+= dtheta){
         double x=x0+r*Math.sin(theta);
         double y=y0+r*Math.cos(theta);
         FlowLine fl = new FlowLine(x, y, this);
         fl.circle.color=flowColor;
         drawingPanel.addDrawable(fl);
      }
      for (double theta = -Math.PI/4, dtheta = Math.PI/n; theta<Math.PI/4; theta += dtheta){
         double x = x0+0.5*r*Math.sin(theta);
         double y = y0+0.5*r*Math.cos(theta);
         FlowLine fl = new FlowLine(x, -y, this);
         fl.circle.color=flowColor.darker().darker();
         drawingPanel.addDrawable(fl);
      }
      FlowLine fl = new FlowLine(x0, y0+0.3*r, this);
      fl.circle.color=flowColor.darker().darker();
      drawingPanel.addDrawable(fl);
      fl = new FlowLine(x0-0.3*r, y0+0.6*r, this);
      fl.circle.color=flowColor.darker().darker();
      drawingPanel.addDrawable(fl);
      fl = new FlowLine(x0+0.3*r, y0+0.6*r, this);
      fl.circle.color=flowColor.darker().darker();
      drawingPanel.addDrawable(fl);
      drawingPanel.repaint();
   }

    public void catControl(){
       if(cat==null){
          cat=new CatControl(this);
       }
       cat.setVisible(xmin + 0.6*(xmax-xmin), (ymax+ymin)/2.0, 0.1*(xmax-xmin),32);
    }

   /**
    * Initializes the program.
    */
   public void initialize() {
      solverName = control.getString("ODE Solver");
      dt = control.getDouble("dt");
      xmin = control.getDouble("x min");
      xmax = control.getDouble("x max");
      periodicx = control.getBoolean("periodic x");
      ymin = control.getDouble("y min");
      ymax = control.getDouble("y max");
      periodicy = control.getBoolean("periodic y");
      initField(control.getInt("size"));
      sampleField();
      drawableBuffer.invalidateImage();
      drawableBuffer.setVisible(control.getBoolean("show field"));
      showPaths = control.getBoolean("show paths");
      flowColor = (Color) control.getObject("flow color");
      ArrayList flowLines = drawingPanel.getDrawables(FlowLine.class);
      Iterator it = flowLines.iterator();
      while (it.hasNext()){
         ( (FlowLine) it.next()).reset();
      }

      drawingPanel.repaint();
   }

   /**
    * Method handleMouseAction
    *
    * @param panel
    */
   public void handleMouseAction(InteractivePanel panel, MouseEvent evt) {
      double x = panel.getMouseX();
      double y = panel.getMouseY();
      switch(panel.getMouseAction()) {
         case InteractivePanel.MOUSE_DRAGGED :
            if(fl!=null) {
               fl.setXY(x, y);
            }
            break;
         case InteractivePanel.MOUSE_PRESSED :
            solverName = control.getString("ODE Solver");
            flowColor = (Color) control.getObject("flow color");
            fl = new FlowLine(x, y, this);
            panel.addDrawable(fl);
            break;
         case InteractivePanel.MOUSE_RELEASED :
            fl = null;
            break;
         case InteractivePanel.MOUSE_EXITED :
            fl = null;
            return;
      }
      panel.repaint();
   }

   /**
    * Steps the differential equations.
    */
   public void doStep() {
      ArrayList flowLines = drawingPanel.getDrawables(FlowLine.class);
      Iterator it = flowLines.iterator();
      while(it.hasNext()) {
         ((FlowLine) it.next()).stepFlow();
      }
   }

   /**
    * Returns an XML.ObjectLoader to save and load data for this program.
    *
    * @return the object loader
    */
   public static XML.ObjectLoader getLoader() {
      return new FlowLineAppLoader();
   }

   /**
    * Start the Java application.
    * @param args  command line parameters
    */
   public static void main(String[] args) {
      OSPControl c= SimulationControl.createApp(new FlowLineApp(), args);
      c.addButton("clearFlow","Clear","Removes all flow lines.");
      c.addButton("catControl","Cat","Makes a cat.");
   }
}

/**
 * An XML loader for the FlowLineApp
 */
class FlowLineAppLoader implements XML.ObjectLoader {

   /**
    * createObject
    *
    * @param element XMLControl
    * @return Object
    */
   public Object createObject(XMLControl element) {
      Simulation model = new FlowLineApp(); // model without control
      // SimulationControl control = new SimulationControl(model);
      // model.setControl(control);
      return model;
   }

   /**
    * saveObject
    *
    * @param element XMLControl
    * @param obj Object
    */
   public void saveObject(XMLControl control, Object obj) {
      FlowLineApp app = (FlowLineApp) obj;
      control.setValue("flow", app.drawingPanel.getDrawables(FlowLine.class));
      control.setValue("fx", app.fxStr);
      control.setValue("fy", app.fxStr);
   }

   /**
    * loadObject
    *
    * @param element XMLControl
    * @param obj Object
    * @return Object
    */
   public Object loadObject(XMLControl control, Object obj) {
      FlowLineApp app = (FlowLineApp) obj;
      app.stopSimulation();
      app.fxStr=control.getString("fx");
      app.fyStr=control.getString("fy");
      app.sampleField();   //
      ArrayList particles = (ArrayList) control.getObject("flow");
      app.drawingPanel.removeObjectsOfClass(FlowLine.class);
      int n = particles.size();
      for(int i = 0; i<n; i++) {
         FlowLine fl = (FlowLine) particles.get(i);
         fl.app=app;
         app.drawingPanel.addDrawable(fl);
      }
      app.initialize();
      app.drawingPanel.repaint();
      return obj;
   }
}
