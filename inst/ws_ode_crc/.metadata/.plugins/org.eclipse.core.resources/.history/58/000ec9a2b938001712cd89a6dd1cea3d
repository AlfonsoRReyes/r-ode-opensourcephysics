package org.opensourcephysics.davidson.ode.basic_examples;
import org.opensourcephysics.numerics.ODE;

/**
 * Reaction models an autocatalytic oscillating chemical reaction (Brusselator model)
 * by implementing the ODE interface.
 * @author Wolfgang Christian
 * @version 1.0
 */
public class Reaction implements ODE {
  double[] state = new double[3]; // X, Y, t
  double k1=1.0, k2=2.5, k3=1.0, k4=1.0; // reaction rates
  double A=1, B=1;       // reactant concenterations

  /**
   * Constructs an autocatalytic oscillating chemical reaction using the given initial conditions.
   * @param initialConditions double[]
   */
  Reaction(double[] initialConditions){
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
    double xxy=state[0]*state[0]*state[1];
    rate[0] = k1*A - k2*B*state[0] + k3*xxy-k4*state[0];  // X rate
    rate[1] = k2*B*state[0]-k3*xxy ; // Y rate
    rate[2] = 1; // time derivative
  }
}
