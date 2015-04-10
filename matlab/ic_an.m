% draw countermeasure with additive noise with 2 channels
importName = ['/Users/ningli/Desktop/input.txt'];
table = importdata(importName);
[m,n] = size(table);
x = table(1, :);
y1 = table(2, :);
y2 = table(3, :);
ad_50_1 = table(4, :);
ad_50_2 = table(5, :);
ad_75_1 = table(6, :);
ad_75_2 = table(7, :);
ad_100_1 = table(8, :);
ad_100_2 = table(9, :);
figure;
hold on;
plot(x, y1, '-ro', x, y2, '-rx');
plot(x, ad_50_1, '-bo', x, ad_50_2, '-bx');
plot(x, ad_75_1, '-go', x, ad_75_2, '-gx');
plot(x, ad_100_1, '-mo', x, ad_100_2, '-mx');
legend({'channel 0, noise: 0','channel 1, noise: 0',...
    'channel 0, noise: 0.5','channel 1, noise: 0.5',...
    'channel 0, noise: 0.75','channel 1, noise: 0.75',...
    'channel 0, noise: 1','channel 1, noise: 1'}, 'FontSize',12);
legend('boxoff');
title('\fontsize{13}IC vs N with additive noise');
xlabel('N: number of queries', 'FontSize',12);
ylabel('IC for each channel', 'FontSize',12);
