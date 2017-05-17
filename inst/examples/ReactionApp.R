#'
#' ReactionApp.R
#' 
# ReactionApp solves an autocatalytic oscillating chemical 
# reaction (Brusselator model) using
# a fouth-order Runge-Kutta algorithm.

source("./R/ode_generics.R")
source("./R/Reaction.R")
source("./R/RK4.R")

X <- 1; Y <- 5;
dt <- 0.1

reaction <- Reaction(c(X, Y, 0))

solver <- RK4(reaction)

while (solver@ode@state[3] < 100) {
    cat(sprintf("%12f %12f %12f \n", solver@ode@state[1], 
                solver@ode@state[2], 
                solver@ode@state[3]))
    solver <- step(solver)    
}
# at t=100, dt=0.1,  c(2.131958,     1.105316,   100.000000)
