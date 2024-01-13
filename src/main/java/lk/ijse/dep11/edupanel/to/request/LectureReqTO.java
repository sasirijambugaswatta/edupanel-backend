package lk.ijse.dep11.edupanel.to.request;

import lk.ijse.dep11.edupanel.util.LecturerType;
import lk.ijse.dep11.edupanel.validation.LecturerImage;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.groups.Default;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class LectureReqTO implements Serializable {
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
    @Null(groups = Create.class, message = "Display Order should be empty")
    @NotNull(groups = Update.class, message = "Display Order can't be empty")
    @PositiveOrZero(groups = Update.class, message = "Invalid display order")
    Integer displayOrder;
    @LecturerImage
    MultipartFile picture;
    @Pattern(regexp = "^http(s)://.+$", message = "Invalid linkedin url")
    String linkedin;

    public interface Create extends Default{}
    public interface Update extends Default{}
}
