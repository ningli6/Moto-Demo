class PyFunctions:

	# Compute average at each index among all lines
	# @param {[][]} lines
	# @return {[]}  average lines
	def average(self, lines):
		if not lines or len(lines[0]) == 0:
			return []
		re = [0 for x in range(len(lines[0]))]
		for x in range(len(lines[0])):
			for j in range(len(lines)):
				re[x] += lines[j][x] / float(len(lines))
		return re

	# Normalize values in the array
	# @param {[]} array
	# @return {[]}  normalized array
	def normalize(self, nums):
		maxVal = float(max(nums))
		return [(val / float(maxVal)) for val in nums]