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
public class CreateTransactionTests {
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
	public void createTransaction() throws CbsInterfaceException {
		int tranTypeId = 1;
		int requestId = 1;
		int createdByUserId = 1;
		int PoSDevicedId = 1;
		String payerId = "123";
		String payerIdName = "playerName";
		int payerIdType = 1;
		int cashierId = 1;
		int merchantId = 1;
		int payerBankId = 1;
		int paymentMethodId = 100;
		int paymentChannelId = 1;
		double sxTranAmount = 1;
		String currency = "USD";
		String invoiceId = "123";
		String description = "description";
		double sxTranCharges = 12;
		int transtatusid = 1;
		int serviceId = 1;
		int chargeModeId = 1;
		double rxTranCharges = 1;
        String issuerTranId = "123";
        String pmtProcTranId = "123";
        int refTranId = 1;
        int sxUserId = 1;
        int sxUserTypeId = 1;
        int sxAccountId = 1;
        int rxUserId = 1;
        int rxUserTypeId = 1;
        int rxAccountId = 1;
        
		int result = this.chInterface.createTransaction(tranTypeId, requestId, createdByUserId, PoSDevicedId, payerId, payerIdName, payerIdType, cashierId, merchantId, payerBankId, paymentMethodId, paymentChannelId, sxTranAmount, currency, invoiceId, description, sxTranCharges, transtatusid, serviceId, chargeModeId, rxTranCharges, 
		           issuerTranId, pmtProcTranId,  refTranId, sxUserId, sxUserTypeId, sxAccountId, rxUserId, rxUserTypeId, rxAccountId);
		System.out.println("result "+result);
		assertTrue(result > 0);
	}
}
