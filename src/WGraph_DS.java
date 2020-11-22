package ex1.src;

import java.io.Serializable;
import java.util.*;

public class WGraph_DS implements weighted_graph, Serializable {

    public class NodeInfo implements node_info, Serializable {

        private Integer key;
        double tag;
        HashSet<node_info> neighbors;
        String info;

        public NodeInfo() {
            key = 0;
            tag = 0;
            neighbors = new HashSet<>();
            info = "";
        }

        public NodeInfo(int key) {
            this.key = key;
            tag = 0;
            neighbors = new HashSet<>();
            info = "";
        }

        @Override
        public int getKey() {
            return key;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {
            info = s;
        }

        @Override
        public double getTag() {
            return tag;
        }

        @Override
        public void setTag(double t) {
            tag = t;
        }

        public void addNi(node_info t) {
            if(!neighbors.contains(t)) {
                neighbors.add(t);
            }
        }

        public Collection<node_info> getNi() {
            return neighbors;
        }

        public void removeNi(node_info t) {
            if(!neighbors.contains(t)) {
                return;
            }
            neighbors.remove(t);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NodeInfo nodeInfo = (NodeInfo) o;

            if (Double.compare(nodeInfo.tag, tag) != 0) return false;
            if (key != null ? !key.equals(nodeInfo.key) : nodeInfo.key != null) return false;
            return info != null ? info.equals(nodeInfo.info) : nodeInfo.info == null;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = key != null ? key.hashCode() : 0;
            temp = Double.doubleToLongBits(tag);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }

    private HashMap<Integer,node_info> graph;
    private HashMap<String,Double> edges;
    private int mc;

    public WGraph_DS() {
        graph = new HashMap<>();
        edges = new HashMap<>();
        mc = 0;
    }

    /** Initialisation function
     * @param g
     */
    public void reset(WGraph_DS g) {
        for(node_info i : g.getV()) {
            i.setTag(0);
        }
    }

    /** the isConnected function is a worker function using that's implementing the BFS algo.
     * first we check if the graph size is not 0.
     * afterwards we create a Queue to and add our first node (we find that node via stream().findFirst().get() ).
     * then we set it's tag to be 1 meaning we've already visited it.
     * (unvisited nodes will have tag=0)
     * now we enter the while loop and start polling 1 node at a time from it, then we're looking at the node's neighbors and start
     * iterating them via for each loop checking if they're visited or not.
     * We'll notice that each time we change the tag we're adding back to the queue to keep on iterating and update our counter.
     * if we finish the run with a counter that does not equal the size of the entire graph it means we missed some nodes because they're not connected
     * then returning false and vise-versa.
     * @return
     */
    public boolean isConnected() {
        if(graph.size()==0) {
            return true;
        }
        int counter = 1;
        node_info first = graph.values().stream().findFirst().get();
        Queue<NodeInfo> queue = new LinkedList<>();
        queue.add((NodeInfo)first);
        first.setTag(1);
        while (!queue.isEmpty()) {
            NodeInfo temp = queue.poll();
            for (node_info i : temp.getNi()) {
                if (i.getTag() == 0) {
                    i.setTag(1);
                    queue.add((NodeInfo)i);
                    counter++;
                }
            }
        }
        reset(this);
        return counter == graph.size();
    }

    /** this function is used as an alternate way of pairing 2 nodes.
     * @param node1
     * @param node2
     * @return
     */
    private String generateKey(int node1, int node2) {
        String s = node1 <= node2 ? node1 + "/" + node2 : node2 + "/" + node1;
        return s;
    }


    @Override
    public node_info getNode(int key) {
        return graph.get(key);
    }

    /** checks if the 2 given node has an edge between them
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if(graph == null) { return false; }
        if(!graph.containsKey(node1) && !graph.containsKey(node2)) {
            return false;
        }
        String s = generateKey(node1,node2);
        Double ret = edges.get(s);
        if(ret == null) {
            return false;
        }
        return true;
    }

    @Override
    public double getEdge(int node1, int node2) {
        if(!graph.containsKey(node1) && !graph.containsKey(node2)) {
            return -1;
        }
        String s = generateKey(node1,node2);
        double ret = edges.get(s);
        return ret;
    }

    /** adds a node to the graph from a given key.
     * @param key
     */
    @Override
    public void addNode(int key) {
        if (graph == null) {
             return;
        }
        graph.put(key, new NodeInfo(key));
        mc++;
    }

    /** connects 2 nodes using the "generateString" helper function */
    @Override
    public void connect(int node1, int node2, double w) {
        if(node1 == node2) {
            return;
        }
        if(graph.containsKey(node1) && graph.containsKey(node2)) {
            String key = generateKey(node1,node2);
            if(!hasEdge(node1,node2)) {
                edges.put(key,w);
                NodeInfo x = (NodeInfo)graph.get(node1);
                NodeInfo y = (NodeInfo)graph.get(node2);
                x.addNi(y);
                y.addNi(x);
                mc++;
            }
        }
    }

    /** returns the graph values.
     * @return
     */
    @Override
    public Collection<node_info> getV() {
        if(graph == null) { return null; }
        return graph.values();
    }

    /** returns the neighbors of a given node.
     * @param node_id
     * @return
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        NodeInfo ret = (NodeInfo)graph.get(node_id);
        return ret.getNi();
    }

    /** removes a node from the graph using a given key.
     * @param key
     * @return
     */
    @Override
    public node_info removeNode(int key) {
        NodeInfo ret = (NodeInfo)graph.get(key);
        LinkedList<node_info> l = new LinkedList<>();
        if (ret != null) {
            for (node_info i : ret.getNi()) {
                l.add(i);
            }
            for(node_info i : l) {
                removeEdge(i.getKey(),key);
            }
            mc++;
            return graph.remove(ret.getKey());
        }
        return null;
    }

    /** removes an edge between 2 given nodes.
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if(graph==null) { return; }
        if(graph.containsKey(node1) && graph.containsKey(node2)) {
            String s = generateKey(node1, node2);
            if (hasEdge(node1, node2)) {
                edges.remove(s);
                NodeInfo nodeX = (NodeInfo)graph.get(node1);
                NodeInfo nodeY = (NodeInfo)graph.get(node2);
                nodeX.removeNi(nodeY);
                nodeY.removeNi(nodeX);
                mc++;
            }
        }
    }

    /** returns the size of a graph.
     * @return
     */
    @Override
    public int nodeSize() {
        if(graph == null) { return 0; }
        return graph.size();
    }

    /** returns the size of the edges list of a certain graph.
     * @return
     */
    @Override
    public int edgeSize() {
        if(graph == null) { return 0; }
        return edges.size();
    }

    @Override
    public int getMC() {
        return mc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WGraph_DS wGraph_ds = (WGraph_DS) o;

        if (mc != wGraph_ds.mc) return false;
        if (graph != null ? !graph.equals(wGraph_ds.graph) : wGraph_ds.graph != null) return false;
        return edges != null ? edges.equals(wGraph_ds.edges) : wGraph_ds.edges == null;
    }

    @Override
    public int hashCode() {
        int result = graph != null ? graph.hashCode() : 0;
        result = 31 * result + (edges != null ? edges.hashCode() : 0);
        result = 31 * result + mc;
        return result;
    }
}
