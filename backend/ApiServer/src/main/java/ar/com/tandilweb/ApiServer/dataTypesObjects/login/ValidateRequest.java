package ar.com.tandilweb.ApiServer.dataTypesObjects.login;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

@Validated
public class ValidateRequest {
	
	@NotNull()
	public Long userID;

	@NotNull(message = "El codigo de validacion es obligatoria")
	@Size(min = 6, max = 6, message = "El codigo de validacion debe contener 6 caracteres.")
	public String validationCode;

}
