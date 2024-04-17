package lk.ijse.dep11.edupanel.repository.custom.impl;

import lk.ijse.dep11.edupanel.repository.RepositoryFactory;
import lk.ijse.dep11.edupanel.repository.custom.LectureService;
import lk.ijse.dep11.edupanel.repository.custom.LecturerRepository;
import lk.ijse.dep11.edupanel.repository.custom.LinkedInRepository;
import lk.ijse.dep11.edupanel.repository.custom.PictureRepository;
import lk.ijse.dep11.edupanel.to.LectureTo;
import lk.ijse.dep11.edupanel.to.request.LectureReqTO;
import lk.ijse.dep11.edupanel.util.LecturerType;

import javax.persistence.EntityManager;
import java.util.List;

public class LectureServiceImpl implements LectureService {

    private final LecturerRepository lecturerRepository =  RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryType.LECTURER);
    private final LinkedInRepository linkedInRepository =  RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryType.LINKEDIN);
    private final PictureRepository pictureRepository =  RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryType.PICTURE);


    @Override
    public LectureTo saveLecturer(LectureReqTO lecturerReqTO) {
        return null;
    }

    @Override
    public void updateLecturerDetailsWithImage(LectureReqTO lecturerReqTO) {

    }

    @Override
    public void updateLecturerDetailsWithoutImage(LectureTo lecturerTO) {

    }

    @Override
    public void deleteLecturer(Integer lecturerId) {

    }

    @Override
    public LectureTo getLecturerDetails(Integer lecturerId) {
        return null;
    }

    @Override
    public List<LectureTo> getLecturers(LecturerType type) {
        return List.of();
    }
}
