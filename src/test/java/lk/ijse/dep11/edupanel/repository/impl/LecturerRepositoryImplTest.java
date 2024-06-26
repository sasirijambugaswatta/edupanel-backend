package lk.ijse.dep11.edupanel.repository.impl;

import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.repository.LectureRepository;
import lk.ijse.dep11.edupanel.util.LecturerType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = WebRootConfig.class)

@SpringBootTest
@Transactional
class LecturerRepositoryImplTest {


    @Autowired
    private LectureRepository repository;

   @PersistenceContext
   private EntityManager entityManager;

    @Test
    void save(){
        Lecturer lecturer = new Lecturer("Sasiri Deshajith", "Senior trainer", "Bsc(Hons) in Engineering", LecturerType.FULL_TIME, 0);
        Lecturer savedLecturer = repository.save(lecturer);
        assertTrue(savedLecturer.getId() > 0);
        savedLecturer = entityManager.find(Lecturer.class, savedLecturer.getId());
        assertNotNull(savedLecturer);
    }

    @Test
    void update() {
        Lecturer lecturer = new Lecturer("Kasun Sampath",
                "Senior Trainer",
                "BSc (Hons) in Computing",
                LecturerType.FULL_TIME,
                0);
        Lecturer savedLecturer = repository.save(lecturer);
        savedLecturer.setName("Nuwan Ramindu");
        savedLecturer.setQualifications("DEP");
        savedLecturer.setType(LecturerType.VISITING);
        repository.save(savedLecturer);

        Lecturer actualLecturer = entityManager.find(Lecturer.class, savedLecturer.getId());
        assertEquals(savedLecturer, actualLecturer);
    }

    @Test
    void deleteById() {
        Lecturer lecturer = new Lecturer("Kasun Sampath",
                "Senior Trainer",
                "BSc (Hons) in Computing",
                LecturerType.FULL_TIME,
                0);
        Lecturer savedLecturer = repository.save(lecturer);
        repository.deleteById(savedLecturer.getId());

        Lecturer dbLecturer = entityManager.find(Lecturer.class, savedLecturer.getId());
        assertNull(dbLecturer);
    }

    @Test
    void existsById() {
        Lecturer lecturer = new Lecturer("Kasun Sampath",
                "Senior Trainer",
                "BSc (Hons) in Computing",
                LecturerType.FULL_TIME,
                0);
        Lecturer savedLecturer = repository.save(lecturer);
        boolean result = repository.existsById(savedLecturer.getId());

        assertTrue(result);
    }

    @Test
    void findById() {
        Lecturer lecturer = new Lecturer("Kasun Sampath",
                "Senior Trainer",
                "BSc (Hons) in Computing",
                LecturerType.FULL_TIME,
                0);
        Lecturer savedLecturer = repository.save(lecturer);
        Optional<Lecturer> optLecturer1 = repository.findById(savedLecturer.getId());
        Optional<Lecturer> optLecturer2 = repository.findById(-2500);

        assertTrue(optLecturer1.isPresent());
        assertTrue(optLecturer2.isEmpty());
    }

    @Test
    void findAll() {
        for (int i = 0; i < 8; i++) {
            Lecturer lecturer = new Lecturer("Kasun Sampath",
                    "Senior Trainer",
                    "BSc (Hons) in Computing",
                    LecturerType.FULL_TIME,
                    0);
            repository.save(lecturer);
        }
        List<Lecturer> lecturerList = repository.findAll();

        assertEquals(8, lecturerList.size());
        lecturerList.forEach(System.out::println);
    }

    @Test
    void count() {
        for (int i = 0; i < 120; i++) {
            Lecturer lecturer = new Lecturer("Kasun Sampath",
                    "Senior Trainer",
                    "BSc (Hons) in Computing",
                    LecturerType.FULL_TIME,
                    0);
            repository.save(lecturer);
        }
        long count = repository.count();

        assertTrue(count > 120);
    }


}