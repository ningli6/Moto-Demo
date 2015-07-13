function [] = runPlot(nc)
    for channel = 1 : nc
        % info for output files
        filename = 'Simulation_result';

        % import data from a text file
        channelID = num2str(channel - 1);

        importName = ['/Users/ningli/Desktop/motoData/demoTable_', channelID, '.txt'];
        %         importName = ['C:\Users\Administrator\Desktop\motoData\demoTable_', channelID, '.txt'];
        import = importdata(importName);
        A = import.data;
        importName = ['/Users/ningli/Desktop/motoData/demoTable_', channelID, '_rowcol.txt'];
        %         importName = ['C:\Users\Administrator\Desktop\motoData\demoTable_', channelID, '_rowcol.txt'];
        import = importdata(importName);
        B = import.data;
        importName = ['/Users/ningli/Desktop/motoData/demoTable_', channelID, '_bounds.txt'];
        %         importName = ['C:\Users\Administrator\Desktop\motoData\demoTable_', channelID, '_bounds.txt'];
        import = importdata(importName);
        C = import.data;
        importName = ['/Users/ningli/Desktop/motoData/demoTable_', channelID, '_pu.txt'];
        %         importName = ['C:\Users\Administrator\Desktop\motoData\demoTable_', channelID, '_pu.txt'];
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

        % axis
        x = (LongStart):((LongEnd - LongStart)/(cols - 1)):(LongEnd);
        y = (latStart):((latEnd - latStart)/(rows - 1)):(latEnd);

        % plot probability
        figure();
        hold on;
        h = pcolor(x, y, enlargeMat);
        set(h,'Edgecolor', 'interp');
        
        % color bar
        contourcmap('jet', 'Colorbar', 'on', ...
           'Location', 'vertical', ...
           'ColorAlignment', 'center',...
           'TitleString', 'Probability value');
       
        % title and label
        title(['Probability distribution for channel ', channelID]);
        xlabel('longitude');
        ylabel('latitude');
       
        % plot location of pu
        plot(markers(:, 2), markers(:, 1), 'y*', 'MarkerSize', 20);

        % draw google map
        plot_google_map('maptype','hybrid', 'alpha', 1, 'APIKey','AIzaSyB6ss_yCVoGjERLDXwydWcyu21SS-dToBA');
        hold off;

        % Output the contours into pdf and png file
        fileextension = '.png';
        name = ['/Users/ningli/Desktop/motoPlot/', filename, '_channel_', channelID, fileextension];
%         name = ['C:\Users\Administrator\Desktop\motoPlot\', filename, '_channel_', channelID, fileextension];
        print('-dpng',name);
        
        % close figure
        close all;
    end
end