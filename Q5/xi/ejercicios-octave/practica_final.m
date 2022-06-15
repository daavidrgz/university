clear all;
close all;

fs = 48000;
T = 1;

A = [0.5 0.5 1 1];
f0 = [150 250 400 550];

t = 1/fs : 1/fs : T;
L = length(t);
f = -fs/2+fs/L : fs/L : fs/2;

sumx = 0
sumX = 0

for i=1:4
  freq = num2str(f0(i));
  
  figure(1)
  x = A(i) * cos(2*pi*f0(i).*t);
  subplot(510+i)
  plot(t, x)
  axis([0 0.05 -1 1], "tic", "label")
  xlabel("Time (s)")
  ylabel("Amplitude")  
  title(cstrcat("Frequency = ", freq, " Hz"));
 
  sound(x, fs)
  
  figure(2)
  X = fftshift(fft(x));
  subplot(510+i)
  plot(f, abs(X)/max(abs(X)))
  axis([-1000 1000 0 1.1], "tic", "label")
  xlabel("Frecuency (rad/s)")
  ylabel("Amplitude")
  title(cstrcat("Frequency = ", freq, " Hz"));
  
  sumx += x;
  sumX += X;
end

figure(1)
subplot(515)
plot(t, sumx)
axis([0 0.05 -0.5 0.5], "tic", "label")
xlabel("Time (s)")
ylabel("Amplitude")  
title("Signals sum");

figure(2)
subplot(515)
plot(f, abs(sumX)/max(abs(sumX)))
axis([-1000 1000 0 1.1], "tic", "label")
xlabel("Frecuency (rad/s)")
ylabel("Amplitude")  
title("Fourier transformed signals sum");

# -_-_-_-_-_-_-_-_-_-_-_-_-_ #

# Filtros

f_low = 420;
Hlow = zeros(1, L/2);
Hlow(1:f_low*T) = ones(1, f_low*T);
Hlow = [fliplr(Hlow) Hlow];

f_high = 350;
Hhigh = ones(1, L/2);
Hhigh(1:f_high*T) = zeros(1, f_high*T);
Hhigh = [fliplr(Hhigh) Hhigh];

Hband = Hlow .* Hhigh;

HEband = 1 - Hband;

H = Hband;

# Señal filtrada

Y = sumX .* H;
y = ifft(fftshift(Y));
y = real(y);

# Nueva figura

figure(3)
subplot(411)
plot(t, sumx)
axis([0 0.05 -1.5 1.5], "tic", "label")
xlabel("Time (s)")
ylabel("Amplitude")  
title("Signals sum in time");

subplot(412)
plot(f, abs(sumX)/max(abs(sumX)))

hold on

plot(f, abs(H),'g');
axis([-1000 1000 0 1.1], "tic", "label")
xlabel("Frecuency (rad/s)")
ylabel("Amplitude")  
title("Signals sum in frequency");

subplot(413)
plot(f, abs(Y)/max(abs(Y)))
axis([-1000 1000 0 1.1], "tic", "label")
xlabel("Frecuency (rad/s)")
ylabel("Amplitude")  
title("Filtered signal in frequency");

subplot(414)
plot(t, y)
axis([0 0.05 -1 1], "tic", "label")
xlabel("Time (s)")
ylabel("Amplitude")  
title("Filtered signal in time");

clear all;

















