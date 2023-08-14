package ne.engeconi.testspringsecurity.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ne.engeconi.testspringsecurity.RobotAuthentification;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class RobotFilter extends OncePerRequestFilter {

    public static final String X_ROBOT_PASSWORD = "x-robot-password";

    private final AuthenticationManager authenticationManager;

    public RobotFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //0 check if there is a need to get the robot
        if (!Collections.list(request.getHeaderNames()).contains(X_ROBOT_PASSWORD)) {
            filterChain.doFilter(request, response);
            return;
        }
        //1. authentification

        try {
            var password = request.getHeader(X_ROBOT_PASSWORD);
            var autRequest = RobotAuthentification.unauthenticated(password);
            var authenticated = authenticationManager.authenticate(autRequest);
            //ok 👍🏿
            var context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticated);
            SecurityContextHolder.setContext(context);
            filterChain.doFilter(request, response);
            return;
        } catch (AuthenticationException e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-type", "text/plain;charset=utf-8");
            response.getWriter().print(e.getMessage());
        }
    }
}
