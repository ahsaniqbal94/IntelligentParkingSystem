package com.ned;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "InComing", path = "InComing")
public interface InComingRepository extends MongoRepository<InComing, String> {

	public List<InComing> findAll();
	
	public List<InComing> findByRfid(@Param("rfid") String rfid);
	
	
}
