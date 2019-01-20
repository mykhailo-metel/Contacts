package com.homework;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRepository extends MongoRepository<Contact, String> {
	Contact findBy_id(String _id);
}
