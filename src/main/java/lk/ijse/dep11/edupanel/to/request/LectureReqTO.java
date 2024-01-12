package lk.ijse.dep11.edupanel.to.request;

import lk.ijse.dep11.edupanel.util.LecturerType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class LectureReqTO implements Serializable {
    String name;
    String designation;
    String qualifications;
    LecturerType type;
    Integer displayOrder;
    MultipartFile picture;
    String linkedin;
}
