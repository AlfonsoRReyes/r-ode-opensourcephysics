# r-ode-opensourcephysics



## Motivation

Convert ODE solvers from the *OpenSourcePhysics* Java libraries to R to be able to perform data analysis on the differential equation solutions. The classes in R are implemented using **S4**.



## Background

**OSP**, for *OpenSourcePhysics* , is a collection of powerful and neat libraries for physics using computer simulations. The real time plotting libraries are sort of unique in reaching understanding of the phenomena developing over time.



## Current work

So far, few of the ODE solvers have been converted from Java to R:

* Euler
* Euler-Richardson
* Runge-Kutta 4
* Runge-Kutta 45 with Dormand-Prince-45



## Important

We are not looking for speed when looking at the R routines as ODE solvers; we are looking at understanding the data that is being generated. For speed, you can always go back to the original Java, or convert the ODE solver scripts from R (or Java) to C, C++ or Fortran; all options available within the R environment.

