'''
Show relation between IC and query for each channel with smart plot
'''

import matplotlib.pyplot as plt
import sys
from pyFunctions import PyFunctions

class cmpPlots_Smart:

	def plot_smart(self, dataDir, plotDir, files):
		output = plotDir
		pyf = PyFunctions()
		for x in range(len(files)):
			path = dataDir + files[x]
			try:
				f = open(path, 'r')
			except Exception, e:
				continue
			l = [map(float, line.split()) for line in f]
			if files[x] == 'cmp_smart_NoCountermeasure.txt':
				label = "No countermeasure"
				marker = '.'
			elif files[x] == 'cmp_smart_AdditiveNoise.txt':
				label = "Additive noise"
				marker = "*"
			elif files[x] == 'cmp_smart_Transfiguration.txt':
				label = "Transfiguration"
				marker = "o"
			elif files[x] == 'cmp_smart_KAnonymity.txt':
				label = "K anonymity"
				marker = "^"
			elif files[x] == 'cmp_smart_KClustering.txt':
				label = "K clustering"
				marker = "D"
			plt.plot(l[0], pyf.normalize(pyf.average(l[1:])), label = label, marker = marker)
		plt.legend()
		plt.title('Inaccuracy vs queries with smart queries')
		plt.xlabel('Number of queries')
		plt.ylabel('IC')
		plt.tight_layout()
		plt.savefig(output + 'ICvsQ_Smart.png')

pt = cmpPlots_Smart()
pt.plot_smart(sys.argv[1], sys.argv[2], [sys.argv[x] for x in range(3, len(sys.argv))])