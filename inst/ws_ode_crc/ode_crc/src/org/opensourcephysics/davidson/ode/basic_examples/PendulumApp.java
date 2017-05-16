package org.opensourcephysics.davidson.ode.basic_examples;

import org.opensourcephysics.controls.*;
import org.opensourcephysics.numerics.*;
import org.opensourcephysics.frames.*;

/**
 * PendulumApp solves a driven pendulum model using a fourth-order Runge-Kutta algorithm.
 * @author Wolfgang Christian
 * @version 1.0
 */
public class PendulumApp extends AbstractSimulation {
   Pendulum pendulum;
   ODESolver solver;
   PlotFrame plot= new PlotFrame("time","angle","Pendulum Simulation");

   /**
    * Initializes the simulation by reading parameters from the control.
    */
   public void initialize(){
      double theta = control.getDouble("theta");
      double omega = control.getDouble("omega");
      pendulum = new Pendulum(new double[]{theta, omega, 0});
      pendulum.b=control.getDouble("damping");
      pendulum.amp=control.getDouble("torque amp");
      pendulum.driving_omega=control.getDouble("torque omega");
      solver = new RK4(pendulum);
   }

   /**
    * Steps (advances) the pendulum position and velocity and plots the position.
    * The solver increments the time using its internal stepsize.
    */
   protected void doStep() {
      solver.step();
      plot.append(0,pendulum.state[2],pendulum.state[0]);
   }

   /**
    * Resets the pendulum into a predefined state.
    */
   public void reset() {
      plot.setConnected(0,true);
      control.setValue("theta","pi/2");
      control.setValue("omega",0);
      control.setValue("damping",0);
      control.setValue("torque amp",0);
      control.setValue("torque omega",1.00);
      control.setValue("dt",0.1);
      enableStepsPerDisplay(true);
      initialize();
   }
   /**
    * Starts the PendulumApp program.
    * @param args String[]
    */
   public static void main(String[] args) { SimulationControl.createApp(new PendulumApp()); }
}
