package com.safari.pg.cbsint.udr;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
public class GetTransactionInfoTests {
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
	public void getTransactionInfo() throws Exception {
		int transactionId = 1220175;
		assertNotNull(chInterface.getTransactionInfo(transactionId).getCashierId());
	}
	@Test
	public void getTransactionInfo_failure() throws Exception {
		int inValidTransactionId = 1000;
		assertNull(chInterface.getTransactionInfo(inValidTransactionId).getCurrency());
	}
	
//	@Test(expected=CbsInterfaceException.class)
//	public void getTransactionInfo_ThrowsException() throws Exception {
//		int inValidTransactionId = -1000;
//		chInterface.getTransactionInfo(inValidTransactionId);
//	}
}
