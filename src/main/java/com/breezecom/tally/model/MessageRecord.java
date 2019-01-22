package com.breezecom.tally.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.breezecom.tally.model.nopersistence.MessageType;

@Entity
@Table(name = "message_record")
@NamedQueries({
		@NamedQuery(name = "MessageRecord.listActiveServiceIds", query = "SELECT DISTINCT rec.serviceId FROM MessageRecord rec WHERE rec.date BETWEEN :startDate AND :endDate"),
		@NamedQuery(name = "MessageRecord.listActiveClientIds", query = "SELECT DISTINCT rec.clientId FROM MessageRecord rec WHERE rec.date BETWEEN :startDate AND :endDate"),
		@NamedQuery(name = "MessageRecord.shortCountByServiceId", query = "SELECT COUNT(*) FROM MessageRecord rec where rec.date BETWEEN :startDate AND :endDate AND serviceId = :serviceId AND message_type = 'SHORT_MESSAGE'"),
		@NamedQuery(name = "MessageRecord.shortCountByClientId", query = "SELECT COUNT(*) FROM MessageRecord rec where rec.date BETWEEN :startDate AND :endDate AND client_id = :client_id AND message_type = 'SHORT_MESSAGE'"),
		@NamedQuery(name = "MessageRecord.longCountByServiceId", query = "SELECT COUNT(*) FROM MessageRecord rec where rec.date BETWEEN :startDate AND :endDate AND serviceId = :serviceId AND message_type = 'LONG_MESSAGE'"),
		@NamedQuery(name = "MessageRecord.longCountByClientId", query = "SELECT COUNT(*) FROM MessageRecord rec where rec.date BETWEEN :startDate AND :endDate AND client_id = :client_id AND message_type = 'LONG_MESSAGE'"),
		@NamedQuery(name = "MessageRecord.totalCountByServiceId", query = "SELECT COUNT(*) FROM MessageRecord rec where rec.date BETWEEN :startDate AND :endDate AND serviceId = :serviceId"),
		@NamedQuery(name = "MessageRecord.totalCountByClientId", query = "SELECT COUNT(*) FROM MessageRecord rec where rec.date BETWEEN :startDate AND :endDate AND client_id = :client_id"),

		@NamedQuery(name = "MessageRecord.getDidByServiceId", query = "SELECT did FROM MessageRecord WHERE serviceId = :serviceId AND date BETWEEN :startDate AND :endDate GROUP BY did ORDER BY count(did) DESC"),
		@NamedQuery(name = "MessageRecord.getClientIdByServiceId", query = "SELECT clientId FROM MessageRecord WHERE serviceId = :serviceId AND date BETWEEN :startDate AND :endDate GROUP BY clientId ORDER BY count(clientId) DESC"), })
public class MessageRecord {

	// Basic fields
	@Id
	private int callRefNo;

	private String serviceId;

	private String clientId;

	private String businessName;

	private String did;

	private String callerNumberCli;

	private String callerNumberProvided;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String emailRecipients;

	private String smsRecipients;

	// Message body
	private String messageCallerName;

	@Column(length = 510)
	private String messageContent;

	// Calculated
	@Enumerated(EnumType.STRING)
	private MessageType messageType;

	// Unused
	private String addQuestion1;

	public int getCallRefNo() {
		return callRefNo;
	}

	public void setCallRefNo(int callRefNo) {
		this.callRefNo = callRefNo;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String client_id) {
		this.clientId = client_id;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String business_name) {
		this.businessName = business_name;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getCaller_number_cli() {
		return callerNumberCli;
	}

	public void setCallerNumberCli(String caller_number_cli) {
		this.callerNumberCli = caller_number_cli;
	}

	public String getCallerNumberProvided() {
		return callerNumberProvided;
	}

	public void setCallerNumberProvided(String caller_number_provided) {
		this.callerNumberProvided = caller_number_provided;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getEmailRecipients() {
		return emailRecipients;
	}

	public void setEmailRecipients(String email_recipients) {
		this.emailRecipients = email_recipients;
	}

	public String getSmsRecipients() {
		return smsRecipients;
	}

	public void setSmsRecipients(String sms_recipients) {
		this.smsRecipients = sms_recipients;
	}

	public String getMessageCallerName() {
		return messageCallerName;
	}

	public void setMessageCallerName(String message_caller_name) {
		this.messageCallerName = message_caller_name;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String message_content) {
		try {
			int size = getClass().getDeclaredField("message_content").getAnnotation(Column.class).length();
			int inLength = message_content.length();

			if (inLength > size) {
				// Debug
				// System.out.println("DATA_IS_TOO_LONG! = " +
				// message_content.length());
				// System.out.println("DATA_BEFORE = " + message_content);
				message_content = message_content.substring(0, size);
				// System.out.println("DATA_AFTER = " + message_content);
			}
		} catch (NoSuchFieldException ex) {

		} catch (SecurityException ex) {

		}

		this.messageContent = message_content;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType message_type) {
		this.messageType = message_type;
	}

	public String getAddQuestion1() {
		return addQuestion1;
	}

	public void setAddQuestion1(String add_question_1) {
		this.addQuestion1 = add_question_1;
	}

}
