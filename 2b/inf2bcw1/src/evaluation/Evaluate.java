
package evaluation;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import closestPair.EmptyCollectionException;
import closestPair.ISolution;
import closestPair.InsufficientNumberOfPointsException;
import closestPair.NaiveSolution;
import closestPair.PointSet;
import closestPair.StudentSolution;

public class Evaluate
{
    private static String DATAPATH, PREFIX, EXT, REPORT_PATH,
        PERFORMANCE_PATH, RATIOS_PATH, SUMMARY_PATH, PLOT_PATH,
        CACHED_PATH, CACHED_PERFORMANCE_PATH, CACHED_RATIOS_PATH,
        CACHED_SUMMARY_PATH, CACHED_PLOT_PATH;

    private static TimerSuite timerSuite = new TimerSuite();
    
    private static void show(String message)
    { System.out.println(message); }

    private static void initializePaths(String data, String prefix, String ext,
            String report)
    {
        String TIMESTAMP        = getTimeStamp();
        DATAPATH                = data + File.separator;
        PREFIX                  = prefix;
        EXT                     = "." + ext;
        REPORT_PATH             = report + File.separator;
        PERFORMANCE_PATH        = REPORT_PATH + "performance.txt";
        RATIOS_PATH             = REPORT_PATH + "ratios.txt";
        SUMMARY_PATH            = REPORT_PATH + "summary.txt";
        PLOT_PATH               = REPORT_PATH + "plot.png";
        CACHED_PATH             = REPORT_PATH + "cached" + File.separator;
        CACHED_PERFORMANCE_PATH = CACHED_PATH + TIMESTAMP + "_performance.txt";
        CACHED_RATIOS_PATH      = CACHED_PATH + TIMESTAMP + "_ratios.txt";
        CACHED_SUMMARY_PATH     = CACHED_PATH + TIMESTAMP + "_summary.txt";
        CACHED_PLOT_PATH        = CACHED_PATH + TIMESTAMP + "_plot.png";
    }
    
    public static void main(String[] args) 
            throws InvalidNumberOfArgumentsException,
                   NullPointerException,
                   InsufficientNumberOfPointsException,
                   InsufficientNumberOfTrialsException
    {
        long totalTime = System.currentTimeMillis();
        
        if (args.length != 9)
        {
            throw new InvalidNumberOfArgumentsException("Expected 7 arguments: "
                    + "data_path test_file_prefix test_file_extension "
                    + "report_path first_value step_value last_value"); 
        }
        
        ArrayList<ISolution> solutions = new ArrayList<ISolution>();
        solutions.add(new NaiveSolution());
        solutions.add(new StudentSolution());
        
        initializePaths(args[0], args[1], args[2], args[3]);
        
        int first = Integer.parseInt(args[4]);
        int step = Integer.parseInt(args[5]);
        int last = Integer.parseInt(args[6]);
        String useSolutions = args[7];
        String interpolateSolutions = args[8];
        
        for (ISolution sol : solutions)
        {
            if (useSolutions.contains(sol.toString())
                    || useSolutions.equals("all"))
            { timerSuite.add(sol); }
        }
//        timerSuite.add(new NaiveSolution());
//        timerSuite.add(new StudentSolution());
        
        // testing
        show("Performing tests on selected data from " + DATAPATH);
        for (int i = first; i < last; i += step)
        {
            String dataFile = DATAPATH + PREFIX + Integer.toString(i) + EXT;
            show("Loading data from " + dataFile);
            PointSet problem = new PointSet(dataFile);
            System.out.println("Performing test on data set #" + i + " of size "
                             + problem.size() + ".");
            timerSuite.test(problem);
        }
        
        // report: pretty print to files
        PrettyPrint pp = new PrettyPrint();
        pp.append(timerSuite.performanceTable());
        pp.save(PERFORMANCE_PATH, CACHED_PERFORMANCE_PATH);
        
        pp = new PrettyPrint();
        pp.append(timerSuite.ratioTable());
        pp.save(RATIOS_PATH, CACHED_RATIOS_PATH);
        
        try
        {
            pp = new PrettyPrint();
            pp.append(timerSuite.summaryTable());
            pp.save(SUMMARY_PATH, CACHED_SUMMARY_PATH);
        }
        catch (EmptyCollectionException e)
        {
            System.err.println("Unable do generate summary table due to it"
                    + " being empty.");
            e.printStackTrace();
        }
        
        // report: save plot to file
        for (ISolution sol : solutions)
        {
            if (interpolateSolutions.contains(sol.toString())
                    || interpolateSolutions.equals("all"))
            { timerSuite.interpolate(sol); }
        }
//        timerSuite.interpolate(new NaiveSolution(), "n^2");
//        timerSuite.interpolate(new StudentSolution(), "n * lg (n)");
        
        totalTime = System.currentTimeMillis() - totalTime;
        int total = (int) totalTime / 1000;
        int s = total % 60;
        int m = (total - s) / 60;
        
        show("Total time spend in solving problems: "
                + timerSuite.getTotalEvaluationTime() + ".");
        show(String.format("Total evaluation time %d m %d s.", m, s));
        
        timerSuite.plotAndSave(PLOT_PATH, CACHED_PLOT_PATH);
        
        show("Done, exiting...");
    }
    
    private static String getTimeStamp()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        Date date = new Date();
        String timestamp = dateFormat.format(date);
        return timestamp;
    }

}
