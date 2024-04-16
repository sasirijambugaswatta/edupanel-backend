package lk.ijse.dep11.edupanel.repository.impl;

import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.entity.SuperEntity;
import lk.ijse.dep11.edupanel.repository.custom.LinkedInRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class LinkedInRepositoryImpl implements LinkedInRepository {


    @Override
    public LinkedIn save(LinkedIn entity) {
        return null;
    }

    @Override
    public void update(LinkedIn entity) {

    }

    @Override
    public void deleteById(Lecturer pk) {

    }

    @Override
    public Optional<LinkedIn> findById(Lecturer pk) {
        return Optional.empty();
    }

    @Override
    public List<LinkedIn> findAll() {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void setEntityManager(EntityManager em) {

    }
}
