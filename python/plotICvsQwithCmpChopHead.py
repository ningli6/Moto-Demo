import matplotlib.pyplot as plt
import sys

def chophead(l):
	res = []
	for line in l:
		line = line[1:]
		res.append(line)
	return res

path1 = "C:/Users/Administrator/Desktop/motoData/" + sys.argv[1]
path2 = "C:/Users/Administrator/Desktop/motoData/" + sys.argv[2]
output = 'C:/Users/Administrator/Desktop/motoPlot/'
label = [
			['channel 0', 'channel 1', 'channe 2'],
			['No countermasure, channel 0', 'No countermasure, channel 1', 'No countermasure, channe 2']
		]
marker = ['*', 'o', 'x']

f = open ( path1 , 'r')
l = chophead([ map(float, line.split()) for line in f ])
channels = len(l) - 1
for c in range(0,channels):
	plt.plot(l[0], l[c + 1], linestyle = '--', marker = marker[c], label = label[1][c])

f2 = open ( path2 , 'r')
l2 = chophead([ map(float, line.split()) for line in f2 ])
channels = len(l2) - 1
for c2 in range(0,channels):
	plt.plot(l2[0], l2[c2 + 1], marker = marker[c2], label = label[0][c2])

cmTitle = '';
if sys.argv[2] == "cmp_AdditiveNoise.txt": 
	cmTitle = 'with additive noise'
elif sys.argv[2] == "cmp_Transfiguration.txt":
	cmTitle = 'with transfiguration'

plt.legend()
plt.title('Inaccuracy vs queries ' + cmTitle)
plt.xlabel('Number of queries')
plt.ylabel('IC')
plt.tight_layout()
# plt.show()
plt.savefig(output + 'ICvsQ.png')


