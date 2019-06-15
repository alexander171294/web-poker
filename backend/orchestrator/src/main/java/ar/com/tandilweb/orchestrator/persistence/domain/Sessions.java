package ar.com.tandilweb.orchestrator.persistence.domain;

import java.util.Date;

public class Sessions {
	
	private long id_session;
	private long id_user;
	private String jwt_passphrase;
	private Date expiration;
	
	public Sessions(long id_session, long id_user, String jwt_passphrase, Date expiration) {
		super();
		this.id_session = id_session;
		this.id_user = id_user;
		this.jwt_passphrase = jwt_passphrase;
		this.expiration = expiration;
	}

	public Sessions() {
		
	}

	public long getId_session() {
		return id_session;
	}

	public void setId_session(long id_session) {
		this.id_session = id_session;
	}

	public long getId_user() {
		return id_user;
	}

	public void setId_user(long id_user) {
		this.id_user = id_user;
	}

	public String getJwt_passphrase() {
		return jwt_passphrase;
	}

	public void setJwt_passphrase(String jwt_passphrase) {
		this.jwt_passphrase = jwt_passphrase;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	
}
