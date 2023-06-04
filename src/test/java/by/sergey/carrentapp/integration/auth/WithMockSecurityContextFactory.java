package by.sergey.carrentapp.integration.auth;

import by.sergey.carrentapp.domain.UserDetailsImpl;
import liquibase.pro.packaged.S;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.testcontainers.shaded.org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WithMockSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {

        final List<GrantedAuthority> authorities;

        if (ObjectUtils.isEmpty(withMockCustomUser.authorities())) {
            authorities = Collections.emptyList();
        } else {
            authorities = Arrays.stream(withMockCustomUser.authorities())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        User testUser = new UserDetailsImpl(withMockCustomUser.email(),
                withMockCustomUser.password(),
                authorities,
                withMockCustomUser.id(),
                withMockCustomUser.username());
        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(testUser, testUser.getPassword(), authorities);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(securityContext);
        return securityContext;
    }
}
