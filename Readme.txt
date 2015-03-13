Usage:
main.java is the entrance of the program.
Right now there is only 1 PU an 1 SU. SU sends query Q(location) to server, server respondes with R(power), according to the distances between query point to PU.
SU then uses that response to update its inference map, based on different situation proposed in the paper. 

Uses latitude & longitude instead of cell index to compute. Cell size are defined by degree. User must assign a rectangle area to the program.

Add PU class. Test both String & double input format. Enable decimals using String input.

Add a ColorPan class to visualize the results of probability matrix. The gray area means the probability of existence of PU is still 0.5. The white area means p[i][j] is set to 0. Black area means p[i][j] has increased.