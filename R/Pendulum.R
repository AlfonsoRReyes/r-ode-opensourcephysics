#' Pendulum.R
#' 
#' Original Pendulum class uses Euler-Richardson solver

source("./R/ODE.R")
source("./R/EulerRichardson.R")



setClass("Pendulum", slots = c(
    omega0Squared = "numeric",
    state = "numeric",
    odeSolver = "EulerRichardson"
    ),
    prototype = prototype(
        omega0Squared = 3,
        state = c(0, 0, 0)
    ),
    contains = c("ODE")
    )

setMethod("initialize", "Pendulum", function(.Object) {
    .Object@odeSolver <- EulerRichardson(.Object)                               
    return(.Object)
})

setMethod("setStepSize", signature("Pendulum"), function(object, dt, ...) {
    # use explicit parameter declaration
    # setStepSize generic may use two different step parameters: stepSize and dt
    object@odeSolver <- setStepSize(object@odeSolver, dt)
    object
})


setMethod("step", "Pendulum", function(object) {
    object@odeSolver <- step(object@odeSolver)
    object@rate  <- object@odeSolver@ode@rate                           
    object@state <- object@odeSolver@ode@state                          
    object
})

setMethod("setState", signature("Pendulum"), function(object, theta, thetaDot, ...) {
    object@state[1] <- theta     # angle
    object@state[2] <- thetaDot  # derivative of angle
    #                              state[3] is time
    object@odeSolver@ode@state <- object@state
    object
})

setMethod("getState", "Pendulum", function(object) {                
    object@state
})


setMethod("getRate", "Pendulum", function(object, state, rate) {    
    rate[1] <- state[2]     # rate of change of angle               
    rate[2] <- -object@omega0Squared * sin(state[1])  # rate of change of dtheta                 
    rate[3] <- 1            # rate of change of time, dt/dt
    
    object@state <- object@odeSolver@ode@state <- state
    object@rate  <- object@odeSolver@ode@rate  <- rate
    # object@rate   
    invisible(object)
})


# constructor
Pendulum <- function()  new("Pendulum")
