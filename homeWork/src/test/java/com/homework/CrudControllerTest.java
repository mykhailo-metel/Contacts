package com.homework;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CrudControllerTest {

	private Contact c1 = new Contact("n1", "s1", "111", "adr1");
	private Contact c2 = new Contact("n2", "s2", "222", "adr2");
	private Contact c3 = new Contact("n3", "s3", "333", "adr3");
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	ContactRepository rep;
	
	@Before
	public void setUpBeforeClass() throws Exception {
		rep.deleteAll();
		assertNull(c1.get_id());
		assertNull(c2.get_id());
		assertNull(c3.get_id());
		rep.save(c1);
		rep.save(c2);
		rep.save(c3);
		assertNotNull(c1.get_id());
		assertNotNull(c2.get_id());
		assertNotNull(c3.get_id());
	}
	
	@Test
	public void contextLoads() {
		assertThat(rep).isNotNull();
		assertThat(restTemplate).isNotNull();
	}
	
	@Test
	public void testGetContactById() {
		List<Contact> contactList = rep.findAll(); 
		Contact c = contactList.get(0);
		Contact c1 = this.restTemplate.getForObject("http://localhost:" + port + "/contacts/" + c.get_id(), Contact.class);
		assert c.equals(c1);
	}

	@Test
	public void testGetAllContacts() {
		String response = this.restTemplate.getForObject("http://localhost:" + port + "/contacts", String.class);
		System.out.println(response);
		assertThat(response).isNotEqualTo("");
		ObjectMapper mapper = new ObjectMapper();
		Contact[] contactArray = null;
		try {
			contactArray = mapper.readValue(response, Contact[].class);
		} catch (IOException e) {
			e.printStackTrace();
			fail("exception in JSON parsing");
		}
		assertNotNull(contactArray);
		assert contactArray.length > 0;
		
		List<Contact> contactList = new ArrayList<Contact>(0);
		for (int i = 0; i < contactArray.length; i++) {
			contactList.add(contactArray[i]);
		}
		assert contactList.contains(c1);
		assert contactList.contains(c2);
		assert contactList.contains(c3);
	}
	
	@Test
	public void canIgetArrayFromRestTemplate() {
		Contact[] arr = restTemplate.getForObject("http://localhost:" + port + "/contacts", Contact[].class);
		assertNotNull(arr);
		assert arr.length > 0;
	}
	
	@Test
	public void canIgetListFromRestTemplate() {
		ResponseEntity<List<Contact>> response = restTemplate.exchange(
		  "http://localhost:"+ port+ "/contacts",
		  HttpMethod.GET,
		  null,
		  new ParameterizedTypeReference<List<Contact>>(){});
		List<Contact> contactList = response.getBody();
		assert contactList.contains(c1);
		assert contactList.contains(c2);
		assert contactList.contains(c3);
	}

	@Test
	public void testSaveNewContact() {
		String createUrl = "http://localhost:"+ port+ "/contacts";
		Contact c = new Contact("Bill", "Smith", "111-22-33", "addr");
		
		assert !rep.findAll().contains(c);
		
		HttpEntity<Contact> http = new HttpEntity<Contact>(c);
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> response = restTemplate.exchange(
		  createUrl,
		  HttpMethod.PUT,
		  http,
		  String.class);
		System.out.println(response.getBody());
		assert rep.findAll().contains(c);
	}

	@Test
	public void testUpdateExistingContact() {
		String url = "http://localhost:"+ port+ "/contacts";
		List<Contact> contactList = rep.findAll(); 
		Contact c = contactList.get(0);
		int n = contactList.size();
		assertNotNull(c);
		c.setName("aaa");
		c.setSurname("bbb");
		
		HttpEntity<Contact> http = new HttpEntity<Contact>(c);
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> response = restTemplate.exchange(
		  url,
		  HttpMethod.PUT,
		  http,
		  String.class);
		System.out.println(response.getBody());
		contactList = rep.findAll();
		assert contactList.contains(c);
		assert n == contactList.size();
		assert rep.findBy_id(c.get_id()).getSurname().equals("bbb");
	}

	@Test
	public void testDeleteContact() {
		List<Contact> contactList = rep.findAll(); 
		Contact c = contactList.get(0);
		assertNotNull(c);
		int n = contactList.size();
		
		String url = "http://localhost:" + port + "/contacts/" + c.get_id();
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.exchange(
		  url,
		  HttpMethod.DELETE,
		  null,
		  String.class);
		contactList = rep.findAll();
		assert !contactList.contains(c);
		assert contactList.size() ==  n - 1;
	}

}
