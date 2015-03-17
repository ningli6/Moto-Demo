% same setting as java program does. Hard coded for now.
latStart = 45;
latEnd = 50;
LongStart = -100;
LongEnd = -95;
cellsize = 0.01;
% location of PU
PU_Lat = 47.5;
PU_Lon = -97.5;

% import data
A = importdata('/Users/ningli/Desktop/demodata.txt');
A = A.data;
x = (LongStart):((LongEnd - LongStart)/499):(LongEnd);
y = (latStart):((latEnd - latStart)/499):(latEnd);
[X,Y] = meshgrid(x,y);

% change entries value to gain better output map
I = zeros(length(A), 1);
for i = 1:length(A)
    if A(i,3) == 0.5
        I(i) = NaN;
    else if A(i, 3) > 0.5
        I(i) = 0.7;
        end
    end
end
Z = griddata(A(:,2),A(:,1),I,X,Y,'natural');

% plot on google map
figure();
contourf(X,Y,Z);
title('Probability');
xlabel('longitude');
ylabel('latitude');
hold on;
% draw the PU location on the map
plot(PU_Lon ,PU_Lat,'w.','MarkerSize',20);
% drwa google map
plot_google_map('maptype','hybrid','APIKey','AIzaSyDMSjokr-3WVHSYtZeW5xM2gI6uO8BkiMI');
hold off;