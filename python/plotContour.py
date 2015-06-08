'''
Try to plot contourf from output data then overlay on google map
'''
import numpy as np
import matplotlib.pyplot as plt

# read in data from text file
rows = 52
cols = 94
Nlat = 37.900865092570065;
Slat = 35.30086509257006;
Wlng = -81.4910888671875;
Elng = -76.7910888671875;

f = open ( '/Users/ningli/Desktop/Project/output/demoTable_0.txt' , 'r');
input = [ map(float, line.split()) for line in f ]

# print 'input rows: ' + str(len(input))
# print 'input cols: ' + str(len(input[0]))

# reconstruct matrix format of input data
data = [[input[i * cols + j][2] for j in range(0, cols)] for i in range(0, rows)]
for i in range(0, rows):
	for j in range(0, cols):
		if (data[i][j] == 0):
			data[i][j] = np.nan;


# print len(data)
# print len(data[0])

cs = plt.contourf(data)
plt.colorbar(cs)

plt.gca().invert_yaxis()
plt.title('Transfiguration')
plt.xlabel('columns')
plt.ylabel('rows')
plt.show()