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
public class CreateMerchantOrderVendorResponseTests {
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
	public void createMerchantOrderVendorResponse() throws CbsInterfaceException {
		int orderId = 100;
		String issuerTranId = "100";
		String issuerApprovalCode = "Code";
		String issuerRespCode = "Code";
		int resultCodeInt = 1;
		String resultMsg = "resultMsg";
		String issuerTimestamp = "timeStamp";
        String cardType = "cardType";
        String cardholder = "cardholder";
        String f414 = "f414";
        String expDate = "2019/13/03";
        String desc = "description";
        double chargedAmount = 100;
        
		int result = this.chInterface.createMerchantOrderVendorResponse(orderId, issuerTranId, issuerApprovalCode, issuerRespCode, resultCodeInt, resultMsg, issuerTimestamp,
		        cardType, cardholder, f414, expDate, desc, chargedAmount);
		System.out.println("result "+result);
		assertTrue(result > 0);
	}
}
