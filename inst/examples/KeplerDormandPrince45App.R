#' KeplerDormandPrince45App.R
#' 
#' Demostration of the use of ODE solver RK45
#' Brought from original routine for testing Verlet ODE solver
#' 
#' 
source("./R/ode_generics.R")
source("./R/DormandPrince45.R")
source("./inst/examples/KeplerDormandPrince45.R")

cat("\014")

particle <- Kepler()

x  <- 1
vx <- 0
y  <- 0
vy <- 2 * pi
dt <- 0.01
tol <- 1e-3

particle <- init(particle, c(x, vx, y, vy, 0))

odeSolver <- DormandPrince45(particle)
odeSolver <- init(odeSolver, dt)

odeSolver <- setTolerance(odeSolver, tol)   # this works for adaptive solvers

particle@odeSolver <- odeSolver



initialEnergy <- getEnergy(particle)
i <- 0
while (getTime(particle) < 1.5) {
    # odeSolver <- step(odeSolver)
    particle <- doStep(particle)
    # odeSolver <- particle@odeSolver
    energy <- getEnergy(particle)
    cat(sprintf("time=%12f energy=%12f state[5]=%12f x=%10f y=%10f \n", getTime(particle), 
                energy, particle@state[5], particle@state[1], particle@state[3]))
    i <- i + 1
}
# Values from Java at t approx = 1.5 
# time=    1.444831 energy=  -19.737930 state[4]=    1.444831 
# time=    1.507215 energy=  -19.737875 state[4]=    1.507215 
# time=    1.569599 energy=  -19.737819 state[4]=    1.569599
