package org.opensourcephysics.davidson.ode.basic_examples;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.numerics.*;
import org.opensourcephysics.frames.*;

/**
 * ReactionApp solves an autocatalytic oscillating chemical reaction (Brusselator model) using
 * a fouth-order Runge-Kutta algorithm.
 * @author Wolfgang Christian
 * @version 1.0
 */
public class ReactionApp extends AbstractSimulation {
   Reaction reaction;
   ODESolver solver;
   PlotFrame phasePlot= new PlotFrame("X","Y","Product Phase Space");
   PlotFrame timePlot= new PlotFrame("t","X and Y","Reaction Products");

   /**
    * Initializes the simulation by reading parameters from the control.
    */
   public void initialize(){
      double x = control.getDouble("X");
      double y = control.getDouble("Y");
      reaction = new Reaction(new double[]{x, y, 0}); // create reaction model
      solver = new RK4(reaction);                     // create solver for this model
   }

   /**
    * Steps (advances) the reaction and plots the results.
    * The solver increments the time using its internal stepsize.
    */
   protected void doStep() {
	   System.out.format("%12f", reaction.state[0]);
	   System.out.format("%12f", reaction.state[1]);
	   System.out.format("%12f \n", reaction.state[2]);
      solver.step();
      timePlot.append(0,reaction.state[2],reaction.state[0]);
      timePlot.append(1,reaction.state[2],reaction.state[1]);
      phasePlot.append(0,reaction.state[0],reaction.state[1]);
   }

   /**
    * Resets the reaction into a predefined state.
    */
   public void reset() {
      timePlot.setConnected(true);  // connect data points with straight line segments
      phasePlot.setConnected(true); // connect data points with straight line segments
      control.setValue("X","1");
      control.setValue("Y",5);
      control.setValue("dt",0.1);
      enableStepsPerDisplay(true);
      initialize();
   }

   /**
    * Starts the ReactionApp program.
    * @param args String[]
    */
   public static void main(String[] args) { 
	   SimulationControl.createApp(new ReactionApp(), args); }
}
