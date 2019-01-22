package com.breezecom.tally.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.breezecom.tally.business.api.ApiInvoker;
import com.breezecom.tally.business.api.model.MessageRecordResponse;
import com.breezecom.tally.business.api.model.ResponseObject;
import com.breezecom.tally.business.repo.MessageRecordRepository;
import com.breezecom.tally.model.MessageRecord;
import com.breezecom.tally.model.SystemParameter;
import com.breezecom.tally.model.nopersistence.MessageType;

@Service
public class MessageImporter {

	private static final Logger log = LoggerFactory.getLogger(MessageImporter.class);

	@Autowired
	private SystemParameterBean systemParameterBean;

	@Autowired
	private ApiInvoker apiInvoker;

	@Autowired
	private MessageRecordRepository messageRecordRepository;

	public void importMessageRecord() {

		log.info("Starting Message Record import");

		// Request message record
		// Get count from parameter database
		int ocmsApiIdCount = systemParameterBean.getIntValue(SystemParameter.OCMS_API_ID_COUNT);
		log.info("Continuing from MessageRecord ID " + ocmsApiIdCount);
		boolean updateOcmsApiCount = false;

		MessageRecordResponse response = apiInvoker.retrieveMessageRecord(ocmsApiIdCount);

		// interpret API data
		if (response == null) {
			log.info("API invocation failed - No response returned.");
		}

		else if (response.getErrorCode() != null || response.getResponseCode() != 0
				|| response.getResponseObject() == null) {
			log.info(response.toString());
			log.info("API invocation failed with error response returned. Please see API response details.");
			return;
		} else if (response.getResponseObject().size() == 0) {
			// log.info(response.toString());
			log.info("API invocation done - no new message records.");
			return;
		}

		log.info("API invokation succeeded. Interpreting data.");
		// log.info(response.toString());

		for (ResponseObject ro : response.getResponseObject()) {
			// Update API id count
			if (ro.CallRefNo > ocmsApiIdCount) {
				ocmsApiIdCount = ro.CallRefNo;
				updateOcmsApiCount = true;
			}

			// Check if message record exist
			Optional<MessageRecord> optionalRec = messageRecordRepository.findById(ro.CallRefNo);
			MessageRecord rec;

			// If exists, update SMS / Email contact and email body
			if (optionalRec.isPresent()) {
				rec = optionalRec.get();

				if (ro.SmsOrEmail.equals("Email")
						&& (rec.getEmailRecipients() == null || rec.getEmailRecipients().isEmpty())) {
					rec.setEmailRecipients(ro.Recipients);
					saveEmailMessageBody(rec, ro.Body);
				}

				else if (ro.SmsOrEmail.equals("SMS")
						&& (rec.getSmsRecipients() == null || rec.getSmsRecipients().isEmpty())) {
					rec.setSmsRecipients(cleanUpSmsRecipients(ro.Recipients));
				}
			}

			// If no, save new record
			else {
				// Save message record
				rec = new MessageRecord();
				rec.setCallRefNo(ro.CallRefNo);
				rec.setServiceId(ro.ServiceId);
				rec.setDid(ro.DialinInternalNo);
				rec.setCallerNumberCli(ro.Telephone);

				// Get date
				Date date = null;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(ro.DateTimeSent);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				rec.setDate(date);

				// Get client id + business name
				String desc = ro.ServiceDescription;
				if (desc.contains("client_id=") || desc.contains("bus_name=")) {

					String[] props = desc.split(";");

					for (String prop : props) {
						if (prop.startsWith("client_id=")) {
							String client_id = prop.split("=")[1];
							rec.setClientId(client_id);
						}

						else if (prop.startsWith("bus_name=")) {
							String business_name = prop.split("=")[1];
							rec.setBusinessName(business_name);
						}
					}
				}

				// Message type
				String shortMessageKeyString = systemParameterBean.getValue(SystemParameter.SHORT_MESSAGE_KEY);
				String[] keys = shortMessageKeyString.split(",");
				boolean isShort = false;
				for (String key : keys) {
					if (ro.Body.contains(key)) {
						isShort = true;
						break;
					}
				}

				if (isShort) {
					rec.setMessageType(MessageType.SHORT_MESSAGE);
				} else {
					rec.setMessageType(MessageType.LONG_MESSAGE);
				}

				// Message rcpt + split body
				if (ro.SmsOrEmail.equals("Email")) {
					rec.setEmailRecipients(ro.Recipients);
					saveEmailMessageBody(rec, ro.Body);
				} else if (ro.SmsOrEmail.equals("SMS")) {
					rec.setSmsRecipients(cleanUpSmsRecipients(ro.Recipients));
					saveSmsMessageBody(rec, ro.Body);
				}
			}

			messageRecordRepository.save(rec);
		}

		// Update count to parameter database
		if (updateOcmsApiCount) {
			// Reactivate update mechanism for production - in test, I want
			// all records to be read, every single time
			systemParameterBean.updateParam(SystemParameter.OCMS_API_ID_COUNT, String.valueOf(ocmsApiIdCount));
		}

		log.info("Finishing Message Record import");
	}

	private String cleanUpSmsRecipients(String recipients) {
		if (!recipients.contains("@"))
			return recipients;

		String cleanRcpt = "";
		String[] list = recipients.split(",");
		for (String recipient : list) {
			String[] parts = recipient.split("@");
			if (parts != null && parts.length >= 1) {
				cleanRcpt += parts[0] + ",";
			}
		}

		if (!cleanRcpt.isEmpty())
			cleanRcpt = cleanRcpt.substring(0, cleanRcpt.length() - 1);

		return cleanRcpt;
	}

	private void saveEmailMessageBody(MessageRecord rec, String rawMessage) {
		saveMessageBody(rec, rawMessage, "Name: ", "Telephone: ", "Nature Of Call: ", "\r\n");
	}

	private void saveSmsMessageBody(MessageRecord rec, String rawMessage) {
		saveMessageBody(rec, rawMessage, "Name: ", "Ph: ", "Msg: ", "\r\n");
	}

	private void saveMessageBody(MessageRecord rec, String rawMessage, String nameIdentifier,
			String telephoneIdentifier, String contentIdentifier, String newLineIdentifier) {

		// Caller name
		try {
			int nameStart = rawMessage.indexOf(nameIdentifier);
			int nameEnd = rawMessage.substring(nameStart).indexOf(newLineIdentifier);
			rec.setMessageCallerName(rawMessage.substring(nameStart + nameIdentifier.length(), nameStart + nameEnd));
		} catch (Exception e) {
			log.info("Failed to extract caller name from rawmessage: " + rawMessage);
		}

		// Caller number
		try {
			int numberStart = rawMessage.indexOf(telephoneIdentifier);
			int numberEnd = rawMessage.substring(numberStart).indexOf(newLineIdentifier);
			rec.setCallerNumberProvided(
					rawMessage.substring(numberStart + telephoneIdentifier.length(), numberStart + numberEnd));
		} catch (Exception e) {
			log.info("Failed to extract caller number from raw message: " + rawMessage);
		}

		// Body
		try {
			int contentStart = rawMessage.indexOf(contentIdentifier);
			rec.setMessageContent(rawMessage.substring(contentStart));
		} catch (Exception e) {
			log.info("Failed to extract main message cotent from raw message: " + rawMessage);
		}
	}
}
