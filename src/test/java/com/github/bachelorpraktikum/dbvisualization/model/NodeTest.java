package com.github.bachelorpraktikum.dbvisualization.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class NodeTest {
    private Context context;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void init() {
        context = new Context();
    }

    @Test
    public void testInstanceManager() {
        Node node = Node.in(context).create("node", new Coordinates(0, 0));
        assertSame(node, Node.in(context).get(node.getName()));
        assertSame(node, Node.in(context).create(node.getName(), node.getCoordinates()));
        assertTrue(Node.in(context).getAll().contains(node));
    }

    @Test
    public void testInstanceManagerInvalidName() {
        expected.expect(IllegalArgumentException.class);
        Node.in(context).get("t");
    }

    @Test
    public void testInstanceManagerExistsDifferentCoordinates() {
        String name = "node";
        Node node = Node.in(context).create(name, new Coordinates(0, 0));
        expected.expect(IllegalArgumentException.class);
        Node.in(context).create(name, new Coordinates(0, 1));
    }

    @Test
    public void testName() {
        Node node = Node.in(context).create("node", new Coordinates(0, 0));
        assertEquals("node", node.getName());
    }

    @Test
    public void testCoordinates() {
        Coordinates coords = new Coordinates(0, 0);
        Node node = Node.in(context).create("node", coords);
        assertEquals(coords, node.getCoordinates());
    }

    @Test
    public void testToString() {
        Node node = Node.in(context).create("node", new Coordinates(0, 0));
        assertNotNull(node.toString());
        assertFalse(node.toString().trim().isEmpty());
    }

    @Test
    public void testNullCoordinates() {
        expected.expect(NullPointerException.class);
        Node.in(context).create("node", null);
    }

    @Test
    public void testNullName() {
        expected.expect(NullPointerException.class);
        Node.in(context).create(null, new Coordinates(0, 0));
    }

    @Test
    public void testAddEdge() {
        Node node1 = Node.in(context).create("node1", new Coordinates(0, 0));
        Node node2 = Node.in(context).create("node2", new Coordinates(1, 0));
        Edge edge = Edge.in(context).create("edge", 10, node1, node2);

        Node testNode = Node.in(context).create("testNode", new Coordinates(10, 0));
        testNode.addEdge(edge);
        assertTrue(testNode.getEdges().contains(edge));
    }

    @Test
    public void testAddElement() {
        Node node = Node.in(context).create("node", new Coordinates(0, 0));
        Element element = Element.in(context).create("element", Element.Type.HauptSignalImpl, node, Element.State.NOSIG);

        Node testNode = Node.in(context).create("testNode", new Coordinates(10, 0));
        testNode.addElement(element);
        assertTrue(testNode.getElements().contains(element));
    }

    @Test
    public void testOtherEdges() {
        Node node1 = Node.in(context).create("node1", new Coordinates(0, 0));
        Node node2 = Node.in(context).create("node2", new Coordinates(1, 0));
        Node node3 = Node.in(context).create("node3", new Coordinates(2, 1));

        Edge edge1 = Edge.in(context).create("edge1", 10, node1, node2);
        Edge edge2 = Edge.in(context).create("edge2", 10, node2, node3);
        Edge edge3 = Edge.in(context).create("edge3", 10, node1, node3);

        Collection<Edge> otherEdges = node1.otherEdges(edge1);
        assertEquals(1, otherEdges.size());
        assertFalse(otherEdges.contains(edge1));
        assertTrue(otherEdges.contains(edge3));
    }

    @Test
    public void testOtherEdgesOnlyEdge() {
        Node node1 = Node.in(context).create("node1", new Coordinates(0, 0));
        Node node2 = Node.in(context).create("node2", new Coordinates(1, 0));
        Edge edge = Edge.in(context).create("edge", 10, node1, node2);

        Collection<Edge> otherEdges = node1.otherEdges(edge);

        assertTrue(otherEdges.isEmpty());
    }

    @Test
    public void testOtherEdgesNull() {
        Node node1 = Node.in(context).create("node1", new Coordinates(0, 0));
        Node node2 = Node.in(context).create("node2", new Coordinates(1, 0));
        Edge edge = Edge.in(context).create("edge", 10, node1, node2);

        expected.expect(NullPointerException.class);
        node1.otherEdges(null);
    }
}
