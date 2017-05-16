package org.opensourcephysics.davidson.ode.events;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;
import org.opensourcephysics.controls.*;
import org.opensourcephysics.numerics.*;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * CollisionApp models hard disk collisions in a 2D box.
 *
 * @author W. Christian
 * @author F. Esquembre
 * @version 1.0
 */
public class CollisionApp extends AbstractSimulation implements ODE, InteractiveMouseHandler, PropertyChangeListener {

   InteractiveShape balls[];
   DisplayFrame frame = new DisplayFrame("Collision Events");
   ODEBisectionEventSolver solver;
   int nBalls = 10;
   double radius = 0.25;
   double xmin = 0, xmax = 10, ymin = 0, ymax = 10;
   double[] state; // x_i, vx_i, y_i, vy_i and time
   double g = 0, k = 1;
   double mass[];
   boolean keepRunning = false;

   /**
    * Constructs a CollisionApp.
    */
   public CollisionApp() {
      frame.setInteractiveMouseHandler(this);
      frame.setSize(400, 500);
   }

   /**
 * Responds to all property changes by reinitializing the program.
 *
 * @param evt PropertyChangeEvent
*/
public void propertyChange(PropertyChangeEvent evt){
   boolean running = isRunning();
   if (running){
      stopSimulation();
   }
   initialize();
   if (running){
      startSimulation();
   }
}


   /**
    * Gets the state vector for the balls.  Implementation of ODE interface
    *
    * @return double[]
    */
   public double[] getState() {
      return state;
   }

   /**
    * Gets the rate.  Implementation of ODE interface.
    * @param state double[]
    * @param rate double[]
    */
   public void getRate(double[] state, double[] rate) {
      for(int i = 0, j = 0; i<nBalls; i++, j += 4) {
         rate[j] = state[j+1];   // d  x/dt = vx
         rate[j+1] = 0;          // d vx/dt = 0 i.e. Uniform motion
         rate[j+2] = state[j+3]; // d  y/dt = vy
         rate[j+3] = (state[j+2]-radius>0)
                     ? -g
                     : 0;        // d vy/dt = g i.e. gravity
      }
      rate[rate.length-1] = 1; // time
   }

   /**
    * Initializes the model.
    */
   public void initialize() {
      frame.clearDrawables();
      DrawableShape box = DrawableShape.createRectangle((xmax+xmin)/2, (ymin+ymax)/2, xmax-xmin, ymax-ymin);
      box.setMarkerColor(Color.WHITE, Color.RED);
      frame.addDrawable(box);
      nBalls = control.getInt("number of balls");
      g = control.getDouble("acceleration of gravity");
      k = control.getDouble("coefficient of restitution");
      state = new double[4*nBalls+1]; // x_i, vx_i, y_i, vy_i and time
      mass = new double[nBalls];
      for(int i = 0; i<nBalls; i++) {
         mass[i] = 1.0;
      }
      switch(control.getInt("experiment #")) {
         default :
         case 0 :
            randomize();
            break;
         case 1 :
            experiment1();
            break;
         case 2 :
            experiment2();
            break;
         case 3 :
            experiment3();
            break;
         case 4 :
            experiment4();
            break;
      }
      // Use a solver that supports state events
      solver = new ODEBisectionEventSolver(this, org.opensourcephysics.numerics.RK4.class);
      solver.addEvent(new BounceEvent());    // Bouncing of balls against the walls
      solver.addEvent(new CollisionEvent()); // Collision of balls among themselves
      solver.initialize(control.getDouble("dt"));
      // Display
      frame.setPreferredMinMax(xmin, xmax, ymin, ymax);
      balls = new InteractiveShape[nBalls];
      for(int i = 0, j = 0; i<nBalls; i++, j += 4) {
         balls[i] = InteractiveShape.createCircle(state[j], state[j+2], 2*radius);
         frame.addDrawable(balls[i]);
      }
      frame.setMessage("Energy = "+energy(), 3);
   }

   public void startRunning(){
      frame.setMessage(null,0);
   }

   /**
    * Handles mouse actions in the panel.
    *
    * @param panel
    * @param evt
    */
   public void handleMouseAction(InteractivePanel panel, MouseEvent evt) {
      panel.handleMouseAction(panel, evt);
      Interactive iad = panel.getInteractive(); // identify the interactive object
      switch(panel.getMouseAction()) {
         case InteractivePanel.MOUSE_PRESSED :
            iad = panel.getInteractive(); // identify the interactive object
            if(iad!=null) {
               keepRunning = isRunning();
               stopSimulation();
            }
            break;
         case InteractivePanel.MOUSE_RELEASED :
            iad = panel.getInteractive(); // identify the interactive object
            for(int i = 0, n = balls.length; i<n; i++) {
               if(iad==balls[i]) {
                  removeOverlap(balls[i]);
                  state[4*i] = balls[i].getX();
                  state[4*i+2] = balls[i].getY();
                  break;
               }
            }
            if(keepRunning) {
               startSimulation();
            } else {
               panel.repaint();
            }
            break;
      }
   }

