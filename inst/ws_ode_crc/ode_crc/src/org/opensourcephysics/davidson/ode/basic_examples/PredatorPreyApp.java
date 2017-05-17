package org.opensourcephysics.davidson.ode.basic_examples;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.numerics.*;
import org.opensourcephysics.frames.*;

/**
 * PredatorPreyApp solves a predator-prey model (Lokta-Volterra model) using a fouth-order Runge-Kutta algorithm.
 * @author Wolfgang Christian
 * @version 1.0
 */
public class PredatorPreyApp extends AbstractSimulation {
   PredatorPrey population;
   ODESolver solver;
   PlotFrame plot= new PlotFrame("time","population","Predator-Prey Simulation");

   /**
    * Initializes the simulation by reading parameters from the control.
    */
   public PredatorPreyApp(){
      plot.setConnected(0,true);
   }

   public void initialize(){
      double x1 = control.getDouble("x1");
      double x2 = control.getDouble("x2");
      population = new PredatorPrey(new double[]{x1, x2, 0}); // creates the model
      solver = new RK4(population);  // creates a solver for the model
   }

   /**
    * Steps (advances) the populations and plots the results.
    * The solver increments the time using its internal stepsize.
    */
   protected void doStep() {
      solver.step();
      plot.append(0,population.state[2],population.state[0]);
      plot.append(1,population.state[2],population.state[1]);
   }

   /**
    * Resets the preditor-prey populations into a predefined state.
    */
   public void reset() {
      control.setValue("x1","5");
      control.setValue("x2",1);
      control.setValue("dt",0.1);
      enableStepsPerDisplay(true);
      initialize();
   }

   /**
    * Starts the PredatorPreyApp program.
    * @param args String[]
    */
   public static void main(String[] args) { SimulationControl.createApp(new PredatorPreyApp(),args); }
}
