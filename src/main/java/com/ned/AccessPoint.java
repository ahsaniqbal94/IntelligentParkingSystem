package com.ned;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class AccessPoint {
	
	@NotNull
	@Indexed(unique=true)
	private String ipAddress;
	
	@NotNull
	private String category;
	

	@Id
	private String id;

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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
		this.category = category;
	}

	
	
	@Override
	public String toString() {
		return "AccessPoint [ipAddress=" + ipAddress + ", category=" + category + ", id=" + id + "]";
	}

	public String toStringNoId() {
		return "AccessPoint [ipAddress=" + ipAddress + ", category=" + category + "]";
	}
	
	public String toStringWithBr() {
		return "ipAddress=" + ipAddress + ", category=" + category + "<br>";
	}
	
}
