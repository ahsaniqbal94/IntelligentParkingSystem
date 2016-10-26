package com.ned;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class AccessPoint {
	
	@NotNull
	@Indexed(unique=true)
	private String ipAddress;
	
	

	@Id
	private String id;

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AccessPoint(String ipAddress, String category) {
		super();
		this.ipAddress = ipAddress;
	}

	
	

	
	public String toStringWithBr() {
		return "ipAddress=" + ipAddress + "<br>";
	}
	
}
