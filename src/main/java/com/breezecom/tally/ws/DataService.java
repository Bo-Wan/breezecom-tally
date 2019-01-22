package com.breezecom.tally.ws;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.breezecom.tally.business.SystemParameterBean;
import com.breezecom.tally.business.repo.ClientSummaryRepository;
import com.breezecom.tally.business.repo.MessageRecordRepository;
import com.breezecom.tally.business.repo.ServiceSummaryRepository;
import com.breezecom.tally.business.repo.SystemParameterRepository;
import com.breezecom.tally.model.ClientSummary;
import com.breezecom.tally.model.MessageRecord;
import com.breezecom.tally.model.ServiceSummary;
import com.breezecom.tally.model.SystemParameter;

@RestController
public class DataService {

	@Autowired
	private SystemParameterBean systemParameterBean;

	@Autowired
	private SystemParameterRepository systemParameterRepository;

	@Autowired
	private MessageRecordRepository messageRecordRepository;

	@Autowired
	private ServiceSummaryRepository serviceSummaryRepository;

	@Autowired
	private ClientSummaryRepository clientSummaryRepository;

	@RequestMapping("/getParameters")
	public List<SystemParameter> getParameters() {
		return (List<SystemParameter>) systemParameterRepository.findAll();
	}

	@RequestMapping("/setParameter")
	public String setParameter(@RequestParam(value = "name") String name, @RequestParam(value = "value") String value) {
		if (name != null && !name.isEmpty() && value != null && !value.isEmpty()) {

			if (systemParameterBean.getValue(name) == null) {
				systemParameterRepository.save(new SystemParameter(name, value));
				return "SUCCESS: added new parameter key value pair.";
			} else {
				systemParameterRepository.save(new SystemParameter(name, value));
				return "SUCCESS: updated existing parameter key value pair.";
			}

		} else {
			return "ERROR: name and value cannot be null.";
		}
	}

	@RequestMapping("/reloadParameters")
	public String reloadParameters() {
		systemParameterBean.loadParams();
		return "SUCCESS - Parameters reloaded";
	}

	@RequestMapping("/getServiceSummaries")
	public List<ServiceSummary> getServiceSummaries() {
		return (List<ServiceSummary>) serviceSummaryRepository.findAll();
	}

	@RequestMapping("/getClientSummaries")
	public List<ClientSummary> getClientSummaries() {
		return (List<ClientSummary>) clientSummaryRepository.findAll();
	}

	@RequestMapping("/getMessageRecordById")
	public Optional<MessageRecord> getMessageRecordById(@RequestParam(value = "id") int id) {
		return messageRecordRepository.findById(id);
	}
}
