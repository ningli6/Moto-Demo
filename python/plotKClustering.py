'''
This script is used for plotting results of k-clustering from output
for small number of channels such as 1 and 2
'''
import matplotlib.pyplot as plt

# compute average of both channels
def avglist(l1, l2):
	assert len(l1) == len(l2)
	result = []
	for index in range(len(l1)):
		result.append((l1[index] + l2[index]) / 2.0)
	return result

# if needed, use this function to get rid of ic for query number 0
def chophead(l):
	assert len(l) > 0
	return l[1: ]

plotKClustering = raw_input('Which K-Clustering data you want to plot: ')
plotKClustering = sorted(map(int, plotKClustering.split()))
ch = raw_input('Do you want to plot privacy at query number 0 [Y/N]: ').lower()
assert ch == 'y' or ch == 'yes' or ch == 'n' or ch == 'no'

for index in plotKClustering:
	try:
		# if -1 plot original ic, the one without countermeasure
		if index == -1:
			path = '/Users/ningli/Desktop/Project/output/ic.txt'
		# plot the one with k-clustering technique
		else:
			path = '/Users/ningli/Desktop/Project/output/ic_kc_' + str(index) + '.txt'
		f = open ( path , 'r')
		l = ([ map(float,line.split()) for line in f ])
		# for 2 channels
		# avg = avglist(l[1], l[2])
		if index == -1:
			label = 'No countermeasure'
		else:
			label = 'K: ' + str(index)
		# for 1 channel
		avg = l[1]
		if (ch == 'n' or ch == 'no'):
			avg = chophead(avg)
			l[0] = chophead(l[0])
		plt.plot(l[0], avg, marker = 'o', label = label)
		plt.legend()
	except IOError:
		print 'No such file'

plt.title('IC vs queries with K-Clustering')
plt.xlabel('Number of queries')
plt.ylabel('IC')
plt.show()