package com.ned;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "accesspoint", path = "accesspoint")
public interface AccessPointRepository extends MongoRepository<AccessPoint, String> {

	public List<AccessPoint> findAll();
	
	public AccessPoint findByIpAddress(String ipAddress);
	
	
	
}
