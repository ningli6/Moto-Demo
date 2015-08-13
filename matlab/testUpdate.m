A = dlmread('0_8.txt');
rows = size(A, 1);
cols = size(A, 2);
for i = 1: rows
    for j = 1 : cols
        if A(i, j) > 0.5
            A(i, j) = 1;
        end
    end
end
figure();
contourf(A);
colorbar;
B = dlmread('8_14.txt');
for i = 1: rows
    for j = 1 : cols
        if B(i, j) > 0.5
            B(i, j) = 1;
        end
    end
end
figure();
contourf(B);
colorbar;
C = dlmread('14_25.txt');
for i = 1: rows
    for j = 1 : cols
        if C(i, j) > 0.5
            C(i, j) = 1;
        end
    end
end
figure();
contourf(C);
colorbar;
D = dlmread('25_25.txt');
for i = 1: rows
    for j = 1 : cols
        if D(i, j) > 0.5
            D(i, j) = 1;
        end
    end
end
figure();
contourf(D);
colorbar;