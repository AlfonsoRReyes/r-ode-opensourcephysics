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
while (getTime(particle) <= 1.20) {
    # odeSolver <- step(odeSolver)
    particle <- doStep(particle)
    # odeSolver <- particle@odeSolver
    energy <- getEnergy(particle)
    cat(sprintf("time=%12f energy=%12f state[5]=%12f \n", getTime(particle), energy, particle@state[5]))
    i <- i + 1
}

#' output from Java run
#' 
# time=    0.970000 energy=  -19.739208 state[4]=    0.970000 
# time=    0.980000 energy=  -19.739208 state[4]=    0.980000 
# time=    0.990000 energy=  -19.739209 state[4]=    0.990000 
# time=    1.000000 energy=  -19.739209 state[4]=    1.000000 
# time=    1.010000 energy=  -19.739209 state[4]=    1.010000 
# time=    1.020000 energy=  -19.739209 state[4]=    1.020000 
# time=    1.030000 energy=  -19.739208 state[4]=    1.030000 
# time=    1.040000 energy=  -19.739208 state[4]=    1.040000 

#' output from R run
#' 
# time=    0.970000 energy=  -19.739208 state[5]=    0.970000 
# time=    0.980000 energy=  -19.739208 state[5]=    0.980000 
# time=    0.990000 energy=  -19.739209 state[5]=    0.990000 
# time=    1.000000 energy=  -19.739209 state[5]=    1.000000 
# time=    1.010000 energy=  -19.739209 state[5]=    1.010000 
# time=    1.020000 energy=  -19.739209 state[5]=    1.020000 
# time=    1.030000 energy=  -19.739208 state[5]=    1.030000 
# time=    1.040000 energy=  -19.739208 state[5]=    1.040000 
