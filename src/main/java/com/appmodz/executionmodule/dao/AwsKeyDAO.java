package com.appmodz.executionmodule.dao;

import com.appmodz.executionmodule.dto.SearchRequestDTO;
import com.appmodz.executionmodule.dto.SearchResultDTO;
import com.appmodz.executionmodule.model.AwsKey;
import com.appmodz.executionmodule.model.Stack;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class AwsKeyDAO {
    private final
    SessionFactory sessionFactory;

    @Autowired
    public AwsKeyDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void save(AwsKey awsKey) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(awsKey);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public AwsKey get(long awsKeyId) {
        Query query = this.sessionFactory
                .getCurrentSession()
                .createQuery("from AwsKey where awsKeyId = :awsKeyId")
                .setParameter("awsKeyId",awsKeyId);
        query.setMaxResults(1);
        return (AwsKey) query.uniqueResult();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public AwsKey getByStackId(long stackId) {
        Query query = this.sessionFactory
                .getCurrentSession()
                .createQuery("select ak from AwsKey ak join ak.stacks aks where aks.stackId = :stackId")
                .setParameter("stackId",stackId);
        query.setMaxResults(1);
        return (AwsKey) query.uniqueResult();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(AwsKey awsKey) {
        this.sessionFactory
                .getCurrentSession().delete(awsKey);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List getAll() {
        Query query = this.sessionFactory
                .getCurrentSession()
                .createQuery("from AwsKey");
        return query.list();
    }


}
