package lk.ijse.dep11.edupanel.api;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.entity.Picture;
import lk.ijse.dep11.edupanel.to.LectureTo;
import lk.ijse.dep11.edupanel.to.request.LectureReqTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerHttpController {

    @Autowired
    EntityManager em;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Bucket bucket;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public LectureTo createNewLecturer(@ModelAttribute @Validated(LectureReqTO.Create.class) LectureReqTO lectureReqTO) {
        em.getTransaction().begin();
        try{
            Lecturer lecturer = mapper.map(lectureReqTO, Lecturer.class);
            lecturer.setPicture(null);
            lecturer.setLinkedIn(null);
            em.persist(lecturer);
            LectureTo lectureTo = mapper.map(lecturer, LectureTo.class);

            if(lectureReqTO.getLinkedin() != null){
                em.persist(new LinkedIn(lecturer, lectureReqTO.getLinkedin()));
                lectureTo.setLinkedIn(lectureReqTO.getLinkedin());
            }
            if(lectureReqTO.getPicture() != null){
                Picture picture = new Picture(lecturer, "lecturers/" + lecturer.getId());
                em.persist(picture);

                Blob blobRef = bucket.create(picture.getPicturePath(), lectureReqTO.getPicture().getInputStream(), lectureReqTO.getPicture().getContentType());
                lectureTo.setPicture(blobRef.signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());

            }

            em.getTransaction().commit();
            return lectureTo;

        }catch (Throwable t){
            em.getTransaction().rollback();
            throw new RuntimeException();
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}",consumes = "multipart/form-data")
    public void updateLecturerDetailsViaMultipart(@PathVariable("lecturer-id") Integer lecturerId){}

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}",consumes = "application/json")
    public void updateLecturerDetailsViaJson(@PathVariable("lecturer-id") Integer lecturerId){}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(@PathVariable("lecturer-id") Integer lecturerId){}
    @GetMapping(produces = "application/json")
    public void getAllLecturer(){}
    @GetMapping(value = "/{lecturer-id}", produces = "application/json")
    public void getLecturerDetails(@PathVariable("lecturer-id") Integer lecturerId){}
    @GetMapping(params = "type=full-time", produces = "application/json")
    public void getFullTimeLecturers(){}
    @GetMapping(params = "type=visiting", produces = "application/json")
    public void getVisitingLecturers(){}
}
