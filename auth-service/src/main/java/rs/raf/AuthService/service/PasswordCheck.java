package rs.raf.AuthService.service;

import org.springframework.stereotype.Service;

@Service
public class PasswordCheck {

    public boolean checkPasswordNumber(String password){

        boolean hasDigit = false;

        for(char c : password.toCharArray()){

            if(Character.isDigit(c)) {
                hasDigit = true;
                break;
            }
        }
        return hasDigit;
    }
    public boolean checkPasswordLength(String password){
        boolean flag = false;
        if(password.length() >= 6 && password.length() <= 16)
            flag = true;
        return flag;
    }
}
