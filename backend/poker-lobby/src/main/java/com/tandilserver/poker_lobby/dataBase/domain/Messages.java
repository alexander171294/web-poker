package com.tandilserver.poker_lobby.dataBase.domain;

import java.util.Date;

public class Messages {

	private Long id_message;
	private Long id_conversation;
	private Long id_user_sender;
	private Date date_sended;
	private Boolean read;
	
	public Messages() {
		
	}
	
	public Messages(Long id_message, Long id_conversation, Long id_user_sender, Date date_sended) {
		this.id_message = id_message;
		this.id_conversation = id_conversation;
		this.id_user_sender = id_user_sender;
		this.date_sended = date_sended;
	}
	
	public Long getId_conversation() {
		return id_conversation;
	}
	public void setId_conversation(Long id_message) {
		this.id_conversation = id_message;
	}
	public Long getSender() {
		return id_user_sender;
	}
	public void setSender(Long sender) {
		this.id_user_sender = sender;
	}
	public Date getDate_sended() {
		return date_sended;
	}
	public void setDate_sended(Date date_sended) {
		this.date_sended = date_sended;
	}
	public Boolean getRead() {
		return read;
	}
	public void setRead(Boolean read) {
		this.read = read;
	}
	public Long getId_message() {
		return id_message;
	}
	public void setId_message(Long id_message) {
		this.id_message = id_message;
	}
	
}
