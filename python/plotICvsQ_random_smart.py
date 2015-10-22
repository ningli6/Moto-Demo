'''
Show relation between IC and query of random queries and smart queris without countermeasure for each channel
'''

import matplotlib.pyplot as plt
import sys
from pyFunctions import PyFunctions

class cmpPlots_Random_Smart:

	def plot_random_smart(self, dataDir, plotDir, files):
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
				label = "Random queries"
				marker = '.'
			elif files[x] == 'cmp_smart_NoCountermeasure.txt':
				label = "Smart qeuries"
				marker = "*"
			plt.plot(l[0], pyf.normalize(pyf.average(l[1:])), label = label, marker = marker)
		plt.legend()
		plt.title('Inaccuracy vs queries compared between random queries and smart queries');
		plt.xlabel('Number of queries')
		plt.ylabel('IC')
		plt.tight_layout()
		plt.savefig(output + 'ICvsQ_Random_Smart.png')

pt = cmpPlots_Random_Smart()
pt.plot_random_smart(sys.argv[1], sys.argv[2], [sys.argv[x] for x in range(3, len(sys.argv))])