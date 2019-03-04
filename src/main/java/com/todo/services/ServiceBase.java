/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.todo.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.safari.pg.cbs.def._UserAuthInfo;
import com.safari.pg.cbs.services.sh.ServiceUdr;
import com.todo.SpringApplicationContext;
import com.todo.dtos.MerchantTransactionDto;
import com.todo.dtos.WebPermissionDto;
import com.safari.pg.cbsint.CbsAgent;
import com.safari.pg.cbsint.CbsBaseInterface;
import com.safari.pg.cbsint.CbsShInterface;
import com.safari.pg.cbs.dataaccess.CbsDataAccessController;
import com.safari.pg.cbs.def.CbsConstants;
import com.safari.pg.cbs.def.CbsException;
import com.safari.pg.cbs.def.CbsInterfaceException;
import com.safari.pg.cbs.def._AccountInfo;
import com.safari.pg.cbs.def._CashierInfo;
import com.safari.pg.cbs.def._MerchantFavCustomer;
import com.safari.pg.cbs.def._MerchantSetting;
import com.safari.pg.cbs.def._MerchantTransactionDetail;


/**
 *
 * @author Safarifone
 */
public class ServiceBase {
    
    CbsAgent ca;
    private JdbcTemplate jdbcTemplate;
    CbsShInterface chInterface;
    
    
    
    public ServiceBase() throws CbsInterfaceException, CbsException {
        SpringApplicationContext appContext = new SpringApplicationContext();
    	JdbcTemplate jdbcTemplate = appContext.getContext().getBean("jdbcTemplate",JdbcTemplate.class );

    	this.ca = new CbsAgent(jdbcTemplate);
    	this.chInterface = new  CbsShInterface(this.ca);
    	
    }
//    public void init(){
//        ca = new CbsAgent();
//    }
//    public _UserAuthInfo process(String si) throws CbsException{
//    	CbsAuthInterface ai = new CbsAuthInterface(ca);
//    	return ai.authenticate("1000008", "APIXTOIWEHSDLKOWERH", 0, CbsConstants.AUTHTYPE_USERID_ACCESSKEY);
//    }
//    public void close(){
//        
//    }
    
    public String mapService(String input) throws Exception {
    	CbsShInterface chInterface = new  CbsShInterface(this.ca);
    	//ServiceUdr service = new ServiceUdr(chInterface);
    	return null;//chInterface.mapService(input);
    }
    
    public _MerchantFavCustomer getMerchantSettingsWhenCodeIsNull(int  input) throws Exception {
    	CbsShInterface chInterface = new  CbsShInterface(this.ca);
    	//ServiceUdr service = new ServiceUdr(chInterface);
    	return chInterface.getMerchantFavCustomer(null, null);
    	//return null;
    }
    
    public _MerchantFavCustomer getMerchantSettingsWhenInputIsSmall(int  input) throws Exception {
    	CbsShInterface chInterface = new  CbsShInterface(this.ca);
    	//ServiceUdr service = new ServiceUdr(chInterface);
    	return chInterface.getMerchantFavCustomer(input, "Code");
    	//return null;
    }
   
    public _MerchantFavCustomer getMerchantSettingsWhenCodeIsEmpty(int  input) throws Exception {
    	CbsShInterface chInterface = new  CbsShInterface(this.ca);
    	//ServiceUdr service = new ServiceUdr(chInterface);
    	return chInterface.getMerchantFavCustomer(100, "");
    	//return null;
    }
    
    public _CashierInfo getCashierInfoByUserId(int  input) throws Exception {
    	CbsShInterface chInterface = new  CbsShInterface(this.ca);
    	//ServiceUdr service = new ServiceUdr(chInterface);
    	return chInterface.getCashierInfoByUserId(input);
    	//return null;
    }
    
    public List<_MerchantTransactionDetail> getMerchantTransactionsById(MerchantTransactionDto merchantDto) throws CbsInterfaceException, CbsException, ParseException {
    	CbsShInterface chInterface = new  CbsShInterface(this.ca);
    	SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy"); // New Pattern
        java.util.Date stdate = sdf1.parse(merchantDto.getStartDate()); // Returns a Date format object with the pattern
        java.sql.Date sqlStartDate = new java.sql.Date(stdate.getTime());
        java.util.Date enddate = sdf1.parse(merchantDto.getEndDate()); // Returns a Date format object with the pattern
        java.sql.Date sqlendDate = new java.sql.Date(enddate.getTime());
		return chInterface.getMerchantTransactionsById(merchantDto.getMerchantId(), merchantDto.getStatusId(), merchantDto.getRecordCount(), sqlStartDate, sqlendDate);
    	
    }
    public List<_MerchantSetting> getMerchantGroupSettings( int groupId) throws CbsInterfaceException{
    	return this.chInterface.getMerchantGroupSettings(groupId);
    	
    }
    public _AccountInfo getAccountInfo( int accountId) throws Exception{
    	return this.chInterface.getAccountInfo(accountId);
    	
    }
    public int createWebPermission(WebPermissionDto dto) throws Exception{
    	return this.chInterface.createWebPermission(dto.getUserId(), dto.getName(), dto.getDesc());
    	
    }
}
