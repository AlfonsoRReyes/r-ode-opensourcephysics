package org.opensourcephysics.davidson.ode.driven_pendulum;

import org.opensourcephysics.frames.DisplayFrame;
import org.opensourcephysics.display.*;


public class PendulumPhaseSpace extends DisplayFrame {
   Trail trail = new Trail();
   Circle circle = new Circle(0,0,3);
   int rev=0;

 /**
  * Constructs a PendulumPhaseSpace with the given labels and title.
  *
  * @param xlabel String
  * @param ylabel String
  * @param title String
  */
  public PendulumPhaseSpace(String xlabel, String ylabel, String title) {
     super( xlabel,  ylabel,  title);
     trail.setMeasured(true);
     limitAutoscaleY(-2,2);
     setPreferredMinMaxX(-Math.PI, Math.PI);
     setSquareAspect(false);
     addDrawable(trail);
     addDrawable(circle);
  }

  public void clearData(){
     trail.clear();
     super.clearData();
  }


  void addPoint(double theta, double omega){
     int rev= (int)Math.floor(0.5d+theta/(2*Math.PI));
     if(this.rev!=rev){
        this.rev=rev;
        trail.addPoint(theta-2*(rev-this.rev)*Math.PI, omega);
        trail.moveToPoint(theta-2*rev*Math.PI, omega);
     }else{
        trail.addPoint(theta-2*rev*Math.PI, omega);
     }
     circle.setXY(theta-2*rev*Math.PI, omega);
  }

  void moveToPoint(double theta, double omega){
     this.rev = (int) Math.floor(0.5d+theta/ (2*Math.PI));
     trail.moveToPoint(theta-2*rev*Math.PI, omega);
     circle.setXY(theta-2*rev*Math.PI, omega);
  }


}
