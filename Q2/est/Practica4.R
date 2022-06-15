#' Introducción
#' 
dbinom(0, 10, 0.7)

sum(dbinom(0:3, 10, 0.7))

pbinom(3, 10, 0.7)

1-pbinom(2, 10, 0.7)

qbinom(c(0.25, 0.5, 0.75), 10, 0.7)

dpois(0, 3)

#' Representaciones gráficas
#' 
x = c(0:100)
p = dbinom(x, 100, 0.3)
plot(x, p,type = 'h')

x = c(0:30)
p = dpois(x, 10)
plot(x, p, type='h')
#'
#'
#' Ejercicio 4.2.3
#' 
dnbinom(2, 1, 0.8)

dnbinom(2, 10, 0.8)
#'
#'
#'
#' # V.A Continuas
#' 
# X ~ U(2, 5) P(X < 3.7)

punif(3.7, 2, 5)

# X ~ Exp(lambda=3) P(X < 0.5) P(X > 1.5)

pexp(0.5, 3)
pexp(1.5, 3, lower.tail = FALSE)
# O también
1 - pexp(1.5, 3)

#X ~ Exp(lambda=3) Cuantiles 0.05 y 0.95

qexp(0.05, 3)

#X ~ N(1.5, 3.2) El valor de a t.q P(X > a) = 0.3

cuantil = qnorm(0.7, 1.5, 3.2)
pnorm(cuantil, 1.5, 3.2, lower.tail = FALSE)

#' Representaciones gráficas
#' 
#' Distribución uniforme
curve(dunif(x, -1, 2), xlim=c(-1,2), ylim=c(0,1), xlab="X", 
      ylab = "Función de densidade", main="Distribución uniforme U(-1, 2)")
#'
#'Distribución exponencial
curve(dexp(x, 2), xlim=c(0, 4), ylim=c(0, 2), xlab="Tiempo entre dos éxitos consec.", ylab="Densidad", main="Distribución exponencial Exp(0.5)")
curve(dexp(x, 0.5), col="red", add=T)
curve(dexp(x, 10), col="blue", add=T)
legend("topright", c("Exp(2)", "Exp(0.5", "Exp(10"), col=c("black", "red", "blue"), lty=1, bty="n")
#'
#' Distribución normal
curve(dnorm(x, 0, 1), xlim=c(-10, 10), ylim=c(0, 1), ylab="Densidad")
curve(dnorm(x, 0, 4), col="yellow", add=T)
curve(dnorm(x, 0, 0.5), col="orange", add=T)
legend("topright", c("N(0,1)", "N(0,4)", "N(0,0.5)"), col=c("black", "yellow", "orange"), lty=1, bty="n")
#'
#' EJERCICIO 4.3.3
#' 
curve(dnorm(x, 1+1.5, sqrt(0.05^2+0.1^2)), xlim = c(0, 5))
prob = pnorm(2.55, 1+1.5, sqrt(0.05^2+0.1^2)) - pnorm(2.4, 1+1.5, sqrt(0.05^2+0.1^2))
prob_def = 1 - prob
cuantil_0.99 = qnorm(0.99, 1+1.5, sqrt(0.05^2+0.1^2))

prob_5defect = dbinom(5, 10, prob_def)
#'
#' EJERCICIO 4.4
x=c(0:200)
y=dbinom(x, 200, 0.1)
plot(x, y, type='h')
prob_bin = pbinom(10, 200, 0.1, lower.tail = FALSE)

t=dnbinom(x, 1, 0.9)
plot(x, t, type='h', xlim=c(0, 20))

#' Transformación BINOMIAL a NORMAL
mean = 200 * 0.1
sd = sqrt(200*0.1*0.9)
curve(dnorm(x, mean, sd), col="red", add=T)

prob_norm = pnorm(9.5, mean, sd, lower.tail = FALSE)
