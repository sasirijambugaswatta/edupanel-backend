package lk.ijse.dep11.edupanel.service;

import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.repository.custom.impl.LectureServiceImpl;

public class ServiceFactory {
    private static ServiceFactory instance;

    public enum ServiceType {
        LECTURER, USER
    }

    private ServiceFactory() {}

    public static ServiceFactory getInstance() {
        return instance == null ? instance = new ServiceFactory() : instance;
    }

    public <T extends SuperService> T getService(ServiceType serviceType) {
        switch (serviceType) {
            case LECTURER:
                return (T) new LectureServiceImpl();
            case USER:
                throw new RuntimeException("Not implemented yet");
            default:
                throw new IllegalArgumentException("Invalid service type");
        }
    }
}
