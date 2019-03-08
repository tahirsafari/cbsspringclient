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
public class UpdateBankTests {
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
	public void updateBank() throws CbsInterfaceException, CbsException {
		String bankName ="bankName";
		String bankCode ="bankCode"; 
		String addr = "addr";
		int ppid = 1;
		int bankId = 1014;

		boolean  result = chInterface.updateBank(bankId, bankName, bankCode, addr, ppid);
		System.out.println("result "+result);
		assertTrue(result);
	}
	
//	@Test(expected=CbsInterfaceException.class)
//	public void updateBank_ThrowsException() throws CbsInterfaceException, CbsException {
//		String bankName ="bankName";
//		String bankCode ="bankCode"; 
//		String addr = "";
//		int ppid = 1;
//		int bankId = 1014;
//
//		boolean  result = chInterface.updateBank(bankId, bankName, bankCode, addr, ppid);
//		System.out.println("result "+result);
//	}
}
