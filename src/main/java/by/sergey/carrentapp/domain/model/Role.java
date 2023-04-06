package by.sergey.carrentapp.domain.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    CLIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}
