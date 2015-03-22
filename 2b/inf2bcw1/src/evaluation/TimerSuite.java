
package evaluation;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import closestPair.EmptyCollectionException;
import closestPair.ISolution;
import closestPair.InsufficientNumberOfPointsException;
import closestPair.PointSet;

import org.math.plot.Plot2DPanel;
import org.math.plot.plotObjects.Line;

public class TimerSuite
{
    private final double DASHES_PER_SEGMENT = 5;
    
    private ArrayList<Timer> timers = new ArrayList<Timer>();
    private ArrayList<Tuple<String, double[][]>> interpolations =
            new ArrayList<Tuple<String, double[][]>>();
    
    private long evalTime = 0L;

    public TimerSuite()
    { }

    public void add(ISolution solution)
    {
        if (!contains(solution)) 
        { timers.add(new Timer(solution)); }
    }
    
    public Timer get(ISolution solution)
    {
        for (Timer timer : timers)
        {
            if (timer.getSolutionName() == solution.toString())
            { return timer; }
        }
        
        return null;
    }
    
    private boolean contains(ISolution solution)
    { return get(solution) != null; }
    
    public void test(PointSet problem) 
            throws NullPointerException, InsufficientNumberOfPointsException, 
            InsufficientNumberOfTrialsException
    { test(problem, 1); }
    
    public void test(PointSet problem, int trials) 
            throws NullPointerException, InsufficientNumberOfPointsException, 
            InsufficientNumberOfTrialsException
    {
        if (trials < 1)
        {
            throw new InsufficientNumberOfTrialsException("Expected at least 1"
                    + " trial to be performed, but found "
                    + Integer.toString(trials) + ".");
        }
        
        long start = System.currentTimeMillis();
        Integer[] results = new Integer[timers.size()];
        
        for (int i = 0; i < timers.size(); i++)
        {
            results[i] = timers.get(i).test(problem);
        }
        
        InconsistentCalculationsException.checkConsistency(results);
        evalTime += System.currentTimeMillis() - start;
    }
    
    public ArrayList<Timer> getTimers()
    { return timers; }
    
    public void interpolate(ISolution solution)
    {
        Timer timer = get(solution);
        
        try
        {
            double[][] avgInterp = timer.interpolateToAverage(
                    solution.getEquation());
            double[][] maxInterp = timer.interpolateToMax(
                    solution.getEquation());
            interpolations.add(new Tuple<String, double[][]>(
                    timer.getSolutionName() + " Solution Performance "
                            + "Interpolation Based on Average Ratio", 
                    avgInterp));
            interpolations.add(new Tuple<String, double[][]>(
                    timer.getSolutionName() + "Student Solution Performance "
                            + "Interpolation Based on Maximum Ratio", 
                    maxInterp));
        }
        catch (EmptyCollectionException e)
        { e.printStackTrace(); }
    }

    public String getTotalEvaluationTime()
    {
        int total = (int) evalTime / 1000;
        int sec = total % 60;
        int min = (total - sec) / 60;
        return String.format("%d m %d s", min, sec);
    }
    
    public String performanceTable()
    {
        if (timers.size() == 0)
        { return ""; }
        
        PrettyPrint pp = new PrettyPrint();
        
        pp.evaluateWidth(this);
        pp.setColumnsNumber(timers.size() + 1);
        pp.appendTitle("Performance Table");
        
        pp.appendCell("Elements [-]");
        pp.evaluateWidth("Elements [-]");
        for (Timer timer : timers)
        { 
            String label = timer.getSolutionName() + " [\u00b5s]";
            pp.evaluateWidth(label);
            pp.appendCell(label); }
        pp.newRow();
        
        PointSet[] pointSets = timers.get(0).getSortedPointSets();
        for (PointSet problem : pointSets)
        {
            pp.appendCell(problem.size());
            for (Timer timer : timers)
            { pp.appendCell(timer.getExecutionTime(problem)); }
            pp.newRow();
        }
        
        return pp.toString();
    }
    
    public String ratioTable()
    {
        if (timers.size() == 0)
        { return ""; }
        
        PrettyPrint pp = new PrettyPrint();
        
        pp.evaluateWidth(this);
        pp.setColumnsNumber(timers.size() + 1);
        pp.appendTitle("Ratios Table");
        
        pp.appendCell("Elements");
        for (Timer timer : timers)
        { pp.appendCell(timer.getSolutionName()); }
        pp.newRow();
        
        for (PointSet problem : timers.get(0).getSortedPointSets())
        {
            pp.appendCell(problem.size());
            for (Timer timer : timers)
            { 
                try
                { pp.appendCell(timer.getRatio(problem)); }
                catch (NullPointerException e)
                { e.printStackTrace(); }
                catch (InsufficientNumberOfPointsException e)
                { e.printStackTrace(); }
                catch (InsufficientNumberOfTrialsException e)
                { e.printStackTrace(); }
            }
            pp.newRow();
        }
        
        return pp.toString();
    }
    
