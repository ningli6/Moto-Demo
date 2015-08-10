'''
Show relation between IC and query for each channel
'''

import matplotlib.pyplot as plt
import sys
from pyFunctions import PyFunctions

class cmpPlots:

	def plot(self, files):
		output = 'C:/Users/Administrator/Desktop/motoPlot/'
		pyf = PyFunctions()
		for x in range(len(files)):
			path = "C:/Users/Administrator/Desktop/motoData/" + files[x]
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
			plt.plot(l[0], pyf.normalize(pyf.average(l[1:])), label = label, marker = marker)
		plt.legend()
		plt.title('Inaccuracy vs queries')
		plt.xlabel('Number of queries')
		plt.ylabel('IC')
		plt.tight_layout()
		plt.savefig(output + 'ICvsQ.png')
		# plt.show()

pt = cmpPlots()
pt.plot([sys.argv[x] for x in range(1, len(sys.argv))])