#' ---
#' title: Prácticas en R
#' author: David Rodríguez
#' output: word_document
#' ---
#'
#' # UTILIDADES BÁSICAS
#' Knit: Genera un documento a prtir del código fuente
#'
#' Funcion rm (remove): Elimina variables del entorno
x=5
rm(x)
#'
#' source (Contl + Shift +S): Ejecuta todas las instrucciones de un script
#'
#' # VECTORES
#' 
#' ## Definición de vectores
#' 
#' * Función c():
#' 
c(1, 2, 3, 4, 5, 6) #'(Crea un vector)

#' * Función seq():
#' 
seq(1,5) #' (Crea un vector desde 1 hasta 5 con salto 1)
1:5 #' (Sentencia equivalente)
seq(10,50, by=10) #' (Crea un vector desde 10 hasta 50 con salto 10)
seq(10,50, length=10) #' (Crea un vector desde 10 hasta 50 con exactamente 5 elementos)
#'
#' * Función rep():
#' 
rep(5, 10) #' (Repite el número 5, 10 veces; permite repetir vectores)
#'
#'
#' ## Indexado de vectores
#' 
#' Todos los vectores se indexan con [ ] y **empiezan en 1**
#' 
v = seq(10, 50, 10)
v[1]
v[10]
#' * Indexado con otros vectores
#' 
indice = c(1, 2, 3); v[indice]
#'
#' * Indexado de forma condicional:
#' 
indice = v > 25 #' Devuelve un vector con valores TRUE o FALSE
v[indice]
#'
#' # FUNCIONES
#' 
f = function(x, y)
{
  #' Cuerpo de la función
  return(x + y)
}
f(5,10)
#'
#' # MEDIA ARITMÉTICA
#' 
v= seq(10,20)
#'
#' 1. Suma de  los elementos de v con la función sum();
sum(v)

#' 2. Número de elementos de v con lenght();
length(v)

#' 3. Suma de los elementos / Número de elementos
sum(v) / length (v)
#' 
#' Con R podemos utilizar también la función ya implementada mean();
mean(v)
#' 
#' # VARIANZA
#' 
v= seq(1,10)
#
#' 1. Media del vector
m = mean(v);
#'
#' 2. Diferencia de cada elemento con la media
v - m
#'
#' 3. El cuadrado de la diferencia
(v-m)^2
#'
#' 4. Sumatorio
sum((v-m)^2)
#'
#' 5. Sumatorio / NumElementos
varianza = sum((v-m)^2)/length(v)
varianza
#'
#' En R, la función var() no devuelve la varianza; calcula la **cuasi-varianza**
var(v)
#'
