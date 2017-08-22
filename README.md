# Implement Network Quantity Information measure in CentiScaPe
Google Summer of Code 2017 project for National Resource for Network Biology  

- **Student:** Ivan Bestvina  
- **Primary mentor:** Giovanni Scardoni  

### About
CentiScaPe is a Cytoscape application used to calculate a number of different node centralities of an optionally weighted network, both directed and undirected, as well as plot the results by using real and normalized values in different plots. Several centrality measures are available: degree, average shortest path, eccentricity, closeness, betweenness, centroid, stress, radiality, eigenvector, edge betweenness, bridging centrality, etc. 

This project aims to implement a brand new centrality measure based on Shannon entropy called Network Quantity Information [1]. This measure aims to quantify the entropy of a given network, by firstly calculating the entropy of its nodes and edges which can then be observed as a type of centrality measure. Once we obtain all the node and edge centralities, we can derive from them the complexity of the whole network, which provides us not only with an interesting “one-value” description of the network, but also gives us the ability to compare multiple networks, or multiple parts of the same network.

Apart from comparing multiple networks using this “one-value” approach, we can gain a deeper insight into their similarities and differences by using the two-sample Kolmogorov–Smirnov test.



[1] Netsci2011, the international school and conference on network science (http://www.netsci2011.net/). “A measure for network complexity”. June 2011, Budapest.