    public String summaryTable() throws EmptyCollectionException
    {
        if (timers.size() == 0)
        { return ""; }
        
        PrettyPrint pp = new PrettyPrint();
        
        // pp.evaluateWidth(this);
        pp.setColumnWidth(12);
        String[] labels = new String[]{"Average Execution Time [\u00b5s]",
                                       "Worst Execution Time [\u00b5s]",
                                       "Average Time / Points Ratio [\u00b5s]", 
                                       "Worst Time / Points Ratio [\u00b5s]"};
        pp.setFirstColumnWidth(labels);
        pp.setColumnsNumber(timers.size() + 1);
        pp.appendTitle("Summary Table");
        
        pp.appendLabel("Solution");
        for (Timer timer : timers)
        { pp.appendCell(timer.getSolutionName()); }
        pp.newRow();
        
        pp.appendLabel(labels[0]);
        for (Timer timer : timers)
        { pp.appendCell((double) timer.getAverageExecutionTime()); }
        pp.newRow();
        
        pp.appendLabel(labels[1]);
        for (Timer timer : timers)
        { pp.appendCell((double) timer.getWorstExecutionTime()); }
        pp.newRow();
        
        pp.appendLabel(labels[2]);
        for (Timer timer : timers)
        { pp.appendCell((double) timer.getAverageRatio()); }
        pp.newRow();
        
        pp.appendLabel(labels[3]);
        for (Timer timer : timers)
        { pp.appendCell((double) timer.getWorstRatio()); }
        pp.newRow();
        
        return pp.toString();
    }
    
    public void plotAndSave(String... pathnames)
    {
        if (timers.size() < 1)
        { return; }
        
        Plot2DPanel plot = new Plot2DPanel("SOUTH");
        PaintBucket color = new PaintBucket();
        
        plot.setAxisLabel(0, "Point Set Size [-]");
        plot.setAxisLabel(1, "Execution Time [\u00b5s]");
        
        // plot experimental data
        for (Timer timer : timers)
        {
            String title = timer.getSolutionName() + " Solution Performance";
            double[][] xy = timer.toPlot();
            color.next();
            plot.addScatterPlot(title, color.get(), xy);
            connectPoints(plot, color.get(), xy);
        }
        
        // plot interpolations based on maximum and average ratio as well as
        // assumed interpolation equation
        for (Tuple<String, double[][]> interp : interpolations)
        {
            color.next();
            plot.addScatterPlot(interp.first, color.get(), interp.second);
            // connectPoints(plot, color.next(), interp.second);
            connectPointsUsingDashedLine(plot, color.get(), interp.second);
        }
        
        JFrame frame = new JFrame("Peformance Plot");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(plot);
        frame.setSize(960, 700);
        frame.setVisible(true);
        
        // save
        try
        {
            for (String pathname : pathnames)
            {
                System.out.println("Saving plot to PNG file " + pathname + ".");
                plot.toGraphicFile(new File(pathname));
            }
        }
        catch (IOException e)
        { e.printStackTrace(); }
    }
    
    private void connectPoints(Plot2DPanel plot, Color color, double[][] xy)
    {
        for (int i = 1; i < xy[0].length; i++)
        {
            double[] p0 = new double[] {xy[0][i-1], xy[1][i-1]},
                     p1 = new double[] {xy[0][i],   xy[1][i]};
            plot.addPlotable(new Line(color, p0, p1));
        }
    }

    private void connectPointsUsingDashedLine(Plot2DPanel plot, Color color, 
            double[][] xy)
    {
        for (int i = 1; i < xy[0].length; i++)
        {
            double x = xy[0][i - 1], y = xy[1][i - 1],
                   a = Math.atan(Math.abs(xy[1][i] - y)
                           / Math.abs(xy[0][i] - x));
            while (x < xy[0][i] && y < xy[1][i])
            {
                double dx = getDashHorizontalLength();
                double dy = dx * Math.tan(a);
                
                double[] p0 = new double[] { Math.min(xy[0][i], x), 
                                             Math.min(xy[1][i], y) },
                         p1 = new double[] { Math.min(xy[0][i], x + dx),
                                             Math.min(xy[1][i], y + dy) };
                plot.addPlotable(new Line(color, p0, p1));
                
                x += dx * 2; y += dy * 2;
            }
        }
    }
    
    private double getDashHorizontalLength()
    {
        int width  = timers.get(0).width();
        int N = timers.get(0).size();
        return width / N / (DASHES_PER_SEGMENT * 2);
    }
    
    private class PaintBucket
    {
        private final Color DEFAULT_COLOR = Color.BLACK;
        
        ArrayList<Color> bucket;
        private Iterator<Color> iter;
        private Color current;
        
        public PaintBucket()
        {
            bucket = new ArrayList<Color>();
            bucket.add(new Color(  0, 28,  153)); // naive
            bucket.add(new Color(214,   0,   0)); // student
            bucket.add(new Color( 22, 130,  12)); // naive interpolation avg
            bucket.add(new Color(129,   3, 255)); // naive interpolation max
            bucket.add(new Color(255, 157,   0));  // student interpolation avg
            bucket.add(new Color(252,  96, 179));  // student interpolation max
            iter = bucket.iterator();
        }
        
        public Color next()
        {
            if (iter.hasNext())
            { current = iter.next(); }
            else
            { current = DEFAULT_COLOR; }
            return current;
        }
        
        public Color get()
        { return current; }
    }

}
