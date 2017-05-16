package org.opensourcephysics.davidson.ode.sho;
import org.opensourcephysics.ejs.control.*;
import org.opensourcephysics.display.GUIUtils;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.display.DatasetManager;

/**
 * An EJS control object for the SHOApp that allows users to select an ODE solver with an adaptive step size.
 *
 * @author Wolfgang Christian
 * @version 1.0
 */
public class AdaptiveStepWRApp extends EjsControlFrame {

   SHOApp model;
   DrawingPanel drawingPanel;
   DatasetManager positionDatasets;
   double tol = 1.0e-3;
   String solverName = "CashKarp45";
   boolean resetMode=true;

   /**
    * Constructs AdaptiveStepWRApp for the given SHO.
    *
    * @param model SHOApp
    * @param args String[]
    */
   public AdaptiveStepWRApp(SHOApp model, String[] args) {
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
         setProperty("options","CashKarp45;DormandPrince45");
      add("Label", "position=west; parent=buttonPanel;text=  tol=;horizontalAlignment=right");
      add("NumberField", "name=tolField;position=center;action=control.setCurrentValues;parent=buttonPanel;variable=solver tolerance;format=0.000000;value=0.001");
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
         getControl("tolField").setProperty("enabled", "false");
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
         getControl("tolField").setProperty("enabled", "false");
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
      getControl("tolField").setProperty("enabled", "true");
      getControl("resetButton").setProperty("text", "Reset");
      model.reset();
      setValue("ODE_Solver", solverName);
      loadDefaultXML();
      GUIUtils.clearDrawingFrameData(true);
      if(!resetMode){  // keep current values the first time button is pressed
         setValue("ODE_Solver", solverName);
         setValue("solver tolerance", tol);
      }
      model.initialize();
      GUIUtils.showDrawingAndTableFrames();
      resetMode = true;
   }

   public void setCurrentValues() {
      solverName = getString("ODE_Solver");
      tol = (Double.isNaN(getDouble("solver tolerance")))?0.001:getDouble("solver tolerance");
   }

   /**
    * Runs the SHOApp program with a custom Ejs control that allows users to select
    * an ODE solver with a fixed step size.
    * @param args String[]
    */
   public static void main(String[] args) {
      new AdaptiveStepWRApp(new SHOApp(), args);
   }
}
