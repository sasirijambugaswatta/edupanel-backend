package lk.ijse.dep11.edupanel.api;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.entity.Picture;
import lk.ijse.dep11.edupanel.repository.custom.LectureService;
import lk.ijse.dep11.edupanel.service.ServiceFactory;
import lk.ijse.dep11.edupanel.store.AppStore;
import lk.ijse.dep11.edupanel.to.LectureTo;
import lk.ijse.dep11.edupanel.to.request.LectureReqTO;
import lk.ijse.dep11.edupanel.util.LecturerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
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
    private Bucket bucket;

    private LectureService lectureService;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public LectureTo createNewLecturer(@ModelAttribute @Validated(LectureReqTO.Create.class) LectureReqTO lecturerReqTo) throws IOException {
        return lectureService.saveLecturer(lecturerReqTo);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}",consumes = "multipart/form-data")
    public void updateLecturerDetailsViaMultipart(@PathVariable("lecturer-id") Integer lecturerId,
                                                  @ModelAttribute @Validated(LectureReqTO.Update.class) LectureReqTO lecturerReqTO) {
        lecturerReqTO.setId(lecturerId);
        lectureService.updateLecturerDetails(lecturerReqTO);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}",consumes = "application/json")
    public void updateLecturerDetailsViaJson(@PathVariable("lecturer-id") Integer lecturerId,
                                             @RequestBody @Validated LectureTo lecturerTO) {

        lecturerTO.setId(lecturerId);
        lectureService.updateLecturerDetails(lecturerTO);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(@PathVariable("lecturer-id") Integer lecturerId){

        lectureService.deleteLecturer(lecturerId);
    }
    @GetMapping(produces = "application/json")
    public List<LectureTo> getAllLecturer(){

        return lectureService.getLecturers(null);
    }
    @GetMapping(value = "/{lecturer-id}", produces = "application/json")
    public LectureTo getLecturerDetails(@PathVariable("lecturer-id") Integer lecturerId){

        return lectureService.getLecturerDetails(lecturerId);
    }
    @GetMapping(params = "type=full-time", produces = "application/json")
    public List<LectureTo> getFullTimeLecturers(){

        return lectureService.getLecturers(LecturerType.FULL_TIME);
    }
    @GetMapping(params = "type=visiting", produces = "application/json")
    public List<LectureTo> getVisitingLecturers(){

        return lectureService.getLecturers(LecturerType.VISITING);
    }


}
