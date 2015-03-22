package evaluation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import closestPair.InsufficientNumberOfPointsException;
import closestPair.NaiveSolution;
import closestPair.PointSet;
import closestPair.StudentSolution;

public class TestTimerSuite
{
    private TimerSuite emptyTimerSuite;
    private TimerSuite timerSuite;
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
    {
        emptyTimerSuite = new TimerSuite();
        
        timerSuite = new TimerSuite();
        timerSuite.add(new NaiveSolution());
        timerSuite.add(new StudentSolution());
    }
    
    @Test
    public void testAdd()
    {
        assertEquals(0, emptyTimerSuite.getTimers().size());
        
        emptyTimerSuite.add(new NaiveSolution());
        emptyTimerSuite.add(new StudentSolution());
        assertEquals(2, timerSuite.getTimers().size());
        
        emptyTimerSuite.add(new NaiveSolution());
        assertEquals(2, emptyTimerSuite.getTimers().size());
    }
    
    @Test
    public void testTest() 
            throws NullPointerException,
                   InsufficientNumberOfPointsException,
                   InsufficientNumberOfTrialsException
    {
        boolean catched = false;
        try
        { timerSuite.test(new PointSet()); }
        catch (InsufficientNumberOfPointsException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        timerSuite.test(problem0);
        timerSuite.test(problem5);
        timerSuite.test(problem1);
        timerSuite.test(problem2);
        timerSuite.test(problem2);
        timerSuite.test(problem2);
        timerSuite.test(problem3);
        timerSuite.test(problem4);
        timerSuite.test(problem5);
        timerSuite.test(problem5);
        timerSuite.test(problem5);
        timerSuite.test(problem5);
        timerSuite.test(problem5, 10);
        timerSuite.test(problem5);
        timerSuite.test(problem6);
        timerSuite.test(problem7);
        timerSuite.test(problem7, 101);
        timerSuite.test(problem8);
        timerSuite.test(problem5);
        timerSuite.test(problem9);
        
        catched = false;
        try { timerSuite.test(problem5, -1); }
        catch (InsufficientNumberOfTrialsException e) { catched = true; }
        finally { assertTrue(catched); }
        
        catched = false;
        try { timerSuite.test(problem5, 0); }
        catch (InsufficientNumberOfTrialsException e) { catched = true; }
        finally { assertTrue(catched); }
        
        for (Timer timer : timerSuite.getTimers())
        { assertEquals(10, timer.size()); }
    }
    
    @Test
    public void testGetTimers()
    {
        assertEquals(0, emptyTimerSuite.getTimers().size());
        assertEquals(2, timerSuite.getTimers().size());
    }
    
}
