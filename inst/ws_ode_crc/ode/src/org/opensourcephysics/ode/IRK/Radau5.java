package org.opensourcephysics.ode.IRK;
import org.opensourcephysics.numerics.ODE;

/**
 * Numerical solution of a stiff system of first order ordinary
 * differential equations. The method used is an implicit
 * Runge-Kutta method (Radau IIA) of order 5 with step size
 * control and continuous output.
 *
 * The code is transferred from the Fortran sources.
 * authors of original Fortran code:
 *    E. Hairer and G. Wanner
 *    Universite de Geneve, Dept. De Mathematiques
 *    ch-1211 Geneve 24, Switzerland
 *    e-mail:  rnst.hairer@math.unige.ch
 *             gerhard.wanner@math.unige.ch <br>
 *
 * original Fortran code is part of the book:
 *    E. Hairer and G. Wanner, Solving ordinary differential
 *    equations II. Stiff and differential-algebraic problems.
 *    Springer series in computational mathematics 14,
 *    Springer-Verlag 1991, second edition 1996.
 * @author Andrei Goussev
 */

public class Radau5 extends Radau5Adaptive implements org.opensourcephysics.ode.ODEInterpolator{

    final static double c1 = (4.0 - Math.sqrt(6.0)) / 10.0;
    final static double c2 = (4.0 + Math.sqrt(6.0)) / 10.0;
    final static double c1m1 = c1 - 1.0;
    final static double c2m1 = c2 - 1.0;
    final static double c1mc2 = c1 - c2;

    double [][] interpolationCoeffs;
    double takenStepSize = 0;

    /**
     * Constructs the Radau5 ODESolver for a system of ordinary differential equations.
     *
     * @param ode the system of differential equations.
     */
    public Radau5(ODE ode) {
        super(ode);
        interpolationCoeffs = new double[4][ode.getState().length];
    }
    /**
     * Unsures up to time update of the interpolations coefficients for
     * the continiuos output.
     * @return the taken step size (because th e overrides)
     */
    public double step() {
        takenStepSize = super.step();
        constructInterpolationCoeffs();
        return takenStepSize;
    }
    /**
     * Constructs the interpolation coefficients.
     * Method uses the increment to the initial state on perfomed iteration
     * as input data.
     */
    public void constructInterpolationCoeffs(){
        for(int i = 0; i < numEqn; i++) {
            interpolationCoeffs[0][i] = state[i]; // not intialState !!!!
            interpolationCoeffs[1][i] = (intermediateStagesIncrement[1][i] - intermediateStagesIncrement[2][i]) / c2m1;
            double ak = (intermediateStagesIncrement[0][i] - intermediateStagesIncrement[1][i]) / c1mc2;
            double acont3 = intermediateStagesIncrement[0][i] / c1;
            acont3 = (ak - acont3) / c2;
            interpolationCoeffs[2][i] = (ak - interpolationCoeffs[1][i]) / c1m1;
            interpolationCoeffs[3][i] = interpolationCoeffs[2][i] - acont3;
        }
    }

    /**
     * Implements continiuos vector fuction that approximates the solution
     * of the ODE. Value of the vector fuction constructs using interpolation coefficients
     * @param time the point where approximation to the solution to be obtaied.
     *        (0 < time < takenstepSize corresponds to the interpolation, in other cases
     *         output will indeed the extrapolation)
     * @param result the result, i.e. approximated solution of ODE
     */
    public void doInterpolation(double time, double [] result){
        if (takenStepSize == 0){
            for (int i = 0; i < numEqn; i++)
               result[i] = state[i];
        }
        double s = (time-takenStepSize) / takenStepSize;
// TODO: if result.lengh != numEqn;
        for (int i = 0; i < numEqn; i++) {
            result[i] = interpolationCoeffs[0][i] + s * (interpolationCoeffs[1][i] + (s - c2m1) * (interpolationCoeffs[2][i] + (s - c1m1) * interpolationCoeffs[3][i]));
        }
    }

    /**
     * Predicits the increment to the stages vectors array for the next step
     * usigng the extrapolation
     * @param initialvalue the value to be adjusted
     */
    protected void estimateNewtonInitialValue(double[][] initialvalue) {
        double s = stepSize / takenStepSize;
        double s1 = c1 * s;
        double s2 = c2 * s;
        for (int i = 0; i < numEqn; i++) {
            initialvalue[0][i] = s1 * (interpolationCoeffs[1][i] + (s1 - c2m1) * (interpolationCoeffs[2][i] + (s1 - c1m1) * interpolationCoeffs[3][i]));
            initialvalue[1][i] = s2 * (interpolationCoeffs[1][i] + (s2 - c2m1) * (interpolationCoeffs[2][i] + (s2 - c1m1) * interpolationCoeffs[3][i]));
            initialvalue[2][i] = s * (interpolationCoeffs[1][i] + (s - c2m1) * (interpolationCoeffs[2][i] + (s - c1m1) * interpolationCoeffs[3][i]));
        }
    }
}
