package lk.ijse.dep11.edupanel.service.util;

import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.entity.Picture;
import lk.ijse.dep11.edupanel.to.LectureTo;
import lk.ijse.dep11.edupanel.to.request.LectureReqTO;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

public class Transformer {
    private final ModelMapper modelMapper = new ModelMapper();

    public Transformer() {
        modelMapper.typeMap(LinkedIn.class, String.class).setConverter(ctx ->ctx.getSource() != null ? ctx.getSource().getUrl() : null);
        modelMapper.typeMap(String.class, LinkedIn.class).setConverter(ctx ->ctx.getSource() != null ? new LinkedIn(null, ctx.getSource()) : null);
        modelMapper.typeMap(MultipartFile.class, Picture.class).setConverter(ctx -> null);
    }

    public Lecturer fromLecturerReqTO(LectureReqTO lecturerReqTO){
        Lecturer lecturer = modelMapper.map(lecturerReqTO, Lecturer.class);
        if (lecturerReqTO.getLinkedin() != null) lecturer.getLinkedIn().setLecturer(lecturer);

//        if (lecturerReqTO.getPicture() == null || lecturerReqTO.getPicture().isEmpty())
//            lecturer.setPicture(null);

        return lecturer;
    }

    public Lecturer fromLecturerTO(LectureTo lecturerTO){
        Lecturer lecturer = modelMapper.map(lecturerTO, Lecturer.class);
        if (lecturerTO.getLinkedIn() != null) lecturer.getLinkedIn().setLecturer(lecturer);
        return lecturer;

    }

    public LectureTo toLecturerTO(Lecturer lecturer){
        return modelMapper.map(lecturer, LectureTo.class);
    }

    public List<LectureTo> toLectureTOList(List<Lecturer> lecturerList){
        return lecturerList.stream().map(this::toLecturerTO).collect(Collectors.toList());
    }
}
