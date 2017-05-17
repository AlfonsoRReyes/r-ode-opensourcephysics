######################
# FallingParticleODE.R 
#
source("./R/ODE.R")

setClass("FallingParticleODE", slots = c(
    g = "numeric"
), 
    prototype = prototype(
        g = 9.8
    ),
    contains = c("ODE")
    )


setMethod("initialize", "FallingParticleODE", function(.Object, ...) {
    .Object@state <- vector("numeric", 3)
    return(.Object)
})

setMethod("getState", "FallingParticleODE", function(object, ...) {
    # Gets the state variables.
    return(object@state)
})

setMethod("getRate", "FallingParticleODE", function(object, state, rate, ...) {
    # Gets the rate of change using the argument's state variables.
    # cat("getRate()  called with ", class(object), "\n")
    object@rate[1] <- state[2]
    object@rate[2] <- - object@g
    object@rate[3] <- 1
    # object@rate   # last change
    invisible(object)
    
})

# constructor
FallingParticleODE <- function(y, v) {
    .FallingParticleODE <- new("FallingParticleODE")
    .FallingParticleODE@state[1] <- y
    .FallingParticleODE@state[2] <- v
    .FallingParticleODE@state[3] <- 0
    .FallingParticleODE
}