This project is to implement techniques for protecting privacy of primary users proposed in paper: Protecting the Primary Users’ Operational Privacy in Spectrum Sharing. [2014 IEEE International Symposium on Dynamic Spectrum Access Networks (DYSPAN), p 236-47, 2014]

Usage:
Main.java is the entrance of the program. We will allow user to specify parameters such as maps range, cell size, number of queries. We set up several channels that PUs are working in, and assign PUs in different channels. The attacker then makes queries and receive responses from PU. With this knowledge it will infer the location of PUs for each channel. The program then produces the probability of PU's existence for each channels. The Matlab code uses these data to plot on google map to reflect these data.

We use latitude & longitude instead of cell index to do computation in our map. Cell size are defined by degree. Conceptually user will assign a rectangle area for the program.

We compute inaccuracy for each channel to evaluate the result of inference. The actual compute formula is in presented in the paper.

First technique for protecting location privacy is perturbation with additive noise.