package org.opensourcephysics.davidson.ode.driven_pendulum;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.numerics.*;
import org.opensourcephysics.frames.*;
import org.opensourcephysics.display.axes.PolarType1;
import org.opensourcephysics.display.*;
import java.awt.event.MouseEvent;
import java.beans.*;

public class PendulumApp extends AbstractSimulation implements InteractiveMouseHandler, PropertyChangeListener {
   Pendulum pendulum;
   ODESolver solver;
   PlotFrame plot= new PlotFrame("time","$\\theta$ and $\\omega$","Pendulum Time Series");
   DisplayFrame view= new DisplayFrame("","","Pendulum Motion");
   PendulumPhaseSpace phaseSpace= new PendulumPhaseSpace("$\\theta$","$\\omega$","Pendulum Phase Space");
   boolean shouldRun=false;

   PendulumApp(){
      plot.setXYColumnNames(0,"time","theta","theta");
      plot.setXYColumnNames(1,"time","omega","omega");
      view.setInteractiveMouseHandler(this);
      view.setPreferredMinMax(-1.0,1.0,-1.0,1.0);
      plot.limitAutoscaleX(0,1.0);
      new PolarType1((PlottingPanel)view.getDrawingPanel(),"r=", " theta=",-Math.PI/2);
   }

   /**
    * Responds to property changes such as loading xml files by reinitializing the program.
    *
    * @param evt PropertyChangeEvent
    */
   public void propertyChange(PropertyChangeEvent evt){
      boolean running = isRunning();
      if (running){
         stopSimulation();
      }
      initialize();
      if (running){
         startSimulation();
      }
   }


   public void initialize(){
      double theta = control.getDouble("theta");
      double omega = control.getDouble("omega");
      pendulum = new Pendulum(new double[]{theta, omega, 0});
      pendulum.driveOmega = control.getDouble("drive omega");
      pendulum.amp = control.getDouble("drive amp");
      pendulum.b=control.getDouble("damping");
      view.clearDrawables();
      view.addDrawable(pendulum);
      String solverName = control.getString("ODE Solver").toLowerCase().trim();
      solver= ODESolverFactory.createODESolver(pendulum, solverName);
      if(solver==null){
         control.println("Solver not found. Multistep-RK45 solver created.");
         solver= new ODEMultistepSolver(pendulum);
      }
      if (solver instanceof ODEAdaptiveSolver){
         ( (ODEAdaptiveSolver) solver).setTolerance(control.getDouble("adaptive solver tolerance"));
      }
      solver.initialize(control.getDouble("dt"));
      plot.append(0,pendulum.state[2],pendulum.state[0]);
      plot.append(1,pendulum.state[2],pendulum.state[1]);
      view.setMessage("t="+decimalFormat.format(pendulum.state[2]));
      phaseSpace.moveToPoint(theta,omega);
   }

   protected void doStep() {
      solver.step();
      double theta = pendulum.state[0];
      double omega = pendulum.state[1];
      double t = pendulum.state[2];
      control.setValue("omega",omega);
      control.setValue("theta",theta);
      plot.append(0,t,theta);
      plot.append(1,t,omega);
      view.setMessage("t="+decimalFormat.format(t));
      phaseSpace.addPoint(theta,omega);
   }

   public void reset(){
      plot.setConnected(true);
      control.setValue("damping",0);
      control.setValue("omega",0);
      control.setValue("theta","pi/2");
      control.setValue("drive omega","sqrt(9.8)");
      control.setValue("drive amp",0);
      control.setValue("dt",0.1);
      control.setValue("ODE Solver","multistep");
      control.setValue("adaptive solver tolerance", 1e-3);
      enableStepsPerDisplay(true);
      initialize();
      view.setMessage("Drag pendulum to set theta.",0);
   }

   /**
    * Removes numeric data without reinitializing.
    */
   public void clearData(){
      boolean running = isRunning();
      stopSimulation();
      GUIUtils.clearDrawingFrameData(true);
      plot.limitAutoscaleX(pendulum.state[2],pendulum.state[2]+1);
      plot.append(0,pendulum.state[2],pendulum.state[0]);
      plot.append(1,pendulum.state[2],pendulum.state[1]);
      view.setMessage("t="+decimalFormat.format(0));
      phaseSpace.addPoint(pendulum.state[0],pendulum.state[1]);
      if(running)startSimulation();
   }


   public void handleMouseAction(InteractivePanel panel, MouseEvent evt){
      panel.handleMouseAction(panel, evt);
      Interactive iad=panel.getInteractive();
      if(iad==null) return;  // nothing to do
      double x = iad.getX();
      double y = iad.getY();
      switch (panel.getMouseAction()){
         case InteractivePanel.MOUSE_DRAGGED:
            pendulum.state[0]=Math.atan2(x,-y);
            control.setValue("theta",pendulum.state[0]);
            phaseSpace.moveToPoint(pendulum.state[0],pendulum.state[1]);
            phaseSpace.repaint();
            break;
         case InteractivePanel.MOUSE_PRESSED:
            shouldRun=isRunning();
            stopSimulation();
            break;
         case InteractivePanel.MOUSE_RELEASED:
            pendulum.state[0]=Math.atan2(x,-y);
            control.setValue("theta",pendulum.state[0]);
            phaseSpace.moveToPoint(pendulum.state[0],pendulum.state[1]);
            if(shouldRun)startSimulation();
            shouldRun=false;
            break;
         case InteractivePanel.MOUSE_EXITED:
            return;
      }
      GUIUtils.repaintAnimatedFrames();
   }

   public static void main(String[] args){
      OSPControl c=SimulationControl.createApp(new PendulumApp(),args);
      c.addButton("clearData","Clear","Removes data without reinitializing.");
   }


}
