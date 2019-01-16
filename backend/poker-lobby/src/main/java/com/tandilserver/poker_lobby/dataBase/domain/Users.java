package com.tandilserver.poker_lobby.dataBase.domain;

import java.util.Date;

public class Users {

	private Long id_usuario;
	private String nick;
	private String email;
	private String password;
	private Date signup_date;
	private Date last_activity;
	private Long coins;
	private String hashSignature;
	private Long id_user_recommend;
	
	@SuppressWarnings("unused")
	private Object league; // for ranked purposes
	
	public Users() {
		
	}
	
	public Users(Long id_usuario, String nick, String email, String password, Date signup_date, Date last_activity, Long coins, String hashSignature, Long id_user_recommend) {
		this.id_usuario = id_usuario;
		this.nick = nick;
		this.email = email;
		this.password = password;
		this.signup_date = signup_date;
		this.last_activity = last_activity;
		this.coins = coins;
		this.hashSignature = hashSignature;
		this.id_user_recommend = id_user_recommend;
	}
	
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
	public String getHashSignature() {
		return hashSignature;
	}
	public void setHashSignature(String hashSignature) {
		this.hashSignature = hashSignature;
	}
	public Date getSignup_date() {
		return signup_date;
	}
	public void setSignup_date(Date signup_date) {
		this.signup_date = signup_date;
	}
	public Date getLast_activity() {
		return last_activity;
	}
	public void setLast_activity(Date last_activity) {
		this.last_activity = last_activity;
	}
	public Long getCoins() {
		return coins;
	}
	public void setCoins(Long coins) {
		this.coins = coins;
	}

	public Long getId_user_recommend() {
		return id_user_recommend;
	}

	public void setId_user_recommend(Long id_user_recommend) {
		this.id_user_recommend = id_user_recommend;
	}
	

}
