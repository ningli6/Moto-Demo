'''
This script is used to plot shapes of transfiguration and compare with original shape
so that we can convince ourselves that transfiguration is correct.
'''

import matplotlib.pyplot as plt

f = open ('C:/Users/Administrator/Desktop/motoData/testPolyPUResponse.txt', 'r')
l = [ map(float,line.split()) for line in f ]

f1 = open ('C:/Users/Administrator/Desktop/motoData/testPolyPUResponseCMP.txt', 'r')
l1 = [ map(float,line.split()) for line in f1 ]

levels = [0, 0.5, 0.75, 1]

plt.figure()
# plt.plot(111, 115, marker='*')
cs = plt.contour(l, levels, colors = 'r')
plt.contour(l1, levels, colors = 'g')

# plt.clabel(cs, inline=1, fontsize=10)
plt.gca().invert_yaxis()
# plt.title('Transfiguration')
plt.xlabel('columns')
plt.ylabel('rows')
plt.show()