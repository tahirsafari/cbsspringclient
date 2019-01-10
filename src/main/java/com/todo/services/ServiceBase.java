/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.todo.services;

import com.safari.pg.util._UserAuthInfo;
import com.todo.cbs.CbsAgent;
import com.todo.cbs.CbsAuthInterface;
import com.todo.cbs.CbsDataAccessController;

/**
 *
 * @author Safarifone
 */
public class ServiceBase {
    
    CbsAgent ca;
    
    
    public ServiceBase() {
    	this.ca = new CbsAgent();
    	
    }
//    public void init(){
//        ca = new CbsAgent();
//    }
    public _UserAuthInfo process(String si){
    	CbsAuthInterface ai = new CbsAuthInterface(ca);
    	return ai.someAction(si);
    }
//    public void close(){
//        
//    }
}
