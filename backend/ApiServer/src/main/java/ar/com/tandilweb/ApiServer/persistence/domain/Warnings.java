package ar.com.tandilweb.ApiServer.persistence.domain;

import java.util.Date;

public class Warnings {
	
	private long id_user;
	private String message;
	private Date resgistered;
	private long restarts;
	private String last_reporter;
	
	public Warnings(long id_user, String message, Date resgistered, long restarts, String last_reporter) {
		super();
		this.id_user = id_user;
		this.message = message;
		this.resgistered = resgistered;
		this.restarts = restarts;
		this.last_reporter = last_reporter;
	}
	
	public Warnings() {
		
	}

	public long getId_user() {
		return id_user;
	}

	public void setId_user(long id_user) {
		this.id_user = id_user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getResgistered() {
		return resgistered;
	}

	public void setResgistered(Date resgistered) {
		this.resgistered = resgistered;
	}

	public long getRestarts() {
		return restarts;
	}

	public void setRestarts(long restarts) {
		this.restarts = restarts;
	}

	public String getLast_reporter() {
		return last_reporter;
	}

	public void setLast_reporter(String last_reporter) {
		this.last_reporter = last_reporter;
	}
	
}
