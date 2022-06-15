clear;
close all;

fs = 48000;
A = 1;

semi = 0.25;
corch = 0.5;
negr = 1;

T = [semi semi semi semi semi+corch semi+corch negr corch semi semi semi semi semi+corch semi+corch semi+corch semi corch semi semi semi semi negr corch corch+semi semi negr corch negr 2*negr];
lt = length(T);

D = 261
R = 293
M = 329
F = 349
S = 391
L = 440

song = [D R F R L L S 0 D R F R S S F M R D R F R F S M R D D S F];
lsong = length(song);

for i=1:lt
  pause(0.1)
  t = 1/fs : 1/fs : T(i);
  x = A * cos(2*pi*song(i).*t);
  sound(x, fs);
end

clear;