
package evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import static closestPair.AuxiliaryMath.*;
import static closestPair.Sort.sort;
import closestPair.EmptyCollectionException;
import closestPair.ISolution;
import closestPair.InsufficientNumberOfPointsException;
import closestPair.PointSet;

public class Timer
{

    private ISolution               solution;
    // keeps track of worst execution times of closestPair for each PointSet
    private HashMap<PointSet, Long> stopwatch;
    private boolean firstRun = true;

    public Timer(ISolution solution)
    {
        this.solution = solution;
        this.stopwatch = new HashMap<PointSet, Long>();
    }

    public int test(PointSet problem) 
            throws NullPointerException, InsufficientNumberOfPointsException, 
            InsufficientNumberOfTrialsException
    { return test(problem, 10); }
    
    public int test(PointSet problem, int trials) 
            throws NullPointerException, InsufficientNumberOfPointsException,
            InsufficientNumberOfTrialsException
    {
        if (trials < 1)
        { 
            throw new InsufficientNumberOfTrialsException("Expected at least "
                + "1 trial to be performed, but " + Integer.toString(trials) +
                " was specified.");
        }
        
        long worst;
        
        if (stopwatch.containsKey(problem))
        { worst = stopwatch.get(problem); }
        else
        { worst = Long.MIN_VALUE; }
        
        Integer results[] = new Integer[trials];
        
        for (int i = 0; i < trials; i++)
        {
            Tuple<Long, Integer> current = measureExecutionTime(problem);
            results[i] = current.second;
            worst = Math.max(worst, current.first);
        }
        
        InconsistentCalculationsException.checkConsistency(results);
        
        stopwatch.put(problem, worst);
        return results[0];
    }
    
    /** @return Execution time in microseconds [us] */
    private Tuple<Long, Integer> measureExecutionTime(PointSet problem) 
            throws NullPointerException, InsufficientNumberOfPointsException, 
            InsufficientNumberOfTrialsException
    {
        // suggest garbage collection to avoid peaks in execution time
        System.gc();
        // smooths first run of measure time (removes effect of gc)
        if (firstRun)
        {
            firstRun = false;
            test(problem);
            stopwatch.remove(problem);
        }
        
        // actual measure
        long start = System.nanoTime();
        Integer result = solution.closestPair(problem);
        long elapsed = System.nanoTime() - start;
        
        return new Tuple<Long, Integer>(elapsed / 1000, result);
    }
    
    public double getRatio(PointSet problem) 
            throws NullPointerException,
                   InsufficientNumberOfPointsException,
                   InsufficientNumberOfTrialsException
    {
        Equation eq = new Equation(solution.getEquation());
        
        if (!stopwatch.containsKey(problem))
        { test(problem); }
        
        return (double) stopwatch.get(problem) / eq.y(problem.size());
    }

    public long getExecutionTime(PointSet problem)
    { return stopwatch.get(problem); }
    
    public double getAverageExecutionTime() 
            throws EmptyCollectionException
    { return average(stopwatch.values()); }
    
    public long getWorstExecutionTime() throws EmptyCollectionException
    { return max(stopwatch.values()).longValue(); }
    
    public double getWorstRatio() throws EmptyCollectionException
    {
        if (stopwatch.size() == 0)
        {
            throw new EmptyCollectionException("Expected at least one"
                    + " time measurement experiment in order to find the"
                    + " worst, but found 0 (nothing to return).");
        }
        
        Equation eq = new Equation(solution.getEquation());
        double worst = -1.0;
        
        for (PointSet p : stopwatch.keySet())
        {
            double ratio = (double) getExecutionTime(p) / eq.y(p.size());
            worst = Math.max(worst, ratio);
        }
        
        return worst;
    }

    public double getAverageRatio() throws EmptyCollectionException
    {
        if (stopwatch.size() == 0)
        {
            throw new EmptyCollectionException("Expected at least one"
                    + " time measurement experiment in order to compute the"
                    + " average, but found 0 (division by zero error).");
        }
        
        Equation eq = new Equation(solution.getEquation());
        double sum = 0.0;
        
        for (PointSet p : stopwatch.keySet())
        { sum += (double) getExecutionTime(p) / (double) eq.y(p.size()); }
        
        return sum / (double) stopwatch.size();
    }

