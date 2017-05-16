package org.opensourcephysics.ode;

import org.opensourcephysics.numerics.*;

/**
 * ODEInterpolationSolver extends the ODEAdaptiveSolver to add interpolation between steps.
 */

public interface  ODEInterpolator extends ODEAdaptiveSolver {

  /**
   * Does an interpolation in order to estimate the state at an intermediate value of the independent variable.
   *
   * @param shortStepValue double
   */
  public void doInterpolation(double shortStepValue, double [] result);

}
