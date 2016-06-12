package com.martink.verification;

public class VerifyTicketHolder {

	private static VerifyTicket verifyTicket;
	
	public static synchronized VerifyTicket getVerifyTicket() {
		return verifyTicket;
	}
	
	public static synchronized void setVerifyTicket(VerifyTicket newHolder) {
		verifyTicket = newHolder;
	}
}
