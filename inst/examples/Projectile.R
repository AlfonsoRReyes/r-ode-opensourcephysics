####################
# Projectile.R
#
# Projectile class to be solved with Euler method
#
######################

source("./R/ODE.R")
# source("./R/Euler.R")
source("./R/RK4.R")

# setGeneric("setState", function(object, x, vx, y, vy, ...) standardGeneric("setState"))

setClass("Projectile", slots = c(
    g = "numeric",
    odeSolver = "RK4"
    ),
    prototype = prototype(
        g = 9.8
    ),
    contains = c("ODE")
    )

setMethod("initialize", "Projectile", function(.Object) {
    .Object@odeSolver <- RK4(.Object)                              
    return(.Object)
})

setMethod("setStepSize", "Projectile", function(object, stepSize, ...) {
    # use explicit parameter declaration
    # setStepSize generic has two step parameters: stepSize and dt
    object@odeSolver <- setStepSize(object@odeSolver, stepSize)
    object
})


setMethod("step", "Projectile", function(object) {
    object@odeSolver <- step(object@odeSolver)
    
    object@rate  <- object@odeSolver@ode@rate                           
    object@state <- object@odeSolver@ode@state                      
    
    object
})

setMethod("setState", signature("Projectile"), function(object, x, vx, y, vy, ...) {
    object@state[1] <- x
    object@state[2] <- vx
    object@state[3] <- y
    object@state[4] <- vy
    object@state[5] <- 0     # t + dt
    
    object@odeSolver@ode@state <- object@state
    object
})

setMethod("getState", "Projectile", function(object) {                
    object@state
})


setMethod("getRate", "Projectile", function(object, state, rate) {    
    rate[1] <- state[2]     # rate of change of x                                          # diff 11
    rate[2] <- 0            # rate of change of vx                    
    rate[3] <- state[4]     # rate of change of y
    rate[4] <- - object@g   # rate of change of vy
    rate[5] <- 1            # dt/dt = 1
    
    object@state <- object@odeSolver@ode@state <- state
    object@rate  <- object@odeSolver@ode@rate  <- rate
    # object@rate     
    invisible(object)
})


# constructor
Projectile <- function()  new("Projectile")
