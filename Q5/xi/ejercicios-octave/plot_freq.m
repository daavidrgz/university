function plot_freq (title, f, X, figure, subplot)
  figure(figure);
  subplot(subplot);
  plot(f, X)
  axis([-1000 1000 0 1.1], "tic", "label")
  xlabel("Frecuency (rad/s)")
  ylabel("Amplitude")
  title(title);
endfunction