#'
#' ProjectileApp.R
#                                                      test Projectile with RK4
#                                                      originally uses Euler
library(data.table)
library(ggplot2)

source("./R/ode_generics.R")
source("./inst/examples/Projectile.R")


x <- 0; vx <- 10; y <- 0; vy <- 10
state <- c(x, vx, y, vy, 0)
dt <- 0.01

projectile <- Projectile()

projectile <- setState(projectile, x, vx, y, vy)
projectile@odeSolver <- init(projectile@odeSolver, 0.123)
projectile@odeSolver <- setStepSize(projectile@odeSolver, dt) 

rowV <- vector("list")
i <- 1
while (projectile@state[3] >= 0)    {                
    # state[5]:           state[1]: x;  # state[3]: y
    cat(sprintf("%12f %12f %12f \n", projectile@state[5], 
                projectile@state[1], projectile@state[3]))
    rowV[[i]] <- list(state1 = projectile@state[1], 
                      state3 = projectile@state[3], 
                      state5 = projectile@state[5])
    projectile <- step(projectile)
    i <- i + 1
}

datatable <- data.table::rbindlist(rowV)
datatable


qplot(state1, state3, data = datatable)
qplot(state1, state5, data = datatable)
