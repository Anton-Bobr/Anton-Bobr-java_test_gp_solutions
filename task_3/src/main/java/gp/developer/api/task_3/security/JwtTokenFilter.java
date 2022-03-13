package gp.developer.api.task_3.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gp.developer.api.task_3.exception.AuthenticateAuthorizationException;
import gp.developer.api.task_3.exception.DeveloperNotFoundException;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException{

        String jwtToken = parseJwt(request);

        if (jwtToken == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            try {
                if (jwtUtils.validateJwtToken(jwtToken)) {
                    List <String> roles = new ArrayList<String>(jwtUtils.getRoleFromJwtToken(jwtToken));

                    UserDetails user = User.withDefaultPasswordEncoder()
                            .username("user")
                            .password("password")
                            .roles(roles.toArray(new String[0]))
                            .build();

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }catch (AuthenticateAuthorizationException e) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                System.err.println(e);
            }
        }

//        String jwtToken = null;
//        try {
//            jwtToken = parseJwt(request).orElseThrow(() -> new AuthenticateAuthorizationException("You need JWT token for authorization"));
//        } catch (AuthenticateAuthorizationException e) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            System.err.println(e);
//        }

//        try {
//            if (jwtUtils.validateJwtToken(jwtToken)) {
//                List <String> roles = new ArrayList<String>(jwtUtils.getRoleFromJwtToken(jwtToken));
//
//                UserDetails user = User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("password")
//                        .roles(roles.toArray(new String[0]))
//                        .build();
//
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//        }catch (AuthenticateAuthorizationException e) {
//            response.setStatus(HttpStatus.FORBIDDEN.value());
//            System.err.println(e);
//        }

        filterChain.doFilter(request, response);
    }


    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

//    private Optional<String> parseJwt(HttpServletRequest request) {
//        String headerAuth = request.getHeader("Authorization");
//        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
//            return Optional.of(headerAuth.substring(7, headerAuth.length()));
//        }
//        return Optional.empty();
//    }

}
