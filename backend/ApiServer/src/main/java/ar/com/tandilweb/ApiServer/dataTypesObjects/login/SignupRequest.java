package ar.com.tandilweb.ApiServer.dataTypesObjects.login;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

@Validated
public class SignupRequest {

	@NotNull(message = "El nickname es obligatorio")
	@Size(min = 3, max = 120, message = "El nickname debe contener 3 o más caracteres.")
	public String nick_name;
	@NotNull(message = "El correo es obligatorio")
	@Size(min = 3, max = 120, message = "El correo debe contener 3 o más caracteres.")
	public String email;
	@NotNull(message = "La contraseña es obligatoria")
	@Size(min = 8, max = 120, message = "La contraseña debe contener 8 o más caracteres.")
	public String password;

	public String photo;
	
	
}
