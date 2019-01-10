/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.todo.cbs;

import com.safari.pg.util._UserAuthInfo;

/**
 *
 * @author Safarifone
 */
public class CbsShInterface {
    
     CbsDataAccessController cac;
    
    public CbsShInterface (CbsAgent ca){        
        cac = new CbsDataAccessController(ca);
    }
    
    public _UserAuthInfo someAction(String input){
//        
//        String dbresult = cac.someDbCall("input params");
//        
//        
//        String custom_obj = dbresult;
//        
//        return custom_obj;
    	return cac.db_get_User_AuthInfo_ByUserId(Integer.parseInt(input));
    }
}
