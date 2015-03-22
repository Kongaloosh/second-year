
package evaluation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import closestPair.EmptyCollectionException;
import closestPair.InsufficientNumberOfPointsException;
import closestPair.NaiveSolution;
import closestPair.PointSet;

public class TestTimer
{

    private Timer timer;
    private PointSet problem0 = new PointSet("data/test_0.txt"),
                     problem1 = new PointSet("data/test_1.txt"),
                     problem2 = new PointSet("data/test_2.txt"),
                     problem3 = new PointSet("data/test_3.txt"),
                     problem4 = new PointSet("data/test_4.txt"),
                     problem5 = new PointSet("data/test_5.txt"),
                     problem6 = new PointSet("data/test_6.txt"),
                     problem7 = new PointSet("data/test_7.txt"),
                     problem8 = new PointSet("data/test_8.txt"),
                     problem9 = new PointSet("data/test_9.txt");

    @Before
    public void setup()
    { timer = new Timer(new NaiveSolution()); }

    @Test
    public void testTest() 
            throws NullPointerException,
                   InsufficientNumberOfPointsException,
                   InsufficientNumberOfTrialsException
    {
        boolean catched = false;
        try
        { timer.test(new PointSet()); }
        catch (InsufficientNumberOfPointsException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        timer.test(problem0);
        assertTrue(timer.getExecutionTime(problem0) > 0);
        timer.test(problem0, 1);
        timer.test(problem0, 100);
        timer.test(problem0, 1);
        assertTrue(timer.getExecutionTime(problem0) > 0);
        timer.test(problem1, 1);
        timer.test(problem3, 100);
        timer.test(problem5, 1);
        assertTrue(timer.getExecutionTime(problem1) > 0);
        
        catched = false;
        try { timer.test(problem0, 0); }
        catch (InsufficientNumberOfTrialsException e) { catched = true; }
        finally { assertTrue(catched); }
        
        catched = false;
        try { timer.test(problem0, -9); }
        catch (InsufficientNumberOfTrialsException e) { catched = true; }
        finally { assertTrue(catched); }
        
        catched = false;
        try { timer.test(problem2, 0); }
        catch (InsufficientNumberOfTrialsException e) { catched = true; }
        finally { assertTrue(catched); }
        
        catched = false;
        try { timer.test(problem4, -9); }
        catch (InsufficientNumberOfTrialsException e) { catched = true; }
        finally { assertTrue(catched); }
        
        catched = false;
        try
        { timer.getExecutionTime(problem2); }
        catch (NullPointerException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        assertTrue(timer.getExecutionTime(problem3) > 0);
        
        catched = false;
        try
        { timer.getExecutionTime(problem4); }
        catch (NullPointerException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        assertTrue(timer.getExecutionTime(problem5) > 0);
    }
    
    @Test
    public void testRatios() 
            throws NullPointerException,
                   InsufficientNumberOfPointsException,
                   InsufficientNumberOfTrialsException
    {
        timer.test(problem0);
        double expected = (double) timer.getExecutionTime(problem0) / 
                (double) (problem0.size() * problem0.size());
        double actual = timer.getRatio(problem0);
        double eps = 1e-9;
        assertTrue(expected - eps <= actual && actual <= expected + eps);
        
    }
    
    @Test
    public void testAverageExecutionTime() 
            throws NullPointerException,
                   InsufficientNumberOfPointsException,
                   EmptyCollectionException,
                   InsufficientNumberOfTrialsException
    {
        boolean catched = false;
        try
        { timer.getWorstExecutionTime(); }
        catch (EmptyCollectionException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        timer.test(problem0);
        timer.test(problem1);
        timer.test(problem2);
        timer.test(problem3);
        timer.test(problem4);
        
        double expected = 0.0;
        for (PointSet problem : timer.getSortedPointSets())
        { expected += (double) timer.getExecutionTime(problem); }
        expected /= 5.0;
        
        double actual = timer.getAverageExecutionTime();
        assertTrue(expected == actual);
    }
    
    @Test
    public void testWorstExecutionTime() 
            throws NullPointerException,
                   InsufficientNumberOfPointsException, 
                   EmptyCollectionException,
                   InsufficientNumberOfTrialsException
    {
        boolean catched = false;
        try
        { timer.getWorstExecutionTime(); }
        catch (EmptyCollectionException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        timer.test(problem0);
        timer.test(problem1);
        timer.test(problem2);
        timer.test(problem3);
        timer.test(problem4);
        timer.test(problem5);
        timer.test(problem6);
        timer.test(problem7);
        timer.test(problem8);
        timer.test(problem9);
        
        long expected = 0;
        for (PointSet problem : timer.getSortedPointSets())
        { expected = Math.max(expected, timer.getExecutionTime(problem)); }
        
        assertEquals(expected, timer.getWorstExecutionTime());
    }
    
    @Test
    public void testGetSolutionName()
    { assertEquals((new NaiveSolution()).toString(), timer.getSolutionName()); }
    
    @Test
    public void testGetSortedPointSets()
            throws NullPointerException,
                   InsufficientNumberOfPointsException,
                   InsufficientNumberOfTrialsException
    {
        timer.test(problem0);
        timer.test(problem1);
        timer.test(problem2);
        timer.test(problem3);
        timer.test(problem4);
        timer.test(problem5);
        timer.test(problem6);
        timer.test(problem7);
        timer.test(problem8);
        timer.test(problem9);
        
        int previousSize = 0;
        for (PointSet problem : timer.getSortedPointSets())
        {
            assertTrue(previousSize <= problem.size());
            previousSize = problem.size();
        }
    }
    
    @Test
    public void testSize() 
            throws NullPointerException,
                   InsufficientNumberOfPointsException,
                   InsufficientNumberOfTrialsException
    {
        assertEquals(timer.size(), 0);
        
        timer.test(problem0);
        assertEquals(timer.size(), 1);
        
        timer.test(problem0);
        assertEquals(timer.size(), 1);
        
        timer.test(problem0);
        assertEquals(timer.size(), 1);
        
        timer.test(problem1);
        assertEquals(timer.size(), 2);
        
        timer.test(problem1);
        assertEquals(timer.size(), 2);
        
        timer.test(problem1);
        timer.test(problem2);
        timer.test(problem3);
        timer.test(problem4);
        timer.test(problem5);
        timer.test(problem6);
        timer.test(problem7);
        timer.test(problem8);
        timer.test(problem9);
        assertEquals(timer.size(), 10);
    }
    
    @Test
    public void testToPlot() 
            throws NullPointerException,
                   InsufficientNumberOfPointsException,
                   InsufficientNumberOfTrialsException
    {
        timer.test(problem1);
        timer.test(problem2);
        timer.test(problem3);
        timer.test(problem4);
        timer.test(problem5);
        timer.test(problem6);
        timer.test(problem7);
        timer.test(problem8);
        timer.test(problem9);
        
        double[][] xy = timer.toPlot();
        
        assertEquals(2, xy.length);
        assertEquals(xy[0].length, xy[1].length);
    }
    
    @Test
    public void testGetAverageSolutionRatio() 
            throws NullPointerException,
                   InsufficientNumberOfPointsException, 
                   EmptyCollectionException,
                   InsufficientNumberOfTrialsException
    {
        boolean catched = false;
        try
        { timer.getAverageRatio(); }
        catch (EmptyCollectionException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        timer.test(problem0);
        timer.test(problem1);
        timer.test(problem2);
        timer.test(problem3);
        timer.test(problem4);
        timer.test(problem5);
        timer.test(problem6);
        timer.test(problem7);
        timer.test(problem8);
        timer.test(problem9);
        
        double expected = 0.0;
        for (PointSet problem : timer.getSortedPointSets())
        { expected += timer.getRatio(problem); }
        expected /= 10.0;
        
        double actual = timer.getAverageRatio();
        
        double eps = 1e-9;
        assertTrue(expected - eps <= actual && actual <= expected + eps);
    }
    
    @Test
    public void testGetWorstRatio() 
            throws NullPointerException,
                   InsufficientNumberOfPointsException, 
                   EmptyCollectionException,
                   InsufficientNumberOfTrialsException
    {
        boolean catched = false;
        try
        { timer.getWorstRatio(); }
        catch (EmptyCollectionException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        timer.test(problem0);
        timer.test(problem1);
        timer.test(problem2);
        timer.test(problem3);
        timer.test(problem4);
        timer.test(problem5);
        timer.test(problem6);
        timer.test(problem7);
        timer.test(problem8);
        timer.test(problem9);
        
        double expected = 0.0;
        
        for (PointSet problem : timer.getSortedPointSets())
        { expected = Math.max(expected, timer.getRatio(problem)); }
        
        double actual = timer.getWorstRatio();
        
        assertTrue(expected == actual);
    }
    
}
