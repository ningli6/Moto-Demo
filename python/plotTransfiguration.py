'''
This script is used for plotting results of transfiguration from output
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

plotPolygon = raw_input('Which polygon data you want to plot: ')
plotPolygon = sorted(map(int, plotPolygon.split()))

for index in plotPolygon:
	try:
		if index == -1:
			path = '/Users/ningli/Desktop/Project/output/ic.txt'
		else:
			path = '/Users/ningli/Desktop/Project/output/ic_tf_' + str(index) + '.txt'
		f = open ( path , 'r')
		l = chophead([ map(float,line.split()) for line in f ])
		# for 2 channels compute average
		avg = avglist(l[1], l[2]);
		if index == -1:
			label = 'sides: infinity'
		else:
			label = 'sides: ' + str(index)
		plt.plot(l[0], avg, marker = 'o', label = label)
		plt.legend()
	except IOError:
		print 'No such file'

plt.title('IC vs queries with transfiguration')
plt.xlabel('Number of queries')
plt.ylabel('IC')
plt.show()