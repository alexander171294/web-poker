package com.tandilserver.poker_lobby.filters.JWT;

import java.util.Date;

public class JWTUnpackedData {
	private Date expiration;
	private String id;
	private Date IssuedAt;
	private String issuer;
	private Date notBefore;
	private String subject;
	
	public Date getExpiration() {
		return expiration;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getIssuedAt() {
		return IssuedAt;
	}
	public void setIssuedAt(Date issuedAt) {
		IssuedAt = issuedAt;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public Date getNotBefore() {
		return notBefore;
	}
	public void setNotBefore(Date notBefore) {
		this.notBefore = notBefore;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String target) {
		this.subject = target;
	}
	
}
