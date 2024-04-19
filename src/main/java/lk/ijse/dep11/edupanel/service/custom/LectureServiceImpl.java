package lk.ijse.dep11.edupanel.service.custom;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.entity.Picture;
import lk.ijse.dep11.edupanel.repository.RepositoryFactory;
import lk.ijse.dep11.edupanel.repository.custom.LectureService;
import lk.ijse.dep11.edupanel.repository.custom.LecturerRepository;
import lk.ijse.dep11.edupanel.repository.custom.LinkedInRepository;
import lk.ijse.dep11.edupanel.repository.custom.PictureRepository;
import lk.ijse.dep11.edupanel.service.util.Transformer;
import lk.ijse.dep11.edupanel.store.AppStore;
import lk.ijse.dep11.edupanel.to.LectureTo;
import lk.ijse.dep11.edupanel.to.request.LectureReqTO;
import lk.ijse.dep11.edupanel.util.LecturerType;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LectureServiceImpl implements LectureService {

    private final LecturerRepository lecturerRepository =  RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryType.LECTURER);
    private final LinkedInRepository linkedInRepository =  RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryType.LINKEDIN);
    private final PictureRepository pictureRepository =  RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryType.PICTURE);

    private final Transformer transformer = new Transformer();

    public LectureServiceImpl() {
        lecturerRepository.setEntityManager(AppStore.getEntityManager());
        linkedInRepository.setEntityManager(AppStore.getEntityManager());
        pictureRepository.setEntityManager(AppStore.getEntityManager());
    }

    @Override
    public LectureTo saveLecturer(LectureReqTO lecturerReqTO) throws IOException {
        AppStore.getEntityManager().getTransaction().begin();
        try {
            Lecturer lecturer = transformer.fromLecturerReqTO(lecturerReqTO);
            lecturerRepository.save(lecturer);

            if(lecturerReqTO.getLinkedin() != null){
                linkedInRepository.save(lecturer.getLinkedIn());
            }

            String signUrl = null;
            if(lecturerReqTO.getPicture() != null){
                Picture picture = new Picture(lecturer, "/lecturers" + lecturer.getId());
                pictureRepository.save(picture);
                Blob blobRef = AppStore.getBucket().create(picture.getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
                signUrl = (blobRef.signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature())).toString();
            }

            AppStore.getEntityManager().getTransaction().commit();
            LectureTo lecturerTO = transformer.toLecturerTO(lecturer);
            lecturerTO.setPicture(signUrl);
            return lecturerTO;
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }

    @Override
    public void updateLecturerDetailsWithImage(LectureReqTO lecturerReqTO) {
        AppStore.getEntityManager().getTransaction().begin();
        try {

            AppStore.getEntityManager().getTransaction().commit();
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }

    @Override
    public void updateLecturerDetailsWithoutImage(LectureTo lecturerTO) {
        AppStore.getEntityManager().getTransaction().begin();
        try {

            AppStore.getEntityManager().getTransaction().commit();
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }

    @Override
    public void deleteLecturer(Integer lecturerId) {
        AppStore.getEntityManager().getTransaction().begin();
        try {

            AppStore.getEntityManager().getTransaction().commit();
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }

    @Override
    public LectureTo getLecturerDetails(Integer lecturerId) {
        AppStore.getEntityManager().getTransaction().begin();
        try {

            AppStore.getEntityManager().getTransaction().commit();
            return null;
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }

    @Override
    public List<LectureTo> getLecturers(LecturerType type) {
        AppStore.getEntityManager().getTransaction().begin();
        try {

            AppStore.getEntityManager().getTransaction().commit();
            return null;
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }
}
