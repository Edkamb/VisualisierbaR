package com.github.bachelorpraktikum.visualisierbar.model.train;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.github.bachelorpraktikum.visualisierbar.model.Context;
import com.github.bachelorpraktikum.visualisierbar.model.Coordinates;
import com.github.bachelorpraktikum.visualisierbar.model.Edge;
import com.github.bachelorpraktikum.visualisierbar.model.Event;
import com.github.bachelorpraktikum.visualisierbar.model.FactoryTest;
import com.github.bachelorpraktikum.visualisierbar.model.Node;
import com.github.bachelorpraktikum.visualisierbar.model.train.Train.Position;
import com.github.bachelorpraktikum.visualisierbar.model.train.Train.TrainFactory;
import javax.annotation.Nonnull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TrainTest extends FactoryTest<Train> {

    private Context context;
    private int counter = 0;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void init() {
        context = new Context();
    }

    @Test
    public void testCreateNegativeLength() {
        expected.expect(IllegalArgumentException.class);
        getFactory(context).create("train", "t", -1);
    }

    @Test
    public void testCreateZeroLength() {
        expected.expect(IllegalArgumentException.class);
        getFactory(context).create("train", "t", 0);
    }

    @Override
    protected TrainFactory getFactory(Context context) {
        return Train.in(context);
    }

    @Override
    protected Train createRandom(Context context) {
        int count = this.counter++;
        return getFactory(context).create("train" + count, "t" + count, 10);
    }

    @Override
    protected Train createSame(Context context, Train train) {
        return getFactory(context)
            .create(train.getName(), train.getReadableName(), train.getLength());
    }

    @Override
    public void testCreateDifferentArg(Context context, Train train, int argIndex) {
        switch (argIndex) {
            case 1:
                getFactory(context).create(
                    train.getName(),
                    train.getReadableName() + "invalid",
                    train.getLength()
                );
                break;
            case 2:
                getFactory(context).create(
                    train.getName(),
                    train.getReadableName(),
                    train.getLength() + 1
                );
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private Edge[] createEdges(Integer edgeLength, Integer... edgeLengths) {
        Edge[] result = new Edge[edgeLengths.length + 1];
        Node startNode = Node.in(context).create("startNode", new Coordinates(0, 0));
        Node previousNode = Node.in(context).create("n0", new Coordinates(0, 1));
        result[0] = Edge.in(context).create("e0", edgeLength, startNode, previousNode);

        for (int i = 1; i <= edgeLengths.length; i++) {
            Node node = Node.in(context).create("n" + i, new Coordinates(i, 1));
            result[i] = Edge.in(context).create("e" + i, edgeLengths[i - 1], previousNode, node);
            previousNode = node;
        }

        return result;
    }

    @Test
    public void testName() {
        Train train = Train.in(context).create("t", "train", 100);
        assertEquals("t", train.getName());
    }

    @Test
    public void testReadableName() {
        Train train = Train.in(context).create("t", "train", 100);
        assertEquals("train", train.getReadableName());
    }

    @Test
    public void testLength() {
        Train train = Train.in(context).create("t", "train", 50);
        assertEquals(50, train.getLength());
    }

    @Test
    public void testColor() {
        Train train = createRandom(context);
        assertNotNull(train.getColor());
    }

    @Test
    public void testGetStateNegativeTimeBeforeInit() {
        Train train = Train.in(context).create("t", "train", 20);
        Edge edge = createEdges(30)[0];
        train.eventFactory().init(0, edge);

        expected.expect(IllegalArgumentException.class);
        train.getState(Context.INIT_STATE_TIME - 1);
    }

    @Test
    public void testGetStateNotInitialized() {
        Train train = Train.in(context).create("t", "train", 20);
        assertFalse(train.getState(0).isInitialized());
        assertFalse(train.getState(100).isInitialized());

        expected.expect(IllegalStateException.class);
        train.getState(0).getPosition();
    }

    @Test
    public void testInitStart() {
        Train train = Train.in(context).create("t", "train", 20);
        Edge edge = createEdges(30)[0];
        train.eventFactory().init(0, edge);

        Train.State state = train.getState(0);
        assertEquals(train.getLength(), state.getPosition().getFrontDistance());
        assertEquals(edge.getLength(), state.getPosition().getBackDistance());
        assertEquals(edge, state.getPosition().getFrontEdge());
        assertEquals(edge, state.getPosition().getBackEdge());
        assertEquals(0, state.getTotalDistance());
        assertTrue(state.isInitialized());
    }

    @Test
    public void testInitMiddle() {
        Train train = Train.in(context).create("t", "train", 20);
        Edge edge2 = createEdges(30, 40, 50)[1];
        train.eventFactory().init(5, edge2);

        Train.State state = train.getState(5);
        assertEquals(train.getLength(), state.getPosition().getFrontDistance());
        assertEquals(edge2, state.getPosition().getFrontEdge());
        assertEquals(edge2.getLength(), state.getPosition().getBackDistance());
        assertEquals(0, state.getTotalDistance());
        assertTrue(state.isInitialized());

        String description = train.getEvents().get(1).getDescription().toLowerCase();
        assertTrue(description.contains("init"));
        assertTrue(description.contains(String.valueOf(5)));
        assertTrue(description.contains(edge2.getName().toLowerCase()));
    }

    @Test
    public void testSpeed() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        train.eventFactory().init(0, edge);

        Train.State initState = train.getState(0);

        int time = 10000;
        int distance = 20;
        int speed = 2;
        train.eventFactory().speed(time, distance, speed);

        assertNotEquals(initState, train.getState(0));

        Train.State state = train.getState(time);
        assertFalse(state.isTerminated());
        assertEquals(train, state.getTrain());
        assertEquals(time, state.getTime());
        assertEquals(edge, state.getPosition().getFrontEdge());
        assertEquals(train.getLength() + distance, state.getPosition().getFrontDistance());
        assertEquals(speed, state.getSpeed(), 0.5);
        assertEquals(distance, state.getTotalDistance());
        assertTrue(state.isInitialized());

        assertEquals(state, train.getState(time * 2));

        String description = train.getEvents().get(2).getDescription().toLowerCase();
        assertTrue(description.contains("speed"));
        assertTrue(description.contains(String.valueOf(time)));
        assertTrue(description.contains(String.valueOf(distance)));
        assertTrue(description.contains(String.valueOf(speed)));
    }

    @Test
    public void testMove() {
        Train train = createRandom(context);
        Edge edge = createEdges(50)[0];
        train.eventFactory().init(0, edge);

        Train.State initState = train.getState(0);

        int time = 10000;
        int distance = 20;
        train.eventFactory().move(time, distance);

        assertNotEquals(initState, train.getState(0));

        Train.State state = train.getState(time);
        assertFalse(state.isTerminated());
        assertEquals(train, state.getTrain());
        assertEquals(time, state.getTime());
        assertEquals(edge, state.getPosition().getFrontEdge());
        assertEquals(train.getLength() + distance, state.getPosition().getFrontDistance());
        assertEquals(distance, state.getTotalDistance());
        assertTrue(state.isInitialized());

        assertEquals(state, train.getState(time * 2));

        String description = train.getEvents().get(2).getDescription().toLowerCase();
        assertTrue(description.contains("speed"));
        assertTrue(description.contains(String.valueOf(time)));
        assertTrue(description.contains(String.valueOf(distance)));
    }

    @Test
    public void testReach() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge[] edges = createEdges(20, 40);
        train.eventFactory().init(0, edges[0]);

        train.eventFactory().reach(10, edges[1], 10);
        Train.State state = train.getState(10);
        assertEquals(10, state.getTime());
        assertEquals(0, state.getPosition().getFrontDistance());
        assertEquals(edges[1], state.getPosition().getFrontEdge());
        assertEquals(10, state.getTotalDistance());

        assertEquals(edges[0], state.getPosition().getBackEdge());
        assertEquals(10, state.getPosition().getBackDistance());
        assertFalse(state.isTerminated());

        String description = train.getEvents().get(2).getDescription().toLowerCase();
        assertTrue(description.contains("reach"));
        assertTrue(description.contains(String.valueOf(10)));
        assertTrue(description.contains(String.valueOf(10)));
        assertTrue(description.contains(edges[1].getName()));
    }

    @Test
    public void testLeave() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge[] edges = createEdges(20, 40);
        train.eventFactory().init(0, edges[0]);

        train.eventFactory().reach(10, edges[1], 10);
        train.eventFactory().leave(20, edges[1], 10);
        Train.State state = train.getState(20);
        assertEquals(20, state.getTime());
        assertEquals(edges[1].getLength(), state.getPosition().getBackDistance());
        assertEquals(edges[1], state.getPosition().getBackEdge());

        assertEquals(edges[1], state.getPosition().getFrontEdge());
        assertEquals(10, state.getPosition().getFrontDistance());
        assertEquals(20, state.getTotalDistance());
        assertFalse(state.isTerminated());

        String description = train.getEvents().get(3).getDescription().toLowerCase();
        assertTrue(description.contains("leave"));
        assertTrue(description.contains(String.valueOf(20)));
        assertTrue(description.contains(String.valueOf(10)));
        assertTrue(description.contains(edges[1].getName()));
    }

    @Test
    public void testTerminate() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(40)[0];
        train.eventFactory().init(0, edge);

        train.eventFactory().speed(5000, 4, 1);
        train.eventFactory().terminate(10000, 4);
        Train.State state = train.getState(10000);
        assertEquals(10000, state.getTime());
        assertTrue(state.isTerminated());
        assertEquals(18, state.getPosition().getFrontDistance());
        assertEquals(8, state.getTotalDistance());
        assertEquals(0.8, state.getSpeed(), 0.01);

        String description = train.getEvents().get(3).getDescription().toLowerCase();
        assertTrue(description.contains("terminate"));
        assertTrue(description.contains(String.valueOf(10000)));
    }

    @Test
    public void testInterpolatableStateEqualsBogusState() {
        Train train = createRandom(context);
        Train.State state = train.getState(0);

        assertFalse(state.equals(new Train.State() {
            @Nonnull
            @Override
            public Train getTrain() {
                return state.getTrain();
            }

            @Override
            public int getTime() {
                return state.getTime();
            }

            @Override
            public boolean isTerminated() {
                return state.isTerminated();
            }

            @Override
            public boolean isInitialized() {
                return state.isInitialized();
            }

            @Override
            public double getSpeed() {
                return state.getSpeed();
            }

            @Nonnull
            @Override
            public Position getPosition() {
                return null;
            }

            @Override
            public int getTotalDistance() {
                return state.getTotalDistance();
            }
        }));
    }

    @Test
    public void testInterpolationTimeTooSmall() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        train.eventFactory().init(5, edge);
        train.eventFactory().speed(10, 10, 10);

        InterpolatableState init = (InterpolatableState) train.getState(5);
        InterpolatableState speed = (InterpolatableState) train.getState(10);

        expected.expect(IllegalArgumentException.class);
        init.interpolate(2, speed);
    }

    @Test
    public void testInterpolationTimeTooBig() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        train.eventFactory().init(5, edge);
        train.eventFactory().speed(10, 10, 10);

        InterpolatableState init = (InterpolatableState) train.getState(5);
        InterpolatableState speed = (InterpolatableState) train.getState(10);

        expected.expect(IllegalArgumentException.class);
        init.interpolate(100, speed);
    }

    @Test
    public void testInterpolationWrongStartState() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        train.eventFactory().init(5, edge);
        train.eventFactory().speed(10, 10, 10);

        InterpolatableState init = (InterpolatableState) train.getState(5);
        InterpolatableState speed = (InterpolatableState) train.getState(10);

        assertEquals(init.interpolate(8, speed), speed.interpolate(8, init));
    }

    @Test
    public void testInterpolationUninitialized() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        train.eventFactory().init(5, edge);

        Train.State start = train.getState(Context.INIT_STATE_TIME);
        Train.State state = train.getState(2);
        assertEquals(start.isInitialized(), state.isInitialized());
        assertEquals(start.isTerminated(), state.isTerminated());
        assertEquals(start.getSpeed(), state.getSpeed(), 0.0001);
        assertEquals(start.getTrain(), state.getTrain());
        assertEquals(start.getTotalDistance(), state.getTotalDistance());
    }

    @Test
    public void testSpeedInterpolation() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        train.eventFactory().init(0, edge);

        train.eventFactory().speed(20000, 5, 0);

        assertEquals(0.25, train.getState(12000).getSpeed(), 0.01);
        assertEquals(0.25, train.getState(15000).getSpeed(), 0.01);
        assertEquals(0.25, train.getState(17000).getSpeed(), 0.01);
    }

    @Test
    public void testSpeedInterpolationBeforeFirstReach() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge[] edges = createEdges(20, 50);
        train.eventFactory().init(0, edges[0]);

        train.eventFactory().speed(5000, 5, 1);
        train.eventFactory().reach(10000, edges[1], 5);

        assertEquals(1, train.getState(0).getSpeed(), 0.1);
        assertEquals(1, train.getState(9).getSpeed(), 0.1);
    }

    @Test
    public void testPositionInterpolation() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        train.eventFactory().init(0, edge);

        train.eventFactory().speed(10, 10, 5);

        assertEquals(11, train.getState(1).getPosition().getFrontDistance());
        assertEquals(13, train.getState(3).getPosition().getFrontDistance());
        assertEquals(15, train.getState(5).getPosition().getFrontDistance());
    }

    @Test
    public void testDistanceInterpolation() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        train.eventFactory().init(0, edge);

        train.eventFactory().speed(10, 10, 10);
        train.eventFactory().speed(20, 10, 20);

        assertEquals(10, train.getState(10).getTotalDistance());
        assertEquals(20, train.getState(20).getTotalDistance());
        assertEquals(12, train.getState(12).getTotalDistance());
        assertEquals(15, train.getState(15).getTotalDistance());
        assertEquals(18, train.getState(18).getTotalDistance());
    }

    @Test
    public void testMergeEvents() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge[] edges = createEdges(110, 20);
        train.eventFactory().init(0, edges[0]);

        train.eventFactory().reach(10000, edges[1], 100);
        train.eventFactory().speed(10000, 0, 10);

        Train.State state = train.getState(10000);
        assertEquals(0, state.getPosition().getFrontDistance());
        assertEquals(10, state.getSpeed(), 0.05);
        assertEquals(100, state.getTotalDistance());
    }

    @Test
    public void testGetStateWithStart() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge[] edges = createEdges(20, 20);
        train.eventFactory().init(0, edges[0]);

        train.eventFactory().reach(10, edges[1], 10);
        train.eventFactory().speed(20, 5, 10);

        Train.State wanted = train.getState(20);

        Train.State state = train.getState(15);
        assertEquals(wanted, train.getState(20, state));
    }

    @Test
    public void testGetStateWithStartAfterWanted() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge[] edges = createEdges(20, 20);
        train.eventFactory().init(0, edges[0]);

        train.eventFactory().reach(10, edges[1], 10);
        train.eventFactory().speed(20, 5, 10);

        Train.State wanted = train.getState(15);

        Train.State state = train.getState(20);
        assertEquals(wanted, train.getState(15, state));
    }

    @Test
    public void testGetStateWithStartNull() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge[] edges = createEdges(20);
        train.eventFactory().init(0, edges[0]);

        Train.State state = train.getState(10, null);
        Train.State expected = train.getState(10);
        assertEquals(expected, state);
    }

    @Test
    public void testGetStateWithStartWrongTrain() {
        Train train = Train.in(context).create("t", "train", 10);
        Train train2 = Train.in(context).create("t2", "train2", 20);
        Edge[] edges = createEdges(20, 20);

        train.eventFactory().init(0, edges[0]);
        train2.eventFactory().init(0, edges[0]);

        Train.State state = train2.getState(5);
        expected.expect(IllegalArgumentException.class);
        train.getState(10, state);
    }

    @Test
    public void testGetStateWithBogusStateImplementation() {
        Train train = createRandom(context);
        Edge[] edges = createEdges(20, 20);

        train.eventFactory().init(0, edges[0]);
        train.eventFactory().reach(5, edges[1], 10);

        Train.State state = train.getState(10, new Train.State() {
            @Nonnull
            @Override
            public Train getTrain() {
                return train;
            }

            @Override
            public int getTime() {
                return 0;
            }

            @Override
            public boolean isTerminated() {
                return false;
            }

            @Override
            public boolean isInitialized() {
                return false;
            }

            @Override
            public double getSpeed() {
                return 0;
            }

            @Nonnull
            @Override
            public Position getPosition() {
                return null;
            }

            @Override
            public int getTotalDistance() {
                return 0;
            }
        });

        assertEquals(train.getState(10), state);
    }

    @Test
    public void testInitAlreadyInitialized() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge[] edges = createEdges(20, 20);
        train.eventFactory().init(0, edges[0]);
        expected.expect(IllegalStateException.class);
        train.eventFactory().init(0, edges[1]);
    }

    @Test
    public void testInitNegativeTime() {
        Train train = createRandom(context);
        Edge[] edges = createEdges(20);
        train.eventFactory().init(-5, edges[0]);
        assertFalse(train.getEvents().get(1).getWarnings().isEmpty());
    }

    @Test
    public void testSpeedNotInitialized() {
        Train train = Train.in(context).create("t", "train", 10);
        expected.expect(IllegalStateException.class);
        train.eventFactory().speed(10, 10, 10);
    }

    @Test
    public void testReachNotInitialized() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        expected.expect(IllegalStateException.class);
        train.eventFactory().reach(10, edge, 10);
    }

    @Test
    public void testLeaveNotInitialized() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        expected.expect(IllegalStateException.class);
        train.eventFactory().leave(10, edge, 10);
    }

    @Test
    public void testTerminateNotInitialized() {
        Train train = Train.in(context).create("t", "train", 10);
        expected.expect(IllegalStateException.class);
        train.eventFactory().terminate(10, 10);
    }

    @Test
    public void testSpeedAlreadyTerminated() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        train.eventFactory().init(0, edge);
        train.eventFactory().terminate(5, 0);
        train.eventFactory().speed(10, 10, 10);
        assertFalse(train.getEvents().get(train.getEvents().size() - 1).getWarnings().isEmpty());
    }

    @Test
    public void testReachAlreadyTerminated() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        train.eventFactory().init(0, edge);
        train.eventFactory().terminate(5, 0);
        train.eventFactory().reach(10, edge, 10);
        assertFalse(train.getEvents().get(train.getEvents().size() - 1).getWarnings().isEmpty());
    }

    @Test
    public void testLeaveAlreadyTerminated() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        train.eventFactory().init(0, edge);
        train.eventFactory().terminate(5, 0);
        train.eventFactory().leave(10, edge, 10);
        assertFalse(train.getEvents().get(train.getEvents().size() - 1).getWarnings().isEmpty());
    }

    @Test
    public void testTerminateAlreadyTerminated() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        train.eventFactory().init(0, edge);
        train.eventFactory().terminate(5, 0);
        train.eventFactory().terminate(10, 10);
        assertFalse(train.getEvents().get(train.getEvents().size() - 1).getWarnings().isEmpty());
    }

    @Test
    public void testEventBeforeLast() {
        Train train = Train.in(context).create("t", "train", 10);
        Edge edge = createEdges(50)[0];
        train.eventFactory().init(0, edge);
        train.eventFactory().speed(10, 10, 20);
        Event beforeEvent = train.getEvents().get(train.getEvents().size() - 1);
        train.eventFactory().speed(5, 10, 10);
        Event newEvent = train.getEvents().get(train.getEvents().size() - 1);
        assertTrue(beforeEvent != newEvent);

        assertTrue(beforeEvent.getWarnings().isEmpty());
        assertEquals(10, beforeEvent.getTime());

        assertFalse(newEvent.getWarnings().isEmpty());
        assertEquals(10, newEvent.getTime());
        assertEquals(beforeEvent.getTime(), newEvent.getTime());
    }

    @Test
    public void testToString() {
        Train train = createRandom(context);
        assertNotNull(train.toString());
        assertTrue(train.toString().contains(String.valueOf(train.getLength())));
        assertTrue(train.toString().contains(train.getName()));
        assertTrue(train.toString().contains(train.getReadableName()));
    }
}
