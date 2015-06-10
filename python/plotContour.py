'''
Try to plot contourf from output data then overlay on google map
'''
import numpy as np
import matplotlib.pyplot as plt
import urllib
import math
import MercatorProjection
import scipy.interpolate as inter
import scipy.ndimage

def latLonToMeters(Slat, Nlat, Wlng, Elng):
	pi = math.pi
	originShift = 2 * pi * 6378137 / 2.0
	x1 = Wlng * originShift / 180
	x2 = Elng * originShift / 180
	y1 = math.log(math.tan((90 + Slat) * pi / 360 )) / (pi / 180)
	y2 = math.log(math.tan((90 + Nlat) * pi / 360 )) / (pi / 180)
	y1 = y1 * originShift / 180
	y2 = y2 * originShift / 180
	return [x1, x2, y1, y2]

# read in data from text file
f = open ( '/Users/ningli/Desktop/Project/output/demoTable_0_bounds.txt' , 'r')
input = [ map(float, line.split()) for line in f ]
Nlat = input[0][0]
Slat = input[0][1]
Wlng = input[0][2]
Elng = input[0][3]

f = open ( '/Users/ningli/Desktop/Project/output/demoTable_0_rowcol.txt' , 'r')
input = [ map(float, line.split()) for line in f ]
rows = int(input[0][0])
cols = int(input[0][1])

f = open ( '/Users/ningli/Desktop/Project/output/demoTable_0_pu.txt' , 'r')
input = [ map(float, line.split()) for line in f ]
markers = input

f = open ( '/Users/ningli/Desktop/Project/output/demoTable_0.txt' , 'r')
input = [ map(float, line.split()) for line in f ]
# reconstruct matrix format of input data
data = [[input[i * cols + j][2] for j in range(0, cols)] for i in range(0, rows)]

# get static map
# map size
width = 640;
height = 640;
size = str(width) + 'x' + str(height)
# compute center
center = str((Nlat + Slat) / 2) + ',' + str((Wlng + Elng) / 2)
# compute zoom level
xyList = latLonToMeters(Slat, Nlat, Wlng, Elng)
minResX = (xyList[1] - xyList[0]) / width
minResY = (xyList[3] - xyList[2]) / height
minRes = max(minResX, minResY)
tileSize = 256;
initialResolution = 2 * math.pi * 6378137 / tileSize
zoomlevel = math.floor(math.log(initialResolution/minRes, 2))
if zoomlevel < 0:
    zoomlevel = 0
if zoomlevel > 19:
    zoomlevel = 19
zoom = str(int(zoomlevel))
# Resolution scale factor
scale = str(2)
# image format 
format = 'png'
# map type
type = 'hybrid'
# google api key
key = 'AIzaSyB6ss_yCVoGjERLDXwydWcyu21SS-dToBA'
# markers
markerURL = ''
for i in range(0, len(markers)):
	markerURL += '&markers=' + str(markers[i][0]) + "," + str(markers[i][1])
# construct url
url = 'http://maps.googleapis.com/maps/api/staticmap?center=' + center + '&zoom=' + zoom + '&size=' + size + '&scale=' + scale + '&maptype=' + type + '&format=' + format + '&key=' + key + markerURL

# calculate bounds
centerPoint = MercatorProjection.G_LatLng((Nlat + Slat) / 2, (Wlng + Elng) / 2)
corners = MercatorProjection.getCorners(centerPoint, int(zoomlevel), width, height)
latPerPixel = (corners['N'] - corners['S']) / 1280
lngPerPixel = (corners['E'] - corners['W']) / 1280

# interpolate matrix
newRows = int(round(0.05 / latPerPixel * rows))
newCols = int(round(0.05 / lngPerPixel * cols))
grid_z = scipy.ndimage.zoom(data, [((float)(newRows))/rows, ((float)(newCols))/cols], order=0)

# project matrix on google map
startRow = 640 - (len(grid_z) / 2)
startCol = 640 - (len(grid_z[0]) / 2)
extMatrix = [[0 for i in range(0, 1280)] for j in range(0, 1280)]
# print extMatrix
for i in range(startRow, startRow + len(grid_z)):
	for j in range(startCol, startCol + len(grid_z[0])):
		extMatrix[i][j] = grid_z[i - startRow][j - startCol]
for i in range(0, 1280):
	for j in range(0, 1280):
		if (extMatrix[i][j] == 0):
			extMatrix[i][j] = np.nan;

# save image
img = urllib.urlopen(url)
with open('/Users/ningli/Desktop/map.png', 'w') as f:
    f.write(img.read())
# read image
im = plt.imread('/Users/ningli/Desktop/map.png')
implot = plt.imshow(im)

# overlay
cs = plt.contourf(extMatrix)
plt.colorbar(cs)
plt.xlabel('columns')
plt.ylabel('rows')
plt.show()