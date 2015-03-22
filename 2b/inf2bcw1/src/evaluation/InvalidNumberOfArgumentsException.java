package evaluation;

public class InvalidNumberOfArgumentsException extends Exception
{
    
    private static final long serialVersionUID = 5L;

    public InvalidNumberOfArgumentsException(String message)
    { super(message); }

    public InvalidNumberOfArgumentsException(String message,
            Throwable throwable)
    { super(message, throwable); }
    
}
