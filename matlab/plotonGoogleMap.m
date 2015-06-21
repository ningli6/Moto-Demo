% info for output files
filename = 'Demo';
userid = 'ec2-user';

% import data from a text file
channelID = '0';

% importName = ['/Users/ningli/Desktop/Project/output/demoTable_', channelID, '.txt'];
importName = ['C:\Users\Administrator\Desktop\motoData\demoTable_', channelID, '.txt'];
import = importdata(importName);
A = import.data;
% importName = ['/Users/ningli/Desktop/Project/output/demoTable_', channelID, '_rowcol.txt'];
importName = ['C:\Users\Administrator\Desktop\motoData\demoTable_', channelID, '_rowcol.txt'];
import = importdata(importName);
B = import.data;
% importName = ['/Users/ningli/Desktop/Project/output/demoTable_', channelID, '_bounds.txt'];
importName = ['C:\Users\Administrator\Desktop\motoData\demoTable_', channelID, '_bounds.txt'];
import = importdata(importName);
C = import.data;
% importName = ['/Users/ningli/Desktop/Project/output/demoTable_', channelID, '_pu.txt'];
importName = ['C:\Users\Administrator\Desktop\motoData\demoTable_', channelID, '_pu.txt'];
import = importdata(importName);
D = import.data;

% number of rows, cols
rows = B(1, 1);
cols = B(1, 2);

% boundary
latStart = C(1, 1);
latEnd = C(1, 2);
LongStart = C(1, 3);
LongEnd = C(1, 4);

% markers
[tr, tc] = size(D);
markers = zeros(tr, 2);
for i = 1: tr
    markers(i, 1) = D(i, 1);
    markers(i, 2) = D(i, 2);
end

% data matrix
M = zeros(rows, cols);
for i = 1: rows
    for j = 1 : cols
        M(i, j) = A(j + cols * (i - 1), 3);
    end
end

x = (LongStart):((LongEnd - LongStart)/(cols - 1)):(LongEnd);
y = (latStart):((latEnd - latStart)/(rows - 1)):(latEnd);
x2 = (LongStart):((LongEnd - LongStart)/(2 * cols - 1)):(LongEnd);
y2 = (latStart):((latEnd - latStart)/(2 * rows - 1)):(latEnd);
[X,Y] = meshgrid(x, y);
[X2,Y2] = meshgrid(x2, y2);
vq = interp2(X,Y,M,X2,Y2);
[m,n] = size(vq);
% 
% inverseM = zeros(m, n);
% for i = 1: m
%     inverseM(i, :) = vq(m - i + 1, :);
% end

% copy = inverseM;
copy = vq;
for i = 1: m
    for j = 1 : n
        if copy(i, j) == 0
            copy(i, j) = NaN;
        end
    end
end

% plot on google map
figure();
contourf(x2, y2, copy);
title('Input interpolation by griddata');
xlabel('longitude');
ylabel('latitude');
% plot color bar
caxis([0.4, 0.6]);
% caxis auto;
colorbar;
% contourcmap('jet', 'Colorbar', 'on', ...
%    'Location', 'vertical', ...
%    'ColorAlignment', 'center',...
%    'TitleString', 'Probability value');
hold on;
% plot location of pu
plot(markers(:, 2), markers(:, 1), 'r*', 'MarkerSize', 20);
% for i = 1 : tr
% %     plot(markers(i, 1) ,markers(i, 2),'r*','MarkerSize',20);
% end
% drwa google map
plot_google_map('maptype','hybrid','APIKey','AIzaSyDMSjokr-3WVHSYtZeW5xM2gI6uO8BkiMI');
% colormap default;
hold off;

% Output the contours into pdf and png file
fileextension = '.png';
% name = ['/Users/ningli/Desktop/Project/output/', userid, '_', filename, '_probability_', channelID, fileextension];
name = ['C:\Users\Administrator\Desktop\motoPlot\', userid, '_', filename, '_probability_', channelID, fileextension];
print('-dpng',name);

% close figure
close all;
