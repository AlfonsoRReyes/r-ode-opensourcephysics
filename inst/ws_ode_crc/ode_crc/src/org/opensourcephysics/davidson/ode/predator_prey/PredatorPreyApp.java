package org.opensourcephysics.davidson.ode.predator_prey;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.numerics.*;
import org.opensourcephysics.frames.*;


public class PredatorPreyApp extends AbstractSimulation {
   PredatorPrey population;
   ODESolver solver;
   PlotFrame plot= new PlotFrame("time","angle","Pendulum Simulation");

   public PredatorPreyApp(){
      plot.setConnected(0,true);
   }

   public void initialize(){
      double x1 = control.getDouble("x1");
      double x2 = control.getDouble("x2");
      population = new PredatorPrey(new double[]{x1, x2, 0});
      solver = new RK4(population);
   }

   protected void doStep() {
      solver.step();
      plot.append(0,population.state[2],population.state[0]);
      plot.append(1,population.state[2],population.state[1]);
   }

   public void reset() {
      control.setValue("x1","5");
      control.setValue("x2",1);
      control.setValue("dt",0.1);
      enableStepsPerDisplay(true);
      initialize();
   }

   public static void main(String[] args) { 
	   SimulationControl.createApp(new PredatorPreyApp()); 
   }
}
