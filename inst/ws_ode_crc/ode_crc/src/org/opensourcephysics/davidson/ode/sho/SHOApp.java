package org.opensourcephysics.davidson.ode.sho;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.numerics.*;
import org.opensourcephysics.frames.*;
import org.opensourcephysics.display.*;
import java.beans.*;

/**
 * SHOApp models a simple harmonic oscillator (SH0).
 *
 * The user can select various ODE solvers.
 *
 * @author W. Christian
 * @version 1.0
 */
public class SHOApp extends AbstractSimulation implements  PropertyChangeListener {
   SHO sho;
   ODESolver solver;
   PlotFrame positionErrorPlot= new PlotFrame("time","position error","Position Error vs. Time");
   PlotFrame energyErrorPlot= new PlotFrame("time","energy error","Energy Error vs. Time");
   PlotFrame positionPlot= new PlotFrame("time","x","Position vs. Time");

   /**
    * Constructs the SHOApp program.
    */
   SHOApp(){
      positionPlot.setName("Position Plot");
      positionPlot.setXYColumnNames(0,"time","x numeric","position");
      positionPlot.setXYColumnNames(1,"time","x analytic","analytic");
      positionPlot.limitAutoscaleX(0,1.0);
      positionPlot.setMarkerSize(0,2);
      positionPlot.setConnected(1,true);
      positionPlot.setMarkerSize(1,1);
      positionPlot.setMaximumFractionDigits(14);
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

   /**
    * Initializes the model.
    */
   public void initialize(){
      double x = control.getDouble("x");
      double v = control.getDouble("v");
      double k = control.getDouble("k");
      sho = new SHO(x,v,k);
      String solverName = control.getString("ODE_Solver").toLowerCase().trim();
      solver= ODESolverFactory.createODESolver(sho, solverName);
      if(solver==null){
         control.println("Solver not found. Multistep-RK45 solver created.");
         solver= new ODEMultistepSolver(sho);
      }
      if (solver instanceof ODEAdaptiveSolver){
         ( (ODEAdaptiveSolver) solver).setTolerance(control.getDouble("solver tolerance"));
      }
      solver.initialize(control.getDouble("dt"));
      positionPlot.limitAutoscaleX(0,2*Math.PI/Math.sqrt(k));
      positionPlot.append(0,sho.state[2],sho.state[0]);
      positionPlot.append(1,sho.state[2],sho.getAnalyticX());
      positionPlot.setMessage("# computations="+sho.count,2);
      positionErrorPlot.append(0,sho.state[2],sho.getPositionError());
      energyErrorPlot.append(0,sho.state[2],sho.getEnergyError());
   }

   /**
    * Does a simulation step by advancing the time.
    */
   protected void doStep() {
      solver.step();
      System.out.format("%12f %12f %12f \n", sho.state[0], sho.state[1], sho.state[2]);
      double x = sho.state[0];
      double v = sho.state[1];
      double t = sho.state[2];
      control.setValue("x",x);
      control.setValue("v",v);
      positionPlot.append(0,t,x);
      positionPlot.append(1,t,sho.getAnalyticX());
      positionPlot.setMessage("# computations="+sho.count,2);
      positionErrorPlot.append(0,sho.state[2],sho.getPositionError());
      energyErrorPlot.append(0,sho.state[2],sho.getEnergyError());
   }

   /**
    * Resets the control and model to a known state.
    */
   public void reset(){
      control.setValue("x",1.0);
      control.setValue("v",0);
      control.setValue("k",1.0);
      control.setValue("dt",0.1);
      control.setValue("ODE_Solver","Euler");
      control.setValue("solver tolerance", 1e-3);
      enableStepsPerDisplay(true);
      initialize();
   }

   /**
    * Removes current data.
    */
   public void clearData(){
      boolean running = isRunning();
      stopSimulation();
      GUIUtils.clearDrawingFrameData(true);
      sho.count=0;
      positionPlot.limitAutoscaleX(sho.state[2],sho.state[2]+2*Math.PI/Math.sqrt(sho.k));
      positionPlot.append(0,sho.state[2],sho.state[0]);
      positionPlot.append(1,sho.state[2],sho.getAnalyticX());
      positionErrorPlot.append(0,sho.state[2],sho.getPositionError());
      energyErrorPlot.append(0,sho.state[2],sho.getEnergyError());
      positionPlot.setMessage("# computations="+sho.count,2);
      if(running)startSimulation();
   }
   /**
    * Starts the SHOApp program with basic SimulationControl.
    * @param args String[]
    */
   public static void main(String[] args){
      OSPControl c=SimulationControl.createApp(new SHOApp(),args);
      c.addButton("clearData","Clear","Removes data without reinitializing.");
   }


}
