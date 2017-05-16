/*
* The org.opensourcephysics.numerics package contains numerical methods
* for the book Simulations in Physics.
* Copyright (c) 2001  H. Gould, J. Tobochnik, and W. Christian.
*/
package org.opensourcephysics.ode;
import org.opensourcephysics.numerics.*;
//import org.opensourcephysics.numerics.odetestgenerators.VerifUtils;

/**
 * Title:        ODEExplicitRKSolver
 * Description:  Base class for all explicit Runge Kutta's solvers.
 * @version 1.0
 */

public abstract class ExplicitRKSolver implements ODEInterpolator {
    int error_code=ODEAdaptiveSolver.NO_ERROR;
    protected int nStages;
    protected int methodOrder;
    protected double [][] a;
    protected double [] b;

    protected double stepSize = 0.1;
    protected double takenStepSize = 0;
    protected double tolerance = 1.0e-6;
    protected int numEqn   = 0;
    protected double [] state;
    protected double [] initialState;
    protected double [][] intermidiateStages;
    protected ODE ode;

// step size estimation's vars
    private double errOld = 1.e-4;
    private double fac = 0;
// interpolation vars;
    protected boolean interpolationIsValid = false;
    protected int nInterpolationStages;

    /**
     * Constructs the Dopri853core ODESolver for a system of ordinary  differential equations.
     *
     * @param _ode the system of differential equations.
     */
    public ExplicitRKSolver(ODE _ode, double [][] a, double [] b, int methodOrder, int nStages, int nInterpolationStages ) {
        this.nInterpolationStages=nInterpolationStages;
        this.nStages = nStages;
        this.methodOrder = methodOrder;
        this.a = a;
        this.b = b;
        ode = _ode;
        state = ode.getState();
        numEqn = state.length;
        initialState = new double[numEqn];
        intermidiateStages = new double[nStages+nInterpolationStages][numEqn];  // the intermidiate stages
    }

