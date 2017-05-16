package org.opensourcephysics.ode;
import org.opensourcephysics.numerics.*;

/**
 * Title:        Dopri5
 * Description:  Core of Dorman Prinse 5 ODE solver with variable step size.
 * @author        Andrei Goussev
 * @version 1.0
 */
public class Dopri5 extends ExplicitRKSolver implements ODEInterpolator {

  static final double[][] a = {
    {0.2},
    {3.0 / 40.0, 9.0 / 40.0},
    {44.0 / 45.0, -56.0 / 15.0, 32.0 / 9.0},
    {19372.0 / 6561.0, -25360.0 / 2187.0, 64448.0 / 6561.0, -212.0 / 729.0},
    {9017.0 / 3168.0, -355.0 / 33.0, 46732.0 / 5247.0, 49.0 / 176.0, -5103.0 / 18656.0}
  };
  static final double[] b = {35.0 / 384.0, 0, 500.0 / 1113.0, 125.0 / 192.0, -2187.0 / 6784.0, 11.0 / 84.0};

  static final double[] er = {71.0 / 57600.0, 0, -71.0 / 16695.0, 71.0 / 1920, -17253.0 / 339200.0, 22.0 / 525.0, -1.0 / 40.0};

  static final double[] dense = {-12715105075.0 / 11282082432.0, 0, 87487479700.0 / 32700410799.0, -10690763975.0 / 1880347072.0, 701980252875.0 / 199316789632.0, -1453857185.0 / 822651844.0, 69997945.0 / 29380423.0};

  private double [][] coeffs;

  /**
   * Constructs the Dopri5core ODESolver for a system of ordinary  differential equations.
   *
   * @param ode the system of differential equations.
   */
  public Dopri5(ODE ode) {
    // 5 - order of the method
    // 6 - number of intermediate stages for solution computation
    // 1 - number of addinitional intermediate stages for interpolation constructing
    super(ode, a, b, 5, 6, 1);
    coeffs = new double [5][numEqn];
    initialize(stepSize);
  }


  /**
   * Estimates an error on the current iteration
   *
   * Important notice:
   * implicit input variable is double intermediateStages[][]
   * @return then estimated error
   */
  protected double estimateError (){
    double truncErr = 0;
    double err = 0;
    double sk = 0;
    double atol = tolerance;
    double rtol = tolerance;
    // compute the error norm
    ode.getRate(state, intermidiateStages[6]);
    for(int i = 0; i < numEqn; i++) {
      sk = atol + rtol * Math.max(Math.abs(state[i]), Math.abs(initialState[i]));
      truncErr = 0;
      for (int s = 0; s < nStages+1; s++) truncErr += er[s]*intermidiateStages[s][i];
      err += Math.pow(truncErr / sk, 2);
    }
    err = Math.sqrt(err / (double)numEqn);
    return err;
  }

  public void doInterpolation(double remainder, double [] result){
    if (!interpolationIsValid) {
      interpolationIsValid = true;
  // calculation of coeffs matrix.
      for (int i = 0; i < numEqn; i++){
        coeffs[0][i] = initialState[i]; // i'am not sure -> Y[i]
        coeffs[1][i] = state[i] - initialState[i];
        coeffs[2][i] = takenStepSize*intermidiateStages[0][i] - coeffs[1][i];
        coeffs[3][i] = coeffs[1][i] - takenStepSize*intermidiateStages[6][i] - coeffs[2][i];
        coeffs[4][i] = 0;
        for (int s = 0; s < 7; s++) coeffs[4][i] += takenStepSize*dense[s]*intermidiateStages[s][i];
      }
    }
    double theta = remainder / takenStepSize;
    double theta1 = 1 - theta;
    if (result != state){
      for (int i = 0; i < numEqn; i ++)
        result[i] = coeffs[0][i] + theta * (coeffs[1][i] + theta1 * (coeffs[2][i] +
                  theta * (coeffs[3][i] + theta1 * coeffs[4][i])));
    } else
      System.err.println("Can't interpolate to the internal state vector. Please call initialize(double, double []) method");
  }
}
