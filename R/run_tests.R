library(testthat)

source("./R/ode_generics.R")
source("./R/ODE.R")
source("./R/ODESolver.R")
source("./R/AbstractODESolver.R")
source("./R/Euler.R")
source("./R/RK4.R")
source("./R/RK45.R")
source("./R/EulerRichardson.R")

source("./R/FallingParticleODE.R")
source("./R/Pendulum.R")
source("./R/Planet.R")
source("./R/Projectile.R")
source("./R/Reaction.R")
source("./R/Kepler.R")
source("./R/ODETest.R")



# source("./R/Kepler.R")

# test_file("./tests/test_FallingParticleODEApp.R")
# test_file("./tests/test_Pendulum.R", reporter = "tap")
# test_file("./tests/test_Planet.R", reporter = "tap")
# test_file("./tests/test_Projectile.R", reporter = "tap")
# test_file("./tests/test_ReactionApp.R", reporter = "summary")

# test_file("./tests/test_AbstractODESolver.R", reporter = "summary")
# test_file("./tests/test_Euler.R", reporter = "summary")
# test_file("./tests/test_EulerRichardson.R", reporter = "summary")
# test_file("./tests/test_ODESolver.R", reporter = "summary")
# test_file("./tests/test_RK4.R", reporter = "summary")

# RK45 tests
test_file("./tests/test_DormandPrince45.R", reporter = "summary")
test_file("./tests/test_RK45.R", reporter = "summary")
test_file("./tests/test_KeplerApp.R", reporter = "summary")
test_file("./tests/test_AdaptiveStepApp.R", reporter = "summary")

# test_dir("./tests", reporter = "summary")


