package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
class WGraph_AlgoTest {

    private weighted_graph graph;
    private weighted_graph_algorithms algo;

    @BeforeEach
    void setup() {
        algo = new WGraph_Algo();
        graph = new WGraph_DS();
        algo.init(graph);
    }

    @Test
    void copy() {
        weighted_graph copied = new WGraph_DS();
        copied = algo.copy();
        assertEquals(graph,copied);
        assertNotSame(graph,copied);
    }

    @Test
    void isConnected() {
        assertTrue(algo.isConnected());
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.connect(1,2,10);
        assertFalse(algo.isConnected());
        graph.connect(1, 3, 15);
        assertTrue(algo.isConnected());
        graph.removeNode(1);
        graph.removeNode(2);
        assertTrue(algo.isConnected());
    }

    @Test
    void shortestPathDist() {
        double check = -1;
        assertTrue(check == algo.shortestPathDist(1,10));

        for(int i = 1; i<=10; i++) {
            graph.addNode(i);;
        }
        graph.connect(1,2,1);
        graph.connect(2,3, 1);
        graph.connect(3,4,1);
        graph.connect(4,5,1);
        graph.connect(5,10,1);

        graph.connect(1,6,1);
        graph.connect(6,7,1);
        graph.connect(7,8,1);
        graph.connect(8,9,1);
        graph.connect(9,10,2);

        check = 6;
        double ret = algo.shortestPathDist(1,10);
        assertTrue(check == ret);
        graph.removeNode(5);
        assertTrue(check == ret);
    }

    @Test
    void shortestPath() {
        for(int i = 1; i<=10; i++) {
            graph.addNode(i);;
        }
        graph.connect(1,2,1);
        graph.connect(2,3, 1);
        graph.connect(3,4,1);
        graph.connect(4,5,1);
        graph.connect(5,10,1);

        graph.connect(1,6,10);
        graph.connect(6,7,20);
        graph.connect(7,8,30);
        graph.connect(8,9,40);
        graph.connect(9,10,50);

        LinkedList<node_info> list = new LinkedList<>();
        for(int i = 1; i<=5; i++ ) {
            node_info temp = graph.getNode(i);
            list.add(temp);
        }
        node_info last = graph.getNode(10);
        list.add(last);
        assertEquals(list,algo.shortestPath(1,10));
        list.pollLast();
        assertNotEquals(list,algo.shortestPath(1,10));
    }

    @Test
    void save() {
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.connect(1,2,5);
        graph.connect(1,3,10);

        String fileName = "new_graph.bin";
        try {
            algo.save(fileName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void load() {
        try {
            save();
            weighted_graph_algorithms algo2 = new WGraph_Algo();
            algo2.load("new_graph.bin");
            assertNotSame(algo,algo2);
            assertEquals(algo,algo2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}