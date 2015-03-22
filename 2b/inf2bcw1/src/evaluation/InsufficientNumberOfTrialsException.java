package evaluation;

public class InsufficientNumberOfTrialsException extends Exception
{
    private static final long serialVersionUID = 5L;

    public InsufficientNumberOfTrialsException(String message)
    { super(message); }

    public InsufficientNumberOfTrialsException(String message,
            Throwable throwable)
    { super(message, throwable); }
}
