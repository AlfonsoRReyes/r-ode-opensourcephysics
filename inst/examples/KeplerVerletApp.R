#' KeplerVerletApp.R
#' 
#' Demostration of the use of ODE solver RK45
#' 
#' 
source("./R/ode_generics.R")
source("./R/Verlet.R")
source("./inst/examples/KeplerVerlet.R")

cat("\014")

particle <- KeplerVerlet()

x  <- 1
vx <- 0
y  <- 0
vy <- 2 * pi
dt <- 0.01
tol <- 1e-3

particle <- init(particle, c(x, vx, y, vy, 0))

odeSolver <- Verlet(particle)
odeSolver <- init(odeSolver, dt)

particle@odeSolver <- odeSolver

# odeSolver <- setTolerance(odeSolver, tol)
# particle@odeSolver <- init(particle@odeSolver, dt)



initialEnergy <- getEnergy(particle)
i <- 0
while (i < 25) {
    # odeSolver <- step(odeSolver)
    particle <- doStep(particle)
    # odeSolver <- particle@odeSolver
    energy <- getEnergy(particle)
    cat(sprintf("time=%12f energy=%12f state[5]=%12f \n", getTime(particle), energy, particle@state[5]))
    i <- i + 1
}
