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
public class Lecturer implements SuperEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(nullable = false, length = 300)
    String name;
    @Column(nullable = false, length = 600)
    String designation;
    @Column(nullable = false, length = 600)
    String qualifications;
    @Column(nullable = false, columnDefinition = "ENUM('FULL_TIME', 'VISITING')")
    @Enumerated(EnumType.STRING)
    LecturerType type;
    @Column(name = "display_order", nullable = false)
    int displayOrder;

    @ToString.Exclude
    @OneToOne(mappedBy = "lecturer",cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    Picture picture;

    @ToString.Exclude
    @OneToOne(mappedBy = "lecturer", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    LinkedIn linkedIn;

    public Lecturer(String name, String designation, String qualifications, LecturerType type, int displayOrder) {
        this.name = name;
        this.designation = designation;
        this.qualifications = qualifications;
        this.type = type;
        this.displayOrder = displayOrder;
    }

    public Lecturer(int id, String name, String designation, String qualifications, LecturerType type, int displayOrder) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.qualifications = qualifications;
        this.type = type;
        this.displayOrder = displayOrder;
    }

    public void setPicture(Picture picture) {
        if(picture != null) picture.setLecturer(this);
        this.picture = picture;
    }

    public void setLinkedIn(LinkedIn linkedIn) {
        if(linkedIn != null) linkedIn.setLecturer(this);
        this.linkedIn= linkedIn;
    }

}
