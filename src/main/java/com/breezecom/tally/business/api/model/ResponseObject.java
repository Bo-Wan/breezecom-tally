package com.breezecom.tally.business.api.model;

public class ResponseObject {
	 public String ServiceId;
	 public String ProductType;
	 public String ServiceDescription;
	 public String DialinInternalNo;
	 public int CallRefNo;
	 public String Telephone;
	 public String DateTimeSent;
	 public String Recipients;
	 public String SmsOrEmail;
	 public String Body;
	 public int NumOfSentMessage;

	@Override
	public String toString() {
		String toStringoutput = "\n<Response Object: " 
				+ "\nServiceId = " + ServiceId 
				+ "\nProductType = " + ProductType 
				+ "\nServiceDescription = " + ServiceDescription 
				+ "\nDialinInternalNo = " + DialinInternalNo 
				+ "\nCallRefNo = " + CallRefNo 
				+ "\nTelephone = " + Telephone 
				+ "\nDateTimeSent = " + DateTimeSent 
				+ "\nRecipients = " + Recipients 
				+ "\nSmsOrEmail = " + SmsOrEmail 
				+ "\nBody = " + Body
				+ "\nNumOfSentMessage = " + NumOfSentMessage 
				+ ">\n";

		return toStringoutput;
	}
}
