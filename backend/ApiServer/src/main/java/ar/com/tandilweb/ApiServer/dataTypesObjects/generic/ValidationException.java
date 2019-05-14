package ar.com.tandilweb.ApiServer.dataTypesObjects.generic;

public class ValidationException extends Exception {
	
	private static final long serialVersionUID = -4955914967317962206L;
	
	private long idECode;
	
	public ValidationException(long code, String message) {
		super(message);
		this.idECode = code;
	}

	public long getIdECode() {
		return idECode;
	}

}
