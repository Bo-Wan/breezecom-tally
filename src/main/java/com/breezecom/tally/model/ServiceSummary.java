package com.breezecom.tally.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "service_summary")
@NamedQueries({
	@NamedQuery(name = "ServiceSummary.getTotalAdjustmentByServiceId", query = "SELECT SUM(adjustment) from ServiceSummary WHERE clientId = :clientId AND year = :year AND month = :month"),
})
public class ServiceSummary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String serviceId;
	
	private String did;
	
	private String clientId;
	
	private int year;
	
	private int month;
	
	private int shortCount;
	
	private int longCount;
	
	private int totalCount;
	
	private int adjustment;

	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getServiceId() {
		return serviceId;
	}



	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}



	public String getDid() {
		return did;
	}



	public void setDid(String did) {
		this.did = did;
	}



	public String getClientId() {
		return clientId;
	}



	public void setClientId(String clientId) {
		this.clientId = clientId;
	}



	public int getYear() {
		return year;
	}



	public void setYear(int year) {
		this.year = year;
	}



	public int getMonth() {
		return month;
	}



	public void setMonth(int month) {
		this.month = month;
	}



	public int getShortCount() {
		return shortCount;
	}



	public void setShortCount(int shortCount) {
		this.shortCount = shortCount;
	}



	public int getLongCount() {
		return longCount;
	}



	public void setLongCount(int longCount) {
		this.longCount = longCount;
	}



	public int getTotalCount() {
		return totalCount;
	}



	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}



	public int getAdjustment() {
		return adjustment;
	}



	public void setAdjustment(int adjustment) {
		this.adjustment = adjustment;
	}



	@Override
	public String toString() {
		String toString = "ServiceSummary:" + "\n"
			+ "id = " + id + "\n"
			+ "serviceId = " + serviceId + "\n"
			+ "did = " + did + "\n"
			+ "clientId = " + clientId + "\n"
			+ "year = " + year + "\n"
			+ "month = " + month + "\n"
			+ "count_short = " + shortCount + "\n"
			+ "count_long = " + longCount + "\n"
			+ "total_count = " + totalCount + "\n"
			+ "adjustment = " + adjustment + "\n\n";
		
		return toString;
	}
}
