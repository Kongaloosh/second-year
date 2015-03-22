
package closestPair;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class TestSort
{

    private static final Integer[] intReversed         = { 10, 9, 8, 7, 6, 5,
            4, 3, 2, 1, 0, -1, -2, -3, -4, -5, -6, -7, -8, -9, -10 };
    private static final Integer[] intSorted           = { -10, -9, -8, -7, -6,
            -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    private static final Integer[] intNull             = null;
    private static final Integer[] intZeroElement      = {};
    private static final Integer[] intOneElement       = { 1 };
    private static final Integer[] intTwoElement       = { 1, -1 };

    private static final PointSet  pointSetNull        = null;
    private static final PointSet  pointSetZeroElement = new PointSet();
    private static final PointSet  pointSetOneElement  = new PointSet(
                                                               new Point[] { new Point(
                                                                       0, 0) });
    private static final PointSet  pointSetTwoElement  = new PointSet(
                                                               new Point[] {
            new Point(-1, -1), new Point(1, 1)                });
    private static final PointSet  pointSetFiveElement = new PointSet(
                                                               new Point[] {
            new Point(0, 0), new Point(10, 10), new Point(-5, -5),
            new Point(-100, 100), new Point(10, 20)           });
    private static final PointSet  pointSetSixElement  = new PointSet(
                                                               new Point[] {
            new Point(0, 0), new Point(10, 10), new Point(-5, -5),
            new Point(-100, 100), new Point(4, 7), new Point(10, 20) });
    
    @Test
    public void testIntReversed()
    { 
        testIsNotSorted(intReversed);
        testSort(intReversed);
    }

    @Test
    public void testIntSorted()
    {
        testIsSorted(intSorted);
        testSort(intSorted);
    }

    @Test
    public void testIntNull()
    {
        boolean catched = false;

        try
        { testSort(intNull); }
        catch (NullPointerException e)
        {
            catched = true;
        }

        assertTrue(catched);
    }

    @Test
    public void testIntZeroElement()
    { testSort(intZeroElement); }

    @Test
    public void testIntOneElement()
    { testSort(intOneElement); }

    @Test
    public void testIntTwoElement()
    { testSort(intTwoElement); }

    @Test
    public void testIntStressLight()
    {
        int N = 1;
        for (int i = 0; i < 20; i++)
        {
            Integer[] a = generateInts(N);
            testSort(a, N);
            N *= 2;
        }
    }

    @Test
    public void testIntStressMaxIntValue()
    {
        try
        {
            int N = Integer.MAX_VALUE;
            Integer[] a = generateInts(N);
            testSort(a, N);
        }
        catch (OutOfMemoryError e)
        { /* Pass if fail due to out of heap memory error */}
    }

    @Test
    public void testIntStressBeyondMaxIntValue()
    {
        boolean catched = false;
        try
        {
            int N = Integer.MAX_VALUE + 666;
            Integer[] a = generateInts(N);
            testSort(a, N);
        }
        catch (NegativeArraySizeException e)
        {
            catched = true;
        }
        assertTrue(catched);
    }

    @Test
    public void testPointNull()
    {
        boolean catched = false;

        try
        {
            testSort(pointSetNull.getPoints(), pointSetNull.size());
        }
        catch (NullPointerException e)
        {
            catched = true;
        }

        assertTrue(catched);
    }

    @Test
    public void testPointZeroElement()
    {
        testSort(pointSetZeroElement.getPoints(),
                pointSetZeroElement.size());
    }

    @Test
    public void testPointOneElement()
    {
        testSort(pointSetOneElement.getPoints(), pointSetOneElement.size());
    }

    @Test
    public void testPointTwoElement()
    {
        testSort(pointSetTwoElement.getPoints(), pointSetTwoElement.size());
    }

    @Test
    public void testPointFiveElement()
    {
        testSort(pointSetFiveElement.getPoints(),
                pointSetFiveElement.size());
    }

    @Test
    public void testPointSixElement()
    {
        /*
         * testIsSorted(pointSetSixElement.getPoints(),
         * pointSetSixElement.size());
         */
        pointSetSixElement.sort();
        testSort(pointSetSixElement.getPoints(), pointSetSixElement.size());
    }
    
    @Test
    public void testSortPartiallyInitialized()
    {
        Integer[] ints = new Integer[12];
        int hi = 6;
        
        for (int i = 0; i < hi; i++)
        { ints[i] = i * i; }
        
        testSort(ints, hi);
    }
    
    @Test
    public void testStress()
    {
        int N = 1000000;
        Integer[] a = new Integer[N];
        
        for (int i = 0; i < N; i++)
        { a[i] = (i % 2 == 1 ? -1 : 1)* (7 * i) / 3; }
        
        testIsNotSorted(a);
        testSort(a);
    }
    
    @Test
    public void testStressPartiallyInitialized()
    {
        Integer N = 1000000;
        int elements = N / 2; 
        Integer[] a = new Integer[N];
        
        for (int i = 0; i < elements; i++)
        { a[i] = (i % 2 == 1 ? -1 : 1)* (7 * i) / 3; }
     
        testIsNotSorted(a, elements);
        testSort(a, elements);
    }

    @Test
    public void testNullPointer()
    {
        Point[] nullArray = null;
        boolean catched = false;
        
        try
        { Sort.sort(nullArray); }
        catch (NullPointerException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        catched = false;
        try
        { Sort.sort(nullArray, 10); }
        catch (NullPointerException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
    }
    
    @Test
    public void testNegativeElementsArgument()
    {
        boolean catched = false;
        
        try
        { Sort.sort(pointSetSixElement.getPoints(), -1); }
        catch(NegativeArraySizeException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        catched = false;
        try
        { Sort.sort(pointSetSixElement.getPoints(), 0); }
        catch(NegativeArraySizeException e)
        { catched = true; }
        finally
        { assertFalse(catched); }
        
        catched = false;
        try
        { Sort.sort(pointSetSixElement.getPoints(), 1); }
        catch(NegativeArraySizeException e)
        { catched = true; }
        finally
        { assertFalse(catched); }
    }
    
    private static <T extends Comparable<T>> void testSort(T[] a)
    {
        int N = a.length;
        
        Sort.sort(a);
        assertTrue("Expected sorted array, but found:\n" + arrayToString(a, N),
                testIsSorted(a, N));
    }
    
    private static <T extends Comparable<T>> void testSort(T[] a, int N)
    {
        Sort.sort(a, N);
        assertTrue("Expected sorted array, but found:\n" + arrayToString(a, N),
                testIsSorted(a, N));
    }

    // TODO N is length
    private static <T extends Comparable<T>> String arrayToString(T[] a, int N)
    {
        StringBuilder sb = new StringBuilder();

        if (N == 0) { return "{}"; }

        sb.append("{");
        for (int i = 0; i < N - 1; i++)
        {
            sb.append(a[i].toString() + ", ");
        }
        sb.append(a[N - 1] + "}");

        return sb.toString();
    }

    private static <T extends Comparable<T>> boolean testIsNotSorted(T[] a)
    { return testIsNotSorted(a, a.length); }
    
    private static <T extends Comparable<T>> boolean testIsNotSorted(T[] a, int N)
    { return !testIsSorted(a, N); }
    
    private static <T extends Comparable<T>> boolean testIsSorted(T[] a)
    { return testIsSorted(a, a.length); }
    
    private static <T extends Comparable<T>> boolean testIsSorted(T[] a, int N)
    {
        if (N <= 1) { return true; }

        // Has two or more elements
        for (int i = 1; i < N; i++)
        {
            if (a[i - 1].compareTo(a[i]) == 1)
            { return false; }
        }
        return true;
    }

    private static Integer[] generateInts(int samples)
    {
        Integer[] a = new Integer[samples];
        Random rand = new Random();
        for (int i = 0; i < samples; i++)
        {
            a[i] = rand.nextInt();
        }
        return a;
    }

}
