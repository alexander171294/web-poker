package com.tandilserver.poker_lobby.dataBase.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Users {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id_usuario;
	private String nick;
	private String email;
	private String password;
	private Date fecha_registro;
	private Date ultima_actividad;
	private Long fichas;
	private String hashSignature;
	private Object league; // for ranked purposes
	
	public Long getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(Long id_usuario) {
		this.id_usuario = id_usuario;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getFecha_registro() {
		return fecha_registro;
	}
	public void setFecha_registro(Date fecha_registro) {
		this.fecha_registro = fecha_registro;
	}
	public Date getUltima_actividad() {
		return ultima_actividad;
	}
	public void setUltima_actividad(Date ultima_actividad) {
		this.ultima_actividad = ultima_actividad;
	}
	public Long getFichas() {
		return fichas;
	}
	public void setFichas(Long fichas) {
		this.fichas = fichas;
	}
	public String getHashSignature() {
		return hashSignature;
	}
	public void setHashSignature(String hashSignature) {
		this.hashSignature = hashSignature;
	}
	
	
	
}
