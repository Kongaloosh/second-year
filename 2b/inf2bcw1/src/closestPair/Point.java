
package closestPair;

/**
 * Fixed point, instantiated at the time of creation. Comparable in first order
 * by x, then by y.
 */

public class Point implements Comparable<Point>
{

    private final int x, y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    { return x; }

    public int getY()
    { return y; }

    /**
     * Computes square distance from this to other point.
     * 
     * @param other - point to which distance is measured to from this one
     * @return Square value of distance between this and other point
     */
    public int getSquareDistance(Point other)
    {
        return (this.x - other.x) * (this.x - other.x) 
             + (this.y - other.y) * (this.y - other.y);
    }

    public boolean less(Point other)
    { return this.compareTo(other) == -1; }
    
    @Override
    public boolean equals(Object other)
    { return this.compareTo((Point) other) == 0; }

    @Override
    public String toString()
    { return "(" + x + ", " + y + ")"; }

    @Override
    public int compareTo(Point other)
    {
        if (this.x < other.getX())
        { return -1; }
        
        else if (this.x == other.x && this.y < other.y)
        { return -1; }

        else if (this.x == other.x && this.y == other.y)
        { return 0; }

        else
        { return 1; }
    }

}
