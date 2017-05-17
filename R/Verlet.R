#' ############################################################################
#' 
#' Verlet.R
#'
#' Verlet ODE solver
#' 
###############

source("./R/AbstractODESolver.R")


setClass("Verlet", slots = c(
  rate1 = "numeric",
  rate2 = "numeric",                             
  rateCounter = "numeric"                           
),
    contains = c("AbstractODESolver") 
)

setMethod("initialize", "Verlet", function(.Object, ode, ...) {
    # initialize the class
    .Object@rateCounter <- -1
    .Object@ode <- ode
    return(.Object)
})


setMethod("init", "Verlet", function(object, stepSize, ...) {
    # inititalize the solver
    object <- callNextMethod(object, stepSize)        # call superclass init
    
    # set the rate vectors to the number of equations
    object@rate1 <- vector("numeric", object@numEqn)  # make the rate vector
    object@rate2 <- vector("numeric", object@numEqn)  # make the rate vector
    object@rateCounter <- -1
    object
})

setMethod("getRateCounter", "Verlet", function(object, ...) {
    return(object@rateCounter)
})


setMethod("step", "Verlet", function(object, ...) {
    # state[]: x1, d x1/dt, x2, d x2/dt .... xN, d xN/dt, t
    state <- getState(object@ode)                         # get the state vector
    # cat(sprintf("state=%s \n", state))
    # cat("state:");print(state)
    # cat(sprintf("stepSize=%8f \n", object@stepSize))
    
    if (length(state) != object@numEqn) {
        object <- init(object, object@stepSize)
    }
    
    object@rateCounter <- 0     #  getRate has not been called
    object@rate1 <- getRate(object@ode, state, object@rate1)@rate
    
    dt2 <- object@stepSize * object@stepSize  # the step size squared
    # cat(sprintf("dt2=%12f \n", dt2))
    # increment the positions using the velocity and acceleration
    for (i in seq(1, object@numEqn-1, 2)) {
    # for (i in seq(1, object@numEqn, 2)) {
        # cat(sprintf("i1=%3d \n", i-1))
        state[i] <- state[i] + 
            object@stepSize * object@rate1[i] +
            dt2 * object@rate1[i+1] / 2 
        # cat(sprintf("i1=%3d, state1=%12f, @rate1=%12f \n", i, state[i], object@rate1[i]))
        cat(sprintf("i1=%3d, state1=%12f, rate1=%11f\n", i-1, state[i], object@rate1[i]))
    }
    object@rateCounter <- 1  # getRate has been called once
    object@rate2 <- getRate(object@ode, state, object@rate2)@rate
    object@rateCounter <- 2
    
    for (i in seq(2, object@numEqn, 2)) {
        # cat(sprintf("i2=%3d \n", i-1))
        # increment the velocities with the average rate
        state[i] <- state[i] + object@stepSize * (object@rate1[i] +
            object@rate2[i]) / 2.0 
        # cat(sprintf("i2=%3d, state2=%12f, @rate2=%12f \n", i, state[i], object@rate2[i]))
        cat(sprintf("i2=%3d, state1=%12f, rate2=%11f \n", i-1, state[i], object@rate2[i]))
    }
    cat(sprintf("numEqn=%3d \t", object@numEqn))
    if (object@numEqn%%2 == 1) { # last equation if  we have an odd number of equations
        
        state[object@numEqn-1] <- state[object@numEqn-1] +
            object@stepSize * object@rate1[object@numEqn-1]
        
        
        
        
        cat(sprintf("@numEqn2=%3d, state[@numEqn-1]=%12f rate1=%12f\n\n", 
                    object@numEqn%%2, state[object@numEqn-1], 
                    object@rate1[object@numEqn-1]))
    }
    object@ode@state <- state
    # cat(sprintf("stepSize=%8f \n", object@stepSize))
    object                          # use this object to reassign in R
}) 


# constructor
Verlet <- function(.ode) {
    # constructor for RK4 ODE solver
    verlet <- new("Verlet", .ode)
    verlet <- init(verlet, verlet@stepSize)                        
    return(verlet)
}

