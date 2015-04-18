'''
This script is used to plot shapes of transfiguration and compare with original shape
so that we can convince ourselves that transfiguration is correct.
'''

import matplotlib.pyplot as plt

f = open ( '/Users/ningli/Desktop/Project/output/precompute0.txt' , 'r')
l = [ map(float,line.split()) for line in f ]

f2 = open ( '/Users/ningli/Desktop/Project/output/original0.txt' , 'r')
l2 = [ map(float,line.split()) for line in f2 ]

levels = [0, 0.5, 0.75, 1]

plt.figure()

cs = plt.contour(l, levels, colors = 'r')
cs2 = plt.contour(l2, levels, colors = 'k')
plt.clabel(cs2, inline=1, fontsize=10)
plt.gca().invert_yaxis()
plt.title('Transfiguration')
plt.xlabel('columns')
plt.ylabel('rows')
plt.show()