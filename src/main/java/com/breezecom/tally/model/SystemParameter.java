package com.breezecom.tally.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "system_parameter")
public class SystemParameter {
	
	public static final String OCMS_API_ID_COUNT = "OCMS_API_ID_COUNT";
	public static final String SHORT_MESSAGE_KEY = "SHORT_MESSAGE_KEY";
	public static final String PACKAGE_COST = "PACKAGE_COST";
	public static final String LONG_MESSAGE_INCLUDED = "LONG_MESSAGE_INCLUDED";
	public static final String SHORT_MESSAGE_INCLUDED = "SHORT_MESSAGE_INCLUDED";
	public static final String GRACE_VALUE = "GRACE_VALUE";
	public static final String FREE_INCLUSION = "FREE_INCLUSION";
    
    @Id
    private String name;
    private String value;
    
    public SystemParameter() {
    }
 
    public SystemParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }
 
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
