function [] = probPlot(varargin)

% Usage:
% probPlot(nc, rows, cols, latStart, latEnd, lngStart, lngEnd)
% @Param nc        [Number of channels]
% @Param rows      [Number of rows for analysis area]
% @Param cols      [Number of cols for analysis area]
% @Param latStart  [top bound]
% @Param latEnd    [bot bound]
% @Param lngStart  [left bound]
% @Param lngEnd    [right bound]

filename = 'Simulation_result';
channelID = varargin{1};
rows = str2double(varargin{2});
cols = str2double(varargin{3});
latStart = str2double(varargin{4});
latEnd = str2double(varargin{5});
lngStart = str2double(varargin{6});
lngEnd = str2double(varargin{7});

% import data from a text file
% importName = ['/Users/ningli/Desktop/motoData/demoTable_', channelID, '.txt'];
importName = ['C:\Users\Administrator\Desktop\motoData\demoTable_', channelID, '.txt'];
import = importdata(importName);
A = import.data;

% importName = ['/Users/ningli/Desktop/motoData/demoTable_', channelID, '_pu.txt'];
importName = ['C:\Users\Administrator\Desktop\motoData\demoTable_', channelID, '_pu.txt'];
import = importdata(importName);
D = import.data;

% markers
sz = size(D);
tr = sz(1, 1);
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

% enlarge probability difference
enlargeMat = zeros(rows, cols);
maxVal = max(max(M));  % find max val
minVal = 1;            % find min val
for i = 1: rows
    for j = 1 : cols
        if M(i, j) ~= 0 && M(i, j) < minVal
            minVal = M(i, j);
        end
    end
end
level = 10;            % segment level
interval = (maxVal - minVal) / level;
for i = 1: rows
    for j = 1 : cols
        if M(i, j) == 0
            enlargeMat(i, j) = NaN;     % set to nan if 0
        elseif interval ~= 0            % do not enlarge if interval is 0
            enlargeMat(i, j) = fix((M(i, j) - minVal) / interval);
        else
            enlargeMat(i, j) = M(i, j);
        end
    end
end

ylabels = cell(1, 11);
for count = 1 : (level + 1)
    ylabels{count} = num2str(minVal + interval * (count - 1));
end

% axes
x = (lngStart):((lngEnd - lngStart)/(cols - 1)):(lngEnd);
y = (latStart):((latEnd - latStart)/(rows - 1)):(latEnd);

% plot probability
figure();
hold on;
h = pcolor(x, y, enlargeMat);
set(h,'Edgecolor', 'interp');

% color bar
if interval == 0
    colorbar;
else colorbar('YTick', [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10], ...
              'YTickLabels', ylabels);
end

% title and label
title(['Probability distribution for channel ', channelID]);
xlabel('longitude');
ylabel('latitude');

% plot location of pu
plot(markers(:, 2), markers(:, 1), 'y*', 'MarkerSize', 20);

% draw google map
plot_google_map('maptype','hybrid','APIKey','AIzaSyB6ss_yCVoGjERLDXwydWcyu21SS-dToBA');
hold off;

% Output the contours into pdf and png file
fileextension = '.png';
% name = ['/Users/ningli/Desktop/motoPlot/', filename, '_channel_', channelID, fileextension];
name = ['C:\Users\Administrator\Desktop\motoPlot\', filename, '_channel_', channelID, fileextension];
print('-dpng',name);

% close figure
close all;

end