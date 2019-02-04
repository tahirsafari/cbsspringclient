/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.todo.services;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.safari.pg.cbs.def._UserAuthInfo;
import com.safari.pg.cbs.services.sh.ServiceUdr;
import com.todo.SpringApplicationContext;
import com.safari.pg.cbsint.CbsAgent;
import com.safari.pg.cbsint.CbsBaseInterface;
import com.safari.pg.cbsint.CbsShInterface;
import com.safari.pg.cbs.dataaccess.CbsDataAccessController;
import com.safari.pg.cbs.def.CbsConstants;
import com.safari.pg.cbs.def.CbsException;
import com.safari.pg.cbs.def._MerchantFavCustomer;
import com.safari.pg.cbs.def._MerchantSetting;

/**
 *
 * @author Safarifone
 */
public class ServiceBase {
    
    CbsAgent ca;
    private JdbcTemplate jdbcTemplate;
    
    
    public ServiceBase() {
        SpringApplicationContext appContext = new SpringApplicationContext();
    	jdbcTemplate = appContext.getContext().getBean("jdbcTemplate",JdbcTemplate.class );

    	this.ca = new CbsAgent(jdbcTemplate);
    	
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
}
