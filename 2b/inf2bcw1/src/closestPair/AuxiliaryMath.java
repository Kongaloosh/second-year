package closestPair;

import java.util.Collection;
import java.util.Iterator;

public class AuxiliaryMath
{
    private static Number boundary(Collection<? extends Number> c, int TYPE) 
            throws EmptyCollectionException
    {
        Number extremum;
        Iterator<? extends Number> iter = c.iterator();
        
        if (c.size() == 0)
        { throw new EmptyCollectionException(); }
        else
        { extremum = iter.next(); }
        
        while (iter.hasNext())
        {
            double value = iter.next().doubleValue();
            if (Double.compare(value, extremum.doubleValue()) == TYPE)
            { extremum = value; }
        }
        
        return extremum;
    }
    
    public static Number max(Collection<? extends Number> c) 
            throws EmptyCollectionException
    { return boundary(c, 1); }
    
    public static Number min(Collection<? extends Number> c) 
            throws EmptyCollectionException
    { return boundary(c, -1); }
    
    public static double average(Collection<? extends Number> c)
            throws EmptyCollectionException
    {
        double sum = 0;
        int N = c.size();
        Iterator<? extends Number> iter = c.iterator();
        
        if (N == 0)
        {
            throw new EmptyCollectionException("Expected a collection to have"
                    + " at least one element to, but found the array to be"
                    + " empty. Computing the average would cause division by 0"
                    + " error.");
        }
        
        while (iter.hasNext())
        { sum += iter.next().doubleValue(); }
        
        return (double) sum / (double) N;
    }

}
