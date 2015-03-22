
package closestPair;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestPointSet
{

    private static final PointSet pointSetZeroElement  = new PointSet();
    private static final PointSet pointSetOneElement   = new PointSet(
            new Point[] { new Point(0, 0) });
    private static final PointSet pointSetTwoElement   = new PointSet(new Point[] {
            new Point(-1, -1), new Point(1, 1) });
    private static final PointSet pointSetFiveElement  = new PointSet(new Point[] {
            new Point(0, 0), new Point(10, 10), new Point(-5, -5),
            new Point(-100, 100), new Point(10, 20) });
    private static final PointSet pointSetSixElement   = new PointSet(new Point[] {
            new Point(0, 0), new Point(10, 10), new Point(-5, -5),
            new Point(-100, 100), new Point(4, 7), new Point(10, 20) });
    private static final int bigPointSetSize = 1000, 
                             hugePointSetSize = 10000,
                             enormousPointSetSize = 100000;
    private static final PointSet bigPointSet = new PointSet();
    private static final PointSet hugePointSet = new PointSet();
    private static final PointSet enormousPointSet = new PointSet();
    
    @BeforeClass
    public static void prepareSets() throws ReadonlyException
    {
        for (int i = 0; i < bigPointSetSize; i++)
        {
            int x = (i % 2 == 1 ? 1 : -1) * i * 7 / 3;
            int y = (i % 5 < 2 ? 1 : -1) * i * 13 / 2;
            bigPointSet.add(new Point(x, y));
            hugePointSet.add(new Point(x, y));
            enormousPointSet.add(new Point(x, y));
        }
        
        for (int i = bigPointSetSize; i < hugePointSetSize; i++)
        {
            int x = (i % 2 == 1 ? 1 : -1) * i * 7 / 3;
            int y = (i % 5 < 2 ? 1 : -1) * i * 13 / 2;
            hugePointSet.add(new Point(x, y));
            enormousPointSet.add(new Point(x, y));
        }
        
        for (int i = hugePointSetSize; i < enormousPointSetSize; i++)
        {
            int x = (i % 2 == 1 ? 1 : -1) * i * 7 / 3;
            int y = (i % 5 < 2 ? 1 : -1) * i * 13 / 2;
            enormousPointSet.add(new Point(x, y));
        }
    }
    
    @Test
    public void testSize()
    {
        assertEquals(0, pointSetZeroElement.size());
        assertEquals(1, pointSetOneElement.size());
        assertEquals(2, pointSetTwoElement.size());
        assertEquals(5, pointSetFiveElement.size());
        assertEquals(6, pointSetSixElement.size());
        assertEquals(bigPointSetSize, bigPointSet.size());
        assertEquals(hugePointSetSize, hugePointSet.size());
        assertEquals(enormousPointSetSize, enormousPointSet.size());
    }
    
    @Test
    public void testSubset()
    {
        boolean catched;
        
        // unable to create subset
        catched = false;
        try
        { bigPointSet.subset(-1, 1); }
        catch (ArrayIndexOutOfBoundsException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        catched = false;
        try
        { bigPointSet.subset(5, bigPointSetSize + 1); }
        catch (ArrayIndexOutOfBoundsException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        catched = false;
        try
        { bigPointSet.subset(-1, bigPointSetSize + 1); }
        catch (ArrayIndexOutOfBoundsException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        // successfully create subset
        int lo = 895, hi = 1311;
        PointSet subset = hugePointSet.subset(lo, hi);
        
        assertEquals(hi - lo, subset.size());
        
        // assert that is read-only
        catched = false;
        
        try
        { subset.add(new Point(-123, 123)); }
        
        catch (ReadonlyException e)
        { catched = true; }
        
        finally
        { assertTrue(catched); }
        
        for (int i = lo; i < hi; i++)
        { assertEquals(hugePointSet.get(i), subset.get(i - lo)); }
        
        int N = bigPointSet.size();
        int median = N / 2;
        PointSet left = bigPointSet.subset(0, median);
        PointSet right = bigPointSet.subset(median, N);
        
        // test for uniqueness (PointSet has all points unique, so two disjoint
        // subset should have all points different)
        int repetitions = 0;
        
        for (Point pl : left.getPoints())
        { 
            if (right.contains(pl))
            { repetitions ++; }
        }
        
        assertEquals("Expected no repetitions between 2 disjoint sets, but "
                + "found " + repetitions, 0, repetitions);
        
        repetitions = 0;
        left.sort();
        right.sort();
        
        for (Point pl : left.getPoints())
        { 
            if (right.contains(pl))
            { repetitions ++; }
        }
        
        assertEquals("Expected no repetitions between 2 disjoint sets, but "
                + "found " + repetitions, 0, repetitions);
        
        // intersetion of 10 points
        left = bigPointSet.subset(0, median + 5);
        right = bigPointSet.subset(median - 5, N);
        
        repetitions = 0;
        for (Point pl : left.getPoints())
        { 
            if (right.contains(pl))
            { repetitions ++; }
        }
        
        assertEquals("Expected exactly 10 repetitions between two subsets, but"
                + " found " + repetitions, 10, repetitions);
        
        // intersection of 5 points points
        left = bigPointSet.subset(10, 100);
        left = left.subset(10, 50);
        left = left.subset(10, 30);
        assertEquals(20, left.size());
        
        right = bigPointSet.subset(30, 200);
        right = right.subset(10, 100);
        right = right.subset(5, 50);
        right = right.subset(0, 20);
        assertEquals(20, right.size());
        
        repetitions = 0;
        for (Point pl : left.getPoints())
        { 
            if (right.contains(pl))
            { repetitions ++; }
        }
        
        assertEquals("Expected exactly 5 repetitions between two subsets, but"
                + " found " + repetitions, 5, repetitions);
    }
    
    @Test
    public void testClone()
    {
        PointSet clone = bigPointSet.clone();
        
        // TODO: How to request physical address explicitly
        assertFalse(clone.equals(bigPointSet));
        
        for (int i = 0; i < bigPointSetSize; i++)
        { assertEquals(bigPointSet.get(i), clone.get(i)); }
        
        // clone part, 100-200
        int lo = 100, hi = 200;
        clone = bigPointSet.clone(lo, hi);
        for (int i = lo; i < hi; i++)
        { assertEquals(bigPointSet.get(i), clone.get(i - lo)); }
        
        // clone part few times, end up with 450 - 550
        clone = bigPointSet.clone(300, 700);
        clone = clone.clone(50, 350);
        clone = clone.clone(100, 200);
        for (int i = 450; i < 550; i++)
        { assertEquals(bigPointSet.get(i), clone.get(i - 450)); }
    }
    
    @Test
    public void testAdd() throws ReadonlyException
    {
        PointSet pointSet = new PointSet();
        Point point = new Point(0, 0);
        
        for (int i = 0; i < 1000; i++)
        { pointSet.add(point); }
        
        assertEquals(1, pointSet.size());
        
        int expected = bigPointSet.size();
        bigPointSet.add(bigPointSet.get(101));
        int actual = bigPointSet.size();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGet()
    {
        boolean catched = false;
        
        try
        { bigPointSet.get(bigPointSetSize + 1); }
        catch (ArrayIndexOutOfBoundsException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
        
        int i = 0;
        Point point = pointSetTwoElement.get(i);
        int x = point.getX();
        int y = point.getY();
        for (int k = 0; k < 100; k++)
        { assertEquals(new Point(x, y), pointSetTwoElement.get(i)); }
    }
    
    @Test
    public void testComparison()
    {
        ArrayList<PointSet> sets = new ArrayList<PointSet>();
        sets.add(pointSetZeroElement);
        sets.add(pointSetOneElement);
        sets.add(pointSetTwoElement);
        sets.add(pointSetFiveElement);
        sets.add(pointSetSixElement);
        sets.add(bigPointSet);
        sets.add(hugePointSet);
        sets.add(enormousPointSet);
        
        for (int i = 0; i < sets.size(); i++)
        {
            assertEquals(0, sets.get(i).compareTo(sets.get(i)));
            for (int j = i + 1; j < sets.size(); j++)
            {
                assertEquals(-1, sets.get(i).compareTo(sets.get(j)));
                assertTrue(sets.get(i).less(sets.get(j)));
                assertEquals(1, sets.get(j).compareTo(sets.get(i)));
                assertFalse(sets.get(j).less(sets.get(i)));
            }
        }
    }
    
    @Test
    public void testSaveAndRead() throws ReadonlyException
    {
        String file = "tests/files/test.txt";
        
        PointSet save = new PointSet(10000, -5000, 5000);
        save.save(file);
        PointSet read = new PointSet(file);
        
        assertEquals("Expected both PointSet from which dump was created and "
                + "the one to which they were read to have the same size.", 
                (long) save.size(), (long) read.size());
        
        for (int i = 0; i < save.size(); i++)
        { assertEquals(save.get(i), read.get(i));}
        
        // test cannot read into sealed PointSet (e.g. subset)
        PointSet subset = save.subset(500, 1000);
        boolean catched = false;
        try
        { subset.read(file); }
        catch (ReadonlyException e)
        { catched = true; }
        finally
        { assertTrue(catched); }
    }

}
