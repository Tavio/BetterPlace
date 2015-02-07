package br.com.betterplace.core.configuration;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.service.ServiceRegistryBuilder;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:mongodb.properties")
public class DatasourceConfiguration {

    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    @Bean(name = "mysqlDataSource")
    public DataSource mysqlDataSource(
            @Value("${jdbc.url}") String url,
            @Value("${jdbc.username}") String username,
            @Value("${jdbc.password}") String password) {
        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
        ds.setDriverClassName(MYSQL_DRIVER);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setMaxActive(40);
        ds.setInitialSize(10);
        ds.setMaxIdle(30);
        ds.setMinIdle(5);
        ds.setJmxEnabled(true);
        ds.setTestWhileIdle(false);
        ds.setTestOnBorrow(true);
        ds.setValidationQuery("SELECT 1 FROM DUAL");
        ds.setTestOnReturn(false);
        ds.setValidationInterval(90000);
        ds.setTimeBetweenEvictionRunsMillis(90000);
        ds.setMaxWait(60000);
        ds.setRemoveAbandonedTimeout(120);
        ds.setMinEvictableIdleTimeMillis(75000);
        ds.setLogAbandoned(true);
        ds.setRemoveAbandoned(true);
        return ds;
    }

    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(@Qualifier("mysqlDataSource") DataSource ds) {
        Properties properties = new Properties();
        properties.put("hibernate.connection.datasource", ds);
        properties.put("transaction.factory_class", "org.springframework.orm.hibernate4.HibernateTransactionManager");
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure();
        configuration.addProperties(properties);
        ServiceRegistryBuilder builder = new ServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.buildServiceRegistry());
    }

    @Bean(name = "txManager")
    public PlatformTransactionManager getTxManager(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("mysqlDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean
    public DSLContext dslContext(DataSource ds) {
        return DSL.using(ds, SQLDialect.MYSQL);
    }

    @Bean
    public MongoClient getLocalMongoDB(@Value("${mongodb.hosts}") String mongodbHost) {
        try {
            String[] parts = mongodbHost.split(",");

            if (parts.length > 1) {
                return new MongoClient(
                        this.hostsStringToServerAddressList(parts));
            } else {
                return new MongoClient(
                        this.hostStringToServerAddress(mongodbHost));
            }
        } catch (UnknownHostException e) {
            return null;
        }
    }

    private List<ServerAddress> hostsStringToServerAddressList(String[] hosts) throws NumberFormatException, UnknownHostException {
        List<ServerAddress> hostList = new ArrayList<ServerAddress>();

        for (String host : hosts) {
            hostList.add(this.hostStringToServerAddress(host));
        }
        return hostList;
    }

    private ServerAddress hostStringToServerAddress(String host) throws NumberFormatException, UnknownHostException {
        String[] hostInfo = host.split(":");
        if (hostInfo.length > 1) {
            return new ServerAddress(hostInfo[0], Integer.parseInt(hostInfo[1]));
        } else {
            return new ServerAddress(host);
        }
    }
}