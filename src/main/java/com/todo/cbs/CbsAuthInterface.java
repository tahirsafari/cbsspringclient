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
public class CbsAuthInterface {
    
    CbsDataAccessController cac;
    
    public CbsAuthInterface (CbsAgent ca){        
        cac = new CbsDataAccessController(ca);
    }
    
    public _UserAuthInfo someAction(String input){
        
        return cac.db_get_User_AuthInfo_ByUserId(Integer.parseInt(input));
        
    }
}
