package ne.engeconi.testspringsecurity.configure;

import ne.engeconi.testspringsecurity.filters.RobotFilter;
import ne.engeconi.testspringsecurity.provider.RobotAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

public class RobotLoginConfigurer extends AbstractHttpConfigurer<RobotLoginConfigurer, HttpSecurity> {

    private final List<String> passwords = new ArrayList<>();

    @Override
    public void init(HttpSecurity http) throws Exception {
        //init the providers
        http.authenticationProvider(new RobotAuthenticationProvider(passwords));

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        var authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilterBefore(new RobotFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
    }

    public RobotLoginConfigurer password(final String password) {
        passwords.add(password);
        return this;
    }
}
