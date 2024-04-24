package lk.ijse.dep11.edupanel.repository.custom.impl;

import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.repository.CrudRepositoryImpl;
import lk.ijse.dep11.edupanel.repository.custom.LinkedInRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class LinkedInRepositoryImpl extends CrudRepositoryImpl<LinkedIn, Integer> implements LinkedInRepository{

}
