package org.opensourcephysics.davidson.ode.basic_examples;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.numerics.*;
import org.opensourcephysics.frames.*;

/**
 * KeplerApp solves an inverse-square law model (Kepler model) using an adaptive stepsize algorithm.
 * @author Wolfgang Christian
 * @version 1.0
 */
public class KeplerApp extends AbstractSimulation {
   Kepler planet;
   ODESolver solver;
   PlotFrame positionPlot= new PlotFrame("x","y","Kepler Orbits");
   PlotFrame timePlot= new PlotFrame("t","x and v_{x}","Kepler Orbits");

   /**
    * Initializes the simulation by reading parameters from the control.
    */
   public void initialize(){
      double[] x = (double [])control.getObject("r");
      double[] v = (double [])control.getObject("v");
      planet = new Kepler(x,v);  // creates the model
      solver = new RK45(planet); // creates a solver for the model
   }

   /**
    * Steps (advances) the orbit and plots the results.
    * The solver increments the time using its internal stepsize.
    */
   protected void doStep() {
      solver.step();
      positionPlot.append(0,planet.state[0],planet.state[2]);
      timePlot.append(0,planet.state[4],planet.state[0]);
      timePlot.append(1,planet.state[4],planet.state[1]);
      positionPlot.setMessage("t="+decimalFormat.format(planet.state[4]));
   }

   /**
    * Resets the orbit into a predefined state.
    */
   public void reset() {
      positionPlot.setConnected(0,true);
      positionPlot.limitAutoscaleX(-1,1);
      positionPlot.limitAutoscaleY(-1,1);
      timePlot.setSize(700,250);
      control.setValue("r",new double[]{2,0});
      control.setValue("v",new double[]{0,0.25});
      control.setValue("dt",0.1);
      enableStepsPerDisplay(true);
      initialize();
   }

   /**
    * Starts the KeplerApp program.
    * @param args String[]
    */
   public static void main(String[] args) { SimulationControl.createApp(new KeplerApp(),args); }
}
