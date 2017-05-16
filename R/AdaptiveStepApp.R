source("./R/ode_generics.R")
source("./R/RK45.R")


setClass("Impulse", contains = c("ODE"),
         slots = c(
             epsilon = "numeric"
         ))

setMethod("initialize", signature = c("Impulse"), function(.Object) {
    .Object@epsilon <- 0.01
    .Object@state <- c(-3.0, 1.0, 0.0)    # x, v, t
    return(.Object)
})

setMethod("getState", signature = c("Impulse"), function(object, ...) {
    return(object@state)
})

setMethod("getRate", signature = c("Impulse"), function(object, state, rate, ...) {
    rate[1] <- state[2]
    rate[2] <- object@epsilon / ( object@epsilon * object@epsilon +
        state[1] * state[1] )
    rate[3] <- 1                             # dt/dt
    object@rate <- rate
    return(object)
})


# main 
ode        <- new("Impulse")
ode_solver <- RK45(ode)
ode_solver <- init(ode_solver, 0.1)
ode_solver <- setTolerance(ode_solver, 1.0e-4)

while (getState(ode)[1] < 12) {
    ode_solver <- step(ode_solver)
    ode <- ode_solver@ode
    cat(sprintf("state[1] =%12f, state[3] =%12f, state[2] =%12f \n", getState(ode)[1], getState(ode)[3], getState(ode)[2]))
}