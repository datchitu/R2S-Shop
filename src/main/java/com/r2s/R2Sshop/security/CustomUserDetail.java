package com.r2s.R2Sshop.security;

import com.r2s.R2Sshop.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetail implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final String userName;
    private final String password;
    private final Set<GrantedAuthority> authorities; // ROLE_ADMIN
    private final boolean isAccountNonLocked;
    private final boolean isEnabled;

    public CustomUserDetail(User user) {
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName())).collect(Collectors.toSet());

        this.isAccountNonLocked = (user.getStatus() == 0);
        this.isEnabled = (user.getDeleted().equals(false));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
