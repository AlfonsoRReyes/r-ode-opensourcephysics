package org.opensourcephysics.ode;

//~--- non-JDK imports --------------------------------------------------------

import org.opensourcephysics.numerics.ODE;
import org.opensourcephysics.numerics.ODEAdaptiveSolver;
import org.opensourcephysics.ode.IRK.Radau5;

//~--- classes ----------------------------------------------------------------

/**
 * ODEInterpolationSolver adjusts its internal step size in order to obtain the desired accuracy and
 * interpolates the final state in order to maintain a fixed step size.
 *
 * @author       Wolfgang Christian
 * @version 1.0
 */
public class ODEInterpolationSolver implements ODEAdaptiveSolver, ODE {
    private double          fixedStepSize = 0.1;    // default value
    private double          remainder     = fixedStepSize;
    private double          takenStepSize = 0;
    private double[]        odeSolverState;
    private ODEInterpolator solverCore;
    private ODE             userODE;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs a ODEInterpolationSolver without an ODE so that a factory method
     * can create a custom solver.
     */
    private ODEInterpolationSolver() {
        remainder = fixedStepSize;
    }

    public ODEInterpolationSolver(ODE ode) {
        this.odeSolverState = new double[ode.getState().length];
        solverCore          = new Dopri853(setODE(ode));    // the default solver
        initialize(0.1);
    }

    //~--- methods ------------------------------------------------------------

    public static ODEAdaptiveSolver Dopri5(ODE ode) {
        ODEInterpolationSolver interpolationSolver =
            new ODEInterpolationSolver(ode);

        interpolationSolver.solverCore =
            new Dopri5(interpolationSolver.setODE(ode));

        return interpolationSolver;
    }

    /**
     * A factory method that creates an interpolation solver using the Dopri853 engine.
     * @param ode ODE
     * @return ODESolver
     */
    public static ODEAdaptiveSolver Dopri853(ODE ode) {
        ODEInterpolationSolver interpolationSolver =
            new ODEInterpolationSolver(ode);

        interpolationSolver.solverCore =
            new Dopri853(interpolationSolver.setODE(ode));

        return interpolationSolver;
    }

    public static ODEAdaptiveSolver Radau5(ODE ode) {
        ODEInterpolationSolver interpolationSolver =
            new ODEInterpolationSolver(ode);

        interpolationSolver.solverCore =
            new Radau5(interpolationSolver.setODE(ode));

        return interpolationSolver;
    }

    /**
     * Initializes the ODE solver.
     *
     * Temporary state and rate arrays are allocated by invoking the superclass method.
     *
     * @param _stepSize
     */
    public void initialize(double _stepSize) {
        fixedStepSize = _stepSize;
        remainder     = fixedStepSize;
        System.arraycopy(userODE.getState(), 0, odeSolverState, 0,
                         odeSolverState.length);
        solverCore.initialize(0.1);
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
        while (fixedStepSize * remainder > 0) {
            takenStepSize = solverCore.step();
            remainder     -= takenStepSize;
        }
        ;

        // it changes ode.state
        solverCore.doInterpolation(remainder + takenStepSize,
                                   userODE.getState());
        remainder += fixedStepSize;

        return fixedStepSize;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Gets the error code.
     * Error codes:
     *   ODEAdaptiveSolver.NO_ERROR
     *   ODEAdaptiveSolver.DID_NOT_CONVERGE
     *   ODEAdaptiveSolver.BISECTION_EVENT_NOT_FOUND=2;
     * @return int
     */
    public int getErrorCode() {
        return solverCore.getErrorCode();
    }

    public void getRate(double[] state, double[] rate) {
        userODE.getRate(state, rate);
    }

    public double[] getState() {
        return odeSolverState;
    }

    /**
     * Gets the step size.
     *
     * The step size is the fixed step size, not the size of the RK4/5 steps that are combined into a single step.
     *
     * @return the step size
     */
    public double getStepSize() {
        return fixedStepSize;
    }

    /**
     * Method getTolerance
     *
     *
     * @return
     */
    public double getTolerance() {
        return solverCore.getTolerance();
    }

    //~--- set methods --------------------------------------------------------

    private ODE setODE(ODE ode) {
        userODE = ode;
        System.arraycopy(ode.getState(), 0, odeSolverState, 0,
                         odeSolverState.length);

        return this;
    }

    /**
     * Method setStepSize
     *
     * @param stepSize
     */
    public void setStepSize(double stepSize) {
        if (stepSize == 0) {
            System.err.println(
                "Error: The stepsize in ODE solvers cannot be zero.");
            stepSize = 0.1;
        }

        if (fixedStepSize * stepSize < 0) {
            solverCore.setStepSize(-solverCore.getStepSize());
        }

        remainder     += (stepSize - fixedStepSize);
        fixedStepSize = stepSize;    // the fixed step size
    }

    public void setTolerance(double _tol) {
        solverCore.setTolerance(_tol);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
