package com.safari.pg.cbsint.udr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
public class GetTranRoutingInfoTests {
	@Autowired
	JdbcTemplate jdbcTemplate;
	CbsAgent ca;
	CbsShInterface chInterface;
	
	@Before
	public void setup() throws CbsException {
		this.ca = new CbsAgent(jdbcTemplate);
    	this.chInterface = new  CbsShInterface(this.ca);
	}
	//Mapping class needs to be fixed. _PaymentProcessorInfoWithSystem
	@Test
	public void getTranRoutingInfo() throws Exception {
//		int transactionId = 1220175;
//		assertNotNull(chInterface.getTranRoutingInfo(transactionId).getBankId());
	}
	@Test
	public void getTranRoutingInfo_failure() throws Exception {
//		int inValidTransactionId = 1000;
//		assertNull(chInterface.getTranRoutingInfo(inValidTransactionId).getBankId());
	}
	
	@Test(expected=CbsInterfaceException.class)
	public void getTranRoutingInfo_ThrowsException() throws Exception {
		int inValidTransactionId = -1000;
		chInterface.getTranRoutingInfo(inValidTransactionId);
	}
}
