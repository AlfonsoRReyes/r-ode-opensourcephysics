#' KeplerVerletApp.R
#' 
#' Demostration of the use of ODE solver RK45
#' 
#' 
source("./R/ode_generics.R")
source("./R/KeplerVerlet.R")
source("./R/Verlet.R")

particle <- KeplerVerlet()

x <- 1
vx <- 0
y <- 0
vy <- 2 * pi
dt <- 0.01
tol <- 1e-3

odeSolver <- Verlet(particle)

particle@odeSolver <- odeSolver

# odeSolver <- setTolerance(odeSolver, tol)
particle@odeSolver <- init(particle@odeSolver, dt)

particle <- init(particle, c(x, vx, y, vy, 0))

initialEnergy <- getEnergy(particle)
i <- 0
while (i < 50) {
    particle <- doStep(particle)
    energy <- getEnergy(particle)
    cat(sprintf("%12f %12f \n", getTime(particle), initialEnergy-energy))
    i <- i + 1
}
