package gp.developer.api.task_3.service;

import gp.developer.api.task_3.exception.AuthenticateAuthorizationException;
import gp.developer.api.task_3.security.JwtUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {

    public String authenticate ( Map<String, Object> claims) throws AuthenticateAuthorizationException {

        Optional.ofNullable(claims.get("iss")).orElseThrow(() -> new AuthenticateAuthorizationException("There is no parameter \"iss\" in the request"));
        Optional.ofNullable(claims.get("roles")).orElseThrow(() -> new AuthenticateAuthorizationException("There is no parameter \"roles\" in the request"));

        return JwtUtils.generateJwtToken(claims);
    }
}
