function depconoutcontour(userid, filename, lat, long)
    close all;
    % Load data from the file "conc_dep_data.txt"
% 	A = importdata(['.\', userid, '\conc_dep_data.txt']);
    A = importdata('/Users/ningli/Documents/Moto/data.txt');
	A = A.data;
	min1 = min(A);
	max1 = max(A);
    lat = str2double(lat);
    long = str2double(long);
    
    h1 = figure();
% 	set(h1, 'Visible', 'off');
	% Generate the mesh matrix by separating the location range into 200x200 cells
    x = (min1(4)):((max1(4)-min1(4))/200):(max1(4));
	y = (min1(3)):((max1(3)-min1(3))/200):(max1(3));
	[X,Y] = meshgrid(x,y);
    
    % Interpolate the existing deposition data to get the deposition value for all the mesh point 
    for i = 1:length(A)
        if A(i,5)<1.e-30
            I(i) = NaN; % Change all the zero value into "NaN"
        else
            I(i) = log10(A(i,5));
        end
    end
	Z = griddata(A(:,4),A(:,3),I,X,Y,'natural');
    
    %  Generate the filled deposition contour
    i1 = min(I); i2 = max(I);
    z1 = min(min(Z)); z2 = max(max(Z));
    color_range = z1:((z2-z1)/11):z2; 
    Contours = 10.^color_range;
    contourf(X,Y,Z,color_range);
    h = contourcmap('jet',...
   'Location', 'vertical', ...
   'TitleString', 'deposition');
    g = colorbar('YTick',color_range,'YTickLabel',Contours);
    title(g,'deposition(grains/m2)');
    xlabel('longitude');ylabel('latitude');
    
    % Draw the source location on the map
    % Sometimes, the source location input by the user isn't included in
    % the output map range, so update the source location by finding the nearest location in the output data to the source location.
    hold on;
    marker_lat = lat;
    marker_long = long;
    if lat < min1(3)
        marker_lat = min1(3);
    end
    if lat > max1(3)
        marker_lat = max1(3);
    end
    if long < min1(4)
        marker_long = min1(4);
    end
    if long > max1(4)
        marker_long = max1(4);
    end
    plot(marker_long ,marker_lat,'k.','MarkerSize',20); 
    
    % plot the google map on the background of the current figure 
    plot_google_map('maptype','hybrid','APIKey','AIzaSyDMSjokr-3WVHSYtZeW5xM2gI6uO8BkiMI');

    % Output the contours into pdf and png file
%     fileextension = '.pdf';
%     name = ['.\', userid, '\', filename, 'deposition',fileextension];
%     name = ['/Users/ningli/Desktop/', userid, '_', filename, '_deposition',fileextension];
%     print('-dpdf',name);
%     fileextension = '.png';
%     name=['.\', userid, '\', filename, 'deposition',fileextension];
%     name=['/Users/ningli/Desktop/', userid, '_', filename, '_deposition',fileextension];
%     print('-dpng',name);
        
	delete(h);
    
%     close all;
end