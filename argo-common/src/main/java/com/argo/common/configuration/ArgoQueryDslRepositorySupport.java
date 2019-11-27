package com.argo.common.configuration;


import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public abstract class ArgoQueryDslRepositorySupport extends QuerydslRepositorySupport {

    public ArgoQueryDslRepositorySupport(Class<?> domainClass) {
        super(domainClass);
    }

    @Override
    @PersistenceContext(unitName = "argo")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }
}
