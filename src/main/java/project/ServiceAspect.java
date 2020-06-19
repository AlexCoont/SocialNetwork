package project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceAspect {

    private static Logger logger = LogManager.getLogger("default");

    @Before(value = "execution(* (@org.springframework.stereotype.Service *).*(..))")
    public void entering(JoinPoint joinPoint){

        String user = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null){
            user = SecurityContextHolder.getContext().getAuthentication().getName();
        }
        logger.info("Osoba: " + user + " wszedł v: " + joinPoint.getStaticPart().getSignature());
    }

    @AfterThrowing(pointcut = "execution(* (@org.springframework.stereotype.Service *).*(..))", throwing = "ex")
    public void logRuntimeException(RuntimeException ex){
        logger.error("Och kurva! To nie jest zgodne z planem. Zastrzeżenie! -  " +  ex.toString() + " Message: " + ex.getLocalizedMessage());
    }
}