fs = 48000;
A = [1 1 1 1];
f0 = [261.6256 329 391.9954 493];
T = 2;

t = 1/fs : 1/fs : T;
Ls = length(t);
f = -fs/2 +  fs/Ls : fs/Ls : fs/2;

sum = 0
SUM = 0

for i=1:4
  figure(1)
  x = atan(cot(pi*f0(i).*t)) * -2*A(i)/pi;
  y = A(i) * cos(2*pi*f0(i).*t);
  x = x*0 + y;
  subplot(510 + i)
  plot(t, x)
  axis([0 0.05], "tic", "label")
  
  sound(x, fs)
  
  figure(2)
  X = fftshift(fft(x));
  subplot(510 + i)
  plot(f, abs(X)/max(abs(X)))
  axis([-100000 100000 0 1.1], "tic", "label")
  
  sum += x;
  SUM += X;
end

sound(sum/max(sum), fs)
figure(1)
subplot(515)
plot(t, sum)
axis("tic", "label")

figure(2)
subplot(414)
plot(f, abs(SUM)/max(abs(SUM)))
axis([-1000 1000 0 1.1], "tic", "label")

