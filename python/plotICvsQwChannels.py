'''
Show relation between IC and query for each channel
'''

import matplotlib.pyplot as plt
import sys

class cmpPlots:

	def plot(self, dataDir, plotDir, files):
		output = plotDir
		for x in range(len(files)):
			path = dataDir + files[x]
			try:
				f = open(path, 'r')
			except Exception, e:
				continue
			l = [map(float, line.split()) for line in f]
			if files[x] == 'averageIC_NoCountermeasure.txt':
				label0 = "No countermeasure channel 0"
				label1 = "No countermeasure channel 1"
			elif files[x] == 'cmp_AdditiveNoise.txt':
				label0 = "Additive noise channel 0"
				label1 = "Additive noise channel 1"
			elif files[x] == 'cmp_Transfiguration.txt':
				label0 = "Transfiguration channel 0"
				label1 = "Transfiguration channel 1"
			elif files[x] == 'cmp_kAnonymity.txt':
				label0 = "K anonymity channel 0"
				label1 = "K anonymity channel 1"
			elif files[x] == 'cmp_kClustering.txt':
				label0 = "K clustering channel 0"
				label1 = "K clustering channel 1"
			plt.plot(l[0], l[1], label = label0)
			plt.plot(l[0], l[2], label = label1)
		plt.legend()
		plt.title('Inaccuracy vs queries')
		plt.xlabel('Number of queries')
		plt.ylabel('IC')
		plt.tight_layout()
		plt.savefig(output + 'ICvsQwChannel.png')
		# plt.show()

pt = cmpPlots()
pt.plot(sys.argv[1], sys.argv[2], [sys.argv[x] for x in range(3, len(sys.argv))])