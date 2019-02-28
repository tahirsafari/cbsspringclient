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
public class CreateAccountTests {
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
	public void createAccount() throws CbsInterfaceException, CbsException {
		int userId = 10;
		String accountNo = "C09122";
		int accountTypeId = 1;
		int createdByUserId = 10;
		String currency = "USD";

		int  result = chInterface.createAccount(userId, accountNo, accountTypeId, createdByUserId, currency);
		System.out.println("result "+result);
		assertTrue(result > 0);
	}
	
	@Test(expected=CbsInterfaceException.class)
	public void createAccount_ThrowsException() throws CbsInterfaceException, CbsException {
		int userId = 10;
		String accountNo = "";
		int accountTypeId = 1;
		int createdByUserId = 10;
		String currency = "USD";

		int  result = chInterface.createAccount(userId, accountNo, accountTypeId, createdByUserId, currency);
		System.out.println("result "+result);
	}
}
