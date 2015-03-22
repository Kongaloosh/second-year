package closestPair;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import static closestPair.AuxiliaryMath.*;

public class TestAuxiliaryMath
{
    
    private ArrayList<Integer> intList;
    private ArrayList<Double> doubleList;
    
    @Before
    public void setup()
    {
        intList = new ArrayList<Integer>();
        doubleList = new ArrayList<Double>();
    }
  

    @Test
    public void testMaxEmpty()
    {
        boolean exceptionRaised = false;
        
        try
        { max(intList); }
        catch (EmptyCollectionException e)
        { exceptionRaised = true; }
        finally
        { assertTrue(exceptionRaised); }
        
        exceptionRaised = false;
        try
        { max(doubleList); }
        catch (EmptyCollectionException e)
        { exceptionRaised = true; }
        finally
        { assertTrue(exceptionRaised); }
    }
    
    @Test
    public void testMinEmpty()
    {
        boolean exceptionRaised = false;
        
        try
        { min(intList); }
        catch (EmptyCollectionException e)
        { exceptionRaised = true; }
        finally
        { assertTrue(exceptionRaised); }
        
        exceptionRaised = false;
        try
        { min(intList); }
        catch (EmptyCollectionException e)
        { exceptionRaised = true; }
        finally
        { assertTrue(exceptionRaised); }
    }
    
    @Test
    public void testMaxOneElement() throws EmptyCollectionException
    {
        intList.add(0);
        assertEquals(0, max(intList));
        
        doubleList.add(1.0/3.0);
        assertEquals(1.0/3.0, max(doubleList));
    }
    
    @Test
    public void testMinOneElement() throws EmptyCollectionException
    {
        intList.add(0);
        assertEquals(0, min(intList));
        
        doubleList.add(1.0/3.0);
        assertEquals(1.0/3.0, min(doubleList));
    }
    
    @Test
    public void testMaxOddNumberOfElements() throws EmptyCollectionException
    {
        int expected = 89;
    
        for (int i = 1; i <= expected; i += 2)
        { intList.add(i); }
        
        assertTrue(intList.size() % 2 == 1);
        
        assertEquals(expected, max(intList).intValue());
    }
    
    @Test
    public void testMaxEvenNumberOfElements() throws EmptyCollectionException
    {
        int expected = 91;
    
        for (int i = 1; i <= expected; i += 2)
        { intList.add(i); }
        
        assertTrue(intList.size() % 2 == 0);
        
        assertEquals(expected, max(intList).intValue());
    }
    
    @Test
    public void testMinOddNumberOfElements() throws EmptyCollectionException
    {
        int max = 89;
        int expected = 1;
    
        for (int i = expected; i <= max; i += 2)
        { intList.add(i); }
        
        assertTrue(intList.size() % 2 == 1);
        
        assertEquals(expected, min(intList));
    }
    
    @Test
    public void testMinEvenNumberOfElements() throws EmptyCollectionException
    {
        int max = 91;
        int expected = 1;
    
        for (int i = expected; i <= max; i += 2)
        { intList.add(i); }
        
        assertTrue(intList.size() % 2 == 0);
        
        assertEquals(expected, min(intList));
    }
    
    @Test
    public void testMaxExtremeValue() throws EmptyCollectionException
    {
        int expected = Integer.MAX_VALUE;
        
        for (int k = 7; k < 3000; k = (k * 7 - 3) *  (int) Math.pow(-1, k % 2))
        { intList.add(k); }
        
        intList.add(expected);
        
        assertEquals(expected, max(intList).intValue());
    }
    
    @Test
    public void testMinExtremeValue() throws EmptyCollectionException
    {
        int expected = Integer.MIN_VALUE;
        
        for (int k = 7; k < 3000; k = (k * 7 - 3) * (int) Math.pow(-1, k % 2))
        { intList.add(k); }
        
        intList.add(expected);
        
        assertEquals(expected, min(intList).intValue());
    }
    
    @Test
    public void testMaxStess() throws EmptyCollectionException
    {
        int expected = 1000000;
        
        for (int i = 0; i <= expected; i++)
        { intList.add(i); }
        
        assertEquals(expected, max(intList).intValue());
    }
    
    @Test 
    public void testMinStress() throws EmptyCollectionException
    {
        int expected = 0;
        
        for (int i = 1000000; i >= expected; i--)
        { intList.add(i); }
        
        assertEquals(expected, min(intList).intValue());
    }
    
    @Test
    public void testAverage() throws EmptyCollectionException
    {
        boolean catched = false;
        try
        { average(intList); }
        catch (EmptyCollectionException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        double epsilon = 1.e-9;
        double expected = 50.5;
        
        for (int i = 1; i <= 100; i++)
        { intList.add(i); }
        
        assertTrue(withinEpsilon(expected, average(intList), epsilon));
        
        for (double d = 1.0; d <= 100.0; d += 1.0)
        { doubleList.add(d); }
        assertTrue(withinEpsilon(expected, average(intList), epsilon));
    }
    
    private boolean withinEpsilon(double expected, double actual, double epsilon)
    { return expected - epsilon < actual && actual < expected + epsilon; }
    
}
