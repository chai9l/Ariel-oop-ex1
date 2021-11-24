# Ariel-oop-ex1
![enter image description here](https://www.researchgate.net/profile/Jun_Luo4/publication/224611870/figure/fig1/AS:668974324920326@1536507316167/Network-graph-model-The-WSN-has-300-nodes-black-points-and-10-sinks-white-points.png)
***This repository represent a collection of algorithms on weighted undirected graphs.***
## **File List :**

 - weighted_graph - This interface represents an undirectional unweighted graph.
 - weighted_graph_algorithms - This interface represents an Undirected (positive) Weighted Graph Theory    algorithms.
 - node_info - This interface represents the set of operations applicable on a node in a graph.
 - WGraph_DS - Graph_DS implements weighted_graph.
 - WGraph_Algo - Graph_Algo implements graph_algorithms.

## WGraph_Algo methods
the methods that activates for a given graph:

***copy() :***
this function copies a given graph.

***isConnected() :*** 
The isConnected function is a manager function using a worker function called by the same name.
the worker function "isConnected" which can be found at the WGraph_DS.java file implements the BFS algorithm.

***shortestPath(int src, int dest) :***
The ShortestPath function implements the Dijkstra algorithm to find the shortest between a given integer src
to a given integer dest.

***shortestPathDist(int src, int dest) :***
This function relies on "shortestPath" function, we run the mentioned function and get back the list containing the shortest known path from a given src to dest. 
this function calculates the distance between each node in the list and returns the sum of the weights between each node.

## JUnit 5 Tests
-   WGraph_AlgoTest
-   WGraph_DSTest
