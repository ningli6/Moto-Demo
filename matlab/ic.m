x = [10, 25, 50, 75, 100, 125, 150, 175, 200, 300];
y1 = [22698, 15743, 16551, 4118, 679, 3302, 3532, 3, 884, 241];
y2 = [61798, 16650, 10996, 4370, 956, 3813, 6244, 561, 535, 232];
figure;
plot(x, y1, '-ro', x, y2, '-bx');
title('IC vs N');
xlabel('N: number of queries');
ylabel('IC for each channel');
legend('channel 0','channel 1');