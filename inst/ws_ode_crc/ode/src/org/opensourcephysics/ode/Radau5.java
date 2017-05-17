package org.opensourcephysics.ode;
import org.opensourcephysics.numerics.*;

/**
 * A convenience class that consructs a Radau5 ODESolver.
 *
 * @author W. Christian
 * @version 1.0
 */
public class Radau5 extends org.opensourcephysics.ode.IRK.Radau5{

   /**
    * Radau5 constructor.
    *
    * @param ode ODE
    */
   public Radau5(ODE ode){
      super(ode);
   }

   /**
    * Sets the step size.
    * @param dt double
    */
   public void setStepSize(double dt){
      if(Math.abs(dt)>0.1){ // solver hangs if dt is too large.
         dt=(dt<0)?-0.1:0.1;
      }
      super.setStepSize(dt);
   }
}



