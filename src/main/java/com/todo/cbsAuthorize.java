package com.todo;

import com.safari.pg.util.CbsException;
import com.todo.cbsServiceBase;
import com.todo.DataAccessController;
import com.safari.pg.util.CbsResponse;
import com.safari.pg.util.IxAuthorize;
import java.util.logging.Logger;

/**
 *
 * @author Safarifone
 */
public class cbsAuthorize extends cbsServiceBase {
    Logger logger = Logger.getLogger(cbsAuthorize.class.getName());    
    
    public cbsAuthorize(DataAccessReplicaController dbc){
        setDataAccessController(dbc);
    }
    
    CbsResponse process(IxAuthorize input) throws CbsException{
        CbsResponse res = new CbsResponse();
        
       
        
        return res;
    }  
    
}
