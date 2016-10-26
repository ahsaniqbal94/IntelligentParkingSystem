package com.ned;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Request;
import org.apache.log4j.Logger;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.Index.Duplicates;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.SocketUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.hash.HashCode;
import com.google.gson.Gson;

@RestController
public class AccountController {

	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	private AccountRepository accountRepository;
	
	private static final Logger logger = Logger.getLogger(AccountController.class);
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/account", method=RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
		public @ResponseBody String handleFileUpload (@RequestParam("studentName") String studentName,
				@RequestParam("studentId") String studentId,
				@RequestParam("activeAccount") String activeAccount,
				@RequestParam("batch") String batch,
				@RequestParam("department") String department,
				@RequestParam("file") MultipartFile file,
				@RequestParam("rfid")String rfid,
				@RequestParam("sex")String sex,
				@RequestParam("vehicleNumber")String vehicleNumber,
				@RequestParam("vehicleType")String vehicleType,
				@RequestParam("cnicNo")String cnicNo,
				@RequestParam("accountType")String accountType,
				@RequestParam("rollNumber")String rollNumber){	
		Gson gson=new Gson();
		String newFileName = null;
		byte[] bytes = null;

		if(!file.isEmpty()){
			try {
				bytes=file.getBytes();
				String frontImageContentType=file.getContentType();
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				String myCreationDate=dateFormat.format(cal.getTime());
				
				newFileName=studentId.replace("-", "") +	myCreationDate.replace(":", "");
				newFileName=newFileName.replace("", "");
				newFileName=newFileName.replace("/", "");
					
				logger.info("pushing the rfid " + rfid + " into the database");
				

				Account account=new Account(studentName, accountType, studentId, activeAccount,
						department, rollNumber, batch, bytes, rfid, frontImageContentType, sex,
						vehicleType, vehicleNumber, cnicNo);
				
				mongoTemplate.indexOps("account").ensureIndex(new Index().on("studentId", Order.ASCENDING)
						.on("batch", Order.ASCENDING).unique(Duplicates.DROP));
				
				mongoTemplate.indexOps("account").ensureIndex(new Index().on("rfid", Order.ASCENDING).unique(Duplicates.DROP));
				
				try{
				mongoTemplate.save(account, "account");
				//accountRepository.save(account);
				
				}catch(DuplicateKeyException e){
					Map<String, Object> responseMap=new LinkedHashMap<String, Object>();
					responseMap.put("message","Account already exists with RFID:"+ rfid +" or Enrollment No.:" + studentId);
					return gson.toJson(responseMap);
				}
			} catch (IOException e) {
				Map<String, Object> responseMap=new LinkedHashMap<String, Object>();
				responseMap.put("message","You failed to upload " + newFileName + " => " + e.getMessage());
				return gson.toJson(responseMap);
			}
			
	}
		Map<String, Object> responseMap=new LinkedHashMap<String, Object>();
		responseMap.put("message","Account added successfully");
		
		if(!file.isEmpty()){
			responseMap.put("fileUploadStatus", "uploaded");
		}
		
		return gson.toJson(responseMap);
		
	}	// post /upload ends here
	
	
	
	
	@RequestMapping(value="/editaccount/", method=RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Map<String, Object> editAccount(@RequestParam("oldAccountId") String oldAccountId,
			@RequestParam("idType") String idType,
			@RequestParam("accountId") String newAccountId,
			@RequestParam("studentName") String studentName,
			@RequestParam("activeAccount") String activeAccount,
			@RequestParam("batch") String batch,
			@RequestParam("sex") String sex,
			@RequestParam("file") MultipartFile file,
			@RequestParam("vehicleType") String vehicleType,
			@RequestParam("department") String department,
			@RequestParam("rfid") String rfid,
			@RequestParam("studentId") String studentId,
			@RequestParam("vehicleNumber") String vehicleNumber,
			@RequestParam("cnic") String cnicNo,
			@RequestParam("rollNumber") String rollNumber){
		
			if(idType.equals("rfid")){
				
				Account fetchedAccount=mongoTemplate.findOne(new Query(Criteria.where("rfid").is(rfid)),
						Account.class,"account");
				
				// if there are fetched objects means we can modify and object is in fetchedObject
				if(fetchedAccount!=null){
					logger.info("fetchedAccount object is not null");
					String tempFetchedId=fetchedAccount.getId();
					
					
					Account updatedAccount;
					try {
						updatedAccount = new Account(
								studentName,
								fetchedAccount.getAccountType(),
								studentId,
								activeAccount,
								department,
								rollNumber,
								batch,
								file.getBytes(),
								rfid,
								file.getContentType(),
								sex,
								vehicleType,
								vehicleNumber,
								cnicNo
								);
					
						updatedAccount.setId(tempFetchedId);
						updatedAccount.setrfid(fetchedAccount.getrfid());
						logger.info("id before editing : " + tempFetchedId);
						
						//updated account saving
//						mongoTemplate.remove(fetchedAccount, "account");
						mongoTemplate.save(updatedAccount, "account");
						//now push into database and success
											
						Map<String, Object> responseMap=new LinkedHashMap<String, Object>();
						responseMap.put("message", "Value Updated Successfully");
						responseMap.put("account",updatedAccount);
						//System.out.println("returning");
						return responseMap;
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}else{
					logger.error("no registered rfid");
					Map<String, Object> responseMap=new LinkedHashMap<String, Object>();
					responseMap.put("message", "No account with the specified rfid");
					//System.out.println("returning");
					return responseMap;
					
				}
				
			}else{
				logger.info("have to push into database in else block");
			}
			return null;
		
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="/getaccountcode")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String getAccountCode(@RequestParam("studentIdCode") String studentIdCode,
			@RequestParam("batchCode") String batchCode){
				
				
				Account collectedAccounts=accountRepository.findByStudentId(studentIdCode);
				
				if(collectedAccounts!=null){
				
				logger.info("fetching account code for studentId and Batch " + studentIdCode + " " +
				batchCode);
					
				if (collectedAccounts.getBatch().toString().equals(batchCode)){
					return collectedAccounts.getId();
				}else{
					return "no such account";
				}
			} else {
				return "no such account";
			}
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/accountToDelete/")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public @ResponseBody Map<String, Object> deleteAccount(@RequestParam("accountToDelete") String accountToDelete){
		

		Account accountBeingDeleted=mongoTemplate.findOne(new Query().addCriteria(Criteria.where("id").is(accountToDelete)), Account.class,"account");
		
		if(accountBeingDeleted!=null){
			
			accountRepository.delete(accountToDelete);
			Map<String, Object> response= new HashMap<String, Object>();
			response.put("message", "Account deleted successfully against the code " + accountToDelete + " with following info :");
			response.put("studentName", accountBeingDeleted.getStudentName());
			response.put("studentId", accountBeingDeleted.getStudentId());
			response.put("batch", accountBeingDeleted.getBatch());
			response.put("department", accountBeingDeleted.getDepartment());
			
			return response;
			
		} else {
			Map<String, Object> response= new HashMap<String, Object>();
			response.put("message", "Account cannot be deleted no account registered against the code : " + accountToDelete);
				
			return response;
		}
	}
	
	
	 @RequestMapping("/")
		@PreAuthorize("hasRole('ROLE_USER')")
	    public String index() {
	        return "<script>window.location = \"index.html\";</script>";
	    }


		@RequestMapping(method=RequestMethod.POST, value="/accountToDeleteWithRfid/")
		@PreAuthorize("hasRole('ROLE_ADMIN')")
		public @ResponseBody Map<String, Object> deleteAccountWithRfid(@RequestParam("accountToDelete") String accountToDelete){
			
			
			
			Account accountBeingDeleted=mongoTemplate.findOne(new Query().addCriteria(Criteria.where("rfid").is(accountToDelete)), Account.class,"account");
			if(accountBeingDeleted!=null){
				
				accountRepository.delete(accountBeingDeleted);
				Map<String, Object> response= new HashMap<String, Object>();
				response.put("message", "Account deleted successfully against the rfid " + accountToDelete + " with following info :");
				response.put("studentName", accountBeingDeleted.getStudentName());
				response.put("studentId", accountBeingDeleted.getStudentId());
				response.put("batch", accountBeingDeleted.getBatch());
				response.put("department", accountBeingDeleted.getDepartment());
				
				return response;
				
			} else {
				Map<String, Object> response= new HashMap<String, Object>();
				//	give back the response that the user has been deleted
				response.put("message", "Account cannot be deleted no account registered against the code : " + accountToDelete);
					
				return response;
				
			}
			
		}

	@RequestMapping(value="/getallaccount",method=RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Map<String, Object> getAllAccounts(HttpServletRequest request){

		String userIpAddress = request.getRemoteAddr();
        final String userAgent = request.getHeader("user-agent");
        String ipAddress = request.getHeader("X-FORWARDED-FOR");  
        
        if (ipAddress == null) {  
     	   ipAddress = request.getRemoteAddr();  
        }
        
        logger.info(ipAddress);
        logger.info(userIpAddress);
		
		Query query=new Query();
		
		List<Account> allAccounts=mongoTemplate.find(query, Account.class,"account");

		Map<String, Object> response= new LinkedHashMap<String, Object>();
		
		response.put("Total Accounts", allAccounts.size());
		response.put("Accounts", allAccounts);
		return response;
	}
	
	
	@RequestMapping(value="/getaccountinformation", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public @ResponseBody Map<String, Object> getAccountInformation(@RequestParam("studentid") String studentId,
			@RequestParam("batch") String batch,
			@RequestParam("rfid") String rfid){
		
		
		
		Query query1=new Query(Criteria.where("batch").is(batch).and("studentId").is(studentId));

		// find with batch and studentID
		
		List<Account> foundAccounts1=mongoTemplate.find(query1, Account.class,"account");
		
		if(foundAccounts1.size()>0){
		
		
		Map<String, Object> response= new LinkedHashMap<String, Object>();
		
		//find with rfid
		Query query2=new Query(Criteria.where("rfid").is(rfid));

		List<Account> foundAccounts2=mongoTemplate.find(query2, Account.class,"account");
		
		if(!(foundAccounts2.get(0).equals(foundAccounts1.get(0)))){
			logger.error("accounts not equal");
		}else{
			logger.info("accounts are equal ^-^");
		}
		
		response.put("Total_Accounts", foundAccounts1.size());
		response.put("Accounts", foundAccounts1);
		
		return response;
		
		}
		
		logger.error("Account not found against : " + rfid);
		return null;
		
	}
	
	@RequestMapping(value="/checkaccountsagainststudentid", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public @ResponseBody Map<String, Object> checkAccountsAgaiinstStudentId(@RequestParam("studentId") String studentId){
		Query query1=new Query(Criteria.where("studentId").is(studentId));
		query1.fields().include("batch").include("rfid");
		
		
		List<Account> foundAccounts1=mongoTemplate.find(query1, Account.class,"account");
		
		
		if(foundAccounts1.size()>0){
			Map<String, Object> response= new LinkedHashMap<String, Object>();
			response.put("batch", foundAccounts1.get(0).getBatch());
			response.put("rfid", foundAccounts1.get(0).getrfid());
			return response;
		}
		
		return null;
	}
	
	
	@RequestMapping(value="/togglesuspension", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public @ResponseBody Map<String, Object> toggleSuspension(@RequestParam("rfid") String rfid){
		
		logger.info("Toggling suspension status for rfid : " + rfid);
		Map<String, Object> response= new LinkedHashMap<String, Object>();
		Query newQuery=new Query(Criteria.where("rfid").is(rfid));
		
		Account foundAccount=mongoTemplate.findOne(newQuery, Account.class,"account");
		
		if(foundAccount!=null){
		String activeAccountStatus=	foundAccount.getActiveAccount();
			
			if(activeAccountStatus.equals("active")){
				foundAccount.setActiveAccount("suspended");
				response.put("message", rfid +  " now suspended");
				mongoTemplate.save(foundAccount, "account");
			} else	{
				foundAccount.setActiveAccount("active");
				response.put("message", rfid +  " now active");
				mongoTemplate.save(foundAccount, "account");
			}
			
		} else {
			response.put("message", "no account found against the rfid");
		}
		
		return response;
		
	} 

	
	
	
}
