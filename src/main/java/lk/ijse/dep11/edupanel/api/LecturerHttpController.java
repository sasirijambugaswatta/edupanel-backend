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
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    public LectureTo createNewLecturer(@ModelAttribute @Validated(LectureReqTO.Create.class) LectureReqTO lecturerReqTo){
        em.getTransaction().begin();
        try {
            Lecturer lecturer = mapper.map(lecturerReqTo, Lecturer.class);
            lecturer.setPicture(null);
            lecturer.setLinkedIn(null);
            em.persist(lecturer);
            LectureTo lecturerTO = mapper.map(lecturer, LectureTo.class);

            if (lecturerReqTo.getLinkedin() != null){
                em.persist(new LinkedIn(lecturer, lecturerReqTo.getLinkedin()));
                lecturerTO.setLinkedIn(lecturerReqTo.getLinkedin());
            }

            if (lecturerReqTo.getPicture() != null){
                Picture picture = new Picture(lecturer, "lectures/" + lecturer.getId());
                em.persist(picture);

                Blob blobRef = bucket.create(picture.getPicturePath(), lecturerReqTo.getPicture().getInputStream(), lecturerReqTo.getPicture().getContentType());
                lecturerTO.setPicture(blobRef.signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
            }

            em.getTransaction().commit();
            return lecturerTO;
        }catch (Throwable t){
            em.getTransaction().rollback();
            throw new RuntimeException(t);
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}",consumes = "multipart/form-data")
    public void updateLecturerDetailsViaMultipart(@PathVariable("lecturer-id") Integer lecturerId,
                                                  @ModelAttribute @Validated(LectureReqTO.Update.class) LectureReqTO lecturerReqTO) {
        Lecturer currentLecturer = em.find(Lecturer.class, lecturerId);
        if (currentLecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        em.getTransaction().begin();
        try {
            Lecturer newLecturer = mapper.map(lecturerReqTO, Lecturer.class);
            newLecturer.setId(lecturerId);
            newLecturer.setPicture(null);
            newLecturer.setLinkedIn(null);

            if (lecturerReqTO.getPicture() != null) {
                newLecturer.setPicture(new Picture(newLecturer, "lecturers/" + lecturerId));
            }
            if (lecturerReqTO.getLinkedin() != null) {
                newLecturer.setLinkedIn(new LinkedIn(newLecturer, lecturerReqTO.getLinkedin()));
            }

            updateLinkedIn(currentLecturer, newLecturer);

            if (newLecturer.getPicture() != null && currentLecturer.getPicture() == null) {
                em.persist(newLecturer.getPicture());
                bucket.create(newLecturer.getPicture().getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
            } else if (newLecturer.getPicture() == null && currentLecturer.getPicture() != null) {
                em.remove(currentLecturer.getPicture());
                bucket.get(currentLecturer.getPicture().getPicturePath()).delete();
            } else if (newLecturer.getPicture() != null) {
                em.merge(newLecturer.getPicture());
                bucket.create(newLecturer.getPicture().getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
            }

            em.merge(newLecturer);
            em.getTransaction().commit();
        } catch (Throwable t) {
            em.getTransaction().rollback();
            throw new RuntimeException(t);
        }
    }

    private void updateLinkedIn(Lecturer currentLecturer, Lecturer newLecturer){
        if (newLecturer.getLinkedIn() != null && currentLecturer.getLinkedIn() == null) {
            em.persist(newLecturer.getLinkedIn());
        } else if (newLecturer.getLinkedIn() == null && currentLecturer.getLinkedIn() != null) {
            em.remove(currentLecturer.getLinkedIn());
        } else if (newLecturer.getLinkedIn() != null) {
            em.merge(newLecturer.getLinkedIn());
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}",consumes = "application/json")
    public void updateLecturerDetailsViaJson(@PathVariable("lecturer-id") Integer lecturerId,
                                             @RequestBody @Validated LectureTo lecturerTO) {
        Lecturer currentLecturer = em.find(Lecturer.class, lecturerId);
        if (currentLecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        em.getTransaction().begin();
        try {
            Lecturer newLecturer = mapper.map(lecturerTO, Lecturer.class);
            newLecturer.setId(lecturerId);
            newLecturer.setPicture(currentLecturer.getPicture());
            newLecturer.setLinkedIn(lecturerTO.getLinkedIn() != null ? new LinkedIn(newLecturer, lecturerTO.getLinkedIn()) : null);

            updateLinkedIn(currentLecturer, newLecturer);

            em.merge(newLecturer);
            em.getTransaction().commit();
        } catch (Throwable t) {
            em.getTransaction().rollback();
            throw new RuntimeException(t);
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(@PathVariable("lecturer-id") Integer lecturerId){
        Lecturer lecturer = em.find(Lecturer.class, lecturerId);
        if(lecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        em.getTransaction().begin();
        try{
            em.remove(lecturer);

            if(lecturer.getPicture() != null){
                bucket.get(lecturer.getPicture().getPicturePath()).delete();
            }

            em.getTransaction().commit();
        }catch (Throwable t){
            em.getTransaction().rollback();
            throw new RuntimeException(t);
        }
    }
    @GetMapping(produces = "application/json")
    public List<LectureTo> getAllLecturer(){
        TypedQuery<Lecturer> query = em.createQuery("SELECT l FROM Lecturer l", Lecturer.class);
        return getLectureToList(query);
    }
    @GetMapping(value = "/{lecturer-id}", produces = "application/json")
    public LectureTo getLecturerDetails(@PathVariable("lecturer-id") Integer lecturerId){
        Lecturer lecturer = em.find(Lecturer.class, lecturerId);
        if (lecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return getLectureTo(lecturer);
    }
    @GetMapping(params = "type=full-time", produces = "application/json")
    public List<LectureTo> getFullTimeLecturers(){
        TypedQuery<Lecturer> query = em.createQuery("SELECT l FROM Lecturer l WHERE l.type = lk.ijse.dep11.edupanel.util.LecturerType.FULL_TIME", Lecturer.class);
        return getLectureToList(query);
    }
    @GetMapping(params = "type=visiting", produces = "application/json")
    public List<LectureTo> getVisitingLecturers(){
        TypedQuery<Lecturer> query = em.createQuery("SELECT l FROM Lecturer l WHERE l.type = lk.ijse.dep11.edupanel.util.LecturerType.VISITING", Lecturer.class);
        return getLectureToList(query);
    }

    private List<LectureTo> getLectureToList(TypedQuery<Lecturer> query) {
        return query.getResultStream().map(lecturerEntity -> {
            LectureTo lectureTo = mapper.map(lecturerEntity, LectureTo.class);
            if(lecturerEntity.getLinkedIn() != null) lectureTo.setLinkedIn(lecturerEntity.getLinkedIn().getUrl());

            if(lecturerEntity.getPicture() != null){
                lectureTo.setPicture(bucket.get(lecturerEntity.getPicture().getPicturePath()).signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
            }
            return lectureTo;
        }).collect(Collectors.toList());
    }

    private LectureTo getLectureTo(Lecturer lectureEntity) {
        LectureTo lecturerTO = mapper.map(lectureEntity, LectureTo.class);
        if (lectureEntity.getLinkedIn() != null) lecturerTO.setLinkedIn(lectureEntity.getLinkedIn().getUrl());
        if (lectureEntity.getPicture() != null) {
            lecturerTO.setPicture(bucket.get(lectureEntity.getPicture().getPicturePath()).signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
        }
        return lecturerTO;
    }
}
