package io.tharka.samvada.security;

import io.tharka.samvada.user.User;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private final User user;
    public UserPrincipal(User user) {
        this.user = user;
    }


    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
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

    public String getId() { return user.getId().toHexString(); }

    public String getName() { return user.getName(); }
}
