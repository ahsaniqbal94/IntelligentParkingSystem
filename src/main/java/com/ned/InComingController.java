package com.ned;



import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class InComingController  {
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	InComingRepository incomingRepository;
	private static final Logger logger = Logger.getLogger(AccountController.class);


	//status and timestamp too
	
	@RequestMapping(value="/incoming", method=RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_USER')")
	public @ResponseBody Map<String, Object> inComingVehicle (
			@RequestParam("file") MultipartFile file,
			@RequestParam("rfid")String rfid,
			@RequestParam("module")String module,
			@RequestParam("timestamp")String timestamp,
			@RequestParam("status")String status,
			HttpServletRequest request){	

		logger.info("incoming " + rfid);
		
		String userIpAddress = request.getRemoteAddr();
        final String userAgent = request.getHeader("user-agent");
        String ipAddress = request.getHeader("X-FORWARDED-FOR");  
        if (ipAddress == null) {  
     	   ipAddress = request.getRemoteAddr();  
        }
        
        logger.info(ipAddress);
        logger.info(userIpAddress);
	    
        AccessPoint accessingPoint= mongoTemplate.findOne(new Query(Criteria.where("ipAddress").is(ipAddress)), AccessPoint.class	, "accesspoints");
        
        
        
        // Means that correct ip is accessing the server
        if(accessingPoint!=null){
        
        	String newFileName = null;
    		InComing incomingVehicle;
    		byte[] bytes;
    		
    		try {
    			
    			bytes = file.getBytes();
    			
    			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    			Calendar cal = Calendar.getInstance();
    			String myCreationDate=dateFormat.format(cal.getTime());
    			
    			
    			
    			try{
    				
    				// Now check if the account exists with the current RFID
    				Query query=new Query();
    				query.addCriteria(Criteria.where("rfid").is(rfid));
    				
    				
    				System.out.println("querying : " + query);
    				Account checkAccount =mongoTemplate.findOne(query, Account.class,"account");
    				
    				
    				// check if it is intruder
    				// If the account is null, means no account is present with that rfid
    				// so the account is intruder
    				if(checkAccount==null){
    					
    					System.out.println("null returned");
    					Map<String, Object> myException=new LinkedHashMap<String, Object>();
    					incomingVehicle=new InComing(rfid,bytes,accessingPoint.getCategory(),myCreationDate,module);
    					
    					mongoTemplate.save(incomingVehicle, "Intruder");
    					myException.put("message", "found no such account in the database: INTRUDER");
    					return myException;
    					
    				
    				}else{
    					// now check if it is incoming after is account is active
    					
    					// check if account is active
    					if(checkAccount.getActiveAccount().equals("suspended")){
    						// if account is suspended
    						
    						incomingVehicle=new InComing(rfid,bytes,accessingPoint.getCategory(),timestamp, module);
							mongoTemplate.save(incomingVehicle, "Intruder");
							
							Map<String, Object> returnFromIncomingData=new LinkedHashMap<String, Object>();
							returnFromIncomingData.put("message", "Account is suspended: INTRUDER");
							return	returnFromIncomingData;
    						
    					} 
    					
    				    if(accessingPoint.getCategory().equals("incoming")){
    						
    								System.out.println("User : " + checkAccount.getStudentName().toString()
    										+	" is " + accessingPoint.getCategory().toString() + " module : " +
    										module);
    									System.out.println("Account exist,,,,,,\nuploading file");

    									newFileName=myCreationDate.replace(":", "");
    									newFileName=newFileName.replace("", "");
    									newFileName=newFileName.replace("/", "");
    									
    									
//    									incomingVehicle=new InComing(rfid,bytes,accessingPoint.getCategory(),myCreationDate,module);
//    									mongoTemplate.save(incomingVehicle, accessingPoint.getCategory() + "Vehicle");
    						
    									incomingVehicle=new InComing(rfid,bytes,accessingPoint.getCategory(),timestamp,module);
    									mongoTemplate.save(incomingVehicle, accessingPoint.getCategory() + "Vehicle");
    						
    									
    									Map<String, Object> returnFromIncomingData=new LinkedHashMap<String, Object>();
    									returnFromIncomingData.put("message", "file uploaded");
    									returnFromIncomingData.put("user", checkAccount.getStudentName());
    									returnFromIncomingData.put("status", "Incoming vehicle");
    									returnFromIncomingData.put("module", module);
    									returnFromIncomingData.put("timestamp", timestamp);
    									return	returnFromIncomingData;
    									
    									
    						// Now see if the account is outgoing				
    						}else if(accessingPoint.getCategory().equals("outgoing")){ 
    							
    							//now take out the last entry of the vehicle which would have prev
    							// entered the parking lot
    							
    							Query queryLatestIncomingEntry=new Query();
    							queryLatestIncomingEntry.fields().exclude("frontImageBytes");
    							queryLatestIncomingEntry.addCriteria(Criteria.where("rfid").is(rfid));
    							queryLatestIncomingEntry.with(new Sort(Sort.Direction.DESC,"_id"));
    							queryLatestIncomingEntry.limit(1);
    							
    							
    							List<InComing> incomingLatestVehicle=mongoTemplate.find(queryLatestIncomingEntry, InComing.class, "incomingVehicle");
    							
    							
    							System.out.println("query executed");
    							
    							
    							if(incomingLatestVehicle.size()>0){
    								
    								// means that the incoming vehicle is present
    								System.out.println("checking when the last time vehicle came in");
    								String lastInfo=incomingLatestVehicle.get(0).toString();
    								
    								
    								System.out.println("last time it came : "+ lastInfo);
    								
    								
    								// take out the day
    								System.out.println("myCreationDate : " + myCreationDate);
    								
    								
    								//TODO adjust date from substring here
    								int myCreationDateDay = 0;
    								int incomingLatestVehicleDay = 0;
    								
    								try{
    								System.out.println(myCreationDate);
    								//8,10
    								myCreationDateDay=Integer.parseInt(myCreationDate.substring(7, 10));
    								System.out.println(myCreationDateDay);
    								
    								System.out.println(incomingLatestVehicle.get(0).getTimeStamp());
    								incomingLatestVehicleDay=Integer.parseInt(incomingLatestVehicle.get(0).getTimeStamp().substring(8, 10));
    								System.out.println(incomingLatestVehicleDay);
    								}catch(NumberFormatException e){
    									System.out.println(e);
    								}
    								
    								// if day isn't same, alert a suspicion
    								if(myCreationDateDay!=incomingLatestVehicleDay){
    									System.out.println("This vehicle didn't enter today : SUSPICION");
    								}
    								
    								
    								System.out.println("User : " + checkAccount.getStudentName().toString()
    	    								+	" is " + accessingPoint.getCategory().toString() + " module : " +
    										module);
    								
    	    							System.out.println("Account exist,,,,,,\nuploading file");

    	    							newFileName=myCreationDate.replace(":", "");
    	    							newFileName=newFileName.replace("", "");
    	    							newFileName=newFileName.replace("/", "");
    	    							
    	    							
    	    							incomingVehicle=new InComing(rfid,bytes,accessingPoint.getCategory(),timestamp, module);
    	    							mongoTemplate.save(incomingVehicle, accessingPoint.getCategory() + "Vehicle");
    	    							
    	    							Map<String, Object> returnFromIncomingData=new LinkedHashMap<String, Object>();
    	    							returnFromIncomingData.put("message", "file uploaded");
    	    							returnFromIncomingData.put("user", checkAccount.getStudentName());
    	    							returnFromIncomingData.put("status", "Outgoing vehicle");
    	    							returnFromIncomingData.put("timestamp", myCreationDate);
    	    							returnFromIncomingData.put("module", module);
    	    							returnFromIncomingData.put("Last entered", incomingLatestVehicle.get(0).getTimeStamp());
    	    							return	returnFromIncomingData;
    								
    							}else{	
    								
    								// when the vehicle never came in
    								incomingVehicle=new InComing(rfid,bytes,accessingPoint.getCategory(),timestamp, module);
    								mongoTemplate.save(incomingVehicle, "Intruder");
    								
	    							Map<String, Object> returnFromIncomingData=new LinkedHashMap<String, Object>();
	    							returnFromIncomingData.put("message", "it never came in, suspicion");
	    							return	returnFromIncomingData;
    							}
	
    							}else{
    								Map<String, Object> myException=new LinkedHashMap<String, Object>();
    								incomingVehicle=new InComing(rfid,bytes,accessingPoint.getCategory(),timestamp, module);
    								mongoTemplate.save(incomingVehicle, "Intruder");
    								myException.put("message", "No incoming or outgoing, account"
    										+ " exists : SUSPICION");
    								return myException;
    								
    							}
// here
    				    
    				    
    				}
    			}catch(EmptyResultDataAccessException e){

    				Map<String, Object> myException=new LinkedHashMap<String, Object>();
    				myException.put("message", "found nothing in the database: INTRUDER");
    				return myException;
	
    			}
    			

    		} catch (IOException e) {
    			Map<String, Object> responseMap=new LinkedHashMap<String, Object>();
    			responseMap.put("message","You failed to upload " + newFileName + " => " + e.getMessage());
    			return responseMap;
    		}
        	
        }else{
        	logger.error("Ip trying to access is Intruder, the IP is : " + ipAddress);
        	Map<String, Object> responseMap=new LinkedHashMap<String, Object>();
			responseMap.put("message","Ip trying to access is Intruder, the IP is : " + ipAddress);
			return responseMap;
        }
		
		
	}
	
	@RequestMapping(value="/getallincoming",method=RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	public Map<String, Object> getAllAccounts(){

		logger.info("get all accounts");
		
		
		Query query=new Query();
		List<InComing> allAccounts=mongoTemplate.find(query, InComing.class,"incomingVehicle");
		Map<String, Object> response= new LinkedHashMap<String, Object>();
		response.put("Total Accounts", allAccounts.size());
		response.put("Accounts", allAccounts);
		return response;
	}

	
	@RequestMapping(value="/fetchallinoutrecordsexcludingimages", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public @ResponseBody Map<String, Object> fetchAllInOutRecordsExcludingImages(@RequestParam("rfid") String rfid){
	
		logger.info("Fetch All In Out Records Excluding Image");
		
		Query inRecordQuery=new Query();
		inRecordQuery.addCriteria(Criteria.where("rfid").is(rfid));
		inRecordQuery.fields().exclude("frontImageBytes");
		inRecordQuery.fields().exclude("status");
		inRecordQuery.fields().exclude("rfid");
		
		List<InComing> allIncomingAccounts=mongoTemplate.find(inRecordQuery, InComing.class,"incomingVehicle");
		List<InComing> allOutgoingAccounts=mongoTemplate.find(inRecordQuery, InComing.class,"outgoingVehicle");
		
		Map<String, Object> response= new LinkedHashMap<String, Object>();
		response.put("TotalIncomingAccounts", allIncomingAccounts.size());
		response.put("IncomingAccounts", allIncomingAccounts);
		response.put("TotalOutgoingAccounts", allOutgoingAccounts.size());
		response.put("OutgoingAccounts", allOutgoingAccounts);
		
		return response;
		
	}
	

	@RequestMapping(value="/fetchinoutimages", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public @ResponseBody Map<String, Object> fetchInOutImages(@RequestParam("inout") String inOut,
			@RequestParam("datetime") String dateTime){
	
		logger.info("Fetch In Out Images Executed");
		
		
		Map<String, Object> response= new LinkedHashMap<String, Object>();
		
		if(inOut.equals("in")){
			Query inRecordQuery1=new Query();
			inRecordQuery1.addCriteria(Criteria.where("timeStamp").is(dateTime));
		
			Query inRecordQuery2=new Query();
			inRecordQuery2.addCriteria(Criteria.where("timeStamp").is(dateTime));
			
			inRecordQuery1.addCriteria(Criteria.where("module").is("reader"));
			List<InComing> allIncomingAccountsReaderModule=mongoTemplate.find(inRecordQuery1, InComing.class,"incomingVehicle");
			inRecordQuery2.addCriteria(Criteria.where("module").is("camera"));
			List<InComing> allIncomingAccountsCameraModule=mongoTemplate.find(inRecordQuery2, InComing.class,"incomingVehicle");
			
			response.put("TotalAccountsReaderModule", allIncomingAccountsReaderModule.size());
			response.put("AccountsReaderModule", allIncomingAccountsReaderModule);
			response.put("TotalAccountsCameraModule", allIncomingAccountsCameraModule.size());
			response.put("AccountsCameraModule", allIncomingAccountsCameraModule);
			
		}else if(inOut.equals("out")){
			Query inRecordQuery1=new Query();
			inRecordQuery1.addCriteria(Criteria.where("timeStamp").is(dateTime));

			Query inRecordQuery2=new Query();
			inRecordQuery2.addCriteria(Criteria.where("timeStamp").is(dateTime));
			
			
			inRecordQuery1.addCriteria(Criteria.where("module").is("reader"));
			List<InComing> allOutgoingAccountsReaderModule=mongoTemplate.find(inRecordQuery1, InComing.class,"outgoingVehicle");
			
			if(allOutgoingAccountsReaderModule!=null){
				inRecordQuery2.addCriteria(Criteria.where("module").is("camera"));
				List<InComing> allOutingAccountsCameraModule=mongoTemplate.find(inRecordQuery2, InComing.class,"outgoingVehicle");
				
				if(allOutingAccountsCameraModule!=null){
					response.put("TotalAccountsReaderModule", allOutgoingAccountsReaderModule.size());
					response.put("AccountsReaderModule", allOutgoingAccountsReaderModule);
					response.put("TotalAccountsCameraModule", allOutingAccountsCameraModule.size());
					response.put("AccountsCameraModule", allOutingAccountsCameraModule);
				
				} else {
					response.put("TotalAccountsReaderModule", allOutgoingAccountsReaderModule.size());
					response.put("AccountsReaderModule", allOutgoingAccountsReaderModule);
					response.put("TotalAccountsCameraModule", 0);
					response.put("AccountsCameraModule", null);
					
				}
				
			}	// if allOutGoingAccountsReaderModule ends here 
			else{	// if no reader module
				
				inRecordQuery2.addCriteria(Criteria.where("module").is("camera"));
				List<InComing> allOutingAccountsCameraModule=mongoTemplate.find(inRecordQuery2, InComing.class,"outgoingVehicle");
				
				if(allOutingAccountsCameraModule!=null){
				
					response.put("TotalAccountsReaderModule", 0);
					response.put("AccountsReaderModule", null);
					response.put("TotalAccountsCameraModule", allOutingAccountsCameraModule.size());
					response.put("AccountsCameraModule", allOutingAccountsCameraModule);
					
				} else {
					
					response.put("TotalAccountsReaderModule", 0);
					response.put("AccountsReaderModule", null);
					response.put("TotalAccountsCameraModule", 0);
					response.put("AccountsCameraModule", null);
				
				}
			}
		}
		
		return response;
		
	}

	
}