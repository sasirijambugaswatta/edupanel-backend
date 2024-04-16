package lk.ijse.dep11.edupanel;


import lk.ijse.dep11.edupanel.repository.RepositoryFactory;
import lk.ijse.dep11.edupanel.repository.SuperRepository;
import lk.ijse.dep11.edupanel.repository.custom.LecturerRepository;

public class MyService {
    void saveLecturer(){
        LecturerRepository repository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryType.LECTURER);
    }
}
