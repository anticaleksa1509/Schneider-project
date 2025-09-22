package raf.rs.SportsBooking.security;

import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import raf.rs.SportsBooking.service.TokenService;

@Aspect
@Configuration
public class SecurityAction {

    @Autowired
    TokenService tokenService;

    @Around(value = "@annotation(checkSecurity)", argNames = "joinPoint,checkSecurity")
    public Object checkPermission(ProceedingJoinPoint joinPoint, raf.rs.SportsBooking.annotations.CheckSecurity checkSecurity) throws Throwable {

        String token = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
            if (methodSignature.getParameterNames()[i].equals("authorization")) {
                if (joinPoint.getArgs()[i].toString().startsWith("Bearer ")) {
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                    break;
                }
            }
        }

        if (token == null)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

        Claims claims = tokenService.parseToken(token);
        Boolean permission = claims.get(checkSecurity.value(), Boolean.class);

        if (permission != null && permission)
            return joinPoint.proceed();
        return new ResponseEntity<>("You do not have permission to " +
                "perform this action", HttpStatus.FORBIDDEN);
    }
}
