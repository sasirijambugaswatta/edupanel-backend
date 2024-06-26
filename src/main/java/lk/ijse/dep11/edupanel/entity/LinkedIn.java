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
@Table(name = "linkedin")
public class LinkedIn implements SuperEntity{
    @Id
    @Column(name = "lecturer_id")
    private Integer lecturerId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "lecturer_id", referencedColumnName = "id")
    Lecturer lecturer;
    @Column(nullable = false, length = 2000)
    String url;

    public LinkedIn(Lecturer lecturer, String url) {
        this.lecturer = lecturer;
        this.url = url;
    }
}
