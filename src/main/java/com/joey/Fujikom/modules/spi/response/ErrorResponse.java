package com.joey.Fujikom.modules.spi.response;

public class ErrorResponse {
	
	public ErrorResponse(String code, String msg) {
		this.setCode(code);
		this.msg = msg;
	    }

	    private String code;

	    private String msg;


	    public String getMsg() {
		return msg;
	    }

	    public void setMsg(String msg) {
		this.msg = msg;
	    }

	    public String getCode() {
		return code;
	    }

	    public void setCode(String code) {
		this.code = code;
	    }

}
