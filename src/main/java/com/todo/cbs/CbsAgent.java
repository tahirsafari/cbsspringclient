/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.todo.cbs;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.todo.SpringApplicationContext;

/**
 *
 * @author Safarifone
 */
@Component
public class CbsAgent {

	
	private JdbcTemplate jdbcTemplate;
    public CbsAgent(){
    	SpringApplicationContext appContext = new SpringApplicationContext();
    	jdbcTemplate = appContext.getContext().getBean("jdbcTemplate",JdbcTemplate.class );

    }
    
    public Connection getConnection() throws SQLException {
        return jdbcTemplate.getDataSource().getConnection();
    }
    
}
