This project implements techniques for protecting privacy of primary users proposed in paper: Protecting the Primary Users’ Operational Privacy in Spectrum Sharing. [2014 IEEE International Symposium on Dynamic Spectrum Access Networks (DYSPAN), p 236-47, 2014]

We will allow user to specify parameters such as maps range, cell size, number of queries. We set up several channels assign PUs in different channels. The attacker then makes queries and receive responses from PU. With this knowledge it will infer the location of PUs for each channel. The program then produces the probability of PU's existence for each channels. The Matlab code uses these data to plot on google map to reflect these data. All PUs and SUs are assumed to be at the center of the cell.

We use latitude & longitude instead of cell index to do computation in our map, which is more accurate. Cell size are defined by degree. Conceptually user will assign a rectangle area for the program to do the demo.

We compute inaccuracy for each channel to evaluate the result of inference. The actual compute formula is in presented in the paper.

First technique for protecting location privacy is perturbation with additive noise. Whenever response the server will lie to the querying client in a way that it will chooses to response with a smaller value if possible.

Second technique for protecing privacy is to use transfiguration, which tansfigure circular contours into convex polygon. It works pretty good for small numbers of sides, such as triangle and rectangle.

The third technique is K-anonymity, which groups PUs into a group of k members, such that they share a larger area of protection contour, then compute a virtual primary user whose protection coutour is the minimum covering circle for all other PUs in that group. Use the virtual PU as the new PU to response to new queries. Poor grouping decision can lead to poor spectrum utility performance.

The forth technique is K-Clustering, which uses a k-means algorithm to classify PUs into different group according to their loaction distribution, then compute a virtual primary user whose protection coutour is the minimum covering circle for all other PUs in that cluster. Use the virtual PU as the new PU to response to new queries.

Author: Ning Li(ningli@vt.edu)
