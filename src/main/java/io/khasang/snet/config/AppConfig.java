package io.khasang.snet.config;

import io.khasang.snet.model.*;
import io.khasang.snet.service.PostService;
import io.khasang.snet.service.QueryHandler;
import io.khasang.snet.service.QuestionService;
import io.khasang.snet.service.UsersPasswordChanger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@PropertySource(value = {"classpath:util.properties"})
@PropertySource(value = {"classpath:auth.properties"})
@PropertySource(value = {"classpath:backup.properties"})
public class AppConfig {
    @Autowired
    Environment environment;

    @Bean
    public UserDetailsService userDetailsService() {
        JdbcDaoImpl jdbcImpl = new JdbcDaoImpl();
        jdbcImpl.setDataSource(dataSource());
        jdbcImpl.setUsersByUsernameQuery(environment.getRequiredProperty("usersByQuery"));
        jdbcImpl.setAuthoritiesByUsernameQuery(environment.getRequiredProperty("rolesByQuery"));
        return jdbcImpl;
    }

    @Bean
    CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setMaxUploadSize(1000000);
        return commonsMultipartResolver;
    }

    @Bean
    public Hello hello() {
        return new Hello("This is hello message");
    }

    @Bean
    public BackupBase backupBase(){
        return new BackupBase();
    }

    @Bean
    public DeleteTable deleteTable(){
        return new DeleteTable(jdbcTemplate());
    }

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("jdbc.postgresql.driverClass"));
        dataSource.setUrl(environment.getProperty("jdbc.postgresql.url"));
        dataSource.setUsername(environment.getProperty("jdbc.postgresql.username"));
        dataSource.setPassword(environment.getProperty("jdbc.postgresql.password"));
        return dataSource;
    }

    @Bean
    public QueryHandler queryHandler() {
        return new QueryHandler(jdbcTemplate());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

    @Bean
    public CreateTable createTable(){
        return new CreateTable(jdbcTemplate());
    }
    
    @Bean
    public SelectFromTable selectFromTable(){
        return new SelectFromTable(jdbcTemplate());
    }
    
    @Bean
    public TableConfiguration createTableEmployee(){
        return new TableConfiguration(jdbcTemplate());
    }

    @Bean
    public TruncateTable truncateTable() {
        return new TruncateTable(jdbcTemplate());
    }

    @Bean
    public UsersPasswordChanger usersPasswordChanger() {
        return new UsersPasswordChanger(queryHandler());
    }


    @Bean
    public PostService postService(){
        return new PostService();
    }


}
