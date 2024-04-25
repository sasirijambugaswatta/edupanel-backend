package lk.ijse.dep11.edupanel.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "picture")
public class Picture implements SuperEntity {
    @Id
    @Column(name = "lecturer_id")
    private Integer lecturerId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "lecturer_id", referencedColumnName = "id")
    Lecturer lecturer;
    @Column(name = "picture_path", nullable = false, length = 400)
    String picturePath;

    public Picture(Lecturer lecturer, String picturePath) {
        this.lecturer = lecturer;
        this.picturePath = picturePath;
    }
}
