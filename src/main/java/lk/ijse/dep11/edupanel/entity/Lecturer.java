package lk.ijse.dep11.edupanel.entity;

import lk.ijse.dep11.edupanel.util.LecturerType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "lecturer")
public class Lecturer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(nullable = false, length = 300)
    String name;
    @Column(nullable = false, length = 600)
    String designation;
    @Column(nullable = false, length = 600)
    String qualification;
    @Column(nullable = false, columnDefinition = "ENUM('FULL_TIME', 'VISITING')")
    @Enumerated(EnumType.STRING)
    LecturerType type;
    @Column(name = "display_order", nullable = false)
    int displayOrder;

    @ToString.Exclude
    @OneToOne(mappedBy = "lecturer")
    Picture picture;

    @ToString.Exclude
    @OneToOne(mappedBy = "lecturer")
    LinkedIn linkedIn;

    public Lecturer(String name, String designation, String qualification, LecturerType type, int displayOrder) {
        this.name = name;
        this.designation = designation;
        this.qualification = qualification;
        this.type = type;
        this.displayOrder = displayOrder;
    }

    public Lecturer(int id, String name, String designation, String qualification, LecturerType type, int displayOrder) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.qualification = qualification;
        this.type = type;
        this.displayOrder = displayOrder;
    }

    public void setPicture(Picture picture) {
        if(picture != null) picture.setLecturer(this);
    }

    public void setLinkedIn(LinkedIn linkedIn) {
        if(linkedIn != null) linkedIn.setLecturer(this);
    }

}
