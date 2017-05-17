# ODESolver.R
#


setClass("ODESolver")


setMethod("init", "ODESolver", function(object, stepSize, ...) {
})

setMethod("step", "ODESolver", function(object, ...) {
})

setMethod("setStepSize", "ODESolver", function(object, stepSize, ...) {
})

setMethod("getStepSize", "ODESolver", function(object, ...) {
})
