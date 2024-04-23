package lk.ijse.dep11.edupanel.advice;

import com.google.cloud.storage.Bucket;
import lk.ijse.dep11.edupanel.service.ServiceFactory;
import lk.ijse.dep11.edupanel.store.AppStore;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;

@Aspect
@Component
public class RequestAdvice {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private Bucket bucket;

    @Before("within(lk.ijse.dep11.edupanel.api.*) && @target(org.springframework.web.bind.annotation.RestController)")
    public void interceptHandlerMethod(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {
        AppStore.setBucket(bucket);
        AppStore.setEntityManager(entityManager);

        Field field = joinPoint.getTarget().getClass().getDeclaredField("lectureService");
        field.setAccessible(true);
        field.set(joinPoint.getTarget(), ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.LECTURER));
    }
}