    /**
     * Initializes the ODE solver.
     *
     * Temporary state and rate arrays are allocated.
     * The number of differential equations is determined by invoking getState().length on the ODE.
     *
     * @param stepSize
     */
    public void initialize(double stepSize) {
      this.stepSize = stepSize;
      interpolationIsValid = false;
      if (state != ode.getState()) {
        state = ode.getState();
        numEqn = state.length;
        initialState = new double[numEqn];
        intermidiateStages = new double[nStages + nInterpolationStages][numEqn]; // the intermidiate stages
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
    final public double step() {
        error_code=ODEAdaptiveSolver.NO_ERROR;
        interpolationIsValid = false;
        if(state.length != numEqn) {
            initialize(stepSize);
        }
        int    i, j, s;  // counters
        double err = 0;
        int    iterations = 500;
        if (takenStepSize == 0 ) stepSize = getInitialStepSize(stepSize);
        System.arraycopy(state, 0, initialState, 0, numEqn);  // save the initial state
        ode.getRate(state, intermidiateStages[0]);                          // get the initial rates
        do {
            iterations--;
            takenStepSize = stepSize;
            for(s = 1; s < nStages; s++) {
                for(i = 0; i < numEqn; i++) {
                    state[i] = initialState[i];                 // reset to the initial state
                    for(j = 0; j < s; j++) {
                        state[i] += stepSize * a[s - 1][j] * intermidiateStages[j][i];
                    }
                }
                ode.getRate(state, intermidiateStages[s]);                  // get the intermediate rates
            }

            for(i = 0; i < numEqn; i++) {
                state[i] = initialState[i];
                for(s = 0; s < nStages; s++) state[i] += stepSize * b[s] * intermidiateStages[s][i];
            }

            err = estimateError();
            stepSize = estimateStepSize(err);
            if (iterations < 499) stepSize = Math.min(stepSize, takenStepSize);
        } while((err > 1) && (iterations > 0));
        if ((err > 1) || Double.isNaN(err)){
          System.err.println("Method did not converge");
          error_code=ODEAdaptiveSolver.DID_NOT_CONVERGE;
        }
        return takenStepSize;  // the value of the step that was actually taken.
    }

    /**
     * Gets the error code.
     * Error codes:
     *   ODEAdaptiveSolver.NO_ERROR
     *   ODEAdaptiveSolver.DID_NOT_CONVERGE
     *   ODEAdaptiveSolver.BISECTION_EVENT_NOT_FOUND=2;
     * @return int
     */
    public int getErrorCode() {
      return error_code;
    }


    /**
     * Estimates an error on the current iteration
     *
     * Important notice:
     * implicit input variable is double intermediateStages[][]
     * @return then estimated error
     */
    protected abstract double estimateError ();

    private double estimateStepSize(double err){
        // parameters of next step_size controlling
        // fac1, fac2, - parameters for step size selection
        // beta - for step control stabilisation
        // safe - safety factor
        double fac1 = 0.33;
        double fac2 = 6;
        double beta = 0;
        double safe = 0.9;
        double expO1 = 1.0 / methodOrder - beta * 0.75;
        double estStepSize = 0;
        double fac11 = 0;

        // taking decision for HNEW/H value
        if(err != 0) {
            fac11 = Math.exp(expO1 * Math.log(err));
            //stabilisation
            fac = fac11 / Math.exp(beta * Math.log(errOld));
            //we require fac1 <= HNEW/H <= fac2
            fac = Math.max(1/fac2, Math.min(1/fac1, fac / safe));
        }
        else {
            fac11 = 1/fac1;
            fac = 1/fac2;
        }//if

        if (err <=1) {
            // step suppose to be accepted
            errOld = Math.max(err, 1.0e-4);
            estStepSize = stepSize / fac;
        }
        else
        // step rejected
            estStepSize = stepSize/Math.min(1/ fac1, fac11 / safe);
        return estStepSize;
    }

    public double getInitialStepSize(double hMax){
        int i;
        double normF, normX, sk, h, der2, der12;
        double [] initialState = this.state;
        double [] state = new double[initialState.length];
        double [] rate = new double [state.length];
        double [] initialRate = new double [state.length];

       // int posneg = MathAddon.sign(hMax);
       // xxx changed by W. Christian in order to compile
        int posneg =(hMax<0)?-1:1;
        hMax = Math.abs(hMax);

//  initing initialState & initialRate values
//    System.arraycopy(state,0,initialState,0,numEqn);
        ode.getRate(initialState, initialRate);
        normF = 0.0;
        normX = 0.0 ;
        for(i = 0; i < numEqn; i++) {
            sk = tolerance + tolerance*Math.abs(initialState[i]);
            normF += Math.pow(initialRate[i]/sk, 2);
            normX += Math.pow(initialState[i]/sk, 2);
        }

        if((normF <= 1.e-10) || (normX <= 1.e-10)) {
            h = 1.0e-6;
        }
        else {
            h = Math.sqrt(normX / normF) * 0.01;
        }
        h = posneg*Math.min(h, hMax);
//  perform an explicit base method step
        for(i = 0; i < numEqn; i++) {
            state[i] = initialState[i] + h * initialRate[i];
        }
//estimate the second derivative of the solution
        ode.getRate(state, rate);
        der2 = 0.0;
        for(i = 0; i < state.length; i++) {
            sk = tolerance + tolerance * Math.abs(initialState[i]);
            der2 += Math.pow((rate[i] - initialRate[i]) / sk, 2);
        }
        der2 = Math.sqrt(der2) / h;
//step size is computed as follows
//h**iord * max ( norm (initialRate), norm (der2)) = 0.01
        der12 = Math.max(Math.abs(der2), Math.sqrt(normF));
        double h1 = 0;
        if(der12 <= 1.0e-15) {
            h1 = Math.max(1.0e-6, Math.abs(h) * 1.0e-3);
        }
        else {
            h1 = Math.exp((1.0 / methodOrder) * Math.log(0.01 / der12));
        }
        h = posneg*Math.min(100 * h, h1);
        if (hMax != 0) h = posneg*Math.min(Math.abs(h),hMax);
        return h;
    }

    public void setStepSize(double _stepSize) {
        stepSize = _stepSize;
    }

    public double getStepSize() {
        return stepSize;
    }

    public void setTolerance(double _tol) {
        tolerance = Math.abs(_tol);
    }

    public double getTolerance() {
        return tolerance;
    }

    public void doInterpolation(double shortStepValue, double [] result){
        if (result != state){
            for (int i = 0; i < numEqn; i++) result[i] = initialState[i] + (state[i]-initialState[i]) * shortStepValue / takenStepSize;
        } else  System.err.println("Cann't interpolate to the internal state vector. Please call initialize(double, double []) method");
    }
}
