package com.joey.Fujikom.modules.spi.response;

import com.joey.Fujikom.common.mapper.JsonMapper;

public class BaseResponse {

	/**
	 * 返回默认的处理成功的结果
	 */
	public BaseResponse() {
		this.setStatus(true);
	}

	/**
	 * 设置状态和返回结果
	 * 
	 * @param status
	 * @param data
	 */
	public BaseResponse(boolean status, Object data) {
		this.setStatus(status);
		this.setData(data);
	}

	public BaseResponse(boolean status, Object data, String message) {
		this.setStatus(status);
		this.setData(data);
		this.setMessage(message);
	}

	public BaseResponse(boolean status, String message) {
		this.setStatus(status);
		this.setMessage(message);
	}

	/**
	 * 处理正常，并返回结果
	 * 
	 * @param data
	 */
	public BaseResponse(Object data) {
		/*this.setStatus(true);*/
		this.setData(data);
	}
	
	public BaseResponse(boolean status) {
		/*this.setStatus(true);*/
		this.setStatus(status);
	}
	/**
	 * 默认系统异常
	 * 
	 * @param status
	 */
	/*
	 * public BaseResponse(boolean status) { this.setStatus(status); if
	 * (!status) { this.data = new ErrorResponse(ResultConstant.SYSTEM_ERROR,
	 * ResultConstant.SYSTEM_ERROR_MSG); } }
	 */
	private boolean status;

	private Object data;
	
	private String message;
	
   
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String toJson() {
		return JsonMapper.getInstance().toJson(this);
	}
}
