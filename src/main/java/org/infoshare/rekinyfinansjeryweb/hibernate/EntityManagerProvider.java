package org.infoshare.rekinyfinansjeryweb.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class EntityManagerProvider {

    public static EntityManager get(){
        final EntityManagerFactory factory = Persistence.createEntityManagerFactory("test");
        return factory.createEntityManager();
    }

    public static void startTransaction(EntityManager entityManager){
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
    }

    public static void commitTransaction(EntityManager entityManager) {
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.commit();
    }
}