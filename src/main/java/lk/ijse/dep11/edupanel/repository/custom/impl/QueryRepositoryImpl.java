package lk.ijse.dep11.edupanel.repository.custom.impl;

import lk.ijse.dep11.edupanel.repository.custom.QueryRepository;

import javax.persistence.EntityManager;

public class QueryRepositoryImpl implements QueryRepository {
    private EntityManager entityManager;
    @Override
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }
}
