package com.tandilserver.poker_lobby.services.utils;

import com.tandilserver.poker_lobby.services.dto.out.StatusCodes;

public class ExceptionResponse extends Exception{

	private static final long serialVersionUID = 1L;
	private StatusCodes code;
	
	public ExceptionResponse(StatusCodes code, String message) {
		super(message);
		this.code = code;
	}

	public StatusCodes getCode() {
		return code;
	}

	public void setCode(StatusCodes code) {
		this.code = code;
	}
}
