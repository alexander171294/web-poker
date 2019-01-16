package com.tandilserver.poker_lobby.dataBase.domain;

import java.util.Date;

public class Conversations {

	private Long id_conversation;
    private Users user_origin;
    private Users user_target;
	private Date last_message;
	
	public Conversations() {
		
	}
	
	public Conversations(Long id_conversation, Users user_origin, Users user_target, Date last_message) {
		this.id_conversation = id_conversation;
		this.user_origin = user_origin;
		this.user_target = user_target;
		this.last_message = last_message;
	}
	
	public Long getId_conversation() {
		return id_conversation;
	}
	public void setId_conversation(Long id_conversation) {
		this.id_conversation = id_conversation;
	}
	public Users getUser_origin() {
		return user_origin;
	}
	public void setUser_origin(Users user_origin) {
		this.user_origin = user_origin;
	}
	public Users getUser_target() {
		return user_target;
	}
	public void setUser_target(Users user_target) {
		this.user_target = user_target;
	}
	public Date getLast_message() {
		return last_message;
	}
	public void setLast_message(Date last_messages) {
		this.last_message = last_messages;
	}
    
}
