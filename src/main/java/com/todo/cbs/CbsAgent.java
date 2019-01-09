/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.todo.cbs;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import com.todo.DBManager;
import com.todo.SpringApplicationContext;

/**
 *
 * @author Safarifone
 */
@Component
public class CbsAgent {

	
	private DBManager dbManager;
    public CbsAgent(){
    	SpringApplicationContext appContext = new SpringApplicationContext();
    	dbManager = appContext.getContext().getBean("dbManager",DBManager.class );

    }
    
    public Connection getConnection() throws SQLException {
        return dbManager.getJdbcTemplate().getDataSource().getConnection();
    }
    
}
