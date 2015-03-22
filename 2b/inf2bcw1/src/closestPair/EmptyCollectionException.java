package closestPair;

public class EmptyCollectionException extends Exception
{

    private static final long serialVersionUID = 1L;
    private static String errorMessage = "Expected at least one element to"
            + " perform operation, but collection was empty.\n";

    public EmptyCollectionException()
    { this(errorMessage); }
    
    public EmptyCollectionException(String message)
    { super(message); }

    public EmptyCollectionException(String message,
            Throwable throwable)
    { super(message, throwable); }

    
}
