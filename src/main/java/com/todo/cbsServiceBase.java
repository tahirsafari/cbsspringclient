package com.todo;

import com.todo.DataAccessController;

/**
 *
 * @author Safarifone
 */
public class cbsServiceBase {
    public DataAccessReplicaController dbc = null;
     
    public cbsServiceBase() {
      
    }
    
    public void setDataAccessController(DataAccessReplicaController dbc2){
        this.dbc=dbc2;        
    }
}
