
package evaluation;

import static org.junit.Assert.*;

import org.junit.Test;

import closestPair.PointSet;

public class TestTuple
{
    @Test
    public void test()
    {
        Tuple<PointSet, Double> tuple;
        PointSet first = new PointSet();
        Double second = 1e-9;
        tuple = new Tuple<PointSet, Double>(first, second);
        assertEquals(first, tuple.first);
        assertEquals(second, tuple.second);
    }
    
}
