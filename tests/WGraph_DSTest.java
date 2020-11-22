package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {

    static Random _rnd = null;
    weighted_graph graph;
    weighted_graph graph2;

    @BeforeEach
    void setup() {
        graph = new WGraph_DS();
        graph2 = new WGraph_DS();
    }

    @Test
    void hasEdge() {
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);

        graph.connect(1,3,10);
        assertFalse(graph.hasEdge(1,2));
        assertTrue(graph.hasEdge(1,3));

        graph.removeEdge(1, 3);
        graph.connect(1,2,15);
        assertTrue(graph.hasEdge(1,2));
        assertFalse(graph.hasEdge(1,3));
    }

    @Test
    void connect() {
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);

        graph.connect(1,3,10);
        assertFalse(graph.hasEdge(1,2));
        assertTrue(graph.hasEdge(1,3));

        graph.removeEdge(1, 3);
        graph.connect(1,2,15);
        assertTrue(graph.hasEdge(1,2));
        assertFalse(graph.hasEdge(1,3));
    }

    @Test
    void getV() {
        assertNull(graph);
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);

        graph.connect(1,2,10);
        graph.connect(1,3,20);

        Collection<node_info> g = graph.getV();
        Iterator<node_info> runner = g.iterator();
        while(runner.hasNext()) {
            node_info check = runner.next();
            assertNotNull(check);
        }
    }


    @Test
    void removeNode() {
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);

        assertTrue(graph.getV(1) != null);
        graph.removeNode(1);
        assertFalse(graph.getV(1) != null);
        assertNull(graph.removeNode(1));
    }

    @Test
    void removeEdge() {
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);

        graph.connect(1,3,100);
        assertTrue(graph.hasEdge(1,3));
        graph.removeEdge(1,3);
        assertFalse(graph.hasEdge(1,3));
    }

    @Test
    void nodeSize() {
        int size = 10;
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        graph.addNode(5);
        graph2.addNode(6);
        graph2.addNode(7);
        graph2.addNode(8);
        graph2.addNode(9);
        graph2.addNode(10);

        assertTrue(graph.nodeSize() == graph2.nodeSize());
        graph.removeNode(5);
        assertFalse(graph.nodeSize() == graph2.nodeSize());
    }

    @Test
    void edgeSize() {
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.connect(1,3,10);
        graph.connect(1,2,15);

        graph2.addNode(4);
        graph2.addNode(5);
        graph2.addNode(6);
        graph2.connect(4,5,20);
        graph2.connect(4,6,25);

        assertEquals(graph.edgeSize(),graph2.edgeSize());
        graph.removeEdge(1,3);
        assertNotEquals(graph.edgeSize(),graph2.edgeSize());
    }

}