   /**
    * Removes any overlap after the given ball is moved.
    * @param ball InteractiveShape
    */
   void removeOverlap(InteractiveShape ball) {
      double r2 = 4*radius*radius;
      boolean overlap = true;
      int count = 10;
      while(overlap&&(count>0)) {
         overlap = false;
         count--;
         for(int i = 0; i<nBalls; i++) {
            if((balls[i]==null)||(ball==balls[i])) {
               continue;
            }
            double deltax = ball.getX()-balls[i].getX(); // x2-x1
            double deltay = ball.getY()-balls[i].getY(); // y2 - y1
            double d2 = deltax*deltax+deltay*deltay;     // separation squared
            if(d2<r2) {
               overlap = true;
               d2 = Math.sqrt(d2);
               ball.setX(ball.getX()+(2*radius-d2)*deltax/d2);
               ball.setY(ball.getY()+(2*radius-d2)*deltay/d2);
            }
         }                                               // End for i
      }
   }

   /**
    * Resets the program.
    */
   public void reset() {
      control.setValue("experiment #", 1);
      control.setValue("number of balls", 10);
      control.setValue("acceleration of gravity", 0);
      control.setValue("coefficient of restitution", 1);
      control.setValue("dt", 0.1);
      initialize();
      frame.setMessage("Click-drag to position balls.",0);
   }

   /**
    * Steps (advances) the time.
    */
   public void doStep() {
      solver.step();
      for(int i = 0, j = 0; i<nBalls; i++, j += 4) {
         balls[i].setXY(state[j], state[j+2]);
      }
      frame.setMessage("Energy = "+energy(), 3);
   }

   /**
    * Computes the kinetic eneryg.
    * @return double
    */
   public double energy() {
      double ke = 0.0, pe = 0.0;
      for(int i = 0, j = 0; i<nBalls; i++, j += 4) {
         ke = ke+0.5*(state[j+1]*state[j+1]+state[j+3]*state[j+3]);
         pe = pe+g*state[j+2];
      }
      return ke+pe;
   }

   /**
    * BounceEvent models collisions with the floor
    */
   private class BounceEvent implements StateEvent {

      static final double TOLERANCE = 0.001;
      private boolean horizontal = true;
      private int index;
      public double getTolerance() {
         return TOLERANCE;
      }

      public double evaluate(double[] state) {
         double minimum = TOLERANCE;
         for(int i = 0, j = 0; i<nBalls; i++, j += 4) {
            double d = state[j+2]-ymin-radius;
            if((state[j+3]<0)&&(d<minimum)) {
               horizontal = false;
               index = j;
               minimum = d;
            }
            d = ymax-radius-state[j+2];
            if((state[j+3]>0)&&(d<minimum)) {
               horizontal = false;
               index = j;
               minimum = d;
            }
            d = state[j]-xmin-radius;
            if((state[j+1]<0)&&(d<minimum)) {
               horizontal = true;
               index = j;
               minimum = d;
            }
            d = xmax-radius-state[j];
            if((state[j+1]>0)&&(d<minimum)) {
               horizontal = true;
               index = j;
               minimum = d;
            }
         }
         return minimum;
      }

      public boolean action() {
         if(horizontal) {
            state[index+1] = -k*state[index+1]; // Invert vx
         } else {
            state[index+3] = -k*state[index+3]; // Invert vy
         }
         return false; // return exactly at the event point
      }
   } // End of inner class BounceEvent

   /**
    * CollisionEvent models collisions between balls.
    */
   private class CollisionEvent implements StateEvent {

      static private final double TOLERANCE = 0.0001;
      private int b1, b2, collision1, collision2;
      private final double R = 2*radius*2*radius; // Avoid square roots
      public double getTolerance() {
         return TOLERANCE;
      }

      public double evaluate(double[] state) {
         double minimum = TOLERANCE;
         for(int i = 0, index1 = 0; i<nBalls; i++, index1 += 4) {
            for(int j = i+1, index2 = index1+4; j<nBalls; j++, index2 += 4) {
               double deltax = state[index2]-state[index1];     // x2-x1
               double deltay = state[index2+2]-state[index1+2]; // y2 - y1
               double deltaVx = state[index2+1]-state[index1+1];
               double deltaVy = state[index2+3]-state[index1+3];
               double distance = deltax*deltax+deltay*deltay-R;
               if((distance<minimum)&&(deltax*deltaVx+deltay*deltaVy<0)) {
                  b1 = i;
                  collision1 = index1;
                  b2 = j;
                  collision2 = index2;
                  minimum = distance;
               }
            }                                                   // End for j
         }                                                      // End for i
         return minimum;
      }

