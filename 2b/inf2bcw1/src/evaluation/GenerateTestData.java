package evaluation;

import java.io.File;

import closestPair.PointSet;

public class GenerateTestData
{
    
    private static void prepareFolder(String folderName)
    {
        File folder = new File(folderName);
        
        if (!folder.exists())
        { folder.mkdir(); }
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length != 8)
        {
            throw new Exception("Expected 8 arguments: target_path file_prefix"
                    + " file_extension (int)tests (int) step (int)first"
                    + " (int)min (int)max");
        }
        
        String targetPath = args[0];
        String filePrefix = targetPath + File.separator + args[1];
        String ext = args[2];
        
        int tests = Integer.parseInt(args[3]);
        int step  = Integer.parseInt(args[4]);
        int first = Integer.parseInt(args[5]);
        int min   = Integer.parseInt(args[6]);
        int max   = Integer.parseInt(args[7]);
        
        prepareFolder(targetPath);
        
        for (int i = 0, n = first; i < tests; n += step, i++)
        {
            String pathname = filePrefix + String.format("%d.%s", i, ext);
            PointSet pointSet = new PointSet(n, min, max);
            pointSet.sort();
            System.out.println(String.format("Generated test PointSet with %d "
                    + "unique points.", pointSet.size()));
            pointSet.save(pathname);
        }
    }
    
}
