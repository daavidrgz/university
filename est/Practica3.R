#' ---
#' title: Simulación
#' author: David
#' pdf_document
#' ---
#' 
set.seed(1)
runif(n=5) #' Genera 5 números aleatorios entre 0 y 1
runif(n=5 , min=1, max=10)

rnorm(n=5, mean=0, sd=1) #' Menos utilizada

#' # 3.3. Método congruencial
#' 
gcl = function(n, a=0, b=7^5, m=2^31-1, seed=12345){
  x = seed
  u = rep(0, n)
  for( i in 1:n){
    x = (a + b*x) %% m
    u[i] = x/m
  }
  return (u)
}
gcl(500)
#' 3.3.2
#' 
u = gcl(500, 1, 5, 512)
hist(u, freq=FALSE) #' Función densidad
abline(h=1) #' Densidad teórica
#'
#' 3.3.4
#' Esperanza = (b-a)/2 = 0.5
mean(u)
#'
#' P(0,4:0,8) = b - a = 0,4
mean((u > 0.4) & ( u < 0.8))
#'
#' 3.3.6
plot(u[-500], u[-1])
a = a[-1] # Elimina el primer elemento
#' 
#' 3.3.7
#' 
u = gcl(n=500)
plot(u[-500], u[-1])
#' 
#' 
#' 
#' 
#EJERCICIO 3.6
#' 
moneda = c(cara=0, cruz=1)
n = 10000
x = sample(moneda, n, replace=TRUE, prob = c(0.5, 0.5))
mean(x)


y = runif(n, 0, 1)
x = y<0.5
mean(x)


#EJERCICIO 3.7

n=100000
x1 = rbinom(n, size=1, prob=0.8)
x2 = rbinom(n, size=1, prob=0.9)
x3 = rbinom(n, size=1, prob=0.6)
x4 = rbinom(n, size=1, prob=0.5)
x5 = rbinom(n, size=1, prob=0.7)

z1 = x1|x2
z2 = x3|x4

mean(x5 & (z1 | z2))

#' 
# EJERCICIO 3.8
tirada = c(1:6)
dado = function(n=4){
  u = sample(tirada, n, replace=TRUE)
  return(6%in%u)
}

n=10000
mean(replicate(n, dado()))







