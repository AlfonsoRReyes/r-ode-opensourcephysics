// with debug prints

/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.opensourcephysics.numerics;

import java.util.Arrays;

/**
 *
 * DormandPrince45 implements a RKF 4/5 ODE solver with variable step size using Dormand-Prince coefficients.
 * @author       W. Christian
 * @author       F. Esquembre
 * @version 1.0
 */
public class DormandPrince45 implements ODEAdaptiveSolver {
  int error_code = ODEAdaptiveSolver.NO_ERROR;
  // embedding constants Dormand-Prince 4th and 5th order
  static final double[][] a = {
    {1.0/5.0}, {3.0/40.0, 9.0/40.0}, {3.0/10.0, -9.0/10.0, 6.0/5.0}, {226.0/729.0, -25.0/27.0, 880.0/729.0, 55.0/729.0}, {-181.0/270.0, 5.0/2.0, -266.0/297.0, -91.0/27.0, 189.0/55.0}
  };
  // ch contains the 5th order coefficients
  static final double[] b5 = {19.0/216.0, 0.0, 1000.0/2079.0, -125.0/216.0, 81.0/88.0, 5.0/56.0};
  // er array contains the error coefficients; the difference between the 4th and 5th order coefficients
  // er[0] is computed to be -11/360 = 31/540-19/216
  static final double[] er = {-11.0/360.0, 0.0, 10.0/63.0, -55.0/72.0, 27.0/40.0, -11.0/280.0};
  static final int numStages = 6; // number of intermediate rate computations
  private volatile double stepSize = 0.01;
  private int numEqn = 0;
  private double[] temp_state;
  private double[][] k;
  private double truncErr;
  private ODE ode;
  protected double tol = 1.0e-6;
  protected boolean enableExceptions = false;

  /**
   * Constructs the DormandPrince45 ODESolver for a system of ordinary  differential equations.
   *
   * @param _ode the system of differential equations.
   */
  public DormandPrince45(ODE _ode) {
    ode = _ode;
    initialize(stepSize);
  }

  /**
   * Initializes the ODE solver.
   *
   * Temporary state and rate arrays are allocated.
   * The number of differential equations is determined by invoking getState().length on the ODE.
   *
   * @param _stepSize
   */
  public void initialize(double _stepSize) {
    stepSize = _stepSize;
    double state[] = ode.getState();
    if(state==null) { // state vector not defined.
      return;
    }
    if(numEqn!=state.length) {
      numEqn = state.length;
      temp_state = new double[numEqn];
      k = new double[numStages][numEqn]; // six intermediate rates
    }
  }

