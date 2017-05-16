# AbstractODESolver.R
#
# source("./R/ode_generics.R")
source("./R/ODE.R")
source("./R/ODESolver.R")


setClass("AbstractODESolver", slots = c(
    stepSize = "numeric",
    numEqn   = "numeric",
    ode      = "ODE"
), prototype = prototype(
    stepSize = 0.1,
    numEqn = 0
), contains = c("ODESolver")
)


setMethod("initialize", "AbstractODESolver", function(.Object, .ode, ...) {
    .Object <- init(.Object, 0.1)                                 
    return(.Object)
})

setMethod("step", "AbstractODESolver", function(object, ...) {
    # object
})

setMethod("setStepSize", "AbstractODESolver", function(object, stepSize, ...) {
    object@stepSize = stepSize
    object
})

setMethod("init", "AbstractODESolver", function(object, stepSize, ...) {
    object@stepSize <- stepSize
    state <- getState(object@ode)
    if (is.null(state)) {
        object@numEqn <-  0
    } else {
        object@numEqn = length(state)
    }
    object
})

setMethod("getStepSize", "AbstractODESolver", function(object, ...) {
    return(object@stepSize)
})


# constructor
AbstractODESolver <- function(.ode) {
    odesolver <- new("AbstractODESolver", .ode)
    odesolver@ode <- .ode
    odesolver
}
