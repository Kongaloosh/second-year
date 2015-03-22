/**
 * @author S1210313
 */

package closestPair;

import java.util.*;

import evaluation.Tuple;

/** The student's implementation of Shamos's Algorithm. */
public class StudentSolution implements ISolution
{
    /* Reference to naive solution to be used in Shamos' Algorithm */
    private ISolution naive = new NaiveSolution();

    /**
     * Find the square distance of the closest pairs in the [PointSet]. This
     * method function is the preparation step for the recursive part of the
     * algorithm defined in the method closestPairAux.
     * 
     * You should implement the following exceptions:
     * @throws InsufficientNumberOfPointsException 
     * @throws NullPointerException 
     */
    public int closestPair(PointSet p) throws NullPointerException, InsufficientNumberOfPointsException {
    	
    	if (p.size() <= 3) { // given the size is less than or equal to three, return the naive solution
			NaiveSolution naiveSolution = new NaiveSolution();
			return naiveSolution.closestPair(p);
		}else {
			PointSet sortedByX = p.clone();
			sortedByX.sort();
			PointSet sortedByY = p.clone();
			sortedByY.sortByY();
			
			int w = 0;
			int x = (int) Math.ceil((double)sortedByX.size()/2);
			int z = sortedByX.size();
			PointSet YL = new PointSet();
			PointSet YR = new PointSet();

			Tuple<PointSet, PointSet> splitTuple = splitY(sortedByX.get(x-1), sortedByY);			
			YL = splitTuple.first;
			YR = splitTuple.second;
			
			int dl = closestPairAux(sortedByX.subset(w,x), YL);
			int dr = closestPairAux(sortedByX.subset((x),z), YR);
		
			int d;
			int dtimes2;
			if (dl<dr){
				d = dl;
				dtimes2 = 2*dl;
			}else{
				d = dr;
				dtimes2 = 2*dr;
			}
			
			ArrayList<Point> yV = new ArrayList<Point>(); // Yv: the points within the 2d strip.
			int lineIndex = (int) Math.ceil((sortedByX.size()+1)/2); // find the point for the centre line
			int verticalLineX = sortedByX.get(lineIndex).getX(); // find the x-coord of the point 
			for (int i = 0; i < sortedByY.size(); i++) { // must iterate over the sorted points
				Point currentPoint = sortedByX.get(i); //
				if (currentPoint.getX()-verticalLineX <= dtimes2) {
					yV.add(currentPoint);
				}
			}
		
			for (int i = 0; i < yV.size()-1; i++) { // does not check last point
				for (int j = i+1; j <= i+7; j++) {
					if (j < yV.size()) {
						Point currentIPoint = yV.get(i);
						Point currentJPoint = yV.get(j);
						int xDist = (currentIPoint.getX() - currentJPoint.getX());
						int yDist = (currentIPoint.getY() - currentJPoint.getY());
						int distanceSquared = (int) (Math.pow(xDist, 2) + Math.pow(yDist, 2));
						
						if (distanceSquared < d) {
							d = distanceSquared;
						}
					}
				}
			}
			return d;
		}
    }
    
    /** Recursive part of the Shamos Algorithm 
     * 
     * You should implement the following exceptions:
     * @throws InsufficientNumberOfPointsException
     * @throws NullPointerException
     */
    
    private int closestPairAux(PointSet X, PointSet Y) throws InsufficientNumberOfPointsException, NullPointerException {
    	if (X.size() <= 3) { // given the size is less than or equal to three, return the naive solution
    		NaiveSolution naiveSolution = new NaiveSolution();
    		return naiveSolution.closestPair(X);
    	}else {
    		PointSet sortedByX = X.clone();
    		sortedByX.sort();
    		PointSet sortedByY = Y.clone();
    		sortedByY.sortByY();

    		int w = 0;
    		int x = (int) Math.ceil((double)sortedByX.size()/2);
    		int y = sortedByX.size();
    		PointSet YL = new PointSet();
    		PointSet YR = new PointSet();

			Tuple<PointSet, PointSet> splitTuple = splitY(sortedByX.get(x-1), sortedByY);			
			YL = splitTuple.first;
			YR = splitTuple.second;
			
			int dl = closestPairAux(sortedByX.subset(w,x), YL);
			int dr = closestPairAux(sortedByX.subset((x),y), YR);

			int d;
			int dtimes2;
			if (dl<dr){
				d = dl;
				dtimes2 = 2*dl;
			}else{
				d = dr;
				dtimes2 = 2*dr; // have to worry about it being squared?
			}
			
			ArrayList<Point> yV = new ArrayList<Point>(); // Yv: the points within the 2d strip.
			int lineIndex = (int) Math.ceil((sortedByX.size()+1)/2); // find the point for the centre line
			int verticalLineX = sortedByX.get(lineIndex).getX(); // find the x-coord of the point 
			for (int i = 0; i < sortedByY.size(); i++) { // must iterate over the sorted points
				Point currentPoint = sortedByX.get(i); //
				if (currentPoint.getX()-verticalLineX <= dtimes2) {
					yV.add(currentPoint);
				}
			}
		
			for (int i = 0; i < yV.size()-1; i++) { // does not check last point
				for (int j = i+1; j <= i+7; j++) {
					if (j < yV.size()) {
						Point currentIPoint = yV.get(i);
						Point currentJPoint = yV.get(j);
						int xDist = (currentIPoint.getX() - currentJPoint.getX());
						int yDist = (currentIPoint.getY() - currentJPoint.getY());
						int distanceSquared = (int) (Math.pow(xDist, 2) + Math.pow(yDist, 2));
						
						if (distanceSquared < d) {
							d = distanceSquared;
						}
					}
				}
			}
			return d;
		}
    }
    
    /* Implement your splitY method here as outlined in the handout fig. 3 
     * Note that you must pass references to initialised PointSet YL, YR. */
    
    private Tuple<PointSet, PointSet> splitY(Point testPoint, PointSet Y) {
        int N = Y.size();
        int left = 0;
        int right = 0;
        Point[] YL = new Point[(N + 1)/ 2];
        Point[] YR = new Point[N / 2];
        
        try
        {
            for (int i = 0; i < Y.size(); i++)
            {
                Point y = Y.get(i);
                if (y.compareTo(testPoint) <= 0)
                { YL[left++] = y; }
                else
                { YR[right++] = y; }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Expected testPoint to split Y into two halves"
                    + " of size " + (N + 1)/ 2 + " and " + N / 2
                    + " respectively but did not.");
            e.printStackTrace();
        }
        
        PointSet YLPS = new PointSet(YL, 0, YL.length, true);
        PointSet YRPS = new PointSet(YR, 0, YR.length, true);
        
        return new Tuple<PointSet, PointSet>(YLPS, YRPS);
    }
    
    /** The name of the solution */
    public String toString()
    { return "Student"; }

    /** Theoretical model of the runtime of the algorithm */
    public String getEquation()
    { return "n * lg(n)"; }
    
    /** You can use this method to test your solution */
    public static void main(String[] args)
    {
    	
        // You initialise student solution like below
        StudentSolution student = new StudentSolution();
        Point[] ps = {
        		new Point(1, 1), new Point(1, 2),
        		new Point(2, 1), new Point(2, 2),
        		new Point(3, 1), new Point(3, 2),
        		new Point(4, 1), new Point(4, 2)
        		};
        PointSet p = new PointSet(ps);
        try {
			System.out.print(student.closestPair(p));
		} catch (NullPointerException e) {
			System.out.println("null pointer");
			e.printStackTrace();
		} catch (InsufficientNumberOfPointsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}