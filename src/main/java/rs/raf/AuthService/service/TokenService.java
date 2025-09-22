package rs.raf.AuthService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
public class TokenService {

    @Value("${oauth.jwt.secret}")
    private String jwtSecret;
    public String generateToken(Claims claims){
        if(!claims.containsKey("id_user"))
            throw new IllegalArgumentException("Claims must contain ID user");
        Duration duration = Duration.ofHours(1);
        Date now = new Date(); // Trenutno vreme
        long expirationTime = now.getTime() + duration.toMillis(); // Dodajemo trajanje u milisekundama
        Date expirationDate = new Date(expirationTime);
        return Jwts.builder().
                setClaims(claims).
                setSubject(claims.getId()).
                setIssuedAt(now).
                signWith(SignatureAlgorithm.HS256,jwtSecret).
                setExpiration(expirationDate).
                compact();
    }

    public Claims parseToken(String jwt){
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody();
        }catch (Exception e){
            return null;
        }
        return claims;
    }


}
