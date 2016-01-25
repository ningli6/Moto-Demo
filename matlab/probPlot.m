function [] = probPlot(varargin)

% Usage:
% probPlot(dataDir, plotDir, nc, rows, cols, latStart, latEnd, lngStart, lngEnd, ad, tf, ka, kc)
% 1@Param dataDir    [Directory for data file]
% 2@Param plotDir    [Save directory for plotted figure]
% 3@Param nc         [Number of channels]
% 4@Param rows       [Number of rows for analysis area]
% 5@Param cols       [Number of cols for analysis area]
% 6@Param latStart   [top bound]
% 7@Param latEnd     [bot bound]
% 8@Param lngStart   [left bound]
% 9@Param lngEnd     [right bound]
% 10@Param noCM      [plot map of no countermeasure]
% 11@Param ad        [plot additive noise]
% 12@Param tf        [plot transfiguration]
% 13@Param ka        [plot anonymity]
% 14@Param kc        [plot clustering]

rows = str2double(varargin{4});
cols = str2double(varargin{5});
latStart = str2double(varargin{6});
latEnd = str2double(varargin{7});
lngStart = str2double(varargin{8});
lngEnd = str2double(varargin{9});
cmArray = cell(0);
for i = 10 : nargin
    cmArray{i - 9} = varargin{i};
end

for iter = 1:size(cmArray, 2)
    for nc = 1: int32(str2double(varargin{3}))
        channelID = num2str(nc - 1);
        % import data from a text file
        importName = [varargin{1}, cmArray{iter}, '_', channelID, '_pMatrix','.txt'];
        import = importdata(importName);
        A = import.data;
        
        importName = [varargin{1}, cmArray{iter}, '_', channelID, '_pu', '.txt'];
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
        fig = figure();
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
        cm = '';
        if (strcmp(cmArray{iter}, 'No_Countermeasure') == 1)
            cm = 'No countermeasure';
        elseif (strcmp(cmArray{iter}, 'Additive_Noise') == 1)
            cm = 'Additive noise';
        elseif (strcmp(cmArray{iter}, 'Transfiguration') == 1)
            cm = cmArray{iter};
        elseif (strcmp(cmArray{iter}, 'K_Anonymity') == 1)
            cm = 'K Anonymity';
        elseif (strcmp(cmArray{iter}, 'K_Clustering') == 1)
            cm = 'K Clustering';
        elseif (strcmp(cmArray{iter}, 'smart_No_Countermeasure') == 1)
            cm = 'No countermeasure with smart queries';
        elseif (strcmp(cmArray{iter}, 'smart_Additive_Noise') == 1)
            cm = 'Additive noise with smart queries';
        elseif (strcmp(cmArray{iter}, 'smart_Transfiguration') == 1)
            cm = 'Transfiguration with smart queries';
        elseif (strcmp(cmArray{iter}, 'smart_K_Anonymity') == 1)
            cm = 'K Anonymity with smart queries';
        elseif (strcmp(cmArray{iter}, 'smart_K_Clustering') == 1)
            cm = 'K Clustering with smart queries';
        end
        title(['Probability distribution with ', cm, ' on channel ', channelID]);
        xlabel('longitude');
        ylabel('latitude');
        
        locParams = {};
        countLocParams = 1;
        for i = 1: tr
            locStrSpec = '%f,%f';
            locParams{countLocParams} = 'Marker';
            locParams{countLocParams + 1} = sprintf(locStrSpec, markers(i, 1), markers(i, 2));
            countLocParams = countLocParams + 2;
        end
        
        % draw google map
        plot_google_map('maptype','roadmap','APIKey','AIzaSyB6ss_yCVoGjERLDXwydWcyu21SS-dToBA', locParams{:});
        
        % plot location of pu
%         plot(markers(:, 2), markers(:, 1), 'v', 'MarkerSize', 15, 'MarkerEdgeColor','k', 'MarkerFaceColor', 'r');
        hmarker = plot(markers(:, 2), markers(:, 1), 'r*', 'MarkerSize', 30);
        set(hmarker, 'linewidth', 1);
        
        hold off;
        
        name = [varargin{2}, cmArray{iter}, '_', channelID, '_gMaps.png'];
        saveas(fig, name);
        close(fig);
    end
end
end
