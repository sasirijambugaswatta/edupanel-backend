package lk.ijse.dep11.edupanel.repository.custom;

import lk.ijse.dep11.edupanel.service.SuperService;
import lk.ijse.dep11.edupanel.to.LectureTo;
import lk.ijse.dep11.edupanel.to.request.LectureReqTO;
import lk.ijse.dep11.edupanel.util.LecturerType;

import java.io.IOException;
import java.util.List;

public interface LectureService extends SuperService {
    LectureTo saveLecturer(LectureReqTO lecturerReqTO) throws IOException;

    void updateLecturerDetailsWithImage(LectureReqTO lecturerReqTO);

    void updateLecturerDetailsWithoutImage(LectureTo lecturerTO);

    void deleteLecturer(Integer lecturerId);

    LectureTo getLecturerDetails(Integer lecturerId);

    List<LectureTo> getLecturers(LecturerType type);
}
