#' ComparisonRK45App02.R
#' 
#' Updates ODE state instead of using internal state of ODE in ODE solver
#' File ComparisonRK45App.R does npt makde ode abstraction
#' 
source("./R/ode_generics.R")
source("./R/RK45.R")
source("./R/ODETest.R")

ode <- new("ODETest")

ode_solver <- RK45(ode)

ode_solver <- setStepSize(ode_solver, 1)
ode_solver <- setTolerance(ode_solver, 1e-8)

time <-  0

while (time < 50) {
    ode_solver <- step(ode_solver)
    stepSize <-  getStepSize(ode_solver)
    time <- time + stepSize
    ode <- ode_solver@ode
    state <- getState(ode)
    cat("time =", time, "\t xl =", state[1], "\t error =", 
        (state[1] - getExactSolution(ode, time)), "\t n =", ode@n, "\n")
}
cat("rate evaluated #", ode@n)