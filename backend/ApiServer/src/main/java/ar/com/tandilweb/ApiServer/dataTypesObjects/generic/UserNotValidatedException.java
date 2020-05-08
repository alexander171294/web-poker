package ar.com.tandilweb.ApiServer.dataTypesObjects.generic;

public class UserNotValidatedException extends ValidationException {

	private static final long serialVersionUID = -6790490056366493736L;
	
	private long userID;

	public UserNotValidatedException(long code, String message, Long userID) {
		super(code, message);
		this.userID = userID;
	}

	public long getUserID() {
		return userID;
	}

}
