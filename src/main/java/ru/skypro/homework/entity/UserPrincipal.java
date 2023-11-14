package ru.skypro.homework.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.UserPrincipalDto;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private final UserPrincipalDto userPrincipalDto;

    public UserPrincipal(final UserPrincipalDto userPrincipalDto) {
        this.userPrincipalDto = userPrincipalDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userPrincipalDto.getRole());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return userPrincipalDto.getPassword();
    }

    @Override
    public String getUsername() {
        return userPrincipalDto.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
