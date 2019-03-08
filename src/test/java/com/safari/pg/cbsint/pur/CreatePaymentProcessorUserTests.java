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
public class CreatePaymentProcessorUserTests {
	@Autowired
	JdbcTemplate jdbcTemplate;
	CbsAgent ca;
	CbsShInterface chInterface;
	
	@Before
	public void setup() throws CbsException, CbsInterfaceException {
		this.ca = new CbsAgent(jdbcTemplate);
    	this.chInterface = new  CbsShInterface(this.ca);
	}
	
	@Test
	public void createPaymentProcessorUser() throws CbsInterfaceException, CbsException {
		int cuserId = 100;
		int productId = 1;
		String fname = "fname";
		String mname = "mname";
		String lname = "lname";
		String email = "test@mail.com";
		String tel = "1234-1";
		String addr = "address";
		String addr2 = "address2";
		String userTitle = "titile";
		String loginId = "admin";
		String loginPwd = "admin";
		int userTypeId = 1;
		String city = "NY";
		String country = "USA";
		String website = "www.website.com";
		int pptypeid = 1;
		int systemId = 1;
		String currency = "USD";

		int  result = chInterface.createPaymentProcessorUser(cuserId,  fname, mname, lname, email, tel, addr, addr2, city, country, website, loginId, loginPwd, userTitle, userTypeId, pptypeid, currency, productId, systemId);
		System.out.println("result "+result);
		assertTrue(result > 0);
	}
	
	@Test(expected=CbsInterfaceException.class)
	public void createPaymentProcessorUser_ThrowsException() throws CbsInterfaceException, CbsException {
		int cuserId = 100;
		int productId = 1;
		String fname = "fname";
		String mname = "mname";
		String lname = "lname";
		String email = "test@mail.com";
		String tel = "1234-1";
		String addr = "address";
		String addr2 = "";
		String userTitle = "titile";
		String loginId = "admin";
		String loginPwd = "admin";
		int userTypeId = 1;
		String city = "NY";
		String country = "USA";
		String website = "www.website.com";
		int pptypeid = 1;
		int systemId = 1;
		String currency = "USD";

		int  result = chInterface.createPaymentProcessorUser(cuserId,  fname, mname, lname, email, tel, addr, addr2, city, country, website, loginId, loginPwd, userTitle, userTypeId, pptypeid, currency, productId, systemId);
		System.out.println("result "+result);
	}
}
