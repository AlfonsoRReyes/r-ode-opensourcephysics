# source("./R/ode_generics.R")
source("./R/ODESolver.R")


setClass("ODEAdaptiveSolver", slots = c(
    NO_ERROR                  = "numeric",
    DID_NOT_CONVERGE          = "numeric",
    BISECTION_EVENT_NOT_FOUND = "numeric"
    ), 
    prototype = prototype(
        NO_ERROR                  = 0,
        DID_NOT_CONVERGE          = 1,
        BISECTION_EVENT_NOT_FOUND = 2
    ),
    contains = c("ODESolver")
)


setMethod("setTolerance", "ODEAdaptiveSolver", function(object, tol) {
})


setMethod("getTolerance", "ODEAdaptiveSolver", function(object) {
})

setMethod("getErrorCode", "ODEAdaptiveSolver", function(object) {
})