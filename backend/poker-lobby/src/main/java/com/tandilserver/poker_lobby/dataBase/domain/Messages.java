package com.tandilserver.poker_lobby.dataBase.domain;

import java.util.Date;

public class Messages {

	private Long id_message;
	private Long id_conversation;
	private Long sender;
	private Date fecha;
	private Boolean read;
	
	public Long getId_conversation() {
		return id_conversation;
	}
	public void setId_conversation(Long id_message) {
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
	public Boolean getRead() {
		return read;
	}
	public void setRead(Boolean read) {
		this.read = read;
	}
	
}
