package ar.com.tandilweb.orchestrator.persistence.domain;

import java.util.Date;

public class Friendships {
	
	private long id_user_origin;
	private long id_user_target;
	private Date requested;
	private boolean accepted;
	
	public Friendships(long id_user_origin, long id_user_target, Date requested, boolean accepted) {
		super();
		this.id_user_origin = id_user_origin;
		this.id_user_target = id_user_target;
		this.requested = requested;
		this.accepted = accepted;
	}
	
	public Friendships() {
		
	}

	public long getId_user_origin() {
		return id_user_origin;
	}

	public void setId_user_origin(long id_user_origin) {
		this.id_user_origin = id_user_origin;
	}

	public long getId_user_target() {
		return id_user_target;
	}

	public void setId_user_target(long id_user_target) {
		this.id_user_target = id_user_target;
	}

	public Date getRequested() {
		return requested;
	}

	public void setRequested(Date requested) {
		this.requested = requested;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

}
