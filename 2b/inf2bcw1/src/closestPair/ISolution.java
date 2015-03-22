
package closestPair;

public interface ISolution
{

    public int closestPair(PointSet pointSet)
            throws InsufficientNumberOfPointsException,
                   NullPointerException;
    
    public String getEquation();
    
    @Override
    public String toString();
}
