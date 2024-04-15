package lk.ijse.dep11.edupanel.repository.impl;

import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.SuperEntity;
import lk.ijse.dep11.edupanel.repository.custom.LecturerRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class LecturerRepositoryImpl implements LecturerRepository {
    private EntityManager em;
    @Override
    public SuperEntity save(SuperEntity entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public void update(SuperEntity entity) {
        em.merge(entity);
    }

    @Override
    public void deleteById(Serializable pk) {
        em.remove(em.find(Lecturer.class, pk));
    }

    @Override
    public Optional<SuperEntity> findById(Serializable pk) {
        return Optional.ofNullable(em.find(Lecturer.class, pk));
    }

    @Override
    public List<SuperEntity> findAll() {
        return em.createQuery("SELECT l From Lecturer l").getResultList();
    }

    @Override
    public long count() {
        return em.createQuery("SELECT count(l) FROM Lecturer l", Long.class).getSingleResult();
    }

    @Override
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
