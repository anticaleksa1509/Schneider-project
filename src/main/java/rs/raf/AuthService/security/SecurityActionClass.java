package rs.raf.AuthService.security;

import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.raf.AuthService.annotations.CheckSecurity;
import rs.raf.AuthService.bootStrap.BootStrapClass;
import rs.raf.AuthService.model.User;
import rs.raf.AuthService.service.TokenService;

import java.util.Arrays;
import java.util.Objects;

@Aspect
@Configuration
public class SecurityActionClass {

    @Autowired
    TokenService tokenService;

    @Around(value = "@annotation(checkSecurity)", argNames = "joinPoint,checkSecurity")
    public Object checkPermission(ProceedingJoinPoint joinPoint,rs.raf.AuthService.annotations.CheckSecurity checkSecurity) throws Throwable{

        String token = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        for(int i = 0; i < methodSignature.getParameterNames().length; i++){
            if(methodSignature.getParameterNames()[i].equals("authorization")){
                if(joinPoint.getArgs()[i].toString().startsWith("Bearer ")){
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                    break;
                }
            }
        }

        if(token == null)
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);

        Claims claims = tokenService.parseToken(token);
        Boolean permission = claims.get(checkSecurity.value(),Boolean.class);

        //UPDATE USER CHECK, ONLY EDITOR AND ADMIN CAN UPDATE EVERYONE
        String role;

        if(checkSecurity.value().equalsIgnoreCase("can_update")){
            role = claims.get("role", String.class);
            if(role.equalsIgnoreCase("Editor") || role.equalsIgnoreCase("Admin")){
                return joinPoint.proceed();
            }else{
                Long id_user = (Long) joinPoint.getArgs()[0];
                Number idUserNum = claims.get("id_user", Number.class);
                Long id_userClaim = idUserNum.longValue();
                if((permission != null && permission) && Objects.equals(id_userClaim, id_user))
                    return joinPoint.proceed();
                return new ResponseEntity<>("You do not have permission to update any other user but yourself!"
                        ,HttpStatus.FORBIDDEN);
            }
        }

        ///OSTALE PROVERE PROVERAVAMO SAMO DA LI IMAJU PERMISIJU, ZA SADA
        if(permission != null && permission)
            return joinPoint.proceed();
        return new ResponseEntity<>("You do not have permission to " +
                "perform this action",HttpStatus.FORBIDDEN);
    }

    @Around(value = "@annotation(checkRoleSecurity)", argNames = "joinPoint,checkRoleSecurity")
    public Object checkPermissionRole(ProceedingJoinPoint joinPoint,rs.raf.AuthService.annotations.CheckRolePermission checkRoleSecurity) throws Throwable {

        String token = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        for(int i = 0; i < methodSignature.getParameterNames().length; i++){
            if(methodSignature.getParameterNames()[i].equals("authorization")){
                if(joinPoint.getArgs()[i].toString().startsWith("Bearer ")){
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                    break;
                }
            }
        }
        if(token == null)
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);

        Claims claims = tokenService.parseToken(token);
        String roleType = claims.get("role", String.class);

        boolean flag = false;

        for(String s : checkRoleSecurity.value()){
            if(s.equalsIgnoreCase(roleType)){
                flag = true;
                break;
            }
        }

        if(flag)
            return joinPoint.proceed();
        return new ResponseEntity<>("You are not authorized to perform this action",
                HttpStatus.FORBIDDEN);

    }
}
