fs = 20000
Ls = 5
t = 0:1/fs:0.5

f1 = 330
a = [1 2 -1]

for i=1:3
  figure(i)
  x_sum = 0
  for j=1:3
    subplot(410 + j);
    x = a(i)*cos(2*pi*f1*j*t)
    plot(t, x);
    title("Frequency = 330Hz");
    xlabel("Time");
    ylabel("Amplitude");
    axis ([0, 0.05], "tic", "label");
    
    x_sum += x
  end

  subplot(414)
  plot(t, x_sum)
  title("Sum")
  xlabel("Time")
  ylabel("Amplitude")
  axis ([0, 0.05], "tic", "label");
end

