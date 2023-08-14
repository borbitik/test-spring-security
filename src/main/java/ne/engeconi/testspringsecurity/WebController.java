package ne.engeconi.testspringsecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class WebController {


    @GetMapping("/")
    public String publicPage() {
        return "Hello Test";

    }


    @GetMapping("/private")
    public String privatePage() {
        var authentifaction = SecurityContextHolder.getContext().getAuthentication();
        return "normally this page is private 🤬 but open for you : " + getName(authentifaction);
    }

    private String getName(Authentication authentication) {
        return Optional.of(authentication.getPrincipal())
                .filter(OidcUser.class::isInstance)
                .map(OidcUser.class::cast)
                .map(OidcUser::getFullName)
                .orElseGet(authentication::getName);

    }

}
