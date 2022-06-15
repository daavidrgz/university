#' ---
#' title: Probabilidadin Introducción
#' author: David Rodríguez
#' output: pdf_document
#' ---
#' # Probabilidad es juegos de azar. La Ruleta
#' 
#' * Consta de 37 números, 18 de color rojo, 18 de color negro y 1 de color verde (0)
#' * En función de la apuesta hay hay una ganancia
#' 
colores = c('R', 'N')
ruleta = c('V', rep(colores, 18))
#'
#' Probabilidad de victoria (Laplace)
#' 
favorables = sum(ruleta == 'R') # Se pueden sumar booleanos tomando TRUE=1 y FALSE=0
#'
posibles = length(ruleta)
#'
prob_color = favorables/posibles
#'
#' Hay 2 posibilidades de tirada: Victoria: 1, Derrota :-1
esperanza = (prob_color * 1) + ((1-prob_color)* -1)# valor esperado, expected value
esperanza # Representa lo que gana a la larga el casino
#'
#' Probabilidad simulada
#' 
#' * Simular una tirada de una ruleta
#' * Decidir si la apuesta ees ganadora
#' * Cuanto he ganado
#' 
tirada = function()
{
  # Simula una tirada a un color
  numeros = 0:36
  numero_ganador = sample(numeros, 1) # Sample (Muestra): Escoge de forma aleatoria entre un conjunto
  return (ruleta[numero_ganador + 1]) # + 1 ya que en R, la posición 0 del vector no existe
}
tirada() # Ejecuta 1 tirada
replicate(10, tirada()) # Ejecuta la función 10 veces y lo devuelve en forma de vector
r = replicate(10000, tirada())
table(r)
barplot(table(r), col=c('black', 'red', 'green'))
#'
apuesta= function(color, cantidad)
{
  # Simula una apuesta a un color concreto
  color_ganador = tirada() #Simula 1 tirada
  if (color== color_ganador)
  {
    return(cantidad)
  } else {
    return(-cantidad)
  }
}
#'
#'
#'
apuesta('R', 1) #Apuesta al rojo 1 u.m.
replicate(10, apuesta('R', 1))
#'
simulacion = function(apuestas, color='R', cantidad=1)
{
  jugadas = replicate(apuestas, apuesta(color, cantidad))
  table(jugadas)
  barplot(table(jugadas), col=c('red', 'green'))
  balance=sum(jugadas)
  return(balance)
}
apuestas= 250000
balance = simulacion(apuestas)
valor_simulado = balance/apuestas; valor_simulado
#' El efecto del color verde hace que haya una dieferencia mayor entre victorias y derrotas













