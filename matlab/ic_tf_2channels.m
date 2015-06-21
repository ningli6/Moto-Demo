% draw countermeasure with additive noise with 2 channels
% normalization
importName = ['/Users/ningli/Desktop/input.txt'];
table = importdata(importName);
[m,n] = size(table);
x = table(1, :);
tf_3_0 = table(2, :);
tf_3_1 = table(3, :);
tf_3 = (tf_3_1 + tf_3_0) / 2;
tf_10_0 = table(4, :);
tf_10_1 = table(5, :);
tf_10 = (tf_10_0 + tf_10_1) / 2;
% tf_5_0 = table(6, :);
% tf_5_1 = table(7, :);
% tf_5 = (tf_5_0 + tf_5_1) / 2;
% tf_6_0 = table(8, :);
% tf_6_1 = table(9, :);
% tf_6 = (tf_6_1 + tf_6_0) / 2;
% normalization
% nor = tf_3(1, 1);
% tf_3 = tf_3 / nor;
% tf_4 = tf_4 / nor;
% tf_5 = tf_5 / nor;
% tf_6 = tf_6 / nor;
figure;
hold on;
plot(x, tf_3, '-ro');
plot(x, tf_10, '-bo');
% plot(x, tf_5, '-go');
% plot(x, tf_6, '-mo');
% legend({'sides: 3',...
%     'sides: 10',...
%     'sides: 5',...
%     'sides: 6'}, 'FontSize',12);
legend({'sides: 3',...
    'sides: 10'}, 'FontSize',12);
legend('boxoff');
title('\fontsize{13}IC vs N with transfiguration');
xlabel('N: number of queries', 'FontSize',12);
ylabel('IC for each channel', 'FontSize',12);
