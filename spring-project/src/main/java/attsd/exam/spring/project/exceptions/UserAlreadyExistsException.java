package attsd.exam.spring.project.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public UserAlreadyExistsException() {
        super();
    }
    public UserAlreadyExistsException(String exception) {
        super(exception);
    }
    public UserAlreadyExistsException(String exception, Throwable throwable) {
        super(exception, throwable);
    }
    public UserAlreadyExistsException(Throwable throwable) {
        super(throwable);
    }

}
