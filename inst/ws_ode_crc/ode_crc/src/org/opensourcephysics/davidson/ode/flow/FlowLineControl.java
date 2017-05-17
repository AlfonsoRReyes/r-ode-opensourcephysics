/*
 * The org.opensourcephysics.demoapps package contains longer programs that demonstrate
 * how to use various Open Source Physics tools.
 * These examples are proof-of-concept only.
 *
 * Copyright (c) 2007  W. Christian.
 */
package org.opensourcephysics.davidson.ode.flow;
import org.opensourcephysics.ejs.control.*;
import org.opensourcephysics.display.DrawingPanel;

/**
 * An EJS control object for the FlowLineWRApp
 * @author Wolfgang Christian
 * @version 1.0
 */
public class FlowLineControl extends EjsControlFrame {

   FlowLineWRApp model;
   DrawingPanel drawingPanel;

   public FlowLineControl(FlowLineWRApp model, String[] args) {
      super(model, "name=controlFrame;title=Phase Space Flow;location=400,0;size=400,500;layout=border;exit=true; visible=false");
      this.model = model;
      addTarget("control", this);
      drawingPanel = model.drawingFrame.getDrawingPanel();
      model.drawingFrame.dispose();
      addTarget("control", this);
      addObject(drawingPanel, "Panel", "name=drawingPanel; parent=controlFrame; position=center");
      add ("Panel", "name=controlPanel; layout=vbox; parent=controlFrame;position=south");
      //add("Slider","position=center;parent=controlPanel;variable=size;minimum=2;maximum=64;ticks=0;action=sliderMoved; format=grid size=0");
      add("Panel", "name=fxPanel;parent=controlPanel;layout=border");
      add("Label", "position=west; parent=fxPanel;text=F(x,y) = ;horizontalAlignment=right");
      add("TextField", "name=fxField;position=center;parent=fxPanel;variable=fx;value=y;action=resetPlot");
      add("Panel", "name=fyPanel;parent=controlPanel;layout=border");
      add("Label", "position=west; parent=fyPanel;text=G(x,y) = ;horizontalAlignment=right");
      add("TextField", "name=fyField;position=center;parent=fyPanel;variable=fy;value=-sin(x);action=resetPlot");
      add("Panel", "name=buttonPanel;parent=controlPanel;layout=flow");
      add("Button", "parent=buttonPanel; text=Start; action=control.runAnimation(); name=runButton");
      add("Button", "parent=buttonPanel; text=Clear; action=resetPlot");
      add("Button", "parent=buttonPanel; text=Reset; action=control.resetSimulation()");
      add("CheckBox", "parent=buttonPanel;variable=show paths;text=Show Paths;action=setShowPaths");
      add("CheckBox", "parent=buttonPanel;variable=show field;text=Show Field;action=setShowField");
      getMainFrame().setAnimated(true);     // the frame's render method will be called automatically
      model.setControl(this);
      loadXML(args);
      resetSimulation();
      getMainFrame().setVisible(true);
      addPropertyChangeListener(model); // loading an XML data file will a fire property change event
   }

   /**
    * Renders (draws) the panel immediately.
    *
    * Unlike repaint, the render method is draws the panel within the calling method's thread.
    * This method is called automatically if the frame is animated.
    */
   public void render(){
      drawingPanel.render(); // simulations should render their panels at every time step
   }

   public void makeCat() {
      model.clearFlow();
      model.makeCat(model.xmin + 0.6*(model.xmax-model.xmin), (model.ymax+model.ymin)/2.0, 0.1*(model.xmax-model.xmin),32);
   }

   public void resetSimulation(){
      model.stopSimulation();
      getControl("runButton").setProperty("text", "Start ");
      model.reset();
      loadDefaultXML();
      model.initialize();
      drawingPanel.repaint();
   }


   /**
 *  Switches the text on the run button
 */
public void runAnimation() {
   if(model.isRunning()) {
      model.stopSimulation();
      getControl("runButton").setProperty("text", "Start");
      getControl("fxField").setProperty("enabled", "true");
      getControl("fyField").setProperty("enabled", "true");
   } else {
      getControl("runButton").setProperty("text", "Stop");
      getControl("fxField").setProperty("enabled", "false");
      getControl("fyField").setProperty("enabled", "false");
      drawingPanel.setMessage(null, 0);
      model.startSimulation();
   }
}


}
