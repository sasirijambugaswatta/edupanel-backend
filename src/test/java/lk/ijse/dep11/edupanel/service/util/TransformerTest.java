package lk.ijse.dep11.edupanel.service.util;

import lk.ijse.dep11.edupanel.WebAppConfig;
import lk.ijse.dep11.edupanel.WebRootConfig;
import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.to.LectureTo;
import lk.ijse.dep11.edupanel.to.request.LectureReqTO;
import lk.ijse.dep11.edupanel.util.LecturerType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(classes = {WebAppConfig.class, WebRootConfig.class})
class TransformerTest {

    @Autowired
    private Transformer transformer;

    @Test
    void toLecturerTO() {
        Lecturer lecturer = new Lecturer(2, "Nuwan", "Associate Trainer", "BSc in Computing", LecturerType.VISITING, 5);

          lecturer.setLinkedIn(new LinkedIn(lecturer,"https://linkedin.com/nuwan"));
        LectureTo lectureTo = transformer.toLecturerTO(lecturer);

        assertEquals(lecturer.getId(), lectureTo.getId());
        assertEquals(lecturer.getName(), lectureTo.getName());
        assertEquals(lecturer.getDesignation(), lectureTo.getDesignation());
        assertEquals(lecturer.getQualifications(), lectureTo.getQualifications());
        assertEquals(lecturer.getType(), lectureTo.getType());

    }

    @Test
    void fromLecturerTO() {
        LectureTo lecturerTO = new LectureTo(5, "Nuwan", "Senior Trainer", "BSc in Computing", LecturerType.FULL_TIME, 6, null, "https://linked.in/nuwan-kasun");
        Lecturer lecturer = transformer.fromLecturerTO(lecturerTO);

        assertEquals(lecturerTO.getId(), lecturer.getId());
        assertEquals(lecturerTO.getName(), lecturer.getName());
        assertEquals(lecturerTO.getDesignation(), lecturer.getDesignation());
        assertEquals(lecturerTO.getQualifications(), lecturer.getQualifications());
        assertEquals(lecturerTO.getDisplayOrder(), lecturer.getDisplayOrder());
        assertEquals(lecturerTO.getLinkedIn(), lecturer.getLinkedIn().getUrl());
    }

    @Test
    void fromLecturerReqTO() {
        LectureReqTO lecturerReqTO = new LectureReqTO("Thisara", "Senior Trainer", "BSc in Computing", LecturerType.FULL_TIME, 10, null, "http://linkedin.com/thisara");
        Lecturer lecturer = transformer.fromLecturerReqTO(lecturerReqTO);

        assertEquals(lecturerReqTO.getName(), lecturer.getName());
        assertEquals(lecturerReqTO.getDesignation(), lecturer.getDesignation());
        assertEquals(lecturerReqTO.getQualifications(), lecturer.getQualifications());
        assertEquals(lecturerReqTO.getType(), lecturer.getType());
        assertEquals(lecturerReqTO.getDisplayOrder(), lecturer.getDisplayOrder());
        assertEquals(lecturerReqTO.getLinkedin(), lecturer.getLinkedIn().getUrl());
    }
}