package ar.com.tandilweb.persistence.domain;

import java.util.Date;

public class UsersBanned {
	
	private long id_user;
	private long id_room;
	private Date registered;
	
	public UsersBanned(long id_user, long id_room, Date registered) {
		super();
		this.id_user = id_user;
		this.id_room = id_room;
		this.registered = registered;
	}
	
	public UsersBanned() {
		
	}

	public long getId_user() {
		return id_user;
	}

	public void setId_user(long id_user) {
		this.id_user = id_user;
	}

	public long getId_room() {
		return id_room;
	}

	public void setId_room(long id_room) {
		this.id_room = id_room;
	}

	public Date isRegistered() {
		return registered;
	}

	public void setRegistered(Date registered) {
		this.registered = registered;
	}

}
