'''
This script is used for plotting results of k-anonymity from output
'''
import matplotlib.pyplot as plt

def avglist(l1, l2):
	assert len(l1) == len(l2)
	result = []
	for index in range(len(l1)):
		result.append((l1[index] + l2[index]) / 2.0)
	return result

def chophead(l):
	assert len(l) == 3
	res = []
	for line in l:
		line = line[1:]
		res.append(line)
	return res

plotKAnonymity = raw_input('Which K-anonymity data you want to plot: ')
plotKAnonymity = sorted(map(int, plotKAnonymity.split()))

for index in plotKAnonymity:
	try:
		if index == -1:
			path = '/Users/ningli/Desktop/Project/output/ic.txt'
		else:
			path = '/Users/ningli/Desktop/Project/output/ic_ka_' + str(index) + '.txt'
		f = open ( path , 'r')
		l = ([ map(float,line.split()) for line in f ])
		# for 2 channels
		# avg = avglist(l[1], l[2])
		# for 1 channel
		avg1 = l[1]
		avg2 = l[2]
		# if index == -1:
		# 	label = 'No countermeasure'
		# else:
		# 	label = 'K: ' + str(index)
		label1 = 'channel 0'
		label2 = 'channel 1'
		plt.plot(l[0], avg1, marker = 'o', label = label1)
		plt.plot(l[0], avg2, marker = 'x', label = label2)
		plt.legend()
	except IOError:
		print 'No such file'

plt.title('IC vs queries with K-anonymity')
plt.xlabel('Number of queries')
plt.ylabel('IC')
plt.show()