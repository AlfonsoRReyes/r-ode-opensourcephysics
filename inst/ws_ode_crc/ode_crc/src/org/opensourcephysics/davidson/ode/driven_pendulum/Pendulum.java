package org.opensourcephysics.davidson.ode.driven_pendulum;

import org.opensourcephysics.numerics.ODE;
import org.opensourcephysics.display.*;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;

public class Pendulum extends InteractiveCircle implements ODE {
  double[] state = new double[] {Math.PI/2.0, 0.0, 0.0};
  double m=1.0, l=1.0, g=9.8, b=0.1; // parameters
  double driveOmega, amp;
  DrawableShape rect = DrawableShape.createRectangle(0,0,0.05,1);

  Pendulum(double[] initialConditions){
     state=(double[])initialConditions.clone();
     rect.transform(AffineTransform.getTranslateInstance(0,-0.5));
  }

  public double[] getState() { return state; }

  public void getRate(double[] state, double[] rate ){
    rate[0] = state[1];
    rate[1] = - g/l*Math.sin(state[0]) - b/(m*l*l)*state[1]+ amp*Math.sin(driveOmega*state[2]);
    rate[2] = 1; // time derivative
  }

   public void draw(DrawingPanel panel, Graphics g){
      // sets the xy coordiantes in the superclass
      x=l*Math.sin(state[0]);
      y=-l*Math.cos(state[0]);
      rect.setTheta(state[0]);
      rect.draw(panel, g);
      super.draw(panel,g);
   }
}
