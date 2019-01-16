package com.tandilserver.poker_lobby.dataBase.domain;

import java.util.Date;

public class Messages {

	private Long id_message;
	private Long sender;
	private Date fecha;
	
	public Long getId_message() {
		return id_message;
	}
	public void setId_message(Long id_message) {
		this.id_message = id_message;
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
