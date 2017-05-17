source("./R/DormandPrince45.R")


setClass("RK45", 
    contains = c("DormandPrince45") 
)

setMethod("initialize", "RK45", function(.Object, ode, ...) {
    # initialized the ODE solver
    .Object@ode <- ode                          # set the ode to ODESolver slot
    return(.Object)
})



# constructor ODE solver
RK45 <- function(ode) {
    rk45 <- DormandPrince45(ode)             # equivalent to Java "super(ode)"
    return(rk45)
}

