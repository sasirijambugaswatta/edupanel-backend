package lk.ijse.dep11.edupanel.repository.custom.impl;

import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.repository.CrudRepositoryImpl;
import lk.ijse.dep11.edupanel.repository.custom.LinkedInRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

public class LinkedInRepositoryImpl extends CrudRepositoryImpl<LinkedIn, Lecturer> implements LinkedInRepository{
    @Override
    public void deleteById(Lecturer pk) {
        getEntityManager().remove(getEntityManager().find(LinkedIn.class, pk.getId()));
    }

    @Override
    public Optional<LinkedIn> findById(Lecturer pk) {
        return Optional.ofNullable(getEntityManager().find(LinkedIn.class, pk.getId()));
    }

}
