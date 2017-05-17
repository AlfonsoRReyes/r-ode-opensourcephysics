setGeneric("getRate", function(object, state, rate, ...) standardGeneric("getRate"))
setGeneric("getState", function(object, ...) standardGeneric("getState"))
setGeneric("step", function(object, ...) standardGeneric("step"))
setGeneric("getStepSize", function(object, ...) standardGeneric("getStepSize"))
setGeneric("doStep", function(object, ...) standardGeneric("doStep"))
# setGeneric("setState", function(object, x, vx, y, vy, ...) standardGeneric("setState"))
# setGeneric("setState", function(object, theta, thetaDot, ...) standardGeneric("setState"))

setGeneric("init", function(object, ...) standardGeneric("init"))


# setStepSize uses either of two step parameters: stepSize and dt
# `stepSize`` works for most of the applications
# `dt`` is used in Pendulum
setGeneric("setStepSize", function(object, ...) standardGeneric("setStepSize"))


#' New setState that should work with different methods
#'  "theta", "thetaDot":  used in PendulumApp
#'  "x", "vx", "y", "vy": used in ProjectileApp
setGeneric("setState", function(object, ...) standardGeneric("setState"))


setGeneric("getExactSolution", function(object, t, ...) standardGeneric("getExactSolution"))


setGeneric("setTolerance", function(object, tol, ...) standardGeneric("setTolerance"))
setGeneric("getTolerance", function(object, ...) standardGeneric("getTolerance"))
setGeneric("getErrorCode", function(object, tol, ...) standardGeneric("getErrorCode"))

setGeneric("enableRuntimeExceptions", function(object, enable, ...) 
    standardGeneric("enableRuntimeExceptions"))


setGeneric("getRateCounter", function(object, ...) standardGeneric("getRateCounter"))
setGeneric("getTime", function(object, ...) standardGeneric("getTime"))
setGeneric("getEnergy", function(object, ...) standardGeneric("getEnergy"))


# setState
# setGeneric("setState", function(object, x, vx, y, vy, theta, thetaDot, ...) 
#     standardGeneric("setState"),
#     signature = c("object", "x", "vx", "y", "vy", "theta", "thetaDot"))



# setGeneric("setStepSize", function(object, stepSize, dt, ...) 
#     standardGeneric("setStepSize"),
#     signature = c("object", "stepSize", "dt"))
