package com.ned;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@CompoundIndex(name = "studentID_batch _ind1", def = "{studentId: 1, batch: 1}", unique = true)
public class Account {
	
	@Id
	private String id;
	private String creationDate;
	@NotNull
	private String studentName;
	
	@NotNull
	private String accountType;

	// need compound index
	@Indexed(unique=true)
	private String studentId;
	@NotNull
	private String activeAccount;
	
	private String department;
	
	private String rollNumber;
	
	private String batch;
	@NotNull
	private byte[] frontImageBytes;
	
	@Indexed(unique=true)
	private String rfid;
	private String frontImageContentType;
	
	@NotNull 
	private String sex;
	
	@NotNull
	private String vehicleType;
	
	@NotNull
	private String vehicleNumber;
	
	private String cnic;
	
	
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Account(){}
	
	
	public Account(String creationDate, String studentName, String studentId, String activeAccount, String department,
			String batch, byte[] frontImageBytes, String rfid, String frontImageContentType, String sex,
			String vehicleType, String vehicleNumber) {
		super();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		this.creationDate=dateFormat.format(cal.getTime());
		this.studentName = studentName;
		this.studentId = studentId;
		this.activeAccount = activeAccount;
		this.department = department;
		this.batch = batch;
		this.frontImageBytes = frontImageBytes;
		this.rfid = rfid;
		this.frontImageContentType = frontImageContentType;
		this.sex = sex;
		this.vehicleType = vehicleType;
		this.vehicleNumber = vehicleNumber;
	}


	public Account(String studentName, String studentId, String activeAccount,
			String department, String batch, byte[] bytes, String rfid, String frontImageContentType , String sex,
			String vehicleType, String vehicleNumber, String accountType){
		this.studentName=studentName;
		this.studentId=studentId;
		this.activeAccount=activeAccount;
		this.department=department;
		this.batch=batch;
		this.frontImageBytes=bytes;
		this.rfid=rfid;
		this.frontImageContentType=frontImageContentType;
		this.sex=sex;
		this.vehicleType=vehicleType;
		this.vehicleNumber=vehicleNumber;
		
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		this.creationDate=dateFormat.format(cal.getTime());
		System.out.println(this.creationDate);	
		
	}
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getActiveAccount() {
		return activeAccount;
	}

	public void setActiveAccount(String activeAccount) {
		this.activeAccount = activeAccount;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public byte[] getFrontImageBytes() {
		return frontImageBytes;
	}

	public void setFrontImageBytes(byte[] frontImageBytes) {
		this.frontImageBytes = frontImageBytes;
	}


	public String getrfid() {
		return rfid;
	}

	public void setrfid(String rfid) {
		this.rfid = rfid;
	}

	public String getFrontImageContentType() {
		return frontImageContentType;
	}

	public void setFrontImageContentType(String frontImageContentType) {
		this.frontImageContentType = frontImageContentType;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	
	
	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}

	
	public String getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((activeAccount == null) ? 0 : activeAccount.hashCode());
		result = prime * result + ((batch == null) ? 0 : batch.hashCode());
		result = prime * result + ((cnic == null) ? 0 : cnic.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + Arrays.hashCode(frontImageBytes);
		result = prime * result + ((frontImageContentType == null) ? 0 : frontImageContentType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((rfid == null) ? 0 : rfid.hashCode());
		result = prime * result + ((rollNumber == null) ? 0 : rollNumber.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + ((studentId == null) ? 0 : studentId.hashCode());
		result = prime * result + ((studentName == null) ? 0 : studentName.hashCode());
		result = prime * result + ((vehicleNumber == null) ? 0 : vehicleNumber.hashCode());
		result = prime * result + ((vehicleType == null) ? 0 : vehicleType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (activeAccount == null) {
			if (other.activeAccount != null)
				return false;
		} else if (!activeAccount.equals(other.activeAccount))
			return false;
		if (batch == null) {
			if (other.batch != null)
				return false;
		} else if (!batch.equals(other.batch))
			return false;
		if (cnic == null) {
			if (other.cnic != null)
				return false;
		} else if (!cnic.equals(other.cnic))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (!Arrays.equals(frontImageBytes, other.frontImageBytes))
			return false;
		if (frontImageContentType == null) {
			if (other.frontImageContentType != null)
				return false;
		} else if (!frontImageContentType.equals(other.frontImageContentType))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (rfid == null) {
			if (other.rfid != null)
				return false;
		} else if (!rfid.equals(other.rfid))
			return false;
		if (rollNumber == null) {
			if (other.rollNumber != null)
				return false;
		} else if (!rollNumber.equals(other.rollNumber))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		if (studentId == null) {
			if (other.studentId != null)
				return false;
		} else if (!studentId.equals(other.studentId))
			return false;
		if (studentName == null) {
			if (other.studentName != null)
				return false;
		} else if (!studentName.equals(other.studentName))
			return false;
		if (vehicleNumber == null) {
			if (other.vehicleNumber != null)
				return false;
		} else if (!vehicleNumber.equals(other.vehicleNumber))
			return false;
		if (vehicleType == null) {
			if (other.vehicleType != null)
				return false;
		} else if (!vehicleType.equals(other.vehicleType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", creationDate=" + creationDate + ", studentName=" + studentName
				+ ", accountType=" + accountType + ", studentId=" + studentId + ", activeAccount=" + activeAccount
				+ ", department=" + department + ", rollNumber=" + rollNumber + ", batch=" + batch
				+ ", frontImageBytes=" + Arrays.toString(frontImageBytes) + ", rfid=" + rfid
				+ ", frontImageContentType=" + frontImageContentType + ", sex=" + sex + ", vehicleType=" + vehicleType
				+ ", vehicleNumber=" + vehicleNumber + ", cnic=" + cnic + "]";
	}

	public Account(  String studentName, String accountType, String studentId,
			String activeAccount, String department, String rollNumber, String batch, byte[] frontImageBytes,
			String rfid, String frontImageContentType, String sex, String vehicleType, String vehicleNumber,
			String cnic) {
		super();
		
		// add your own creation date
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		this.creationDate=dateFormat.format(cal.getTime());
		this.studentName = studentName;
		this.accountType = accountType;
		this.studentId = studentId;
		this.activeAccount = activeAccount;
		this.department = department;
		this.rollNumber = rollNumber;
		this.batch = batch;
		this.frontImageBytes = frontImageBytes;
		this.rfid = rfid;
		this.frontImageContentType = frontImageContentType;
		this.sex = sex;
		this.vehicleType = vehicleType;
		this.vehicleNumber = vehicleNumber;
		this.cnic = cnic;
		
	}

	private String toStringExceptImageContent() {
		return "Account [id=" + id + ", creationDate=" + creationDate + ", studentName=" + studentName
				+ ", accountType=" + accountType + ", studentId=" + studentId + ", activeAccount=" + activeAccount
				+ ", department=" + department + ", rollNumber=" + rollNumber + ", batch=" + batch
				+ ", rfid=" + rfid
				+ ", frontImageContentType=" + frontImageContentType + ", sex=" + sex + ", vehicleType=" + vehicleType
				+ ", vehicleNumber=" + vehicleNumber + ", cnic=" + cnic + "]";
	
	}

	
	
	
	
}