  /**
   * Steps (advances) the differential equations by the stepSize.
   *
   * The ODESolver invokes the ODE's getRate method to obtain the initial state of the system.
   * The ODESolver then advances the solution and copies the new state into the
   * state array at the end of the solution step.
   *
   * @return the step size
   */
  public double step() {
//	  System.out.print("Top first Step --------------------- \n");
//	  System.out.println(Arrays.deepToString(k));
    error_code = ODEAdaptiveSolver.NO_ERROR;
    int iterations = 10;
    double currentStep = stepSize, error = 0;
    
    double state[] = ode.getState();
//    System.out.println(Arrays.toString(k[0]));
    ode.getRate(state, k[0]); // get the initial rate
//    System.out.println(Arrays.toString(k[0]) + "\n");
    // NEW iteration
    do {
//    	System.out.print("NEW iteration \n");
//        System.out.print("state:"); System.out.println(Arrays.toString(state));
//        System.out.println(Arrays.deepToString(k));
      iterations--;
      currentStep = stepSize;
      // Compute the k's
      int cum = 0;
      for(int s = 1; s<numStages; s++) {
        for(int i = 0; i<numEqn; i++) {
          temp_state[i] = state[i];
          for(int j = 0; j<s; j++) {
            temp_state[i] = temp_state[i] + stepSize * a[s-1][j] * k[j][i];
//            System.out.format("[%d][%d] tempState=%12f, stepSize=%12f, a=%12f, k=%12f \n", s, j, temp_state[i], stepSize, a[s-1][j], k[j][i]);
            cum = cum +1;
          }
        }
        // print k array
//        System.out.format("k[%d]=", s);
//        System.out.println(Arrays.toString(k[s]));
        ode.getRate(temp_state, k[s]);
      } // end for "s"
//      System.out.format("cum=%d \n", cum);
      
      // Compute the error
      error = 0;
      for(int i = 0; i<numEqn; i++) {
        truncErr = 0;
        for(int s = 0; s<numStages; s++) {
          truncErr = truncErr + stepSize * er[s] * k[s][i];
//          System.out.format("[%d] [%s] error = %12f truncError=%12f er=%12f, k=%12f \n", i, s, error, truncErr, er[s] , k[s][i]);
        }
        error = Math.max(error, Math.abs(truncErr));
        
      }
//      System.out.format("[after trunc] error = %12f \n", error);
      if(error<=Float.MIN_VALUE) { // error too small to be meaningful,
//    	  System.out.print("error <= Float.MIN_VALUE --------- \n");
        error = tol/1.0e5;         // increase stepSize x10
      }
      // find h step for the next try.
      if(error>tol) {              // shrink, no more than x10
//    	  System.out.print("error > tol ++++++ \n");
        double fac = 0.9*Math.pow(error/tol, -0.25);
        stepSize = stepSize*Math.max(fac, 0.1);
      } else if(error<tol/10.0) {  // grow, but no more than factor of 10
//    	  System.out.print("inside if else ++++++ \n");
        double fac = 0.9*Math.pow(error/tol, -0.2);
        if(fac>1) {                // sometimes fac is <1 because error/tol is close to one
          stepSize = stepSize*Math.min(fac, 10);
        }
      }
//      System.out.format("error=%10f, tol=%10f, iterations=%d \n", error, tol, iterations);
    } while((error>tol)&&(iterations>0));
    // advance the state
//    System.out.print("OUTSIDE LOOP ------- \n");
    for(int i = 0; i<numEqn; i++) {
      for(int s = 0; s<numStages; s++) {
        state[i] += currentStep * b5[s] * k[s][i];
      }
    }
    if(iterations==0) {
      error_code = ODEAdaptiveSolver.DID_NOT_CONVERGE;
      if(enableExceptions) {
        throw new ODESolverException("DormanPrince45 ODE solver did not converge."); //$NON-NLS-1$
      }
    }
//    System.out.format("currentStep=%12f \n", currentStep);
    return currentStep; // the value of the step actually taken.
  }

  /**
   * Enables runtime exceptions if the solver does not converge.
   * @param enable boolean
   */
  public void enableRuntimeExpecptions(boolean enable) {
    this.enableExceptions = enable;
  }

  /**
   * Sets the step size.
   *
   * The step size may change when the step method is invoked.
   *
   * @param stepSize
   */
  public void setStepSize(double stepSize) {
    this.stepSize = stepSize;
  }

  /**
   * Gets the step size.
   *
   * The stepsize is adaptive and may change as the step() method is invoked.
   *
   * @return the step size
   */
  public double getStepSize() {
    return stepSize;
  }

  /**
   * Method setTolerance
   *
   * @param _tol
   */
  public void setTolerance(double _tol) {
    tol = Math.abs(_tol);
    if(tol<1.0E-12) {
      String err_msg = "Error: Dormand-Prince ODE solver tolerance cannot be smaller than 1.0e-12."; //$NON-NLS-1$
      if(enableExceptions) {
        throw new ODESolverException(err_msg);
      }
      System.err.println(err_msg);
      tol = 1.0e-12;
    }
  }

  /**
   * Method getTolerance
   *
   *
   * @return
   */
  public double getTolerance() {
    return tol;
  }

  /**
   * Gets the error code.
   * Error codes:
   *   ODEAdaptiveSolver.NO_ERROR
   *   ODEAdaptiveSolver.DID_NOT_CONVERGE
   * @return int
   */
  public int getErrorCode() {
    return error_code;
  }

}

/*
 * Open Source Physics software is free software; you can redistribute
 * it and/or modify it under the terms of the GNU General Public License (GPL) as
 * published by the Free Software Foundation; either version 2 of the License,
 * or(at your option) any later version.

 * Code that uses any portion of the code in the org.opensourcephysics package
 * or any subpackage (subdirectory) of this package must must also be be released
 * under the GNU GPL license.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston MA 02111-1307 USA
 * or view the license online at http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2007  The Open Source Physics project
 *                     http://www.opensourcephysics.org
 */
