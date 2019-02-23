package com.todo.controllers;

import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safari.pg.cbs.def.CbsException;
import com.safari.pg.cbs.def.CbsInterfaceException;
import com.safari.pg.cbs.def._AccountInfo;
import com.safari.pg.cbs.def._CashierInfo;
import com.safari.pg.cbs.def._MerchantFavCustomer;
import com.safari.pg.cbs.def._MerchantSetting;
import com.safari.pg.cbs.def._MerchantTransactionDetail;
import com.safari.pg.cbs.def._UserAuthInfo;
import com.safari.pg.cbs.services.sh.ServiceUdr;
import com.todo.dtos.MerchantTransactionDto;
import com.todo.dtos.WebPermissionDto;
import com.todo.services.Service1;
import com.todo.services.ServiceBase;

@RestController
@RequestMapping("/cbs")
public class CBSController {

//    @Autowired
//    DataSource dataSource;
//    @Autowired
//    JdbcTemplate template;

    private static final Logger logger = LogManager.getLogger(CBSController.class);
    
  
//    @GetMapping(value = "/someendpoint")
//	public _UserAuthInfo serviceMethod() throws CbsException  {
//        
//        //depending on some condition, ServiceX will be invoked
//    	//Service1 obj = new Service1();
//        //String result = obj.process();
//            
//        ServiceBase srvcObj = null;
//        if(1 == 1){
//            srvcObj =new Service1();
//        } 
//        
//        _UserAuthInfo result = srvcObj.process("1000008");
//        return result;
//            
//       
//	}
	
    @GetMapping(value = "/code/null")
	public ResponseEntity<_MerchantFavCustomer> testService() throws Exception  {
    
        
		ServiceBase srvcObj = null;
		if(1 == 1){
			srvcObj =new Service1();
		}
		//String input = "{\"commandCode\":\"2001\",\"serviceParams\":{\"groupId\":\"20\"}}";
		String input = "{\"commandCode\":\"2002\",\"serviceParams\":{\"userId\":\"1000008\"}}";
		//String output =  srvcObj.mapService(input); 
		_MerchantFavCustomer list = srvcObj.getMerchantSettingsWhenCodeIsNull(100);
		return new ResponseEntity<_MerchantFavCustomer>(list, HttpStatus.OK); 
            
       
	}
	
    @GetMapping(value = "/group/small")
	public ResponseEntity<_MerchantFavCustomer> whenGroupIdIsSmall() throws Exception  {
    
        
		ServiceBase srvcObj = null;
		if(1 == 1){
			srvcObj =new Service1();
		}
		_MerchantFavCustomer list = srvcObj.getMerchantSettingsWhenInputIsSmall(4);
		return new ResponseEntity<_MerchantFavCustomer>(list, HttpStatus.OK); 
            
       
	}
	
    @GetMapping(value = "/code/empty")
	public ResponseEntity<_MerchantFavCustomer> whenCodeIsEmpty() throws Exception  {
    
        
		ServiceBase srvcObj = null;
		if(1 == 1){
			srvcObj =new Service1();
		}
		_MerchantFavCustomer list = srvcObj.getMerchantSettingsWhenCodeIsEmpty(100);
		return new ResponseEntity<_MerchantFavCustomer>(list, HttpStatus.OK); 
            
       
	}
	
    @GetMapping(value = "/cashier/userId/{userId}")
	public ResponseEntity<_CashierInfo> getCashierInfoByUserId(@PathVariable("userId") int userId) throws Exception  {
    
        
		ServiceBase srvcObj = null;
		if(1 == 1){
			srvcObj =new Service1();
		}
		_CashierInfo list = srvcObj.getCashierInfoByUserId(userId);
		return new ResponseEntity<_CashierInfo>(list, HttpStatus.OK); 
            
       
	}
	
    @RequestMapping(value = "/merchant/transation", method = RequestMethod.POST)
	public ResponseEntity<List<_MerchantTransactionDetail>> getMerchantTransactionsById(@RequestBody MerchantTransactionDto transactionDto) throws Exception  {
    	ServiceBase srvcObj = null;
		if(1 == 1){
			srvcObj =new Service1();
		}
		List<_MerchantTransactionDetail> list = srvcObj.getMerchantTransactionsById(transactionDto);
		return new ResponseEntity<List<_MerchantTransactionDetail>>(list, HttpStatus.OK); 
	}
    
//    @GetMapping(value = "/merchant/group/transactions/{groupId}")
//	public ResponseEntity<List<_MerchantSetting>> getMerchantGroupSettings(@PathVariable int groupId) throws Exception  {
//    	ServiceBase srvcObj = null;
//		if(1 == 1){
//			srvcObj =new Service1();
//		}
//		List<_MerchantSetting> list = srvcObj.getMerchantGroupSettings(groupId);
//		return new ResponseEntity<List<_MerchantSetting>>(list, HttpStatus.OK); 
//	}
//    
//    @GetMapping(value = "/merchant/group/transactions/{groupId}")
//	public ResponseEntity<_AccountInfo> getAccountInfo(@PathVariable int accountId) throws Exception  {
//    	ServiceBase srvcObj = null;
//		if(1 == 1){
//			srvcObj =new Service1();
//		}
//		_AccountInfo list = srvcObj.getAccountInfo(accountId);
//		return new ResponseEntity<_AccountInfo>(list, HttpStatus.OK); 
//	}
	
    @RequestMapping(value = "/create/webPermission", method = RequestMethod.POST)
	public ResponseEntity<Integer> createWebPermission(@RequestBody WebPermissionDto dto) throws Exception  {
    	ServiceBase srvcObj = null;
		if(1 == 1){
			srvcObj =new Service1();
		}
		int result = srvcObj.createWebPermission(dto);
		return new ResponseEntity<Integer>(result, HttpStatus.OK); 
	}
    
}
