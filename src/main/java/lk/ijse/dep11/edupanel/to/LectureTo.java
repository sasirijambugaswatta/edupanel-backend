package lk.ijse.dep11.edupanel.to;

import lk.ijse.dep11.edupanel.util.LecturerType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LectureTo implements Serializable {
    Integer id;
    String name;
    String designation;
    String qualification;
    LecturerType type;
    Integer displayOrder;
    String picture;
    String linkedIn;
}
