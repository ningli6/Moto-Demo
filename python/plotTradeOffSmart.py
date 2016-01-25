'''
Plot trade-off curves for additive noise and transfiguration with smart queries
'''
import sys
import matplotlib.pyplot as plt

from pyFunctions import PyFunctions

class SmartTradeOffPlots:

	def plot(self, dataDir, plotDir, files):
		output = plotDir
		pyf = PyFunctions()
		for x in range(len(files)):
			path = dataDir + files[x]
			try:
				f = open(path, 'r')
			except Exception, e:
				continue
			plt.figure()
			l = [map(float, line.split()) for line in f]
			if files[x] == 'traddOff_smart_AdditiveNoise.txt':
				title = "Trade-off curve of additive noise with smart queries"
				xlabel = 'Noise level'
			elif files[x] == 'traddOff_smart_Transfiguration.txt':
				title = "Trade-off curve of transfiguration with smart queries"
				xlabel = 'Number of sides'
				plt.xticks(map(int, l[0]))
			plt.plot(l[0], pyf.normalize(l[1]))
			plt.legend()
			plt.title(title)
			plt.xlabel(xlabel)
			plt.ylabel('IC')
			plt.tight_layout()
			plt.savefig(output + files[x].replace('txt', 'png'))

trdOfP = SmartTradeOffPlots()
trdOfP.plot(sys.argv[1], sys.argv[2], [sys.argv[x] for x in range(3, len(sys.argv))])
