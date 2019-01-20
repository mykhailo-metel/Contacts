package com.homework;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contacts")
public class CrudController {
	
	@Autowired
	private ContactRepository repository;
	
	@GetMapping
	public List<Contact> getAllContacts() {
		return repository.findAll();
	}
	
	@PutMapping
	public void saveOrUpdateContact(@RequestBody Contact contact) {
		repository.save(contact);
	}
	
	@GetMapping("/{id}")
	public Contact getContact(@PathVariable String id) {
		return repository.findBy_id(id);
	}
	
	@DeleteMapping("/{id}")
	public void deleteContact(@PathVariable String id) {
		repository.deleteById(id);
	}

}
