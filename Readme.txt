Usage:
main.java is the entrance of the program.
Right now there is only 1 PU an 1 SU. SU sends query Q(location) to server, server respondes with R(power), according to the distances between query point to PU.
SU then uses that response to update its inference map, based on different situation proposed in the paper. It's kind of hard to visualize the results using terminal output. With small numbers of cells we can still convince ourselves that the algorithm works as expected though.