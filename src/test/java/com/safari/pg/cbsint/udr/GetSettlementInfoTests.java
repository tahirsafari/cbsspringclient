package com.safari.pg.cbsint.udr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

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
public class GetSettlementInfoTests {
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
	public void getSettlementInfo() throws Exception {
		int validSettlementId = 1111000;
		assertEquals(validSettlementId, chInterface.getSettlementInfo(validSettlementId).getSettlementId());
	}
	@Test
	public void getSettlementInfo_failure() throws Exception {
		int inValidSettlementId = 1000;
		assertNotEquals(inValidSettlementId, chInterface.getSettlementInfo(inValidSettlementId).getSettlementId());
	}
	
	@Test(expected=CbsInterfaceException.class)
	public void getSettlementInfo_ThrowsException() throws Exception {
		int inValidSettlementId = -1000;
		assertNotEquals(inValidSettlementId, chInterface.getSettlementInfo(inValidSettlementId).getSettlementId());
	}
}
