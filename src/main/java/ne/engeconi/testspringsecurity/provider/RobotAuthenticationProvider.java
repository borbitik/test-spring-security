package ne.engeconi.testspringsecurity.provider;

import ne.engeconi.testspringsecurity.RobotAuthentification;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

public class RobotAuthenticationProvider implements AuthenticationProvider {


    List<String> passwords;

    public RobotAuthenticationProvider(List<String> passwords) {
        this.passwords = passwords;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var authRequest = (RobotAuthentification) authentication;
        var password = authRequest.getPassword();
        if (!passwords.contains(password)) {
            throw new BadCredentialsException("You are not Ms Robot 🤖⛔️");

        }
        return RobotAuthentification.authenticated();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RobotAuthentification.class.isAssignableFrom(authentication);
    }
}
