package com.ned;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class slotController {

	
	@Autowired
	MongoTemplate mongoTemplate;
	
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/getslotstatus", method=RequestMethod.GET)

	@PreAuthorize("hasRole('ROLE_USER')")
	public @ResponseBody Map<String, Object> getSlotStatus( ){
		
		Map<String, Object> responseMap= new LinkedHashMap<String, Object>();
		

		Date date= new Date(System.currentTimeMillis()-1*60*1000);
		
		Timestamp ts=new Timestamp(date.getTime());
		//System.out.println(ts.toString());
		
		List<slot> slots1=mongoTemplate.findAllAndRemove(new Query().addCriteria(Criteria.where("timestamp").lte(ts.toString())), slot.class);
		responseMap.put("totaldeletedslots", slots1.size());
		
		responseMap.put("deletedslots", slots1);
		
		 List<slot> slots= mongoTemplate.findAll(slot.class, "slot");
		responseMap.put("totalResults",slots.size());
		responseMap.put("slots", slots);
		
		return responseMap;		
	}
	
	@RequestMapping(value="/setslot", method=RequestMethod.GET)

	@PreAuthorize("hasRole('ROLE_USER')")
	public String setslotStatus(
		@RequestParam("slotid") String slotid,@RequestParam("stat") String status){
		Query q=new Query();
		q.addCriteria(Criteria.where("slotid").is(slotid));
		
		slot slots= mongoTemplate.findOne(q, slot.class,"slot");
		slot slot1=new slot(slotid, status);
		if(slots == null){
			
			mongoTemplate.save(slot1, "slot");
		}
		else
		{
			slots.setStat(slot1.getStat());
			slots.setTimestamp(slot1.getTimestamp());
			mongoTemplate.save(slots,"slot");
		}
		

		
		return "OK";		
	}

	
}
