package com.breezecom.tally.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client_summary")
public class ClientSummary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String clientId;

	private int year;

	private int month;

	private int shortCount;

	private int longCount;

	private int totalCount;

	private int totalAdjustment;

	private boolean graceApplied;

	private int chargedCount;

	private int finalPacks;

	private double finalCharge;

	@Override
	public String toString() {
		String toString = "ClientSummary:" + "\n" + "id = " + id + "\n" + "clientId = " + clientId + "\n" + "year = "
				+ year + "\n" + "month = " + month + "\n" + "count_short = " + shortCount + "\n" + "count_long = "
				+ longCount + "\n" + "total_count = " + totalCount + "\n" + "totalAdjustment = " + totalAdjustment
				+ "\n" + "graceApplied = " + graceApplied + "\n" + "chargedCount = " + chargedCount + "\n"
				+ "finalPacks = " + finalPacks + "\n" + "finalCharge = " + finalCharge + "\n" + "\n";

		return toString;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public int getTotalAdjustment() {
		return totalAdjustment;
	}

	public void setTotalAdjustment(int totalAdjustment) {
		this.totalAdjustment = totalAdjustment;
	}

	public boolean isGraceApplied() {
		return graceApplied;
	}

	public void setGraceApplied(boolean graceApplied) {
		this.graceApplied = graceApplied;
	}

	public int getChargedCount() {
		return chargedCount;
	}

	public void setChargedCount(int finalCount) {
		this.chargedCount = finalCount;
	}

	public int getFinalPacks() {
		return finalPacks;
	}

	public void setFinalPacks(int finalPacks) {
		this.finalPacks = finalPacks;
	}

	public double getFinalCharge() {
		return finalCharge;
	}

	public void setFinalCharge(double finalCharge) {
		this.finalCharge = finalCharge;
	}
}
