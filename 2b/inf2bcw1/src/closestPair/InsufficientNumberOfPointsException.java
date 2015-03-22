
package closestPair;

public class InsufficientNumberOfPointsException extends Exception
{
    
    private static final long serialVersionUID = 2L;

    public InsufficientNumberOfPointsException(String message)
    { super(message); }

    public InsufficientNumberOfPointsException(String message,
            Throwable throwable)
    { super(message, throwable); }

}
