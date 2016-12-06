package com.joey.Fujikom.modules.spi.response;

import org.springframework.stereotype.Component;

@Component
public class MemberResponseData {
	
	private Long memberId;
	private String sessionId;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	

}
