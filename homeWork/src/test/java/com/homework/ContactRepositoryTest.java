package com.homework;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactRepositoryTest {

	private String testId;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Before
	public void setUpBeforeClass() throws Exception {
		contactRepository.deleteAll();
		Contact c1 = new Contact("n1", "s1", "111", "adr1");
		Contact c2 = new Contact("n2", "s2", "222", "adr2");
		Contact c3 = new Contact("n3", "s3", "333", "adr3");
		assertNull(c1.get_id());
		assertNull(c2.get_id());
		assertNull(c3.get_id());
		contactRepository.save(c1);
		contactRepository.save(c2);
		contactRepository.save(c3);
		testId = c1.get_id();
		assertNotNull(c1.get_id());
		assertNotNull(c2.get_id());
		assertNotNull(c3.get_id());
	}
	
	@Test
	public void testFindAll() {
		assert contactRepository.findAll().size()>2;
	}
	
	@Test
	public void testSave() {
		int n = contactRepository.findAll().size();
		Contact c = new Contact("nn","ss","123","adr1");
		contactRepository.save(c);
		assert contactRepository.findAll().size() == n+1;
	}

	@Test
	public void testFindBy_id() {
		assert contactRepository.findBy_id(testId).getName().equals("n1");
	}

	@Test
	public void testDeleteById() {
		Contact c = new Contact("nn","ss","123","adr1");
		contactRepository.save(c);
		String id = c.get_id();
		int n = contactRepository.findAll().size();
		contactRepository.deleteById(id);
		assert contactRepository.findAll().size() == n-1;
	}
	
	@After
	public void tearDown() {
		this.contactRepository.deleteAll();
	}

}
