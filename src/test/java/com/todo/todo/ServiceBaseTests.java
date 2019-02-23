package com.todo.todo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.safari.pg.cbs.def._CashierInfo;
import com.todo.services.ServiceBase;

@RunWith(MockitoJUnitRunner.class)
public class ServiceBaseTests {

	private ServiceBase serviceBase;
	@Before
	public void init() {
		serviceBase = mock(ServiceBase.class);
	}
	@Test
	public void getCashierInfoByUserId() throws Exception {
		_CashierInfo cashierInfo = new _CashierInfo();
		cashierInfo.setAccessProfileId(1200);
		when(serviceBase.getCashierInfoByUserId(10)).thenReturn(cashierInfo);
		assertEquals(cashierInfo, serviceBase.getCashierInfoByUserId(10));
		
	}
}
