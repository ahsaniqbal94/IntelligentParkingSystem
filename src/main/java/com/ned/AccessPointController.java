package com.ned;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.Index.Duplicates;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessPointController {

	
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	private AccessPointRepository accessPointRepository;
	
	private static final Logger logger = Logger.getLogger(AccessPointController.class);
	
	@RequestMapping(value="/getAllAccessPoints", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public @ResponseBody Map<String, Object> getAllAccessPoints(){
		
		Map<String, Object> responseMap= new LinkedHashMap<String, Object>();
		
		List<AccessPoint> accessPoints= mongoTemplate.findAll(AccessPoint.class, "accesspoints");
		
			if(accessPoints.isEmpty()){
				logger.info("not empty accesspoints");

				responseMap.put("totalAccounts",accessPoints.size() );
				
			}else{
				logger.info("accounts present");
				responseMap.put("totalAccounts",accessPoints.size() );
			
				String myString="";
				
				// replace zero by one
				
				for(int i=0; i<accessPoints.size();i++){
					myString=myString+accessPoints.get(i).getIpAddress()+ " and category is " +accessPoints.get(i).getCategory() + "<br>";
				}
				
				responseMap.put("stringAccounts",myString);
			}
		
		
		return responseMap;		
	}
	
	
	@RequestMapping(value="/addAccessPoints", method=RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public @ResponseBody Map<String, Object> addAccessPoint(@RequestParam("accessPoint") String accessPoint,@RequestParam("category") String category ){
		Map<String, Object> responseMap= new LinkedHashMap<String, Object>();
		System.out.println("here before try block");
		try{
			System.out.println("here");
			
			mongoTemplate.indexOps("accesspoints").ensureIndex(new Index().on("ipAddress", Order.ASCENDING)
					.unique(Duplicates.DROP));
			
			mongoTemplate.save(new AccessPoint(accessPoint,category),"accesspoints");
			responseMap.put("message", "data inserted is ip : " + accessPoint.toString() +" and categorized for "+ category);
			return responseMap;

		}catch(DuplicateKeyException e){

			responseMap.put("message", "the value couldn't be stored because given access point already exist");
			return responseMap;
		}
		
	}
	
	
	
	// /deleteAccessPoints
	@RequestMapping(value="/deleteAccessPoints", method=RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public @ResponseBody Map<String, Object> deleteAccessPoint(@RequestParam("accessPoint") String accessPoint ){

		Map<String, Object> responseMap=new LinkedHashMap<String, Object>();
		
		System.out.println(accessPoint);
		AccessPoint accountBeingDeleted=mongoTemplate.findOne(new Query().addCriteria(Criteria.where("ipAddress").is(accessPoint)), AccessPoint.class,"accesspoints");
		System.out.println("account being deleted : "+accountBeingDeleted);
		mongoTemplate.remove(new Query().addCriteria(Criteria.where("ipAddress").is(accessPoint)), AccessPoint.class, "accesspoints");
		
		System.out.println(mongoTemplate.getDb().getLastError());
		
		responseMap.put("message", "data deleted is ip : " + accessPoint.toString());
		return responseMap;
				
	}
	
}