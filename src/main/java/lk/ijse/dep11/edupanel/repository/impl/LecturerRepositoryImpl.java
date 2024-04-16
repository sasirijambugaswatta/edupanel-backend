package lk.ijse.dep11.edupanel.repository.impl;

import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.repository.custom.LecturerRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class LecturerRepositoryImpl implements LecturerRepository {
    private EntityManager em;
    @Override
    public Lecturer save(Lecturer entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public void update(Lecturer entity) {
        em.merge(entity);
    }

    @Override
    public void deleteById(Integer pk) {
        em.remove(em.find(Lecturer.class, pk));
    }

    @Override
    public boolean existsById(Integer pk) {
        return findById(pk).isPresent();
    }


    @Override
    public Optional<Lecturer> findById(Integer pk) {
        return Optional.ofNullable(em.find(Lecturer.class, pk));
    }

    @Override
    public List<Lecturer> findAll() {
        return em.createQuery("SELECT l From Lecturer l",Lecturer.class).getResultList();
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
