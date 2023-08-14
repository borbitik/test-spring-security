package ne.engeconi.testspringsecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RobotAuthentification implements Authentication {

    private final List<GrantedAuthority> authorities;
    private final Boolean isAuthenticated;


    private final String password;

    public RobotAuthentification(List<GrantedAuthority> authorities, String password) {
        this.authorities = authorities;
        this.isAuthenticated = password == null;
        this.password = password;
    }


    public static RobotAuthentification unauthenticated(final String password){
        return new RobotAuthentification(Collections.emptyList(),password);
    }

    public static RobotAuthentification authenticated(){
         return new RobotAuthentification(AuthorityUtils.createAuthorityList("ROLE_robot"), null);
    }

    @Override
    public String getName() {
        return "Welcome Mrs Robot 🤖";
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_robot");
    }

    public String getPassword() {
        return password;
    }
    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return getName();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("don't");
    }
}
