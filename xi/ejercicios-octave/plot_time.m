function plot_time (title, t, x)
  plot(t, x);
  axis([0 0.05 -0.1 0.1], "tic", "label")
  xlabel("Time (s)")
  ylabel("Amplitude")  
  title(title);
endfunction