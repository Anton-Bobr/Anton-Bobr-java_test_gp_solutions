package gp.developer.api.task_3.security;


import gp.developer.api.task_3.exception.AuthenticateAuthorizationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JwtTokenFilter extends GenericFilterBean {

    private JwtUtils jwtUtils;

    public JwtTokenFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        String jwtToken = parseJwt((HttpServletRequest) req);

        if (jwtToken == null) {
            ((HttpServletResponse) res).setStatus(HttpServletResponse.SC_UNAUTHORIZED, "For authentication you need JWT token");
            System.err.println("For authentication you need JWT token");
            throw new JwtAuthenticationException("For authentication you need JWT token");
        } else {
            try {
                boolean validate = jwtUtils.validateJwtToken(jwtToken);
                if (validate) {
                    List<String> roles = new ArrayList<String>(jwtUtils.getRoleFromJwtToken(jwtToken));

                    UserDetails user = User.withDefaultPasswordEncoder()
                            .username("user")
                            .password("password")
                            .roles(roles.toArray(new String[0]))
                            .build();

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) req));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    throw new AuthenticateAuthorizationException ("JWT token is expired or invalid");
                }
            }catch (AuthenticateAuthorizationException e) {
                ((HttpServletResponse) res).setStatus(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
                System.err.println(e.getMessage());
                throw new JwtAuthenticationException(e.getMessage());
            }
        }

        filterChain.doFilter(req, res);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
}