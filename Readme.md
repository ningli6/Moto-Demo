## Introduction
This project implements techniques for protecting privacy of primary users proposed in paper: Protecting the Primary Users’ Operational Privacy in Spectrum Sharing. [2014 IEEE International Symposium on Dynamic Spectrum Access Networks (DYSPAN), p 236-47, 2014]
### Web interface description
User need to specify number of channels that primary users operate on. Then draw an analysis region on Google Map and select locations for primary users on different channels. The region should cover all primary users on any channel. Each channel must have at least one primary user. This web application is able to plot 3 different kinds of figures: Inaccuracy vs Queries, heat map on Google map, trade-off curve/bar. Users need to specify number of queries. Query locations are  either uniformly randomly generated or generated based on more sophisticated algorithm which selects next query location wisely.
### Protection techniques
We compute inaccuracy for each channel to evaluate the result of inference as a metric for our protecting algorithms.
* First technique for protecting location privacy is perturbation with additive noise. Whenever response the server will lie to the querying client in a way that it will chooses to respond with a smaller value if possible.

* Second technique for protecting privacy is to use transfiguration, which transfigure circular contours into convex polygon. It works pretty good for small numbers of sides, such as triangle and rectangle.

* The third technique is K-anonymity, which groups PUs into a group of k members, such that they share a larger area of protection contour, then compute a virtual primary user whose protection contour is the minimum covering circle for all other PUs in that group. Use the virtual PU as the new PU to respond to new queries. Poor grouping decision can lead to poor spectrum utility performance.

* The fourth technique is K-Clustering, which uses a k-means algorithm to classify PUs into different group according to their location distribution, then compute a virtual primary user whose protection contour is the minimum covering circle for all other PUs in that cluster. Use the virtual PU as the new PU to respond to new queries.
### Query options
User can select random query and/or smart query in the process of simulation. Generally random query is much faster and works better with countermeasures. Smart query usually brings down inaccuracy really quick but it's computationally demanding so it can't operate on a larger map. Smart query sometimes won’t cooperate with countermeasures.
### Author
*Ning Li(ningli@vt.edu)*
