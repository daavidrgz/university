t = -1:0.01:1;
f = 10;
figure(1)
subplot(311)
plot(t, cos(2*pi*f*t))
subplot(312)
plot(t, t>=0)
subplot(313)
plot(t, cos(2*pi*f*t).*(t>=0))