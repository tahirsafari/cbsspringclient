package com.safari.pg.cbsint.udr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
public class GetSettlementInfoByTranHeadIdTests {
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
	public void getSettlementInfoByTransId() throws Exception {
		int transactionId = 1220175;
		assertEquals(1220175, chInterface.getSettlementInfoByTranHeadId(transactionId).getRefTranHeadId());
	}
	@Test
	public void getSettlementInfoByTransId_failure() throws Exception {
		int inValidTransactionId = 1000;
		assertNotEquals(inValidTransactionId, chInterface.getSettlementInfoByTranHeadId(inValidTransactionId).getSettlementId());
	}
	
	@Test(expected=CbsInterfaceException.class)
	public void getSettlementInfoByTransId_ThrowsException() throws Exception {
		int inValidTransactionId = -1000;
		chInterface.getSettlementInfoByTranHeadId(inValidTransactionId);
	}

}
