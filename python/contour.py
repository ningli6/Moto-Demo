'''
This script is used to plot shapes of transfiguration and compare with original shape
so that we can convince ourselves that transfiguration is correct.
'''

import matplotlib.pyplot as plt

f = open ( 'C:/Users/Administrator/Desktop/motoData/tf_0.txt' , 'r')
l = [ map(float,line.split()) for line in f ]

levels = [0, 0.5, 0.75, 1]

plt.figure()
plt.plot(111, 115, marker='*')
cs = plt.contour(l, levels, colors = 'r')

plt.clabel(cs, inline=1, fontsize=10)
plt.gca().invert_yaxis()
plt.title('Transfiguration')
plt.xlabel('columns')
plt.ylabel('rows')
plt.show()