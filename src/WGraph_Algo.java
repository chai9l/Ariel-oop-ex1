package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms, Serializable {

    private WGraph_DS graph;
    static double solution = 0;

    public WGraph_Algo() {
        graph = new WGraph_DS();
    }

    /** Initialisation function
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        graph = (WGraph_DS) g;
    }

    @Override
    public weighted_graph getGraph() {
        return graph;
    }

    /** this function copies a given graph via iterator called runner.
     * we first go through the graph we want to copy and start copying the key values.
     * then we go through the graph via iterator and check the edges between each node.
     * we then update the nodes we need to connect in the copied graph.
     * @return
     */
    @Override
    public weighted_graph copy() {
        weighted_graph copied = new WGraph_DS();
        for (node_info i : graph.getV()) {
            copied.addNode(i.getKey());
        }
        Iterator<node_info> runner = this.graph.getV().iterator();
        while (runner.hasNext() && copied.edgeSize() != graph.edgeSize()) {
            int k = runner.next().getKey();
            for (node_info neighbor : graph.getV(k)) {
                double e = graph.getEdge(k,neighbor.getKey());
                int n = neighbor.getKey();
                copied.connect(n,k,e);
            }
        }
        return copied;
    }

    /** isConnected function uses the implementation "isConnected" that's over the Graph_DS class. */
    @Override
    public boolean isConnected() {
        if(graph == null) { return true; }
        boolean ret = graph.isConnected();
        return ret;
    }

    /** This function relies on "shortestPath" function, we run the mentioned function and get back the list containing the shortest known path
     * from a given src to dest.
     * this function calculates the distance between each node in the list and returns the sum of the weights between each node.
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        List<node_info> x = new LinkedList<>();
        x=shortestPath(src,dest);
        if(x.isEmpty()) { return -1;}
        else return solution;
    }

    /** The ShortestPath function implements the Dijkstra algorithm.
     * first we create a nested utility class to hold 3 values of choice.
     * now we're starting setting up a table to measure the distances between the given nodes.
     * first we create a priority queue, we compare the shortest distance to know which node we prioritize.
     * then we start traversing the graph via tags, meaning we check each node's neighbors by checking if it's tag = 1 or 0. (1 = visited, 0 = unvisited)
     * say we got to an unvisited node, now we check it's distance.
     * first we use "dist" to take the current node's distance from it's previous node, then we add it to tempShortestDistance to check if the new
     * achieved distance is bigger than the currDistance. (currDistance = is the shortest distance registered up until now)
     * after we finish setting up our table, meaning we know what the shortest distance between dest and src are,
     * we go to the position where dest is at and then start tracing back our steps until we reach back to the start,
     * when we do we're done and we're returning the linked list we've added the nodes to.
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {

        //Utility class that holds 3 fields:
        // 1) Shortest distance
        // 2) Previous node
        // 3) Current node
        class hashmapValue {
            Double shortestDist;
            WGraph_DS.NodeInfo previous;
            WGraph_DS.NodeInfo current;
            public hashmapValue(WGraph_DS.NodeInfo c) {
                shortestDist = Double.MAX_VALUE;
                previous = null;
                current=c;
            }

            public void setShortestDist(Double shortestDist) {
                this.shortestDist = shortestDist;
            }
        }

        LinkedList<node_info> ret = new LinkedList<>();
        if(graph == null) { return ret; }
        if(graph.getNode(src) == null || graph.getNode(dest) == null) {
            return ret;
        }
        PriorityQueue<hashmapValue> que = new PriorityQueue<>(new Comparator<hashmapValue>() {
            @Override
            public int compare(hashmapValue o1, hashmapValue o2) {
                return Double.compare(o1.shortestDist,o2.shortestDist);
            }
        });

        HashMap<Integer,hashmapValue> table = new HashMap<>();
        for(node_info i : graph.getV()) {
            table.put(i.getKey(), new hashmapValue((WGraph_DS.NodeInfo)i));
        }
        table.get(src).setShortestDist(0.0);
        que.add(table.get(src));

        // Creating the table to map our graph
        while(!que.isEmpty()) {
            WGraph_DS.NodeInfo poll = que.poll().current;
            for(node_info node : poll.getNi()) {
                if(node.getTag() == 0) {
                    node.setTag(1);
                    double dist = graph.getEdge(node.getKey(), poll.getKey());
                    double tempShortestDist = table.get(poll.getKey()).shortestDist;
                    double currDist = table.get(node.getKey()).shortestDist;
                    if(currDist >= dist + tempShortestDist) {
                        table.get(node.getKey()).shortestDist = dist + tempShortestDist;
                        table.get(node.getKey()).previous = poll;
                        que.add(table.get(node.getKey()));
                    }
                }
            }
        }
        WGraph_DS.NodeInfo temp = (WGraph_DS.NodeInfo) graph.getNode(dest);
        while(temp!=null) {
            ret.addFirst(temp);
            temp = table.get(temp.getKey()).previous;
        }
        solution = table.get(dest).shortestDist;
        reset(graph);
        return ret;
    }

    /** This function resets the graph's tags to 0 by going through each node via for each loop */
    public void reset(WGraph_DS g) {
        for(node_info i : g.getV()) {
            i.setTag(0);
        }
    }


    /** the Save function, attempts to save a graph to a given file via String argument.
     * we first open a file using the given string we get.
     * afterwards we write into the file via object output stream.
     * @param file - the file name (may include a relative path).
     * @return
     */
    @Override
    public boolean save(String file) {
        try {
            File saveFile = new File(file);
            saveFile.createNewFile();
            FileOutputStream write = new FileOutputStream(saveFile);
            ObjectOutputStream out = new ObjectOutputStream(write);
            out.writeObject(graph);
            out.close();
            write.close();
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    /** the Load function, attempts to open a file from a given string via argument.
     * we first open the file afterwards we write into a new graph via object input stream.
     * @param file - file name
     * @return
     */
    @Override
    public boolean load(String file) {
        try{
            FileInputStream read = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(read);
            graph = (WGraph_DS)in.readObject();
            in.close();
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WGraph_Algo)) return false;
        WGraph_Algo that = (WGraph_Algo) o;
        return Objects.equals(getGraph(), that.getGraph());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGraph());
    }
}
