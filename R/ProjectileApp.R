# generic functions
setGeneric("getRate", function(object, state, rate, ...) standardGeneric("getRate"))
setGeneric("getState", function(object, ...) standardGeneric("getState"))

setGeneric("step", function(object, ...) standardGeneric("step"))
setGeneric("setStepSize", function(object, stepSize, ...) standardGeneric("setStepSize"))
setGeneric("getStepSize", function(object, ...) standardGeneric("getStepSize"))

setGeneric("doStep", function(object, ...) standardGeneric("doStep"))
setGeneric("setState", function(object, x, vx, y, vy, ...) standardGeneric("setState"))
setGeneric("init", function(object, ...) standardGeneric("init"))   

####################################################### ODE -----------------

setClass("ODE", slots = c(
    state = "numeric",
    rate  = "numeric"
))


setMethod("getState", "ODE", function(object, ...) {
    # Gets the state variables.
    return(object@state)
})

setMethod("getRate", "ODE", function(object, state, rate, ...) {
    # Gets the rate of change using the argument's state variables.
    return(object@rate)
})




# -------------------------------------------------------------------- ODESolver

setClass("ODESolver", slots = c(
    stepSize = "numeric",
    numEqn   = "numeric",
    ode      = "ODE"
), prototype = prototype(
    stepSize = 0.1,
    numEqn = 0
)
)

setMethod("initialize", "ODESolver", function(.Object, .ode) {
    .Object <- init(.Object, 0.1)                                 
    return(.Object)
})

setMethod("step", "ODESolver", function(object, ...) {
    # object                                                         
})

setMethod("setStepSize", "ODESolver", function(object, stepSize, ...) {
    object@stepSize = stepSize
    object
})

setMethod("init", "ODESolver", function(object, stepSize, ...) {
    # in this method we get the length of the state and assign it to numEqn
    object@stepSize <- stepSize
    state <- getState(object@ode)
    
    if (is.null(state)) {
        object@numEqn <-  0
    } else {
        object@numEqn = length(state)
    }
    object
})

setMethod("getStepSize", "ODESolver", function(object, ...) {
    return(object@stepSize)
})


# constructor
ODESolver <- function(.ode) {
    odesolver <- new("ODESolver", .ode)
    odesolver@ode <- .ode
    # odesolver <- init(odesolver, 0.1)                            
    odesolver
}




# ----------------------------------------------------------------- Euler ------

setClass("Euler", 
    contains = c("ODESolver") 
    )

setMethod("initialize", "Euler", function(.Object, ode, ...) {
    .Object@ode <- ode
    .Object@ode@rate <- vector("numeric")
    return(.Object)
})


setMethod("init", "Euler", function(object, stepSize, ...) {
    object <- callNextMethod(object, stepSize)          # call superclass init()
    object@ode@rate <- vector("numeric", object@numEqn)  # make the rate vector
    object
})


setMethod("step", "Euler", function(object, ...) {
    # cat("| 4 |")
    state <- getState(object@ode) # <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    rate  <- getRate(object@ode, state, object@ode@rate)  # <<<<<<<<<<<< 
    
    for (i in 1:object@numEqn) {                         # numEqn not refreshing
        state[i] <- state[i] + object@stepSize * rate[i]
    }
    object@ode@state <- state       # return state and rate for new iter
    object@ode@rate  <- rate  
    object
}) 

setMethod("setStepSize", "Euler", function(object, stepSize, ...) {
    object@stepSize = stepSize
    object
})


setMethod("getStepSize", "Euler", function(object, ...) {
    return(object@stepSize)
})



# constructor
Euler <- function(.ode) {
    euler <- new("Euler", .ode)
    euler <- init(euler, euler@stepSize)                            # diff 5
    return(euler)
}





# ------------------------------------------------------------------ Projectile

setClass("Projectile", slots = c(
    g = "numeric",
    odeSolver = "Euler"
    ),
    prototype = prototype(
        g = 9.8
    ),
    contains = c("ODE")
    )

setMethod("initialize", "Projectile", function(.Object) {
    # .Object@odeSolver <- new("Euler", .Object)
    # .Object@odeSolver <- Euler(.Object) <- gives error
    # .Object@odeSolver <- init(.Object@odeSolver, .Object)
    # .Object@odeSolver <- init(.Object@odeSolver, 0.1)   # no effect
    # cat("| 0 |")
    .Object@odeSolver <- Euler(.Object)                               # diff 6
    return(.Object)
})

setMethod("setStepSize", "Projectile", function(object, stepSize, ...) {
    object@odeSolver <- setStepSize(object@odeSolver, stepSize)
    object
})


setMethod("step", "Projectile", function(object) {
    # cat("Projectile:step:state:rate(1)", object@state, object@rate, "\n")
    object@odeSolver <- step(object@odeSolver)
    
    object@rate  <- object@odeSolver@ode@rate                           # diff 7
    object@state <- object@odeSolver@ode@state                          # diff 8
    
    # cat("Projectile:step:state:rate(2)", object@state, object@rate, "\n")
    object
})

setMethod("setState", "Projectile", function(object, x, vx, y, vy) {
    object@state[1] <- x
    object@state[2] <- vx
    object@state[3] <- y
    object@state[4] <- vy
    object@state[5] <- 0
    
    object@odeSolver@ode@state <- object@state
    object
})

setMethod("getState", "Projectile", function(object) {                 # diff 9
    object@state
})


setMethod("getRate", "Projectile", function(object, state, rate) {    # diff 10
    rate[1] <- state[2]                                               # diff 11
    rate[2] <- 0                                                      # diff 12
    rate[3] <- state[4]
    rate[4] <- - object@g
    rate[5] <- 1
    
    object@state <- object@odeSolver@ode@state <- state
    object@rate  <- object@odeSolver@ode@rate  <- rate
    object@rate                                                       # diff 18
})





# constructor
Projectile <- function()  new("Projectile")
