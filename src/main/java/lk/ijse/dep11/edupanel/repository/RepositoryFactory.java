package lk.ijse.dep11.edupanel.repository;

import lk.ijse.dep11.edupanel.repository.custom.impl.LecturerRepositoryImpl;
import lk.ijse.dep11.edupanel.repository.custom.impl.LinkedInRepositoryImpl;
import lk.ijse.dep11.edupanel.repository.custom.impl.PictureRepositoryImpl;
import lk.ijse.dep11.edupanel.repository.custom.impl.QueryRepositoryImpl;

public class RepositoryFactory {
    public enum RepositoryType {
        LECTURER, LINKEDIN, PICTURE, QUERY
    }

    private static RepositoryFactory instance;

    private RepositoryFactory() {

    }

    public static RepositoryFactory getInstance() {
        return (instance == null) ? (instance = new RepositoryFactory()) : instance;
    }

    public <T extends SuperRepository> T getRepository(RepositoryType type) {
        switch (type) {
            case LECTURER:
                return (T) new LecturerRepositoryImpl();
            case PICTURE:
                return (T) new PictureRepositoryImpl();
            case QUERY:
                return (T) new QueryRepositoryImpl();
            case LINKEDIN:
                return (T) new LinkedInRepositoryImpl();
            default:
                throw new IllegalArgumentException("Unsupported repository type");
        }
    }

}
