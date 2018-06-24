package com.tandilserver.poker_lobby.dataBase.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Tablatest {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id_tabla;
	private String namejaja;
	
	public Integer getId_tabla() {
		return id_tabla;
	}

	public void setId_tabla(Integer id_tabla) {
		this.id_tabla = id_tabla;
	}

	public String getNamejaja() {
		return namejaja;
	}

	public void setNamejaja(String namejaja) {
		this.namejaja = namejaja;
	}
	
}
