package com.safari.pg.cbsint.udr;

import static org.junit.Assert.assertTrue;

import java.util.List;

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
import com.safari.pg.cbs.def._MerchantSetting;
import com.safari.pg.cbsint.CbsAgent;
import com.safari.pg.cbsint.CbsShInterface;
import com.todo.TodoApplication;

@RunWith(SpringRunner.class)
@ActiveProfiles(value="test")
@SpringBootTest(classes = TodoApplication.class)
@AutoConfigureWebMvc
public class GetMerchantGroupSettingsTests {
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
	public void getMerchantGroupSettings_success() throws CbsInterfaceException {
		int validGroupId = 2;
		List<_MerchantSetting> merchantSettings = chInterface.getMerchantGroupSettings(validGroupId);
		assertTrue(merchantSettings.size() > 0);
		
	}
	@Test
	public void getMerchantGroupSettings_InvalidGroupId() throws CbsInterfaceException {
		int validGroupId = 200;
		List<_MerchantSetting> merchantSettings = chInterface.getMerchantGroupSettings(validGroupId);
		assertTrue(merchantSettings.size() == 0);
		
	}
	
//	@Test(expected=CbsInterfaceException.class)
//	public void getMerchantGroupSettings_ThrowsException() throws CbsInterfaceException {
//		Integer validGroupId = -200;
//		chInterface.getMerchantGroupSettings(validGroupId);
//		
//	}
}
