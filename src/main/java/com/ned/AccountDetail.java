package com.ned;

import org.springframework.data.rest.core.config.Projection;

@Projection(name="allaccountdetail", types={Account.class} )
public interface AccountDetail {

	String getStudentName();
	String getActiveAccount();
	String getStudentId();
	String getBatch();
	String getrfid();
	
}
