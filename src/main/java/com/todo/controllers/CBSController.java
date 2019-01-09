package com.todo.controllers;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safari.pg.util.CbsException;
import com.safari.pg.util._UserAuthInfo;
import com.todo.DBManager;
import com.todo.DataAccessReplicaController;

@RestController
@RequestMapping("/cbs")
public class CBSController {

    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcTemplate template;
    @Autowired
    DBManager dataManager;
    private static final Logger logger = LogManager.getLogger(CBSController.class);
    
	@GetMapping(value = "/authenticate")
	public _UserAuthInfo authenticate() throws Exception, CbsException {
		DataAccessReplicaController controller = new DataAccessReplicaController();
            
                return controller.db_get_User_AuthInfo_ByUserId(template, 1000008);

	}
	
	@GetMapping(value = "/config")
	public boolean updateConfigParam() throws Exception, CbsException {
        
        DataAccessReplicaController controller = new DataAccessReplicaController();
        return controller.db_update_SysConfigParam(template, 1000008, 1, "P001", "Merchant_Reg_Monthly_Charges", "10");

	}
        
        @GetMapping(value = "/someendpoint")
	public boolean serviceMethod()  {
        
            //depending on some condition, ServiceX will be invoked
            Service1 obj = new Service1();
            String result = obj.process();
            
            ServiceBase srvcObj = null;
            if(1 = 1){
                srvcObj =new Service1();
            } else if (){
                srvcObj =new Service2();
            }
            
            srvcObj.process();
            return result;
            
       
	}
	
	
}
