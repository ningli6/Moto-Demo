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
        
% count sum of M
        sum = 0;
        for i = 1: rows
            for j = 1 : cols
                sum = sum + M(i, j);
            end
        end
        disp(sum);
        
        % interpolate data
        Vq = interp2(M);
        [mR, mC] = size(Vq);
%         for i = 1: mR
%             for j = 1 : mC
%                 if Vq(i, j) == 0
%                     Vq(i, j) = NaN;
%                 end
%             end
%         end
        
        fakeMatrix = zeros(mR, mC);
        maxVal = max(max(Vq));
        minVal = 1;
        for i = 1: mR
            for j = 1 : mC
                if Vq(i, j) ~= 0 && Vq(i, j) < minVal
                    minVal = Vq(i, j);
                end
            end
        end
        level = 10;
        interval = (maxVal - minVal) / level;
        for i = 1: mR
            for j = 1 : mC
                if Vq(i, j) == 0
                    fakeMatrix(i, j) = NaN;
                else
                    fakeMatrix(i, j) = (Vq(i, j) - minVal) / interval;
                end
            end
        end


        x = (LongStart):((LongEnd - LongStart)/(mC - 1)):(LongEnd);
        y = (latStart):((latEnd - latStart)/(mR - 1)):(latEnd);

        % plot on google map
        figure();
%         contourf(x2, y2, copy);
%         contourf(x, y, Vq);
        contourf(x, y, fakeMatrix);
        title(['Probability distribution for channel ', channelID]);
        xlabel('longitude');
        ylabel('latitude');
        % plot color bar
%         caxis([0.4, 0.6]);
%         caxis auto;
%         colorbar;
        contourcmap('jet', 'Colorbar', 'on', ...
           'Location', 'vertical', ...
           'ColorAlignment', 'center',...
           'TitleString', 'Probability value');
        hold on;
        % plot location of pu
        plot(markers(:, 2), markers(:, 1), 'r*', 'MarkerSize', 20);
        % for i = 1 : tr
        % %     plot(markers(i, 1) ,markers(i, 2),'r*','MarkerSize',20);
        % end
        % drwa google map
        plot_google_map('maptype','hybrid','APIKey','AIzaSyB6ss_yCVoGjERLDXwydWcyu21SS-dToBA');
        % colormap default;
        hold off;

        % Output the contours into pdf and png file
        fileextension = '.png';
        name = ['/Users/ningli/Desktop/motoPlot/', filename, '_channel_', channelID, fileextension];
%         name = ['C:\Users\Administrator\Desktop\motoPlot\', filename, '_channel_', channelID, fileextension];
        print('-dpng',name);

        % close figure
%         close all;
    end
end