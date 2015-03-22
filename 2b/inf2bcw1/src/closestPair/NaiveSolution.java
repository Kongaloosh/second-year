
package closestPair;

/**
 * O(n^2) brute-force solution
 */

public class NaiveSolution implements ISolution
{

    public int closestPair(PointSet pointSet) 
            throws InsufficientNumberOfPointsException
    {
        if (pointSet == null)
        { 
            throw new NullPointerException("PointSet parameter was null, but "
                + "expected to be initialized.");
        }
        
        if (pointSet.size() < 2)
        { 
            throw new InsufficientNumberOfPointsException("Expected at least 2 "
                    + "points in pointSet, but " + pointSet.size() + " found."); 
        }
        
        int min = Integer.MAX_VALUE;
        int N = pointSet.size();

        for (int i = 0; i < N; i++)
        {
            for (int j = i + 1; j < N; j++)
            {
                min = Math.min(min, pointSet.get(i).getSquareDistance(
                    pointSet.get(j)));
            }
        }

        return min;
    }

    @Override
    public String toString()
    { return "Naive"; }
    
    public String getEquation()
    { return "n^2"; }

}
