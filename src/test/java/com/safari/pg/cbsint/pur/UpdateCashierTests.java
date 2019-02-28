package com.safari.pg.cbsint.pur;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.safari.pg.cbs.def.CbsException;
import com.safari.pg.cbs.def.CbsInterfaceException;
import com.safari.pg.cbsint.CbsAgent;
import com.safari.pg.cbsint.CbsShInterface;
import com.todo.TodoApplication;

@RunWith(SpringRunner.class)
@ActiveProfiles(value="test")
@SpringBootTest(classes = TodoApplication.class)
@AutoConfigureWebMvc
public class UpdateCashierTests {
	@Autowired
	JdbcTemplate jdbcTemplate;
	CbsAgent ca;
	CbsShInterface chInterface;
	
	@Before
	public void setup() throws CbsException {
		this.ca = new CbsAgent(jdbcTemplate);
    	this.chInterface = new  CbsShInterface(this.ca);
	}
	
	@Test
	public void updateCashier() throws CbsInterfaceException, CbsException {
		int cuserId = 10;
		int cashierId = 10001;
		String fname ="fname";
		String mname ="mname"; 
		String lname = "lname"; 
		String email = "test@mail.com"; 
		String tel = "12345";
		String addr = "addr";
		String addr2 = "addr2"; 
		String city = "city"; 
		String country = "country"; 
		String website = "www.website.com";
		String userTitle = "userTitle";
		
		boolean  result = chInterface.updateCashier(cuserId, cashierId, fname, mname, lname, email, tel, addr,  addr2, city, country, website,  userTitle);
		System.out.println("result "+result);
		assertTrue(result);
	}
	
	@Test(expected=CbsInterfaceException.class)
	public void updateCashier_ThrowsException() throws CbsInterfaceException, CbsException {
		int cuserId = 10;
		int cashierId = 10001;
		String fname ="fname";
		String mname ="mname"; 
		String lname = "lname"; 
		String email = ""; 
		String tel = "12345";
		String addr = "addr";
		String addr2 = "addr2"; 
		String city = "city"; 
		String country = "country"; 
		String website = "www.website.com";
		String userTitle = "userTitle";
		
		boolean  result = chInterface.updateCashier(cuserId, cashierId, fname, mname, lname, email, tel, addr,  addr2, city, country, website,  userTitle);
		System.out.println("result "+result);
		assertTrue(result);
	}
}
