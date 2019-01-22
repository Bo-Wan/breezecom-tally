package com.breezecom.tally.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.breezecom.tally.business.repo.SystemParameterRepository;
import com.breezecom.tally.model.SystemParameter;

@Service
public class SystemParameterBean {
	private static final Logger log = LoggerFactory.getLogger(SystemParameterBean.class);

	private boolean initialised = false;

	private Map<String, String> params;

	@Autowired
	private SystemParameterRepository systemParameterRepository;

	public boolean isInitialised() {
		return initialised;
	}

	@PostConstruct
	public void loadParams() {
		log.info("Loading system parameters");

		List<SystemParameter> paramObjs = (List<SystemParameter>) systemParameterRepository.findAll();

		params = new HashMap<String, String>(paramObjs.size());

		for (SystemParameter pramObj : paramObjs) {
			params.put(pramObj.getName(), pramObj.getValue());
			// Debug
			// log.info("name = " + pramObj.getName() + " | value = " +
			// pramObj.getValue());
		}

		initialised = true;

		log.info("System parameters successfully loaded");
	}

	public SystemParameter getParam(String paramName) {
		return systemParameterRepository.findByName(paramName);
	}

	public String getValue(String paramName) {
		return params.get(paramName);
	}

	public int getIntValue(String paramName) {
		String paramString = params.get(paramName);
		int param = 0;
		try {
			param = Integer.parseInt(paramString);
		} catch (NumberFormatException e) {
		}
		return param;
	}

	public double getDoubleValue(String paramName) {
		String paramString = params.get(paramName);
		double param = 0;
		try {
			param = Double.parseDouble(paramString);
		} catch (NumberFormatException e) {
		}
		return param;
	}

	public boolean updateParam(String name, String value) {
		SystemParameter param = getParam(name);

		if (param == null) {
			return false;
		}

		param.setValue(value);
		systemParameterRepository.save(param);

		loadParams();

		return true;
	}

}
