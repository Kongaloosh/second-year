
package closestPair;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestPoint
{

    @Test
    public void testCompareTo()
    {
        assertTrue((new Point(0, 0).compareTo(new Point(1, 1))) == -1);
        assertTrue((new Point(-1, -1).compareTo(new Point(1, 1))) == -1);
        assertTrue((new Point(-2, 100).compareTo(new Point(1, 1))) == -1);
        assertTrue((new Point(1, 0).compareTo(new Point(1, 1))) == -1);
        
        assertTrue((new Point(1, 1).compareTo(new Point(1, 1))) == 0);
        assertTrue((new Point(0, 1).compareTo(new Point(0, 1))) == 0);
        assertTrue((new Point(-1, 1).compareTo(new Point(-1, 1))) == 0);
        
        assertTrue((new Point(1, 1).compareTo(new Point(0, 0))) == 1);
        assertTrue((new Point(1, 1).compareTo(new Point(-1, -1))) == 1);
        assertTrue((new Point(1, 1).compareTo(new Point(-2, 100))) == 1);
        assertTrue((new Point(1, 1).compareTo(new Point(1, 0))) == 1);
    }
    
    @Test
    public void testEquals()
    {
        assertTrue((new Point(0, 0)).equals(new Point(0, 0)));
        assertTrue((new Point(-5, 2)).equals(new Point(-5, 2)));
        assertTrue((new Point(2, -5)).equals(new Point(2, -5)));
        
        assertFalse((new Point(0, 0)).equals(new Point(1, 0)));
        assertFalse((new Point(0, 0)).equals(new Point(0, 1)));
        assertFalse((new Point(2, -5)).equals(new Point(-2, -5)));
    }
    
    @Test
    public void testLess()
    {
        // Smaller on base of x
        for (int x = -100; x < 101; x++)
        {
            for (int xplus = 1; x < 100; x++)
            {
                Point point1 = new Point(x, 0);
                Point point2 = new Point(x + xplus, 0);
                assertTrue(point1.less(point2));
            }
        }
    }
    
    @Test
    public void testGetX()
    {
        int y = 100;
        for (int x = -10; x < 11; x++)
        {
            Point point = new Point(x, y);
            assertEquals(x, point.getX());
        }
    }

    @Test
    public void testGetY()
    {
        int x = 100;
        for (int y = -10; y < 11; y++)
        {
            Point point = new Point(x, y);
            assertEquals(y, point.getY());
        }
    }
    
    @Test
    public void testToString()
    {
        for (int x = -100; x < 101; x++)
        {
            for (int y = -100; y < 101; y++)
            {
                String expected = "(" + x + ", " + y + ")";
                Point point = new Point(x, y);
                assertEquals(expected, point.toString());
            }
        }
    }
    
    @Test 
    public void testGetSquareDistance()
    {
        for (int x1 = -100; x1 < 101; x1 += 5)
        for (int y1 = -100; y1 < 101; y1 += 7)
        for (int x2 = -300; x2 < 301; x2 += 23)
        for (int y2 = -300; y2 < 301; y2 += 31)
        {
            int expected = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
            Point point1 = new Point(x1, y1);
            Point point2 = new Point(x2, y2);
            assertEquals(expected, point1.getSquareDistance(point2));
        }
    }

}
