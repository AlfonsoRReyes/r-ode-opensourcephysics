package org.opensourcephysics.davidson.ode.basic_examples;
import org.opensourcephysics.numerics.ODE;

/**
 * Kepler models Keplerian orbits of a mass moving under the influence of an inverse square force
 * by implementing the ODE interface.
 * @author Wolfgang Chrisitan
 * @version 1.0
 */
public class Kepler implements ODE {
   static final double GM=1.0;      // gravitation constant times combined mass
   double[] state = new double[5];  // x, vx, y, vy, t

  /**
   * Constructs an inverse square law model using the given initial conditions.
   * @param initialConditions double[]
   */
   Kepler(double[] r, double[] v){
      state[0]=r[0];
      state[1]=v[0];
      state[2]=r[1];
      state[3]=v[1];
      state[4]=0;
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
    double r2=state[0]*state[0]+state[2]*state[2];  // distance squared
    double r3=Math.sqrt(r2)*r2;  // distance cubed
    rate[0] = state[1];
    rate[1] = -GM*state[0]/r3;
    rate[2] = state[3];
    rate[3] = -GM*state[2]/r3;
    rate[4] = 1; // time derivative
  }
}
