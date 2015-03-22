package closestPair;

public class ReadonlyException extends Exception
{

    private static final long serialVersionUID = 3L;

    public ReadonlyException(String message)
    { super(message); }

    public ReadonlyException(String message, Throwable throwable)
    { super(message, throwable); }
    
}
