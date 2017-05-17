package org.opensourcephysics.davidson.ode.sho;

import org.opensourcephysics.numerics.ODE;

public class SHO implements ODE {
  double[] state = new double[] {Math.PI/2.0, 0.0, 0.0};
  double k=0.1; // spring constant; assume unit mass
  double amp, phase, omega, energy;
  int count;

  SHO(double x, double v, double k){
     this.k=k;
     state[0]=x;
     state[1]=v;
     amp = Math.sqrt(x*x+v*v/k);
     phase=Math.atan2(x,v/Math.sqrt(k));
     omega=Math.sqrt(k);
     energy= 0.5*(k*x*x+v*v);
  }

  public double[] getState() { return state; }

  public void getRate(double[] state, double[] rate ){
    rate[0] = state[1];
    rate[1] = - k*state[0];
    rate[2] = 1; // time derivative
    count++;
  }

  public double getAnalyticX(){
     return amp*Math.sin(omega*state[2]+phase);
  }

  public double getPositionError(){
     return state[0]-amp*Math.sin(omega*state[2]+phase);
  }

  public double getEnergyError(){
     return 0.5*(k*state[0]*state[0]+state[1]*state[1])-energy;
  }

}
