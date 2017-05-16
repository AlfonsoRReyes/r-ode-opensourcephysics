package org.opensourcephysics.davidson.ode.basic_examples;
import org.opensourcephysics.numerics.ODE;

/**
 * Pendulum models a driven pendulum by implementing the ODE interface.
 * @author Wolfgang Chrisitan
 * @version 1.0
 */
public class Pendulum implements ODE {
  double[] state = new double[] {Math.PI/2.0, 0.0, 0.0}; // state variables
  double m=1.0, l=1.0, g=9.8, b=0.0; // mass, pendulum length, acceleration of gravity, and damping
  double amp=0.0, driving_omega=1.0;// torque amplitude and frequency

  /**
   * Constructs a Pendulum model using the given initial conditions.
   * @param initialConditions double[]
   */
  Pendulum(double[] initialConditions){
     state=(double[])initialConditions.clone();
  }

  /**
   * Gets the model's state variables.
   * @return double[]
   */
  public double[] getState() { return state; }

  /**
   * Computes the rate using the given state.
   * @param state double[] the state that will be used to compute the rate
   * @param rate double[]  the computed rate
   */
  public void getRate(double[] state, double[] rate ){
    rate[0] = state[1];
    rate[1] = - g/l*Math.sin(state[0]) - b/(m*l*l)*state[1]+amp*Math.sin(driving_omega*state[2]);
    rate[2] = 1; // time derivative
  }
}
