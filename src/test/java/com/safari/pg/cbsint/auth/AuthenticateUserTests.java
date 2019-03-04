package com.safari.pg.cbsint.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.safari.pg.cbs.def.CbsConstants;
import com.safari.pg.cbs.def.CbsException;
import com.safari.pg.cbs.def.CbsInterfaceException;
import com.safari.pg.cbs.def._UserAuthInfo;
import com.safari.pg.cbsint.CbsAgent;
import com.safari.pg.cbsint.CbsAuthInterface;
import com.todo.TodoApplication;

@RunWith(SpringRunner.class)
@ActiveProfiles(value="test")
@SpringBootTest(classes = TodoApplication.class)
@AutoConfigureWebMvc
public class AuthenticateUserTests {
	@Autowired
	JdbcTemplate jdbcTemplate;
	CbsAgent ca;
	CbsAuthInterface chInterface;
	
	@Before
	public void setup() throws CbsInterfaceException, CbsException {
		this.ca = new CbsAgent(jdbcTemplate);
    	this.chInterface = new  CbsAuthInterface(this.ca);
	}
	
	@Test
	public void authenticate_success() throws Exception {
		String accessId = "1000008";
		String password = "APIXTOIWEHSDLKOWERH";
		int channelId = 0;
		int authType = CbsConstants.AUTHTYPE_USERID_ACCESSKEY;
	
		_UserAuthInfo  user = chInterface.authenticate(accessId, password, channelId, authType);
		System.out.println("result "+user.toString());

	}
	
	@Test(expected=CbsInterfaceException.class)
	public void authenticate_ThrowsException() throws Exception {
		String accessId = "1000008";
		String invalidPassword = "APIXTOIWEHSDLKOWERHk";
		int channelId = 0;
		int authType = CbsConstants.AUTHTYPE_USERID_ACCESSKEY;
	
		_UserAuthInfo  user = chInterface.authenticate(accessId, invalidPassword, channelId, authType);
		System.out.println("result "+user.toString());

	}
}
