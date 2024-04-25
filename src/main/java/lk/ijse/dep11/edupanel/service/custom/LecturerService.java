package lk.ijse.dep11.edupanel.service.custom;

import lk.ijse.dep11.edupanel.service.SuperService;
import lk.ijse.dep11.edupanel.to.LectureTo;
import lk.ijse.dep11.edupanel.to.request.LectureReqTO;
import lk.ijse.dep11.edupanel.util.LecturerType;

import java.util.List;

public interface LecturerService extends SuperService {

    LectureTo saveLecturer(LectureReqTO lecturerReqTO);
    void updateLecturerDetails(LectureReqTO lecturerReqTO);
    void updateLecturerDetails(LectureTo lecturerTO);
    void deleteLecturer(Integer lecturerId);
    LectureTo getLecturerDetails(Integer lecturerId);
    List<LectureTo> getLecturers(LecturerType type);
}
