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
public class CreateMerchantOrderTests {
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
	public void createMerchantOrder() throws CbsInterfaceException {
		int merchantId = 1;
		int cashierId = 1;
		String referenceId = "123";
		String invoiceId = "123";
		double amount = 12;
		String currency = "USD";
		int createdByUserId = 1;
		int requestId = 100;
		String serviceCode = "120";
		String tranDesc = "desc";
		int statusId = 1;
		int pmtMethodId = 1;
		String scburl = "scbUrl";
		String fcburl = "fcbUrl";
		int hppRespDataFormatId = 1;
        
		int result = this.chInterface.createMerchantOrder(merchantId, cashierId, referenceId, invoiceId, amount, currency, createdByUserId, requestId, serviceCode, tranDesc, statusId, pmtMethodId, scburl, fcburl, hppRespDataFormatId);
		System.out.println("result "+result);
		assertTrue(result > 0);
	}
}
