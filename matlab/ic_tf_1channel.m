% draw countermeasure with additive noise with only 1 channels
% normalize results
importName = ['/Users/ningli/Desktop/input.txt'];
table = importdata(importName);
[m,n] = size(table);
x = table(1, :);
y = table(2, :);
nor = y(1, 1);
tf_3 = y / nor;
tf_4 = table(3, :) / nor;
tf_5 = table(4, :) / nor;
tf_6 = table(5, :) / nor;
figure;
hold on;
plot(x, tf_3, '-ro');
plot(x, tf_4, '-bo');
plot(x, tf_5, '-go');
plot(x, tf_6, '-mo');
legend({'sides: 3',...
    'sides: 4',...
    'sides: 5',...
    'sides: 6'}, 'FontSize',12);
legend('boxoff');
title('\fontsize{13}IC vs N with transfiguration');
xlabel('N: number of queries', 'FontSize',12);
ylabel('IC for each channel', 'FontSize',12);