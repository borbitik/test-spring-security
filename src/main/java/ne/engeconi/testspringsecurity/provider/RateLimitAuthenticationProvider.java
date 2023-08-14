package ne.engeconi.testspringsecurity.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitAuthenticationProvider implements AuthenticationProvider {

    private AuthenticationProvider delegate;

    private final Map<String, Instant> cache = new ConcurrentHashMap<>();


    public RateLimitAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.delegate = authenticationProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var auth = delegate.authenticate(authentication);
        if(Objects.isNull(auth)){
            return null;
        }
        if (updateCache(auth)) {
            return auth;
        }

        throw new BadCredentialsException("Not SO FAST MAN 🙅🏿");
    }

    private boolean updateCache(Authentication auth) {
        Instant now = Instant.now();
        Instant previous = cache.get(auth.getName());
        cache.put(auth.getName(),now);
        return previous == null || previous.plus(Duration.ofMinutes(1)).isBefore(now);
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return delegate.supports(authentication);
    }
}
