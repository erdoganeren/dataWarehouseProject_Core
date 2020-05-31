package pacApp.pacException;

public class RegistrationBadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public RegistrationBadRequestException(){
        super("Incorrect registration data");
    }
    public RegistrationBadRequestException (String message) {
        super(message);
    }

}

