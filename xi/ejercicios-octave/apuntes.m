clear; close all;

## REPRESENTACIÓN ##

z1 = ones(1,5)
z2 = [1,2,3,4,5]

figure(1)
subplot(311)
plot(z1)
title('z1 var')
xlabel('Position')
ylabel('Amplitude')

zprod = z1.*z2 # Producto elemento a elemento
plot(zprod)

# Para representar una función continua
figure(2)
plot(z2)

# Para represetnar una función discreta
figure(3)
stem(z2)

## NÚMEROS ALEATORIOS ##

# Vector de 20 números aleatorios entre 0 y 1
r = rand(1,20) 
z = (r > 0.5) - (r < 0.5) 


## BUCLES ##

# Bucle for
for i=1:20
  a(i) = i^2;
end;

# Lo mismo pero sin bucles (+ recomendado)
i = 1:20
a = i.^2





