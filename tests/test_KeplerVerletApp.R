library(testthat)
#' test_KeplerVerletApp.R
#' 
#' Demostration of the use of ODE solver RK45
#' 
#' 
source("./R/ode_generics.R")
source("./R/KeplerVerlet.R")
source("./R/Verlet.R")

particle <- KeplerVerlet()
expect_equal(slotNames(particle), c("GM", "odeSolver", "counter", "state", "rate"))
expect_equal(particle@state, c(0,0,0,0,0))
expect_equal(getState(particle), c(0,0,0,0,0))
expect_true(getTime(particle) == 0)
expect_true(getEnergy(particle) == -Inf)
expect_equal(getRate(particle, c(0,0,0,0,0))@rate, c(0, NaN,   0, NaN,   1))


x <- 1
vx <- 0
y <- 0
vy <- 2 * pi
dt <- 0.01
tol <- 1e-3

odeSolver <- Verlet(particle)

particle@odeSolver <- odeSolver

# odeSolver <- setTolerance(odeSolver, tol)
cat("stepSize (before)=", particle@odeSolver@stepSize, "\n")
particle@odeSolver <- init(particle@odeSolver, dt)
cat("stepSize(afer)=", particle@odeSolver@stepSize, "\n")

cat("state(before)=", particle@state, "\n")
particle <- init(particle, c(x, vx, y, vy, 0))

cat(" state(after)=", particle@state, "\n")
# cat(particle@odeSolver@ode@state)

particle <- doStep(particle)

expect_equal(particle@state, c(1.000000, 0.000000, 0.000000, 6.283185, 0.000000), tolerance = 1e-6)


initialEnergy <- getEnergy(particle)
print(initialEnergy)
# i <- 0
# while (i < 10) {
#     particle <- doStep(particle)
#     energy <- getEnergy(particle)
#     # cat(sprintf("%12f %12f \n", getTime(particle), initialEnergy-energy))
#     i <- i + 1
# }
