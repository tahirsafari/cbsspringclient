package com.todo;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;

import com.todo.DBManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


@Configuration
@ConditionalOnClass(DataSource.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@PropertySource("classpath:db.properties")
public class DBConfiguration {

	@Value("${db.mysql.driver}")
	private String dbDriver;
	@Value("${db.mysql.url}")
	private String dbUrl;
	@Value("${db.mysql.username}")
	private String dbUsername;
	@Value("${db.mysql.password}")
	private String dbPassword;
	

    @Bean
    @ConditionalOnProperty(name = "usemysql", havingValue = "local")
    @ConditionalOnMissingBean
    public DataSource dataSource() {
    	
	    HikariConfig hikariConfig = new HikariConfig();
	    hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    hikariConfig.setJdbcUrl(dbUrl); 
	    hikariConfig.setUsername(dbUsername);
	    hikariConfig.setPassword(dbPassword);

	    hikariConfig.setMaximumPoolSize(5);
	    hikariConfig.setConnectionTestQuery("SELECT 1");
	    hikariConfig.setPoolName("springHikariCP");
	    hikariConfig.addDataSourceProperty("datasource.maximumPoolSize", "20");

	    hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
	    hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
	    hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
	    hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");

	    HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        return dataSource;
    }
    
	  @Bean
	  @ConditionalOnBean(name = "dataSource")
	  @ConditionalOnMissingBean
	  public JdbcTemplate jdbcTemplate() {
		  return new JdbcTemplate(dataSource());
	  }
	  
	  @Bean
	  @ConditionalOnBean(name = "jdbcTemplate")
	  @ConditionalOnMissingBean
	  public DBManager dbManager() {
		  return new DBManager(jdbcTemplate());
	  }
}
