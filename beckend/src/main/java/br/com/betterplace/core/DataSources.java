package br.com.betterplace.core;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataSources {

    private SessionFactory sessionFactory;
    
    @Autowired
    public DataSources(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }
}