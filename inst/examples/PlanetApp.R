# #################
# PlanetApp.R
#
#
source("./R/ode_generics.R")
source("./inst/examples/Planet.R")

# x =  1, AU or Astronomical Units. Length of semimajor axis or the orbit of the Earth around the Sun.

x <- 1; vx <- 0; y <- 0; vy <- 6.28; t <- 0
state <- c(x, vx, y, vy, t)
dt <-  0.01

planet <- Planet()

planet@odeSolver <- setStepSize(planet@odeSolver, dt)
planet <- init(planet, initState = state)

# run infinite loop. stop with ESCAPE.
while (planet@state[5] <= 365) {     # Earth orbit is 365 days around the sun
    for (i in 1:5) {                 # advances time
        planet <- doStep(planet)
    }
    cat(sprintf("%12f %12f %12f %12f %12f \n", 
                planet@state[1], planet@state[2], planet@state[3],
                planet@state[4], planet@state[5]))
}
