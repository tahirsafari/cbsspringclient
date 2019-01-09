package com.todo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DBManager {
	
	private static JdbcTemplate jdbcTemplate;
	
	public DBManager() {
		
	}
	
	public DBManager(JdbcTemplate jdbcTemplate) {
		setJdbcTemplate(jdbcTemplate);
	}
	
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
      if(DBManager.jdbcTemplate==null)
    	  DBManager.jdbcTemplate = jdbcTemplate;       
    }
    
    public JdbcTemplate getJdbcTemplate() {
    		return DBManager.jdbcTemplate;
      }

}
