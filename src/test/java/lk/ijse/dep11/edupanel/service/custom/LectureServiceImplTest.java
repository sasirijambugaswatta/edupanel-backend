package lk.ijse.dep11.edupanel.service.custom;

import com.google.cloud.storage.Bucket;
import lk.ijse.dep11.edupanel.WebAppConfig;
import lk.ijse.dep11.edupanel.exception.AppException;
import lk.ijse.dep11.edupanel.repository.custom.LectureService;
import lk.ijse.dep11.edupanel.service.ServiceFactory;
import lk.ijse.dep11.edupanel.store.AppStore;
import lk.ijse.dep11.edupanel.to.LectureTo;
import lk.ijse.dep11.edupanel.to.request.LectureReqTO;
import lk.ijse.dep11.edupanel.util.LecturerType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@SpringJUnitWebConfig(classes = {WebAppConfig.class, WebAppConfig.class})
class LectureServiceImplTest {

    private LectureService lectureService;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    @Autowired
    private Bucket bucket;

    @BeforeEach
    void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
        AppStore.setEntityManager(entityManager);
        AppStore.setBucket(bucket);
        lectureService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.LECTURER);
    }

    @AfterEach
    void tearDown() {
        entityManager.close();
    }

    @Test
    void saveLecturer() throws IOException {
        LectureReqTO lectureReqTO = new LectureReqTO("Amith", "Assosiate Lecturer", "Bsc", LecturerType.VISITING, 6, null, "https://linkedin.com/amith");
        LectureTo lectureTo = lectureService.saveLecturer(lectureReqTO);

        assertNotNull(lectureTo.getId());
        assertTrue(lectureTo.getId() > 0);
        assertEquals(lectureReqTO.getName(), lectureTo.getName());
        assertEquals(lectureReqTO.getDesignation(), lectureTo.getDesignation());
        assertEquals(lectureReqTO.getQualifications(), lectureTo.getQualifications());
        assertEquals(lectureReqTO.getDisplayOrder(), lectureTo.getDisplayOrder());
        assertEquals(lectureReqTO.getType(), lectureTo.getType());

        assumingThat(lectureReqTO.getLinkedin() != null, ()-> assertEquals(lectureReqTO.getLinkedin(), lectureTo.getLinkedIn()));
        assumingThat(lectureReqTO.getLinkedin() == null, ()-> assertNull(lectureTo.getLinkedIn()));

        /*if(lectureReqTO.getLinkedin() != null){
            assertEquals(lectureReqTO.getLinkedin(), lectureTo.getLinkedIn());
        }else {
            assertNull(lectureTo.getLinkedIn());
        }*/
    }



    @Test
    void deleteLecturer() throws IOException {
        LectureReqTO lectureReqTO = new LectureReqTO("Amith", "Assosiate Lecturer", "Bsc",
                LecturerType.VISITING, 6, null, "https://linkedin.com/");
        LectureTo lectureTo = lectureService.saveLecturer(lectureReqTO);
        lectureService.deleteLecturer(lectureTo.getId());

        assertThrows(AppException.class, () -> lectureService.getLecturerDetails(lectureTo.getId()));
        assertThrows(AppException.class, () -> lectureService.deleteLecturer(-100));

    }

    @Test
    void getLecturerDetails() throws IOException {
        LectureReqTO lectureReqTO = new LectureReqTO("Amith", "Assosiate Lecturer", "Bsc",
                LecturerType.VISITING, 6, null, "https://linkedin.com/");
        LectureTo lectureTo = lectureService.saveLecturer(lectureReqTO);
        LectureTo lecture = lectureService.getLecturerDetails(lectureTo.getId());

        assertEquals(lectureReqTO.getName(), lecture.getName());


        assertThrows(AppException.class,() -> lectureService.getLecturerDetails(-100));


    }

    @Test
    void getLecturers() throws IOException {
        for (int i = 0; i < 10; i++) {
            LectureReqTO lectureReqTO = new LectureReqTO("Amith", "Assosiate Lecturer", "Bsc",
                    i< 5 ? LecturerType.VISITING: LecturerType.FULL_TIME, 6, null, "https://linkedin.com/");
            lectureService.saveLecturer(lectureReqTO);
        }
        assertTrue(lectureService.getLecturers(null).size() >= 10);
        assertTrue(lectureService.getLecturers(LecturerType.FULL_TIME).size() >= 5);
        assertTrue(lectureService.getLecturers(LecturerType.VISITING).size() >= 5);

    }

    @Test
    void updateLecturerDetails() throws IOException {
        LectureReqTO lectureReqTO = new LectureReqTO("Amith", "Assosiate Lecturer", "Bsc",
                LecturerType.VISITING, 6, null, "https://linkedin.com/");
        LectureTo lectureTo = lectureService.saveLecturer(lectureReqTO);
        lectureTo.setName("Nuwan");
        lectureTo.setLinkedIn(null);

        lectureService.updateLecturerDetails(lectureTo);

        LectureTo lecture = lectureService.getLecturerDetails(lectureTo.getId());
        assertEquals(lectureTo, lecture);


    }
}