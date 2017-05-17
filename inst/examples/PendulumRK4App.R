#' 
#' PendulumRK4App.R
#' 

library(ggplot2)
#                                                                   PendulumApp
source("./R/ode_generics.R")
source("./R/RK4.R")
source("./inst/examples/PendulumRK4.R")                   # this script uses Euler-Richardson

ode <- new("ODE")
pendulum <- PendulumRK4()

dt <- 0.1
theta <- 0.2
thetaDot <- 0

pendulum@state[3] <- 0      # set time to zero, t = 0

pendulum <- setState(pendulum, theta, thetaDot)
pendulum <- setStepSize(pendulum, dt = dt) # using stepSize in RK4

pendulum@odeSolver <- setStepSize(pendulum@odeSolver, dt) # set new step size

rowvec <- vector("list")
i <- 1
while (pendulum@state[3] <= 1000)    {
    rowvec[[i]] <- list(state1 = pendulum@state[1], # angle
                        state2 = pendulum@state[2],      # derivative of the angle
                        state3 = pendulum@state[3])       # time
    i <- i + 1
    pendulum <- step(pendulum)
}
DTRK4 <- data.table::rbindlist(rowvec)

x11()
print(ggplot(DTRK4, aes(x = state3, y = state1)) + geom_line(col = "blue"))
x11()
print(ggplot(DTRK4, aes(x = state3, y = state2)) + geom_line(col = "red"))
save(DTRK4, file = "./data/pendulumRK4_1e-1.rda")
