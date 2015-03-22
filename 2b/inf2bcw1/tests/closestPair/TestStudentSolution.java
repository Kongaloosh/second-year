// TODO: Implement

package closestPair;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestStudentSolution
{

    ISolution student, naive;

    @Before
    public void setUp()
    {
        student = new StudentSolution();
        naive = new NaiveSolution();
    }

    @Test
    public void testCompareWithNaiveSolution()
            throws NullPointerException, InsufficientNumberOfPointsException
    {
        
        for (int n = 10; n <= 10000; n *= 10)
        {
            for (int t = 0; t < 10; t++)
            {
                PointSet ps = new PointSet(n, -5000, 5000);
                int naiveDist = naive.closestPair(ps);
                int studentDist = student.closestPair(ps);
                assertEquals(naiveDist, studentDist);
            }
        }
    }

    @Test
    public void testSimpleProblems() 
            throws ReadonlyException, 
                   NullPointerException, 
                   InsufficientNumberOfPointsException
    {
        PointSet ps0 = new PointSet();
        ps0.add(new Point(0, 0));
        ps0.add(new Point(0, 1));
        assertEquals(1, student.closestPair(ps0));
        
        //
        PointSet ps1 = new PointSet();
        ps1.add(new Point(0, 0));
        ps1.add(new Point(-5, 5));
        ps1.add(new Point(24, 26));
        ps1.add(new Point(10, 10));
        ps1.add(new Point(25, 25));
        ps1.add(new Point(100, 100));
        ps1.add(new Point(-50, -70));
        assertEquals(2, student.closestPair(ps1));
        
        //
        PointSet ps2 = new PointSet();
        ps2.add(new Point(25, 25));
        ps2.add(new Point(0, 0));
        ps2.add(new Point(-5, 5));
        ps2.add(new Point(10, 10));
        ps2.add(new Point(100, 100));
        ps2.add(new Point(-50, -70));
        ps2.add(new Point(24, 26));
        assertEquals(2, student.closestPair(ps1));
    }

}
