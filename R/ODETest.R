#' ODETest.R

source("./R/ODE.R")

setClass("ODETest", slots = c(
    n     = "numeric"           # counts the number of getRate evaluations
    ),
    contains = c("ODE")
    )


setMethod("initialize", "ODETest", function(.Object, ...) {
    .Object@n <-  0
    .Object@state <- c(5.0, 0.0)
    return(.Object)
})


setMethod("getExactSolution", "ODETest", function(object, t, ...) {              
    return(5.0 * exp(-t))
})


setMethod("getState", "ODETest", function(object, ...) {                
    object@state
})


setMethod("getRate", "ODETest", function(object, state, rate, ...) {    
    rate[1] <- - state[1]     
    rate[2] <-  1            # rate of change of time, dt/dt
    
    object@n <- object@n + 1
    
    object@state <- state
    object@rate  <- rate
    
    # object@rate   
    return(object)
})


# constructor

ODETest <- function() {
    odetest <- new("ODETest")
    odetest
}