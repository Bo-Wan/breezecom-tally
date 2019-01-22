package com.breezecom.tally.business;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.breezecom.tally.business.repo.ClientSummaryRepository;
import com.breezecom.tally.business.repo.MessageRecordRepository;
import com.breezecom.tally.business.repo.ServiceSummaryRepository;
import com.breezecom.tally.model.ClientSummary;
import com.breezecom.tally.model.ServiceSummary;
import com.breezecom.tally.model.SystemParameter;

@Service
public class UsageSummariser {

	private static final Logger log = LoggerFactory.getLogger(MessageImporter.class);

	@Autowired
	private SystemParameterBean systemParameterBean;

	@Autowired
	private MessageRecordRepository messageRecordRepository;

	@Autowired
	private ServiceSummaryRepository serviceSummaryRepository;

	@Autowired
	private ClientSummaryRepository clientSummaryRepository;

	// Update current month's summary
	public void summariseCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;

		summariseByYearAndMonth(year, month, false, false);
	}

	// Update last month's summary
	public void summariseLastMonth() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;

		summariseByYearAndMonth(year, month - 1, true, true);
		// summariseByYearAndMonth(year, month, true, true);
	}

	public Date calculateStartDate(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date start = cal.getTime();
		return start;
	}

	public Date calculateEndDate(int year, int month) {
		// Get ALL message record for first day of month -> now
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		Date end = cal.getTime();
		return end;
	}

	// Update any month's summary
	public void summariseByYearAndMonth(int year, int month, boolean finaliseBill, boolean chargeBlesta) {

		Date start = calculateStartDate(year, month);
		Date end = calculateEndDate(year, month);

		// Debug
		// log.info("Running update summary job for year = " + year + ", month =
		// " + month + ". Start = " + start
		// + ", end = " + end);

		// Service Summaries
		log.info("Starting Service Summary Update");

		List<String> serviceIds = messageRecordRepository.listActiveServiceIds(start, end);

		for (String serviceId : serviceIds) {
			int shortCount = messageRecordRepository.shortCountByServiceId(start, end, serviceId);
			int longCount = messageRecordRepository.longCountByServiceId(start, end, serviceId);
			int totalCount = messageRecordRepository.totalCountByServiceId(start, end, serviceId);

			ServiceSummary serviceSum;
			List<ServiceSummary> serviceSums = serviceSummaryRepository.findByServiceIdAndYearAndMonth(serviceId, year,
					month);

			// Clean more than 1 results just in case
			if (serviceSums != null && serviceSums.size() > 1) {
				log.info("Error - more than one service sum object for service id:" + serviceId);
				for (ServiceSummary sum : serviceSums) {
					serviceSummaryRepository.delete(sum);
				}
				log.info("Excessive service sum object cleaned for service id:" + serviceId);
			}

			// If there's only 1 result
			if (serviceSums != null && serviceSums.size() == 1) {
				serviceSum = serviceSums.get(0);
				log.info("Updating existing Service Sum object for id: " + serviceSum.getServiceId());
			}

			// Null or 0 result or cleaned multiple results
			else {
				serviceSum = new ServiceSummary();
				serviceSum.setServiceId(serviceId);

				List<String> dids = messageRecordRepository.getDidByServiceId(serviceId, start, end);
				if (dids != null) {
					serviceSum.setDid(dids.get(0));
				}

				List<String> clientIds = messageRecordRepository.getClientIdByServiceId(serviceId, start, end);
				if (clientIds != null) {
					serviceSum.setClientId(clientIds.get(0));
				}
				serviceSum.setYear(year);
				serviceSum.setMonth(month);
				serviceSum.setShortCount(0);
				serviceSum.setLongCount(0);
				serviceSum.setTotalCount(0);
				serviceSum.setAdjustment(0);

			}

			// Update
			serviceSum.setShortCount(shortCount);
			serviceSum.setLongCount(longCount);
			serviceSum.setTotalCount(totalCount);

			serviceSummaryRepository.save(serviceSum);

			log.info("Done updating service sum for service id " + serviceId);
		}

		log.info("Finishing All Service Summary Update");

		// Client Summaries
		List<String> clientIds = messageRecordRepository.listActiveClientIds(start, end);

		for (String clientId : clientIds) {
			summariseClientId(clientId, start, end, year, month, finaliseBill, chargeBlesta);
		}

		log.info("Finishing All Client Summary Update");
	}

	// Part of full summarizing job - separated for redoing bill
	public void summariseClientId(String clientId, Date start, Date end, int year, int month, boolean finaliseBill,
			boolean chargeBlesta) {
		log.info("Start updating Client Sum for client id " + clientId);

		if (clientId == null) {
			return;
		}

		int shortCount = messageRecordRepository.shortCountByClientId(start, end, clientId);
		int longCount = messageRecordRepository.longCountByClientId(start, end, clientId);
		int totalCount = messageRecordRepository.totalCountByClientId(start, end, clientId);

		ClientSummary clientSum;
		List<ClientSummary> clientSums = clientSummaryRepository.findByClientIdAndYearAndMonth(clientId, year, month);

		// Clean more than 1 results just in case
		if (clientSums != null && clientSums.size() > 1) {
			log.info("Error - more than one client sum object for client id:" + clientId);
			for (ClientSummary sum : clientSums) {
				clientSummaryRepository.delete(sum);
			}
			log.info("Excessive client sum object cleaned for client id:" + clientId);
		}

		// If there's only 1 result
		if (clientSums != null && clientSums.size() == 1) {
			clientSum = clientSums.get(0);
			log.info("Updating existing Client Sum object for client id: " + clientSum.getClientId());
		}

		// Null or 0 result or cleaned multiple results
		else {
			clientSum = new ClientSummary();
			clientSum.setClientId(clientId);
			clientSum.setYear(year);
			clientSum.setMonth(month);
			clientSum.setShortCount(0);
			clientSum.setLongCount(0);
			clientSum.setTotalCount(0);
			clientSum.setTotalAdjustment(0);
			clientSum.setChargedCount(-1);
			clientSum.setFinalPacks(-1);
			clientSum.setFinalCharge(-1);
		}

		// Update
		clientSum.setShortCount(shortCount);
		clientSum.setLongCount(longCount);
		clientSum.setTotalCount(totalCount);
		Integer totalAdjustment = serviceSummaryRepository.getTotalAdjustmentByServiceId(clientId, year, month);
		if(totalAdjustment != null)
			clientSum.setTotalAdjustment(totalAdjustment);
		else
			clientSum.setTotalAdjustment(0);
		
		if (finaliseBill) {
			ClientSummary result = calculateUsage(shortCount, longCount, totalAdjustment);
			clientSum.setChargedCount(result.getChargedCount());
			clientSum.setFinalPacks(result.getFinalPacks());
			clientSum.setFinalCharge(result.getFinalCharge());

			if (chargeBlesta) {
				// TODO bill through Blesta
			}
		}

		clientSummaryRepository.save(clientSum);
		log.info("Done updating Client Sum for client id " + clientId);
	}

	// Part of Client Summarizing job - separation for testing
	public ClientSummary calculateUsage(int shortCount, int longCount, int totalAdjustment) {

		if (shortCount + longCount > 0) {
			// Global values
			int longIncl = systemParameterBean.getIntValue(SystemParameter.LONG_MESSAGE_INCLUDED);
			int shortIncl = systemParameterBean.getIntValue(SystemParameter.SHORT_MESSAGE_INCLUDED);
			int grace = systemParameterBean.getIntValue(SystemParameter.GRACE_VALUE);
			int freeInclusion = systemParameterBean.getIntValue(SystemParameter.FREE_INCLUSION);
			double packageCost = systemParameterBean.getDoubleValue(SystemParameter.PACKAGE_COST);

			// Initial calculation
			int adjustedCount = longCount - totalAdjustment;
			int chargableCount = adjustedCount - freeInclusion;

			// Check long + short rate
			int basicPacks = (int) Math.ceil((double) chargableCount / (double) longIncl);

			// Excessive short count - goes to -> long count
			double packShortRate = (double) shortIncl / (double) longIncl;
			double actualShortRate = (double) shortCount / (double) longCount;

			int excessiveShortUsage = 0;
			if (actualShortRate > packShortRate) {
				excessiveShortUsage = shortCount - basicPacks * shortIncl;
				if (excessiveShortUsage > 0) {
					chargableCount += excessiveShortUsage;
				}
			}

			// Final bill with grace logic
			boolean graceApplied;
			int chargedCount;
			int finalPacks;

			int noGracePacks = (int) Math.ceil((double) chargableCount / (double) longIncl);

			int graceChargeableCount = chargableCount - grace;
			int gracePacks = (int) Math.ceil((double) graceChargeableCount / (double) longIncl);

			if (gracePacks < noGracePacks) {
				graceApplied = true;
				chargedCount = graceChargeableCount;
				finalPacks = gracePacks;
			} else {
				graceApplied = false;
				finalPacks = noGracePacks;
				chargedCount = chargableCount;
			}

			double finalCharge = finalPacks * packageCost;

			ClientSummary sum = new ClientSummary();
			sum.setChargedCount(chargedCount);
			sum.setGraceApplied(graceApplied);
			sum.setFinalPacks(finalPacks);
			sum.setFinalCharge(finalCharge);

			return sum;
		}

		return null;
	}
}
