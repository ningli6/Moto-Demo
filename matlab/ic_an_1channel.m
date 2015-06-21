% draw countermeasure with additive noise with only 1 channels
% normalize results
importName = ['/Users/ningli/Desktop/input.txt'];
table = importdata(importName);
[m,n] = size(table);
x = table(1, :);
y = table(2, :);
nor = y(1, 1);
ad_0 = y / nor;
ad_50 = table(3, :) / nor;
ad_75 = table(4, :) / nor;
ad_100 = table(5, :) / nor;
figure;
hold on;
plot(x, ad_0, '-ro');
plot(x, ad_50, '-bo');
plot(x, ad_75, '-go');
plot(x, ad_100, '-mo');
legend({'noise: 0',...
    'noise: 0.5',...
    'noise: 0.75',...
    'noise: 1'}, 'FontSize',12);
legend('boxoff');
title('\fontsize{13}IC vs N with additive noise');
xlabel('N: number of queries', 'FontSize',12);
ylabel('IC for each channel', 'FontSize',12);