'''
Show relation between IC and query for each channel
'''

import matplotlib.pyplot as plt
import sys
from pyFunctions import PyFunctions

class cmpPlots:

	def plot(self, dataDir, plotDir, files):
		output = plotDir
		pyf = PyFunctions()
		for x in range(len(files)):
			path = dataDir + files[x]
			try:
				f = open(path, 'r')
			except Exception, e:
				continue
			l = [map(float, line.split()) for line in f]
			if files[x] == 'averageIC_NoCountermeasure.txt':
				label = "No countermeasure"
				marker = '.'
			elif files[x] == 'cmp_AdditiveNoise.txt':
				label = "Additive noise"
				marker = "*"
			elif files[x] == 'cmp_Transfiguration.txt':
				label = "Transfiguration"
				marker = "o"
			elif files[x] == 'cmp_kAnonymity.txt':
				label = "K anonymity"
				marker = "^"
			elif files[x] == 'cmp_kClustering.txt':
				label = "K clustering"
				marker = "D"
			elif files[x] == 'Smart.txt':
				label = "Smart algorithm"
				marker ="x"
			plt.plot(l[0], pyf.normalize(pyf.average(l[1:])), label = label, marker = marker)
		plt.legend()
		plt.title('Inaccuracy vs queries')
		plt.xlabel('Number of queries')
		plt.ylabel('IC')
		plt.tight_layout()
		# plt.savefig(output + 'ICvsQ.png')
		plt.show()

pt = cmpPlots()
dataDir = "C:\\Users\\Administrator\\Desktop\\motoData\\1443203736562\\"
plotDir = "C:\\Users\\Administrator\\Desktop\\motoPlot\\"
files = ["Smart.txt", "averageIC_NoCountermeasure.txt"]
# pt.plot(sys.argv[1], sys.argv[2], [sys.argv[x] for x in range(3, len(sys.argv))])
pt.plot(dataDir, plotDir, files)