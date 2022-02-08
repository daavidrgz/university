[x, fs] = audioread('music.flac');
T = 8;

t = 1/fs : 1/fs : T;
Ls = length(t);
f = -fs/2 +  fs/7764992 : fs/7764992 : fs/2;

figure(1)
plot(x)

figure(2)
X = fftshift(fft(x));
plot(f, abs(X)/max(abs(X)));
axis([-10000 10000 0 1.1], "tic", "label");

pause(1)
sound(x, fs)