    public String getSolutionName()
    { return solution.toString(); }
    
    public PointSet[] getSortedPointSets()
    { 
        PointSet[] pointSets = stopwatch.keySet().toArray(new PointSet[0]);
        Arrays.sort(pointSets);
        return pointSets;
    }

    public int size()
    { return stopwatch.size(); }
    
    public int width()
    {
        PointSet[] pointSets = getSortedPointSets();
        return pointSets[pointSets.length - 1].size() - pointSets[0].size();
    }
    
    /**
     * @return matrix composed of two vectors x and y,
     *         xy[0] -> x vector, xy[1] - y vector
     *         passed as argument to plotting method
     */
    public double[][] toPlot()
    {
        PlotPoint[] points = new PlotPoint[size()];
        int i = 0;
        
        for (PointSet problem : stopwatch.keySet())
        {
            int x = problem.size();
            int y = stopwatch.get(problem).intValue();
            
            points[i++] = new PlotPoint(x, y);
        }
        
        sort(points);
        
        double[][] xy = new double[2][points.length];
        for (int j = 0; j < points.length; j++)
        {
            xy[0][j] = points[j].getX();
            xy[1][j] = points[j].getY();
        }
        
        return xy;
    }
    
    /** Int may be not enough for time measurement in microseconds and overflow,
     * and surely it will for test set of size ~50,000 for Naive Solution
     * giving false result. Could be prettier, but what could not? */
    private class PlotPoint implements Comparable<PlotPoint>
    {
        private long x, y;
    
        public PlotPoint(long x, long y)
        { this.x = x; this.y = y; }
        
        public long getX()
        { return x; }
        
        public long getY()
        { return y; }
        
        public int compareTo(PlotPoint other)
        {
            if (this.x < other.x) { return -1; }
            else if (this.x == other.x && this.y < other.y) { return -1; }
            else if (this.x == other.x && this.y == other.y) { return 0; }
            else { return 1; }
        }
    }
    
    public double[][] interpolate(double c)
    {
        Equation eq = new Equation(solution.getEquation());
        double[][] xy = new double[2][stopwatch.size()];
        int i = 0;
        PointSet[] ps = getSortedPointSets();
        for (PointSet p : ps)
        {
            double x = p.size();
            xy[0][i] = x;
            xy[1][i] = c * eq.y(x);
            i++;
        }
        return xy;
    }
    
    public double[][] interpolateToMax(String eq)
            throws EmptyCollectionException
    { return interpolate(getWorstRatio()); }
    
    public double[][] interpolateToAverage(String eq) 
            throws EmptyCollectionException
    { return interpolate(getAverageRatio()); }
    
    private class Equation
    {
        ArrayList<Operation> eq = null;
        
        public Equation(String def)
        { // parse
            eq = new ArrayList<Operation>();
            def = def.replaceAll(" ", "");
            String[] ops = null;
            if (def.contains("+"))
            { ops = def.split("+"); }
            else
            { ops = new String[1]; ops[0] = def;}
            for (String op : ops)
            { eq.add(new Operation(op)); }
        }
        
        public double y(double x)
        {
            double y = 0.0;
            
            for (Operation op : eq)
            { 
                try { y += op.y(x); }
                catch (Exception e) { e.printStackTrace(); }
            }
            
            return y;
        }
        
        private class Operation
        {
            String op = null;
            
            public Operation(String op)
            { this.op = op.trim(); }
            
            public double y(double x) throws Exception
            {
                Scanner sc = new Scanner(op);
                if (sc.hasNext("[a-zA-Z]\\*lg\\([a-zA-Z]\\)"))
                { // n log n (base 2)
                    return x * Math.log(x) / Math.log(2);
                }
                else if (sc.hasNext("lg\\([a-zA-Z]\\)"))
                { // log (base 2)
                    return Math.log(x) / Math.log(2);
                }
                else if (sc.hasNext("[a-zA-Z]\\^[0-9]"))
                { // polynomial
                    sc.skip("[a-zA-Z]\\^");
                    int pow = sc.nextInt();
                    return Math.pow(x, pow);
                }
                else
                { 
                    throw new Exception("Operation not supported.");
                }
            }
            
        }
    }

}
