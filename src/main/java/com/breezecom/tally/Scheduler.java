package com.breezecom.tally;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.breezecom.tally.business.MessageImporter;
import com.breezecom.tally.business.UsageSummariser;

@Service
public class Scheduler {

	@Autowired
	MessageImporter messageImporter;

	@Autowired
	UsageSummariser usageSummariser;

	// Separate job
	// // @Scheduled(initialDelay = 1 * 1000, fixedRate = 30 * 60 * 1000)
	// @Scheduled(cron = "0 0/10 * * * ?") // FOR_PROD every 10 min
	// public void importMessageRecord() {
	// messageImporter.importMessageRecord();
	// }
	//
	// // @Scheduled(initialDelay = 1 * 1000, fixedRate = 30 * 60 * 1000)
	// @Scheduled(cron = "0 15 * * * ?") // FOR_PROD every 15 min of each hour
	// public void updateSummary() {
	// usageSummariser.summariseCurrentMonth();
	// }

	// Combined job
	@Scheduled(cron = "0 0/10 * * * ?") 
	public void importAndSummarise() {
		try {
			messageImporter.importMessageRecord();
		} catch (Exception e) {
		}
		try {
			usageSummariser.summariseCurrentMonth();
		} catch (Exception e) {
		}
	}

	// @Scheduled(initialDelay = 1 * 1000, fixedRate = 30 * 60 * 1000)
	@Scheduled(cron = "0 30 0 1 * ?") // 00:30 on each 1th day of month
	public void processingMonthlyBill() {
		// Update summary for last month
		usageSummariser.summariseLastMonth();
	}

}
