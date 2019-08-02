package ar.com.tandilweb.room.orchestratorBridge.processors.dtoDB;

public class Users {

	private long id_user;
	private String nick_name;
	private String email;
	private String password;
	private long chips;
	private String photo;
	private short badLogins;

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
}
