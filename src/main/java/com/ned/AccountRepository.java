package com.ned;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "account", path = "account")
public interface AccountRepository extends MongoRepository<Account, String> {
	
	public List<Account> findAll();
	public Account findByStudentId(@Param("studentid") String studentId);
	public List<Account> findByStudentName(@Param("studentname") String studentName);
	
	public List<Account> findByBatch(@Param("batch") String batch);

	public List<Account> findByRfid(@Param("rfid") String rfid);
	
}
