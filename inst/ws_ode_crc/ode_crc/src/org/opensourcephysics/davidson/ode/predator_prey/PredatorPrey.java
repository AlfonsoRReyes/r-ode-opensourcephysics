package org.opensourcephysics.davidson.ode.predator_prey;

import org.opensourcephysics.numerics.ODE;

public class PredatorPrey implements ODE {
  double[] state = new double[] {0.0, 0.0, 0.0};
  double a=0.5, b=0.1, c=0.1, d=0.6; // parameters

  PredatorPrey(double[] initialConditions){
     state=(double[])initialConditions.clone();
  }

  public double[] getState() { return state; }

  public void getRate(double[] state, double[] rate ){
    rate[0] = a*state[0] - b*state[0]*state[1];
    rate[1] = c*state[0]*state[1] - d*state[1] ;
    rate[2] = 1; // time derivative
  }
}
