'''
Plot trade-off curves for additive noise and transfiguration
'''
import sys
import matplotlib.pyplot as plt

from pyFunctions import PyFunctions

class tradeOffPlots:

	# @param {[String]} filename that need to be plot
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
			if files[x] == 'traddOff_AdditiveNoise.txt':
				title = "Trade-off curve of additive noise"
				xlabel = 'Noise level'
			elif files[x] == 'traddOff_Transfiguration.txt':
				title = "Trade-off curve of transfiguration"
				xlabel = 'Number of sides'
			plt.figure()
			plt.plot(l[0], pyf.normalize(l[1]))
			plt.legend()
			plt.title(title)
			plt.xlabel(xlabel)
			plt.ylabel('IC')
			plt.tight_layout()
			plt.savefig(output + files[x].replace('txt', 'png'))

trdOfP = tradeOffPlots()
# trdOfP.plot([sys.argv[x] for x in range(1, len(sys.argv))])
trdOfP.plot(['traddOff_AdditiveNoise.txt', 'traddOff_Transfiguration.txt'])