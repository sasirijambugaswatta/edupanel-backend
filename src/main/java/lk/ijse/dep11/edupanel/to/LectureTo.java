package lk.ijse.dep11.edupanel.to;

import lk.ijse.dep11.edupanel.to.request.LectureReqTO;
import lk.ijse.dep11.edupanel.util.LecturerType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LectureTo implements Serializable {
    @Null(message = "Id should be empty")
    Integer id;
    @NotBlank(message = "Name can't be empty")
    @Pattern(regexp = "^[A-Za-z]{2,}$", message = "Invalid Name")
    String name;
    @NotBlank(message = "Designation can't be empty")
    @Length(min = 3, message = "Invalid Designation")
    String designation;
    @NotBlank(message = "Qualification can't be empty")
    @Length(min = 3, message = "Invalid Qualification")
    String qualifications;
    @NotNull(message = "Type should be either full-time or visiting")
    LecturerType type;
    @NotNull(groups = LectureReqTO.Update.class, message = "Display Order can't be empty")
    @PositiveOrZero(groups = LectureReqTO.Update.class, message = "Invalid display order")
    Integer displayOrder;
    @Null(message = "Picture should be empty ")
    String picture;
    @Pattern(regexp = "^http(s)://.+$", message = "Invalid linkedin url")
    String linkedIn;
}
