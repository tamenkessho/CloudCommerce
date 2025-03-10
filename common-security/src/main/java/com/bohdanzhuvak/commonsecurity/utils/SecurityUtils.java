package com.bohdanzhuvak.commonsecurity.utils;

import com.bohdanzhuvak.commonsecurity.model.Role;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

  public String getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new AccessDeniedException("User not authenticated");
    }
    return authentication.getName();
  }

  public boolean hasCurrentUserRole(Role role) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new AccessDeniedException("User not authenticated");
    }
    return authentication.getAuthorities().stream()
        .anyMatch(g -> g.getAuthority().equals(role.name()));
  }

  public boolean hasAccessToUserData(String userId) {
    return getCurrentUserId().equals(userId) || hasCurrentUserRole(Role.ROLE_ADMIN);
  }

  @PreAuthorize("hasRole('ADMIN') or authentication.name == #userId")
  public void checkAccessToUserData(String userId) {
  }


}
