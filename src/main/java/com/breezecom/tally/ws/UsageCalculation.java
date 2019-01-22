package com.breezecom.tally.ws;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.breezecom.tally.business.UsageSummariser;
import com.breezecom.tally.model.ClientSummary;

@RestController
public class UsageCalculation {
	
	@Autowired
	private UsageSummariser usageSummariser;

	@RequestMapping("/costCalculation")
	public Map<String, Object> costCalculation(@RequestParam(value = "short") int shortCount, @RequestParam(value = "long") int longCount,
			@RequestParam(value = "adjust", defaultValue = "0") int totalAdjustment) {
		ClientSummary sum = usageSummariser.calculateUsage(shortCount, longCount, totalAdjustment);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("Charged Count", sum.getChargedCount());
		response.put("Grace Applied", sum.isGraceApplied());
		response.put("Final Packs", sum.getFinalPacks());
		response.put("Final Charge", sum.getFinalCharge());
		return response;
	}
	
	@RequestMapping("/summariseByYearMonth")
	public String summariseYearMonth(@RequestParam(value = "year") int year, @RequestParam(value = "month") int month,  @RequestParam(value = "final", defaultValue = "false") boolean finaliseBill, @RequestParam(value = "charge", defaultValue = "false") boolean chargeBlesta) {
		
		usageSummariser.summariseByYearAndMonth(year, month, finaliseBill, chargeBlesta);
		
		return "SUCCESS - JOB DONE";
	}
	
	@RequestMapping("/summariseClientId")
	public String summariseClientId(@RequestParam(value = "clientId") String clientId, @RequestParam(value = "year") int year, @RequestParam(value = "month") int month, @RequestParam(value = "final", defaultValue = "false") boolean finaliseBill, @RequestParam(value = "charge", defaultValue = "false") boolean chargeBlesta) {
		
		Date start = usageSummariser.calculateStartDate(year, month);
		Date end = usageSummariser.calculateEndDate(year, month);
		
		usageSummariser.summariseClientId(clientId, start, end, year, month, finaliseBill, chargeBlesta);
		
		return "SUCCESS - JOB DONE";
	}
}
