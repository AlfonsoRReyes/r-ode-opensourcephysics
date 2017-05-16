package org.opensourcephysics.davidson.ode.basic_examples;
import org.opensourcephysics.numerics.ODE;

/**
 * PredatorPrey models  predator-prey interactions (Lokta-Volterra model) by implementing the ODE interface.
 * @author Wolfgang Christian
 * @version 1.0
 */
public class PredatorPrey implements ODE {
  double[] state = new double[] {0.0, 0.0, 0.0};
  double a=0.5, b=0.1, c=0.1, d=0.6; // parameters

  /**
   * Constructs a predator-prey model using the given initial conditions.
   * @param initialConditions double[]
   */
  PredatorPrey(double[] initialConditions){
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
    rate[0] = a*state[0] - b*state[0]*state[1];
    rate[1] = c*state[0]*state[1] - d*state[1] ;
    rate[2] = 1; // time derivative
  }
}
