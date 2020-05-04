package ar.com.tandilweb.persistence.domain;

public class Users {

	private long id_user;
	private String nick_name;
	private String email;
	private String password;
	private long chips;
	private String photo;
	private short badLogins;
	private String validation_code;
	private Boolean validated;

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}


	public Users() { }

	public Users(long id_user, String nick_name, String email, String password, long chips, String photo,
			short badLogins) {
		this.id_user = id_user;
		this.nick_name = nick_name;
		this.email = email;
		this.password = password;
		this.chips = chips;
		this.photo = photo;
		this.badLogins = badLogins;
	}

	public Users(long id_user, String nick_name, String email, String password, long chips, String photo,
			short badLogins, String validation_code, Boolean validated) {
		super();
		this.id_user = id_user;
		this.nick_name = nick_name;
		this.email = email;
		this.password = password;
		this.chips = chips;
		this.photo = photo;
		this.badLogins = badLogins;
		this.validation_code = validation_code;
		this.validated = validated;
	}

	public long getId_user() {
		return id_user;
	}

	public void setId_user(long id_user) {
		this.id_user = id_user;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getChips() {
		return chips;
	}

	public void setChips(long chips) {
		this.chips = chips;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public short getBadLogins() {
		return badLogins;
	}

	public void setBadLogins(short badLogins) {
		this.badLogins = badLogins;
	}
	
	public void setValidationCode(String validationCode) {
		this.validation_code = validationCode;
	}
	
	public String getValidationCode() {
		return this.validation_code;
	}
}
