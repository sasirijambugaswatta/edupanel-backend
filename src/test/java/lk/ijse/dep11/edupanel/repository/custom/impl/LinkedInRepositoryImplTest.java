package lk.ijse.dep11.edupanel.repository.custom.impl;

import lk.ijse.dep11.edupanel.WebRootConfig;
import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.repository.RepositoryFactory;
import lk.ijse.dep11.edupanel.repository.custom.LinkedInRepository;
import lk.ijse.dep11.edupanel.util.LecturerType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(classes = {WebRootConfig.class})

class LinkedInRepositoryImplTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private final LinkedInRepository repository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryType.LINKEDIN);


    @BeforeEach
    void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
        repository.setEntityManager(entityManager);
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    void save(){
        Lecturer lecturer = new Lecturer("Kasun Sampath", "Associate Instructor", "BSc (Hons) in Computing", LecturerType.VISITING, 0);
        entityManager.persist(lecturer);
        LinkedIn linkedIn = new LinkedIn(lecturer, "http://linkedin.com/kasun-sampath");
        repository.save(linkedIn);
        LinkedIn actualLinkedIn = entityManager.find(LinkedIn.class, lecturer.getId());
        assertEquals(linkedIn, actualLinkedIn);
    }

    @Test
    void update() {
        Lecturer lecturer = new Lecturer("Kasun Sampath", "Associate Instructor", "BSc (Hons) in Computing", LecturerType.VISITING, 0);
        entityManager.persist(lecturer);
        LinkedIn linkedIn = new LinkedIn(lecturer, "http://linkedin.com/kasun-sampath");
        repository.save(linkedIn);
        linkedIn.setUrl("https://linkedin.com/kasun-sampath");
        repository.update(linkedIn);

        LinkedIn dbLinkedIn = entityManager.find(LinkedIn.class, lecturer.getId());
        assertEquals(linkedIn, dbLinkedIn);
    }

    @Test
    void deleteById() {
        Lecturer lecturer = new Lecturer("Kasun Sampath",
                "Associate Instructor",
                "BSc (Hons) in Computing",
                LecturerType.VISITING, 0);
        entityManager.persist(lecturer);
        LinkedIn linkedIn = new LinkedIn(lecturer, "http://linkedin.com/kasun-sampath");
        repository.save(linkedIn);
        repository.deleteById(lecturer.getId());

        LinkedIn dbLinkedIn = entityManager.find(LinkedIn.class, lecturer.getId());
        assertNull(dbLinkedIn);
    }

    @Test
    void existsById() {
        Lecturer lecturer = new Lecturer("Kasun Sampath", "Associate Instructor", "BSc (Hons) in Computing", LecturerType.VISITING, 0);
        entityManager.persist(lecturer);
        LinkedIn linkedIn = new LinkedIn(lecturer, "http://linkedin.com/kasun-sampath");
        repository.save(linkedIn);
        boolean result = repository.existsById(lecturer.getId());
        boolean result2 = repository.existsById(-500);

        assertTrue(result);
        assertFalse(result2);
    }
}