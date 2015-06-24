'''
Show relation between IC and query for each channel
'''

import matplotlib.pyplot as plt
import sys

print 'Plotting IC vs Q...'

# print 'args len: ' , len(sys.argv)
# for i in range(0, len(sys.argv)):
# 	print str(sys.argv[i])

path = "C:/Users/Administrator/Desktop/motoData/" + sys.argv[1]
output = 'C:/Users/Administrator/Desktop/motoPlot/'
label = ['channel 0', 'channel 1', 'channe 2']
marker = ['*', 'o', 'x']

try:
	f = open ( path , 'r')
	l = [ map(float, line.split()) for line in f ]
	channels = len(l) - 1
	for c in range(0,channels):
		plt.plot(l[0], l[c + 1], marker = marker[c], label = label[c])
	plt.legend()
	plt.title('Inaccuracy vs queries')
	plt.xlabel('Number of queries')
	plt.ylabel('IC')
	plt.tight_layout()
	plt.savefig(output + 'ICvsQ.png')
	# plt.show()
except Exception, e:
	raise e


