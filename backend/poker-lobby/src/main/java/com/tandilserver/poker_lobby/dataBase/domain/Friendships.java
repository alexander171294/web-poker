package com.tandilserver.poker_lobby.dataBase.domain;

import java.util.Date;

public class Friendships {
	
	private Long id_requester;
	private Long id_target;
	private StatusFriendships status;
	private Date request_time;
	
	public Long getId_requester() {
		return id_requester;
	}
	public void setId_requester(Long id_requester) {
		this.id_requester = id_requester;
	}
	
	public Long getId_target() {
		return id_target;
	}
	public void setId_target(Long id_target) {
		this.id_target = id_target;
	}
	
	public StatusFriendships getStatus() {
		return status;
	}
	public void setStatus(StatusFriendships status) {
		this.status = status;
	}
	
	public Date getRequest_time() {
		return request_time;
	}
	public void setRequest_time(Date request_time) {
		this.request_time = request_time;
	}

}
