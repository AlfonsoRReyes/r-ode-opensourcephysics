package org.opensourcephysics.davidson.ode.sho;

import org.opensourcephysics.ejs.control.*;
import org.opensourcephysics.display.GUIUtils;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.display.DatasetManager;

/**
 * An EJS control object for the SHOApp.
 * @author Wolfgang Christian
 * @version 1.0
 */
public class SHOWRApp extends EjsControlFrame{

   SHOApp model;
   DrawingPanel drawingPanel;
   DatasetManager positionDatasets;

   public SHOWRApp(SHOApp model, String[] args) {
      super(model, "name=controlFrame;title=SHO Position;location=400,0;size=300,400;layout=border;exit=true; visible=false");
      this.model = model;
      addTarget("control", this);
      drawingPanel = model.positionPlot.getDrawingPanel();
      positionDatasets=model.positionPlot.getDatasetManager();
      model.positionPlot.dispose();
      addTarget("control", this);
      addObject(drawingPanel, "Panel","name=drawingPanel; parent=controlFrame; position=center");
      add("Panel", "name=controlPanel; parent=controlFrame;layout=border;position=south");
      add("Panel", "name=buttonPanel; parent=controlPanel;position=north");
      add("Button", "parent=buttonPanel; text=Start; action=control.runAnimation(); name=runButton;tooltip=Starts and stops the simulation.");
      add("Button", "parent=buttonPanel; text=Step; action=control.stepAnimation(); tooltip=Advances the simulation by a single time step.");
      add("Button", "parent=buttonPanel; text=Reset; action=control.reset();tooltip=Rests the simulation to its default state.");
      add("Button", "parent=buttonPanel; text=Clear; action=clearData();tooltip=Clears the data without resetting the conditions.");
      getMainFrame().setAnimated(true); // the frame's render method will be called automatically
      model.setControl(this);
      loadXML(args);
      reset();
      model.positionErrorPlot.setVisible(true);
      model.energyErrorPlot.setVisible(true);
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

   /**
    * Clears data from the datasets within this frame.
    */
   public void clearData(){
      positionDatasets.clear();
   }

   /**
    * Clears the position data and repaints the frame.
    */
   public void clearDataAndRepaint(){
     clearData();
     drawingPanel.repaint();
   }

   /**
    * Steps the animation.
    */
   public void stepAnimation(){
      if (model.isRunning()){
         model.stopSimulation();
         getControl("runButton").setProperty("text", "Start");
      }
      model.stepAnimation();
   }

   /**
    * The runAnimation switches the text on the run button
    */
   public void runAnimation() {
      if(model.isRunning()) {
         model.stopSimulation();
         getControl("runButton").setProperty("text", "Start");
      } else {
         getControl("runButton").setProperty("text", "Stop");
         model.solver.initialize(getDouble("dt"));
         drawingPanel.setMessage(null, 0);
         model.startSimulation();
      }
   }


   /**
    * Rests the simulation.
    */
   public void reset(){
      model.stopSimulation();
      getControl("runButton").setProperty("text", "Start ");
      model.reset();
      loadDefaultXML();
      GUIUtils.clearDrawingFrameData(true);
      model.initialize();
      GUIUtils.showDrawingAndTableFrames();
   }

   public static void main(String[] args) {
      new SHOWRApp(new SHOApp(), args);
   }

}
