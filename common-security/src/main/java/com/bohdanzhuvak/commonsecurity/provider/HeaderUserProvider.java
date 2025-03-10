package com.bohdanzhuvak.commonsecurity.provider;

import com.bohdanzhuvak.commonsecurity.dto.UserHeaderContext;
import com.bohdanzhuvak.commonsecurity.model.Role;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HeaderUserProvider implements UserProvider {

  @Override
  public UserDetails loadUser(UserHeaderContext userHeaderContext) {
    Set<Role> roles = Arrays.stream(userHeaderContext.rolesHeader().split(","))
        .map(String::trim)
        .map(role -> {
          try {
            return Role.valueOf(role);
          } catch (IllegalArgumentException e) {
            log.warn("Invalid role received: {}", role);
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    Collection<? extends GrantedAuthority> authorities = roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toList());
    return new User(userHeaderContext.userId(), "", authorities);
  }
}
