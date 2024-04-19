package lk.ijse.dep11.edupanel.service.custom;

import com.google.cloud.storage.Bucket;
import lk.ijse.dep11.edupanel.WebAppConfig;
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
    void updateLecturerDetailsWithImage() {
    }

    @Test
    void updateLecturerDetailsWithoutImage() {
    }

    @Test
    void deleteLecturer() {
    }

    @Test
    void getLecturerDetails() {
    }

    @Test
    void getLecturers() {
    }
}