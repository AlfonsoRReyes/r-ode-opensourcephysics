package org.opensourcephysics.ode;
import org.opensourcephysics.numerics.*;
import org.opensourcephysics.ode.IRK.Radau5;

/**
 * MultistepSolvers contains static methods to create ODESolvers that perform multiple ODE steps
 * so that a uniform step size is maintained.
 *
 * @author       Wolfgang Christian
 * @version 1.0
 */
public class MultistepSolvers extends ODEMultistepSolver {

    private MultistepSolvers(){  // prohibit instantiation
    }

    /**
     * A factory method that creates a multisetp solver using the Dopri5 engine.
     * @param ode ODE
     * @return ODESolver
     */
    public static ODEMultistepSolver Dopri5(ODE ode){
        MultistepSolvers multistepSolver= new MultistepSolvers();
        multistepSolver.odeEngine  = new Dopri5(multistepSolver.setODE(ode));
        return multistepSolver;
    }

    /**
     * A factory method that creates a multisetp solver using the Dopri853 engine.
     * @param ode ODE
     * @return ODESolver
     */
    public static ODEMultistepSolver Dopri853(ODE ode) {
        MultistepSolvers multistepSolver = new MultistepSolvers();
        multistepSolver.odeEngine  = new Dopri853(multistepSolver.setODE(ode));
        return multistepSolver;
    }

    /**
     * A factory method that creates a multisetp solver using the Radau5 engine.
     * @param ode ODE
     * @return ODESolver
     */
    public static ODEMultistepSolver Radau5(ODE ode) {
        MultistepSolvers multistepSolver = new MultistepSolvers();
        multistepSolver.odeEngine  = new Radau5(multistepSolver.setODE(ode));
        return multistepSolver;
    }

    /**
     * A factory method that creates a multisetp solver using the RK45 engine.
     * @param ode ODE
     * @return ODESolver
     */
    public static ODEMultistepSolver RK45(ODE ode){
       MultistepSolvers multistepSolver = new MultistepSolvers();
       multistepSolver.odeEngine = new RK45(multistepSolver.setODE(ode));
       return multistepSolver;
    }

}
