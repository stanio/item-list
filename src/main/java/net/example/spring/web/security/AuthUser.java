package net.example.spring.web.security;

import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUser {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getName() {
        Authentication auth = getAuthentication();
        if (auth == null) {
            throw new IllegalStateException("No authenticated user");
        }
        return auth.getName();
    }

    /**
     * {@code hasRole(ROLE_ADMIN)}
     */
    public static boolean isAdmin() {
        return hasRole(ROLE_ADMIN);
    }

    public static boolean hasRole(String role) {
        Objects.requireNonNull(role, "null role");

        Authentication auth = getAuthentication();
        if (auth == null) {
            return false;
        }
        return auth.getAuthorities().stream()
                   .anyMatch(a -> role.equals(a.getAuthority()));
    }

}
