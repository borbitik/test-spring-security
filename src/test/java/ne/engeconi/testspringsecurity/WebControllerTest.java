package ne.engeconi.testspringsecurity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
@Import({SecurityConfig.class, WebControllerTest.TestConfig.class})
class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithAnonymousUser
    public void itshouldAccessPublicUrlWithAuthentication() throws Exception {
        mockMvc.perform(get("/")).
                andExpect(status().isOk())
                .andExpect(content().string("Hello Test"));
    }


    @Test
    @WithMockUser(username = "unit", password = "unit")
    public void itshouldAccesPrivateUrlByFormLogin() throws Exception {
     mockMvc.perform(get("/private"))
             .andExpect(status().isOk())
             .andExpect(content().string("normally this page is private 🤬 but open for you : unit"));

    }

    @Test
    public void itshouldNotAuthenticated() throws Exception{
        mockMvc.perform(get("/private"))
                .andExpect(status().isFound());
    }

    @Test
    public void itshouldNotLogin() throws Exception{
        mockMvc.perform(formLogin().user("wrong").password("wrong"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    public void itshoudlRedirectPassFormLogin() throws  Exception{
        mockMvc.perform(formLogin().user("test").password("test"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));

    }


    @TestConfiguration
    public static class TestConfig {

        @Bean
        @Primary
        public UserDetailsService userDetailServiceTest() {
            return new InMemoryUserDetailsManager(User.builder()
                    .username("unit")
                    .password("{noop}unit")
                    .authorities("ROLE_user")
                    .build()
            );

        }

    }


}