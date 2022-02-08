#' ---
#' title: Probabilidadin Introducción
#' author: David Rodríguez
#' output: pdf_document
#' ---
#' 
#' Filtros de SPAM
#' ## 1.-
datos = read.table("practica2.txt", header = TRUE)

#' getwd() --> devuelve el directorio de trabajo
#'
#' setwd() --> Establece el directorio de trabajo
#' 
#' R Studio --> Files -> More -> Setr as working directory
#' 
n = nrow(datos) # Devuelve el número de filas
m = ncol(datos) # Devuelve el número de columnas
#'
#'
#' ## 2.- Probabilidades a priori
#'
s = datos[,4]
count_spam = sum(s); count_spam
count_nospam = length(s) - count_spam; count_nospam
prob_spam = sum(s)/length(s); prob_spam
prob_nospam = 1 - prob_spam; prob_nospam
#'
#' ## 3.- Probabilidades condicionadas
#' 
#' P(A=1 | S=0), P(A=0 | S=0) ...
#' 
word_count = rowsum(datos[,1:3], group=datos[,4]); word_count
prob_cond_char_spam = word_count[2, ] / count_spam; prob_cond_char_spam # Probabilidad condicionada de que aparezca la característica sabiendo que ha sido catalogado como SPAM
prob_cond_nochar_spam = 1 - prob_cond_char_spam; prob_cond_nochar_spam
prob_cond_char_nospam = word_count[1, ] / count_nospam; prob_cond_char_nospam
prob_cond_nochar_nospam = 1 - prob_cond_char_nospam; prob_cond_nochar_nospam
#'
#' ## 4.- Nuevo Mensaje
#' 
#' 001 -> A = 0, B = 0, C = 1
#'
#'P(001 | S=0) = P(A=0 | S=0) \* P(B=0 | S=0) \* P(C=1| S=0) = 0.1875
#'
x= c(0, 0, 1) # A=0, B=0, C=1
p001_nospam=prod(prob_cond_nochar_nospam[x==0], prob_cond_char_nospam[x==1])# Probabilidad de recibir 001 sabiendo que S=0
p001_spam = 1-p001_nospam
#' 0.75 \* 1 \* 0.25   =   0.1875
#'
#' La sentencia anterior, ejecuta la poeración AND (&) entre el propio vector x; nos quedamos así cono un vector de booleanos y posteriormente lo negamos
#' 
p001_nospam
#'
#'
#'
#' ## 5.- Probabilidad Total
#' P(X) = P(X|Y)*P(Y) + P(X|Z)\*P(Z) ...
#' p(001) = P(001 | S=0) * P(S=0) + P(001 | S=1) \* P(S=1)
#'
prob_total = p001_nospam * prob_nospam + p001_spam * prob_spam; prob_total
#'
#'
#' ## 6.- Bayes
#'
#' P(S=1 | 001) = P(001 | S=1)*P(S=1) / P(001)
#'
prob_bayes = p001_spam * prob_spam / prob_total; prob_bayes
#'
#'
#'

