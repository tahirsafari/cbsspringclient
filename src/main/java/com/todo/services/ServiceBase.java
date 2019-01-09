/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.todo.services;

import com.safari.pg.util._UserAuthInfo;
import com.todo.cbs.CbsAgent;
import com.todo.cbs.CbsDataAccessController;

/**
 *
 * @author Safarifone
 */
public class ServiceBase {
    
    CbsAgent ca;
    CbsDataAccessController cbsController;
    
    
    public ServiceBase() {
    	this.ca = new CbsAgent();
    	cbsController = new CbsDataAccessController(this.ca);
    }
//    public void init(){
//        ca = new CbsAgent();
//    }
    public _UserAuthInfo process(String si){
    	return cbsController.db_get_User_AuthInfo_ByUserId(Integer.parseInt(si));
    }
//    public void close(){
//        
//    }
}
