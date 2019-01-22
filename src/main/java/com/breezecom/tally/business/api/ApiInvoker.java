package com.breezecom.tally.business.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.breezecom.tally.business.api.model.MessageRecordRequest;
import com.breezecom.tally.business.api.model.MessageRecordResponse;

@Service
public class ApiInvoker {

	private static final Logger log = LoggerFactory.getLogger(ApiInvoker.class);

	// Actual API but returns -1. Let's set it aside
	public MessageRecordResponse retrieveMessageRecord(int prevCount) {

		log.info("Retrieving Message Record from remote API");

		// Retrieve API from latest count
		RestTemplate rt = new RestTemplate();
		final String uri = "https://ggfaapi.oraclecms.com/api/v1/customertennant/generic";
		MessageRecordRequest request = new MessageRecordRequest(prevCount);

		MessageRecordResponse response = rt.postForObject(uri, request, MessageRecordResponse.class);
		log.info("API consumption done.");

		return response;
	}

}
