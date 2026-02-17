package io.tharka.samvada.user.model;

import io.tharka.samvada.user.entity.User;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;
    public UserPrincipal(User user) {
        this.user = user;
        this.authorities = this.user.getRoles().stream()
                .map(role-> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

    }



    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    @NonNull
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {return user.isActive();}

    public String getEmail() { return user.getEmail(); }
}
