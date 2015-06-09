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
rows = 93
cols = 184
Nlat = 39.232253141714885;
Slat = 34.542762387234845;
Wlng = -86.2646484375;
Elng = -77.05810546875;

f = open ( '/Users/ningli/Desktop/Project/output/demoTable_0.txt' , 'r');
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
print 'xyList: ' + str(xyList[0]) + ' ' + str(xyList[1]) + ' ' + str(xyList[2]) + ' ' + str(xyList[3])
minResX = (xyList[1] - xyList[0]) / width
minResY = (xyList[3] - xyList[2]) / height
print 'minResX: ' + str(minResX)
print 'minResY: ' + str(minResY)
minRes = max(minResX, minResY)
print 'minRes: ' + str(minRes)
tileSize = 256;
initialResolution = 2 * math.pi * 6378137 / tileSize
print 'initialResolution: ' + str(initialResolution)
zoomlevel = math.floor(math.log(initialResolution/minRes, 2))
print 'zoomelevel: ' + str(zoomlevel)
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
marker1 = '37.544577320855815,-82.9248046875'
marker2 = '36.27970720524017,-80.3759765625'
# construct url
url = 'http://maps.googleapis.com/maps/api/staticmap?center=' + center + '&zoom=' + zoom + '&size=' + size + '&scale=' + scale + '&maptype=' + type + '&format=' + format + '&key=' + key + '&markers=' + marker1 + '&markers=' + marker2
# print url
# calculate bounds
centerPoint = MercatorProjection.G_LatLng((Nlat + Slat) / 2, (Wlng + Elng) / 2)
corners = MercatorProjection.getCorners(centerPoint, int(zoomlevel), width, height)
latPerPixel = (corners['N'] - corners['S']) / 1280
lngPerPixel = (corners['E'] - corners['W']) / 1280

# interpolate matrix
# vals = np.reshape(data, (rows * cols))
# pts = np.array([[i,j] for i in np.linspace(0,1,cols) for j in np.linspace(0,1,rows)] )
newRows = int(round(0.05 / latPerPixel * rows))
newCols = int(round(0.05 / lngPerPixel * cols))

grid_z = scipy.ndimage.zoom(data, [int(newRows/rows), int(newCols/cols)], order=0)

# print 'ext rows: ' + str(len(grid_z))
# print 'ext cols: ' + str(len(grid_z[0]))
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

# plt.matshow(grid_z)
# plt.matshow(extMatrix)
# plt.show()

# get image
img = urllib.urlopen(url)
with open('/Users/ningli/Desktop/map.png', 'w') as f:
    f.write(img.read())
# read image
im = plt.imread('/Users/ningli/Desktop/map.png')
implot = plt.imshow(im)

# overlay
cs = plt.contourf(extMatrix)
plt.colorbar(cs)

# plt.gca().invert_yaxis()
plt.title('Transfiguration')
plt.xlabel('columns')
plt.ylabel('rows')
plt.show()