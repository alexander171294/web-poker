package ar.com.tandilweb.ApiServer.persistence.domain;

import java.sql.Date;

public class Bans {
	
	private long id_user;
	private String reason;
	private Date registered;
	private Date expire;
	private String reporter;
	private long restarts;
	
	public Bans(long id_user, String reason, Date registered, Date expire, String reporter, long restarts) {
		super();
		this.id_user = id_user;
		this.reason = reason;
		this.registered = registered;
		this.expire = expire;
		this.reporter = reporter;
		this.restarts = restarts;
	}
	
	public Bans() {
		
	}
	
	public long getId_user() {
		return id_user;
	}
	public void setId_user(long id_user) {
		this.id_user = id_user;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getRegistered() {
		return registered;
	}
	public void setRegistered(Date registered) {
		this.registered = registered;
	}
	public Date getExpire() {
		return expire;
	}
	public void setExpire(Date expire) {
		this.expire = expire;
	}
	public String getReporter() {
		return reporter;
	}
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	public long getRestarts() {
		return restarts;
	}
	public void setRestarts(long restarts) {
		this.restarts = restarts;
	}

}
