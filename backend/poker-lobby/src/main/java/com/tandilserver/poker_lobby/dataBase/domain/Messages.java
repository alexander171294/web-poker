package com.tandilserver.poker_lobby.dataBase.domain;

import java.util.Date;

public class Messages {

	private Long id_conversation;
	private Long sender;
	private Date fecha;
	
	public Long getid_conversation() {
		return id_conversation;
	}
	public void setid_conversation(Long id_message) {
		this.id_conversation = id_message;
	}
	public Long getSender() {
		return sender;
	}
	public void setSender(Long sender) {
		this.sender = sender;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
}
