package evaluation;

public class InconsistentCalculationsException extends Exception
{
    private static final long serialVersionUID = 4L;

    public InconsistentCalculationsException(String message)
    { super(message); }

    public InconsistentCalculationsException(String message,
            Throwable throwable)
    { super(message, throwable); }
    
    public static <T extends Comparable<T>> void
        checkConsistency(T[] results)
    {
        for (int i = 1; i < results.length; i++)
        {
            if ((results[i - 1]).compareTo(results[i]) != 0)
            {
                String message = String.format("Expected all calculations to be"
                        + " consistent, but found results on %1$d and %2$d "
                        + "trial to be different. Result of %1$d trial was "
                        + "%3$d, and of %2$d was %4$d.", i - 1, i,
                        results[i - 1], results[i]);
                try
                { throw new InconsistentCalculationsException(message); }
                catch (InconsistentCalculationsException e)
                { e.printStackTrace(); }
            }
        }
    }

}
