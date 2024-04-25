package lk.ijse.dep11.edupanel.service.custom.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.entity.Picture;
import lk.ijse.dep11.edupanel.exception.AppException;
import lk.ijse.dep11.edupanel.repository.LectureRepository;
import lk.ijse.dep11.edupanel.repository.LinkedInRepository;
import lk.ijse.dep11.edupanel.repository.PictureRepository;
import lk.ijse.dep11.edupanel.service.custom.LecturerService;
import lk.ijse.dep11.edupanel.service.util.Transformer;
import lk.ijse.dep11.edupanel.to.LectureTo;
import lk.ijse.dep11.edupanel.to.request.LectureReqTO;
import lk.ijse.dep11.edupanel.util.LecturerType;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class LectureServiceImpl implements LecturerService {

    private final LectureRepository lectureRepository;
    private final PictureRepository pictureRepository;
    private final LinkedInRepository linkedInRepository;
    private final Transformer transformer;
    private final Bucket bucket;

    public LectureServiceImpl(LectureRepository lectureRepository, PictureRepository pictureRepository, LinkedInRepository linkedInRepository, Transformer transformer, Bucket bucket) {
        this.lectureRepository = lectureRepository;
        this.pictureRepository = pictureRepository;
        this.linkedInRepository = linkedInRepository;
        this.transformer = transformer;
        this.bucket = bucket;
    }

    @Override
    public LectureTo saveLecturer(LectureReqTO lecturerReqTO) {


        Lecturer lecturer = transformer.fromLecturerReqTO(lecturerReqTO);
        lectureRepository.save(lecturer);

        if(lecturerReqTO.getLinkedin() != null) {
            linkedInRepository.save(lecturer.getLinkedIn());
        }

        String signUrl = null;
        if(lecturerReqTO.getPicture() != null) {
            Picture picture = new Picture(lecturer, "/lecturers" + lecturer.getId());
            pictureRepository.save(picture);

            Blob blobRef = null;

            try {
                blobRef = bucket.create(picture.getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
            } catch (IOException e) {
                throw new AppException("Failed to upload picture", e, 500);
            }
            signUrl = (blobRef.signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
        }
        LectureTo lecturerTO = transformer.toLecturerTO(lecturer);
        lecturerTO.setPicture(signUrl);
        return lecturerTO;
    }

    @Override
    public void updateLecturerDetails(LectureReqTO lecturerReqTO) {
        Optional<Lecturer> optLecturer = lectureRepository.findById(lecturerReqTO.getId());
        if (optLecturer.isEmpty()) throw new AppException(404, "No Lecturer Found");
        Lecturer currentLecturer = optLecturer.get();

        Lecturer newLecturer = transformer.fromLecturerReqTO(lecturerReqTO);
        if (lecturerReqTO.getPicture() != null) {
            newLecturer.setPicture(new Picture(newLecturer, "lecturers/" + currentLecturer.getId()));
        }
        if (lecturerReqTO.getLinkedin() != null) {
            newLecturer.setLinkedIn(new LinkedIn(newLecturer, lecturerReqTO.getLinkedin()));
        }

            updateLinkedIn(currentLecturer, newLecturer);

        try {
            if (newLecturer.getPicture() != null && currentLecturer.getPicture() == null) {
                pictureRepository.save(newLecturer.getPicture());
                bucket.create(newLecturer.getPicture().getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
            } else if (newLecturer.getPicture() == null && currentLecturer.getPicture() != null) {
                pictureRepository.deleteById(currentLecturer.getId());
                bucket.get(currentLecturer.getPicture().getPicturePath()).delete();
            } else if (newLecturer.getPicture() != null) {
                pictureRepository.save(newLecturer.getPicture());
                bucket.create(newLecturer.getPicture().getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
            }
        }catch (IOException e){
            throw new AppException("Failed to update picture", e, 500);
        }
        lectureRepository.save(newLecturer);
    }

    @Override
    public void updateLecturerDetails(LectureTo lecturerTO) {
        Optional<Lecturer> optLecturer = lectureRepository.findById(lecturerTO.getId());
        if (optLecturer.isEmpty()) throw new AppException(404, "No Lecturer Found");
        Lecturer currentLecturer = optLecturer.get();

        Lecturer newLecturer = transformer.fromLecturerTO(lecturerTO);
        newLecturer.setPicture(currentLecturer.getPicture());
        updateLinkedIn(currentLecturer, newLecturer);
        lectureRepository.save(newLecturer);
    }

    @Override
    public void deleteLecturer(Integer lecturerId) {
        if (!lectureRepository.existsById(lecturerId)) throw new AppException(404, "No Lecturer Found");

        lectureRepository.deleteById(lecturerId);
    }

    @Override
    public LectureTo getLecturerDetails(Integer lecturerId) {
        Optional<Lecturer> optLecture = lectureRepository.findById(lecturerId);
        if (optLecture.isEmpty()) throw new AppException(404, "No Lecturer Found");
        LectureTo lecturerTO = transformer.toLecturerTO(optLecture.get());
        if(optLecture.get().getPicture() != null) {
            lecturerTO.setPicture(bucket.get(optLecture.get().getPicture().getPicturePath()).signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
        }
        return lecturerTO;
    }

    @Override
    public List<LectureTo> getLecturers(LecturerType type) {
        List<Lecturer> lecturerList;
        if (type == LecturerType.FULL_TIME) {
            lecturerList = lectureRepository.findFullTimeLecturers();
        } else if (type == LecturerType.VISITING) {
            lecturerList = lectureRepository.findVisitingLecturers();
        } else {
            lecturerList = lectureRepository.findAll();
        }
        return lecturerList.stream().map(l -> {
            LectureTo lecturerTO = transformer.toLecturerTO(l);
            if (l.getPicture() != null) {
                lecturerTO.setPicture(bucket.get(l.getPicture().getPicturePath()).signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
            }
            return lecturerTO;
        }).collect(Collectors.toList());
    }

    private void updateLinkedIn(Lecturer currentLecturer, Lecturer newLecturer){
        if (newLecturer.getLinkedIn() != null && currentLecturer.getLinkedIn() == null) {
            linkedInRepository.save(newLecturer.getLinkedIn());
        } else if (newLecturer.getLinkedIn() == null && currentLecturer.getLinkedIn() != null) {
            linkedInRepository.deleteById(currentLecturer.getId());
        } else if (newLecturer.getLinkedIn() != null) {
            linkedInRepository.save(newLecturer.getLinkedIn());
        }
    }
}
