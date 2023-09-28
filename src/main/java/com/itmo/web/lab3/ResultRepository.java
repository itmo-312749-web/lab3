package com.itmo.web.lab3;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Named
@SessionScoped
//@Transactional
public class ResultRepository implements Serializable {

    private EntityManager entityManager;
    private UUID lastInserted = null;

    @PostConstruct
    public void init() {
        //TODO: make safer
         entityManager = Persistence
                 .createEntityManagerFactory("default")
                 .createEntityManager();
        String filename = System.getenv("JBOSS_HOME") + "/standalone/data/last-inserted.txt";
         try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filename)))) {
             lastInserted = UUID.fromString(scanner.nextLine());
         } catch (FileNotFoundException e) {
            //TODO: logging
         }
    }

    @PreDestroy
    private void destroy() {
        entityManager.close();
        String filename = System.getenv("JBOSS_HOME") + "/standalone/data/last-inserted.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(String.valueOf(lastInserted));
        } catch (IOException e) {
            //TODO: logging
        }
    }

    @Transactional
    public void insert(Result result) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(result);
            transaction.commit();
            lastInserted = result.getId();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }
    }

    public Result fetchLastResult() {
        if (lastInserted == null)
            return null;

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            TypedQuery<Result> q = entityManager
                    .createQuery("select r from results r where id=:id", Result.class)
                    .setParameter("id", lastInserted);
            transaction.commit();
            return q.getSingleResult();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            return null;
        }
    }

    @Transactional
    public List<Result> fetchAllResults() {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            TypedQuery<Result> q = entityManager.createNamedQuery("fetchAllResults", Result.class);
            transaction.commit();
            return q.getResultList();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            return null;
        }
    }

    @Transactional
    public void dropAllResults() {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.createNamedQuery("dropAllResults", Result.class);
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }
    }

}
