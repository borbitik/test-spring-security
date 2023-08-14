package ne.engeconi.testspringsecurity;

import ne.engeconi.testspringsecurity.configure.RobotLoginConfigurer;
import ne.engeconi.testspringsecurity.provider.RateLimitAuthenticationProvider;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationEventPublisher authenticationEventPublisher) throws Exception {
        http.
                authorizeHttpRequests(authorizeConfig -> {
                    authorizeConfig.requestMatchers("/").permitAll();
                    authorizeConfig.anyRequest().authenticated();
                })
                .formLogin(withDefaults())
                .oauth2Login(oauth2Configurer -> {
                    oauth2Configurer.withObjectPostProcessor(new ObjectPostProcessor<AuthenticationProvider>() {
                        @Override
                        public <O extends AuthenticationProvider> O postProcess(O object) {
                            return (O) new RateLimitAuthenticationProvider(object);
                        }
                    });
                })
                .apply(new RobotLoginConfigurer())
                .password("beep-boop")
                .password("boop-beep");
        return http.build();

    }


    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(User.builder()
                .username("test")
                .password("{noop}test")
                .authorities("ROLE_user")
                .build()
        );
    }


    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> successEventApplicationListener() {
        return event -> System.out.printf(" SUCESS [%s] %s%n", event.getAuthentication().getClass().getSimpleName(), event.getAuthentication().getName());
    }

}
