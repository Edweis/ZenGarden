package controllers.tools;

/**
 * Exception that occur when the programmer haven't set the right annotation or
 * variable to certain class.
 * 
 * @author François Rullière
 *
 */
public class WrongConstructionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/*
	 * Constructeurs
	 */
	public WrongConstructionException(String message) {
		super(message);
	}

	public WrongConstructionException(String message, Throwable cause) {
		super(message, cause);
	}

	public WrongConstructionException(Throwable cause) {
		super(cause);
	}
}
