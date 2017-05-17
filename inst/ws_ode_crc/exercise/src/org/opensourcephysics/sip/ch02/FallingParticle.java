
package org.opensourcephysics.sip.ch02;

/**
 * FallingParticle models a falling ball by subclassing the Particle class.
 *
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0 05/07/05
 */
public class FallingParticle extends Particle {
  final static double g = 9.8;   // constant
  private double y0 = 0, v0 = 0; // initial position and velocity

  /**
   * Constructs a Falling Particle with the given initial conditions.
   *
   * @param y double
   * @param v double
   */
  public FallingParticle(double y, double v) { // constructor
    System.out.println("A new FallingParticle object is created.");
    this.y = y; // instance value set equal to passed value
    this.v = v; // instance value set equal to passed value
    y0 = y;     // no need to use "this" because there is only one y0
    v0 = v;
  }

  /**
   * Steps (advances) the dynamical variables using the Euler method.
   */
  public void step() {
    y = y+v*dt; // Euler algorithm
    v = v-g*dt;
    t = t+dt;
  }

  /**
   * Computes the position of the ball at the current time using the analytic solution of the equation of motion.
   * @param y0 double
   * @param v0 double
   * @return double
   */
  public double analyticPosition() {
    return y0+v0*t-(g*t*t)/2.0;
  }

  /**
   * Computes the velocity of the ball at the current time using the analytic solution of the equation of motion.
   * @param y0 double
   * @param v0 double
   * @return double
   */
  public double analyticVelocity() {
    return v0-g*t;
  }
  
  public double printSome() 	{	// using new method
	  System.out.println("\nprint something\n");
	  return 0;
  }
}

