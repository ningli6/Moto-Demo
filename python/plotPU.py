'''
This script is used to plot location of PUs in the "map".
Map range are specified by indices as well as PUs' location.
PU's locations are represented as a circle(point).
'''
import matplotlib.pyplot as plt

# get map data and pu's locatioin from user
# mapHeight = int(raw_input('Map Height: '))
# mapLength = int(raw_input('Map Length: '))
mapHeight = 40
mapLength = 60

# number_of_pu = int(raw_input('Numbers of PU in the map: '))

# dict = {}
# for x in range(0, number_of_pu):
# 	xy = raw_input('Indices for pu ' + str(x) + ' [r, c]: ')
# 	assert len(xy.split()) == 2
# 	dict[x] = map(int, xy.split())
dict = {0: [9, 9],
		1: [9, 13],
		2: [30, 55],
		3: [30, 50],
		4: [10, 10],
		5: [11, 7]
		}
number_of_pu = len(dict)


rows = []
cols = []
for x in range(0, number_of_pu):
	rows.append(dict[x][1]) 
	cols.append(dict[x][0]) 

# after k clustering
KCrows = []
KCcols = []
kc = {0: [30, 52],
	  1: [10, 10]}
for x in range(0, 2):
	KCrows.append(kc[x][1]) 
	KCcols.append(kc[x][0]) 

plt.plot(rows, cols, 'ro')
plt.plot(KCrows, KCcols, 'bx', markersize=10)
plt.axis([0, mapLength, 0, mapHeight])
plt.title("PUs' location")
plt.xlabel('columns')
plt.ylabel('rows')
plt.gca().invert_yaxis()
plt.show()
