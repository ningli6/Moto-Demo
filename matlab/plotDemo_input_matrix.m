% same setting as java program does. Hard coded for now.
latStart = 45;
latEnd = 50;
LongStart = -100;
LongEnd = -95;
cellsize = 0.01;

% import data
M = dlmread('/Users/ningli/Desktop/javaout.txt');
[m,n] = size(M);
% change entries value to gain better output map
for i = 1: m
    for j = 1 : n
        if M(i, j) > 0.5 
            M(i, j) = 0.7;
        else if M(i, j) == 0.5
            M(i, j) = NaN;
            end
        end
    end
end
x = (LongStart):((LongEnd - LongStart)/499):(LongEnd);
y = (latStart):((latEnd - latStart)/499):(latEnd);

% plot without google map
figure();
contourf(x,y,M);
title('Probability');
xlabel('longitude');
ylabel('latitude');

% plot with google map
figure();
contourf(x,y,M);
title('Probability');
xlabel('longitude');
ylabel('latitude');
hold on;
plot_google_map('maptype','hybrid','APIKey','AIzaSyDMSjokr-3WVHSYtZeW5xM2gI6uO8BkiMI');
hold off;
