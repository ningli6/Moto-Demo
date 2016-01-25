'''
Show relation between IC and query for each channel
'''

import matplotlib.pyplot as plt
import sys
from pyFunctions import PyFunctions

class cmpPlots:

	def plot_random(self, dataDir, plotDir, files):
		output = plotDir
		pyf = PyFunctions()
		for x in range(len(files)):
			path = dataDir + files[x]
			try:
				f = open(path, 'r')
			except Exception, e:
				continue
			l = [map(float, line.split()) for line in f]
			if files[x] == 'cmp_NoCountermeasure.txt':
				label = "No countermeasure"
				marker = '.'
			elif files[x] == 'cmp_AdditiveNoise.txt':
				label = "Additive noise"
				marker = "*"
			elif files[x] == 'cmp_Transfiguration.txt':
				label = "Transfiguration"
				marker = "o"
			elif files[x] == 'cmp_KAnonymity.txt':
				label = "K anonymity"
				marker = "^"
			elif files[x] == 'cmp_KClustering.txt':
				label = "K clustering"
				marker = "D"
			plt.plot(l[0], pyf.normalize(pyf.average(l[1:])), label = label, marker = marker)
		plt.legend()
		plt.title('Inaccuracy vs queries')
		plt.xlabel('Number of queries')
		plt.ylabel('IC')
		plt.tight_layout()
		plt.savefig(output + 'ICvsQ.png')

pt = cmpPlots()
pt.plot_random(sys.argv[1], sys.argv[2], [sys.argv[x] for x in range(3, len(sys.argv))])