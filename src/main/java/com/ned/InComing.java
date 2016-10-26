package com.ned;

import java.util.Arrays;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


public class InComing {

	
	@NotNull
	@NotEmpty
	private String rfid;
	
	@NotNull
	@NotEmpty
	private byte[] frontImageBytes;
	
	@NotNull
	@NotEmpty
	private String status;
	
	@NotNull
	@NotEmpty
	private String module;
	
	@NotNull
	@NotEmpty
	private String timeStamp;

	public String getRfid() {
		return rfid;
	}
	
	public void setRfid(String rfid) {
		this.rfid = rfid;
	}
	
	public byte[] getFrontImageBytes() {
		return frontImageBytes;
	}
	
	public void setFrontImageBytes(byte[] frontImageBytes) {
		this.frontImageBytes = frontImageBytes;
	}
	

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public InComing(String rfid, byte[] frontImageBytes, String status, String timeStamp, String module) {
		super();
		this.rfid = rfid;
		this.frontImageBytes = frontImageBytes;
		this.status = status;
		this.module = module;
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "InComing [rfid=" + rfid + ", frontImageBytes=" + Arrays.toString(frontImageBytes) + ", status=" + status
				+ ", module=" + module + ", timeStamp=" + timeStamp + "]";
	}

	
	
}
