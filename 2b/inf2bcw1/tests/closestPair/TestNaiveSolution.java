
package closestPair;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

// TODO add stress test
// TODO add performance test
// TODO what are other types of test

public class TestNaiveSolution
{

    private NaiveSolution         solution;

    private static final PointSet nullPointSet = null;
    private static final PointSet zeroElement  = new PointSet();
    private static final PointSet oneElement   = new PointSet(
            new Point[] { new Point(0, 0) });
    private static final PointSet twoElement   = new PointSet(new Point[] {
            new Point(-1, -1), new Point(1, 1) });
    private static final PointSet fiveElement  = new PointSet(new Point[] {
            new Point(0, 0), new Point(10, 10), new Point(-5, -5),
            new Point(-100, 100), new Point(10, 20) });
    private static final PointSet sixElement   = new PointSet(new Point[] {
            new Point(0, 0), new Point(10, 10), new Point(-5, -5),
            new Point(-100, 100), new Point(4, 7), new Point(10, 20) });

    // TODO add some generated and saved from random

    @Before
    public void setUp()
    { solution = new NaiveSolution(); }

    @Test
    public void nullPointSet()
    {
        boolean catched = false;

        try
        { solution.closestPair(nullPointSet); }
        catch (Exception e)
        { catched = true; }

        assertTrue(catched);
    }

    @Test
    public void zeroElement()
    {
        boolean catched = false;

        try
        { solution.closestPair(zeroElement); }
        catch (Exception e)
        { catched = true; }

        assertTrue(catched);
    }

    @Test
    public void oneElement()
    {
        boolean catched = false;

        try
        { solution.closestPair(oneElement); }
        catch (Exception e)
        { catched = true; }

        assertTrue(catched);
    }

    @Test
    public void testTwoElement() throws InsufficientNumberOfPointsException
    {
        int expected = twoElement.get(0).getSquareDistance(
                twoElement.get(1));
        int actual = solution.closestPair(twoElement);
        assertEquals(expected, actual);
    }

    @Test
    public void testFiveElement() throws InsufficientNumberOfPointsException
    {
        int expected = (new Point(0, 0)).getSquareDistance(new Point(-5, -5));
        int actual = solution.closestPair(fiveElement);
        assertEquals(expected, actual);
    }

    @Test
    public void testSixElement() throws InsufficientNumberOfPointsException
    {
        int expected = (new Point(10, 10)).getSquareDistance(new Point(4, 7));
        int actual = solution.closestPair(sixElement);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testStress() 
            throws ReadonlyException, InsufficientNumberOfPointsException
    {
        Point p0 = new Point(0, 0), p1 = new Point(0, 1);
        int expected = p0.getSquareDistance(p1);
        
        PointSet problem = new PointSet();
        for (int x = 0; x < 9999; x = (x * 7 - 3) * (int) Math.pow(-1, x % 2))
        {
            for (int y = 5; y < 9999; y = (y * 7 - 3) * (int) Math.pow(-1, y % 2))
            { problem.add(new Point(x, y)); }
        }
        problem.add(p0);
        problem.add(p1);
        
        assertEquals(expected, new NaiveSolution().closestPair(problem));
    }
    
    @Test
    public void testToStringReturnsNaiveSolutionName()
    {
        assertEquals("Naive", solution.toString());
    }

}
