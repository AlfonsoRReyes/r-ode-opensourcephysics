package org.opensourcephysics.davidson.ode.sho;
import org.opensourcephysics.ejs.control.*;
import org.opensourcephysics.display.GUIUtils;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.display.DatasetManager;

/**
 * An EJS control object for the SHOApp that allows users to select an ODE solver with a fixed step size.
 *
 * @author Wolfgang Christian
 * @version 1.0
 */
public class FixedStepWRApp extends EjsControlFrame {

   SHOApp model;
   DrawingPanel drawingPanel;
   DatasetManager positionDatasets;
   double dt = 0.1;
   String solverName = "Euler";
   boolean resetMode=true;

   /**
    * Constructs FixedStepWRApp for the given SHO.
    *
    * @param model SHOApp
    * @param args String[]
    */
   public FixedStepWRApp(SHOApp model, String[] args) {
      super(model,
            "name=controlFrame;title=SHO Position;location=400,0;size=450,400;layout=border;exit=true; visible=false");
      this.model = model;
      addTarget("control", this);
      drawingPanel = model.positionPlot.getDrawingPanel();
      positionDatasets = model.positionPlot.getDatasetManager();
      model.positionPlot.dispose();
      addTarget("control", this);
      addObject(drawingPanel, "Panel", "name=drawingPanel; parent=controlFrame; position=center");
      add("Panel", "name=controlPanel; parent=controlFrame;layout=border;position=south");
      add("Panel", "name=buttonPanel; parent=controlPanel;position=north");
      add("Button",
          "parent=buttonPanel; text=Start; action=control.runAnimation(); name=runButton;tooltip=Starts and stops the simulation.");
      add("Button",
          "parent=buttonPanel; text=Step; action=control.stepAnimation(); tooltip=Advances the simulation by a single time step.");
      add("Button",
          "parent=buttonPanel; text=Reset; action=control.reset();name=resetButton;tooltip=Rests the simulation to its default state.");
      add("ComboBox",
          "parent=buttonPanel;name=ODE_Solver;action=control.setCurrentValues;variable=ODE_Solver").
         setProperty("options","Euler;Verlet;Ralston2;Heun3;RK4;Butcher5;Adams5");
      add("Label", "position=west; parent=buttonPanel;text=  dt=;horizontalAlignment=right");
      add("NumberField", "name=dtField;position=center;action=control.setCurrentValues;parent=buttonPanel;variable=dt;format=0.000;value=0.1");
      getMainFrame().setAnimated(true);     // the frame's render method will be called automatically
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
   public void render() {
      drawingPanel.render(); // simulations should render their panels at every time step
   }

   /**
    * Clears data from the datasets within this frame.
    */
   public void clearData() {
      positionDatasets.clear();
   }

   /**
    * Clears the position data and repaints the frame.
    */
   public void clearDataAndRepaint() {
      clearData();
      drawingPanel.repaint();
   }

   /**
    * Steps the animation.
    */
   public void stepAnimation() {
      if(resetMode){
         resetMode = false;
         getControl("ODE_Solver").setProperty("enabled", "false");
         getControl("dtField").setProperty("enabled", "false");
         getControl("resetButton").setProperty("text", "New");
         model.initialize();
      }
      if(model.isRunning()) {
         model.stopSimulation();
         getControl("runButton").setProperty("text", "Start");
      }
      model.stepAnimation();
   }

   /**
    * The runAnimation switches the text on the run button
    */
   public void runAnimation() {
      if(resetMode){
         resetMode = false;
         getControl("ODE_Solver").setProperty("enabled", "false");
         getControl("dtField").setProperty("enabled", "false");
         getControl("resetButton").setProperty("text", "New");
         model.initialize();
      }
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
   public void reset() {
      model.stopSimulation();
      getControl("runButton").setProperty("text", "Start ");
      getControl("ODE_Solver").setProperty("enabled", "true");
      getControl("dtField").setProperty("enabled", "true");
      getControl("resetButton").setProperty("text", "Reset");
      model.reset();
      loadDefaultXML();
      GUIUtils.clearDrawingFrameData(true);
      if(!resetMode){  // keep current values the first time button is pressed
         setValue("ODE_Solver", solverName);
         setValue("dt", dt);
      }
      model.initialize();
      GUIUtils.showDrawingAndTableFrames();
      resetMode = true;
   }

   public void setCurrentValues() {
      solverName = getString("ODE_Solver");
      dt = (Double.isNaN(getDouble("dt")))?0.1:getDouble("dt");
   }

   /**
    * Runs the SHOApp program with a custom Ejs control that allows users to select
    * an ODE solver with a fixed step size.
    * @param args String[]
    */
   public static void main(String[] args) {
      new FixedStepWRApp(new SHOApp(), args);
   }
}
