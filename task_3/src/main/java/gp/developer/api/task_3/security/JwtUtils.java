package gp.developer.api.task_3.security;

import gp.developer.api.task_3.entity.EnumRole;
import gp.developer.api.task_3.exception.AuthenticateAuthorizationException;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class JwtUtils {

    private static String key = System.getenv().get("SECRET_KEY");

    public static String generateJwtToken(Map<String, Object> claimsMap) {

        Claims claims = Jwts.claims().setSubject(String.valueOf(claimsMap.get("sub")));
        claims.put("roles", claimsMap.get("roles"));

        Instant now = Instant.now();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(String.valueOf(claimsMap.get("iss")))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(100, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public List getRoleFromJwtToken (String jwtToken) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody().get("roles", List.class);
    }

    public boolean validateJwtToken(String jwtToken) throws AuthenticateAuthorizationException {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken);
            JwtUtils.validateJwtTokenIss(jwtToken);
            JwtUtils.validateJwtTokenRoles(jwtToken);

        } catch (MalformedJwtException | IllegalArgumentException e) {
            throw new AuthenticateAuthorizationException("JWT token wrong");
        }
        return true;
    }

    public static void validateJwtTokenIss(String jwtToken) throws AuthenticateAuthorizationException {
        String iss = Optional.of(Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody().getIssuer())
                .orElseThrow(() -> new AuthenticateAuthorizationException("JWT token must contain ISS"));
        if (! iss.equals("GP")){
            throw new AuthenticateAuthorizationException("JWT token ISS must be equal GP");
        }
    }

    public static void validateJwtTokenRoles(String jwtToken) throws AuthenticateAuthorizationException {

        List rolesList = Optional.of(Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody().get("roles", List.class))
                .orElseThrow(() -> new AuthenticateAuthorizationException("JWT token must contain Roles"));

        for (Object role:rolesList) {
            try {
                EnumRole.valueOf(role.toString());
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new AuthenticateAuthorizationException("Wrong roles in JWT token");
            }
        }
    }
}
