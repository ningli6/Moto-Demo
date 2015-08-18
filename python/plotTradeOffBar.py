'''
Plot trade-off bar for k anonymity and k clustering
'''
import sys
import matplotlib.pyplot as plt

from pyFunctions import PyFunctions

class tradeOffBars:
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
			if files[x] == 'traddOff_KAnonymity.txt':
				title = "Trade-off bar of k anonymity"
			elif files[x] == 'traddOff_KClustering.txt':
				title = "Trade-off bar of k clustering"
			plt.figure()
			plt.bar([k - 0.25 for k in l[0]], pyf.normalize(l[1]), 0.5, color = 'g')
			plt.xticks(map(int, l[0]))
			plt.yticks([0.1 * i for i in range(1, 12)])
			plt.title(title)
			plt.xlabel('k')
			plt.ylabel('IC')
			plt.tight_layout()
			# plt.show()
			plt.savefig(output + files[x].replace('txt', 'png'))

trdOfP = tradeOffBars()
trdOfP.plot([sys.argv[x] for x in range(1, len(sys.argv))])
# trdOfP.plot(['traddOff_KAnonymity.txt'])