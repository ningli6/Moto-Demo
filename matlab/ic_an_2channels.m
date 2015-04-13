% draw countermeasure with additive noise with 2 channels
% normalization
importName = ['/Users/ningli/Desktop/input.txt'];
table = importdata(importName);
[m,n] = size(table);
x = table(1, :);
ad_0_0 = table(2, :);
ad_0_1 = table(3, :);
ad_0 = (ad_0_1 + ad_0_0) / 2;
ad_50_0 = table(4, :);
ad_50_1 = table(5, :);
ad_50 = (ad_50_0 + ad_50_1) / 2;
ad_75_0 = table(6, :);
ad_75_1 = table(7, :);
ad_75 = (ad_75_0 + ad_75_1) / 2;
ad_100_0 = table(8, :);
ad_100_1 = table(9, :);
ad_100 = (ad_100_1 + ad_100_0) / 2;
nor = ad_0(1, 1);
ad_0 = ad_0 / nor;
ad_50 = ad_50 / nor;
ad_75 = ad_75 / nor;
ad_100 = ad_100 / nor;
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
