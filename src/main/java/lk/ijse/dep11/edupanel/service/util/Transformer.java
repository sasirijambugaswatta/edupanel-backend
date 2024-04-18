package lk.ijse.dep11.edupanel.service.util;

import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.to.LectureTo;
import lk.ijse.dep11.edupanel.to.request.LectureReqTO;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class Transformer {
    private final ModelMapper modelMapper = new ModelMapper();

    public Transformer() {
        modelMapper.typeMap(LinkedIn.class, String.class).setConverter(ctx -> ctx.getSource().getUrl());
        modelMapper.typeMap(String.class, LinkedIn.class).setConverter(ctx -> new LinkedIn(null, ctx.getSource()));
    }

    Lecturer fromLecturerReqTO(LectureReqTO lecturerReqTO){
        return modelMapper.map(lecturerReqTO, Lecturer.class);
    }

    Lecturer fromLecturerTO(LectureTo lecturerTO){
        Lecturer lecturer = modelMapper.map(lecturerTO, Lecturer.class);
        lecturer.getLinkedIn().setLecturer(lecturer);
        return lecturer;

    }

    LectureTo toLecturerTO(Lecturer lecturer){
        return modelMapper.map(lecturer, LectureTo.class);
    }

    List<LectureTo> toLectureTOList(List<Lecturer> lecturerList){
        return lecturerList.stream().map(this::toLecturerTO).collect(Collectors.toList());
    }
}
