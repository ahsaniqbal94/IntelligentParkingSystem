package com.ned;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class slot {
	
	@NotNull
	@Indexed(unique=true)
	private String slotid;
	
	private String stat;
	
	private String timestamp;
	

	@Id
	private String id;

	public String getSlotid() {
		return slotid;
	}

	public void setSlotid(String slotid) {
		this.slotid = slotid;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public slot(String slot,String stat) {
		
		this.slotid=slot;
		this.setStat(stat);
		Date date= new Date();
	
		this.setTimestamp(new Timestamp(date.getTime()).toString());
	}
	
	public slot(String id,String slot,String stat,String timestamp) {
		
		this.id=id;
		this.slotid=slot;
		this.stat=stat;
		this.timestamp=timestamp;
	}
	public slot(){}
	public slot(String slot,String stat,String timestamp) {
		
		this.slotid=slot;
		this.stat=stat;
		this.timestamp=timestamp;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String toString()
	{
		return "slot [id=" + id + ", stat="+stat+ "]";
	}
	
	
}