      public boolean action() {
         double deltax = state[collision2]-state[collision1];
         double deltay = state[collision2+2]-state[collision1+2];
         double distance = Math.sqrt(deltax*deltax+deltay*deltay);
         double rx = deltax/distance, ry = deltay/distance;                          // Unit vector joining centers
         double sx = -ry, sy = rx;                                                   // Vector ortogonal to the previous one
         double vr1 = (state[collision1+1]*rx+state[collision1+3]*ry),
                vs1 = (state[collision1+1]*sx+state[collision1+3]*sy);               // Projections for disk 1
         double vr2 = (state[collision2+1]*rx+state[collision2+3]*ry),
                vs2 = (state[collision2+1]*sx+state[collision2+3]*sy);               // Projections for disk 2
         double vr1d = (2*mass[b2]*vr2+(mass[b1]-mass[b2])*vr1)/(mass[b1]+mass[b2]); // New velocity for disk 1
         double vr2d = (2*mass[b1]*vr1+(mass[b2]-mass[b1])*vr2)/(mass[b1]+mass[b2]); // New velocity for disk 2
         // Undo the projections
         state[collision1+1] = vr1d*rx+vs1*sx;
         state[collision1+3] = vr1d*ry+vs1*sy;
         state[collision2+1] = vr2d*rx+vs2*sx;
         state[collision2+3] = vr2d*ry+vs2*sy;
         return false; // return exactly at the event point
      }
   } // End of inner class CollisionEvent

   /**
    * Starts the Java application.
    * @param args  command line parameters
    */
   public static void main(String[] args) {
      SimulationControl.createApp(new CollisionApp(), args);
   }

   // ---------------------------------
   //  Different initial configurations
   // ---------------------------------
   public void experiment1() {
      for(int i = 0, j = 0; i<nBalls; i++, j += 4) {
         state[j] = (xmax+xmin)/2+(nBalls-2*i-1)*radius;
         state[j+1] = 0.0; // vx_i
         state[j+2] = (ymin+ymax)*0.5;
         state[j+3] = 0.0; // vy_i
      }
      state[state.length-1] = 0.0; // time
      state[0] = xmin+2*radius;
      state[1] = 2.0;
      state[state.length-5] = xmax-2*radius;
      state[state.length-4] = -2.0;
   }

   public void experiment2() {
      for(int i = 0, j = 0; i<nBalls; i++, j += 4) {
         state[j] = xmin+radius+2*i*radius;
         state[j+1] = 0.0; // vx_i
         state[j+2] = (ymin+ymax)*0.5;
         state[j+3] = 0.0; // vy_i
      }
      state[state.length-1] = 0.0; // time
      state[state.length-5] = xmax-2*radius;
      state[state.length-4] = -2.0;
   }

   public void experiment3() {
      for(int i = 0, j = 0; i<nBalls; i++, j += 4) {
         state[j] = (xmax+xmin)/2+(nBalls-2*i-1)*radius;
         state[j+1] = 0.0; // vx_i
         state[j+2] = (ymin+ymax)*0.5;
         state[j+3] = 0.0; // vy_i
      }
      state[state.length-1] = 0.0; // time
      state[state.length-5] = xmax-2*radius;
      state[state.length-4] = -2.0;
   }

   public void experiment4() {
      double pos = xmin+radius*6;
      for(int i = 0, j = 0; i<nBalls; i++, j += 4) {
         state[j] = (xmax+xmin)/2;
         state[j+1] = 0.0; // vx_i
         state[j+2] = pos+radius;
         state[j+3] = 0.0; // vy_i
         pos += 2*radius;
      }
      state[state.length-1] = 0.0; // time
   }

   public void randomize() {
      for(int i = 0, j = 0; i<nBalls; i++, j += 4) {
         state[j] = xmin+radius+(xmax-xmin-2*radius)*Math.random();   // x_i
         state[j+1] = (xmax-xmin)*(Math.random()-0.5);                // vx_i
         state[j+2] = ymin+radius+(ymax-ymin-2*radius)*Math.random(); // y_i
         state[j+3] = (ymax-ymin)*(Math.random()-0.5);                // vy_i
      }
      state[state.length-1] = 0.0; // time
      // make sure the state is valid
      for(int i = 0; i<nBalls; i++) {
         for(int j = i+1; j<nBalls; j++) {
            double d1 = state[j*4]-state[i*4];     // x2-x1
            double d2 = state[j*4+2]-state[i*4+2]; // y2 - y1
            double result = d1*d1+d2*d2-2*radius*2*radius;
            if(result<0) {
               randomize();
               return;
            }
         }
      }
   }
}
