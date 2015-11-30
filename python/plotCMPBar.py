'''
Plot final inaccuracy results of random queries vs smart queries
'''

import sys
import matplotlib.pyplot as plt

from pyFunctions import PyFunctions

class CmpBar:

	# @param {[String]} filename that need to be plot
	def plot(self, dataDir, plotDir, files):
		n = len(files) # number of bar pairs needed
		cmName = {'NoCountermeasure': 'No countermeasure', 'AdditiveNoise': 'Additive noise', 
			'Transfiguration': 'Transfiguration', 'KAnonymity': 'K Anonymity', 'KClustering': 'K Clustering'}
		width = 0.35
		pyf = PyFunctions()
		ic_random = []  # hold ic value of random queries
		ic_smart = []   # hold ic value of smart queries
		ctm = []
		for x in range(len(files)):	
			try:
				path = dataDir + 'cmp_' + files[x] + '.txt'
				f = open(path, 'r')
				l = [map(float, line.split()) for line in f]
				avgl = pyf.average(l[1:])
				ic_random.append(avgl[len(avgl) - 1])
				path = dataDir + 'cmp_smart_' + files[x] + '.txt'
				f = open(path, 'r')
				l = [map(float, line.split()) for line in f]
				avgl = pyf.average(l[1:])
				ic_smart.append(avgl[len(avgl) - 1])
				ctm.append(cmName[files[x]])
			except Exception, e:
				continue
		ind_random = [float(2 * i) for i in range(0, n)]
		ind_smart = [float(2 * i) + width for i in range(0, n)]
		fig, ax = plt.subplots()
		rects1 = plt.bar(ind_random, ic_random, width, color='r', label='Random query')
		rects2 = plt.bar(ind_smart, ic_smart, width, color='y', label='Smart query')
		plt.ylabel('IC')
		plt.xlabel('Countermeasures')
		plt.xticks(ind_smart, tuple(ctm))
		plt.title('IC by random query vs smart query with different countermeasures')
		plt.legend(loc='upper center')
		plt.tight_layout()
		# plt.show()
		plt.savefig(plotDir + 'cmpbar.png')

cmpBar = CmpBar()
cmpBar.plot(sys.argv[1], sys.argv[2], [sys.argv[x] for x in range(3, len(sys.argv))])